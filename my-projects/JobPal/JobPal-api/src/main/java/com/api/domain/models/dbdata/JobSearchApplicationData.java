package com.api.domain.models.dbdata;

import java.sql.Time;
import java.sql.Timestamp;

/**
 * 1件分の就職活動申請の情報を表すモデルクラスです。
 *
 * <p>
 * 各項目のデータ構造については、データベース定義を参照してください。
 * </p>
 */
public class JobSearchApplicationData {

	// 就職活動申請に割り振られるID
	private String jobApplicationId;

	// イベントや活動の開始日時
	private Timestamp startTime;

	// 会社名
	private String companyName;

	// イベントの種類を示すコード
	private String eventCategory;

	// 場所の種類を示すコード
	private String locationType;

	// イベントや活動の場所の住所
	private String location;

	// 学校とりまとめ名簿の登録の要否
	private Boolean schoolCheckFlag;

	// 学校名簿に担任が登録済みか
	private Boolean schoolCheckedFlag;

	// 出欠を示すコード
	private String tardinessAbsenceType;

	// 遅刻または早退の時間
	private Time tardyLeaveTime;

	// イベントや活動の終了日時
	private Timestamp endTime;

	// 備考
	private String remarks;

	// レコードの作成日時
	private Timestamp createdAt;

	// レコードの更新日時
	private Timestamp updatedAt;

	// 就職活動に割り振られるID
	private String jobSearchId;

	public JobSearchApplicationData() {

	}

	/**
	* 全フィールドを初期化するコンストラクタ。
	*/
	public JobSearchApplicationData(String jobApplicationId, Timestamp startTime, String companyName,
			String eventCategory, String locationType, String location,
			Boolean schoolCheckFlag, Boolean schoolCheckedFlag, String tardinessAbsenceType,
			Time tardyLeaveTime, Timestamp endTime, String remarks,
			Timestamp createdAt, Timestamp updatedAt, String jobSearchId) {
		this.jobApplicationId = jobApplicationId;
		this.startTime = startTime;
		this.companyName = companyName;
		this.eventCategory = eventCategory;
		this.locationType = locationType;
		this.location = location;
		this.schoolCheckFlag = schoolCheckFlag;
		this.schoolCheckedFlag = schoolCheckedFlag;
		this.tardinessAbsenceType = tardinessAbsenceType;
		this.tardyLeaveTime = tardyLeaveTime;
		this.endTime = endTime;
		this.remarks = remarks;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.jobSearchId = jobSearchId;
	}

	// GetterおよびSetterメソッド
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

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public Timestamp getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getJobSearchId() {
		return jobSearchId;
	}

	public void setJobSearchId(String jobSearchId) {
		this.jobSearchId = jobSearchId;
	}

	// ビルダーパターンの実装
	public static class Builder {
		private String jobApplicationId;
		private Timestamp startTime;
		private String companyName;
		private String eventCategory;
		private String locationType;
		private String location;
		private Boolean schoolCheckFlag;
		private Boolean schoolCheckedFlag;
		private String tardinessAbsenceType;
		private Time tardyLeaveTime;
		private Timestamp endTime;
		private String remarks;
		private Timestamp createdAt;
		private Timestamp updatedAt;
		private String jobSearchId;

		public Builder setJobApplicationId(String jobApplicationId) {
			this.jobApplicationId = jobApplicationId;
			return this;
		}

		public Builder setStartTime(Timestamp startTime) {
			this.startTime = startTime;
			return this;
		}

		public Builder setCompanyName(String companyName) {
			this.companyName = companyName;
			return this;
		}

		public Builder setEventCategory(String eventCategory) {
			this.eventCategory = eventCategory;
			return this;
		}

		public Builder setLocationType(String locationType) {
			this.locationType = locationType;
			return this;
		}

		public Builder setLocation(String location) {
			this.location = location;
			return this;
		}

		public Builder setSchoolCheckFlag(Boolean schoolCheckFlag) {
			this.schoolCheckFlag = schoolCheckFlag;
			return this;
		}

		public Builder setSchoolCheckedFlag(Boolean schoolCheckedFlag) {
			this.schoolCheckedFlag = schoolCheckedFlag;
			return this;
		}

		public Builder setTardinessAbsenceType(String tardinessAbsenceType) {
			this.tardinessAbsenceType = tardinessAbsenceType;
			return this;
		}

		public Builder setTardyLeaveTime(Time tardyLeaveTime) {
			this.tardyLeaveTime = tardyLeaveTime;
			return this;
		}

		public Builder setEndTime(Timestamp endTime) {
			this.endTime = endTime;
			return this;
		}

		public Builder setRemarks(String remarks) {
			this.remarks = remarks;
			return this;
		}

		public Builder setCreatedAt(Timestamp createdAt) {
			this.createdAt = createdAt;
			return this;
		}

		public Builder setUpdatedAt(Timestamp updatedAt) {
			this.updatedAt = updatedAt;
			return this;
		}

		public Builder setJobSearchId(String jobSearchId) {
			this.jobSearchId = jobSearchId;
			return this;
		}

		public JobSearchApplicationData build() {
			return new JobSearchApplicationData(
					jobApplicationId, startTime, companyName, eventCategory, locationType, location,
					schoolCheckFlag, schoolCheckedFlag, tardinessAbsenceType, tardyLeaveTime,
					endTime, remarks, createdAt, updatedAt, jobSearchId);
		}

	}

	// toStringメソッドの追加
	@Override
	public String toString() {
		return "JobSearchApplicationData{" +
				"jobApplicationId='" + jobApplicationId + '\'' +
				", startTime=" + startTime +
				", companyName='" + companyName + '\'' +
				", eventCategory='" + eventCategory + '\'' +
				", locationType='" + locationType + '\'' +
				", location='" + location + '\'' +
				", schoolCheckFlag=" + schoolCheckFlag +
				", schoolCheckedFlag=" + schoolCheckedFlag +
				", tardinessAbsenceType='" + tardinessAbsenceType + '\'' +
				", tardyLeaveTime=" + tardyLeaveTime +
				", endTime=" + endTime +
				", remarks='" + remarks + '\'' +
				", createdAt=" + createdAt +
				", updatedAt=" + updatedAt +
				", jobSearchId='" + jobSearchId + '\'' +
				'}';
	}
}
