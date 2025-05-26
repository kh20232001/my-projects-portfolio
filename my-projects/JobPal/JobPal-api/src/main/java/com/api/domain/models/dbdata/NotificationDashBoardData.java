package com.api.domain.models.dbdata;

import java.sql.Timestamp;

/**
 * 1件分の通知ダッシュボード情報を表すモデルクラスです。
 *
 * <p>
 * 各項目は通知ダッシュボードに関連するデータを保持します。<br>
 * データベース定義を参照して、各フィールドの仕様を確認してください。
 * </p>
 */
public class NotificationDashBoardData {

	/**
	 * デフォルトコンストラクタ。
	 */
	public NotificationDashBoardData() {
	}

	/**
	 * 通知ID。
	 */
	private String notifiationId;

	/**
	 * 割り当てられたユーザID。
	 */
	private String assignedUserId;

	/**
	 * 再送フラグ。
	 * true：再送、false：通常送信。
	 */
	private Boolean resendFlag;

	/**
	 * 就職活動または証明書カテゴリ。
	 */
	private String jobSearchCertificateCategory;

	/**
	 * 通知作成日時。
	 */
	private Timestamp notificationCreatedAt;

	/**
	 * 証明書発行ダッシュボードデータ。
	 */
	private CertificateIssuanceDashBoardData certificateIssuanceDashBoardData;

	/**
	 * 就職活動ダッシュボードデータ。
	 */
	private JobSearchDashBoardData jobSearchDashBoardData;

	// GetterおよびSetterメソッド
	public String getNotifiationId() {
		return notifiationId;
	}

	public void setNotifiationId(String notifiationId) {
		this.notifiationId = notifiationId;
	}

	public String getAssignedUserId() {
		return assignedUserId;
	}

	public void setAssignedUserId(String assignedUserId) {
		this.assignedUserId = assignedUserId;
	}

	public Boolean getResendFlag() {
		return resendFlag;
	}

	public void setResendFlag(Boolean resendFlag) {
		this.resendFlag = resendFlag;
	}

	public String getJobSearchCertificateCategory() {
		return jobSearchCertificateCategory;
	}

	public void setJobSearchCertificateCategory(String jobSearchCertificateCategory) {
		this.jobSearchCertificateCategory = jobSearchCertificateCategory;
	}

	public Timestamp getNotificationCreatedAt() {
		return notificationCreatedAt;
	}

	public void setNotificationCreatedAt(Timestamp notificationCreatedAt) {
		this.notificationCreatedAt = notificationCreatedAt;
	}

	public CertificateIssuanceDashBoardData getCertificateIssuanceDashBoardData() {
		return certificateIssuanceDashBoardData;
	}

	public void setCertificateIssuanceDashBoardData(CertificateIssuanceDashBoardData certificateIssuanceDashBoardData) {
		this.certificateIssuanceDashBoardData = certificateIssuanceDashBoardData;
	}

	public JobSearchDashBoardData getJobSearchDashBoardData() {
		return jobSearchDashBoardData;
	}

	public void setJobSearchDashBoardData(JobSearchDashBoardData jobSearchDashBoardData) {
		this.jobSearchDashBoardData = jobSearchDashBoardData;
	}

	@Override
	public String toString() {
		return "NotificationDashBoardData [notifiationId=" + notifiationId + ", assignedUserId=" + assignedUserId
				+ ", resendFlag=" + resendFlag + ", jobSearchCertificateCategory=" + jobSearchCertificateCategory
				+ ", notificationCreatedAt=" + notificationCreatedAt + ", certificateIssuanceDashBoardData="
				+ certificateIssuanceDashBoardData + ", jobSearchDashBoardData=" + jobSearchDashBoardData + "]";
	}

}
