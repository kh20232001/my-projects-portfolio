package com.api.domain.repositories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.api.domain.models.dbdata.JobSearchDashBoardData;
import com.api.domain.models.dbdata.JobSearchDashBoardDetailData;
import com.api.domain.models.entities.DashBoardEntity;
import com.api.jobpal.common.base.DBDataConversion;

/**
 * 就職活動ダッシュボードに関わるDBアクセスを実現するクラスです。
 *
 * <p>
 * 以下の処理を行います。
 * <ul>
 * <li>就職活動ダッシュボード一覧の取得</li>
 * <li>就職活動ダッシュボード詳細の取得</li>
 * </ul>
 * <p>
 * 処理が継続できない場合は、呼び出し元へ例外をスローします。<br>
 * <strong>呼び出し元では適切な例外処理を行ってください。</strong>
 */
@Repository
public class JobSearchDashBoardRepository {

	/**
	 * SQL ダッシュボード全件取得(管理者用)
	 */
	private static String SQL_SELECT_JOB_SEARCH_DASHBOARD_ADMIN = "SELECT "
			+ "UM.user_name, "
			+ "JS.job_search_status, "
			+ "JA.start_time, "
			+ "JA.company_name, "
			+ "JA.event_category, "
			+ "JR.result, "
			+ "JA.school_check_flag, "
			+ "JA.end_time, "
			+ "JS.job_search_id, "
			+ "TSUM.department, "
			+ "TSUM.grade, "
			+ "TSUM.class_name, "
			+ "TSUM.attendance_number, "
			+ "UM.user_id, "
			+ "GREATEST(JA.updated_at, COALESCE(JR.updated_at, '1970-01-01'), COALESCE(ER.updated_at, '1970-01-01')) AS max_updated_at "
			+ "FROM "
			+ "JOB_SEARCH_T AS JS "
			+ "LEFT OUTER JOIN JOB_SEARCH_APPLICATION_T AS JA "
			+ "ON JS.job_search_id = JA.job_search_id "
			+ "LEFT OUTER JOIN JOB_SEARCH_REPORT_T AS JR "
			+ "ON JS.job_search_id = JR.job_search_id "
			+ "LEFT OUTER JOIN EXAM_REPORT_T AS ER "
			+ "ON JS.job_search_id = ER.job_search_id "
			+ "LEFT OUTER JOIN USER_M AS UM "
			+ "ON UM.user_id = JS.student_user_id "
			+ "LEFT OUTER JOIN TEACHER_STUDENT_USER_M AS TSUM "
			+ "ON JS.student_user_id = TSUM.user_id";

	/**
	 * SQL ダッシュボード全件取得 (担任用)
	 */
	private static String SQL_SELECT_JOB_SEARCH_DASHBOARD_TEACHER = "SELECT "
			+ "UM.user_name, "
			+ "JS.job_search_status, "
			+ "JA.start_time, "
			+ "JA.company_name, "
			+ "JA.event_category, "
			+ "JR.result, "
			+ "JA.school_check_flag, "
			+ "JA.end_time, "
			+ "JS.job_search_id, "
			+ "TSUM.department, "
			+ "TSUM.grade, "
			+ "TSUM.class_name, "
			+ "TSUM.attendance_number, "
			+ "UM.user_id, "
			+ "GREATEST(JA.updated_at, COALESCE(JR.updated_at, '1970-01-01'), COALESCE(ER.updated_at, '1970-01-01')) AS max_updated_at "
			+ "FROM "
			+ "JOB_SEARCH_T AS JS "
			+ "LEFT OUTER JOIN JOB_SEARCH_APPLICATION_T AS JA "
			+ "ON JS.job_search_id = JA.job_search_id "
			+ "LEFT OUTER JOIN JOB_SEARCH_REPORT_T AS JR "
			+ "ON JS.job_search_id = JR.job_search_id "
			+ "LEFT OUTER JOIN EXAM_REPORT_T AS ER "
			+ "ON JS.job_search_id = ER.job_search_id "
			+ "LEFT OUTER JOIN USER_M AS UM "
			+ "ON UM.user_id = JS.student_user_id "
			+ "LEFT OUTER JOIN TEACHER_STUDENT_USER_M AS TSUM "
			+ "ON JS.student_user_id = TSUM.user_id "
			+ "WHERE "
			+ "JS.job_search_status <> '00'";

