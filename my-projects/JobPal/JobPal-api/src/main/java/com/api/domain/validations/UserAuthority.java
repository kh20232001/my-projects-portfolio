package com.api.domain.validations;

import java.util.Arrays;
import java.util.Optional;

/**
 * ユーザ権限を表す列挙型
 *
 */
public enum UserAuthority {
    /**
     * 学生権限
     */
    STUDENT("0", "学生"), // 学生

    /**
     * 担任権限
     */
    TEACHER("1", "担任"), // 担任

    /**
     * 管理者権限
     */
    ADMIN("2", "管理者"), // 管理者

    /**
     * 事務権限
     */
    OFFICE("3", "事務"); // 事務

    /**
     * 権限の日本語名
     */
    private final String label;

    /**
     * DBなどで扱う権限名
     */
    private final String grantId;

    UserAuthority(String grantId, String label) {
        this.label = label;
        this.grantId = grantId;
    }

    // getter
    public String getLabel() {
        return label;
    }

    public String getGrantId() {
        return grantId;
    }

    /**
     * 権限名からオブジェクトの逆引きを行います
     *
     * @param grantId 権限名
     * @return 権限が保持しているかもしれないOptional
     */
    public static Optional<UserAuthority> getByGrantId(String grantId) {
        return Arrays.stream(values()).filter(e -> e.getGrantId().equals(grantId)).findFirst();
    }
}
