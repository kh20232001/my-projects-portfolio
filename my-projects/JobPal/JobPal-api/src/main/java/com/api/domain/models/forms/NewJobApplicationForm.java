package com.api.domain.models.forms;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 新規申請書作成情報を保持するクラスです。
 *
 * <p>
 * 各項目のデータ仕様は基本設計書を参照してください。
 */
public class NewJobApplicationForm {

    /**
     * ユーザID（メールアドレス）
     * 必須項目、メールアドレス形式である必要があります。
     */
    @NotBlank(message = "{require_check}")
    @Email(message = "{email_check}")
    private String userId;

    /**
     * イベント区分
     * 必須項目、1文字の数字（0-9）のみ。
     * - 0：説明会_単
     * - 1：説明会_合
     * - 2：試験_面接
     * - 3：試験_適正
     * - 4：試験_他
     * - 5：インターン
     * - 6：セミナー
     * - 7：内定式
     * - 8：研修
     * - 9：他
     */
    @NotBlank(message = "{require_check}")
    // @Pattern(regexp = "^[0-9]$", message = "{event_category_check}")
    private String eventCategory;

    /**
     * 場所区分
     * 必須項目、1文字の数字（0-2）のみ。
     * - 0：札幌
     * - 1：東京
     * - 2：その他
     */
    @NotBlank(message = "{require_check}")
    // @Pattern(regexp = "^[0-2]$", message = "{location_type_check}")
    private String locationType;

    /**
     * 学校チェックの有無
     * 必須項目、trueなら登録が必要、falseなら不要。
     */
    @NotNull(message = "{require_check}")
    private Boolean schoolCheck;

    /**
     * 会社名
     * 必須項目、最大100文字の文字列。
     */
    @NotBlank(message = "{require_check}")
    @Size(max = 100, message = "{company_size_check}")
    private String company;

    /**
     * 場所
     * 必須項目、最大200文字の文字列。
     */
    @NotBlank(message = "{require_check}")
    @Size(max = 200, message = "{location_size_check}")
    private String location;

    /**
     * 開始日時
     * 必須項目、タイムスタンプ形式。
     */
    @NotBlank(message = "{require_check}")
    private String startTime;

    /**
     * 終了日時
     * 任意項目、タイムスタンプ形式。
     */
    @NotBlank(message = "{require_check}")
    private String finishTime;

    /**
     * 遅刻早退時間
     * 任意項目、時刻形式。
     */
    private String tardyLeaveTime;

    /**
     * 出欠区分
     * 必須項目、1文字の数字（0-3）のみ。
     * - 0：なし
     * - 1：遅刻
     * - 2：早退
     * - 3：欠席
     */
    @NotBlank(message = "{require_check}")
    // @Pattern(regexp = "^[0-3]$", message = "{tardiness_absence_type_check}")
    private String tardinessAbsenceType;

    /**
     * 備考
     * 任意項目、文字数制限なし。
     */
    private String remarks;

    /**
     * 就職活動ID
     *
     * 空白禁止
     */
    private String jobHuntId;

    // getter
    public String getJobHuntId() {
        return jobHuntId;
    }

    public String getUserId() {
        return userId;
    }

    public String getEventCategory() {
        return eventCategory;
    }

    public String getLocationType() {
        return locationType;
    }

    public Boolean isSchoolCheck() {
        return schoolCheck;
    }

    public String getCompany() {
        return company;
    }

    public String getLocation() {
        return location;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public String getTardinessAbsenceType() {
        return tardinessAbsenceType;
    }

    public String getTardyLeaveTime() {
        return tardyLeaveTime;
    }

    public String getRemarks() {
        return remarks;
    }

    // setter
    public void setJobHuntId(String jobHuntId) {
        this.jobHuntId = jobHuntId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setEventCategory(String eventCategory) {
        this.eventCategory = eventCategory;
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    public void setSchoolCheck(Boolean schoolCheck) {
        this.schoolCheck = schoolCheck;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public void setTardyLeaveTime(String tardyLeaveTime) {
        this.tardyLeaveTime = tardyLeaveTime;
    }

    public void setTardinessAbsenceType(String tardinessAbsenceType) {
        this.tardinessAbsenceType = tardinessAbsenceType;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

}
