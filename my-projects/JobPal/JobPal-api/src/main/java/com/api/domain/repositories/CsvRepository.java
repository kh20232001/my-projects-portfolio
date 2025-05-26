package com.api.domain.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.api.jobpal.common.base.DBDataConversion;

/**
 * CSV出力に関わるDBアクセスを実現するクラスです。
 *
 * <p>
 * 以下の処理を行います。
 * <ul>
 * <li>ユーザに関連するデータの取得（就職活動ID、証明書発行IDなど）</li>
 * <li>地域ごとのデータ件数取得（札幌、東京、その他）</li>
 * <li>地域リストの取得</li>
 * </ul>
 * <p>
 * 処理が継続できない場合は、呼び出し元へ例外をスローします。<br>
 * <strong>呼び出し元では適切な例外処理を行ってください。</strong>
 */
@Repository
public class CsvRepository {
	/**
	 * SQL ユーザIDから就職活動IDを取得
	 */
	String SQL_SELECT_CSV_JOB_SEARCH = "SELECT job_search_id "
			+ "FROM JOB_SEARCH_T "
			+ "WHERE student_user_id = :userId";

	/**
	 * SQL ユーザIDから証明書発行IDを取得
	 */
	String SQL_SELECT_CSV_CERTIFICATE_ISSUANCE = "SELECT certificate_issue_id "
			+ "FROM CERTIFICATE_ISSUANCE_T "
			+ "WHERE student_user_id = :userId";

	/**
	 * SQL 指定された担任のクラスに属する学生で、就職活動が未完了（resultが5以外）の人数を取得
	 */
	String SQL_SELECT_CSV_ACTIVITY = "SELECT "
			+ "COUNT(*) AS record_count "
			+ "FROM "
			+ "USER_M UM "
			+ "LEFT OUTER JOIN TEACHER_STUDENT_USER_M TSUM ON UM.user_id = TSUM.user_id "
			+ "LEFT OUTER JOIN JOB_SEARCH_T JST ON UM.user_id = JST.student_user_id "
			+ "LEFT OUTER JOIN JOB_SEARCH_REPORT_T JSRT ON JST.job_search_id = JSRT.job_search_id "
			+ "WHERE UM.user_type = '0' "
			+ "AND UM.user_status = '0' "
			+ "AND JSRT.result <> '5' "
			+ "AND CONCAT(TSUM.department, TSUM.grade, TSUM.class_name)"
			+ " = (SELECT CONCAT(TSUM.department, TSUM.grade, TSUM.class_name) "
			+ "FROM "
			+ "TEACHER_STUDENT_USER_M TSUM "
			+ "WHERE "
			+ "TSUM.user_id = :teacherUserId ) "
			+ "AND JSRT.updated_at = (SELECT MAX(JSRT2.updated_at) "
			+ "FROM "
			+ "JOB_SEARCH_REPORT_T JSRT2 "
			+ "WHERE JSRT2.job_search_id = JSRT.job_search_id)";

	/**
	 * SQL 指定された担任のクラスに属する学生で、就職活動が完了（resultが5）の人数を取得
	 */
	String SQL_SELECT_CSV_ACTIVITY_FINISH = "SELECT "
			+ "COUNT(*) AS record_count "
			+ "FROM "
			+ "USER_M UM "
			+ "LEFT OUTER JOIN TEACHER_STUDENT_USER_M TSUM ON UM.user_id = TSUM.user_id "
			+ "LEFT OUTER JOIN JOB_SEARCH_T JST ON UM.user_id = JST.student_user_id "
			+ "LEFT OUTER JOIN JOB_SEARCH_REPORT_T JSRT ON JST.job_search_id = JSRT.job_search_id "
			+ "WHERE UM.user_type = '0' "
			+ "AND UM.user_status = '0' "
			+ "AND JSRT.result = '5' "
			+ "AND CONCAT(TSUM.department, TSUM.grade, TSUM.class_name)"
			+ " = (SELECT CONCAT(TSUM.department, TSUM.grade, TSUM.class_name) "
			+ "FROM "
			+ "TEACHER_STUDENT_USER_M TSUM "
			+ "WHERE "
			+ "TSUM.user_id = :teacherUserId ) "
			+ "AND JSRT.updated_at = (SELECT MAX(JSRT2.updated_at) "
			+ "FROM "
			+ "JOB_SEARCH_REPORT_T JSRT2 "
			+ "WHERE JSRT2.job_search_id = JSRT.job_search_id)";

