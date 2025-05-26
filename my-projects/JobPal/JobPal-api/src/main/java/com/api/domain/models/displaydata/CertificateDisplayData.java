package com.api.domain.models.displaydata;

import java.util.List;

/**
 * ダッシュボード表示に必要な入力値を保持するクラスです。
 *
 * <p>
 * 各項目のデータ仕様は基本設計書を参照してください。
 * </p>
 */
public class CertificateDisplayData {

    // フィールド定義

    /**
     * 証明書発行ID
     */
    private String certificateIssueId;

    /**
     * ユーザID（メールアドレス）
     */
    private String userId;

    /**
     * ユーザ名
     */
    private String userName;

    /**
     * 学籍番号
     */
    private Integer schoolClassNumber;

    /**
     * 就職活動状態
     */
    private String certificateStateName;

    /**
     * 期日
     */
    private String date;

    /**
     * 証明書リスト
     */
    private List<CertificateDisplayItem> certificateList;

    /**
     * 合計金額
     */
    private Integer totalAmount;

    /**
     * 媒体名
     */
    private String mediaName;

    /**
     * 就職活動の状態の優先度
     */
    private Integer stateIdPriority;

    /**
     * 開始時間の優先度
     */
    private long startTimePriority;

    /**
     * 再通知フラグ
     */
    private boolean reNotifyFlag;

    // GetterとSetter

    public String getCertificateIssueId() {
        return certificateIssueId;
    }

    public void setCertificateIssueId(String certificateIssueId) {
        this.certificateIssueId = certificateIssueId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getSchoolClassNumber() {
        return schoolClassNumber;
    }

    public void setSchoolClassNumber(Integer schoolClassNumber) {
        this.schoolClassNumber = schoolClassNumber;
    }

    public String getCertificateStateName() {
        return certificateStateName;
    }

    public void setCertificateStateName(String certificateStateName) {
        this.certificateStateName = certificateStateName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<CertificateDisplayItem> getCertificateList() {
        return certificateList;
    }

    public void setCertificateList(List<CertificateDisplayItem> certificateList) {
        this.certificateList = certificateList;
    }

    public Integer getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Integer totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getMediaName() {
        return mediaName;
    }

    public void setMediaName(String mediaName) {
        this.mediaName = mediaName;
    }

    public Integer getStateIdPriority() {
        return stateIdPriority;
    }

    public void setStateIdPriority(Integer stateIdPriority) {
        this.stateIdPriority = stateIdPriority;
    }

    public long getStartTimePriority() {
        return startTimePriority;
    }

    public void setStartTimePriority(long startTimePriority) {
        this.startTimePriority = startTimePriority;
    }

    public boolean isReNotifyFlag() {
        return reNotifyFlag;
    }

    public void setReNotifyFlag(boolean reNotifyFlag) {
        this.reNotifyFlag = reNotifyFlag;
    }

    @Override
    public String toString() {
        return "CertificateDisplayData{" +
                "certificateIssueId='" + certificateIssueId + '\'' +
                ", userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", schoolClassNumber=" + schoolClassNumber +
                ", certificateStateName='" + certificateStateName + '\'' +
                ", date='" + date + '\'' +
                ", certificateList=" + certificateList +
                ", totalAmount=" + totalAmount +
                ", mediaName='" + mediaName + '\'' +
                ", stateIdPriority=" + stateIdPriority +
                ", startTimePriority=" + startTimePriority +
                ", reNotifyFlag=" + reNotifyFlag +
                '}';
    }

    // ビルダークラス

    public static class Builder {
        private String certificateIssueId;
        private String userId;
        private String userName;
        private Integer schoolClassNumber;
        private String certificateStateName;
        private String date;
        private List<CertificateDisplayItem> certificateList;
        private Integer totalAmount;
        private String mediaName;
        private Integer stateIdPriority;
        private long startTimePriority;
        private boolean reNotifyFlag;

        public Builder certificateIssueId(String certificateIssueId) {
            this.certificateIssueId = certificateIssueId;
            return this;
        }

        public Builder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder userName(String userName) {
            this.userName = userName;
            return this;
        }

        public Builder schoolClassNumber(Integer schoolClassNumber) {
            this.schoolClassNumber = schoolClassNumber;
            return this;
        }

        public Builder certificateStateName(String certificateStateName) {
            this.certificateStateName = certificateStateName;
            return this;
        }

        public Builder date(String date) {
            this.date = date;
            return this;
        }

        public Builder certificateList(List<CertificateDisplayItem> certificateList) {
            this.certificateList = certificateList;
            return this;
        }

        public Builder totalAmount(Integer totalAmount) {
            this.totalAmount = totalAmount;
            return this;
        }

        public Builder mediaName(String mediaName) {
            this.mediaName = mediaName;
            return this;
        }

        public Builder stateIdPriority(Integer stateIdPriority) {
            this.stateIdPriority = stateIdPriority;
            return this;
        }

        public Builder startTimePriority(long startTimePriority) {
            this.startTimePriority = startTimePriority;
            return this;
        }

        public Builder reNotifyFlag(boolean reNotifyFlag) {
            this.reNotifyFlag = reNotifyFlag;
            return this;
        }

        public CertificateDisplayData build() {
            CertificateDisplayData data = new CertificateDisplayData();
            data.setCertificateIssueId(certificateIssueId);
            data.setUserId(userId);
            data.setUserName(userName);
            data.setSchoolClassNumber(schoolClassNumber);
            data.setCertificateStateName(certificateStateName);
            data.setDate(date);
            data.setCertificateList(certificateList);
            data.setTotalAmount(totalAmount);
            data.setMediaName(mediaName);
            data.setStateIdPriority(stateIdPriority);
            data.setStartTimePriority(startTimePriority);
            data.setReNotifyFlag(reNotifyFlag);
            return data;
        }
    }

    /**
     * ビルダーインスタンスを返す静的メソッド
     *
     * @return Builderのインスタンス
     */
    public static Builder builder() {
        return new Builder();
    }
}
