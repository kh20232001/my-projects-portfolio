package com.api.domain.models.displaydata;

/**
 * CSVに出力する就職活動申請のIDです。
 *
 * <p>
 * 各項目のデータ構造については、データベース定義をご覧ください。
 */
public class JobSearchData {
	/**
	 * 申請ID
	 */
	private String jobHuntId;

	// getter
	public String getJobHuntId() {
		return jobHuntId;
	}

	// setter
	public void setJobHuntId(String jobHuntId) {
		this.jobHuntId = jobHuntId;
	}
}
