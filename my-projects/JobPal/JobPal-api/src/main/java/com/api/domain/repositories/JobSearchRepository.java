package com.api.domain.repositories;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.api.jobpal.common.base.DBDataConversion;

/**
 * 就職活動に関わるDBアクセスを実現するクラスです。
 *
 * <p>
 * 以下の処理を行います。
 * <ul>
 * <li>就職活動の状態を取得・更新</li>
 * <li>就職活動の新規登録</li>
 * <li>就職活動の論理削除</li>
 * </ul>
 * <p>
 * 処理が継続できない場合は、呼び出し元へ例外をスローします。<br>
 * <strong>呼び出し元では適切な例外処理を行ってください。</strong>
 */

@Repository
public class JobSearchRepository {
	/**
	 * SQL 就職活動最大IDを取得
	 */
	private static final String SQL_SELECT_JOB_SEARCH_USER_ID = "SELECT "
			+ "student_user_id "
			+ "FROM "
			+ "JOB_SEARCH_T "
			+ "WHERE job_search_id = :jobSearchId";
	private static final String SQL_SELECT_JOB_SEARCH_ID_MAX = "SELECT "
			+ "MAX(job_search_id) AS max_id "
			+ "FROM "
			+ "JOB_SEARCH_T";
	/**
	 * SQL 就職活動状態を取得
	 */
	private static final String SQL_SELECT_JOB_STATE_ID = "SELECT "
			+ "job_search_status "
			+ "FROM "
			+ "JOB_SEARCH_T "
			+ "WHERE "
			+ "job_search_id = :jobSearchId";

	/**
	 * SQL 就職活動状態を取得
	 */
	private static final String SQL_INSERT_JOB_SEARCH = "INSERT INTO "
			+ "JOB_SEARCH_T (job_search_id, student_user_id, job_search_status)"
			+ "VALUES"
			+ "( :jobSearchId , :userId , '11')";

	/**
	 * SQL 就職活動状態を更新
	 */
	private static final String SQL_UPDATE_JOB_STATE_ID = "UPDATE "
			+ "JOB_SEARCH_T "
			+ "SET "
			+ "job_search_status = :jobSearchStatus "
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
	 * 指定された `jobSearchId` に対応する `jobSearchStatus` を取得します。
	 *
	 * <p>
	 * 就職活動IDを基に、データベースから対応する就職活動ステータスを取得します。<br>
	 * 結果が存在しない場合は`null`を返却します。
	 * </p>
	 *
	 * @param jobSearchId 就職活動ID（必須）
	 * @return `jobSearchStatus`（ステータス）または`null`（該当データが存在しない場合）
	 */
	public String selectJobSearchStatus(String jobSearchId) {
		// クエリのパラメータを設定するマップを作成
		Map<String, Object> params = dbdc.mapInputValues(SQL_SELECT_JOB_STATE_ID, jobSearchId);

		try {
			// SQL_SELECT_JOB_STATE_IDクエリを実行し、jobSearchStatusを直接取得
			return jdbc.queryForObject(SQL_SELECT_JOB_STATE_ID, params, String.class);
		} catch (EmptyResultDataAccessException e) {
			// 結果が見つからない場合はnullを返却
			return null;
		}
	}

	/**
	 * 指定された `jobSearchId` に対応する `jobSearchUserId` を取得します。
	 *
	 * <p>
	 * 就職活動IDを基に、データベースから対応する就職活動ステータスを取得します。<br>
	 * 結果が存在しない場合は`null`を返却します。
	 * </p>
	 *
	 * @param jobSearchId 就職活動ID（必須）
	 * @return `jobSearchUserId`（ステータス）または`null`（該当データが存在しない場合）
	 */
	public String selectJobSearchUserId(String jobSearchId) {
		// クエリのパラメータを設定するマップを作成
		Map<String, Object> params = dbdc.mapInputValues(SQL_SELECT_JOB_SEARCH_USER_ID, jobSearchId);

		try {
			// SQL_SELECT_JOB_SEARCH_USER_IDクエリを実行し、jobSearchUserIdを直接取得
			return jdbc.queryForObject(SQL_SELECT_JOB_SEARCH_USER_ID, params, String.class);
		} catch (EmptyResultDataAccessException e) {
			// 結果が見つからない場合はnullを返却
			return null;
		}
	}

