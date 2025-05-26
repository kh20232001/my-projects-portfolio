package com.api.domain.repositories;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.api.domain.models.dbdata.CertificateData;
import com.api.domain.models.dbdata.CertificateIssuanceDashBoardData;
import com.api.domain.models.dbdata.JobSearchDashBoardData;
import com.api.domain.models.dbdata.MailingData;
import com.api.domain.models.dbdata.NotificationDashBoardData;
import com.api.domain.models.dbdata.NotificationTeacherData;
import com.api.jobpal.common.base.DBDataConversion;

/**
 * 通知に関わるDBアクセスを実現するクラスです。
 *
 * <p>
 * 以下の処理を行います。
 * <ul>
 * <li>通知の件数取得</li>
 * <li>通知済み就職活動一覧の取得</li>
 * <li>通知済み証明書発行一覧の取得</li>
 * <li>通知の新規登録・更新・削除</li>
 * <li>担任通知クラスの登録・削除</li>
 * </ul>
 * <p>
 * 処理が継続できない場合は、呼び出し元へ例外をスローします。<br>
 * <strong>呼び出し元では適切な例外処理を行ってください。</strong>
 */
@Repository
public class NotificationRepository {
	/**
	 * 通知IDの最大を取得。
	 */
	private static final String SQL_SELECT_NOTIFICATION_ID_MAX = "SELECT "
			+ "MAX(notification_id) AS max_id "
			+ "FROM "
			+ "NOTIFICATION_T";
	/**
	 * SQL 通知件数取得
	 */
	private static String SQL_SELECT_NOTIFICATION_COUNT = "SELECT "
			+ "COUNT(*) "
			+ "FROM "
			+ "NOTIFICATION_T "
			+ "WHERE "
			+ "assigned_user_id = :userId";

	/**
	 * SQL 通知就職活動一覧取得
	 */
	private static String SQL_SELECT_NOTIFICATION_DASHBOARD_JOB_SEARCH = "SELECT "
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
			+ "GREATEST(JA.updated_at, COALESCE(JR.updated_at, '1970-01-01'), COALESCE(ER.updated_at, '1970-01-01')) AS max_updated_at, "
			+ "NT.notification_id, "
			+ "NT.assigned_user_id, "
			+ "NT.resend_flag, "
			+ "NT.job_search_certificate_category, "
			+ "NT.notification_timestamp "
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
			+ "INNER JOIN NOTIFICATION_JOB_SEARCH_T AS NJS "
			+ "ON JS.job_search_id = NJS.job_search_id "
			+ "LEFT OUTER JOIN NOTIFICATION_T AS NT "
			+ "ON NT.notification_id = NJS.notification_id "
			+ "WHERE "
			+ "NT.assigned_user_id = :assignedUserId";

