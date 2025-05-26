package com.api.jobpal.common.base;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class Util {

	// 状態を管理するBiMap（双方向）
	private static final BiMap<String, String> STATUS_MAP = HashBiMap.create();
	static {
		STATUS_MAP.put("VALID", "0");
		STATUS_MAP.put("LOCKED", "1");
		STATUS_MAP.put("GRADUATION", "2");
		STATUS_MAP.put("DELETE", "3");
		STATUS_MAP.put("INVALID", "4");
	}

	// ステータス名からコードを取得
	public static String getStatusCode(String status) {
		return STATUS_MAP.get(status);
	}

	// コードからステータス名を取得
	public static String getStatusName(String code) {
		return STATUS_MAP.inverse().get(code);
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

	// イベント区分の説明からコードを取得
	public static String getEventCategoryCode(String description) {
		return EVENT_CATEGORY_MAP.inverse().get(description);
	}

	// イベント区分コードから説明を取得
	public static String getEventCategoryDescription(String code) {
		return EVENT_CATEGORY_MAP.get(code);
	}

	// 場所区分のBiMap（双方向）
	private static final BiMap<String, String> LOCATION_TYPE_MAP = HashBiMap.create();
	static {
		LOCATION_TYPE_MAP.put("0", "札幌");
		LOCATION_TYPE_MAP.put("1", "東京");
		LOCATION_TYPE_MAP.put("2", "その他");
	}

	// 場所区分の説明からコードを取得
	public static String getLocationTypeCode(String description) {
		return LOCATION_TYPE_MAP.inverse().get(description);
	}

	// 場所区分コードから説明を取得
	public static String getLocationTypeDescription(String code) {
		return LOCATION_TYPE_MAP.get(code);
	}

	// 出欠区分のBiMap（双方向）
	private static final BiMap<String, String> TARDINESS_ABSENCE_MAP = HashBiMap.create();
	static {
		TARDINESS_ABSENCE_MAP.put("0", "なし");
		TARDINESS_ABSENCE_MAP.put("1", "遅刻");
		TARDINESS_ABSENCE_MAP.put("2", "早退");
		TARDINESS_ABSENCE_MAP.put("3", "欠席");
	}

	// 出欠区分の説明からコードを取得
	public static String getTardinessAbsenceCode(String description) {
		return TARDINESS_ABSENCE_MAP.inverse().get(description);
	}

	// 出欠区分コードから説明を取得
	public static String getTardinessAbsenceDescription(String code) {
		return TARDINESS_ABSENCE_MAP.get(code);
	}

	// 正引き用のMap
	private static final Map<String, String> JOB_STATE_SHORT_MAP = new HashMap<>();
	// 逆引き用のMap
	private static final Map<String, List<String>> JOB_STATE_SHORT_REVERSE_MAP = new HashMap<>();

	static {
		JOB_STATE_SHORT_MAP.put("00", "削除");
		JOB_STATE_SHORT_MAP.put("11", "承認待ち");
		JOB_STATE_SHORT_MAP.put("12", "承認待ち");
		JOB_STATE_SHORT_MAP.put("13", "差戻し");
		JOB_STATE_SHORT_MAP.put("21", "報告待ち");
		JOB_STATE_SHORT_MAP.put("22", "承認待ち");
		JOB_STATE_SHORT_MAP.put("23", "差戻し");
		JOB_STATE_SHORT_MAP.put("31", "報告待ち");
		JOB_STATE_SHORT_MAP.put("32", "承認待ち");
		JOB_STATE_SHORT_MAP.put("33", "完了");
		JOB_STATE_SHORT_MAP.put("34", "差戻し");

		// 逆引きマップの作成
		for (Map.Entry<String, String> entry : JOB_STATE_SHORT_MAP.entrySet()) {
			JOB_STATE_SHORT_REVERSE_MAP
					.computeIfAbsent(entry.getValue(), k -> new ArrayList<>())
					.add(entry.getKey());
		}
	}

	// キーから値を取得するメソッド
	public static String getJobStateShortName(String code) {
		return JOB_STATE_SHORT_MAP.getOrDefault(code, null);
	}

	// 値からキーを取得するメソッド
	public static List<String> getJobStateShortCode(String description) {
		return JOB_STATE_SHORT_REVERSE_MAP.getOrDefault(description, Collections.emptyList());
	}

	// 正引き用のMap
	private static final Map<String, String> JOB_STATE_MAP = new HashMap<>();
	// 逆引き用のMap
	private static final Map<String, List<String>> JOB_STATE_REVERSE_MAP = new HashMap<>();

	static {
		JOB_STATE_MAP.put("00", "削除");
		JOB_STATE_MAP.put("11", "担任承認待ち");
		JOB_STATE_MAP.put("12", "コース担当承認待ち");
		JOB_STATE_MAP.put("13", "申請差戻し");
		JOB_STATE_MAP.put("21", "受験報告待ち");
		JOB_STATE_MAP.put("22", "受験報告承認待ち");
		JOB_STATE_MAP.put("23", "受験報告差戻し");
		JOB_STATE_MAP.put("31", "報告待ち");
		JOB_STATE_MAP.put("32", "報告承認待ち");
		JOB_STATE_MAP.put("33", "完了");
		JOB_STATE_MAP.put("34", "報告差戻し");

		// 逆引きマップの作成
		for (Map.Entry<String, String> entry : JOB_STATE_MAP.entrySet()) {
			JOB_STATE_REVERSE_MAP
					.computeIfAbsent(entry.getValue(), k -> new ArrayList<>())
					.add(entry.getKey());
		}
	}

	// キーから値を取得するメソッド
	public static String getJobStateName(String code) {
		return JOB_STATE_MAP.getOrDefault(code, "不明");
	}

	// 値からキーを取得するメソッド
	public static List<String> getJobStateCode(String description) {
		return JOB_STATE_REVERSE_MAP.getOrDefault(description, Collections.emptyList());
	}

	// 結果コードと名称を管理するBiMap
	private static final BiMap<String, String> RESULT_STATUS_MAP = HashBiMap.create();

	static {
		RESULT_STATUS_MAP.put("0", "結果待ち");
		RESULT_STATUS_MAP.put("1", "辞退");
		RESULT_STATUS_MAP.put("2", "継続（合格）");
		RESULT_STATUS_MAP.put("3", "不合格");
		RESULT_STATUS_MAP.put("4", "内定");
		RESULT_STATUS_MAP.put("5", "内定承諾");
		RESULT_STATUS_MAP.put("6", "内定辞退");
		RESULT_STATUS_MAP.put("7", "その他");
	}

	// コードから結果を取得するメソッド
	public static String getResultStatus(String code) {
		return RESULT_STATUS_MAP.get(code);
	}

	// 結果からコードを取得するメソッド
	public static String getResultStatusCode(String status) {
		return RESULT_STATUS_MAP.inverse().get(status);
	}

	// イベント区分のBiMap（双方向）
	private static final BiMap<String, String> EXAM_CATEGORY_MAP = HashBiMap.create();
	static {
		EXAM_CATEGORY_MAP.put("0", "適性検査");
		EXAM_CATEGORY_MAP.put("1", "筆記・作文");
		EXAM_CATEGORY_MAP.put("2", "ディスカッション");
		EXAM_CATEGORY_MAP.put("3", "グループワーク");
		EXAM_CATEGORY_MAP.put("4", "個人面接");
		EXAM_CATEGORY_MAP.put("5", "集団面接");
		EXAM_CATEGORY_MAP.put("6", "その他");
	}

	// コードから試験区分を取得するメソッド
	public static String getExamCategoryStatus(String code) {
		return EXAM_CATEGORY_MAP.get(code);
	}

	// 試験区分からコードを取得するメソッド
	public static String getExamCategoryCode(String status) {
		return EXAM_CATEGORY_MAP.inverse().get(status);
	}

	// 証明書のステータスを管理するBiMap（双方向）
	private static final BiMap<String, String> CERTIFICATE_STATUS_MAP = HashBiMap.create();

	static {
		// ステータスコードと説明の対応を設定
		CERTIFICATE_STATUS_MAP.put("0", "担任承認待ち");
		CERTIFICATE_STATUS_MAP.put("1", "支払待ち");
		CERTIFICATE_STATUS_MAP.put("2", "差戻し");
		CERTIFICATE_STATUS_MAP.put("3", "発行待ち");
		CERTIFICATE_STATUS_MAP.put("4", "発行済み");
		CERTIFICATE_STATUS_MAP.put("5", "受取待ち");
		CERTIFICATE_STATUS_MAP.put("6", "完了");
	}

	/**
	 * ステータスコードから説明を取得します。
	 *
	 * @param code ステータスコード
	 * @return ステータス説明（対応がない場合は null）
	 */
	public static String getCertificateStatusDescription(String code) {
		return CERTIFICATE_STATUS_MAP.get(code);
	}

	/**
	 * ステータス説明からコードを取得します。
	 *
	 * @param description ステータス説明
	 * @return ステータスコード（対応がない場合は null）
	 */
	public static String getCertificateStatusCode(String description) {
		return CERTIFICATE_STATUS_MAP.inverse().get(description);
	}

	// 証明書のステータスを管理するBiMap（双方向）
	private static final BiMap<String, String> SHORT_CERTIFICATE_STATUS_MAP = HashBiMap.create();

	static {
		// ステータスコードと説明の対応を設定
		SHORT_CERTIFICATE_STATUS_MAP.put("0", "承認待ち");
		SHORT_CERTIFICATE_STATUS_MAP.put("1", "支払待ち");
		SHORT_CERTIFICATE_STATUS_MAP.put("2", "差戻し");
		SHORT_CERTIFICATE_STATUS_MAP.put("3", "発行待ち");
		SHORT_CERTIFICATE_STATUS_MAP.put("4", "発行済み");
		SHORT_CERTIFICATE_STATUS_MAP.put("5", "受取待ち");
		SHORT_CERTIFICATE_STATUS_MAP.put("6", "完了");
	}

	/**
	 * ステータスコードから説明を取得します。
	 *
	 * @param code ステータスコード
	 * @return ステータス説明（対応がない場合は null）
	 */
	public static String getShortCertificateStatusDescription(String code) {
		return SHORT_CERTIFICATE_STATUS_MAP.get(code);
	}

	/**
	 * ステータス説明からコードを取得します。
	 *
	 * @param description ステータス説明
	 * @return ステータスコード（対応がない場合は null）
	 */
	public static String getShortCertificateStatusCode(String description) {
		return SHORT_CERTIFICATE_STATUS_MAP.inverse().get(description);
	}

	// メディア区分のBiMap（双方向）
	private static final BiMap<String, String> MEDIA_TYPE_MAP = HashBiMap.create();

	static {
		MEDIA_TYPE_MAP.put("0", "原紙");
		MEDIA_TYPE_MAP.put("1", "電子");
		MEDIA_TYPE_MAP.put("2", "郵送");
	}

	// メディア区分の説明からコードを取得
	public static String getMediaTypeById(String id) {
		return MEDIA_TYPE_MAP.get(id);
	}

	// メディア区分コードから説明を取得
	public static String getIdByMediaType(String name) {
		return MEDIA_TYPE_MAP.inverse().get(name);
	}

	// 証明書区分のBiMap（双方向）
	private static final BiMap<String, String> CERTIFICATE_TYPE_MAP = HashBiMap.create();

	static {
		CERTIFICATE_TYPE_MAP.put("0", "在学証明書");
		CERTIFICATE_TYPE_MAP.put("1", "成績証明書");
		CERTIFICATE_TYPE_MAP.put("2", "卒業見込み証明書");
		CERTIFICATE_TYPE_MAP.put("3", "健康診断証明書");
	}

	// 証明書区分の説明からコードを取得
	public static String getCertificateNameById(String id) {
		return CERTIFICATE_TYPE_MAP.get(id);
	}

	// 証明書区分コードから説明を取得
	public static String getIdByCertificateName(String name) {
		return CERTIFICATE_TYPE_MAP.inverse().get(name);
	}

	// 証明書区分のBiMap（双方向）
	private static final BiMap<String, String> SHORT_CERTIFICATE_TYPE_MAP = HashBiMap.create();

	static {
		SHORT_CERTIFICATE_TYPE_MAP.put("0", "在学");
		SHORT_CERTIFICATE_TYPE_MAP.put("1", "成績");
		SHORT_CERTIFICATE_TYPE_MAP.put("2", "卒業");
		SHORT_CERTIFICATE_TYPE_MAP.put("3", "健康");
	}

	// 証明書区分の説明からコードを取得
	public static String getShortCertificateNameById(String id) {
		return SHORT_CERTIFICATE_TYPE_MAP.get(id);
	}

	// 証明書区分コードから説明を取得
	public static String getIdByShortCertificateName(String name) {
		return SHORT_CERTIFICATE_TYPE_MAP.inverse().get(name);
	}

	// 証明書区分のBiMap（双方向）
	private static final BiMap<String, String> USER_TYPE_MAP = HashBiMap.create();

	static {
		USER_TYPE_MAP.put("0", "学生");
		USER_TYPE_MAP.put("1", "担任");
		USER_TYPE_MAP.put("2", "管理者");
		USER_TYPE_MAP.put("3", "事務");
	}

	// 証明書区分の説明からコードを取得
	public static String getUserTypeNameById(String id) {
		return USER_TYPE_MAP.get(id);
	}

	// 証明書区分コードから説明を取得
	public static String getIdByUserTypeName(String name) {
		return USER_TYPE_MAP.inverse().get(name);
	}

	/**
	 * 部門、学年、クラス名を組み合わせて`userClass`文字列を作成します。
	 *
	 * @param department 部門名
	 * @param grade      学年
	 * @param className  クラス名
	 * @return 組み合わせたuserClassの文字列
	 */
	public static String formatUserClass(String department, Integer grade, String className) {
		if (department == null || grade == null || className == null) {
			return null;
		}
		return department + grade + className;
	}

	/**
	 * `attendanceNumber`をIntegerに変換します。
	 *
	 * @param attendanceNumber 出席番号を表す文字列
	 * @return `attendanceNumber`のInteger値
	 */
	public static Integer parseAttendanceNumber(String attendanceNumber) {
		if (attendanceNumber == null) {
			return null;
		}
		try {
			return Integer.valueOf(attendanceNumber);
		} catch (NumberFormatException e) {
			// ログやエラー処理を追加することも可能です
			return null;
		}
	}

	public static String formatToTwoDigits(Integer number) {
		if (number == null) {
			return null;
		}
		// "%02d"で2桁フォーマットを適用、一桁の場合は先頭に0を追加
		return String.format("%02d", number);
	}

	public static String formatToString(Integer number) {
		if (number == null) {
			return null;
		}
		// "%02d"で2桁フォーマットを適用、一桁の場合は先頭に0を追加
		return String.valueOf(number);
	}

	/**
	 * `studentId`が存在する場合に返します。
	 *
	 * @param studentId 学籍番号
	 * @return `studentId`が存在する場合はその値、なければnull
	 */
	public static Integer getValidStudentId(Integer studentId) {
		return studentId != null ? studentId : null;
	}

	/**
	 * `studentId`が存在する場合に返します。
	 *
	 * @param studentId 学籍番号
	 * @return `studentId`が存在する場合はその値、なければnull
	 */
	public static Integer getStudentId(String studentId) {
		return studentId != null ? Integer.valueOf(studentId) : null;
	}

	/**
	 * `createdByUserId`が存在するかを確認して返します。
	 *
	 * @param createdByUserId 作成ユーザーID
	 * @return `createdByUserId`が存在する場合はその値、なければnull
	 */
	public static String getValidCreateUserId(String createdByUserId) {
		return createdByUserId != null ? createdByUserId : null;
	}

	public static String[] splitString(String input) {
		if (input.length() >= 3 && Character.isAlphabetic(input.charAt(0)) && Character.isDigit(input.charAt(1))) {
			String headLetter = input.substring(0, 1); // 頭一文字
			String secondDigit = input.substring(1, 2); // 二文字目数字
			String remaining = input.substring(2); // 残りの文字列
			return new String[] { headLetter, secondDigit, remaining };
		}
		return null; // 入力が不正な場合はnullを返す
	}

	/**
	 * 指定された日付を任意のパターンでフォーマット
	 *
	 * @param date    Dateオブジェクト
	 * @param pattern フォーマットパターン
	 * @return フォーマット済みの文字列
	 */
	public static String formatDate(Date date, String pattern) {
		if (date == null) {
			return null;
		}
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		return formatter.format(date);
	}

	/**
	 * 遅刻早退時間を日付ありでフォーマット
	 *
	 * @param date Dateオブジェクト
	 * @param time 遅刻早退時間
	 * @return フォーマット済みの文字列
	 */
	public static String getTardyLeaveTime(Timestamp date, Time time) {
		// null チェックを追加
		if (time == null) {
			return null;
		}

		// Calendar を使用して日付部分を保持しつつ時間を置き換え
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(date.getTime());
		LocalTime localTime = time.toLocalTime();
		calendar.set(Calendar.HOUR_OF_DAY, localTime.getHour());
		calendar.set(Calendar.MINUTE, localTime.getMinute());
		calendar.set(Calendar.SECOND, localTime.getSecond());

		// 置き換え後の Timestamp
		Timestamp modifiedTimestamp = new Timestamp(calendar.getTimeInMillis());

		return formatDate(modifiedTimestamp, "MM/dd HH:mm");
	}
}
