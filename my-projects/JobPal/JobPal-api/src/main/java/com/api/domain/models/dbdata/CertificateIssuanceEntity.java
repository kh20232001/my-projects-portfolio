package com.api.domain.models.dbdata;

import java.sql.Date;
import java.util.List;

/**
 * 1件分の証明書発行の情報です。
 *
 * <p>
 * 各項目のデータ構造については、データベース定義をご覧ください。
 */
public class CertificateIssuanceEntity {
	/**
	 * 証明書発行ID
	 */
	private String certificateIssueId;

	/**
	 * 学生ユーザID
	 */
	private String studentUserId;

	/**
	 * 申請日
	 */
	private Date applicationDate;

	/**
	 * 媒体区分コード
	 */
	private String mediaType;

	/**
	 * 担任ユーザID
	 */
	private String teacherUserId;

	/**
	 * 担任チェックフラグ
	 */
	private Boolean teacherCheckFlag;

	/**
	 * 事務部ユーザID
	 */
	private String officeUserId;

	/**
	 * 承認日
	 */
	private Date approvalDate;

	/**
	 * 証明書詳細リスト
	 */
	private List<CertificateIssuanceDetailData> CertificateIssuanceDetailList;

	/**
	 * 受取人名
	 */
	private String recipientName;

	/**
	 * 受取人ふりがな
	 */
	private String recipientFurigana;

	/**
	 * 受取人住所
	 */
	private String recipientAddress;

	/**
	 * デフォルトコンストラクタ。
	 */
	public CertificateIssuanceEntity() {
	}

	// GetterおよびSetterメソッド
	public String getCertificateIssueId() {
		return certificateIssueId;
	}

	public void setCertificateIssueId(String certificateIssueId) {
		this.certificateIssueId = certificateIssueId;
	}

	public String getStudentUserId() {
		return studentUserId;
	}

	public void setStudentUserId(String studentUserId) {
		this.studentUserId = studentUserId;
	}

	public Date getApplicationDate() {
		return applicationDate;
	}

	public void setApplicationDate(Date applicationDate) {
		this.applicationDate = applicationDate;
	}

	public String getMediaType() {
		return mediaType;
	}

	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}

	public String getTeacherUserId() {
		return teacherUserId;
	}

	public void setTeacherUserId(String teacherUserId) {
		this.teacherUserId = teacherUserId;
	}

	public Boolean getTeacherCheckFlag() {
		return teacherCheckFlag;
	}

	public void setTeacherCheckFlag(Boolean teacherCheckFlag) {
		this.teacherCheckFlag = teacherCheckFlag;
	}

	public String getOfficeUserId() {
		return officeUserId;
	}

	public void setOfficeUserId(String officeUserId) {
		this.officeUserId = officeUserId;
	}

	public Date getApprovalDate() {
		return approvalDate;
	}

	public void setApprovalDate(Date approvalDate) {
		this.approvalDate = approvalDate;
	}

	public List<CertificateIssuanceDetailData> getCertificateIssuanceDetailList() {
		return CertificateIssuanceDetailList;
	}

	public void setCertificateIssuanceDetailList(List<CertificateIssuanceDetailData> certificateIssuanceDetailList) {
		CertificateIssuanceDetailList = certificateIssuanceDetailList;
	}

	public String getRecipientName() {
		return recipientName;
	}

	public void setRecipientName(String recipientName) {
		this.recipientName = recipientName;
	}

	public String getRecipientFurigana() {
		return recipientFurigana;
	}

	public void setRecipientFurigana(String recipientFurigana) {
		this.recipientFurigana = recipientFurigana;
	}

	public String getRecipientAddress() {
		return recipientAddress;
	}

	public void setRecipientAddress(String recipientAddress) {
		this.recipientAddress = recipientAddress;
	}

	// Builderクラス
	public static class Builder {
		private String certificateIssueId;
		private String studentUserId;
		private Date applicationDate;
		private String mediaType;
		private String teacherUserId;
		private Boolean teacherCheckFlag;
		private String officeUserId;
		private Date approvalDate;
		private List<CertificateIssuanceDetailData> certificateIssuanceDetailList;
		private String recipientName;
		private String recipientFurigana;
		private String recipientAddress;

		public Builder certificateIssueId(String certificateIssueId) {
			this.certificateIssueId = certificateIssueId;
			return this;
		}

		public Builder studentUserId(String studentUserId) {
			this.studentUserId = studentUserId;
			return this;
		}

		public Builder applicationDate(Date applicationDate) {
			this.applicationDate = applicationDate;
			return this;
		}

		public Builder mediaType(String mediaType) {
			this.mediaType = mediaType;
			return this;
		}

		public Builder teacherUserId(String teacherUserId) {
			this.teacherUserId = teacherUserId;
			return this;
		}

		public Builder teacherCheckFlag(Boolean teacherCheckFlag) {
			this.teacherCheckFlag = teacherCheckFlag;
			return this;
		}

		public Builder officeUserId(String officeUserId) {
			this.officeUserId = officeUserId;
			return this;
		}

		public Builder approvalDate(Date approvalDate) {
			this.approvalDate = approvalDate;
			return this;
		}

		public Builder certificateIssuanceDetailList(
				List<CertificateIssuanceDetailData> certificateIssuanceDetailList) {
			this.certificateIssuanceDetailList = certificateIssuanceDetailList;
			return this;
		}

		public Builder recipientName(String recipientName) {
			this.recipientName = recipientName;
			return this;
		}

		public Builder recipientFurigana(String recipientFurigana) {
			this.recipientFurigana = recipientFurigana;
			return this;
		}

		public Builder recipientAddress(String recipientAddress) {
			this.recipientAddress = recipientAddress;
			return this;
		}

		public CertificateIssuanceEntity build() {
			CertificateIssuanceEntity entity = new CertificateIssuanceEntity();
			entity.setCertificateIssueId(this.certificateIssueId);
			entity.setStudentUserId(this.studentUserId);
			entity.setApplicationDate(this.applicationDate);
			entity.setMediaType(this.mediaType);
			entity.setTeacherUserId(this.teacherUserId);
			entity.setTeacherCheckFlag(this.teacherCheckFlag);
			entity.setOfficeUserId(this.officeUserId);
			entity.setApprovalDate(this.approvalDate);
			entity.setCertificateIssuanceDetailList(this.certificateIssuanceDetailList);
			entity.setRecipientName(this.recipientName);
			entity.setRecipientFurigana(this.recipientFurigana);
			entity.setRecipientAddress(this.recipientAddress);
			return entity;
		}
	}

}
