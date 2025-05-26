package com.api.domain.models.displaydata;

import java.util.List;

/**
 * ダッシュボード表示に必要な証明書詳細データを保持するクラスです。
 *
 * <p>
 * 各項目のデータ仕様は基本設計書を参照してください。
 * </p>
 *
 * <p>
 * 本クラスは Builder パターンを利用してインスタンス化することを推奨します。
 * Builder パターンを利用することで、コードの可読性が向上し、柔軟にオブジェクトを生成できます。
 * </p>
 */
public class CertificateDisplayDetail {

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
     * ユーザクラス
     */
    private String userClass;

    /**
     * 出席番号
     */
    private Integer classNumber;

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
     * 担当事務名
     */
    private String officeUserName;

    /**
     * 宛先姓
     */
    private String lastName;

    /**
     * 宛先名
     */
    private String firstName;

    /**
     * 宛先カナ姓
     */
    private String lastNameKana;

    /**
     * 宛先カナ名
     */
    private String firstNameKana;

    /**
     * 証明書リスト
     */
    private List<CertificateDisplayItem> certificateList;

    /**
     * 媒体名
     */
    private String mediaName;

    /**
     * 宛先郵便番号
     */
    private String zipCode;

    /**
     * 宛先住所
     */
    private String address;

    /**
     * 宛先住所（番地以降）
     */
    private String afterAddress;

    // Getters and Setters

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

    public String getUserClass() {
        return userClass;
    }

    public void setUserClass(String userClass) {
        this.userClass = userClass;
    }

    public Integer getClassNumber() {
        return classNumber;
    }

    public void setClassNumber(Integer classNumber) {
        this.classNumber = classNumber;
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

    public String getOfficeUserName() {
        return officeUserName;
    }

    public void setOfficeUserName(String officeUserName) {
        this.officeUserName = officeUserName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastNameKana() {
        return lastNameKana;
    }

    public void setLastNameKana(String lastNameKana) {
        this.lastNameKana = lastNameKana;
    }

    public String getFirstNameKana() {
        return firstNameKana;
    }

    public void setFirstNameKana(String firstNameKana) {
        this.firstNameKana = firstNameKana;
    }

    public List<CertificateDisplayItem> getCertificateList() {
        return certificateList;
    }

    public void setCertificateList(List<CertificateDisplayItem> certificateList) {
        this.certificateList = certificateList;
    }

    public String getMediaName() {
        return mediaName;
    }

    public void setMediaName(String mediaName) {
        this.mediaName = mediaName;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAfterAddress() {
        return afterAddress;
    }

    public void setAfterAddress(String afterAddress) {
        this.afterAddress = afterAddress;
    }

    // Builderクラス

    public static class Builder {
        private String certificateIssueId;
        private String userId;
        private String userClass;
        private Integer classNumber;
        private String userName;
        private Integer schoolClassNumber;
        private String certificateStateName;
        private String officeUserName;
        private String lastName;
        private String firstName;
        private String lastNameKana;
        private String firstNameKana;
        private List<CertificateDisplayItem> certificateList;
        private String mediaName;
        private String zipCode;
        private String address;
        private String afterAddress;

        public Builder certificateIssueId(String certificateIssueId) {
            this.certificateIssueId = certificateIssueId;
            return this;
        }

        public Builder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder userClass(String userClass) {
            this.userClass = userClass;
            return this;
        }

        public Builder classNumber(Integer classNumber) {
            this.classNumber = classNumber;
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

        public Builder officeUserName(String officeUserName) {
            this.officeUserName = officeUserName;
            return this;
        }

        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder lastNameKana(String lastNameKana) {
            this.lastNameKana = lastNameKana;
            return this;
        }

        public Builder firstNameKana(String firstNameKana) {
            this.firstNameKana = firstNameKana;
            return this;
        }

        public Builder certificateList(List<CertificateDisplayItem> certificateList) {
            this.certificateList = certificateList;
            return this;
        }

        public Builder mediaName(String mediaName) {
            this.mediaName = mediaName;
            return this;
        }

        public Builder zipCode(String zipCode) {
            this.zipCode = zipCode;
            return this;
        }

        public Builder address(String address) {
            this.address = address;
            return this;
        }

        public Builder afterAddress(String afterAddress) {
            this.afterAddress = afterAddress;
            return this;
        }

        public CertificateDisplayDetail build() {
            CertificateDisplayDetail detail = new CertificateDisplayDetail();
            detail.setCertificateIssueId(this.certificateIssueId);
            detail.setUserId(this.userId);
            detail.setUserClass(this.userClass);
            detail.setClassNumber(this.classNumber);
            detail.setUserName(this.userName);
            detail.setSchoolClassNumber(this.schoolClassNumber);
            detail.setCertificateStateName(this.certificateStateName);
            detail.setOfficeUserName(this.officeUserName);
            detail.setLastName(this.lastName);
            detail.setFirstName(this.firstName);
            detail.setLastNameKana(this.lastNameKana);
            detail.setFirstNameKana(this.firstNameKana);
            detail.setCertificateList(this.certificateList);
            detail.setMediaName(this.mediaName);
            detail.setZipCode(this.zipCode);
            detail.setAddress(this.address);
            detail.setAfterAddress(this.afterAddress);
            return detail;
        }
    }

    @Override
    public String toString() {
        return "CertificateDisplayDetail{" +
                "certificateIssueId='" + certificateIssueId + '\'' +
                ", userId='" + userId + '\'' +
                ", userClass='" + userClass + '\'' +
                ", classNumber=" + classNumber +
                ", userName='" + userName + '\'' +
                ", schoolClassNumber=" + schoolClassNumber +
                ", certificateStateName='" + certificateStateName + '\'' +
                ", officeUserName='" + officeUserName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastNameKana='" + lastNameKana + '\'' +
                ", firstNameKana='" + firstNameKana + '\'' +
                ", certificateList=" + certificateList +
                ", mediaName='" + mediaName + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", address='" + address + '\'' +
                ", afterAddress='" + afterAddress + '\'' +
                '}';
    }
}
