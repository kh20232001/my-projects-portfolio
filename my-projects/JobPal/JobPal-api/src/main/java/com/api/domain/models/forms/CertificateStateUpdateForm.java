package com.api.domain.models.forms;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 証明書発行状態の変更情報を保持するクラスです。
 *
 * <p>
 * 各項目のデータ仕様は基本設計書を参照してください。
 *
 */
public class CertificateStateUpdateForm {

	/**
	 * 証明書発行ID
	 * 空白禁止
	 */
	@NotBlank(message = "{require_check}")
	private String certificateIssueId;

	/**
	 * 状態名
	 * 空白禁止
	 */
	@NotBlank(message = "{require_check}")
	private String certificateStateName;

	/**
	 * 媒体名
	 * 空白禁止
	 */
	@NotBlank(message = "{require_check}")
	private String mediaName;

	/**
	 * ボタンID（0~7）
	 * 空白禁止
	 * 0：承認、1：取り下げ、2：差戻し、3：受領、4：発行、5：送信、6：郵送、7：完了
	 */
	@NotNull(message = "{require_check}")
	@Min(value = 0, message = "ボタンIDは0以上である必要があります")
	@Max(value = 7, message = "ボタンIDは7以下である必要があります")
	private int buttonId;

	/**
	 * ユーザID（メールアドレス）
	 * <p>
	 * 入力例: user@example.com
	 * </p>
	 */
	@Email(message = "{email_check}")
	private String userId;

	// getter
	public String getCertificateIssueId() {
		return certificateIssueId;
	}

	public String getCertificateStateName() {
		return certificateStateName;
	}

	public int getButtonId() {
		return buttonId;
	}

	public String getMediaName() {
		return mediaName;
	}

	public String getUserId() {
		return userId;
	}

	// setter
	public void setCertificateIssueId(String certificateIssueId) {
		this.certificateIssueId = certificateIssueId;
	}

	public void setCertificateStateName(String certificateStateName) {
		this.certificateStateName = certificateStateName;
	}

	public void setButtonId(int buttonId) {
		this.buttonId = buttonId;
	}

	public void setMediaName(String mediaName) {
		this.mediaName = mediaName;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	// builder
	public static class Builder {
		private String certificateIssueId;
		private String certificateStateName;
		private String mediaName;
		private int buttonId;
		private String userId;

		public Builder certificateIssueId(String certificateIssueId) {
			this.certificateIssueId = certificateIssueId;
			return this;
		}

		public Builder certificateStateName(String certificateStateName) {
			this.certificateStateName = certificateStateName;
			return this;
		}

		public Builder mediaName(String mediaName) {
			this.mediaName = mediaName;
			return this;
		}

		public Builder buttonId(int buttonId) {
			this.buttonId = buttonId;
			return this;
		}

		public Builder userId(String userId) {
			this.userId = userId;
			return this;
		}

		public CertificateStateUpdateForm build() {
			CertificateStateUpdateForm entity = new CertificateStateUpdateForm();
			entity.setCertificateIssueId(this.certificateIssueId);
			entity.setCertificateStateName(this.certificateStateName);
			entity.setMediaName(this.mediaName);
			entity.setButtonId(this.buttonId);
			entity.setUserId(this.userId);
			return entity;
		}
	}

}
