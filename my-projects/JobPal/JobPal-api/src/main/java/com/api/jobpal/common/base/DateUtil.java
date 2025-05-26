package com.api.jobpal.common.base;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class DateUtil {

  // 日本のタイムゾーン
  private static final ZoneId JAPAN_ZONE = ZoneId.of("Asia/Tokyo");
  // 日付フォーマット
  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  // 状態を管理するBiMap（双方向）
    private static final BiMap<String, String> STATUS_MAP = HashBiMap.create();
    static {
        STATUS_MAP.put("VALID", "0");
        STATUS_MAP.put("LOCKED", "1");
        STATUS_MAP.put("GRADUATION", "2");
        STATUS_MAP.put("DELETE", "3");
        STATUS_MAP.put("INVALID", "4");
    }

    // イベント区分のBiMap（双方向）
    private static final BiMap<String, String> EVENT_CATEGORY_MAP = HashBiMap.create();
    static {
        EVENT_CATEGORY_MAP.put("0", "説明会_単");
        EVENT_CATEGORY_MAP.put("1", "説明会_合");
        EVENT_CATEGORY_MAP.put("2", "試験_面接");
        EVENT_CATEGORY_MAP.put("3", "試験_適正");
        EVENT_CATEGORY_MAP.put("4", "試験_他");
        EVENT_CATEGORY_MAP.put("5", "インターン");
        EVENT_CATEGORY_MAP.put("6", "セミナー");
        EVENT_CATEGORY_MAP.put("7", "内定式");
        EVENT_CATEGORY_MAP.put("8", "研修");
        EVENT_CATEGORY_MAP.put("9", "他");
    }

    // 場所区分のBiMap（双方向）
    private static final BiMap<String, String> LOCATION_TYPE_MAP = HashBiMap.create();
    static {
        LOCATION_TYPE_MAP.put("0", "札幌");
        LOCATION_TYPE_MAP.put("1", "東京");
        LOCATION_TYPE_MAP.put("2", "その他");
    }

    // 出欠区分のBiMap（双方向）
    private static final BiMap<String, String> TARDINESS_ABSENCE_MAP = HashBiMap.create();
    static {
        TARDINESS_ABSENCE_MAP.put("0", "なし");
        TARDINESS_ABSENCE_MAP.put("1", "遅刻");
        TARDINESS_ABSENCE_MAP.put("2", "早退");
        TARDINESS_ABSENCE_MAP.put("3", "欠席");
    }

    // ステータス名からコードを取得
    public static String getStatusCode(String status) {
        return STATUS_MAP.get(status);
    }

    // コードからステータス名を取得
    public static String getStatusName(String code) {
        return STATUS_MAP.inverse().get(code);
    }

    // イベント区分の説明からコードを取得
    public static String getEventCategoryCode(String description) {
        return EVENT_CATEGORY_MAP.inverse().get(description);
    }

    // イベント区分コードから説明を取得
    public static String getEventCategoryDescription(String code) {
        return EVENT_CATEGORY_MAP.get(code);
    }

    // 場所区分の説明からコードを取得
    public static String getLocationTypeCode(String description) {
        return LOCATION_TYPE_MAP.inverse().get(description);
    }

    // 場所区分コードから説明を取得
    public static String getLocationTypeDescription(String code) {
        return LOCATION_TYPE_MAP.get(code);
    }

    // 出欠区分の説明からコードを取得
    public static String getTardinessAbsenceCode(String description) {
        return TARDINESS_ABSENCE_MAP.inverse().get(description);
    }

    // 出欠区分コードから説明を取得
    public static String getTardinessAbsenceDescription(String code) {
        return TARDINESS_ABSENCE_MAP.get(code);
    }

  /**
   * 現在の日本時間をフォーマットしてTimestampに変換するメソッド
   *
   * @return 日本時間の現在時刻を表すTimestamp
   */
  public static Timestamp getCurrentTimestampInJapan() {
    // 現在の日本時間を取得
    LocalDateTime nowInJapan = LocalDateTime.now(JAPAN_ZONE);

    // フォーマットした日時をTimestampに変換
    return Timestamp.valueOf(nowInJapan.format(FORMATTER));
  }
}
