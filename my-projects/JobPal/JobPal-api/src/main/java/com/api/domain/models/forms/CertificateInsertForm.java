package com.api.domain.models.forms;

import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 証明書挿入フォームの入力データを保持するクラスです。
 *
 * <p>
 * 各項目のデータ仕様は基本設計書を参照してください。
 * </p>
 */
public class CertificateInsertForm {

  /**
   * ユーザID（メールアドレス）
   * <p>
   * 入力例: user@example.com
   * </p>
   */
  @NotBlank(message = "{require_check}")
  @Email(message = "{email_check}")
  private String userId;

  /**
   * 姓
   * <p>
   * 入力例: 山田
   * </p>
   */
  private String lastName;

  /**
   * 名
   * <p>
   * 入力例: 太郎
   * </p>
   */
  private String firstName;

  /**
   * 姓（カナ）
   * <p>
   * 入力例: ヤマダ
   * </p>
   */
  private String lastNameKana;

  /**
   * 名（カナ）
   * <p>
   * 入力例: タロウ
   * </p>
   */
  private String firstNameKana;

  /**
   * 証明書リスト
   * <p>
   * 複数の証明書詳細データを含みます。
   * </p>
   */
  @NotNull(message = "{require_check}")
  private List<CertificateDetailDataForm> certificateList;

  /**
   * メディア名
   * <p>
   * 入力例: 電子, 紙
   * </p>
   */
  @NotBlank(message = "{require_check}")
  private String mediaName;

  /**
   * 郵便番号
   * <p>
   * 入力例: 123-4567
   * </p>
   */
  private String zipCode;

  /**
   * 住所
   * <p>
   * 入力例: 東京都千代田区千代田1-1
   * </p>
   */
  private String address;

  /**
   * 住所（番地以降）
   * <p>
   * 入力例: 1階101号室
   * </p>
   */
  private String afterAddress;

  /**
   * 証明書発行ID
   * 空白禁止
   */
  private String certificateIssueId;

  // getter
  public String getUserId() {
    return userId;
  }

  public String getLastName() {
    return lastName;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastNameKana() {
    return lastNameKana;
  }

  public String getFirstNameKana() {
    return firstNameKana;
  }

  public List<CertificateDetailDataForm> getCertificateList() {
    return certificateList;
  }

  public String getMediaName() {
    return mediaName;
  }

  public String getZipCode() {
    return zipCode;
  }

  public String getAddress() {
    return address;
  }

  public String getAfterAddress() {
    return afterAddress;
  }

  public String getCertificateIssueId() {
    return certificateIssueId;
  }

  // setter
  public void setUserId(String userId) {
    this.userId = userId;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public void setLastNameKana(String lastNameKana) {
    this.lastNameKana = lastNameKana;
  }

  public void setFirstNameKana(String firstNameKana) {
    this.firstNameKana = firstNameKana;
  }

  public void setCertificateList(List<CertificateDetailDataForm> certificateList) {
    this.certificateList = certificateList;
  }

  public void setMediaName(String mediaName) {
    this.mediaName = mediaName;
  }

  public void setZipCode(String zipCode) {
    this.zipCode = zipCode;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public void setAfterAddress(String afterAddress) {
    this.afterAddress = afterAddress;
  }

  public void setCertificateIssueId(String certificateIssueId) {
    this.certificateIssueId = certificateIssueId;
  }

  // builder
  public static class Builder {
    private String userId;
    private String lastName;
    private String firstName;
    private String lastNameKana;
    private String firstNameKana;
    private List<CertificateDetailDataForm> certificateList;
    private String mediaName;
    private String zipCode;
    private String address;
    private String afterAddress;
    private String certificateIssueId;

    public Builder userId(String userId) {
      this.userId = userId;
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

    public Builder certificateList(List<CertificateDetailDataForm> certificateList) {
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

    public Builder certificateIssueId(String certificateIssueId) {
      this.certificateIssueId = certificateIssueId;
      return this;
    }

    public CertificateInsertForm build() {
      CertificateInsertForm form = new CertificateInsertForm();
      form.setUserId(this.userId);
      form.setLastName(this.lastName);
      form.setFirstName(this.firstName);
      form.setLastNameKana(this.lastNameKana);
      form.setFirstNameKana(this.firstNameKana);
      form.setCertificateList(this.certificateList);
      form.setMediaName(this.mediaName);
      form.setZipCode(this.zipCode);
      form.setAddress(this.address);
      form.setAfterAddress(this.afterAddress);
      form.setCertificateIssueId(this.certificateIssueId);
      return form;
    }
  }

  @Override
  public String toString() {
    return "CertificateInsertForm{" +
        "userId='" + userId + '\'' +
        ", lastName='" + lastName + '\'' +
        ", firstName='" + firstName + '\'' +
        ", lastNameKana='" + lastNameKana + '\'' +
        ", firstNameKana='" + firstNameKana + '\'' +
        ", certificateList=" + certificateList +
        ", mediaName='" + mediaName + '\'' +
        ", zipCode='" + zipCode + '\'' +
        ", address='" + address + '\'' +
        ", afterAddress='" + afterAddress + '\'' +
        ", certificateIssueId='" + certificateIssueId + '\'' +
        '}';
  }
}