	/**
	 * SQL 通知証明書発行一覧取得
	 */
	private static String SQL_SELECT_NOTIFICATION_DASHBOARD_CERTIFICATE_ISSUANCE = "SELECT "
			+ "CIT.certificate_issue_id, "
			+ "CIT.student_user_id, "
			+ "UM.user_name AS student_user_name, "
			+ "TSUM.student_id, "
			+ "CIT.status, "
			+ "CIT.media_type, "
			+ "GREATEST( "
			+ "COALESCE(CIT.application_date, '1900-01-01'), "
			+ "COALESCE(CIT.approval_date, '1900-01-01'), "
			+ "COALESCE(PCIT.delivery_due_date, '1900-01-01'), "
			+ "COALESCE(PCIT.delivery_date, '1900-01-01'), "
			+ "COALESCE(MCIT.post_date, '1900-01-01') "
			+ ") AS latest_date, "
			+ "SUM(CIDT.certificate_quantity * CM.certificate_fee) AS total_fee, "
			+ "CIDT.certificate_quantity, "
			+ "CM.certificate_id, "
			+ "CM.certificate_name, "
			+ "NT.notification_id, "
			+ "NT.assigned_user_id, "
			+ "NT.resend_flag, "
			+ "NT.job_search_certificate_category, "
			+ "NT.notification_timestamp, "
			+ "CM.certificate_weight "
			+ "FROM CERTIFICATE_ISSUANCE_T CIT "
			+ "LEFT OUTER JOIN USER_M UM ON CIT.student_user_id = UM.user_id "
			+ "LEFT OUTER JOIN TEACHER_STUDENT_USER_M TSUM ON CIT.student_user_id = TSUM.user_id "
			+ "LEFT OUTER JOIN PAPER_CERTIFICATE_ISSUANCE_T PCIT ON CIT.certificate_issue_id = PCIT.certificate_issue_id "
			+ "LEFT OUTER JOIN MAILING_CERTIFICATE_ISSUANCE_T MCIT ON CIT.certificate_issue_id = MCIT.certificate_issue_id "
			+ "LEFT OUTER JOIN MAILING_M MM ON MCIT.postal_payment_id = MM.postal_payment_id "
			+ "LEFT OUTER JOIN CERTIFICATE_ISSUANCE_DETAIL_T CIDT ON CIT.certificate_issue_id = CIDT.certificate_issue_id "
			+ "LEFT OUTER JOIN CERTIFICATE_M CM ON CIDT.certificate_id = CM.certificate_id "
			+ "INNER JOIN NOTIFICATION_CERTIFICATE_ISSUANCE_T NCIT ON CIT.certificate_issue_id = NCIT.certificate_issue_id "
			+ "LEFT OUTER JOIN NOTIFICATION_T NT ON NCIT.notification_id = NT.notification_id "
			+ "WHERE NT.assigned_user_id = :assignedUserId " // 条件追加
			+ "GROUP BY CIT.certificate_issue_id, CIT.student_user_id, UM.user_name, "
			+ "TSUM.student_id, CIT.status, CIT.media_type, MM.postal_fee, MM.postal_max_weight, latest_date, "
			+ "CIDT.certificate_quantity, CM.certificate_id,CM.certificate_weight, CM.certificate_name, "
			+ "NT.notification_id, NT.assigned_user_id, NT.resend_flag, NT.job_search_certificate_category, "
			+ "NT.notification_timestamp, NCIT.certificate_issue_id";

	/**
	 * 通知先担任参照
	 */
	private static String SQL_SELECT_NOTIFICATION_CLASS_TEACHER = "SELECT "
			+ "assigned_teacher_user_id "
			+ "FROM "
			+ "NOTIFICATION_CLASS_M "
			+ "WHERE "
			+ "notification_department = :department "
			+ "AND notification_grade = :grade "
			+ "AND notification_class = :class";

	/**
	 * 通知を作成
	 */
	private static String SQL_INSERT_NOTIFICATION = "INSERT INTO "
			+ "NOTIFICATION_T ("
			+ "notification_id, "
			+ "assigned_user_id, "
			+ "resend_flag, "
			+ "job_search_certificate_category, "
			+ "notification_timestamp) "
			+ "VALUES "
			+ "(:notificationId ,"
			+ " :userId ,"
			+ " :resendFlag ,"
			+ " :category ,"
			+ " :timestamp )";

	/**
	 * 通知就職活動作成
	 */
	private static String SQL_INSERT_NOTIFICATION_JOB_SEARCH = "INSERT INTO "
			+ "NOTIFICATION_JOB_SEARCH_T ("
			+ "notification_id,"
			+ " job_search_id) "
			+ "VALUES "
			+ "(:notificationId ,"
			+ " :jobSearchId )";

	/**
	 * 通知証明書発行作成
	 */
	private static String SQL_INSERT_NOTIFICATION_CERTIFICATE_ISSUANCE = "INSERT INTO "
			+ "NOTIFICATION_CERTIFICATE_ISSUANCE_T ("
			+ "notification_id,"
			+ " certificate_issue_id) "
			+ "VALUES "
			+ "(:notificationId ,"
			+ " :certificateIssueId )";

	/**
	 * SQL 就職活動が削除されるとき、参照している通知の削除
	 */
	private static String SQL_DELETE_NOTIFICATION_JOB_SEARCH_USER = "DELETE "
			+ "FROM "
			+ "NOTIFICATION_T "
			+ "WHERE assigned_user_id = :userId "
			+ "AND notification_id IN ( "
			+ "SELECT notification_id "
			+ "FROM NOTIFICATION_JOB_SEARCH_T "
			+ "WHERE job_search_id = :jobSearchId "
			+ ")";

