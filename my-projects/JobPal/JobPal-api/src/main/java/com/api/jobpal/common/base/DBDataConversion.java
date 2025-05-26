package com.api.jobpal.common.base;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Year;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DBDataConversion {
	/**
	 * SQL JOB_SEARCH_APPLICATION_TにJOB_SEARCH_IDの有無チェック
	 */
	private static final String SQL_SELECT_JOB_SEARCH_ID_CHECK_JOB_SEARCH_APPLICATION = "SELECT "
			+ "job_search_id "
			+ "FROM "
			+ "JOB_SEARCH_APPLICATION_T "
			+ "WHERE "
			+ "job_search_id = :jobSearchId";
	/**
	 * SQL JOB_SEARCH_REPORT_TにJOB_SEARCH_IDの有無チェック
	 */
	private static final String SQL_SELECT_JOB_SEARCH_ID_CHECK_JOB_SEARCH_REPORT = "SELECT "
			+ "job_search_id "
			+ "FROM "
			+ "JOB_SEARCH_REPORT_T "
			+ "WHERE "
			+ "job_search_id = :jobSearchId";
	/**
	 * SQL EXAM_REPORT_TにJOB_SEARCH_IDの有無チェック
	 */
	private static final String SQL_SELECT_JOB_SEARCH_ID_CHECK_EXAM_REPORT = "SELECT "
			+ "job_search_id "
			+ "FROM "
			+ "EXAM_REPORT_T "
			+ "WHERE "
			+ "job_search_id = :jobSearchId";
	/**
	 * テーブルのマップ
	 */
	private static final Map<String, String> TABLE_MAP = new HashMap<>();

	static {
		TABLE_MAP.put("JA", SQL_SELECT_JOB_SEARCH_ID_CHECK_JOB_SEARCH_APPLICATION);
		TABLE_MAP.put("JR", SQL_SELECT_JOB_SEARCH_ID_CHECK_JOB_SEARCH_REPORT);
		TABLE_MAP.put("ER", SQL_SELECT_JOB_SEARCH_ID_CHECK_EXAM_REPORT);
	}

	@Autowired
	private NamedParameterJdbcTemplate jdbc;

	public boolean grantConvert(String grant) {

		if (grant.equals("VALID")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * SQL文に必要なパラメータをバインドするためのMapを提供します。
	 *
	 * @param query      SQL文
	 * @param inputValue パラメータの可変長引数
	 * @return バインド用のMap型のparams
	 */
	public Map<String, Object> mapInputValues(String query, Object... inputValue) {

		// クエリのパラメータを設定するマップ
		Map<String, Object> params = new HashMap<String, Object>();

		// 検索する文字を定義
		String searchStr = ":";
		String endStr = " ";

		// 必要な文字の先頭のindexを取得
		int result = query.indexOf(searchStr) + 1;

		// indexからクエリ名を取得
		String str = query.substring(result, query.length());

		int i = 0;

		// カラム名とパラメータをセットするループ
		while (result != -1) {
			// 必要な文字の末尾のindexを取得
			int endIndex = str.indexOf(endStr);

			// 末尾が見つからなかった場合文字の末尾を取得する。
			if (endIndex == -1) {
				endIndex = str.length();
			}

			// カラム名を取得
			String queryName = str.substring(0, endIndex);

			// カラム名とパラメータをセット
			params.put(queryName, inputValue[i]);

			// 必要な文字の先頭のindexを取得
			result = str.indexOf(searchStr);

			// indexからクエリ名を取得
			str = str.substring(result + 1, str.length());
			i++;
		}
		return params;
	}

	public String[] extractColumnNames(String sql) {
		// SELECT と FROM の間を抽出
		String columnPart = sql.substring(sql.indexOf("SELECT") + 6, sql.indexOf("FROM")).trim();

		// カラム名をリストに追加
		String[] columns = columnPart.split(",");
		List<String> columnNames = new ArrayList<>();

		for (String column : columns) {
			// カラム名の前後の空白をトリム
			column = column.trim();

			// `.` が含まれている場合、`.` の後ろの部分のみを抽出
			if (column.contains(".")) {
				column = column.substring(column.indexOf(".") + 1).trim();
			}

			// 切り出したカラム名をリストに追加
			columnNames.add(column);
		}

		// リストを配列に変換して返す
		return columnNames.toArray(new String[0]);
	}

	public Timestamp getNowTime() {
		return new Timestamp(System.currentTimeMillis());
	}

	public Date getNowDate() {
		return new Date(System.currentTimeMillis());
	}

	/**
	 * 就職活動IDがすでに追加されているか確認するロジック
	 * @param jobSearchId 確認したい就職活動ID
	 * @param jobSearchType 就職活動IDを確認するためのSQL
	 * @return 存在するか（true:存在する）
	 * @throws Exception 
	 */
	public Boolean existsJobSearchId(String jobSearchId, String jobSearchType)
			throws Exception, IncorrectResultSizeDataAccessException {
		if (!TABLE_MAP.containsKey(jobSearchType)) {
			throw new Exception("入力値エラーです。");
		}
		String table = TABLE_MAP.get(jobSearchType);

		// パラメータを格納するためのマップを作成
		Map<String, Object> params = mapInputValues(table, jobSearchId);

		// SQLクエリを実行し、結果を取得
		List<Map<String, Object>> results = jdbc.queryForList(table, params);
		Integer resultCount = results.size();
		if (resultCount == 1) {
			return true;
		} else if (resultCount == 0) {
			return false;
		} else {
			throw new IncorrectResultSizeDataAccessException("その就職活動IDは存在します。", resultCount);
		}

	}

	/**
	 * 新規IDを取得するロジック
	 * @param sql 現在の最大のIDを取得するSQL文
	 * @param headString IDの先頭の文字
	 * @return
	 */
	public String getLargerId(String sql, String headString) {
		// 文字の長さを指定
		String idStringFormat = headString.length() == 1 ? "%06d" : "%05d";

		// 現在の年を取得して、新しいIDを作成
		String currentYear = String.valueOf(Year.now().getValue());
		String newId = String.format("%s_%s_" + idStringFormat, headString, currentYear, 1);

		// パラメータを格納するためのマップを作成
		Map<String, Object> params = new HashMap<>();

		String incrementedId = null;
		try {
			// SQLクエリを実行し、結果を取得
			String maxId = jdbc.queryForObject(sql, params, String.class);

			// nullチェックとパターン検証
			if (maxId != null && maxId.contains("_")) {
				// パターンに基づいて接頭辞部分を特定
				int lastUnderscoreIndex = maxId.lastIndexOf("_");
				String prefix = maxId.substring(0, lastUnderscoreIndex + 1);
				String numericPart = maxId.substring(lastUnderscoreIndex + 1);

				try {
					// 数字部分をパースしてインクリメント
					int number = Integer.parseInt(numericPart);
					number += 1; // 数字に1を足す

					// ゼロ埋めを維持
					incrementedId = prefix + String.format(idStringFormat, number);
				} catch (NumberFormatException e) {
					// 数字部分の解析に失敗した場合はnullにする
					incrementedId = null;
				}
			}
		} catch (EmptyResultDataAccessException e) {
			// 結果が見つからない場合は新しいIDにフォールバック
			incrementedId = null;
		} catch (Exception e) {
			// その他の予期しないエラーをキャッチ
			e.printStackTrace();
			incrementedId = null;
		}

		// 比較ロジック
		if (incrementedId == null || incrementedId.compareTo(newId) <= 0) {
			return newId;
		} else {
			return incrementedId;
		}
	}

	/**
	 * マップから整数データを取得します。
	 * 
	 * @param data マップデータ
	 * @param key  項目名
	 * @return 取得した整数データ。存在しない場合はnull。
	 */
	public Integer getIntegerValue(Map<String, Object> data, String key) {
		Object value = data.get(key);
		return value != null ? Integer.parseInt(value.toString()) : null;
	}

	/**
	 * マップから文字列データを取得します。
	 * 
	 * @param data マップデータ
	 * @param key  項目名
	 * @return 取得した文字列データ。存在しない場合はnull。
	 */
	public String getStringValue(Map<String, Object> data, String key) {
		return (String) data.get(key);
	}

	/**
	 * マップから日付データを取得します。
	 * 
	 * @param data マップデータ
	 * @param key  項目名
	 * @return 取得した日付データ。存在しない場合はnull。
	 */
	public Date getDateValue(Map<String, Object> data, String key) {
		return (Date) data.get(key);
	}

	/**
	 * マップからタイムスタンプデータを取得します。
	 * 
	 * @param data マップデータ
	 * @param key  項目名
	 * @return 取得したタイムスタンプデータ。存在しない場合はnull。
	 */
	public Timestamp getTimestampValue(Map<String, Object> data, String key) {
		return (Timestamp) data.get(key);
	}

	/**
	 * マップからブールデータを取得します。
	 * 
	 * @param data マップデータ
	 * @param key  項目名
	 * @return 取得したブールデータ。存在しない場合はnull。
	 */
	public Boolean getBooleanValue(Map<String, Object> data, String key) {
		Object value = data.get(key);
		return value != null ? Boolean.parseBoolean(value.toString()) : null;
	}

	/**
	 * マップから時間データを取得します。
	 * 
	 * @param data マップデータ
	 * @param key  項目名
	 * @return 取得した時間データ。存在しない場合はnull。
	 */
	public Time getTimeValue(Map<String, Object> data, String key) {
		return (Time) data.get(key);
	}

}
