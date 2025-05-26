package com.api.domain.models.entities;

import java.util.ArrayList;
import java.util.List;

import com.api.domain.models.displaydata.CertificateFeeData;
import com.api.domain.models.displaydata.PostalData;

/**
 * 証明書の料金と重量に関する情報を保持するエンティティクラスです。
 *
 * <p>
 * 各項目のデータ仕様は基本設計書を参照してください。
 * </p>
 */
public class CertificateFeeAndWeightEntity {

	/**
	 * 証明書ごとの料金と重量リスト
	 */
	private List<CertificateFeeData> certificateList;

	/**
	 * 郵送の情報
	 */
	private PostalData postal;

	// コンストラクタ
	public CertificateFeeAndWeightEntity() {
		this.certificateList = new ArrayList<>();
	}

	// getter
	public List<CertificateFeeData> getCertificateList() {
		return certificateList;
	}

	public PostalData getPostal() {
		return postal;
	}

	// setter
	public void setCertificateList(List<CertificateFeeData> certificateList) {
		this.certificateList = certificateList;
	}

	public void setPostal(PostalData postal) {
		this.postal = postal;
	}

	// ビルダークラス
	public static class Builder {
		private List<CertificateFeeData> certificateList = new ArrayList<>();
		private PostalData postal;

		public Builder setCertificateList(List<CertificateFeeData> certificateList) {
			this.certificateList = certificateList;
			return this;
		}

		public Builder setPostal(PostalData postal) {
			this.postal = postal;
			return this;
		}

		public CertificateFeeAndWeightEntity build() {
			CertificateFeeAndWeightEntity entity = new CertificateFeeAndWeightEntity();
			entity.setCertificateList(this.certificateList);
			entity.setPostal(this.postal);
			return entity;
		}
	}
}
