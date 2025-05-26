package com.api.domain.models.displaydata;

/**
 * 1件分の就職活動ダッシュボードの情報です。
 *
 * <p>
 * 各項目のデータ構造については、データベース定義をご覧ください。
 */
public class DashBoardDisplayData {
	/**
	 * 申請ID
	 */
	private String jobHuntId;

	/**
	 * ユーザIDを識別する数字
	 * 主キー、必須入力
	 */
	private String userId;

	/**
	 * ユーザ名
	 */
	private String userName;

	/**
	 * クラス+出席番号
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
	 * イベント区分の表示優先度
	 */
	private Integer eventCategoryPriority;

	/**
	 * 就職活動の状態の表示優先度
	 */
	private Integer stateIdPriority;

	/**
	 * 開始時間の表示優先度
	 */
	private Long startTimePriority;

	/**
	 * 再通知フラグ
	 */
	private boolean reNotifyFlag;

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

	public String getJobHuntId() {
		return jobHuntId;
	}

	public String getUserName() {
		return userName;
	}

	public Integer getEventCategoryPriority() {
		return eventCategoryPriority;
	}

	public Integer getStateIdPriority() {
		return stateIdPriority;
	}

	public Long getStartTimePriority() {
		return startTimePriority;
	}

	public boolean isReNotifyFlag() {
		return reNotifyFlag;
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

	public void setJobHuntId(String jobHuntId) {
		this.jobHuntId = jobHuntId;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setEventCategoryPriority(Integer eventCategoryPriority) {
		this.eventCategoryPriority = eventCategoryPriority;
	}

	public void setStateIdPriority(Integer stateIdPriority) {
		this.stateIdPriority = stateIdPriority;
	}

	public void setStartTimePriority(Long startTimePriority) {
		this.startTimePriority = startTimePriority;
	}

	public void setReNotifyFlag(boolean reNotifyFlag) {
		this.reNotifyFlag = reNotifyFlag;
	}

	// privateコンストラクタ
	private DashBoardDisplayData(Builder builder) {
		this.jobHuntId = builder.jobHuntId;
		this.userId = builder.userId;
		this.userName = builder.userName;
		this.schoolClassNumber = builder.schoolClassNumber;
		this.jobStateId = builder.jobStateId;
		this.company = builder.company;
		this.eventCategory = builder.eventCategory;
		this.result = builder.result;
		this.schoolCheck = builder.schoolCheck;
		this.date = builder.date;
		this.startTime = builder.startTime;
		this.finishTime = builder.finishTime;
		this.eventCategoryPriority = builder.eventCategoryPriority;
		this.stateIdPriority = builder.stateIdPriority;
		this.startTimePriority = builder.startTimePriority;
		this.reNotifyFlag = builder.reNotifyFlag;
	}

	// ビルダークラス
	public static class Builder {
		private String jobHuntId;
		private String userId;
		private String userName;
		private String schoolClassNumber;
		private String jobStateId;
		private String company;
		private String eventCategory;
		private String result;
		private Boolean schoolCheck;
		private String date;
		private String startTime;
		private String finishTime;
		private Integer eventCategoryPriority;
		private Integer stateIdPriority;
		private Long startTimePriority;
		private boolean reNotifyFlag;

		// Setterメソッド
		public Builder setJobHuntId(String jobHuntId) {
			this.jobHuntId = jobHuntId;
			return this;
		}

		public Builder setUserId(String userId) {
			this.userId = userId;
			return this;
		}

		public Builder setUserName(String userName) {
			this.userName = userName;
			return this;
		}

		public Builder setSchoolClassNumber(String schoolClassNumber) {
			this.schoolClassNumber = schoolClassNumber;
			return this;
		}

		public Builder setJobStateId(String jobStateId) {
			this.jobStateId = jobStateId;
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

		public Builder setResult(String result) {
			this.result = result;
			return this;
		}

		public Builder setSchoolCheck(Boolean schoolCheck) {
			this.schoolCheck = schoolCheck;
			return this;
		}

		public Builder setDate(String date) {
			this.date = date;
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

		public Builder setEventCategoryPriority(Integer eventCategoryPriority) {
			this.eventCategoryPriority = eventCategoryPriority;
			return this;
		}

		public Builder setStateIdPriority(Integer stateIdPriority) {
			this.stateIdPriority = stateIdPriority;
			return this;
		}

		public Builder setStartTimePriority(Long startTimePriority) {
			this.startTimePriority = startTimePriority;
			return this;
		}

		public Builder setReNotifyFlag(boolean reNotifyFlag) {
			this.reNotifyFlag = reNotifyFlag;
			return this;
		}

		// インスタンス生成メソッド
		public DashBoardDisplayData build() {
			return new DashBoardDisplayData(this);
		}

	}

	@Override
	public String toString() {
		return "DashBoardDisplayData [userId=" + userId + ", schoolClassNumber=" + schoolClassNumber + ", jobStateId="
				+ jobStateId + ", company=" + company + ", eventCategory=" + eventCategory + ", result=" + result
				+ ", schoolCheck=" + schoolCheck + ", date=" + date + ", startTime=" + startTime + ", finishTime="
				+ finishTime
				+ ", jobHuntId=" + jobHuntId + ", userName=" + userName
				+ ", eventCategoryPriority=" + eventCategoryPriority + ", stateIdPriority=" + stateIdPriority
				+ ", startTimePriority=" + startTimePriority + ", reNotifyFlag=" + reNotifyFlag + "]";
	}
}
