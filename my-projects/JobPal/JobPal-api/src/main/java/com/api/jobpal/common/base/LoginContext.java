package com.api.jobpal.common.base;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

public class LoginContext {
  private static final String LOGIN_FLAG = "LOGIN_PROCESS";

  public static void setLoginFlag(boolean isLogin) {
    RequestContextHolder.currentRequestAttributes().setAttribute(LOGIN_FLAG, isLogin, RequestAttributes.SCOPE_REQUEST);
  }

  public static boolean isLogin() {
    Boolean flag = (Boolean) RequestContextHolder.currentRequestAttributes().getAttribute(LOGIN_FLAG,
        RequestAttributes.SCOPE_REQUEST);
    return flag != null && flag;
  }
}