	/**
	 * SQL 証明書発行が削除されるとき、参照している通知の削除
	 */
	private static String SQL_DELETE_NOTIFICATION_CERTIFICATE_ISSUANCE_USER = "DELETE "
			+ "FROM "
			+ "NOTIFICATION_T "
			+ "WHERE assigned_user_id = :userId AND "
			+ "notification_id IN ( "
			+ "SELECT notification_id "
			+ "FROM NOTIFICATION_CERTIFICATE_ISSUANCE_T "
			+ "WHERE certificate_issue_id = :certificateIssueId "
			+ ")";

	/**
	 * SQL 就職活動IDから再通知フラグを更新
	 */
	private static String SQL_UPDATE_NOTIFICATION_JOB_SEARCH = "UPDATE "
			+ "NOTIFICATION_T "
			+ "SET resend_flag = true "
			+ "WHERE notification_id IN ( "
			+ "SELECT notification_id "
			+ "FROM NOTIFICATION_JOB_SEARCH_T "
			+ "WHERE job_search_id = :jobSearchId "
			+ ")";

	/**
	 * SQL 証明書発行IDから再通知フラグを更新
	 */
	private static String SQL_UPDATE_NOTIFICATION_CERTIFICATE_ISSUANCE = "UPDATE "
			+ "NOTIFICATION_T "
			+ "SET resend_flag = true "
			+ "WHERE notification_id IN ( "
			+ "SELECT notification_id "
			+ "FROM NOTIFICATION_CERTIFICATE_ISSUANCE_T "
			+ "WHERE certificate_issue_id = :certificateIssueId "
			+ ")";
	/**
	 * SQL 就職活動が削除されるとき、参照している通知の削除
	 */
	private static String SQL_DELETE_NOTIFICATION_JOB_SEARCH = "DELETE "
			+ "FROM "
			+ "NOTIFICATION_T "
			+ "WHERE notification_id IN ( "
			+ "SELECT notification_id "
			+ "FROM NOTIFICATION_JOB_SEARCH_T "
			+ "WHERE job_search_id = :jobSearchId "
			+ ")";

	/**
	 * SQL 証明書発行が削除されるとき、参照している通知の削除
	 */
	private static String SQL_DELETE_NOTIFICATION_CERTIFICATE_ISSUANCE = "DELETE "
			+ "FROM "
			+ "NOTIFICATION_T "
			+ "WHERE notification_id IN ( "
			+ "SELECT notification_id "
			+ "FROM NOTIFICATION_CERTIFICATE_ISSUANCE_T "
			+ "WHERE certificate_issue_id = :certificateIssueId "
			+ ")";

	/**
	 * SQL 担任通知クラス登録チェック
	 */
	private static String SQL_SELECT_INSERT_CHECK_NOTIFICATION_CLASS = "SELECT "
			+ "COUNT(*) "
			+ "FROM "
			+ "NOTIFICATION_CLASS_M "
			+ "WHERE "
			+ "notification_department = :department "
			+ "AND notification_grade = :grade "
			+ "AND notification_class = :class "
			+ "AND assigned_teacher_user_id = :teacherUserId";

	/**
	 * SQL 担任通知するクラスの追加
	 */
	private static String SQL_INSERT_NOTIFICATION_CLASS = "INSERT INTO "
			+ "NOTIFICATION_CLASS_M ("
			+ "notification_department, "
			+ "notification_grade, "
			+ "notification_class, "
			+ "assigned_teacher_user_id) "
			+ "VALUES "
			+ "(:department ,"
			+ " :grade ,"
			+ " :class ,"
			+ " :teacherUserId )";

	/**
	 * SQL 担任通知するクラスの削除
	 */
	private static String SQL_DELETE_NOTIFICATION_CLASS = "DELETE "
			+ "FROM "
			+ "NOTIFICATION_CLASS_M "
			+ "WHERE "
			+ "notification_department = :department "
			+ "AND notification_grade = :grade "
			+ "AND notification_class = :class";
	/**
	 * SQL 通知クラスの担任削除
	 */
	private static String SQL_DELETE_NOTIFICATION_CLASS_TEACHER = "DELETE "
			+ "FROM "
			+ "NOTIFICATION_CLASS_M "
			+ "WHERE "
			+ "notification_department = :department "
			+ "AND notification_grade = :grade "
			+ "AND notification_class = :class "
			+ "AND assigned_teacher_user_id = :teacherUserId";

	/**
	 * 予想更新件数(ハードコーディング防止用)
	 */
	private static final int EXPECTED_UPDATE_COUNT = 1;

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
	 * 就職活動ダッシュボードデータを操作するためのリポジトリ。
	 */
	@Autowired
	private JobSearchDashBoardRepository JSDBR;

