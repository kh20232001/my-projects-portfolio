package com.api.domain.repositories;

import java.sql.Timestamp;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.api.domain.models.dbdata.JobSearchReportData;
import com.api.jobpal.common.base.DBDataConversion;

/**
 * 就職活動報告に関わるDBアクセスを実現するクラスです。
 *
 * <p>
 * 以下の処理を行います。
 * <ul>
 * <li>新規就職活動報告の作成</li>
 * <li>既存就職活動報告の更新</li>
 * </ul>
 * <p>
 * 処理が継続できない場合は、呼び出し元へ例外をスローします。<br>
 * <strong>呼び出し元では適切な例外処理を行ってください。</strong>
 */

@Repository
public class JobSearchReportRepository {
	/**
	 * SQL 就職活動報告最大IDを取得
	 */
	private static final String SQL_SELECT_JOB_SEARCH_REPORT_ID_MAX = "SELECT "
			+ "MAX(job_report_id) AS max_id "
			+ "FROM "
			+ "JOB_SEARCH_REPORT_T";

	/**
	 * SQL 就職活動報告の追加
	 */
	private static final String SQL_INSERT_JOB_SEARCH_REPORT_ONE = "INSERT INTO "
			+ "JOB_SEARCH_REPORT_T ("
			+ "job_report_id, "
			+ "report_content, "
			+ "result, "
			+ "created_at, "
			+ "updated_at, "
			+ "job_search_id"
			+ ") VALUES ( "
			+ ":jobReportId , "
			+ ":reportContent , "
			+ ":result , "
			+ ":createdAt , "
			+ ":updatedAt , "
			+ ":jobSearchId "
			+ ")";

	/**
	 * SQL 就職活動報告の更新
	 */
	private static final String SQL_UPDATE_JOB_SEARCH_REPORT_ONE = "UPDATE "
			+ "JOB_SEARCH_REPORT_T "
			+ "SET "
			+ "report_content = :reportContent , "
			+ "result = :result , "
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
	 * 就職活動報告を作成します。
	 *
	 * <p>
	 * 新しい就職活動報告をデータベースに登録します。報告IDは自動生成され、作成時刻も現在時刻が設定されます。
	 * </p>
	 *
	 * @param jobSearchReportData 作成する就職活動報告データを格納したオブジェクト
	 * @return 処理が成功した場合は`true`、失敗した場合は`false`を返却します。
	 */
	public Boolean insertJobSearchReport(JobSearchReportData jobSearchReportData) {
		// 新規就職活動報告IDを取得
		String jobSearchReportId = dbdc.getLargerId(SQL_SELECT_JOB_SEARCH_REPORT_ID_MAX, "JR");
		Timestamp createdAt = dbdc.getNowTime();

		// パラメータを格納するためのマップを作成
		Map<String, Object> params = dbdc.mapInputValues(
				SQL_INSERT_JOB_SEARCH_REPORT_ONE,
				jobSearchReportId, // 報告ID
				jobSearchReportData.getReportContent(), // 報告内容
				jobSearchReportData.getResult(), // 報告結果
				createdAt, // 作成日時
				createdAt, // 更新日時
				jobSearchReportData.getJobSearchId() // 就職活動ID
		);

		// SQLクエリを実行し、結果を取得
		int updateRow = jdbc.update(SQL_INSERT_JOB_SEARCH_REPORT_ONE, params);

		// 更新結果を返却
		return updateRow == EXPECTED_UPDATE_COUNT;

	}

	/**
	 * 就職活動報告を更新します。
	 *
	 * <p>
	 * 既存の就職活動報告をデータベースで更新します。
	 * </p>
	 *
	 * @param jobSearchReportData 更新する就職活動報告データを格納したオブジェクト
	 * @return 処理が成功した場合は`true`、失敗した場合は`false`を返却します。
	 */
	public Boolean updateJobSearchReport(JobSearchReportData jobSearchReportData) {
		// 更新時刻を取得
		Timestamp updatedAt = dbdc.getNowTime();

		// パラメータを格納するためのマップを作成
		Map<String, Object> params = dbdc.mapInputValues(
				SQL_UPDATE_JOB_SEARCH_REPORT_ONE,
				jobSearchReportData.getReportContent(), // 報告内容
				jobSearchReportData.getResult(), // 報告結果
				updatedAt, // 更新日時
				jobSearchReportData.getJobSearchId() // 就職活動ID
		);

		// SQLクエリを実行し、結果を取得
		int updateRow = jdbc.update(SQL_UPDATE_JOB_SEARCH_REPORT_ONE, params);

		// 更新結果を返却
		return updateRow == EXPECTED_UPDATE_COUNT;

	}

}
