package com.api.domain.models.dbdata;

import java.sql.Date;
import java.util.List;

/**
 * 証明書発行ダッシュボードの詳細情報を表すモデルクラスです。
 *
 * <p>
 * 各項目は、証明書発行に関する詳細データを保持します。
 * データベース定義に基づき、証明書発行の状態や関連する情報を含みます。
 * </p>
 */
public class CertificateIssuanceDashBoardDetailData {

	// デフォルトコンストラクタ
	public CertificateIssuanceDashBoardDetailData() {
	}

	// 証明書発行ID
	private String certificateIssueId;

	// 学生ユーザID
	private String studentUserId;

	// 学生の所属学科
	private String department;

	// 学年
	private String grade;

	// クラス名
	private String className;

	// 出席番号
	private Integer attendanceNumber;

	// 学生ユーザ名
	private String studentUserName;

	// 学生ID
	private Integer studentId;

	// 発行ステータス
	private String status;

	// 事務局ユーザID
	private String officeUserId;

	// 事務局ユーザ名
	private String officeUserName;

	// 受取人名
	private String recipientName;

	// 受取人名（ふりがな）
	private String recipientFurigana;

	// 受取人住所
	private String recipientAddress;

	// 媒体種別（例：郵送、窓口）
	private String mediaType;

	// 申請日
	private Date applicationDate;

	// 承認日
	private Date approvalDate;

	// 配送予定日
	private Date deliveryDueDate;

	// 配送日
	private Date deliveryDate;

	// 郵送日
	private Date postDate;

	// 郵便料金
	private Integer postalFee;

	// 郵便最大重量
	private Integer postalMaxWeight;

	// 証明書の詳細情報リスト
	private List<CertificateIssuanceDetailData> certificateList;

	// GetterおよびSetterメソッド群
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

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public Integer getAttendanceNumber() {
		return attendanceNumber;
	}

	public void setAttendanceNumber(Integer attendanceNumber) {
		this.attendanceNumber = attendanceNumber;
	}

	public String getStudentUserName() {
		return studentUserName;
	}

	public void setStudentUserName(String studentUserName) {
		this.studentUserName = studentUserName;
	}

	public Integer getStudentId() {
		return studentId;
	}

	public void setStudentId(Integer studentId) {
		this.studentId = studentId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOfficeUserId() {
		return officeUserId;
	}

	public void setOfficeUserId(String officeUserId) {
		this.officeUserId = officeUserId;
	}

	public String getOfficeUserName() {
		return officeUserName;
	}

	public void setOfficeUserName(String officeUserName) {
		this.officeUserName = officeUserName;
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

	public String getMediaType() {
		return mediaType;
	}

	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}

	public Date getApplicationDate() {
		return applicationDate;
	}

	public void setApplicationDate(Date applicationDate) {
		this.applicationDate = applicationDate;
	}

	public Date getApprovalDate() {
		return approvalDate;
	}

	public void setApprovalDate(Date approvalDate) {
		this.approvalDate = approvalDate;
	}

	public Date getDeliveryDueDate() {
		return deliveryDueDate;
	}

	public void setDeliveryDueDate(Date deliveryDueDate) {
		this.deliveryDueDate = deliveryDueDate;
	}

	public Date getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public Date getPostDate() {
		return postDate;
	}

	public void setPostDate(Date postDate) {
		this.postDate = postDate;
	}

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

	public List<CertificateIssuanceDetailData> getCertificateList() {
		return certificateList;
	}

	public void setCertificateList(List<CertificateIssuanceDetailData> certificateList) {
		this.certificateList = certificateList;
	}

	// オブジェクトの文字列表現を返却
	@Override
	public String toString() {
		return "CertificateIssuanceDashBoardDetailData [certificateIssueId=" + certificateIssueId +
				", studentUserId=" + studentUserId +
				", department=" + department +
				", grade=" + grade +
				", className=" + className +
				", attendanceNumber=" + attendanceNumber +
				", studentUserName=" + studentUserName +
				", studentId=" + studentId +
				", status=" + status +
				", officeUserId=" + officeUserId +
				", officeUserName=" + officeUserName +
				", recipientName=" + recipientName +
				", recipientFurigana=" + recipientFurigana +
				", recipientAddress=" + recipientAddress +
				", mediaType=" + mediaType +
				", applicationDate=" + applicationDate +
				", approvalDate=" + approvalDate +
				", deliveryDueDate=" + deliveryDueDate +
				", deliveryDate=" + deliveryDate +
				", postDate=" + postDate +
				", postalFee=" + postalFee +
				", postalMaxWeight=" + postalMaxWeight +
				", certificateList=" + certificateList + "]";
	}
}
