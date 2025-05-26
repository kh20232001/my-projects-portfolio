package com.api.domain.models.data;

import java.util.Date;

/**
 * 1件分の通知画面に表示する通知の情報です。
 *
 * <p>
 * 各項目のデータ構造については、データベース定義をご覧ください。
 */
public class AlertDisplayData {
	/**
	 * ユーザIDを識別する数字
	 * 主キー、必須入力
	 */
	private String userId;

	/**
	 * クラス番号
	 * クラス番号がくっついた状態
	 */
	private String schoolClassNumber;
	/**
	 * 就職活動の状態
	 * - A1：申請承認待ち
	 * - A2：申請承認済み
	 * - A3：申請差戻し
	 * - R1：報告待ち
	 * - R2：報告承認待ち
	 * - R3：報告承認済み
	 * - R4：報告差戻し
	 * - E1：受験報告待ち
	 * - E2：受験報告承認待ち
	 * - E3：受験報告済み
	 * - E4：受験報告差戻し
	 */
	private String jobStateId;

	/**
	 * 企業名
	 */
	private String company;

	/**
	 * イベント区分
	 */
	private String eventCategory;

	/**
	 * 結果
	 */
	private String result;

	/**
	 * 学校申し込みチェック
	 */
	private Boolean schoolCheck;

	/**
	 * 日付
	 */
	private String date;

	/**
	 * 開始時間
	 * 必須入力
	 */
	private String startTime;

	/**
	 * 終了時間
	 */
	private String finishTime;

	/**
	 * 更新日時
	 */
	private Date updateTime;

	/**
	 * 申請ID
	 */
	private String jobHuntId;

	/**
	 * ユーザ名
	 */
	private String userName;

	/**
	 * イベント区分の表示優先度
	 */
	private int eventCategoryPriority;

	/**
	 * 就職活動の状態の表示優先度
	 */
	private int jobStateIdPriority;

	/**
	 * イベント区分の表示優先度
	 */
	private long startTimePriority;

	// getter
	public String getUserId() {
		return userId;
	}

	public String getSchoolClassNumber() {
		return schoolClassNumber;
	}

	public String getJobStateId() {
		return jobStateId;
	}

	public String getCompany() {
		return company;
	}

	public String getEventCategory() {
		return eventCategory;
	}

	public String getResult() {
		return result;
	}

	public Boolean getSchoolCheck() {
		return schoolCheck;
	}

	public String getDate() {
		return date;
	}

	public String getStartTime() {
		return startTime;
	}

	public String getFinishTime() {
		return finishTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public String getJobHuntId() {
		return jobHuntId;
	}

	public String getUserName() {
		return userName;
	}

	public int getEventCategoryPriority() {
		return eventCategoryPriority;
	}

	public int getJobStateIdPriority() {
		return jobStateIdPriority;
	}

	public long getStartTimePriority() {
		return startTimePriority;
	}

	// setter
	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setSchoolClassNumber(String schoolClassNumber) {
		this.schoolClassNumber = schoolClassNumber;
	}

	public void setJobStateId(String jobStateId) {
		this.jobStateId = jobStateId;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public void setEventCategory(String eventCategory) {
		this.eventCategory = eventCategory;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public void setSchoolCheck(Boolean schoolCheck) {
		this.schoolCheck = schoolCheck;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public void setFinishTime(String finishTime) {
		this.finishTime = finishTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public void setJobHuntId(String jobHuntId) {
		this.jobHuntId = jobHuntId;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setEventCategoryPriority(int eventCategoryPriority) {
		this.eventCategoryPriority = eventCategoryPriority;
	}

	public void setJobStateIdPriority(int jobStateIdPriority) {
		this.jobStateIdPriority = jobStateIdPriority;
	}

	public void setStartTimePriority(long startTimePriority) {
		this.startTimePriority = startTimePriority;
	}

	public AlertDisplayData copy(AlertData alertData) {
		AlertDisplayData alertDisplayData = new AlertDisplayData();
		alertDisplayData.setUserId(alertData.getUserId());
		alertDisplayData.setSchoolClassNumber(alertData.getClassNumber());
		alertDisplayData.setCompany(alertData.getCompany());
		alertDisplayData.setEventCategory(alertData.getEventCategory());
		alertDisplayData.setJobHuntId(alertData.getJobHuntId());
		alertDisplayData.setJobStateId(alertData.getJobStateId());
		alertDisplayData.setResult(alertData.getResult());
		alertDisplayData.setSchoolCheck(alertData.getSchoolCheck());
		alertDisplayData.setUpdateTime(alertData.getUpdateTime());
		alertDisplayData.setUserName(alertData.getUserName());
		return alertDisplayData;
	}

	@Override
	public String toString() {
		return "DashBoardData [userId=" + userId + ", classNumber=" + schoolClassNumber + ", jobStateId=" + jobStateId
				+ ", startTime=" + startTime + ", company=" + company + ", eventCategory=" + eventCategory + ", result="
				+ result + ", schoolCheck=" + schoolCheck + ", finishTime=" + finishTime + ", updateTime=" + updateTime
				+ ", jobHuntId=" + jobHuntId + ", userName=" + userName + "]";
	}

}