	/**
	 * SQL ダッシュボード全件取得（学生用）
	 */
	private static String SQL_SELECT_JOB_SEARCH_DASHBOARD_STUDENT = "SELECT "
			+ "UM.user_name, "
			+ "JS.job_search_status, "
			+ "JA.start_time, "
			+ "JA.company_name, "
			+ "JA.event_category, "
			+ "JR.result, "
			+ "JA.school_check_flag, "
			+ "JA.end_time, "
			+ "JS.job_search_id, "
			+ "TSUM.department, "
			+ "TSUM.grade, "
			+ "TSUM.class_name, "
			+ "TSUM.attendance_number, "
			+ "UM.user_id, "
			+ "GREATEST(JA.updated_at, COALESCE(JR.updated_at, '1970-01-01'), COALESCE(ER.updated_at, '1970-01-01')) AS max_updated_at "
			+ "FROM "
			+ "JOB_SEARCH_T AS JS "
			+ "LEFT OUTER JOIN JOB_SEARCH_APPLICATION_T AS JA "
			+ "ON JS.job_search_id = JA.job_search_id "
			+ "LEFT OUTER JOIN JOB_SEARCH_REPORT_T AS JR "
			+ "ON JS.job_search_id = JR.job_search_id "
			+ "LEFT OUTER JOIN EXAM_REPORT_T AS ER "
			+ "ON JS.job_search_id = ER.job_search_id "
			+ "INNER JOIN USER_M AS UM "
			+ "ON UM.user_id = JS.student_user_id "
			+ "INNER JOIN TEACHER_STUDENT_USER_M AS TSUM "
			+ "ON JS.student_user_id = TSUM.user_id "
			+ "WHERE "
			+ "UM.user_id = :userId "
			+ "AND JS.job_search_status <> '00'";

	/**
	 * SQL ダッシュボードの詳細を取得
	 */
	private static final String SQL_SELECT_JOB_SEARCH_DASHBOARD_DETAIL = "SELECT "
			+ "UM.user_name, "
			+ "JS.job_search_id, "
			+ "JS.student_user_id, "
			+ "JS.job_search_status, "
			+ "JA.job_application_id, "
			+ "JA.start_time, "
			+ "JA.company_name, "
			+ "JA.event_category, "
			+ "JA.location_type, "
			+ "JA.location, "
			+ "JA.school_check_flag, "
			+ "JA.school_checked_flag, "
			+ "JA.tardiness_absence_type, "
			+ "JA.tardy_leave_time, "
			+ "JA.end_time, "
			+ "JA.remarks, "
			+ "JR.job_report_id, "
			+ "JR.report_content, "
			+ "JR.result, "
			+ "ER.exam_report_id, "
			+ "ER.exam_opponent_count, "
			+ "ER.exam_opponent_position, "
			+ "ER.exam_count, "
			+ "ER.exam_type, "
			+ "ER.exam_content, "
			+ "ER.impressions, "
			+ "TSUM.department, "
			+ "TSUM.grade, "
			+ "TSUM.class_name, "
			+ "TSUM.attendance_number, "
			+ "TSUM.student_id, "
			+ "GREATEST(JA.updated_at, COALESCE(JR.updated_at, '1970-01-01'), COALESCE(ER.updated_at, '1970-01-01')) AS max_updated_at "
			+ "FROM "
			+ "JOB_SEARCH_T AS JS "
			+ "LEFT OUTER JOIN JOB_SEARCH_APPLICATION_T AS JA "
			+ "ON JS.job_search_id = JA.job_search_id "
			+ "LEFT OUTER JOIN JOB_SEARCH_REPORT_T AS JR "
			+ "ON JS.job_search_id = JR.job_search_id "
			+ "LEFT OUTER JOIN EXAM_REPORT_T AS ER "
			+ "ON JS.job_search_id = ER.job_search_id "
			+ "LEFT OUTER JOIN TEACHER_STUDENT_USER_M AS TSUM "
			+ "ON JS.student_user_id = TSUM.user_id "
			+ "LEFT OUTER JOIN USER_M AS UM "
			+ "ON UM.user_id = JS.student_user_id "
			+ "WHERE "
			+ "JS.job_search_id = :jobSearchId ";

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
	 * ダッシュボードのデータを全件取得します。(管理者・担任用)
	 *
	 * <p>
	 * 管理者か担任かを判定し、対応するダッシュボードデータを取得します。
	 * </p>
	 *
	 * @param adminFlg 管理者チェック（true: 管理者）
	 * @return ダッシュボードのデータを格納したエンティティ
	 */
	public DashBoardEntity selectDashBoardAll(Boolean adminFlg) {
		// クエリのパラメータを設定するマップ
		Map<String, Object> params = new HashMap<>();

		// 管理者か担任かを判定してSQLを設定
		String sql = SQL_SELECT_JOB_SEARCH_DASHBOARD_TEACHER;
		if (adminFlg) {
			sql = SQL_SELECT_JOB_SEARCH_DASHBOARD_ADMIN;
		}

		// SQLクエリを実行し、結果を取得
		List<Map<String, Object>> resultDashBoardList = jdbc.queryForList(sql, params);

		// DashBoardEntityを作成
		DashBoardEntity dashBoardEntity = new DashBoardEntity();

		// SQLのカラム名を取得
		String[] queryList = dbdc.extractColumnNames(sql);

		// DashBoardDataのリスト作成
		List<JobSearchDashBoardData> dashBoardList = new ArrayList<>();

		// DashBoardDataのリストにデータを格納
		for (Map<String, Object> resultDashBoardData : resultDashBoardList) {
			JobSearchDashBoardData jobSearchDashBoardData = packageJobSearchDashBoard(resultDashBoardData, queryList);
			dashBoardList.add(jobSearchDashBoardData);
		}

		// DashBoardEntityにリストを設定
		dashBoardEntity.setDashBoardList(dashBoardList);

		// 取得したデータを返却
		return dashBoardEntity;
	}

