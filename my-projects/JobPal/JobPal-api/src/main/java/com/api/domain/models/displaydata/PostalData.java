package com.api.domain.models.displaydata;

/**
 * 1件分の郵送データの情報です。
 *
 * <p>
 * 各項目のデータ構造については、データベース定義をご覧ください。
 * </p>
 */
public class PostalData {

	/**
	 * 重量
	 */
	private Integer postalMaxWeight;

	/**
	 * 発行料金
	 */
	private Integer postalFee;

	// getter
	public Integer getPostalMaxWeight() {
		return postalMaxWeight;
	}

	public Integer getPostalFee() {
		return postalFee;
	}

	// setter
	public void setPostalMaxWeight(Integer postalMaxWeight) {
		this.postalMaxWeight = postalMaxWeight;
	}

	public void setPostalFee(Integer postalFee) {
		this.postalFee = postalFee;
	}

	// builder
	public static class Builder {
		private Integer postalMaxWeight;
		private Integer postalFee;

		public Builder setPostalMaxWeight(Integer postalMaxWeight) {
			this.postalMaxWeight = postalMaxWeight;
			return this;
		}

		public Builder setPostalFee(Integer postalFee) {
			this.postalFee = postalFee;
			return this;
		}

		public PostalData build() {
			PostalData postalData = new PostalData();
			postalData.setPostalMaxWeight(this.postalMaxWeight);
			postalData.setPostalFee(this.postalFee);
			return postalData;
		}
	}
}
