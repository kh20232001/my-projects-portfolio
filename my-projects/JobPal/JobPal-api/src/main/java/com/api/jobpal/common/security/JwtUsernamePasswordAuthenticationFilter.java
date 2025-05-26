package com.api.jobpal.common.security;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.api.domain.repositories.UserRepository;
import com.api.domain.services.LoginAttemptService;
import com.api.jobpal.common.base.LoginContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * RequestBodyからユーザIDとパスワードを取得する{@link UsernamePasswordAuthenticationFilter}の継承クラス
 */
public class JwtUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LoginAttemptService attemptService;

    private final ObjectMapper objectMapper;

    public JwtUsernamePasswordAuthenticationFilter(AuthenticationManager authenticationManager,
            ObjectMapper objectMapper) {
        super(authenticationManager);
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        String userId;
        String password;
        try {
            JsonNode jsonNode = objectMapper.readTree(request.getInputStream());
            userId = jsonNode.get("userId").asText();
            password = jsonNode.get("password").asText();
        } catch (IOException e) {
            throw new AuthenticationServiceException("JSON形式ではありません", e);
        } catch (NullPointerException e) {
            throw new AuthenticationServiceException("パラメータが見つかりません", e);
        }
        if (StringUtils.isAnyEmpty(userId, password)) {
            throw new AuthenticationServiceException("未入力項目があります");
        }
        LoginContext.setLoginFlag(true);

        UsernamePasswordAuthenticationToken authRequest = UsernamePasswordAuthenticationToken.unauthenticated(userId,
                password);
        setDetails(request, authRequest);

        try {
            Authentication authentication = this.getAuthenticationManager().authenticate(authRequest);
            // 認証成功時にログイン試行回数をリセット
            attemptService.resetAttempts(userId);
            return authentication;
        } catch (AuthenticationException e) {
            // パスワード不一致による認証失敗のみ回数を増やす
            if (e instanceof BadCredentialsException) {
                attemptService.loginFailed(userId);
                if (attemptService.isBlocked(userId)) {
                    // ロック状態(1)に変更
                    userRepository.updateUserStateOne(userId, "1");
                    System.out.println("ユーザID: " + userId + " がロックされました");
                }
            }
            throw e;
        }
    }
}
