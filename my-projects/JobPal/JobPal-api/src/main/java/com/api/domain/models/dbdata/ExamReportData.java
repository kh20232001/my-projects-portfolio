package com.api.domain.models.dbdata;

import java.sql.Timestamp;

/**
 * 1件分の受験報告の情報を表すモデルクラスです。
 *
 * <p>
 * 各項目は受験報告に関連するデータを保持します。<br>
 * データベース定義を参照して、詳細な仕様を確認してください。
 * </p>
 */
public class ExamReportData {

	// 受験報告に割り振られるID
	private String examReportId;

	// 受験をした際の人数
	private Integer examOpponentCount;

	// 受験した際の相手の役職
	private String examOpponentPosition;

	// 受験した試験の回数
	private String examCount;

	// 受験した試験の区分コード
	private String examType;

	// 受験した試験の内容
	private String examContent;

	// 受験した試験の感想
	private String impressions;

	// レコードが作成された日時
	private Timestamp createdAt;

	// レコードが最後に更新された日時
	private Timestamp updatedAt;

	// 就職活動に割り振られるID
	private String jobSearchId;

	/**
	 * デフォルトコンストラクタ。
	 */
	public ExamReportData() {
	}

	public ExamReportData(String examReportId, Integer examOpponentCount, String examOpponentPosition, String examCount,
			String examType, String examContent, String impressions, Timestamp createdAt, Timestamp updatedAt,
			String jobSearchId) {
		super();
		this.examReportId = examReportId;
		this.examOpponentCount = examOpponentCount;
		this.examOpponentPosition = examOpponentPosition;
		this.examCount = examCount;
		this.examType = examType;
		this.examContent = examContent;
		this.impressions = impressions;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.jobSearchId = jobSearchId;
	}

	// GetterおよびSetterメソッド
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

	// ビルダークラス
	public static class Builder {
		private String examReportId;
		private Integer examOpponentCount;
		private String examOpponentPosition;
		private String examCount;
		private String examType;
		private String examContent;
		private String impressions;
		private Timestamp createdAt;
		private Timestamp updatedAt;
		private String jobSearchId;

		public Builder setExamReportId(String examReportId) {
			this.examReportId = examReportId;
			return this;
		}

		public Builder setExamOpponentCount(Integer examOpponentCount) {
			this.examOpponentCount = examOpponentCount;
			return this;
		}

		public Builder setExamOpponentPosition(String examOpponentPosition) {
			this.examOpponentPosition = examOpponentPosition;
			return this;
		}

		public Builder setExamCount(String examCount) {
			this.examCount = examCount;
			return this;
		}

		public Builder setExamType(String examType) {
			this.examType = examType;
			return this;
		}

		public Builder setExamContent(String examContent) {
			this.examContent = examContent;
			return this;
		}

		public Builder setImpressions(String impressions) {
			this.impressions = impressions;
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

		public ExamReportData build() {
			return new ExamReportData(examReportId, examOpponentCount, examOpponentPosition, examCount, examType,
					examContent, impressions, createdAt, updatedAt, jobSearchId);
		}
	}

	@Override
	public String toString() {
		return "ExamReportData{" +
				"examReportId='" + examReportId + '\'' +
				", examOpponentCount=" + examOpponentCount +
				", examOpponentPosition='" + examOpponentPosition + '\'' +
				", examCount='" + examCount + '\'' +
				", examType='" + examType + '\'' +
				", examContent='" + examContent + '\'' +
				", impressions='" + impressions + '\'' +
				", createdAt=" + createdAt +
				", updatedAt=" + updatedAt +
				", jobSearchId='" + jobSearchId + '\'' +
				'}';
	}
}
