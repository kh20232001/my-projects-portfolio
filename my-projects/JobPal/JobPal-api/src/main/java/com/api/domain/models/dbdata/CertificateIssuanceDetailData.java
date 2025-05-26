package com.api.domain.models.dbdata;

/**
 * 証明書発行の詳細情報を表すモデルクラスです。
 *
 * <p>
 * 各項目は証明書発行に関連するデータを保持します。<br>
 * データベース定義を参照して詳細な仕様を確認してください。
 * </p>
 */
public class CertificateIssuanceDetailData {

	/**
	 * デフォルトコンストラクタ。
	 */
	public CertificateIssuanceDetailData() {
	}

	/**
	 * 証明書ID。
	 */
	private String certificateId;

	/**
	 * 証明書発行数。
	 */
	private Integer certificateQuantity;

	// GetterおよびSetterメソッド

	public String getCertificateId() {
		return certificateId;
	}

	public void setCertificateId(String certificateId) {
		this.certificateId = certificateId;
	}

	public Integer getCertificateQuantity() {
		return certificateQuantity;
	}

	public void setCertificateQuantity(Integer certificateQuantity) {
		this.certificateQuantity = certificateQuantity;
	}

	/**
	 * CertificateIssuanceDetailDataのインスタンスを生成するためのビルダークラスです。
	 */
	public static class Builder {
		private String certificateId;
		private Integer certificateQuantity;

		/**
		 * デフォルトコンストラクタ。
		 */
		public Builder() {
		}

		/**
		 * 証明書IDを設定します。
		 *
		 * @param certificateId 証明書ID
		 * @return ビルダーインスタンス
		 */
		public Builder certificateId(String certificateId) {
			this.certificateId = certificateId;
			return this;
		}

		/**
		 * 証明書発行数を設定します。
		 *
		 * @param certificateQuantity 証明書発行数
		 * @return ビルダーインスタンス
		 */
		public Builder certificateQuantity(Integer certificateQuantity) {
			this.certificateQuantity = certificateQuantity;
			return this;
		}

		/**
		 * CertificateIssuanceDetailDataのインスタンスを生成します。
		 *
		 * @return CertificateIssuanceDetailDataのインスタンス
		 */
		public CertificateIssuanceDetailData build() {
			CertificateIssuanceDetailData data = new CertificateIssuanceDetailData();
			data.setCertificateId(this.certificateId);
			data.setCertificateQuantity(this.certificateQuantity);
			return data;
		}
	}
}
