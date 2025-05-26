package com.api.domain.models.dbdata;

import java.sql.Timestamp;

/**
 * 1件分の就職活動報告情報を表すモデルクラスです。
 *
 * <p>
 * 各項目は、就職活動に関連する報告内容を保持します。
 * データベース定義を参照して、項目の詳細仕様を確認してください。
 * </p>
 */
public class JobSearchReportData {

	// 就職活動報告に割り振られるID。13文字の固定長。
	private String jobReportId;

	// 報告内容。文字数制限なし。
	private String reportContent;

	// 就職活動の結果を示すコード。1文字の固定長。
	private String result;

	// レコードの作成日時。
	private Timestamp createdAt;

	// レコードの更新日時。
	private Timestamp updatedAt;

	// 就職活動に割り振られるID。13文字の固定長。
	private String jobSearchId;

	// コンストラクタ
	public JobSearchReportData(String jobReportId, String reportContent, String result, Timestamp createdAt,
			Timestamp updatedAt, String jobSearchId) {
		this.jobReportId = jobReportId;
		this.reportContent = reportContent;
		this.result = result;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.jobSearchId = jobSearchId;
	}

	public JobSearchReportData() {
	}

	// ビルダー
	public static class Builder {
		private String jobReportId;
		private String reportContent;
		private String result;
		private Timestamp createdAt;
		private Timestamp updatedAt;
		private String jobSearchId;

		public Builder setJobReportId(String jobReportId) {
			this.jobReportId = jobReportId;
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

		public JobSearchReportData build() {
			return new JobSearchReportData(jobReportId, reportContent, result, createdAt, updatedAt, jobSearchId);
		}
	}

	// ビルダーインスタンスを取得するための静的メソッド
	public static Builder builder() {
		return new Builder();
	}

	// GetterおよびSetterメソッド
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

	@Override
	public String toString() {
		return "JobSearchReportData [jobReportId=" + jobReportId + ", reportContent=" + reportContent + ", result="
				+ result + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + ", jobSearchId=" + jobSearchId
				+ "]";
	}
}