	/**
	 * 新しい就職活動IDを生成します。
	 *
	 * <p>
	 * データベース内の最大就職活動IDを取得し、それに基づいて新しい就職活動IDを生成します。<br>
	 * 新しいIDは、現在の最大IDに基づいて一意性を確保した形で生成されます。
	 * </p>
	 *
	 * @return 新しく生成された就職活動ID
	 */
	public String getJobSearchId() {
		// SQL_SELECT_JOB_SEARCH_ID_MAXクエリを実行し、結果を取得
		return dbdc.getLargerId(SQL_SELECT_JOB_SEARCH_ID_MAX, "JS");
	}

	/**
	 * 就職活動状態を更新します。
	 *
	 * <p>
	 * 指定された就職活動IDに対応する状態を更新します。<br>
	 * 状態の更新が正常に行われた場合は`true`を返却し、失敗した場合は`false`を返却します。
	 * </p>
	 *
	 * @param jobSearchStatus 更新する状態
	 * @param jobSearchId 更新対象の就職活動ID
	 * @return 処理が成功した場合は`true`、失敗した場合は`false`を返却します。
	 */
	public Boolean updateJobSearchStatus(String jobSearchStatus, String jobSearchId) {
		// クエリのパラメータを設定するマップを作成
		Map<String, Object> params = dbdc.mapInputValues(SQL_UPDATE_JOB_STATE_ID, jobSearchStatus, jobSearchId);

		// SQLクエリを実行し、結果を取得
		int updateRow = jdbc.update(SQL_UPDATE_JOB_STATE_ID, params);

		// 更新結果を返却
		return updateRow == EXPECTED_UPDATE_COUNT;
	}

	/**
	 * 就職活動申請を削除状態にします。(論理削除)
	 *
	 * <p>
	 * 指定された就職活動IDの状態を「削除済み」に設定します。<br>
	 * また、削除状態にした際に関連する通知も削除されます。
	 * </p>
	 *
	 * @param jobSearchId 更新対象の就職活動ID
	 * @return 処理が成功した場合は`true`、失敗した場合は`false`を返却します。
	 */
	public Boolean deleteJobSearchStatus(String jobSearchId) {
		// クエリのパラメータを設定するマップを作成
		Map<String, Object> params = dbdc.mapInputValues(SQL_UPDATE_JOB_STATE_ID, "00", jobSearchId);

		// SQLクエリを実行し、結果を取得
		int updateRow = jdbc.update(SQL_UPDATE_JOB_STATE_ID, params);

		// 更新結果を返却
		return updateRow == EXPECTED_UPDATE_COUNT;
	}

	/**
	 * 就職活動を1件追加します。
	 *
	 * <p>
	 * 指定されたユーザIDと就職活動IDを基に、データベースに新しい就職活動を追加します。
	 * </p>
	 *
	 * @param jobSearchId 新しく追加する就職活動ID
	 * @param userId ユーザID
	 * @return 処理が成功した場合は`true`、失敗した場合は`false`を返却します。
	 */
	public Boolean insertJobSearch(String jobSearchId, String userId) {
		// クエリのパラメータを設定するマップを作成
		Map<String, Object> params = dbdc.mapInputValues(SQL_INSERT_JOB_SEARCH, jobSearchId, userId);

		// SQLクエリを実行し、結果を取得
		int updateRow = jdbc.update(SQL_INSERT_JOB_SEARCH, params);

		// 更新結果を返却
		return updateRow == EXPECTED_UPDATE_COUNT;
	}

}
