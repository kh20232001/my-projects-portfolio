package com.api.domain.models.data;

/**
 * 1件分の受験報告の情報です。
 *
 * <p>
 * 各項目のデータ構造については、データベース定義をご覧ください。
 */
public class JobExamData {

	public JobExamData(String jobHuntId, int cnt, String jobTitle, int eventCategory, String examContentId,
			String examinationContent, String thoughts) {
		super();
		this.jobHuntId = jobHuntId;
		this.cnt = cnt;
		this.jobTitle = jobTitle;
		this.eventCategory = eventCategory;
		this.examContentId = examContentId;
		this.examinationContent = examinationContent;
		this.thoughts = thoughts;
	}

	public JobExamData() {

	}
	// 申請ID
	private String jobHuntId;

	// 件数
	private int cnt;

	// 職種
	private String jobTitle;

	// イベント区分
	private int eventCategory;

	// 受験内容ID
	private String examContentId;

	// 受験内容
	private String examinationContent;

	// 感想
	private String thoughts;

	// getter
	public String getJobHuntId() {
		return jobHuntId;
	}

	public int getCnt() {
		return cnt;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public int getEventCategory() {
		return eventCategory;
	}

	public String getExamContentId() {
		return examContentId;
	}

	public String getExaminationContent() {
		return examinationContent;
	}

	public String getThoughts() {
		return thoughts;
	}

	// setter
	public void setJobHuntId(String jobHuntId) {
		this.jobHuntId = jobHuntId;
	}

	public void setCnt(int cnt) {
		this.cnt = cnt;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public void setEventCategory(int eventCategory) {
		this.eventCategory = eventCategory;
	}

	public void setExamContentId(String examContentId) {
		this.examContentId = examContentId;
	}

	public void setExaminationContent(String examinationContent) {
		this.examinationContent = examinationContent;
	}

	public void setThoughts(String thoughts) {
		this.thoughts = thoughts;
	}

	@Override
	public String toString() {
		return "JobExamData [jobHuntId=" + jobHuntId + ", cnt=" + cnt + ", jobTitle=" + jobTitle + ", eventCategory="
				+ eventCategory + ", examContentId=" + examContentId + ", examinationContent=" + examinationContent
				+ ", thoughts=" + thoughts + "]";
	}

}