	/**
	 * ダッシュボードのデータを全件取得します。(学生用)
	 *
	 * <p>
	 * 指定されたユーザIDに基づき、学生用のダッシュボードデータを取得します。
	 * </p>
	 *
	 * @param userId ユーザID (null不可)
	 * @return ダッシュボードのデータを格納したエンティティ
	 */
	public DashBoardEntity selectDashBoardStudent(String userId) {
		// クエリのパラメータを設定
		Map<String, Object> params = dbdc.mapInputValues(SQL_SELECT_JOB_SEARCH_DASHBOARD_STUDENT, userId);

		// SQLクエリを実行し、結果を取得
		List<Map<String, Object>> resultDashBoardUserList = jdbc.queryForList(SQL_SELECT_JOB_SEARCH_DASHBOARD_STUDENT,
				params);

		// DashBoardEntityを作成
		DashBoardEntity dashBoardEntity = new DashBoardEntity();

		// SQLのカラム名を取得
		String[] queryList = dbdc.extractColumnNames(SQL_SELECT_JOB_SEARCH_DASHBOARD_STUDENT);

		// DashBoardDataのリスト作成
		List<JobSearchDashBoardData> dashBoardList = new ArrayList<>();

		// DashBoardDataのリストにデータを格納
		for (Map<String, Object> resultDashBoardData : resultDashBoardUserList) {
			JobSearchDashBoardData jobSearchDashBoardData = packageJobSearchDashBoard(resultDashBoardData, queryList);
			dashBoardList.add(jobSearchDashBoardData);
		}

		// DashBoardEntityにリストを設定
		dashBoardEntity.setDashBoardList(dashBoardList);

		// 取得したデータを返却
		return dashBoardEntity;
	}

	/**
	 * ダッシュボードの詳細データを取得します。
	 *
	 * <p>
	 * 指定された就職活動IDに基づき、詳細データを取得します。
	 * </p>
	 *
	 * @param jobSearchId 就職活動ID (null不可)
	 * @return ダッシュボードの詳細データ
	 */
	public JobSearchDashBoardDetailData selectJobSearchDashBoardDetail(String jobSearchId) {
		// クエリのパラメータを設定
		Map<String, Object> params = dbdc.mapInputValues(SQL_SELECT_JOB_SEARCH_DASHBOARD_DETAIL, jobSearchId);

		// SQL_SELECT_JOB_SEARCH_DASHBOARD_DETAILクエリを実行し、結果を取得
		Map<String, Object> jobSearchDashBoardDetail = jdbc.queryForList(SQL_SELECT_JOB_SEARCH_DASHBOARD_DETAIL, params)
				.get(0);

		// SQLのカラム名を取得
		String[] queryList = dbdc.extractColumnNames(SQL_SELECT_JOB_SEARCH_DASHBOARD_DETAIL);

		// 詳細データを格納
		JobSearchDashBoardDetailData jobSearchDashBoardDetailData = packageJobSearchDashBoardDetail(
				jobSearchDashBoardDetail, queryList);

		// 取得したデータを返却
		return jobSearchDashBoardDetailData;
	}

