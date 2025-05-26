package com.api.jobpal.common.base;

import jakarta.servlet.http.HttpServletResponse;

/**
 * レスポンス生成クラス.
 * Controllerクラスは必ずこのクラスを利用してAPI通信を行う.
 */
public record BaseResponse(

		/** レスポンスコード */
		String responseCode,

		/** メッセージ */
		String message,

		/** データ */
		Object body) {

	/**
	 * レスポンス生成コンストラクタ（返却値が無い場合用）.
	 *
	 * @param responseCode レスポンスコード
	 * @param message      メッセージ
	 */
	public BaseResponse(int responseCode, String message) {
		this(String.valueOf(responseCode), message, null);
	}

	/**
	 * 成功時のレスポンス生成処理.
	 *
	 * @param body    レスポンス本文
	 * @param message メッセージ
	 * @return レスポンス(本クラス)
	 */
	public static BaseResponse success(Object body, String message) {
		return new BaseResponse(String.valueOf(HttpServletResponse.SC_OK), message, body);
	}

	/**
	 * 処理が許可されないステータス時(210)のレスポンス生成処理.
	 *
	 * @return レスポンス(本クラス)
	 */
	public static BaseResponse badAuthentication() {
		return new BaseResponse(ResponseCode.STATUS_NOT_ALLOWED, ResponseMessage.BAD_AUTHENTICATION_MESSAGE);
	}

	/**
	 * 認証失敗時(211)のレスポンス生成処理.
	 *
	 * @return レスポンス(本クラス)
	 */
	public static BaseResponse statusNotAllowed() {
		return new BaseResponse(ResponseCode.BAD_AUTHENTICATION_CODE, ResponseMessage.STATUS_NOT_ALLOWED_MESSAGE);
	}

	/**
	 * API通信に失敗した場合(213)のレスポンス生成処理.
	 *
	 * @return レスポンス(本クラス)
	 */
	public static BaseResponse apiBadConnection() {
		return new BaseResponse(ResponseCode.API_CONNECTION_ERROR, ResponseMessage.API_CONNECTION_ERROR_MESSAGE);
	}

	/**
	 * 失敗時(400)のレスポンス生成処理.
	 *
	 * @return レスポンス(本クラス)
	 */
	public static BaseResponse badRequest() {
		return new BaseResponse(HttpServletResponse.SC_BAD_REQUEST, ResponseMessage.BAD_REQUEST_MESSAGE);
	}

	/**
	 * ユーザ重複エラー(400)のレスポンス生成処理.
	 *
	 * @return レスポンス(本クラス)
	 */
	public static BaseResponse badUserRequest() {
		return new BaseResponse(HttpServletResponse.SC_BAD_REQUEST, ResponseMessage.ADD_USER_ERROR_MESSAGE);
	}

	/**
	 * 失敗時(403)のレスポンス生成処理.
	 *
	 * @return レスポンス(本クラス)
	 */
	public static BaseResponse forbidden() {
		return new BaseResponse(HttpServletResponse.SC_FORBIDDEN, ResponseMessage.FORBIDDEN_MESSAGE);
	}

	/**
	 * 失敗時(405)のレスポンス生成処理.
	 *
	 * @return レスポンス(本クラス)
	 */
	public static BaseResponse methodNotAllowed() {
		return new BaseResponse(HttpServletResponse.SC_METHOD_NOT_ALLOWED, ResponseMessage.METHOD_NOT_ALLOWED_MESSAGE);
	}

	/**
	 * 失敗時(500)のレスポンス生成処理.
	 *
	 * @return レスポンス(本クラス)
	 */
	public static BaseResponse internalServerError(String message) {
		return new BaseResponse(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
				message);
	}

	/**
	 * 失敗時(500)のレスポンス生成処理.
	 *
	 * @return レスポンス(本クラス)
	 */
	public static BaseResponse internalServerError() {
		return new BaseResponse(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
				ResponseMessage.INTERNAL_SERVER_ERROR_MESSAGE);
	}

}

class ResponseCode {

	/**
	 * 処理が許可されないステータス
	 */
	static final int STATUS_NOT_ALLOWED = 210;

	/**
	 * ログインに失敗
	 */
	static final int BAD_AUTHENTICATION_CODE = 211;

	/**
	 * 外部API通信に失敗
	 */
	static final int API_CONNECTION_ERROR = 212;

}
