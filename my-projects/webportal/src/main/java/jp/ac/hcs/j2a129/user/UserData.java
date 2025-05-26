package jp.ac.hcs.j2a129.user;

import lombok.Data;

/**
 * 1件分のユーザ情報です。
 * 
 * <p>各項目のデータ構造については、データベース定義をご覧ください。
 * @author 春田和也
 */
@Data
public class UserData {

	/**
	 * ユーザID（メールアドレス）
	 */
	private String userId;

	/**
	 * パスワード
	 */
	private String password;

	/**
	 * アカウント有効性
	 */
	private boolean enabled;

	/**
	 * ユーザ名
	 */
	private String userName;

	/**
	 * 権限
	 */
	private String role;
}
