package com.api.jobpal.common.security;

import java.io.IOException;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response,
      AccessDeniedException accessDeniedException) throws IOException {
    // リクエストメソッドが PUT なら 400 エラーを返す
    if ("PUT".equalsIgnoreCase(request.getMethod())) {
      response.sendError(HttpServletResponse.SC_BAD_REQUEST, "権限エラー: 更新へのアクセスが禁止されています");
    } else {
      // それ以外のリクエストはデフォルトの 403 Forbidden を返す
      response.sendError(HttpServletResponse.SC_FORBIDDEN, "権限エラー: 削除へのアクセスが禁止されています");
    }
  }
}
