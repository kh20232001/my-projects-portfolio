package com.api.domain.models.dbdata;

/**
 * 1件分の証明書情報を表すモデルクラスです。
 *
 * <p>
 * 各項目は証明書に関するデータを保持します。<br>
 * 詳細なデータ構造については、データベース定義を参照してください。
 * </p>
 */
public class CertificateData {

	// デフォルトコンストラクタ
	public CertificateData() {
	}

	// 証明書ID
	private String certificateId;

	// 証明書発行数
	private Integer certificateQuantity;

	// 証明書の名前
	private String certificateName;

	// 証明書発行の料金
	private Integer certificateFee;

	// 証明書の重量
	private Integer certificateWeight;

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

	public String getCertificateName() {
		return certificateName;
	}

	public void setCertificateName(String certificateName) {
		this.certificateName = certificateName;
	}

	public Integer getCertificateFee() {
		return certificateFee;
	}

	public void setCertificateFee(Integer certificateFee) {
		this.certificateFee = certificateFee;
	}

	public Integer getCertificateWeight() {
		return certificateWeight;
	}

	public void setCertificateWeight(Integer certificateWeight) {
		this.certificateWeight = certificateWeight;
	}

	// オブジェクトの文字列表現を返却
	@Override
	public String toString() {
		return "CertificateData [certificateId=" + certificateId +
				", certificateQuantity=" + certificateQuantity +
				", certificateName=" + certificateName +
				", certificateFee=" + certificateFee +
				", certificateWeight=" + certificateWeight + "]";
	}
}
