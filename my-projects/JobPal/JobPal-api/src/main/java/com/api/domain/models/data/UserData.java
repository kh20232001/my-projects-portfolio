package com.api.domain.models.data;

import java.sql.Timestamp;

/**
 * 1件分のユーザ情報です。
 *
 * <p>
 * 各項目のデータ構造については、データベース定義をご覧ください。
 *
 */
public class UserData {

	// フィールド定義
	private String userId;
	private String password;
	private String userName;
	private String userClass;
	private Integer classNumber;
	private Integer schoolNumber;
	private String grant;
	private String status;
	private String createUserId;
	private Timestamp createDatetime;

	// GetterおよびSetter
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserClass() {
		return userClass;
	}

	public void setUserClass(String userClass) {
		this.userClass = userClass;
	}

	public Integer getClassNumber() {
		return classNumber;
	}

	public void setClassNumber(Integer classNumber) {
		this.classNumber = classNumber;
	}

	public Integer getSchoolNumber() {
		return schoolNumber;
	}

	public void setSchoolNumber(Integer schoolNumber) {
		this.schoolNumber = schoolNumber;
	}

	public String getGrant() {
		return grant;
	}

	public void setGrant(String grant) {
		this.grant = grant;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	public Timestamp getCreateDatetime() {
		return createDatetime;
	}

	public void setCreateDatetime(Timestamp createDatetime) {
		this.createDatetime = createDatetime;
	}

	@Override
	public String toString() {
		return "UserData [userId=" + userId + ", password=" + password + ", userName=" + userName + ", userClass="
				+ userClass + ", classNumber=" + classNumber + ", schoolNumber=" + schoolNumber + ", grant=" + grant
				+ ", status=" + status + ", createUserId=" + createUserId + ", createDatetime=" + createDatetime + "]";
	}

	// Builderクラスの定義
	public static class Builder {
		private String userId;
		private String password;
		private String userName;
		private String userClass;
		private Integer classNumber;
		private Integer schoolNumber;
		private String grant;
		private String status;
		private String createUserId;
		private Timestamp createDatetime;

		public Builder setUserId(String userId) {
			this.userId = userId;
			return this;
		}

		public Builder setPassword(String password) {
			this.password = password;
			return this;
		}

		public Builder setUserName(String userName) {
			this.userName = userName;
			return this;
		}

		public Builder setUserClass(String userClass) {
			this.userClass = userClass;
			return this;
		}

		public Builder setClassNumber(Integer classNumber) {
			this.classNumber = classNumber;
			return this;
		}

		public Builder setSchoolNumber(Integer schoolNumber) {
			this.schoolNumber = schoolNumber;
			return this;
		}

		public Builder setGrant(String grant) {
			this.grant = grant;
			return this;
		}

		public Builder setStatus(String status) {
			this.status = status;
			return this;
		}

		public Builder setCreateUserId(String createUserId) {
			this.createUserId = createUserId;
			return this;
		}

		public Builder setCreateDatetime(Timestamp createDatetime) {
			this.createDatetime = createDatetime;
			return this;
		}

		public UserData build() {
			UserData userData = new UserData();
			userData.userId = this.userId;
			userData.password = this.password;
			userData.userName = this.userName;
			userData.userClass = this.userClass;
			userData.classNumber = this.classNumber;
			userData.schoolNumber = this.schoolNumber;
			userData.grant = this.grant;
			userData.status = this.status;
			userData.createUserId = this.createUserId;
			userData.createDatetime = this.createDatetime;
			return userData;
		}
	}
}
