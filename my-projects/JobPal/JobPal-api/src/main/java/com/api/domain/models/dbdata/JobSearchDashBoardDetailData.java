package com.api.domain.models.dbdata;

import java.sql.Time;
import java.sql.Timestamp;

/**
 * 1件分の就職活動ダッシュボード詳細情報を表すモデルクラスです。
 *
 * <p>
 * 各項目は、就職活動に関連する詳細な情報を保持します。
 * データベース定義を参照して、項目の詳細仕様を確認してください。
 * </p>
 */
public class JobSearchDashBoardDetailData {

	/**
	 * ユーザ名
	 */
	private String userName;

	/**
	 * 就職活動ID
	 */
	private String jobSearchId;

	/**
	 * 学生ユーザID
	 */
	private String studentUserId;

	/**
	 * 就職活動状態
	 */
	private String jobSearchStatus;

	/**
	 * 就職活動申請ID
	 */
	private String jobApplicationId;

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
	 * 場所区分
	 */
	private String locationType;

	/**
	 * 場所
	 */
	private String location;

	/**
	 * 学校申し込みフラグ
	 */
	private Boolean schoolCheckFlag;

	/**
	 * 名簿確認フラグ
	 */
	private Boolean schoolCheckedFlag;

	/**
	 * 出欠区分
	 */
	private String tardinessAbsenceType;

	/**
	 * 遅刻・早退の時間
	 */
	private Time tardyLeaveTime;

	/**
	 * 就職活動終了時間
	 */
	private Timestamp endTime;

	/**
	 * 備考
	 */
	private String remarks;

	/**
	 * 就職活動報告ID
	 */
	private String jobReportId;

	/**
	 * 報告内容
	 */
	private String reportContent;

	/**
	 * 結果
	 */
	private String result;

	/**
	 * 就職活動報告ID
	 */
	private String examReportId;

	/**
	 * 対応人数
	 */
	private Integer examOpponentCount;

	/**
	 * 対応役職
	 */
	private String examOpponentPosition;

	/**
	 * 試験回数
	 */
	private String examCount;

	/**
	 * 試験区分
	 */
	private String examType;

	/**
	 * 試験内容
	 */
	private String examContent;

	/**
	 * 感想
	 */
	private String impressions;

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
	private String attendanceNumber;

	/**
	 * 学籍番号
	 */
	private Integer studentId;

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

	public String getJobSearchId() {
		return jobSearchId;
	}

	public void setJobSearchId(String jobSearchId) {
		this.jobSearchId = jobSearchId;
	}

	public String getStudentUserId() {
		return studentUserId;
	}

	public void setStudentUserId(String studentUserId) {
		this.studentUserId = studentUserId;
	}

	public String getJobSearchStatus() {
		return jobSearchStatus;
	}

	public void setJobSearchStatus(String jobSearchStatus) {
		this.jobSearchStatus = jobSearchStatus;
	}

	public String getJobApplicationId() {
		return jobApplicationId;
	}

	public void setJobApplicationId(String jobApplicationId) {
		this.jobApplicationId = jobApplicationId;
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

	public String getLocationType() {
		return locationType;
	}

	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Boolean getSchoolCheckFlag() {
		return schoolCheckFlag;
	}

	public void setSchoolCheckFlag(Boolean schoolCheckFlag) {
		this.schoolCheckFlag = schoolCheckFlag;
	}

	public Boolean getSchoolCheckedFlag() {
		return schoolCheckedFlag;
	}

	public void setSchoolCheckedFlag(Boolean schoolCheckedFlag) {
		this.schoolCheckedFlag = schoolCheckedFlag;
	}

	public String getTardinessAbsenceType() {
		return tardinessAbsenceType;
	}

	public void setTardinessAbsenceType(String tardinessAbsenceType) {
		this.tardinessAbsenceType = tardinessAbsenceType;
	}

	public Time getTardyLeaveTime() {
		return tardyLeaveTime;
	}

	public void setTardyLeaveTime(Time tardyLeaveTime) {
		this.tardyLeaveTime = tardyLeaveTime;
	}

	public Timestamp getEndTime() {
		return endTime;
	}

	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getJobReportId() {
		return jobReportId;
	}

	public void setJobReportId(String jobReportId) {
		this.jobReportId = jobReportId;
	}

	public String getReportContent() {
		return reportContent;
	}

	public void setReportContent(String reportContent) {
		this.reportContent = reportContent;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getExamReportId() {
		return examReportId;
	}

	public void setExamReportId(String examReportId) {
		this.examReportId = examReportId;
	}

	public Integer getExamOpponentCount() {
		return examOpponentCount;
	}

	public void setExamOpponentCount(Integer examOpponentCount) {
		this.examOpponentCount = examOpponentCount;
	}

	public String getExamOpponentPosition() {
		return examOpponentPosition;
	}

	public void setExamOpponentPosition(String examOpponentPosition) {
		this.examOpponentPosition = examOpponentPosition;
	}

	public String getExamCount() {
		return examCount;
	}

	public void setExamCount(String examCount) {
		this.examCount = examCount;
	}

	public String getExamType() {
		return examType;
	}

	public void setExamType(String examType) {
		this.examType = examType;
	}

	public String getExamContent() {
		return examContent;
	}

	public void setExamContent(String examContent) {
		this.examContent = examContent;
	}

	public String getImpressions() {
		return impressions;
	}

	public void setImpressions(String impressions) {
		this.impressions = impressions;
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

	public String getAttendanceNumber() {
		return attendanceNumber;
	}

	public void setAttendanceNumber(String attendanceNumber) {
		this.attendanceNumber = attendanceNumber;
	}

	public Integer getStudentId() {
		return studentId;
	}

	public void setStudentId(Integer studentId) {
		this.studentId = studentId;
	}

	public Timestamp getMaxUpdatedAt() {
		return maxUpdatedAt;
	}

	public void setMaxUpdatedAt(Timestamp maxUpdatedAt) {
		this.maxUpdatedAt = maxUpdatedAt;
	}

	@Override
	public String toString() {
		return "JobSearchDashBoardDetailData [userName=" + userName + ", jobSearchId=" + jobSearchId
				+ ", studentUserId=" + studentUserId + ", jobSearchStatus=" + jobSearchStatus
				+ ", jobApplicationId=" + jobApplicationId + ", startTime=" + startTime + ", companyName="
				+ companyName + ", eventCategory=" + eventCategory + ", locationType=" + locationType
				+ ", location=" + location + ", schoolCheckFlag=" + schoolCheckFlag + ", schoolCheckedFlag="
				+ schoolCheckedFlag + ", tardinessAbsenceType=" + tardinessAbsenceType + ", tardyLeaveTime="
				+ tardyLeaveTime + ", endTime=" + endTime + ", remarks=" + remarks + ", jobReportId=" + jobReportId
				+ ", reportContent=" + reportContent + ", result=" + result + ", examReportId=" + examReportId
				+ ", examOpponentCount=" + examOpponentCount + ", examOpponentPosition=" + examOpponentPosition
				+ ", examCount=" + examCount + ", examType=" + examType + ", examContent=" + examContent
				+ ", impressions=" + impressions + ", department=" + department + ", grade=" + grade
				+ ", className=" + className + ", attendanceNumber=" + attendanceNumber + ", studentId=" + studentId
				+ ", maxUpdatedAt=" + maxUpdatedAt + "]";
	}

}