	/**
	 * データベースで取得した就職活動ダッシュボード情報を格納します。
	 *
	 * <p>
	 * SQLクエリで取得したデータを`JobSearchDashBoardData`オブジェクトに格納します。
	 * </p>
	 *
	 * @param resultData SQLで取得したデータを保持するマップ
	 * @param queryList  SQLで取得した項目名の配列
	 * @return 格納された`JobSearchDashBoardData`オブジェクト
	 */
	JobSearchDashBoardData packageJobSearchDashBoard(Map<String, Object> resultData, String[] queryList) {
		// JobSearchDashBoardDataオブジェクトを初期化
		JobSearchDashBoardData jobSearchDashBoardData = new JobSearchDashBoardData();

		// 各フィールドに値を設定
		jobSearchDashBoardData.setUserName(dbdc.getStringValue(resultData, queryList[0])); // ユーザ名
		jobSearchDashBoardData.setJobSearchStatus(dbdc.getStringValue(resultData, queryList[1])); // 就職活動の状態
		jobSearchDashBoardData.setStartTime(dbdc.getTimestampValue(resultData, queryList[2])); // 開始時間
		jobSearchDashBoardData.setCompanyName(dbdc.getStringValue(resultData, queryList[3])); // 会社名
		jobSearchDashBoardData.setEventCategory(dbdc.getStringValue(resultData, queryList[4])); // イベントカテゴリ
		jobSearchDashBoardData.setResult(dbdc.getStringValue(resultData, queryList[5])); // 結果
		jobSearchDashBoardData.setSchoolCheckFlag(dbdc.getBooleanValue(resultData, queryList[6])); // 学校チェックフラグ
		jobSearchDashBoardData.setEndTime(dbdc.getTimestampValue(resultData, queryList[7])); // 終了時間
		jobSearchDashBoardData.setJobSearchId(dbdc.getStringValue(resultData, queryList[8])); // 就職活動ID
		jobSearchDashBoardData.setDepartment(dbdc.getStringValue(resultData, queryList[9])); // 学科名
		jobSearchDashBoardData.setGrade(dbdc.getIntegerValue(resultData, queryList[10])); // 学年
		jobSearchDashBoardData.setClassName(dbdc.getStringValue(resultData, queryList[11])); // クラス名
		jobSearchDashBoardData.setAttendanceNumber(dbdc.getIntegerValue(resultData, queryList[12])); // 出席番号
		jobSearchDashBoardData.setUserId(dbdc.getStringValue(resultData, queryList[13])); // ユーザID
		jobSearchDashBoardData.setMaxUpdatedAt(dbdc.getTimestampValue(resultData, "max_updated_at")); // 最終更新日時

		// 格納されたオブジェクトを返却
		return jobSearchDashBoardData;
	}

