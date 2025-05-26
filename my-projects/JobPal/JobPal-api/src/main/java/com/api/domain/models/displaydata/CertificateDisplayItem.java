package com.api.domain.models.displaydata;

/**
 * 1件分の証明書詳細の情報です。
 *
 * <p>
 * 各項目のデータ構造については、データベース定義をご覧ください。
 */
public class CertificateDisplayItem {
	/**
	 * 証明書ID
	 */
	private String certificateId;

	/**
	 * 証明書名
	 */
	private String certificateName;

	/**
	 * 枚数
	 */
	private Integer count;

	// setter
	public void setCertificateId(String certificateId) {
		this.certificateId = certificateId;
	}

	public void setCertificateName(String certificateName) {
		this.certificateName = certificateName;
	}

	public void setCount(int count) {
		this.count = count;
	}

	// getter
	public String getCertificateId() {
		return certificateId;
	}

	public String getCertificateName() {
		return certificateName;
	}

	public int getCount() {
		return count;
	}

	@Override
	public String toString() {
		return "CertificateDetailData [certificateId=" + certificateId + ", certificateName=" + certificateName + ", count="
				+ count + "]";
	}

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

		public CertificateDisplayItem build() {
			return new CertificateDisplayItem(this);
		}
	}

	// プライベートコンストラクタ
	private CertificateDisplayItem(Builder builder) {
		this.certificateId = builder.certificateId;
		this.certificateName = builder.certificateName;
		this.count = builder.count;
	}
}
