package com.api.jobpal.common.base;

/**
 * メッセージ用パラメータクラス
 */
public class ResponseMessage {
    // 成功
    public static final String SUCCESS = "処理に成功しました";
    // 成功（状態変更）
    public static final String SUCCESS_GRANT = "承認しました";
    public static final String SUCCESS_REJECT = "差戻しました";
    public static final String SUCCESS_CANCEL = "取り下げました";
    public static final String SUCCESS_RECEIPT = "受領しました";
    public static final String SUCCESS_ISSUE = "発行しました";
    public static final String SUCCESS_SEND = "送信しました";
    public static final String SUCCESS_POSTAL = "郵送しました";
    public static final String SUCCESS_FINISH = "完了しました";

    // 共通エラー
    public static final String NO_LOGIN = "ログインされていません";
    public static final String FAIL_LOGIN = "ログインに失敗しました";
    public static final String BAD_REQUEST_MESSAGE = "リクエストパラメータが不足、もしくは正しくありません";
    public static final String FORBIDDEN_MESSAGE = "処理を実行する権限がない、もしくはIPアドレス制限のリソースです";
    public static final String METHOD_NOT_ALLOWED_MESSAGE = "無効とされており使用することができません";
    public static final String INTERNAL_SERVER_ERROR_MESSAGE = "サーバでエラーが発生しています";

    // アプリ独自エラー
    public static final String BAD_AUTHENTICATION_MESSAGE = "メールアドレスもしくはパスワードが違います";
    public static final String STATUS_NOT_ALLOWED_MESSAGE = "処理が許可されないステータスです";
    public static final String API_CONNECTION_ERROR_MESSAGE = "外部API通信に失敗しました";
    public static final String ADD_USER_ERROR_MESSAGE = "そのユーザは既に存在します";

}