	/**
	 * SQL 指定された担任のクラスに属する学生で、就職活動応募先が札幌（location_typeが0）の人数を取得
	 */
	String SQL_SELECT_CSV_SAPPORO = "SELECT COUNT(*) AS record_count "
			+ "FROM USER_M UM "
			+ "LEFT OUTER JOIN TEACHER_STUDENT_USER_M TSUM "
			+ "ON UM.user_id = TSUM.user_id "
			+ "LEFT OUTER JOIN JOB_SEARCH_T JST "
			+ "ON UM.user_id = JST.student_user_id "
			+ "LEFT OUTER JOIN JOB_SEARCH_APPLICATION_T JSA "
			+ "ON JST.job_search_id = JSA.job_search_id "
			+ "WHERE UM.user_type = '0' "
			+ "AND UM.user_status = '0' "
			+ "AND CONCAT(TSUM.department, TSUM.grade, TSUM.class_name) = ( "
			+ "  SELECT CONCAT(TSUM.department, TSUM.grade, TSUM.class_name) "
			+ "  FROM TEACHER_STUDENT_USER_M TSUM "
			+ "  WHERE TSUM.user_id = :teacherUserId "
			+ ") "
			+ "AND JSA.location_type = '0' "
			+ "AND JSA.updated_at = ( "
			+ "  SELECT MAX(JSA2.updated_at) "
			+ "  FROM JOB_SEARCH_APPLICATION_T JSA2 "
			+ "  WHERE JSA2.job_search_id = JSA.job_search_id "
			+ ")";

	/**
	 * SQL 指定された担任のクラスに属する学生で、就職活動応募先が東京（location_typeが1）の人数を取得
	 */
	String SQL_SELECT_CSV_TOKYO = "SELECT COUNT(*) AS record_count "
			+ "FROM USER_M UM "
			+ "LEFT OUTER JOIN TEACHER_STUDENT_USER_M TSUM "
			+ "ON UM.user_id = TSUM.user_id "
			+ "LEFT OUTER JOIN JOB_SEARCH_T JST "
			+ "ON UM.user_id = JST.student_user_id "
			+ "LEFT OUTER JOIN JOB_SEARCH_APPLICATION_T JSA "
			+ "ON JST.job_search_id = JSA.job_search_id "
			+ "WHERE UM.user_type = '0' "
			+ "AND UM.user_status = '0' "
			+ "AND CONCAT(TSUM.department, TSUM.grade, TSUM.class_name) = ( "
			+ "  SELECT CONCAT(TSUM.department, TSUM.grade, TSUM.class_name) "
			+ "  FROM TEACHER_STUDENT_USER_M TSUM "
			+ "  WHERE TSUM.user_id = :teacherUserId "
			+ ") "
			+ "AND JSA.location_type = '1' "
			+ "AND JSA.updated_at = ( "
			+ "  SELECT MAX(JSA2.updated_at) "
			+ "  FROM JOB_SEARCH_APPLICATION_T JSA2 "
			+ "  WHERE JSA2.job_search_id = JSA.job_search_id "
			+ ")";

	/**
	 * SQL 指定された担任のクラスに属する学生で、就職活動応募先がその他（location_typeが2）の人数を取得
	 */
	String SQL_SELECT_CSV_OTHERS = "SELECT COUNT(*) AS record_count "
			+ "FROM USER_M UM "
			+ "LEFT OUTER JOIN TEACHER_STUDENT_USER_M TSUM "
			+ "ON UM.user_id = TSUM.user_id "
			+ "LEFT OUTER JOIN JOB_SEARCH_T JST "
			+ "ON UM.user_id = JST.student_user_id "
			+ "LEFT OUTER JOIN JOB_SEARCH_APPLICATION_T JSA "
			+ "ON JST.job_search_id = JSA.job_search_id "
			+ "WHERE UM.user_type = '0' "
			+ "AND UM.user_status = '0' "
			+ "AND CONCAT(TSUM.department, TSUM.grade, TSUM.class_name) = ( "
			+ "  SELECT CONCAT(TSUM.department, TSUM.grade, TSUM.class_name) "
			+ "  FROM TEACHER_STUDENT_USER_M TSUM "
			+ "  WHERE TSUM.user_id = :teacherUserId "
			+ ") "
			+ "AND JSA.location_type = '2' "
			+ "AND JSA.updated_at = ( "
			+ "  SELECT MAX(JSA2.updated_at) "
			+ "  FROM JOB_SEARCH_APPLICATION_T JSA2 "
			+ "  WHERE JSA2.job_search_id = JSA.job_search_id "
			+ ")";