	/**
	 * 証明書発行ダッシュボードデータを操作するためのリポジトリ。
	 */
	@Autowired
	private CertificateIssuanceDashBoardRepository CIDBR;

	/**
	 * 証明書発行に関するRepository
	 */
	@Autowired
	private CertificateIssuanceRepository cir;

	/**
	 * ユーザIDに関連する通知の件数を取得します。
	 *
	 * <p>
	 * データベースから指定されたユーザIDの通知件数を取得し、返却します。
	 * </p>
	 *
	 * @param userId 通知件数を取得したいユーザID
	 * @return 通知件数
	 */
	public Integer getNotificationCount(String userId) {
		// SQLクエリのパラメータを作成
		Map<String, Object> params = dbdc.mapInputValues(SQL_SELECT_NOTIFICATION_COUNT, userId);

		// SQLクエリを実行して通知件数を取得
		return jdbc.queryForObject(SQL_SELECT_NOTIFICATION_COUNT, params, Integer.class);
	}

	/**
	 * 通知された就職活動の一覧を取得します。
	 *
	 * <p>
	 * 指定されたユーザIDに関連する通知済みの就職活動データをリスト形式で返却します。
	 * </p>
	 *
	 * @param userId 通知一覧を取得したいユーザID
	 * @return 通知された就職活動データのリスト
	 */
	public List<NotificationDashBoardData> getNotificationJobSearchList(String userId) {
		// SQLクエリのパラメータを作成
		Map<String, Object> params = dbdc.mapInputValues(SQL_SELECT_NOTIFICATION_DASHBOARD_JOB_SEARCH, userId);

		// SQLクエリを実行して結果を取得
		List<Map<String, Object>> resultDashBoardList = jdbc.queryForList(SQL_SELECT_NOTIFICATION_DASHBOARD_JOB_SEARCH,
				params);

		// SQLのカラム名を抽出
		String[] queryList = dbdc.extractColumnNames(SQL_SELECT_NOTIFICATION_DASHBOARD_JOB_SEARCH);

		// DashBoardDataのリスト作成
		return resultDashBoardList.stream().map(resultDashBoardData -> {
			NotificationDashBoardData notificationDashBoardData = new NotificationDashBoardData();
			JobSearchDashBoardData jobSearchDashBoardData = JSDBR.packageJobSearchDashBoard(resultDashBoardData,
					queryList);

			// NotificationDashBoardDataに情報を設定
			notificationDashBoardData.setJobSearchDashBoardData(jobSearchDashBoardData);
			notificationDashBoardData.setNotifiationId(dbdc.getStringValue(resultDashBoardData, queryList[19]));
			notificationDashBoardData.setAssignedUserId(dbdc.getStringValue(resultDashBoardData, queryList[20]));
			notificationDashBoardData.setResendFlag(dbdc.getBooleanValue(resultDashBoardData, queryList[21]));
			notificationDashBoardData
					.setJobSearchCertificateCategory(dbdc.getStringValue(resultDashBoardData, queryList[22]));
			notificationDashBoardData
					.setNotificationCreatedAt(dbdc.getTimestampValue(resultDashBoardData, queryList[23]));

			return notificationDashBoardData;
		}).collect(Collectors.toList());
	}

