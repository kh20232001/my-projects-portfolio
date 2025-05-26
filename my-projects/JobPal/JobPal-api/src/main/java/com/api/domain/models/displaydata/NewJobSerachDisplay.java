package com.api.domain.models.displaydata;

/**
 * 新規就職活動申請書作成時に表示するユーザ情報です。
 *
 * <p>
 * 各項目のデータ構造については、データベース定義をご覧ください。
 */
public class NewJobSerachDisplay {

	private String userName;
	private String userClass;
	private Integer classNumber;
	private Integer schoolNumber;

	// Getter
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

	// builder
	private NewJobSerachDisplay(Builder builder) {
		this.userName = builder.userName;
		this.userClass = builder.userClass;
		this.classNumber = builder.classNumber;
		this.schoolNumber = builder.schoolNumber;
	}

	public static class Builder {
		private String userName;
		private String userClass;
		private Integer classNumber;
		private Integer schoolNumber;

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

		public NewJobSerachDisplay build() {
			return new NewJobSerachDisplay(this);
		}
	}
}
