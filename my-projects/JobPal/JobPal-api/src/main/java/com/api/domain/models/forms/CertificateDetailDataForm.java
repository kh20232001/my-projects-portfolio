package com.api.domain.models.forms;

import io.micrometer.common.lang.NonNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * 証明書発行ダッシュボード詳細の表示に必要な入力値を保持するクラスです。
 *
 * <p>
 * 各項目のデータ仕様は基本設計書を参照してください。
 */
public class CertificateDetailDataForm {

	/**
	 * 証明書ID
	 */
	@NotBlank(message = "{require_check}")
	@Pattern(regexp = "^(0|1|2|3)$")
	private String certificateId;

	/**
	 * 証明書名
	 */
	@NotBlank(message = "{require_check}")
	private String certificateName;

	/**
	 * 枚数
	 */
	@NonNull
	private Integer count;

	// getter
	public String getCertificateId() {
		return certificateId;
	}

	public String getCertificateName() {
		return certificateName;
	}

	public Integer getCount() {
		return count;
	}

	// setter
	public void setCount(Integer count) {
		this.count = count;
	}

	public void setCertificateName(String certificateName) {
		this.certificateName = certificateName;
	}

	public void setCertificateId(String certificateId) {
		this.certificateId = certificateId;
	}

	// Builder
	public static class Builder {
		private String certificateId;
		private String certificateName;
		private Integer count;

		public Builder certificateId(String certificateId) {
			this.certificateId = certificateId;
			return this;
		}

		public Builder certificateName(String certificateName) {
			this.certificateName = certificateName;
			return this;
		}

		public Builder count(Integer count) {
			this.count = count;
			return this;
		}

		public CertificateDetailDataForm build() {
			CertificateDetailDataForm form = new CertificateDetailDataForm();
			form.setCertificateId(this.certificateId);
			form.setCertificateName(this.certificateName);
			form.setCount(this.count);
			return form;
		}
	}
}
