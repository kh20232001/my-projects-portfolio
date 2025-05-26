package com.api.domain.models.displaydata;

import java.util.Date;

/**
 * 1件分の就職活動ダッシュボードの詳細情報を表すクラスです。
 *
 * <p>
 * 各項目のデータ構造については、データベース定義をご覧ください。
 * </p>
 */
public class DetailDisplayData {

	/**
	 * 就職活動ID
	 */
	private String jobHuntId;

	/**
	 * ユーザ名
	 */
	private String userName;

	/**
	 * 就職活動状態
	 */
	private String jobStateId;

	/**
	 * クラス
	 */
	private String userClass;

	/**
	 * 出席番号
	 */
	private String classNumber;

	/**
	 * 学籍番号
	 */
	private Integer schoolNumber;

	/**
	 * 会社名
	 */
	private String company;

	/**
	 * イベント区分
	 */
	private String eventCategory;

	/**
	 * 場所
	 */
	private String location;

	/**
	 * 場所区分
	 */
	private String locationType;

	/**
	 * 学校申込
	 */
	private Boolean schoolCheck;

	/**
	 * 名簿チェック
	 */
	private Boolean schoolCheckedFlag;

	/**
	 * 開始時間
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
	 * 出欠区分
	 */
	private String tardinessAbsenceType;

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
	private String supportedCnt;

	/**
	 * 対応職
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

	/**
	 * 遅刻欠席時間
	 */
	private String tardyLeaveTime;

	/**
	 * 備考
	 */
	private String remarks;

	/**
	 * AI予測結果
	 */
	private String predResult;

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

