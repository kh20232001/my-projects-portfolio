package jp.ac.hcs.j2a129.profile;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ProfileRepository {
	/**
	 * プロフィールに関わるDBアクセスを実現するクラスです。
	 * 
	 * <p>以下の処理を行います。
	 * <ul>
	 * <li>検索</li>
	 * <li>追加</li>
	 * <li>削除</li>
	 * <li>更新</li>
	 * </ul>
	 * <p>処理が継続できない場合は、呼び出しもとへ例外をスローします。<br>
	 * <strong>呼び出し元では適切な例が処理を行ってください。</strong>
	 * 
	 * @author 春田和也
	 *
	 */

	/** SQL全件取得（期限日昇順） */
	private static final String SQL_SELECT_COUNT = "SELECT count(*) FROM t_profile WHERE user_id = :userId";
	private static final String SQL_SELECT_PROFILE = "SELECT * FROM t_profile WHERE user_id = :userId";

	//	/** SQL 1件追加 */
	private static final String SQL_INSERT_ONE = "INSERT INTO t_profile (p_id, user_id, user_name, user_kana, class_number, image, comment_text) VALUES ((SELECT MAX(p_id) + 1 FROM t_profile), :userId, :userName, :userKana, :classNumber, :image, :commentText)";

	//	/** SQL 1件更新 */
	private static final String SQL_UPDATE_ONE = "UPDATE t_profile SET user_name = :userName, user_kana = :userKana, class_number = :classNumber, image = :image, comment_text = :commentText WHERE user_id =:userId";

	//	/** SQL 1件削除 */
	private static final String SQL_DELETE_ONE = "DELETE FROM t_profile WHERE user_id = :userId";

	/** 予想更新件数（ハードコーディング防止用）*/
	private static final int EXPECTED_UPDATE_COUNT = 1;

	@Autowired
	private NamedParameterJdbcTemplate jdbc;

	/**
	 * 指定されたユーザIDに関連するすべてのデータを検索します。
	 * @param userId ユーザID
	 * @return 検索結果の数
	 */
	public int findProfileCount(String userId) {
		// クエリのパラメータを設定するマップ
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);

		//SQL_SELECT_ALLクエリを実行し、結果を取得
		int result = jdbc.queryForObject(SQL_SELECT_COUNT, params, Integer.class);
		return result;
	}

	public List<Map<String, Object>> findProfile(String userId) {
		// クエリのパラメータを設定するマップ
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);

		//SQL_SELECT_ALLクエリを実行し、結果を取得
		List<Map<String, Object>> resultList = jdbc.queryForList(SQL_SELECT_PROFILE, params);

		return resultList;
	}

	/**
			 * タスクデータを保存します。
			 * @param taskData 保存するタスクデータ
			 * @return 更新された行数
			 * @throws SQLException 更新に失敗した場合にスローされる例外
			 */
	public int save(ProfileData profileData) throws SQLException {
		// クエリのパラメータを設定するマップ
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", profileData.getUserId());
		params.put("userName", profileData.getUserName());
		params.put("userKana", profileData.getUserKana());
		params.put("classNumber", profileData.getClassNumber());
		params.put("image", profileData.getImage());
		params.put("commentText", profileData.getCommentText());

		// クエリを実行し、更新された行数を取得
		int updateRow = jdbc.update(SQL_INSERT_ONE, params);
		if (updateRow != EXPECTED_UPDATE_COUNT) {
			// 更新件数が異常の場合は例外をスロー
			throw new SQLException("更新に失敗しました 件数:" + updateRow);
		}
		return updateRow;

	}

	/**
	 * 指定されたIDのデータを更新します。
	 * @param id 更新するデータのID
	 * @return 更新された行数
	 * @throws SQLException 更新に失敗した場合にスローされる例外
	 */
	public int update(ProfileData profileData) throws SQLException {
		// クエリのパラメータを設定するマップ
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userName", profileData.getUserName());
		params.put("userKana", profileData.getUserKana());
		params.put("classNumber", profileData.getClassNumber());
		params.put("image", profileData.getImage());
		params.put("commentText", profileData.getCommentText());
		params.put("userId", profileData.getUserId());

		// クエリを実行し、更新された行数を取得
		int updateRow = jdbc.update(SQL_UPDATE_ONE, params);
		if (updateRow != EXPECTED_UPDATE_COUNT) {
			// 更新件数が異常の場合は例外をスロー
			throw new SQLException("更新に失敗しました 件数:" + updateRow);
		}
		return updateRow;

	}

	/**
	 * 指定されたIDのデータを削除します。
	 * @param id 削除するデータのID
	 * @return 更新された行数
	 * @throws SQLException 更新に失敗した場合にスローされる例外
	 */
	public int delete(String userId) throws SQLException {
		// クエリのパラメータを設定するマップ
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);

		// クエリを実行し、更新された行数を取得
		int updateRow = jdbc.update(SQL_DELETE_ONE, params);
		if (updateRow != EXPECTED_UPDATE_COUNT) {
			// 更新件数が異常の場合は例外をスロー
			throw new SQLException("更新に失敗しました 件数:" + updateRow);
		}
		return updateRow;

	}

	//		/**
	//		 * 指定されたユーザーIDに関連するデータをCSVファイルに出力します。
	//		 * @param userId ユーザーID
	//		 */
	//		public void fileOut(String userId) {
	//			Map<String, Object> params = new HashMap<String, Object>();
	//			// カラム名とパラメータをセット
	//			params.put("userId", userId);
	//
	//			// CSVファイル出力用設定
	//			TaskRowCallbackHandler handler = new TaskRowCallbackHandler();
	//			jdbc.query(SQL_SELECT_ALL, params, handler);
	//		}

}
