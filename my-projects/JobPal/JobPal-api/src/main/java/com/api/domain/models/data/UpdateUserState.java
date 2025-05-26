package com.api.domain.models.data;

/**
 * 1件分のユーザ情報です。
 *
 * <p>
 * 各項目のデータ構造については、データベース定義をご覧ください。
 *
 */
public class UpdateUserState {

	// 更新用コンストラクタ
	public UpdateUserState(String userId, String status) {
		this.userId = userId;
		this.status = status;
	}

	/**
	 * ユーザID（メールアドレス）
	 * 主キー、必須入力、メールアドレス形式
	 */
	private String userId;

	/**
	 * アカウント有効性
	 * - VALID：有効
	 * - LOCKED：ロック中
	 * - GRADUATION：卒業
	 * - DELETE：削除
	 * - INVALID：無効
	 *
	 */
	private String status;

	// setter
	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	// getter
	public String getUserId() {
		return userId;
	}

	public String getStatus() {
		return status;
	}

	@Override
	public String toString() {
		return "UserData [userId=" + userId + ", status=" + status + "]";
	}

}
