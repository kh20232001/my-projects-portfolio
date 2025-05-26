package com.api.domain.models.entities;

import java.util.ArrayList;
import java.util.List;

import com.api.domain.models.data.ZipCodeData;

/**
 * 郵便番号検索のレスポンスを保持するエンティティクラスです。
 *
 * <p>
 * 各項目のデータ仕様については、APIの仕様を参照してください。
 * 1つの郵便番号に複数の住所が紐づくこともあるため、リスト構造となっています。
 * </p>
 */
public class ZipCodeEntity {

    /**
     * ステータス
     */
    private String status;

    /**
     * メッセージ
     */
    private String message;

    /**
     * 郵便番号のリスト
     */
    private List<ZipCodeData> list = new ArrayList<>();

    /**
     * エラーメッセージ(表示用)
     */
    private String errorMessage;

    // Getters
    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public List<ZipCodeData> getList() {
        return list;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    // Setters
    public void setStatus(String status) {
        this.status = status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setList(List<ZipCodeData> list) {
        this.list = list;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return "ZipCodeEntity{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", list=" + list +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
