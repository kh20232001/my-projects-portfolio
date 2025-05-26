package com.api.domain.models.entities;

import java.util.List;

import com.api.domain.models.displaydata.CertificateCsvData;
import com.api.domain.models.displaydata.JobSearchData;

/**
 * 削除されたユーザ情報を保持するエンティティクラスです。
 *
 * <p>
 * 本クラスは削除されたユーザに関連する情報（就職活動データや証明書データ）を管理します。
 * </p>
 */
public class UserDeleteEntity {

	/**
	 * ユーザID
	 */
	private String userId;

	/**
	 * ユーザ名
	 */
	private String userName;

	/**
	 * ユーザに関連する就職活動データのリスト
	 */
	private List<JobSearchData> job;

	/**
	 * ユーザに関連する証明書データのリスト
	 */
	private List<CertificateCsvData> certificate;

	// Getters
	public String getUserId() {
		return userId;
	}

	public String getUserName() {
		return userName;
	}

	public List<JobSearchData> getJob() {
		return job;
	}

	public List<CertificateCsvData> getCertificate() {
		return certificate;
	}

	// Setters
	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setJob(List<JobSearchData> job) {
		this.job = job;
	}

	public void setCertificate(List<CertificateCsvData> certificate) {
		this.certificate = certificate;
	}

	// Builderクラス
	public static class Builder {
		private String userId;
		private String userName;
		private List<JobSearchData> job;
		private List<CertificateCsvData> certificate;

		public Builder setUserId(String userId) {
			this.userId = userId;
			return this;
		}

		public Builder setUserName(String userName) {
			this.userName = userName;
			return this;
		}

		public Builder setJob(List<JobSearchData> job) {
			this.job = job;
			return this;
		}

		public Builder setCertificate(List<CertificateCsvData> certificate) {
			this.certificate = certificate;
			return this;
		}

		public UserDeleteEntity build() {
			UserDeleteEntity entity = new UserDeleteEntity();
			entity.setUserId(this.userId);
			entity.setUserName(this.userName);
			entity.setJob(this.job);
			entity.setCertificate(this.certificate);
			return entity;
		}
	}

	@Override
	public String toString() {
		return "UserDeleteEntity{" +
				"userId='" + userId + '\'' +
				", userName='" + userName + '\'' +
				", job=" + job +
				", certificate=" + certificate +
				'}';
	}
}