	/**
	 * 通知された証明書発行一覧を取得します。
	 *
	 * <p>
	 * 指定されたユーザIDに関連する通知済みの証明書発行データをリスト形式で返却します。
	 * </p>
	 *
	 * @param userId 通知一覧を取得したいユーザID
	 * @return 通知された証明書発行データのリスト
	 */
	public List<NotificationDashBoardData> getNotificationCertificateIssuanceList(String userId) {
		// クエリパラメータを設定
		Map<String, Object> params = dbdc.mapInputValues(SQL_SELECT_NOTIFICATION_DASHBOARD_CERTIFICATE_ISSUANCE,
				userId);

		// SQLクエリを実行し、結果を取得
		List<Map<String, Object>> resultDashBoardList = jdbc
				.queryForList(SQL_SELECT_NOTIFICATION_DASHBOARD_CERTIFICATE_ISSUANCE, params);

		// SQLのカラム名を取得
		String[] queryList = dbdc.extractColumnNames(SQL_SELECT_NOTIFICATION_DASHBOARD_CERTIFICATE_ISSUANCE);

		// 結果を格納するリストを作成
		List<NotificationDashBoardData> dashBoardList = new ArrayList<>();
		MailingData mailingData = cir.selectMailing();
		Integer mailFee = mailingData.getPostalFee();
		Integer mailWeight = mailingData.getPostalMaxWeight();

		// 結果データを処理
		for (Map<String, Object> resultDashBoardData : resultDashBoardList) {
			String notificationId = dbdc.getStringValue(resultDashBoardData, "notification_id");
			Boolean mediaTypeFlg = "2".equals(dbdc.getStringValue(resultDashBoardData, queryList[5]));
			NotificationDashBoardData existingData = findExistingNotification(dashBoardList, notificationId);

			if (existingData != null) {
				// 既存の通知データに証明書情報を追加
				CertificateData certificateData = CIDBR.packageCertificateDashBoard(resultDashBoardData);
				existingData.getCertificateIssuanceDashBoardData()
						.setUpTotalAmount(dbdc.getIntegerValue(resultDashBoardData, "total_fee"));
				Integer beforeTotalWeight = existingData.getCertificateIssuanceDashBoardData().getTotalWeight();
				existingData.getCertificateIssuanceDashBoardData()
						.setUpTotalWeight(dbdc.getIntegerValue(resultDashBoardData, "certificate_quantity")
								* dbdc.getIntegerValue(resultDashBoardData, "certificate_weight"));
				Integer afterTotalWeight = existingData.getCertificateIssuanceDashBoardData().getTotalWeight();

				if (mediaTypeFlg) {
					Integer totalBeforeMailFee = (int) (mailFee
							* Math.ceil((double) beforeTotalWeight / mailWeight));
					Integer totalAfterMailFee = (int) (mailFee
							* Math.ceil((double) afterTotalWeight / mailWeight));

					existingData.getCertificateIssuanceDashBoardData()
							.setUpTotalAmount(totalAfterMailFee - totalBeforeMailFee);
				}

				existingData.getCertificateIssuanceDashBoardData().getCertificateList().add(certificateData);
			} else {
				// 新しい通知データを作成
				NotificationDashBoardData notificationDashBoardData = new NotificationDashBoardData();
				CertificateIssuanceDashBoardData certificateIssuanceDashBoardData = CIDBR
						.packageCertificateIssuanceDashBoard(resultDashBoardData, queryList);
				if (mediaTypeFlg) {
					Integer totalMailFee = (int) (mailFee
							* Math.ceil((double) certificateIssuanceDashBoardData.getTotalWeight() / mailWeight));
					certificateIssuanceDashBoardData.setUpTotalAmount(totalMailFee);
				}
				notificationDashBoardData.setCertificateIssuanceDashBoardData(certificateIssuanceDashBoardData);
				notificationDashBoardData.setNotifiationId(dbdc.getStringValue(resultDashBoardData, queryList[20]));
				notificationDashBoardData.setAssignedUserId(dbdc.getStringValue(resultDashBoardData, queryList[21]));
				notificationDashBoardData.setResendFlag(dbdc.getBooleanValue(resultDashBoardData, queryList[22]));
				notificationDashBoardData
						.setJobSearchCertificateCategory(dbdc.getStringValue(resultDashBoardData, queryList[23]));
				notificationDashBoardData
						.setNotificationCreatedAt(dbdc.getTimestampValue(resultDashBoardData, queryList[24]));

				dashBoardList.add(notificationDashBoardData);
			}
		}

		return dashBoardList;
	}

	/**
	 * 担任通知対象クラスの担任ユーザIDを取得します。
	 *
	 * <p>
	 * 指定された学科、学年、クラスコードに基づいて通知対象の担任ユーザIDをリスト形式で返却します。
	 * </p>
	 *
	 * @param department 学科
	 * @param grade 学年
	 * @param classCode クラスコード
	 * @return 担任ユーザIDのリスト。該当データが存在しない場合は空のリストを返却します。
	 */
	public List<String> getNotificationClassTeacher(String department, Integer grade, String classCode) {
		// クエリパラメータを設定
		Map<String, Object> params = dbdc.mapInputValues(SQL_SELECT_NOTIFICATION_CLASS_TEACHER, department, grade,
				classCode);

		// SQLクエリを実行し、結果を取得
		List<Map<String, Object>> result = jdbc.queryForList(SQL_SELECT_NOTIFICATION_CLASS_TEACHER, params);

		// 担任ユーザIDをリスト形式で抽出
		return result.stream()
				.map(map -> dbdc.getStringValue(map, "assigned_teacher_user_id"))
				.collect(Collectors.toList());
	}

