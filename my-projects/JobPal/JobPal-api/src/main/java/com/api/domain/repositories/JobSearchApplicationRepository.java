package com.api.domain.repositories;

import java.sql.Timestamp;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.api.domain.models.dbdata.JobSearchApplicationData;
import com.api.jobpal.common.base.DBDataConversion;

/**
 * 就職活動申請に関わるDBアクセスを実現するクラスです。
 *
 * <p>
 * 以下の処理を行います。
 * <ul>
 * <li>就職活動申請情報の取得</li>
 * <li>就職活動申請情報の作成</li>
 * <li>就職活動申請情報の更新</li>
 * </ul>
 * <p>
 * 処理が継続できない場合は、呼び出し元へ例外をスローします。<br>
 * <strong>呼び出し元では適切な例外処理を行ってください。</strong>
 */

@Repository
public class JobSearchApplicationRepository {
	/**
	 * SQL 就職活動申請最大IDを取得
	 */
	private static final String SQL_SELECT_JOB_SEARCH_APPLICATION_ID_MAX = "SELECT "
			+ "MAX(job_application_id) AS max_id "
			+ "FROM "
			+ "JOB_SEARCH_APPLICATION_T";
	/**
	 * SQL 就職活動申請のイベントカテゴリを取得
	 */
	private static final String SQL_SELECT_JOB_SEARCH_APPLICATION_EVENT_CATEGORY = "SELECT "
			+ "event_category "
			+ "FROM "
			+ "JOB_SEARCH_APPLICATION_T "
			+ "WHERE "
			+ "job_search_id = :jobSearchId";

	/**
	 * SQL 就職活動申請の追加
	 */
	private static final String SQL_INSERT_JOB_SEARCH_APPLICATION_ONE = "INSERT INTO "
			+ "JOB_SEARCH_APPLICATION_T ("
			+ "job_application_id, "
			+ "start_time, "
			+ "company_name, "
			+ "event_category, "
			+ "location_type, "
			+ "location, "
			+ "school_check_flag, "
			+ "school_checked_flag, "
			+ "tardiness_absence_type, "
			+ "tardy_leave_time, "
			+ "end_time, "
			+ "remarks, "
			+ "created_at, "
			+ "updated_at, "
			+ "job_search_id"
			+ ") VALUES ("
			+ ":jobApplicationId , "
			+ ":startTime , "
			+ ":companyName , "
			+ ":eventCategory , "
			+ ":locationType , "
			+ ":location , "
			+ ":schoolCheckFlag , "
			+ ":schoolCheckedFlag , "
			+ ":tardinessAbsenceType , "
			+ ":tardyLeaveTime , "
			+ ":endTime , "
			+ ":remarks , "
			+ ":createdAt , "
			+ ":updatedAt , "
			+ ":jobSearchId "
			+ ")";

	/**
	 * SQL 就職活動申請の更新
	 */
	private static final String SQL_UPDATE_JOB_SEARCH_APPLICATION_ONE = "UPDATE "
			+ "JOB_SEARCH_APPLICATION_T "
			+ "SET "
			+ "start_time = :startTime , "
			+ "company_name = :companyName , "
			+ "event_category = :eventCategory , "
			+ "location_type = :locationType , "
			+ "location = :location , "
			+ "school_check_flag = :schoolCheckFlag , "
			+ "school_checked_flag = :schoolCheckedFlag , "
			+ "tardiness_absence_type = :tardinessAbsenceType , "
			+ "tardy_leave_time = :tardyLeaveTime , "
			+ "end_time = :endTime , "
			+ "remarks = :remarks , "
			+ "updated_at = :updatedAt "
			+ "WHERE "
			+ "job_search_id = :jobSearchId";

	/**
	 * SQL 就職活動申請の学校とりまとめ名簿チェック更新
	 */
	private static final String SQL_UPDATE_SCHOOL_CHECKED_ONE = "UPDATE "
			+ "JOB_SEARCH_APPLICATION_T "
			+ "SET "
			+ "school_checked_flag = true , "
			+ "updated_at = :updatedAt "
			+ "WHERE "
			+ "job_search_id = :jobSearchId";

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
	 * 指定された就職活動IDに関連するイベント区分を取得します。
	 *
	 * <p>
	 * 就職活動の申請データからイベント区分を取得します。<br>
	 * データが存在しない場合はnullを返却します。
	 * </p>
	 *
	 * @param jobSearchId 就職活動ID(null不可)
	 * @return イベント区分。該当データが存在しない場合はnull。
	 */
	public String selectEventCategory(String jobSearchId) {
		// SQLクエリのパラメータをマップ形式で設定
		Map<String, Object> params = dbdc.mapInputValues(SQL_SELECT_JOB_SEARCH_APPLICATION_EVENT_CATEGORY, jobSearchId);

		try {
			// SQLクエリを実行し、結果を取得
			return jdbc.queryForObject(SQL_SELECT_JOB_SEARCH_APPLICATION_EVENT_CATEGORY, params, String.class);
		} catch (EmptyResultDataAccessException e) {
			// データが存在しない場合はnullを返却
			return null;
		}
	}

