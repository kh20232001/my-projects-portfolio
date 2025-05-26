package com.api.domain.models.displaydata;

/**
 * 1件分の証明書の発行料金と重量に関するデータです。
 *
 * <p>
 * 各項目のデータ構造については、データベース定義をご覧ください。
 * </p>
 */
public class CertificateFeeData {

	/**
	 * 証明書ID
	 */
	private String certificateId;

	/**
	 * 重量
	 */
	private Integer weight;

	/**
	 * 発行料金
	 */
	private Integer fee;

	// setter
	public String getCertificateId() {
		return certificateId;
	}

	public Integer getWeight() {
		return weight;
	}

	public Integer getFee() {
		return fee;
	}

	// getter
	public void setCertificateId(String certificateId) {
		this.certificateId = certificateId;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public void setFee(Integer fee) {
		this.fee = fee;
	}

	// builder
	public static class Builder {
		private String certificateId;
		private Integer weight;
		private Integer fee;

		public Builder setCertificateId(String certificateId) {
			this.certificateId = certificateId;
			return this;
		}

		public Builder setWeight(Integer weight) {
			this.weight = weight;
			return this;
		}

		public Builder setFee(Integer fee) {
			this.fee = fee;
			return this;
		}

		public CertificateFeeData build() {
			CertificateFeeData certificateFeeData = new CertificateFeeData();
			certificateFeeData.setCertificateId(this.certificateId);
			certificateFeeData.setWeight(this.weight);
			certificateFeeData.setFee(this.fee);
			return certificateFeeData;
		}
	}
}
