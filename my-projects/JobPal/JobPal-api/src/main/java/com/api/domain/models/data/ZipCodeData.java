package com.api.domain.models.data;

/**
 * 1件分の郵便番号情報を管理する。
 *
 * <p>
 * レスポンスフィールドのresults内のデータを管理します。
 *
 */
public class ZipCodeData {

    /** 郵便番号 */
    private String zipcode;

    /** 都道府県コード */
    private String prefcode;

    /** 都道府県 */
    private String address1;

    /** 市区町村名 */
    private String address2;

    /** 町域名 */
    private String address3;

    /** 都道府県名カナ */
    private String kana1;

    /** 市区町村名カナ */
    private String kana2;

    /** 町域名カナ */
    private String kana3;

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getPrefcode() {
        return prefcode;
    }

    public void setPrefcode(String prefcode) {
        this.prefcode = prefcode;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getAddress3() {
        return address3;
    }

    public void setAddress3(String address3) {
        this.address3 = address3;
    }

    public String getKana1() {
        return kana1;
    }

    public void setKana1(String kana1) {
        this.kana1 = kana1;
    }

    public String getKana2() {
        return kana2;
    }

    public void setKana2(String kana2) {
        this.kana2 = kana2;
    }

    public String getKana3() {
        return kana3;
    }

    public void setKana3(String kana3) {
        this.kana3 = kana3;
    }

    @Override
    public String toString() {
        return "ZipCodeData [zipcode=" + zipcode + ", prefcode=" + prefcode + ", address1=" + address1 + ", address2="
                + address2 + ", address3=" + address3 + ", kana1=" + kana1 + ", kana2=" + kana2 + ", kana3=" + kana3
                + "]";
    }

}
