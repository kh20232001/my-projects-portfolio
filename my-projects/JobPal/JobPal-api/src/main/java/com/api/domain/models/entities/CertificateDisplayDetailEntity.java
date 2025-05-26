package com.api.domain.models.entities;

import java.util.List;

import com.api.domain.models.displaydata.CertificateDisplayItem;

/**
 * 証明書発行情報の詳細エンティティクラスです。
 *
 * <p>
 * 各項目のデータ構造については、データベース定義をご覧ください。
 */
public class CertificateDisplayDetailEntity {
	/**
	 * 証明書発行ID
	 */
	private String certificateIssueId;

	/**
	 * ユーザーID
	 */
	private String userId;

	/**
	 * ユーザークラス
	 */
	private String userClass;

	/**
	 * クラス番号
	 */
	private String classNumber;

	/**
	 * ユーザー名
	 */
	private String userName;

	/**
	 * 学校クラス番号
	 */
	private String schoolClassNumber;

	/**
	 * 証明書状態名
	 */
	private String certificateStateName;

	/**
	 * 事務ユーザー名
	 */
	private String officeUserName;

	/**
	 * 姓
	 */
	private String lastName;

	/**
	 * 名
	 */
	private String firstName;

	/**
	 * カナ姓
	 */
	private String lastNameKana;

	/**
	 * カナ名
	 */
	private String firstNameKana;

	/**
	 * 証明書リスト
	 */
	private List<CertificateDisplayItem> certificateList;

	/**
	 * 媒体名
	 */
	private String mediaName;

	/**
	 * 郵便番号
	 */
	private String zipCode;

	/**
	 * 住所
	 */
	private String address;

	/**
	 * 住所後半部分
	 */
	private String afterAddress;

	/**
	 * 申請日
	 */
	private String applicationDate;

	/**
	 * 支払日
	 */
	private String dueDate;

	/**
	 * 発行日
	 */
	private String issuingDate;

	/**
	 * 受取日
	 */
	private String receivingDate;

	/**
	 * 受取期限日
	 */
	private String receivingExpirationDate;

	/**
	 * 郵送日
	 */
	private String mailDate;

	/**
	 * 郵送料金
	 */
	private Integer mailFee;

	/**
	 * 郵送最大重量
	 */
	private Integer mailMaxWeight;

