package com.api.domain.repositories;

import java.sql.Timestamp;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.api.domain.models.dbdata.ExamReportData;
import com.api.jobpal.common.base.DBDataConversion;

/**
 * 受験報告に関わるDBアクセスを実現するクラスです。
 *
 * <p>
 * 以下の処理を行います。
 * <ul>
 * <li>受験報告の新規作成</li>
 * <li>受験報告の更新</li>
 * </ul>
 * <p>
 * 処理が継続できない場合は、呼び出し元へ例外をスローします。<br>
 * <strong>呼び出し元では適切な例外処理を行ってください。</strong>
 */
@Repository
public class ExamReportRepository {
	/**
	 * SQL 受験報告最大IDを取得
	 */
	private static final String SQL_SELECT_JOB_EXAM_ID_MAX = "SELECT "
			+ "MAX(exam_report_id) AS max_id "
			+ "FROM "
			+ "EXAM_REPORT_T";

	/**
	 * SQL 受験報告の追加
	 */
	private static final String SQL_INSERT_JOB_EXAM_ONE = "INSERT INTO "
			+ "EXAM_REPORT_T ("
			+ "exam_report_id, "
			+ "exam_opponent_count, "
			+ "exam_opponent_position, "
			+ "exam_count, "
			+ "exam_type, "
			+ "exam_content, "
			+ "impressions, "
			+ "created_at, "
			+ "updated_at, "
			+ "job_search_id"
			+ ") VALUES ("
			+ ":examReportId , "
			+ ":examOpponentCount , "
			+ ":examOpponentPosition , "
			+ ":examCount ,"
			+ ":examType ,"
			+ ":examContent ,"
			+ ":impressions , "
			+ ":createdAt , "
			+ ":updatedAt , "
			+ ":jobSearchId "
			+ ")";
	/**
	 * SQL 受験報告の更新
	 */
	private static final String SQL_UPDATE_JOB_EXAM_ONE = "UPDATE "
			+ "EXAM_REPORT_T "
			+ "SET "
			+ "exam_opponent_count = :examOpponentCount , "
			+ "exam_opponent_position = :examOpponentPosition , "
			+ "exam_count = :examCount , "
			+ "exam_type = :examType , "
			+ "exam_content = :examContent , "
			+ "impressions = :impressions , "
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
	 * 受験報告を作成します。
	 *
	 * <p>
	 * 新しい受験報告をデータベースに登録します。受験報告IDは自動生成され、作成時刻も現在時刻が設定されます。
	 * </p>
	 *
	 * @param examReportData 作成する受験報告データを格納したオブジェクト
	 * @return 処理が成功した場合は`true`、失敗した場合は`false`を返却します。
	 */
	public Boolean insertExamReport(ExamReportData examReportData) {
		// 新規受験報告IDを取得
		String examReportId = dbdc.getLargerId(SQL_SELECT_JOB_EXAM_ID_MAX, "ER");

		// 作成時刻を取得
		Timestamp createdAt = dbdc.getNowTime();

		// パラメータを格納するためのマップを作成
		Map<String, Object> params = dbdc.mapInputValues(
				SQL_INSERT_JOB_EXAM_ONE,
				examReportId,
				examReportData.getExamOpponentCount(), // 対戦相手の人数
				examReportData.getExamOpponentPosition(), // 対戦相手のポジション
				examReportData.getExamCount(), // 試験回数
				examReportData.getExamType(), // 試験の種類
				examReportData.getExamContent(), // 試験内容
				examReportData.getImpressions(), // 感想
				createdAt, // 作成日時
				createdAt, // 更新日時
				examReportData.getJobSearchId() // 就職活動ID
		);

		// SQLクエリを実行し、データを更新
		int updateRow = jdbc.update(SQL_INSERT_JOB_EXAM_ONE, params);

		// 更新結果を返却
		return updateRow == EXPECTED_UPDATE_COUNT;

	}

	/**
	 * 受験報告を更新します。
	 *
	 * <p>
	 * 既存の受験報告をデータベースで更新します。
	 * </p>
	 *
	 * @param examReportData 更新する受験報告データを格納したオブジェクト
	 * @return 処理が成功した場合は`true`、失敗した場合は`false`を返却します。
	 */
	public Boolean updateExamReport(ExamReportData examReportData) {
		// 更新時刻を取得
		Timestamp updatedAt = dbdc.getNowTime();

		// パラメータを格納するためのマップを作成
		Map<String, Object> params = dbdc.mapInputValues(
				SQL_UPDATE_JOB_EXAM_ONE,
				examReportData.getExamOpponentCount(), // 対戦相手の人数
				examReportData.getExamOpponentPosition(), // 対戦相手のポジション
				examReportData.getExamCount(), // 試験回数
				examReportData.getExamType(), // 試験の種類
				examReportData.getExamContent(), // 試験内容
				examReportData.getImpressions(), // 感想
				updatedAt, // 更新日時
				examReportData.getJobSearchId() // 就職活動ID
		);

		// SQLクエリを実行し、データを更新
		int updateRow = jdbc.update(SQL_UPDATE_JOB_EXAM_ONE, params);

		// 更新結果を返却
		return updateRow == EXPECTED_UPDATE_COUNT;

	}

}