	/**
	 * 指定されたクラス登録が通知テーブルに存在するかを確認します。
	 *
	 * <p>
	 * 学科、学年、クラスコード、および担任ユーザIDに基づいて登録済みのクラスが存在するか確認します。
	 * </p>
	 *
	 * @param notificationTeacherData 確認するクラスデータ
	 * @return クラス登録が存在する場合はtrue、存在しない場合はfalse
	 */
	public Boolean checkNotificationClassExists(NotificationTeacherData notificationTeacherData) {
		// クエリパラメータを設定
		Map<String, Object> params = dbdc.mapInputValues(
				SQL_SELECT_INSERT_CHECK_NOTIFICATION_CLASS,
				notificationTeacherData.getDepartment(),
				notificationTeacherData.getGrade(),
				notificationTeacherData.getClassName(),
				notificationTeacherData.getTeacherUserId());

		// SQLクエリを実行し、登録数を取得
		Integer count = jdbc.queryForObject(SQL_SELECT_INSERT_CHECK_NOTIFICATION_CLASS, params, Integer.class);

		// 登録数が1以上の場合はtrue、それ以外はfalse
		return count != null && count > 0;
	}

	/**
	 * 新しい通知IDを生成します。
	 *
	 * <p>
	 * データベース内の最大通知IDを取得し、それに基づいて新しい通知IDを生成します。
	 * </p>
	 *
	 * @return 新しく生成された通知ID。
	 */
	public String getNotificationId() {
		// データベースから最大通知IDを取得し、新しいIDを生成して返却
		return dbdc.getLargerId(SQL_SELECT_NOTIFICATION_ID_MAX, "N");
	}

	/**
	 * 通知を作成するメソッド
	 *
	 * <p>
	 * 指定された情報に基づいて通知をデータベースに登録します。
	 * 通知カテゴリーに応じて、関連する通知情報も登録します。
	 * </p>
	 *
	 * @param notificationId 通知ID
	 * @param userId 通知対象のユーザID
	 * @param resendFlag 通知の再送フラグ
	 * @param category 通知カテゴリー
	 * @return 通知が正常に作成された場合はtrue、失敗した場合はfalseを返却します。
	 */
	public Boolean insertNotification(String notificationId, String userId, Boolean resendFlag, String category) {
		// 現在時刻を取得
		Timestamp createdAt = dbdc.getNowTime();

		// パラメータを設定
		Map<String, Object> params = dbdc.mapInputValues(SQL_INSERT_NOTIFICATION, notificationId, userId, resendFlag,
				category, createdAt);

		// 通知データを登録
		int updateRow = jdbc.update(SQL_INSERT_NOTIFICATION, params);

		// 更新結果を返却
		return updateRow == EXPECTED_UPDATE_COUNT;
	}

	/**
	 * 通知就職活動を作成するメソッド
	 *
	 * <p>
	 * 通知IDと就職活動IDを基に通知データを登録します。
	 * </p>
	 *
	 * @param notificationId 通知ID
	 * @param jobSearchId 就職活動ID
	 * @return 正常に通知が作成された場合はtrue、失敗した場合はfalseを返却します。
	 */
	public Boolean insertNotificationJobSearch(String notificationId, String jobSearchId) {
		// パラメータを設定
		Map<String, Object> params = dbdc.mapInputValues(SQL_INSERT_NOTIFICATION_JOB_SEARCH, notificationId,
				jobSearchId);

		// 通知データを登録
		int updateRow = jdbc.update(SQL_INSERT_NOTIFICATION_JOB_SEARCH, params);

		// 更新結果を返却
		return updateRow == EXPECTED_UPDATE_COUNT;
	}

