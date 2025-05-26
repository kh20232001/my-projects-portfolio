package com.api.domain.models.data;

import java.util.Date;

/**
 * 1件分の就職活動ダッシュボードの情報です。
 *
 * <p>
 * 各項目のデータ構造については、データベース定義をご覧ください。
 */
public class DashBoardDetailData {
	/**
	 * 申請ID
	 */
	private String jobHuntId;

	/**
	 * ユーザ名
	 */
	private String userName;

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
	 * クラス
	 */
	private String userClass;

	/**
	 * 出席番号
	 */
	private Integer classNumber;

	/**
	 * 学校番号
	 */
	private Integer schoolNumber;

	/**
	 * 企業名
	 */
	private String company;

	/**
	 * イベント区分
	 */
	private String eventCategory;

	/**
	 * 場所
	 */
	private String place;

	/**
	 * 学校申し込みチェック
	 */
	private Boolean schoolCheck;

	/**
	 * 開始日時
	 * 必須入力
	 */
	private Date startTime;

	/**
	 * 終了日時
	 */
	private Date finishTime;

	/**
	 * 更新日時
	 */
	private Date updateTime;

	/**
	 * 遅刻早退欠席
	 */
	private String attendanceStatus;

	/**
	 * 報告内容
	 */
	private String reportContent;

	/**
	 * 結果
	 */
	private String result;

	/**
	 * 対応人数
	 */
	private Integer cnt;

	/**
	 * 対応役職
	 */
	private String jobTitle;

	/**
	 * 試験区分
	 */
	private Integer examCategory;

	/**
	 * 試験内容
	 */
	private String examContent;

	/**
	 * 受験内容
	 */
	private String ecaminationContent;

	/**
	 * 感想
	 */
	private String thoughts;

	// getter
	public String getJobHuntId() {
		return jobHuntId;
	}

	public String getUserName() {
		return userName;
	}

	public String getJobStateId() {
		return jobStateId;
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

	public String getCompany() {
		return company;
	}

	public String getEventCategory() {
		return eventCategory;
	}

	public String getPlace() {
		return place;
	}

	public Boolean getSchoolCheck() {
		return schoolCheck;
	}

	public Date getStartTime() {
		return startTime;
	}

	public Date getFinishTime() {
		return finishTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public String getAttendanceStatus() {
		return attendanceStatus;
	}

	public String getReportContent() {
		return reportContent;
	}

	public String getResult() {
		return result;
	}

	public Integer getCnt() {
		return cnt;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public Integer getExamCategory() {
		return examCategory;
	}

	public String getExamContent() {
		return examContent;
	}

	public String getEcaminationContent() {
		return ecaminationContent;
	}

	public String getThoughts() {
		return thoughts;
	}

	// setter
	public void setJobHuntId(String jobHuntId) {
		this.jobHuntId = jobHuntId;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setJobStateId(String jobStateId) {
		this.jobStateId = jobStateId;
	}

	public void setUserClass(String userClass) {
		this.userClass = userClass;
	}

	public void setClassNumber(Integer classNumber) {
		this.classNumber = classNumber;
	}

	public void setSchoolNumber(Integer schoolNumber) {
		this.schoolNumber = schoolNumber;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public void setEventCategory(String eventCategory) {
		this.eventCategory = eventCategory;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public void setSchoolCheck(Boolean schoolCheck) {
		this.schoolCheck = schoolCheck;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public void setAttendanceStatus(String attendanceStatus) {
		this.attendanceStatus = attendanceStatus;
	}

	public void setReportContent(String reportContent) {
		this.reportContent = reportContent;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public void setCnt(Integer cnt) {
		this.cnt = cnt;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public void setExamCategory(Integer examCategory) {
		this.examCategory = examCategory;
	}

	public void setExamContent(String examContent) {
		this.examContent = examContent;
	}

	public void setEcaminationContent(String ecaminationContent) {
		this.ecaminationContent = ecaminationContent;
	}

	public void setThoughts(String thoughts) {
		this.thoughts = thoughts;
	}

	@Override
	public String toString() {
		return "DashBoardDetailData [jobHuntId=" + jobHuntId + ", userName=" + userName
				+ ", jobStateId=" + jobStateId + ", userClass=" + userClass + ", classNumber=" + classNumber
				+ ", schoolNumber=" + schoolNumber + ", company=" + company + ", eventCategory=" + eventCategory
				+ ", place=" + place + ", schoolCheck=" + schoolCheck + ", startTime=" + startTime + ", finishTime="
				+ finishTime + ", updateTime=" + updateTime + ", attendanceStatus=" + attendanceStatus
				+ ", reportContent=" + reportContent + ", result=" + result + ", cnt=" + cnt + ", jobTitle=" + jobTitle
				+ ", examCategory=" + examCategory + ", examContent=" + examContent + ", ecaminationContent="
				+ ecaminationContent + ", thoughts=" + thoughts + "]";
	}

}
