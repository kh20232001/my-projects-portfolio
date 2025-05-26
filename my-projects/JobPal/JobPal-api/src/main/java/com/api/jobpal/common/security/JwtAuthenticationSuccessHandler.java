package com.api.jobpal.common.security;

import java.util.Date;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.api.domain.models.dbdata.UserData;
import com.api.domain.repositories.UserRepository;
import com.api.jobpal.common.base.WebConfig;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * JWT認証が成功した際のハンドラ
 */
public class JwtAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    Logger logger;

    private final Algorithm algorithm;

    private final UserRepository userRepository;

    public JwtAuthenticationSuccessHandler(Algorithm algorithm, UserRepository userRepository) {
        this.algorithm = algorithm;
        this.userRepository = userRepository;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest req,
            HttpServletResponse res, Authentication auth) {
        if (res.isCommitted()) {
            logger.warn("トークン認証失敗");
            return;
        }

        UserData userData = userRepository.selectTeacherStudentOne(auth.getName(), false);
        String token = generateAccessToken(this.algorithm, userData);

        res.setHeader(WebConfig.AUTHORIZATION_HEADER_NAME, WebConfig.TOKEN_PREFIX + token);
        res.setStatus(HttpStatus.OK.value());

        HttpSession session = req.getSession(false);
        if (session != null) {
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }
    }

    /**
     * アクセストークンのJWTを作成する.
     *
     * @param algorithm 署名に使用する秘密鍵やアルゴリズム
     * @param userData  ユーザのIDと権限を持つオブジェクト
     * @return 引数の情報から作成されたJWT文字列
     */
    public static String generateAccessToken(Algorithm algorithm, UserData userData) {

        JWTCreator.Builder builder = JWT.create();
        builder.withClaim(WebConfig.JWT_USER_ID_CLAIM_NAME, userData.getUserId());
        builder.withClaim(WebConfig.JWT_AUTHORITY_CLAIM_NAME, userData.getUserType());

        // 有効期限の生成
        Date issuedAt = new Date();
        Date expiresAt = new Date(issuedAt.getTime() + WebConfig.EXPIRATION_TIME_FOR_ACCESS);
        builder.withExpiresAt(expiresAt);

        return builder.sign(algorithm);
    }

}
