package com.api.domain.models.dbdata;

import java.sql.Timestamp;

/**
 * 1件分の就職活動ダッシュボード情報を表すモデルクラスです。
 *
 * <p>
 * 各項目は、就職活動に関連する情報を保持します。
 * データベース定義を参照して、項目の詳細仕様を確認してください。
 * </p>
 */
public class JobSearchDashBoardData {
	public JobSearchDashBoardData() {
	}

	/**
	 * ユーザ名
	 */
	private String userName;
	/**
	 * 就職活動状態
	 */
	private String jobSearchStatus;
	/**
	 * 就職活動の開始時間
	 */
	private Timestamp startTime;
	/**
	 * 会社名
	 */
	private String companyName;
	/**
	 * イベント区分
	 */
	private String eventCategory;
	/**
	 * 結果
	 */
	private String result;
	/**
	 * 学校申し込み
	 */
	private Boolean schoolCheckFlag;
	/**
	 * 就職活動終了時間
	 */
	private Timestamp endTime;
	/**
	 * 就職活動ID
	 */
	private String jobSearchId;
	/**
	 * 学科
	 */
	private String department;
	/**
	 * 学年
	 */
	private Integer grade;
	/**
	 * クラス
	 */
	private String className;
	/**
	 * 出席番号
	 */
	private Integer attendanceNumber;
	/**
	 * ユーザID
	 */
	private String userId;
	/**
	 * 就職活動の更新時刻
	 */
	private Timestamp maxUpdatedAt;

	// GetterおよびSetterメソッド
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getJobSearchStatus() {
		return jobSearchStatus;
	}

	public void setJobSearchStatus(String jobSearchStatus) {
		this.jobSearchStatus = jobSearchStatus;
	}

	public Timestamp getStartTime() {
		return startTime;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getEventCategory() {
		return eventCategory;
	}

	public void setEventCategory(String eventCategory) {
		this.eventCategory = eventCategory;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public Boolean getSchoolCheckFlag() {
		return schoolCheckFlag;
	}

	public void setSchoolCheckFlag(Boolean schoolCheckFlag) {
		this.schoolCheckFlag = schoolCheckFlag;
	}

	public Timestamp getEndTime() {
		return endTime;
	}

	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}

	public String getJobSearchId() {
		return jobSearchId;
	}

	public void setJobSearchId(String jobSearchId) {
		this.jobSearchId = jobSearchId;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public Integer getGrade() {
		return grade;
	}

	public void setGrade(Integer grade) {
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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Timestamp getMaxUpdatedAt() {
		return maxUpdatedAt;
	}

	public void setMaxUpdatedAt(Timestamp maxUpdatedAt) {
		this.maxUpdatedAt = maxUpdatedAt;
	}

	@Override
	public String toString() {
		return "JobSearchDashBoardData [userName=" + userName + ", jobSearchStatus=" + jobSearchStatus + ", startTime="
				+ startTime + ", companyName=" + companyName + ", eventCategory=" + eventCategory + ", result=" + result
				+ ", schoolCheckFlag=" + schoolCheckFlag + ", endTime=" + endTime + ", jobSearchId=" + jobSearchId
				+ ", department=" + department + ", grade=" + grade + ", className=" + className + ", attendanceNumber="
				+ attendanceNumber + ", userId=" + userId + ", maxUpdatedAt=" + maxUpdatedAt + "]";
	}

}
