package com.api.domain.models.dbdata;

/**
 * 郵送に関する情報を表すモデルクラスです。
 *
 * <p>
 * 各項目は、郵送に必要なデータを保持します。
 * データベース定義を参照して、項目の詳細仕様を確認してください。
 * </p>
 */
public class MailingData {

	// 郵送料金（円単位）
	private Integer postalFee;

	// 郵送可能な最大重量（グラム単位）
	private Integer postalMaxWeight;

	// GetterおよびSetterメソッド
	public Integer getPostalFee() {
		return postalFee;
	}

	public void setPostalFee(Integer postalFee) {
		this.postalFee = postalFee;
	}

	public Integer getPostalMaxWeight() {
		return postalMaxWeight;
	}

	public void setPostalMaxWeight(Integer postalMaxWeight) {
		this.postalMaxWeight = postalMaxWeight;
	}

}
