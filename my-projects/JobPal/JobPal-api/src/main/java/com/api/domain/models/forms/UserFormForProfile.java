package com.api.domain.models.forms;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;

/**
 * 一般権限のユーザ更新の入力値を保持するクラスです。
 *
 * <p>
 * 各項目のデータ仕様は基本設計書を参照してください。
 */
public class UserFormForProfile {

    /**
     * パスワード(平文)
     */
    // 入力チェックなし
    private String password;

    /**
     * 名前
     *
     * 空白禁止、2～50文字
     */
    @NotBlank(message = "{require_check}")
    @Length(min = 2, max = 50, message = "{length_check}")
    private String userName;

    // getter
    public String getPassword() {
        return password;
    }

    public String getUserName() {
        return userName;
    }

    // setter
    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}
