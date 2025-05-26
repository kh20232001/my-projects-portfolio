package com.api.jobpal.common.base;

import java.util.Objects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.api.domain.models.data.UserData;

/**
 * アスペクト指向のログ出力を行うクラス.
 */
@Aspect
@Component
public class LogAspect {

	@Autowired
	Logger logger;

	/**
	 * Controllerの処理開始前後にログを出力する.
	 *
	 * @param jp ProceedingJoinPoint
	 * @return 進行オブジェクト
	 * @throws Throwable Controllerからの例外
	 */
	@Around("@within(org.springframework.web.bind.annotation.RestController) && within(com.api..*)")
	public Object controllerLog(ProceedingJoinPoint jp) throws Throwable {
		UserData user = getAuthenticationUser();
		String url = getPath().toString();
		BaseRequest req = getRequest(jp);

		// START
		if (req == null) {
			logger.info("[" + user.getUserId() + "] START: " + url + " PROCESS: " +
					jp.getSignature());
		} else {
			logger.info("[" + user.getUserId() + "] START: " + url + " PROCESS: " +
					jp.getSignature() + " ARGUMENTS: " +
					String.valueOf(req).replaceAll("\\r\\n|\\r|\\n", "↲"));
		}

		// END
		try {
			Object result = jp.proceed();
			BaseResponse res = getResponse(result);
			logger.info("[" + user.getUserId() + "] END: " + url + " RESULT: " +
					String.valueOf(res).replaceAll("\\r\\n|\\r|\\n", "↲"));
			return result;
		} catch (RuntimeException e) {
			logger.info("[" + user.getUserId() + "] EXCEPTION: " + e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error("[" + user.getUserId() + "] EXCEPTION: " + e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * オブジェクト型からリクエスト型へ変換.
	 *
	 * @param jp ProceedingJoinPoint
	 * @return Requestインターフェース
	 */
	private BaseRequest getRequest(ProceedingJoinPoint jp) {
		for (Object args : jp.getArgs()) {
			if (args instanceof BaseRequest) {
				return (BaseRequest) args;
			}
			if (args instanceof String) {
				String pathVariable = (String) args;
				return new PathVariableBaseRequest(pathVariable);
			}
		}
		return null;
	}

	/**
	 * オブジェクト型からレスポンス型へ変換. <br>
	 * BaseResponse型を返却しないメソッドは、nullが返却される.
	 *
	 * @param obj オブジェクト型
	 * @return BaseResponseクラス
	 */
	private BaseResponse getResponse(Object obj) {
		if (obj instanceof BaseResponse) {
			return (BaseResponse) obj;
		}
		if (obj instanceof ResponseEntity) {
			return null;
		}
		throw new RuntimeException();
	}

	/**
	 * リクエストURLを取得.
	 *
	 * @return URL
	 */
	private StringBuffer getPath() {
		return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes()))
				.getRequest().getRequestURL();
	}

	/**
	 * ログイン中のユーザ情報を取得.<br>
	 * <strong>ユーザ情報が取得できない場合は、Exceptionをスローする.</strong>
	 *
	 * @return ユーザ情報
	 */
	public static UserData getAuthenticationUser() throws Exception {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		UserData userData = new UserData();
		if (principal instanceof User) {
			userData.setUserId(((User) principal).getUsername());
			return userData;
		}

		// 未ログイン中の処理
		userData.setUserId("No Login User");
		return userData;
	}

}

/**
 * PathVariableを利用するメソッドの<strong>ログを表示するため</strong>のPOJOクラス.
 */
class PathVariableBaseRequest implements BaseRequest {
	private String param;

	PathVariableBaseRequest(String param) {
		this.param = param;
	}

	public String getParam() {
		return param;
	}

	@Override
	public String toString() {
		return "PathVariableBaseRequest [param=" + param + "]";
	}

}

interface BaseRequest {
}