	public String getClassNumber() {
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

	public String getLocation() {
		return location;
	}

	public String getLocationType() {
		return locationType;
	}

	public Boolean getSchoolCheck() {
		return schoolCheck;
	}

	public Boolean getSchoolCheckedFlag() {
		return schoolCheckedFlag;
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

	public String getTardinessAbsenceType() {
		return tardinessAbsenceType;
	}

	public String getReportContent() {
		return reportContent;
	}

	public String getResult() {
		return result;
	}

	public String getSupportedCnt() {
		return supportedCnt;
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

	public String getTardyLeaveTime() {
		return tardyLeaveTime;
	}

	public String getRemarks() {
		return remarks;
	}

	public String getPredResult() {
		return predResult;
	}

	// builder
	public static class Builder {
		private String jobHuntId;
		private String userName;
		private String jobStateId;
		private String userClass;
		private String classNumber;
		private Integer schoolNumber;
		private String company;
		private String eventCategory;
		private String location;
		private String locationType;
		private Boolean schoolCheck;
		private Boolean schoolCheckedFlag;
		private String startTime;
		private String finishTime;
		private Date updateTime;
		private String tardinessAbsenceType;
		private String reportContent;
		private String result;
		private String supportedCnt;
		private String jobTitle;
		private Integer examCategory;
		private String examContent;
		private String ecaminationContent;
		private String thoughts;
		private String tardyLeaveTime;
		private String remarks;
		private String predResult;

		public Builder setJobHuntId(String jobHuntId) {
			this.jobHuntId = jobHuntId;
			return this;
		}

		public Builder setUserName(String userName) {
			this.userName = userName;
			return this;
		}

		public Builder setJobStateId(String jobStateId) {
			this.jobStateId = jobStateId;
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

		public Builder setSchoolNumber(Integer schoolNumber) {
			this.schoolNumber = schoolNumber;
			return this;
		}

		public Builder setCompany(String company) {
			this.company = company;
			return this;
		}

		public Builder setEventCategory(String eventCategory) {
			this.eventCategory = eventCategory;
			return this;
		}

		public Builder setLocation(String location) {
			this.location = location;
			return this;
		}

		public Builder setLocationType(String locationType) {
			this.locationType = locationType;
			return this;
		}

		public Builder setSchoolCheck(Boolean schoolCheck) {
			this.schoolCheck = schoolCheck;
			return this;
		}

		public Builder setSchoolCheckedFlag(Boolean schoolCheckedFlag) {
			this.schoolCheckedFlag = schoolCheckedFlag;
			return this;
		}

		public Builder setStartTime(String startTime) {
			this.startTime = startTime;
			return this;
		}

		public Builder setFinishTime(String finishTime) {
			this.finishTime = finishTime;
			return this;
		}

		public Builder setUpdateTime(Date updateTime) {
			this.updateTime = updateTime;
			return this;
		}

		public Builder setTardinessAbsenceType(String tardinessAbsenceType) {
			this.tardinessAbsenceType = tardinessAbsenceType;
			return this;
		}

		public Builder setReportContent(String reportContent) {
			this.reportContent = reportContent;
			return this;
		}

		public Builder setResult(String result) {
			this.result = result;
			return this;
		}

		public Builder setSupportedCnt(String supportedCnt) {
			this.supportedCnt = supportedCnt;
			return this;
		}

		public Builder setJobTitle(String jobTitle) {
			this.jobTitle = jobTitle;
			return this;
		}

		public Builder setExamCategory(Integer examCategory) {
			this.examCategory = examCategory;
			return this;
		}

		public Builder setExamContent(String examContent) {
			this.examContent = examContent;
			return this;
		}

		public Builder setEcaminationContent(String ecaminationContent) {
			this.ecaminationContent = ecaminationContent;
			return this;
		}

		public Builder setThoughts(String thoughts) {
			this.thoughts = thoughts;
			return this;
		}

		public Builder setTardyLeaveTime(String tardyLeaveTime) {
			this.tardyLeaveTime = tardyLeaveTime;
			return this;
		}

		public Builder setRemarks(String remarks) {
			this.remarks = remarks;
			return this;
		}

		public Builder setPredResult(String predResult) {
			this.predResult = predResult;
			return this;
		}

		/**
		 * ビルダーから{@code DetailDisplayData}のインスタンスを生成します。
		 *
		 * @return 完成した{@code DetailDisplayData}オブジェクト
		 */
		public DetailDisplayData build() {
			DetailDisplayData data = new DetailDisplayData();
			data.jobHuntId = this.jobHuntId;
			data.userName = this.userName;
			data.jobStateId = this.jobStateId;
			data.userClass = this.userClass;
			data.classNumber = this.classNumber;
			data.schoolNumber = this.schoolNumber;
			data.company = this.company;
			data.eventCategory = this.eventCategory;
			data.location = this.location;
			data.locationType = this.locationType;
			data.schoolCheck = this.schoolCheck;
			data.schoolCheckedFlag = this.schoolCheckedFlag;
			data.startTime = this.startTime;
			data.finishTime = this.finishTime;
			data.updateTime = this.updateTime;
			data.tardinessAbsenceType = this.tardinessAbsenceType;
			data.reportContent = this.reportContent;
			data.result = this.result;
			data.supportedCnt = this.supportedCnt;
			data.jobTitle = this.jobTitle;
			data.examCategory = this.examCategory;
			data.examContent = this.examContent;
			data.ecaminationContent = this.ecaminationContent;
			data.thoughts = this.thoughts;
			data.tardyLeaveTime = this.tardyLeaveTime;
			data.remarks = this.remarks;
			data.predResult = this.predResult;
			return data;
		}
	}

	/**
	 * {@code DetailDisplayData} クラスのビルダーを提供します。
	 */
	public static Builder builder() {
		return new Builder();
	}

	@Override
	public String toString() {
		return "DetailDisplayData{" +
				"jobHuntId='" + jobHuntId + '\'' +
				", userName='" + userName + '\'' +
				", jobStateId='" + jobStateId + '\'' +
				", userClass='" + userClass + '\'' +
				", classNumber=" + classNumber +
				", schoolNumber=" + schoolNumber +
				", company='" + company + '\'' +
				", eventCategory='" + eventCategory + '\'' +
				", location='" + location + '\'' +
				", locationType='" + locationType + '\'' +
				", schoolCheck=" + schoolCheck +
				", schoolCheckedFlag=" + schoolCheckedFlag +
				", startTime=" + startTime +
				", finishTime=" + finishTime +
				", updateTime=" + updateTime +
				", tardinessAbsenceType='" + tardinessAbsenceType + '\'' +
				", reportContent='" + reportContent + '\'' +
				", result='" + result + '\'' +
				", supportedCnt='" + supportedCnt + '\'' +
				", jobTitle='" + jobTitle + '\'' +
				", examCategory='" + examCategory + '\'' +
				", examContent='" + examContent + '\'' +
				", ecaminationContent='" + ecaminationContent + '\'' +
				", thoughts='" + thoughts + '\'' +
				", tardyLeaveTime=" + tardyLeaveTime +
				", remarks='" + remarks + '\'' +
				", predResult='" + predResult + '\'' +
				'}';
	}
}