	// setters
	public void setCertificateIssueId(String certificateIssueId) {
		this.certificateIssueId = certificateIssueId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setUserClass(String userClass) {
		this.userClass = userClass;
	}

	public void setClassNumber(String classNumber) {
		this.classNumber = classNumber;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setSchoolClassNumber(String schoolClassNumber) {
		this.schoolClassNumber = schoolClassNumber;
	}

	public void setCertificateStateName(String certificateStateName) {
		this.certificateStateName = certificateStateName;
	}

	public void setOfficeUserName(String officeUserName) {
		this.officeUserName = officeUserName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastNameKana(String lastNameKana) {
		this.lastNameKana = lastNameKana;
	}

	public void setFirstNameKana(String firstNameKana) {
		this.firstNameKana = firstNameKana;
	}

	public void setCertificateList(List<CertificateDisplayItem> certificateList) {
		this.certificateList = certificateList;
	}

	public void setMediaName(String mediaName) {
		this.mediaName = mediaName;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setAfterAddress(String afterAddress) {
		this.afterAddress = afterAddress;
	}

	public void setApplicationDate(String applicationDate) {
		this.applicationDate = applicationDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	public void setIssuingDate(String issuingDate) {
		this.issuingDate = issuingDate;
	}

	public void setReceivingDate(String receivingDate) {
		this.receivingDate = receivingDate;
	}

	public void setReceivingExpirationDate(String receivingExpirationDate) {
		this.receivingExpirationDate = receivingExpirationDate;
	}

	public void setMailDate(String mailDate) {
		this.mailDate = mailDate;
	}

	public void setMailFee(Integer mailFee) {
		this.mailFee = mailFee;
	}

	public void setMailMaxWeight(Integer mailMaxWeight) {
		this.mailMaxWeight = mailMaxWeight;
	}

	// getters
	public String getCertificateIssueId() {
		return certificateIssueId;
	}

	public String getUserId() {
		return userId;
	}

	public String getUserClass() {
		return userClass;
	}

	public String getClassNumber() {
		return classNumber;
	}

	public String getUserName() {
		return userName;
	}

	public String getSchoolClassNumber() {
		return schoolClassNumber;
	}

	public String getCertificateStateName() {
		return certificateStateName;
	}

	public String getOfficeUserName() {
		return officeUserName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastNameKana() {
		return lastNameKana;
	}

	public String getFirstNameKana() {
		return firstNameKana;
	}

	public List<CertificateDisplayItem> getCertificateList() {
		return certificateList;
	}

	public String getMediaName() {
		return mediaName;
	}

	public String getZipCode() {
		return zipCode;
	}

	public String getAddress() {
		return address;
	}

	public String getAfterAddress() {
		return afterAddress;
	}

	public String getApplicationDate() {
		return applicationDate;
	}

	public String getDueDate() {
		return dueDate;
	}

	public String getIssuingDate() {
		return issuingDate;
	}

	public String getReceivingDate() {
		return receivingDate;
	}

	public String getReceivingExpirationDate() {
		return receivingExpirationDate;
	}

	public String getMailDate() {
		return mailDate;
	}

	public Integer getMailFee() {
		return mailFee;
	}

	public Integer getMailMaxWeight() {
		return mailMaxWeight;
	}

	@Override
	public String toString() {
		return "CertificateIssueDetail [certificateIssueId=" + certificateIssueId + ", userId=" + userId + ", userClass="
				+ userClass + ", classNumber=" + classNumber + ", userName=" + userName + ", schoolClassNumber="
				+ schoolClassNumber + ", certificateStateName=" + certificateStateName + ", officeUserName="
				+ officeUserName + ", lastName=" + lastName + ", firstName=" + firstName + ", lastNameKana="
				+ lastNameKana + ", firstNameKana=" + firstNameKana + ", certificateList=" + certificateList
				+ ", mediaName=" + mediaName + ", zipCode=" + zipCode + ", address=" + address + ", afterAddress="
				+ afterAddress + ", applicationDate=" + applicationDate + ", dueDate=" + dueDate + ", issuingDate="
				+ issuingDate + ", receivingDate=" + receivingDate + ", receivingExpirationDate="
				+ receivingExpirationDate + ", mailDate=" + mailDate + ", mailFee=" + mailFee + ", mailMaxWeight="
				+ mailMaxWeight + "]";
	}

	public static class Builder {
		// 既存フィールドと新規フィールドの追加
		private String certificateIssueId;
		private String userId;
		private String userClass;
		private String classNumber;
		private String userName;
		private String schoolClassNumber;
		private String certificateStateName;
		private String officeUserName;
		private String lastName;
		private String firstName;
		private String lastNameKana;
		private String firstNameKana;
		private List<CertificateDisplayItem> certificateList;
		private String mediaName;
		private String zipCode;
		private String address;
		private String afterAddress;
		private String applicationDate;
		private String dueDate;
		private String issuingDate;
		private String receivingDate;
		private String receivingExpirationDate;
		private String mailDate;
		private Integer mailFee;
		private Integer mailMaxWeight;

		public Builder setCertificateIssueId(String certificateIssueId) {
			this.certificateIssueId = certificateIssueId;
			return this;
		}

		public Builder setUserId(String userId) {
			this.userId = userId;
			return this;
		}

		public Builder setUserClass(String userClass) {
			this.userClass = userClass;
			return this;
		}

		public Builder setClassNumber(String classNumber) {
			this.classNumber = classNumber;
			return this;
		}

		public Builder setUserName(String userName) {
			this.userName = userName;
			return this;
		}

		public Builder setSchoolClassNumber(String schoolClassNumber) {
			this.schoolClassNumber = schoolClassNumber;
			return this;
		}

		public Builder setCertificateStateName(String certificateStateName) {
			this.certificateStateName = certificateStateName;
			return this;
		}

		public Builder setOfficeUserName(String officeUserName) {
			this.officeUserName = officeUserName;
			return this;
		}

		public Builder setLastName(String lastName) {
			this.lastName = lastName;
			return this;
		}

		public Builder setFirstName(String firstName) {
			this.firstName = firstName;
			return this;
		}

		public Builder setLastNameKana(String lastNameKana) {
			this.lastNameKana = lastNameKana;
			return this;
		}

		public Builder setFirstNameKana(String firstNameKana) {
			this.firstNameKana = firstNameKana;
			return this;
		}

		public Builder setCertificateList(List<CertificateDisplayItem> certificateList) {
			this.certificateList = certificateList;
			return this;
		}

		public Builder setMediaName(String mediaName) {
			this.mediaName = mediaName;
			return this;
		}

		public Builder setZipCode(String zipCode) {
			this.zipCode = zipCode;
			return this;
		}

		public Builder setAddress(String address) {
			this.address = address;
			return this;
		}

		public Builder setAfterAddress(String afterAddress) {
			this.afterAddress = afterAddress;
			return this;
		}

		public Builder setApplicationDate(String applicationDate) {
			this.applicationDate = applicationDate;
			return this;
		}

		public Builder setDueDate(String dueDate) {
			this.dueDate = dueDate;
			return this;
		}

		public Builder setIssuingDate(String issuingDate) {
			this.issuingDate = issuingDate;
			return this;
		}

		public Builder setReceivingDate(String receivingDate) {
			this.receivingDate = receivingDate;
			return this;
		}

		public Builder setReceivingExpirationDate(String receivingExpirationDate) {
			this.receivingExpirationDate = receivingExpirationDate;
			return this;
		}

		public Builder setMailDate(String mailDate) {
			this.mailDate = mailDate;
			return this;
		}

		public Builder setMailFee(Integer mailFee) {
			this.mailFee = mailFee;
			return this;
		}

		public Builder setMailMaxWeight(Integer mailMaxWeight) {
			this.mailMaxWeight = mailMaxWeight;
			return this;
		}

		public CertificateDisplayDetailEntity build() {
			CertificateDisplayDetailEntity entity = new CertificateDisplayDetailEntity();
			entity.setCertificateIssueId(certificateIssueId);
			entity.setUserId(userId);
			entity.setUserClass(userClass);
			entity.setClassNumber(classNumber);
			entity.setUserName(userName);
			entity.setSchoolClassNumber(schoolClassNumber);
			entity.setCertificateStateName(certificateStateName);
			entity.setOfficeUserName(officeUserName);
			entity.setLastName(lastName);
			entity.setFirstName(firstName);
			entity.setLastNameKana(lastNameKana);
			entity.setFirstNameKana(firstNameKana);
			entity.setCertificateList(certificateList);
			entity.setMediaName(mediaName);
			entity.setZipCode(zipCode);
			entity.setAddress(address);
			entity.setAfterAddress(afterAddress);
			entity.setApplicationDate(applicationDate);
			entity.setDueDate(dueDate);
			entity.setIssuingDate(issuingDate);
			entity.setReceivingDate(receivingDate);
			entity.setReceivingExpirationDate(receivingExpirationDate);
			entity.setMailDate(mailDate);
			entity.setMailFee(mailFee);
			entity.setMailMaxWeight(mailMaxWeight);
			return entity;
		}
	}
}
