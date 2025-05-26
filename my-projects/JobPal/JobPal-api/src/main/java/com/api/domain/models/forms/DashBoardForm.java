package com.api.domain.models.forms;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * 就職活動ダッシュボード表示に必要な入力値を保持するクラスです。
 *
 * <p>
 * 各項目のデータ仕様は基本設計書を参照してください。
 */
public class DashBoardForm {

	/**
	 * ユーザID（メールアドレス）
	 *
	 * 空白禁止、メールアドレス形式
	 */
	@NotBlank(message = "{require_check}")
	@Email(message = "{email_check}")
	private String userId;

	/**
	 * 権限
	 *
	 * 空白禁止、0～3のいずれか
	 * 0：一般、1：管理者、2：システム管理者、3: 事務
	 */
	@NotBlank(message = "{require_check}")
	@Pattern(regexp = "^(0|1|2|3)$")
	private String grant;

	// getter
	public String getUserId() {
		return userId;
	}

	public String getGrant() {
		return grant;
	}

	// setter
	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setGrant(String grant) {
		this.grant = grant;
	}
}
