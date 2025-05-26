package com.api.jobpal.common.security;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.filter.GenericFilterBean;

import com.api.domain.models.dbdata.UserData;
import com.api.domain.repositories.UserRepository;
import com.api.domain.validations.UserAuthority;
import com.api.jobpal.common.base.ResponseMessage;
import com.api.jobpal.common.base.WebConfig;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * JWTトークンの処理クラス.
 */
public class JwtTokenFilter extends GenericFilterBean {

    @Autowired
    Logger logger;

    private final ObjectMapper objectMapper;

    private final UserRepository userRepository;

    private final JWTVerifier accessJwtVerifier;

    /**
     * 権限を保持するオブジェクトのキャッシュ
     */
    private static final Map<String, Collection<GrantedAuthority>> authorityMap = Map.of(
            UserAuthority.ADMIN.getGrantId(),
            Collections.singleton(new SimpleGrantedAuthority(UserAuthority.ADMIN.getGrantId())),
            UserAuthority.TEACHER.getGrantId(),
            Collections.singleton(new SimpleGrantedAuthority(UserAuthority.TEACHER.getGrantId())),
            UserAuthority.STUDENT.getGrantId(),
            Collections.singleton(new SimpleGrantedAuthority(UserAuthority.STUDENT.getGrantId())));

    public JwtTokenFilter(JWTVerifier accessJwtVerifier, UserRepository userRepository, ObjectMapper objectMapper) {
        this.userRepository = userRepository;
        this.accessJwtVerifier = accessJwtVerifier;
        this.objectMapper = objectMapper;
    }

    @Override
    public void doFilter(
            ServletRequest req,
            ServletResponse res,
            FilterChain chain)
            throws IOException, ServletException {
        // Get Token
        String token = resolveToken(req);
        if (token == null) {
            chain.doFilter(req, res);
            return;
        }

        // Decode Token
        // ...

        DecodedJWT decodedJWT;
        try {
            decodedJWT = accessJwtVerifier.verify(token);
        } catch (JWTVerificationException | DataAccessException e) {
            logger.warn("認証失敗 IPアドレス: " + req.getLocalAddr());
            SecurityContextHolder.clearContext();

            res.setCharacterEncoding("UTF-8");
            res.getWriter().write(objectMapper.writeValueAsString(ResponseMessage.NO_LOGIN));
            ((HttpServletResponse) res).setStatus(HttpStatus.UNAUTHORIZED.value());

            chain.doFilter(req, res);
            return;
        }

        // Success
        authentication(decodedJWT);
        chain.doFilter(req, res);
    }

    /**
     * トークンを取得する.
     *
     * @param request リクエスト
     * @return トークン(取得できなかった場合はNullを返却)
     */
    private String resolveToken(ServletRequest request) {
        String token = ((HttpServletRequest) request).getHeader(WebConfig.AUTHORIZATION_HEADER_NAME.toLowerCase());
        if (!startWithToTokenPrefix(token))
            return null;

        return removeToTokenPrefix(token);
    }

    /**
     * デコードされたJWTを使用しDBからユーザデータを取得し、Principalを作成する.
     *
     * @param jwt デコードされたJWT
     */
    private void authentication(DecodedJWT jwt) {
        String userId = jwt.getClaim(WebConfig.JWT_USER_ID_CLAIM_NAME).asString();
        UserData userData = userRepository.selectTeacherStudentOne(userId, false);
        Collection<GrantedAuthority> authorities = getAuthorityCollection(userData.getUserType());

        User user = new User(userData.getUserId(), "", authorities);
        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()));
    }

    /**
     * 権限を表すオブジェクトを取得する.
     *
     * @param authorityName 権限名
     * @return 権限を表すオブジェクト
     */
    private Collection<GrantedAuthority> getAuthorityCollection(String Grant) {
        Collection<GrantedAuthority> result = authorityMap.get(Grant);
        if (result == null) {
            result = Collections.singleton(new SimpleGrantedAuthority(Grant));
        }
        return result;
    }

    /**
     * 渡された文字列が{@link WebConfig#TOKEN_PREFIX}から始まる文字列であるかをチェックする.
     *
     * @param token 検査したい文字列
     * @return {@link WebConfig#TOKEN_PREFIX}から始まる文字列であればtrue、それ以外はfalse
     */
    public static boolean startWithToTokenPrefix(String token) {
        return token != null && token.startsWith(WebConfig.TOKEN_PREFIX);
    }

    /**
     * 渡された文字列から{@link WebConfig#TOKEN_PREFIX}を含む前の文字列を削除する.
     *
     * @param str プレフィックスを消したい文字列
     * @return プレフィックス以前を削除した文字列、それ以外は引数をそのまま返す.
     */
    public static String removeToTokenPrefix(String str) {
        return StringUtils.substringAfter(str, WebConfig.TOKEN_PREFIX);
    }

}
