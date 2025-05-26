package com.api.domain.models.dbdata;

import java.sql.Date;
import java.util.List;

/**
 * 1件分の証明書発行ダッシュボード情報を表すモデルクラスです。
 *
 * <p>
 * 各項目のデータ構造については、データベース定義を参照してください。
 * </p>
 */
public class CertificateIssuanceDashBoardData {

	// デフォルトコンストラクタ
	public CertificateIssuanceDashBoardData() {
	}

	// 証明書発行ID
	private String certificateIssueId;

	// 学生ユーザID
	private String studentUserId;

	// 学生ユーザ名
	private String studentUserName;

	// 学籍番号
	private Integer studentId;

	// 発行ステータス
	private String status;

	// 媒体種別
	private String mediaType;

	// 最新の日付
	private Date latestDate;

	// 証明書情報のリスト
	private List<CertificateData> CertificateList;

	// 合計金額
	private Integer totalAmount;

	private Integer totalWeight;
	private Integer totalMailFee;

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

	public String getMediaType() {
		return mediaType;
	}

	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}

	public Date getLatestDate() {
		return latestDate;
	}

	public void setLatestDate(Date latestDate) {
		this.latestDate = latestDate;
	}

	public List<CertificateData> getCertificateList() {
		return CertificateList;
	}

	public void setCertificateList(List<CertificateData> certificateList) {
		CertificateList = certificateList;
	}

	public Integer getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Integer totalAmount) {
		this.totalAmount = totalAmount;
	}

	public void setUpTotalAmount(Integer totalAmount) {
		this.totalAmount += totalAmount;
	}

	public Integer getTotalWeight() {
		return totalWeight;
	}

	public void setTotalWeight(Integer totalWeight) {
		this.totalWeight = totalWeight;
	}

	public void setUpTotalWeight(Integer totalWeight) {
		this.totalWeight += totalWeight;
	}

	public Integer getTotalMailFee() {
		return totalMailFee;
	}

	public void setTotalMailFee(Integer totalMailFee) {
		this.totalMailFee = totalMailFee;
	}

	// オブジェクトの文字列表現を返却
	@Override
	public String toString() {
		return "CertificateIssuanceDashBoardData [certificateIssueId=" + certificateIssueId + ", studentUserId="
				+ studentUserId + ", studentUserName=" + studentUserName + ", studentId=" + studentId + ", status="
				+ status + ", mediaType=" + mediaType + ", latestDate=" + latestDate + ", CertificateList="
				+ CertificateList + ", totalAmount=" + totalAmount + ", totalWeight=" + totalWeight + "]";
	}

}