	/**
	 * 通知証明書発行を作成するメソッド
	 *
	 * <p>
	 * 通知IDと証明書発行IDを基に通知データを登録します。
	 * </p>
	 *
	 * @param notificationId 通知ID
	 * @param certificateIssueId 証明書発行ID
	 * @return 正常に通知が作成された場合はtrue、失敗した場合はfalseを返却します。
	 */
	public Boolean insertNotificationCertificateIssuance(String notificationId, String certificateIssueId) {
		// パラメータを設定
		Map<String, Object> params = dbdc.mapInputValues(SQL_INSERT_NOTIFICATION_CERTIFICATE_ISSUANCE, notificationId,
				certificateIssueId);

		// 通知データを登録
		int updateRow = jdbc.update(SQL_INSERT_NOTIFICATION_CERTIFICATE_ISSUANCE, params);

		// 更新結果を返却
		return updateRow == EXPECTED_UPDATE_COUNT;
	}

	/**
	 * ユーザの就職活動通知を削除するメソッド
	 *
	 * <p>
	 * 指定されたユーザIDと就職活動IDに基づいて通知を削除します。
	 * </p>
	 *
	 * @param userId ユーザID
	 * @param jobSearchId 就職活動ID
	 * @return 正常に削除された場合はtrue、失敗した場合はfalseを返却します。
	 */
	public Boolean deleteNotificationJobSearchUser(String userId, String jobSearchId) {
		// クエリパラメータを設定
		Map<String, Object> params = dbdc.mapInputValues(SQL_DELETE_NOTIFICATION_JOB_SEARCH_USER, userId, jobSearchId);
		// クエリを実行し更新件数を取得
		int updateRow = jdbc.update(SQL_DELETE_NOTIFICATION_JOB_SEARCH_USER, params);

		// 更新結果を返却
		return updateRow == EXPECTED_UPDATE_COUNT;
	}

	/**
	 * ユーザの証明書発行通知を削除するメソッド
	 *
	 * <p>
	 * 指定されたユーザIDと証明書発行IDに基づいて通知を削除します。
	 * </p>
	 *
	 * @param userId ユーザID
	 * @param certificateIssueId 証明書発行ID
	 * @return 正常に削除された場合はtrue、失敗した場合はfalseを返却します。
	 */
	public Boolean deleteNotificationCertificateIssuanceUser(String userId, String certificateIssueId) {
		// クエリパラメータを設定
		Map<String, Object> params = dbdc.mapInputValues(SQL_DELETE_NOTIFICATION_CERTIFICATE_ISSUANCE_USER, userId,
				certificateIssueId);
		// クエリを実行し更新件数を取得
		int updateRow = jdbc.update(SQL_DELETE_NOTIFICATION_CERTIFICATE_ISSUANCE_USER, params);

		// 更新結果を返却
		return updateRow == EXPECTED_UPDATE_COUNT;
	}

	/**
	 * 就職活動通知の状態を更新するメソッド
	 *
	 * <p>
	 * 指定された就職活動IDに基づいて通知状態を更新します。
	 * </p>
	 *
	 * @param jobSearchId 就職活動ID
	 * @return 正常に更新された場合はtrue、失敗した場合はfalseを返却します。
	 */
	public Boolean updateNotificationJobSearch(String jobSearchId) {
		// クエリパラメータを設定
		Map<String, Object> params = dbdc.mapInputValues(SQL_UPDATE_NOTIFICATION_JOB_SEARCH, jobSearchId);
		// クエリを実行し更新件数を取得
		int updateRow = jdbc.update(SQL_UPDATE_NOTIFICATION_JOB_SEARCH, params);

		// 更新結果を返却
		return updateRow > 0;
	}

	/**
	 * 証明書発行が削除される際の通知状態を更新するメソッド
	 *
	 * <p>
	 * 指定された証明書発行IDに基づいて通知状態を更新します。
	 * </p>
	 *
	 * @param certificateIssueId 証明書発行ID
	 * @return 正常に更新された場合はtrue、失敗した場合はfalseを返却します。
	 */
	public Boolean updateNotificationCertificateIssuance(String certificateIssueId) {
		// クエリパラメータを設定
		Map<String, Object> params = dbdc.mapInputValues(SQL_UPDATE_NOTIFICATION_CERTIFICATE_ISSUANCE,
				certificateIssueId);
		// クエリを実行し更新件数を取得
		int updateRow = jdbc.update(SQL_UPDATE_NOTIFICATION_CERTIFICATE_ISSUANCE, params);

		// 更新結果を返却
		return updateRow > 0;
	}