	/**
	 * SQL 指定された担任のクラスに属する学生で、就職活動応募先の最新の場所を取得
	 */
	String SQL_SELECT_CSV_LOCATION = "SELECT location "
			+ "FROM USER_M UM "
			+ "LEFT OUTER JOIN TEACHER_STUDENT_USER_M TSUM "
			+ "ON UM.user_id = TSUM.user_id "
			+ "LEFT OUTER JOIN JOB_SEARCH_T JST "
			+ "ON UM.user_id = JST.student_user_id "
			+ "LEFT OUTER JOIN JOB_SEARCH_APPLICATION_T JSA "
			+ "ON JST.job_search_id = JSA.job_search_id "
			+ "WHERE UM.user_type = '0' "
			+ "AND UM.user_status = '0' "
			+ "AND CONCAT(TSUM.department, TSUM.grade, TSUM.class_name) = ( "
			+ "  SELECT CONCAT(TSUM.department, TSUM.grade, TSUM.class_name) "
			+ "  FROM TEACHER_STUDENT_USER_M TSUM "
			+ "  WHERE TSUM.user_id = :teacherUserId "
			+ ") "
			+ "AND JSA.updated_at = ( "
			+ "  SELECT MAX(JSA2.updated_at) "
			+ "  FROM JOB_SEARCH_APPLICATION_T JSA2 "
			+ "  WHERE JSA2.job_search_id = JSA.job_search_id "
			+ ")";
	/**
	 * NamedParameterJdbcTemplateを使用してSQLを実行するためのオブジェクト。
	 */
	@Autowired
	private NamedParameterJdbcTemplate jdbc;

	/**
	 * データベースから取得したデータを変換するためのユーティリティクラス。
	 */
	@Autowired
	private DBDataConversion dbdc;

	/**
	 * 指定されたユーザIDに関連する就職活動IDのリストを取得します。
	 *
	 * <p>
	 * データベースから指定されたユーザIDに対応する就職活動IDをリスト形式で取得します。
	 * </p>
	 *
	 * @param userId ユーザID
	 * @return 就職活動IDのリスト。データが存在しない場合は空のリストを返却します。
	 */
	public List<String> selectJobSearchIdList(String userId) {
		// SQLクエリのパラメータを設定
		Map<String, Object> params = dbdc.mapInputValues(SQL_SELECT_CSV_JOB_SEARCH, userId);

		// SQL_SELECT_CSV_JOB_SEARCHクエリを実行し、結果を取得
		List<Map<String, Object>> result = jdbc.queryForList(SQL_SELECT_CSV_JOB_SEARCH, params);

		// 結果をString型のリストに変換
		List<String> stringList = new ArrayList<>();
		for (Map<String, Object> map : result) {
			stringList.add(map.get("job_search_id").toString()); // ObjectをStringに変換して追加
		}

		return stringList;
	}

	/**
	 * 指定されたユーザIDに関連する証明書発行IDのリストを取得します。
	 *
	 * <p>
	 * データベースから指定されたユーザIDに対応する証明書発行IDをリスト形式で取得します。
	 * </p>
	 *
	 * @param userId ユーザID
	 * @return 証明書発行IDのリスト。データが存在しない場合は空のリストを返却します。
	 */
	public List<String> selectCertificateIssuanceIdList(String userId) {
		// SQLクエリのパラメータを設定
		Map<String, Object> params = dbdc.mapInputValues(SQL_SELECT_CSV_CERTIFICATE_ISSUANCE, userId);

		// SQL_SELECT_CSV_CERTIFICATE_ISSUANCEクエリを実行し、結果を取得
		List<Map<String, Object>> result = jdbc.queryForList(SQL_SELECT_CSV_CERTIFICATE_ISSUANCE, params);

		// 結果をString型のリストに変換
		List<String> stringList = new ArrayList<>();
		for (Map<String, Object> map : result) {
			stringList.add(map.get("certificate_issue_id").toString()); // ObjectをStringに変換して追加
		}

		return stringList;
	}

	/**
	 * 指定されたユーザIDに関連する活動データの件数を取得します。
	 *
	 * <p>
	 * データベースから指定されたユーザIDに対応する活動データの件数を取得します。
	 * </p>
	 *
	 * @param userId ユーザID
	 * @return 活動データの件数。データが存在しない場合は0を返却します。
	 */
	public Integer selectCsvActivity(String userId) {
		// SQLクエリのパラメータを設定
		Map<String, Object> params = dbdc.mapInputValues(SQL_SELECT_CSV_ACTIVITY, userId);

		try {
			// SQLクエリを実行して件数を取得
			return jdbc.queryForObject(SQL_SELECT_CSV_ACTIVITY, params, Integer.class);
		} catch (EmptyResultDataAccessException e) {
			// データが存在しない場合は0を返却
			return 0;
		}
	}