	/**
	 * 就職活動申請を作成します。
	 *
	 * <p>
	 * 新しい就職活動申請をデータベースに登録します。申請IDは自動生成され、作成時刻も現在時刻が設定されます。
	 * </p>
	 *
	 * @param jobSearchApplicationData 作成する就職活動申請データを格納したオブジェクト
	 * @return 処理が成功した場合は`true`、失敗した場合は`false`を返却します。
	 */
	public Boolean insertJobSearchApplication(JobSearchApplicationData jobSearchApplicationData) {
		// 新規就職活動申請IDを取得
		String jobSearchApplicationId = dbdc.getLargerId(SQL_SELECT_JOB_SEARCH_APPLICATION_ID_MAX, "JA");

		// 現在時刻を取得
		Timestamp createdAt = dbdc.getNowTime();

		// パラメータを格納するためのマップを作成
		Map<String, Object> params = dbdc.mapInputValues(
				SQL_INSERT_JOB_SEARCH_APPLICATION_ONE,
				jobSearchApplicationId, // 申請ID
				jobSearchApplicationData.getStartTime(), // 開始時間
				jobSearchApplicationData.getCompanyName(), // 会社名
				jobSearchApplicationData.getEventCategory(), // イベントカテゴリ
				jobSearchApplicationData.getLocationType(), // 場所タイプ
				jobSearchApplicationData.getLocation(), // 場所
				jobSearchApplicationData.getSchoolCheckFlag(), // 学校チェックフラグ
				jobSearchApplicationData.getSchoolCheckedFlag(), // 学校確認フラグ
				jobSearchApplicationData.getTardinessAbsenceType(), // 遅刻欠席区分
				jobSearchApplicationData.getTardyLeaveTime(), // 遅刻退席時間
				jobSearchApplicationData.getEndTime(), // 終了時間
				jobSearchApplicationData.getRemarks(), // 備考
				createdAt, // 作成日時
				createdAt, // 更新日時
				jobSearchApplicationData.getJobSearchId() // 就職活動ID
		);

		// SQLクエリを実行し、結果を取得
		int updateRow = jdbc.update(SQL_INSERT_JOB_SEARCH_APPLICATION_ONE, params);

		// 更新結果を返却
		return updateRow == EXPECTED_UPDATE_COUNT;
	}

	/**
	 * 就職活動申請を更新します。
	 *
	 * <p>
	 * 既存の就職活動申請をデータベースで更新します。
	 * </p>
	 *
	 * @param jobSearchApplicationData 更新する就職活動申請データを格納したオブジェクト
	 * @return 処理が成功した場合は`true`、失敗した場合は`false`を返却します。
	 */
	public Boolean updateJobSearchApplication(JobSearchApplicationData jobSearchApplicationData) {
		// 更新時刻を取得
		Timestamp updatedAt = dbdc.getNowTime();

		// パラメータを格納するためのマップを作成
		Map<String, Object> params = dbdc.mapInputValues(
				SQL_UPDATE_JOB_SEARCH_APPLICATION_ONE,
				jobSearchApplicationData.getStartTime(), // 開始時間
				jobSearchApplicationData.getCompanyName(), // 会社名
				jobSearchApplicationData.getEventCategory(), // イベントカテゴリ
				jobSearchApplicationData.getLocationType(), // 場所タイプ
				jobSearchApplicationData.getLocation(), // 場所
				jobSearchApplicationData.getSchoolCheckFlag(), // 学校チェックフラグ
				jobSearchApplicationData.getSchoolCheckedFlag(), // 学校確認フラグ
				jobSearchApplicationData.getTardinessAbsenceType(), // 遅刻欠席区分
				jobSearchApplicationData.getTardyLeaveTime(), // 遅刻退席時間
				jobSearchApplicationData.getEndTime(), // 終了時間
				jobSearchApplicationData.getRemarks(), // 備考
				updatedAt, // 更新日時
				jobSearchApplicationData.getJobSearchId() // 就職活動ID
		);
		System.out.println(params);
		// SQLクエリを実行し、結果を取得
		int updateRow = jdbc.update(SQL_UPDATE_JOB_SEARCH_APPLICATION_ONE, params);

		// 更新結果を返却
		return updateRow == EXPECTED_UPDATE_COUNT;

	}

	/**
	 * 就職活動申請の学校とりまとめ名簿状態を更新します。
	 *
	 * <p>
	 * 指定された就職活動IDに対応する学校とりまとめ名簿の状態を更新します。
	 * </p>
	 *
	 * @param jobSearchId 就職活動ID
	 * @return 処理が成功した場合は`true`、失敗した場合は`false`を返却します。
	 */
	public Boolean updateSchoolCheckedOne(String jobSearchId) {
		// 現在時刻を取得
		Timestamp updatedAt = dbdc.getNowTime();

		// パラメータを格納するためのマップを作成
		Map<String, Object> params = dbdc.mapInputValues(
				SQL_UPDATE_SCHOOL_CHECKED_ONE,
				updatedAt, // 更新日時
				jobSearchId // 就職活動ID
		);

		// SQLクエリを実行し、結果を取得
		int updateRow = jdbc.update(SQL_UPDATE_SCHOOL_CHECKED_ONE, params);

		// 更新結果を返却
		return updateRow == EXPECTED_UPDATE_COUNT;

	}

}