	/**
	 * 就職活動通知を削除します。
	 *
	 * @param jobSearchId 就職活動ID
	 * @return 正常に削除された場合はtrue、失敗した場合はfalse
	 */
	public Boolean deleteNotificationJobSearch(String jobSearchId) {
		// クエリのパラメータを設定
		Map<String, Object> params = dbdc.mapInputValues(SQL_DELETE_NOTIFICATION_JOB_SEARCH, jobSearchId);
		// クエリを実行し更新件数を取得
		int updateRow = jdbc.update(SQL_DELETE_NOTIFICATION_JOB_SEARCH, params);

		// 更新結果を返却
		return updateRow > 0;
	}

	/**
	 * 証明書発行通知を削除します。
	 *
	 * @param certificateIssueId 証明書発行ID
	 * @return 正常に削除された場合はtrue、失敗した場合はfalse
	 */
	public Boolean deleteNotificationCertificateIssuance(String certificateIssueId) {
		// クエリのパラメータを設定
		Map<String, Object> params = dbdc.mapInputValues(SQL_DELETE_NOTIFICATION_CERTIFICATE_ISSUANCE,
				certificateIssueId);
		// クエリを実行し更新件数を取得
		int updateRow = jdbc.update(SQL_DELETE_NOTIFICATION_CERTIFICATE_ISSUANCE, params);

		// 更新結果を返却
		return updateRow > 0;
	}

	/**
	 * 通知クラスを追加します。
	 *
	 * @param notificationTeacherData 通知クラス情報
	 * @return 正常に追加された場合はtrue、失敗した場合はfalse
	 */
	public Boolean insertNotificationClass(NotificationTeacherData notificationTeacherData) {
		// クエリのパラメータを設定
		Map<String, Object> params = dbdc.mapInputValues(
				SQL_INSERT_NOTIFICATION_CLASS,
				notificationTeacherData.getDepartment(),
				notificationTeacherData.getGrade(),
				notificationTeacherData.getClassName(),
				notificationTeacherData.getTeacherUserId());
		// クエリを実行し更新件数を取得
		int updateRow = jdbc.update(SQL_INSERT_NOTIFICATION_CLASS, params);

		// 更新結果を返却
		return updateRow == EXPECTED_UPDATE_COUNT;
	}

	/**
	 * 通知クラスを削除します。
	 *
	 * @param department 学科
	 * @param grade 学年
	 * @param classCode クラスコード
	 * @return 正常に削除された場合はtrue、失敗した場合はfalse
	 */
	public Boolean deleteNotificationClass(String department, Integer grade, String classCode) {
		// クエリのパラメータを設定
		Map<String, Object> params = dbdc.mapInputValues(SQL_DELETE_NOTIFICATION_CLASS, department, grade, classCode);
		// クエリを実行し更新件数を取得
		int updateRow = jdbc.update(SQL_DELETE_NOTIFICATION_CLASS, params);

		// 更新結果を返却
		return updateRow == EXPECTED_UPDATE_COUNT;
	}

	/**
	 * 通知クラスの担任を削除します。
	 *
	 * @param notificationTeacherData 通知クラスの担任情報
	 * @return 正常に削除された場合はtrue、失敗した場合はfalse
	 */
	public Boolean deleteNotificationClassTeacher(NotificationTeacherData notificationTeacherData) {
		// クエリのパラメータを設定
		Map<String, Object> params = dbdc.mapInputValues(
				SQL_DELETE_NOTIFICATION_CLASS_TEACHER,
				notificationTeacherData.getDepartment(),
				notificationTeacherData.getGrade(),
				notificationTeacherData.getClassName(),
				notificationTeacherData.getTeacherUserId());
		// クエリを実行し更新件数を取得
		int updateRow = jdbc.update(SQL_DELETE_NOTIFICATION_CLASS_TEACHER, params);

		// 更新結果を返却
		return updateRow == EXPECTED_UPDATE_COUNT;
	}

	/**
	 * 指定された通知リストから、同じ証明書発行IDを持つ通知データを検索します。
	 *
	 * @param dashBoardList 通知データのリスト
	 * @param certificateIssueId 検索したい証明書発行ID
	 * @return 一致する通知データ。存在しない場合はnullを返却します。
	 */
	private NotificationDashBoardData findExistingNotification(List<NotificationDashBoardData> dashBoardList,
			String notificationId) {
		for (NotificationDashBoardData data : dashBoardList) {
			if (notificationId.equals(data.getNotifiationId())) {
				return data;
			}
		}
		return null;
	}
}