	/**
	 * 指定されたユーザIDに関連する完了済み活動データの件数を取得します。
	 *
	 * <p>
	 * データベースから指定されたユーザIDに対応する完了済み活動データの件数を取得します。
	 * </p>
	 *
	 * @param userId ユーザID
	 * @return 完了済み活動データの件数。データが存在しない場合は0を返却します。
	 */
	public Integer selectCsvActivityFinish(String userId) {
		// SQLクエリのパラメータを設定
		Map<String, Object> params = dbdc.mapInputValues(SQL_SELECT_CSV_ACTIVITY_FINISH, userId);

		try {
			// SQLクエリを実行して件数を取得
			return jdbc.queryForObject(SQL_SELECT_CSV_ACTIVITY_FINISH, params, Integer.class);
		} catch (EmptyResultDataAccessException e) {
			// データが存在しない場合は0を返却
			return 0;
		}
	}

	/**
	 * 指定されたユーザIDに関連する札幌地域のデータ件数を取得します。
	 *
	 * <p>
	 * データベースから指定されたユーザIDに対応する札幌地域のデータ件数を取得します。
	 * </p>
	 *
	 * @param userId ユーザID
	 * @return 札幌地域のデータ件数。データが存在しない場合は0を返却します。
	 */
	public Integer selectCsvSapporo(String userId) {
		// SQLクエリのパラメータを設定
		Map<String, Object> params = dbdc.mapInputValues(SQL_SELECT_CSV_SAPPORO, userId);

		try {
			// SQLクエリを実行して件数を取得
			return jdbc.queryForObject(SQL_SELECT_CSV_SAPPORO, params, Integer.class);
		} catch (EmptyResultDataAccessException e) {
			// データが存在しない場合は0を返却
			return 0;
		}
	}

	/**
	 * 指定されたユーザIDに関連する東京地域のデータ件数を取得します。
	 *
	 * <p>
	 * データベースから指定されたユーザIDに対応する東京地域のデータ件数を取得します。
	 * </p>
	 *
	 * @param userId ユーザID
	 * @return 東京地域のデータ件数。データが存在しない場合は0を返却します。
	 */
	public Integer selectCsvTokyo(String userId) {
		// SQLクエリのパラメータを設定
		Map<String, Object> params = dbdc.mapInputValues(SQL_SELECT_CSV_TOKYO, userId);

		try {
			// SQLクエリを実行して件数を取得
			return jdbc.queryForObject(SQL_SELECT_CSV_TOKYO, params, Integer.class);
		} catch (EmptyResultDataAccessException e) {
			// データが存在しない場合は0を返却
			return 0;
		}
	}

	/**
	 * 指定されたユーザIDに関連するその他地域のデータ件数を取得します。
	 *
	 * <p>
	 * データベースから指定されたユーザIDに対応するその他地域のデータ件数を取得します。
	 * </p>
	 *
	 * @param userId ユーザID
	 * @return その他地域のデータ件数。データが存在しない場合は0を返却します。
	 */
	public Integer selectCsvOthers(String userId) {
		// SQLクエリのパラメータを設定
		Map<String, Object> params = dbdc.mapInputValues(SQL_SELECT_CSV_OTHERS, userId);

		try {
			// SQLクエリを実行して件数を取得
			return jdbc.queryForObject(SQL_SELECT_CSV_OTHERS, params, Integer.class);
		} catch (EmptyResultDataAccessException e) {
			// データが存在しない場合は0を返却
			return 0;
		}
	}

	/**
	 * 指定されたユーザIDに関連する地域リストを取得します。
	 *
	 * <p>
	 * データベースから指定されたユーザIDに対応する地域情報をリスト形式で取得します。
	 * </p>
	 *
	 * @param userId ユーザID
	 * @return 地域のリスト。データが存在しない場合は空のリストを返却します。
	 */
	public List<String> selectCsvLocationList(String userId) {
		// SQLクエリのパラメータを設定
		Map<String, Object> params = dbdc.mapInputValues(SQL_SELECT_CSV_LOCATION, userId);

		// SQLクエリを実行して結果を取得
		List<Map<String, Object>> result = jdbc.queryForList(SQL_SELECT_CSV_LOCATION, params);

		// 結果をString型のリストに変換
		List<String> stringList = new ArrayList<>();
		for (Map<String, Object> map : result) {
			stringList.add(map.get("location").toString()); // ObjectをStringに変換して追加
		}

		return stringList;
	}

}
