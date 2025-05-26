package com.api.domain.models.displaydata;

/**
 * CSVに出力する値を保持するクラスです。
 *
 * <p>
 * 各項目のデータ仕様は基本設計書を参照してください。
 * </p>
 */
public class CertificateCsvData {

	/**
	 * 証明書発行ID
	 */
	private String certificateIssueId;

	// Getter
	public String getCertificateIssueId() {
		return certificateIssueId;
	}

	// Setter
	public void setCertificateIssueId(String certificateIssueId) {
		this.certificateIssueId = certificateIssueId;
	}

	@Override
	public String toString() {
		return "CertificateCsvData{" +
				"certificateIssueId='" + certificateIssueId + '\'' +
				'}';
	}
}
