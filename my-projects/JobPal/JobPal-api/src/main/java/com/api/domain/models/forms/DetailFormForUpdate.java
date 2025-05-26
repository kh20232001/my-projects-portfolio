package com.api.domain.models.forms;

import io.micrometer.common.lang.NonNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 受験報告の入力情報を保持するクラスです。
 *
 * <p>
 * 各項目のデータ仕様は基本設計書を参照してください。
 *
 */
public class DetailFormForUpdate {

    /**
     * 就職活動ID
     *
     * 空白禁止
     */
    @NotBlank(message = "{require_check}")
    private String jobHuntId;

    /**
     * 面接官の人数
     *
     * 空白禁止
     */
    @NotNull
    private Integer supportedCnt;

    /**
     * 面接官の職業
     *
     * 空白禁止
     */
    @NotBlank(message = "{require_check}")
    private String jobTitle;

    /**
     * 試験区分
     *
     * 空白禁止
     */
    @NonNull
    private Integer examCategory;

    /**
     * 試験内容ID
     *
     * 空白禁止
     */
    @NotBlank(message = "{require_check}")
    private String examContentId;

    /**
     * 受験内容
     *
     * 空白禁止
     */
    @NotBlank(message = "{require_check}")
    private String examinationContent;

    /**
     * 感想
     *
     * 空白禁止
     */
    @NotBlank(message = "{require_check}")
    private String thoughts;

    // getter
    public String getJobHuntId() {
        return jobHuntId;
    }

    public Integer getSupportedCnt() {
        return supportedCnt;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public Integer getExamCategory() {
        return examCategory;
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

    public void setSupportedCnt(Integer supportedCnt) {
        this.supportedCnt = supportedCnt;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public void setExamCategory(Integer examCategory) {
        this.examCategory = examCategory;
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
}
