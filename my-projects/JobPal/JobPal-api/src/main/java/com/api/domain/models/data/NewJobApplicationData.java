package com.api.domain.models.data;

import java.sql.Timestamp;

/**
 * 新規申請書作成情報を保持するクラスです。
 *
 * <p>
 * 各項目のデータ仕様は基本設計書を参照してください。
 * </p>
 */
public class NewJobApplicationData {

    /**
     * 開始日時
     * 必須項目、タイムスタンプ形式。
     */
    private Timestamp startTime;

    /**
     * 会社名
     * 必須項目、最大100文字の文字列。
     */
    private String companyName;

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
    private String eventCategory;

    /**
     * 場所区分
     * 必須項目、1文字の数字（0-2）のみ。
     * - 0：札幌
     * - 1：東京
     * - 2：その他
     */
    private String locationType;

    /**
     * 場所
     * 必須項目、最大200文字の文字列。
     */
    private String location;

    /**
     * 学校チェックの有無
     * 必須項目、trueなら登録が必要、falseなら不要。
     */
    private Boolean schoolCheckFlag;

    /**
     * 名簿入力チェックの有無
     * 任意項目、trueならチェック済み、falseなら未チェック。
     */
    private Boolean schoolCheckedFlag;

    /**
     * 出欠区分
     * 必須項目、1文字の数字（0-3）のみ。
     * - 0：なし
     * - 1：遅刻
     * - 2：早退
     * - 3：欠席
     */
    private String tardinessAbsenceType;

    /**
     * 遅刻早退時間
     * 任意項目、時刻形式。
     */
    private Timestamp tardyLeaveTime;

    /**
     * 終了日時
     * 任意項目、タイムスタンプ形式。
     */
    private Timestamp endTime;

    /**
     * 備考
     * 任意項目、文字数制限なし。
     */
    private String remarks;

    /**
     * 作成時刻
     * 必須項目、タイムスタンプ形式。
     */
    private Timestamp createdAt;

    /**
     * 更新時刻
     * 必須項目、タイムスタンプ形式。
     */
    private Timestamp updatedAt;

    // getter
    public Timestamp getStartTime() {
        return startTime;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getEventCategory() {
        return eventCategory;
    }

    public String getLocationType() {
        return locationType;
    }

    public String getLocation() {
        return location;
    }

    public Boolean getSchoolCheckFlag() {
        return schoolCheckFlag;
    }

    public Boolean getSchoolCheckedFlag() {
        return schoolCheckedFlag;
    }

    public String getTardinessAbsenceType() {
        return tardinessAbsenceType;
    }

    public Timestamp getTardyLeaveTime() {
        return tardyLeaveTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public String getRemarks() {
        return remarks;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    // setter
    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setEventCategory(String eventCategory) {
        this.eventCategory = eventCategory;
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setSchoolCheckFlag(Boolean schoolCheckFlag) {
        this.schoolCheckFlag = schoolCheckFlag;
    }

    public void setSchoolCheckedFlag(Boolean schoolCheckedFlag) {
        this.schoolCheckedFlag = schoolCheckedFlag;
    }

    public void setTardinessAbsenceType(String tardinessAbsenceType) {
        this.tardinessAbsenceType = tardinessAbsenceType;
    }

    public void setTardyLeaveTime(Timestamp tardyLeaveTime) {
        this.tardyLeaveTime = tardyLeaveTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public void setCreatedAt(Timestamp createdAt) {

        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    // プライベートなコンストラクタ（ビルダーからのみインスタンス化可能）
    private NewJobApplicationData(Builder builder) {
        this.startTime = builder.startTime;
        this.companyName = builder.companyName;
        this.eventCategory = builder.eventCategory;
        this.locationType = builder.locationType;
        this.location = builder.location;
        this.schoolCheckFlag = builder.schoolCheckFlag;
        this.schoolCheckedFlag = builder.schoolCheckedFlag;
        this.tardinessAbsenceType = builder.tardinessAbsenceType;
        this.tardyLeaveTime = builder.tardyLeaveTime;
        this.endTime = builder.endTime;
        this.remarks = builder.remarks;
        this.createdAt = builder.createdAt;
        this.updatedAt = builder.updatedAt;
    }

    // ビルダークラス
    public static class Builder {
        private Timestamp startTime;
        private String companyName;
        private String eventCategory;
        private String locationType;
        private String location;
        private Boolean schoolCheckFlag;
        private Boolean schoolCheckedFlag;
        private String tardinessAbsenceType;
        private Timestamp tardyLeaveTime;
        private Timestamp endTime;
        private String remarks;
        private Timestamp createdAt;
        private Timestamp updatedAt;

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

        public Builder setTardyLeaveTime(Timestamp tardyLeaveTime) {
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

        public NewJobApplicationData build() {
            return new NewJobApplicationData(this);
        }
    }

    @Override
    public String toString() {
        return "NewJobApplicationData{" +
                "startTime=" + startTime +
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
                ", createdAt=" + createdAt + '\'' +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
