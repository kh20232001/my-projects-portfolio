package com.api.domain.models.forms;

import jakarta.validation.constraints.NotBlank;

/**
 * 受験報告の入力情報を保持するクラスです。
 *
 * <p>
 * 各項目のデータ仕様は基本設計書を参照してください。
 *
 */
public class DetailFormForReport {

    /**
     * 就職活動ID
     *
     * 空白禁止
     */
    @NotBlank(message = "{require_check}")
    private String jobHuntId = "JS_2024_00002";

    /**
     * 報告内容
     *
     * 空白禁止
     */
    @NotBlank(message = "{require_check}")
    private String reportContent;

    /**
     * 結果
     *
     * 空白禁止
     */
    @NotBlank(message = "{require_check}")
    private String result;

    // getter
    public String getJobHuntId() {
        return jobHuntId;
    }

    public String getReportContent() {
        return reportContent;
    }

    public String getResult() {
        return result;
    }

    // setter
    public void setJobHuntId(String jobHuntId) {
        this.jobHuntId = jobHuntId;
    }

    public void setReportContent(String reportContent) {
        this.reportContent = reportContent;
    }

    public void setResult(String result) {
        this.result = result;
    }

}
