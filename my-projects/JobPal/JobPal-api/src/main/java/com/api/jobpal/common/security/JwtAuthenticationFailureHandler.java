package com.api.jobpal.common.security;

import com.api.jobpal.common.base.ResponseMessage;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

/**
 * JWT認証が失敗した際のハンドラ
 */
public class JwtAuthenticationFailureHandler implements AuthenticationFailureHandler {

    ObjectMapper objectMapper;

    public JwtAuthenticationFailureHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest req,
            HttpServletResponse res, AuthenticationException e)
            throws IOException, ServletException {
        res.setCharacterEncoding("UTF-8");
        res.getWriter().write(objectMapper.writeValueAsString(ResponseMessage.FAIL_LOGIN));
        res.setStatus(HttpStatus.UNAUTHORIZED.value());
    }

}
