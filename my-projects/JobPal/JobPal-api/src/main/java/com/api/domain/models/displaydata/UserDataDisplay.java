package com.api.domain.models.displaydata;

import java.sql.Timestamp;

/**
 * 1件分のユーザ情報です。
 *
 * <p>
 * 各項目のデータ構造については、データベース定義をご覧ください。
 */
public class UserDataDisplay {

	private Integer number;
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

	// Getter
	public Integer getNumber() {
		return number;
	}

	public String getUserId() {
		return userId;
	}

	public String getPassword() {
		return password;
	}

	public String getUserName() {
		return userName;
	}

	public String getUserClass() {
		return userClass;
	}

	public Integer getClassNumber() {
		return classNumber;
	}

	public Integer getSchoolNumber() {
		return schoolNumber;
	}

	public String getGrant() {
		return grant;
	}

	public String getStatus() {
		return status;
	}

	public String getCreateUserId() {
		return createUserId;
	}

	public Timestamp getCreateDatetime() {
		return createDatetime;
	}

	@Override
	public String toString() {
		return "UserData [number=" + number + ", userId=" + userId + ", password=" + password + ", userName=" + userName
				+ ", userClass=" + userClass + ", classNumber=" + classNumber + ", schoolNumber=" + schoolNumber
				+ ", grant=" + grant + ", status=" + status + ", createUserId=" + createUserId + ", createDatetime="
				+ createDatetime + "]";
	}

	// builder
	private UserDataDisplay(Builder builder) {
		this.number = builder.number;
		this.userId = builder.userId;
		this.password = builder.password;
		this.userName = builder.userName;
		this.userClass = builder.userClass;
		this.classNumber = builder.classNumber;
		this.schoolNumber = builder.schoolNumber;
		this.grant = builder.grant;
		this.status = builder.status;
		this.createUserId = builder.createUserId;
		this.createDatetime = builder.createDatetime;
	}

	public static class Builder {
		private Integer number;
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

		public Builder setNumber(Integer number) {
			this.number = number;
			return this;
		}

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

		public UserDataDisplay build() {
			return new UserDataDisplay(this);
		}
	}
}