	/**
	 * データベースで取得した就職活動ダッシュボード詳細情報を格納します。
	 *
	 * <p>
	 * SQLクエリで取得したデータを`JobSearchDashBoardDetailData`オブジェクトに格納します。
	 * </p>
	 *
	 * @param jobSearchDashBoardDetail SQLで取得したデータを保持するマップ
	 * @param queryList                SQLで取得した項目名の配列
	 * @return 格納された`JobSearchDashBoardDetailData`オブジェクト
	 */
	private JobSearchDashBoardDetailData packageJobSearchDashBoardDetail(
			Map<String, Object> jobSearchDashBoardDetail, String[] queryList) {
		// JobSearchDashBoardDetailDataオブジェクトを初期化
		JobSearchDashBoardDetailData jobSearchDashBoardDetailData = new JobSearchDashBoardDetailData();

		// 各フィールドに値を設定
		jobSearchDashBoardDetailData.setUserName(dbdc.getStringValue(jobSearchDashBoardDetail, queryList[0])); // ユーザ名
		jobSearchDashBoardDetailData.setJobSearchId(dbdc.getStringValue(jobSearchDashBoardDetail, queryList[1])); // 就職活動ID
		jobSearchDashBoardDetailData.setStudentUserId(dbdc.getStringValue(jobSearchDashBoardDetail, queryList[2])); // 学生ユーザID
		jobSearchDashBoardDetailData.setJobSearchStatus(dbdc.getStringValue(jobSearchDashBoardDetail, queryList[3])); // 就職活動状態
		jobSearchDashBoardDetailData.setJobApplicationId(dbdc.getStringValue(jobSearchDashBoardDetail, queryList[4])); // 申請ID
		jobSearchDashBoardDetailData.setStartTime(dbdc.getTimestampValue(jobSearchDashBoardDetail, queryList[5])); // 開始時間
		jobSearchDashBoardDetailData.setCompanyName(dbdc.getStringValue(jobSearchDashBoardDetail, queryList[6])); // 会社名
		jobSearchDashBoardDetailData.setEventCategory(dbdc.getStringValue(jobSearchDashBoardDetail, queryList[7])); // イベントカテゴリ
		jobSearchDashBoardDetailData.setLocationType(dbdc.getStringValue(jobSearchDashBoardDetail, queryList[8])); // 場所タイプ
		jobSearchDashBoardDetailData.setLocation(dbdc.getStringValue(jobSearchDashBoardDetail, queryList[9])); // 場所
		jobSearchDashBoardDetailData.setSchoolCheckFlag(dbdc.getBooleanValue(jobSearchDashBoardDetail, queryList[10])); // 学校チェックフラグ
		jobSearchDashBoardDetailData
				.setSchoolCheckedFlag(dbdc.getBooleanValue(jobSearchDashBoardDetail, queryList[11])); // 学校確認フラグ
		jobSearchDashBoardDetailData
				.setTardinessAbsenceType(dbdc.getStringValue(jobSearchDashBoardDetail, queryList[12])); // 遅刻欠席区分
		jobSearchDashBoardDetailData.setTardyLeaveTime(dbdc.getTimeValue(jobSearchDashBoardDetail, queryList[13])); // 遅刻退席時間
		jobSearchDashBoardDetailData.setEndTime(dbdc.getTimestampValue(jobSearchDashBoardDetail, queryList[14])); // 終了時間
		jobSearchDashBoardDetailData.setRemarks(dbdc.getStringValue(jobSearchDashBoardDetail, queryList[15])); // 備考
		jobSearchDashBoardDetailData.setJobReportId(dbdc.getStringValue(jobSearchDashBoardDetail, queryList[16])); // 報告ID
		jobSearchDashBoardDetailData.setReportContent(dbdc.getStringValue(jobSearchDashBoardDetail, queryList[17])); // 報告内容
		jobSearchDashBoardDetailData.setResult(dbdc.getStringValue(jobSearchDashBoardDetail, queryList[18])); // 結果
		jobSearchDashBoardDetailData.setExamReportId(dbdc.getStringValue(jobSearchDashBoardDetail, queryList[19])); // 試験報告ID
		jobSearchDashBoardDetailData
				.setExamOpponentCount(dbdc.getIntegerValue(jobSearchDashBoardDetail, queryList[20])); // 対戦相手数
		jobSearchDashBoardDetailData
				.setExamOpponentPosition(dbdc.getStringValue(jobSearchDashBoardDetail, queryList[21])); // 対戦相手ポジション
		jobSearchDashBoardDetailData.setExamCount(dbdc.getStringValue(jobSearchDashBoardDetail, queryList[22])); // 試験回数
		jobSearchDashBoardDetailData.setExamType(dbdc.getStringValue(jobSearchDashBoardDetail, queryList[23])); // 試験タイプ
		jobSearchDashBoardDetailData.setExamContent(dbdc.getStringValue(jobSearchDashBoardDetail, queryList[24])); // 試験内容
		jobSearchDashBoardDetailData.setImpressions(dbdc.getStringValue(jobSearchDashBoardDetail, queryList[25])); // 感想
		jobSearchDashBoardDetailData.setDepartment(dbdc.getStringValue(jobSearchDashBoardDetail, queryList[26])); // 学科名
		jobSearchDashBoardDetailData.setGrade(dbdc.getIntegerValue(jobSearchDashBoardDetail, queryList[27])); // 学年
		jobSearchDashBoardDetailData.setClassName(dbdc.getStringValue(jobSearchDashBoardDetail, queryList[28])); // クラス名
		jobSearchDashBoardDetailData.setAttendanceNumber(dbdc.getStringValue(jobSearchDashBoardDetail, queryList[29])); // 出席番号
		jobSearchDashBoardDetailData.setStudentId(dbdc.getIntegerValue(jobSearchDashBoardDetail, queryList[30])); // 学生ID
		jobSearchDashBoardDetailData
				.setMaxUpdatedAt(dbdc.getTimestampValue(jobSearchDashBoardDetail, "max_updated_at")); // 最終更新日時

		// 格納されたオブジェクトを返却
		return jobSearchDashBoardDetailData;
	}

}
