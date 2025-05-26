package com.api.domain.models.forms;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

/**
 * ユーザ作成の入力値を保持するクラスです。
 *
 * <p>
 * 各項目のデータ仕様は基本設計書を参照してください。
 */
public class UserForm {

    /**
     * ユーザID（メールアドレス）
     *
     * 空白禁止、メールアドレス形式
     */
    @NotBlank(message = "{require_check}")
    @Email(message = "{email_check}")
    private String userId;

    /**
     * パスワード
     *
     * 空白禁止、6～100文字
     */
    @NotBlank(message = "{require_check}")
    @Length(min = 6, max = 100, message = "{length_check}")
    private String password;

    /**
     * ユーザ名
     *
     * 空白禁止、2～50文字
     */
    @NotBlank(message = "{require_check}")
    @Length(min = 2, max = 50, message = "{length_check}")
    private String userName;

    /**
     * クラス
     *
     * 空白禁止
     */
    @NotBlank(message = "{require_check}")
    private String userClass;

    /**
     * 出席番号
     *
     * 空白禁止
     */
    @NotNull(message = "{require_check}")
    private Integer classNumber;

    /**
     * 学校番号
     *
     * 空白禁止
     */
    @NotNull(message = "{require_check}")
    private Integer schoolNumber;

    /**
     * 権限
     *
     * 空白禁止、0～3のいずれか
     * 0：一般、1：管理者、2：システム管理者、3: 事務
     */
    @NotBlank(message = "{require_check}")
    @Pattern(regexp = "^(0|1|2|3)$")
    private String grant;

    /**
     * 有効性
     *
     * 空白禁止、VALID, LOCKED, GRADUATION, DELETE, INVALIDのいずれか
     * VALID：有効、LOCKED：ロック中、GRADUATION：卒業、DELETE：削除、INVALID：無効
     */
    @Pattern(regexp = "^(VALID|LOCKED|GRADUATION|DELETE|INVALID)$")
    private String status;

    /**
     * 作成者
     *
     * 空白禁止、6～100文字
     */
    @NotBlank(message = "{require_check}")
    @Length(min = 6, max = 100, message = "{length_check}")
    private String createUserId;

    // getter
    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserClass() {
        return userClass;
    }

    public Integer getClassNumber() {
        return classNumber;
    }

    public Integer getSchoolNumber() {
        return schoolNumber;
    }

    public String getGrant() {
        return grant;
    }

    public String getStatus() {
        return status;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    // setter
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserClass(String userClass) {
        this.userClass = userClass;
    }

    public void setClassNumber(Integer classNumber) {
        this.classNumber = classNumber;
    }

    public void setSchoolNumber(Integer schoolNumber) {
        this.schoolNumber = schoolNumber;
    }

    public void setGrant(String grant) {
        this.grant = grant;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }
}
