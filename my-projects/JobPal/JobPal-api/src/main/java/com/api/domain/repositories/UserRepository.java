package com.api.domain.repositories;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import com.api.domain.models.dbdata.UserData;
import com.api.jobpal.common.base.DBDataConversion;

/**
 * ユーザ管理に関わるDBアクセスを実現するクラスです。
 *
 * <p>
 * 以下の処理を行います。
 * <ul>
 * <li>学生ユーザIDから担任ユーザIDの取得</li>
 * <li>全ユーザ共通データの取得</li>
 * <li>担任・学生を含む全ユーザデータの取得</li>
 * <li>特定ユーザの共通データ取得</li>
 * <li>担任・学生を含む特定ユーザデータ取得</li>
 * <li>ユーザ名、区分、存在確認</li>
 * <li>ユーザデータ、削除ユーザデータの追加</li>
 * <li>ユーザ状態、共通情報、担任・学生情報の更新</li>
 * <li>特定ユーザの削除</li>
 * </ul>
 * <p>
 * 処理が継続できない場合は、呼び出し元へ例外をスローします。<br>
 * <strong>呼び出し元では適切な例外処理を行ってください。</strong>
 */

@Repository
public class UserRepository {
	/**
	 * SQL 担任のユーザーIDを取得
	 */
	private static final String SQL_SELECT_TEACHER_USER_ID = "SELECT "
			+ "T.user_id AS teacher_user_id " +
			"FROM TEACHER_STUDENT_USER_M S " +
			"JOIN TEACHER_STUDENT_USER_M T ON S.department = T.department AND S.grade = T.grade AND S.class_name = T.class_name "
			+
			"JOIN USER_M U ON T.user_id = U.user_id " +
			"WHERE S.user_id = :student_user_id AND U.user_type = '1' AND U.user_status = '0'";
	/**
	 * SQL 事務のユーザーIDを取得
	 */
	private static final String SQL_SELECT_OFFICE_USER_ID = "SELECT "
			+ "user_id "
			+ "FROM USER_M "
			+ "WHERE user_type = '3' AND user_status = '0'";

	/**
	 * SQL ユーザ共通情報を全件取得
	 */
	private static final String SQL_SELECT_USER_ALL = "SELECT "
			+ "user_id, "
			+ "encrypted_password, "
			+ "user_name, "
			+ "user_status, "
			+ "user_type, "
			+ "created_by_user_id, "
			+ "created_at, "
			+ "updated_at "
			+ "FROM "
			+ "USER_M "
			+ "ORDER BY "
			+ "created_at";

	/**
	 * SQL ユーザ担任・学生情報を全件取得
	 * ※ ユーザ共通情報も含まれています。
	 */
	private static final String SQL_SELECT_TEACHER_STUDENT_ALL = "SELECT "
			+ "UM.user_id, "
			+ "UM.encrypted_password, "
			+ "UM.user_name, "
			+ "UM.user_status, "
			+ "UM.user_type, "
			+ "UM.created_by_user_id, "
			+ "UM.created_at, "
			+ "UM.updated_at, "
			+ "TSUM.department, "
			+ "TSUM.grade, "
			+ "TSUM.class_name, "
			+ "TSUM.attendance_number, "
			+ "TSUM.student_id "
			+ "FROM "
			+ "USER_M AS UM "
			+ "LEFT OUTER JOIN TEACHER_STUDENT_USER_M AS TSUM "
			+ "ON UM.user_id = TSUM.user_id "
			+ "ORDER BY "
			+ "created_at";

	/**
	 * SQL ユーザ共通情報を1件取得
	 */
	private static final String SQL_SELECT_USER_ONE = "SELECT "
			+ "user_id, "
			+ "encrypted_password, "
			+ "user_name, "
			+ "user_status, "
			+ "user_type, "
			+ "created_by_user_id, "
			+ "created_at, "
			+ "updated_at "
			+ "FROM"
			+ " USER_M "
			+ "WHERE "
			+ "user_id = :userId";

	/**
	 * SQL ユーザ担任・学生情報を1件取得
	 * ※ ユーザ共通情報も含まれています。
	 */
	private static final String SQL_SELECT_TEACHER_STUDENT_ONE = "SELECT "
			+ "UM.user_id, "
			+ "UM.encrypted_password, "
			+ "UM.user_name, "
			+ "UM.user_status, "
			+ "UM.user_type, "
			+ "UM.created_by_user_id, "
			+ "UM.created_at, "
			+ "UM.updated_at, "
			+ "TSUM.department, "
			+ "TSUM.grade, "
			+ "TSUM.class_name, "
			+ "TSUM.attendance_number, "
			+ "TSUM.student_id "
			+ "FROM "
			+ "USER_M AS UM "
			+ "LEFT OUTER JOIN TEACHER_STUDENT_USER_M AS TSUM "
			+ "ON UM.user_id = TSUM.user_id "
			+ "WHERE "
			+ "UM.user_id = :userId";

	/**
	 * SQL ユーザの名前を取得
	 */
	private static final String SQL_SELECT_USER_NAME_ONE = "SELECT "
			+ "user_name "
			+ "FROM "
			+ "USER_M "
			+ "WHERE "
			+ "user_id = :userId";

	/**
	 * SQL ユーザの区分を取得
	 */
	private static final String SQL_SELECT_USER_TYPE_ONE = "SELECT "
			+ "user_type "
			+ "FROM "
			+ "USER_M "
			+ "WHERE "
			+ "user_id = :userId";

	/**
	 * SQL 有効状態のユーザのカウントを取得
	 * 	 */
	private static final String SQL_SELECT_INSERT_CHECK = "SELECT "
			+ "COUNT(*) AS checkcnt "
			+ "FROM "
			+ "USER_M "
			+ "WHERE "
			+ "user_id = :userId ";

	/**
	 * SQL ユーザ共通情報を1件追加
	 */
	private static final String SQL_INSERT_USER_ONE = "INSERT INTO "
			+ "USER_M("
			+ "user_id, "
			+ "encrypted_password, "
			+ "user_name, "
			+ "user_status, "
			+ "user_type, "
			+ "created_by_user_id, "
			+ "created_at, "
			+ "updated_at) "
			+ "VALUES("
			+ ":userId ,"
			+ ":encrypted_password ,"
			+ ":userName ,"
			+ ":userStatus ,"
			+ ":userType ,"
			+ ":createByUserId ,"
			+ ":createdAt ,"
			+ ":updatedAt )";

	/**
	 * SQL ユーザ担任・学生情報を1件追加
	 */
	private static final String SQL_INSERT_TEACHER_STUDENT_ONE = "INSERT INTO "
			+ "TEACHER_STUDENT_USER_M("
			+ "user_id, "
			+ "department, "
			+ "grade, "
			+ "class_name, "
			+ "attendance_number, "
			+ "student_id)"
			+ "VALUES("
			+ ":userId ,"
			+ ":department ,"
			+ ":grade ,"
			+ ":className ,"
			+ ":attendanceNumber ,"
			+ ":studentId )";

	/**
	 * SQL ユーザ共通情報を1件更新(パスワードあり)
	 */
	private static final String SQL_UPDATE_USER_ONE_WITH_PASSWORD = "UPDATE USER_M SET "
			+ "encrypted_password = :encryptedPassword , "
			+ "user_name = :userName , "
			+ "user_status = :userStatus , "
			+ "user_type = :userType , "
			+ "updated_at = :updatedAt "
			+ "WHERE "
			+ "user_id = :userId";

	/**
	 * SQL ユーザ共通情報を1件更新(パスワードなし)
	 */
	private static final String SQL_UPDATE_USER_ONE_WITHOUT_PASSWORD = "UPDATE USER_M SET "
			+ "user_name = :userName , "
			+ "user_status = :userStatus , "
			+ "user_type = :userType , "
			+ "updated_at = :updatedAt "
			+ "WHERE "
			+ "user_id = :userId";

	/**
	 * SQL ユーザ担任・学生情報を1件更新
	 */
	private static final String SQL_UPDATE_TEACHER_STUDENT_ONE = "UPDATE TEACHER_STUDENT_USER_M SET "
			+ "department = :department , "
			+ "grade = :grade , "
			+ "class_name = :className , "
			+ "attendance_number = :attendanceNumber , "
			+ "student_id = :studentId "
			+ "WHERE "
			+ "user_id = :userId";

	/**
	/**
	 * SQL ユーザの状態を更新
	 */
	private static final String SQL_UPDATE_USER_STATUS_ONE = "UPDATE USER_M SET "
			+ "user_status = :userStatus "
			+ "WHERE "
			+ "user_id = :userId";

	/**
	 * SQL ユーザ共通情報を1件削除
	 * ※テーブルの設定でほかに登録されているユーザ情報のテーブルも削除されます。
	 */
	private static final String SQL_DELETE_USER_ONE = "DELETE FROM USER_M "
			+ "WHERE "
			+ "user_id = :userId";

	/**
	 * SQL 削除ユーザテーブルに1件追加
	 */
	private static final String SQL_INSERT_DELETE_USER_ONE = "INSERT INTO "
			+ "DELETED_USER_M ("
			+ "user_id, "
			+ "deleted_at, "
			+ "encrypted_password, "
			+ "user_name, "
			+ "user_status, "
			+ "user_type, "
			+ "created_by_user_id, "
			+ "created_at, "
			+ "updated_at) "
			+ "SELECT "
			+ "user_id, "
			+ ":deletedAt AS deleted_at, "
			+ "encrypted_password, "
			+ "user_name, "
			+ "user_status, "
			+ "user_type, "
			+ "created_by_user_id, "
			+ "created_at, "
			+ "updated_at "
			+ "FROM "
			+ "USER_M "
			+ "WHERE "
			+ "user_id = :userId";

	/**
	 * SQL 削除担任・学生ユーザテーブルに1件追加
	 */
	private static final String SQL_INSERT_DELETE_TEACHER_STUDENT_ONE = "INSERT INTO "
			+ "DELETED_TEACHER_STUDENT_M ("
			+ "user_id, "
			+ "deleted_at, "
			+ "department, "
			+ "grade, "
			+ "class_name, "
			+ "attendance_number, "
			+ "student_id) "
			+ "SELECT "
			+ "user_id, "
			+ ":deletedAt AS deleted_at, "
			+ "department, "
			+ "grade, "
			+ "class_name, "
			+ "attendance_number, "
			+ "student_id "
			+ "FROM "
			+ "TEACHER_STUDENT_USER_M "
			+ "WHERE "
			+ "user_id = :userId";

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
	 * パスワードをエンコードおよび検証するためのエンコーダー。
	 */
	@Autowired
	private PasswordEncoder passwordEncoder;

	/**
	 * 学生ユーザIDから担任ユーザIDを取得します。
	 *
	 * <p>
	 * 指定された学生ユーザIDに対応する担任ユーザIDをデータベースから取得します。
	 * </p>
	 *
	 * @param studentUserId 学生ユーザID
	 * @return 担任ユーザID。該当データが存在しない場合はnullを返却します。
	 */
	public String selectTeacherUserId(String studentUserId) {
		// パラメータを設定
		Map<String, Object> params = dbdc.mapInputValues(SQL_SELECT_TEACHER_USER_ID, studentUserId);

		// SQL_SELECT_TEACHER_USER_IDクエリを実行し、結果を取得
		try {
			return jdbc.queryForObject(SQL_SELECT_TEACHER_USER_ID, params, String.class);
		} catch (EmptyResultDataAccessException e) {
			// 該当するデータが存在しない場合はnullを返却
			return null;
		}
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
	public List<String> selectOfficeUserIdList() {
		// SQLクエリのパラメータを設定
		Map<String, Object> params = new HashMap<>();

		// SQL_SELECT_CSV_CERTIFICATE_ISSUANCEクエリを実行し、結果を取得
		List<Map<String, Object>> result = jdbc.queryForList(SQL_SELECT_OFFICE_USER_ID, params);

		// 結果をString型のリストに変換
		List<String> userList = new ArrayList<>();
		for (Map<String, Object> map : result) {
			userList.add(map.get("user_id").toString()); // ObjectをStringに変換して追加
		}

		return userList;
	}

	/**
	 * 全ユーザの共通データを取得します。
	 *
	 * <p>
	 * データベースから全ユーザレコードを取得し、リスト形式で返却します。<br>
	 * パスワードを含むかどうかは引数`outPassword`で制御します。<br>
	 * <strong>注意:</strong> このメソッドは大量のデータを返却する可能性があるため、利用時には注意してください。
	 * </p>
	 *
	 * @param outPassword パスワードを結果に含めるかどうか（true：パスワードを含める、false：パスワードを含めない）
	 * @return 全ユーザデータのリスト。入力値がnullの場合またはデータが存在しない場合はnullを返却します。
	 */
	public List<UserData> selectUserAll(Boolean outPassword) {
		// 入力値がnullの場合はnullを返却
		if (outPassword == null) {
			return null;
		}

		// SQLクエリを実行して結果を取得
		List<Map<String, Object>> result = jdbc.queryForList(SQL_SELECT_USER_ALL, new HashMap<>());

		// 結果が空の場合はnullを返却
		if (result.isEmpty()) {
			return null;
		}

		// SQLクエリのカラム名を取得
		String[] queryList = dbdc.extractColumnNames(SQL_SELECT_USER_ALL);

		// 結果をNewUserDataのリストに変換して返却
		return result.stream()
				.map(resultData -> packageUser(resultData, queryList, outPassword, false)) // 各結果をNewUserDataに変換
				.collect(Collectors.toList());
	}

	/**
	 * 全ユーザのデータを取得します。
	 *
	 * <p>
	 * データベースから担任と学生の情報を含む全ユーザデータを取得し、リスト形式で返却します。<br>
	 * パスワードを含むかどうかは引数`outPassword`で制御します。<br>
	 * <strong>注意:</strong> このメソッドは大量のデータを返却する可能性があるため、利用時には注意してください。
	 * </p>
	 *
	 * @param outPassword パスワードを結果に含めるかどうか（true：パスワードを含める、false：パスワードを含めない）
	 * @return 担任と学生の全ユーザデータのリスト。入力値がnullの場合またはデータが存在しない場合はnullを返却します。
	 */
	public List<UserData> selectTeacherStudentAll(Boolean outPassword) {
		// 入力値がnullの場合はnullを返却
		if (outPassword == null) {
			return null;
		}

		// SQLクエリを実行して結果を取得
		List<Map<String, Object>> result = jdbc.queryForList(SQL_SELECT_TEACHER_STUDENT_ALL, new HashMap<>());

		// 結果が空の場合はnullを返却
		if (result.isEmpty()) {
			return null;
		}

		// SQLクエリのカラム名を取得
		String[] queryList = dbdc.extractColumnNames(SQL_SELECT_TEACHER_STUDENT_ALL);

		// 結果をNewUserDataのリストに変換して返却
		return result.stream()
				.map(resultData -> packageUser(resultData, queryList, outPassword, true)) // 各結果をNewUserDataに変換
				.collect(Collectors.toList());
	}

	/**
	 * 指定した共通ユーザを取得します。
	 *
	 * <p>
	 * ユーザIDに基づいて1件のユーザデータをデータベースから取得します。<br>
	 * パスワードを含むかどうかは引数`outPassword`で制御します。<br>
	 * ※ログイン認証以外では`outPassword`を`false`に設定してください。
	 * </p>
	 *
	 * @param userId ユーザID（null不可）
	 * @param outPassword パスワードを結果に含めるかどうか（true：パスワードを含む、false：パスワードを含まない）
	 * @return 指定したユーザレコード。`userId`がnullまたはデータが存在しない場合はnullを返却します。
	 */
	public UserData selectUserOne(String userId, Boolean outPassword) {
		// outPasswordがnullの場合はnullを返却
		if (outPassword == null) {
			return null;
		}

		// パラメータを設定
		Map<String, Object> params = dbdc.mapInputValues(SQL_SELECT_USER_ONE, userId);

		// SQLクエリを実行し、結果を取得
		List<Map<String, Object>> result = jdbc.queryForList(SQL_SELECT_USER_ONE, params);

		// 結果が空の場合はnullを返却
		if (result.isEmpty()) {
			return null;
		}

		// 結果の最初のレコードを取得
		Map<String, Object> resultData = result.get(0);

		// SQLクエリのカラム名を取得
		String[] queryList = dbdc.extractColumnNames(SQL_SELECT_USER_ONE);

		// ユーザデータを作成し返却
		return packageUser(resultData, queryList, outPassword, false);
	}

	/**
	 * 指定したユーザを担任と学生の情報を含め取得します。
	 *
	 * <p>
	 * ユーザIDに基づいて1件の担任または学生データをデータベースから取得します。<br>
	 * パスワードを含むかどうかは引数`outPassword`で制御します。<br>
	 * ※ログイン認証以外では`outPassword`を`false`に設定してください。
	 * </p>
	 *
	 * @param userId ユーザID（null不可）
	 * @param outPassword パスワードを結果に含めるかどうか（true：パスワードを含む、false：パスワードを含まない）
	 * @return 指定した担任または学生データのレコード。`userId`がnullまたは該当データが存在しない場合はnullを返却します。
	 */
	public UserData selectTeacherStudentOne(String userId, Boolean outPassword) {
		// outPasswordがnullの場合はnullを返却
		if (outPassword == null) {
			return null;
		}

		// パラメータを設定
		Map<String, Object> params = dbdc.mapInputValues(SQL_SELECT_TEACHER_STUDENT_ONE, userId);

		// SQLクエリを実行し、結果を取得
		List<Map<String, Object>> result = jdbc.queryForList(SQL_SELECT_TEACHER_STUDENT_ONE, params);

		// 結果が空の場合はnullを返却
		if (result.isEmpty()) {
			return null;
		}

		// 結果の最初のレコードを取得
		Map<String, Object> resultData = result.get(0);

		// SQLクエリのカラム名を取得
		String[] queryList = dbdc.extractColumnNames(SQL_SELECT_TEACHER_STUDENT_ONE);

		// ユーザデータを作成し返却
		return packageUser(resultData, queryList, outPassword, true);
	}

	/**
	 * ユーザIDからユーザネームを取得します。
	 *
	 * <p>
	 * 指定されたユーザIDに対応するユーザネームをデータベースから取得します。<br>
	 * データが存在しない場合はnullを返却します。
	 * </p>
	 *
	 * @param userId 検索対象のユーザID（null不可）
	 * @return 該当するユーザのユーザネーム。該当データが存在しない場合はnullを返却します。
	 */
	public String selectUserName(String userId) {
		// パラメータを設定
		Map<String, Object> params = dbdc.mapInputValues(SQL_SELECT_USER_NAME_ONE, userId);

		// SQLクエリを実行し、ユーザネームを直接取得
		try {
			return jdbc.queryForObject(SQL_SELECT_USER_NAME_ONE, params, String.class);
		} catch (EmptyResultDataAccessException e) {
			// 該当するデータが存在しない場合はnullを返却
			return null;
		}
	}

	/**
	 * ユーザIDからユーザが存在するかを確認します。
	 *
	 * <p>
	 * 指定されたユーザIDに対応するユーザがデータベースに存在するかどうかを確認します。<br>
	 * 存在する場合はtrueを返し、存在しない場合はfalseを返します。
	 * </p>
	 *
	 * @param userId 確認したいユーザID（null不可）
	 * @return 該当ユーザが存在する場合はtrue、存在しない場合はfalseを返却します。
	 */
	public Boolean existsUser(String userId) {
		// パラメータを格納するためのマップを作成
		Map<String, Object> params = dbdc.mapInputValues(SQL_SELECT_INSERT_CHECK, userId);

		// SQLクエリを実行し、カウントを取得
		try {
			Integer count = jdbc.queryForObject(SQL_SELECT_INSERT_CHECK, params, Integer.class);

			// カウントが0でなければユーザが存在
			return count != 0;
		} catch (EmptyResultDataAccessException e) {
			// 該当データが存在しない場合はfalseを返却
			return false;
		}
	}

	/**
	 * ユーザIDからユーザ区分を取得します。
	 *
	 * @param userId 取得したいユーザID
	 * @return ユーザ区分。該当するデータが存在しない場合はnullを返却します。
	 */
	public String selectUserType(String userId) {
		// パラメータを設定
		Map<String, Object> params = dbdc.mapInputValues(SQL_SELECT_USER_TYPE_ONE, userId);

		// SQLクエリを実行し、ユーザ区分を直接取得
		try {
			return jdbc.queryForObject(SQL_SELECT_USER_TYPE_ONE, params, String.class);
		} catch (EmptyResultDataAccessException e) {
			// 該当するデータが存在しない場合はnullを返却
			return null;
		}
	}

	/**
	 * ユーザを1件DBに追加します。
	 *
	 * @param userData 追加したいユーザデータ
	 * @return 正常に追加された場合はtrue
	 * @throws IncorrectResultSizeDataAccessException 更新件数が期待値と異なる場合にスローされます。
	 */
	public Boolean insertUserOne(UserData userData) throws IncorrectResultSizeDataAccessException {
		// パスワードをエンコードする
		String password = passwordEncoder.encode(userData.getPassword());

		// 現在のタイムスタンプを取得
		Timestamp createdAt = dbdc.getNowTime();

		// パラメータを格納するためのマップを作成
		Map<String, Object> params = dbdc.mapInputValues(
				SQL_INSERT_USER_ONE,
				userData.getUserId(),
				password,
				userData.getUserName(),
				userData.getUserStatus(),
				userData.getUserType(),
				userData.getCreatedByUserId(),
				createdAt,
				createdAt);

		// SQL_INSERT_USER_ONEクエリを実行し、結果を取得
		int updateRow = jdbc.update(SQL_INSERT_USER_ONE, params);

		// 更新結果を返却
		return updateRow == EXPECTED_UPDATE_COUNT;
	}

	/**
	 * 担任・学生のユーザを1件DBに追加します。
	 *
	 * @param userData 追加したいユーザデータ
	 * @return 正常に追加された場合はtrue
	 * @throws IncorrectResultSizeDataAccessException 更新件数が期待値と異なる場合にスローされます。
	 */
	public Boolean insertTeacherStudentUserOne(UserData userData) throws IncorrectResultSizeDataAccessException {
		// パラメータを格納するためのマップを作成
		Map<String, Object> params = dbdc.mapInputValues(
				SQL_INSERT_TEACHER_STUDENT_ONE,
				userData.getUserId(),
				userData.getDepartment(),
				userData.getGrade(),
				userData.getClassName(),
				userData.getAttendanceNumber(),
				userData.getStudentId());

		// SQL_INSERT_TEACHER_STUDENT_ONEクエリを実行し、結果を取得
		int updateRow = jdbc.update(SQL_INSERT_TEACHER_STUDENT_ONE, params);

		// 更新結果を返却
		return updateRow == EXPECTED_UPDATE_COUNT;
	}

	/**
	 * 削除ユーザを1件DBに追加します。
	 *
	 * @param deletedAt 削除時刻
	 * @param userId 削除するユーザID
	 * @return 正常に追加された場合はtrue
	 * @throws IncorrectResultSizeDataAccessException 更新件数が期待値と異なる場合にスローされます。
	 */
	public Boolean insertDeleteUserOne(Timestamp deletedAt, String userId)
			throws IncorrectResultSizeDataAccessException {
		// パラメータを格納するためのマップを作成
		Map<String, Object> params = dbdc.mapInputValues(SQL_INSERT_DELETE_USER_ONE, deletedAt, userId);

		// SQL_INSERT_DELETE_USER_ONEクエリを実行し、結果を取得
		int updateRow = jdbc.update(SQL_INSERT_DELETE_USER_ONE, params);

		// 更新結果を返却
		return updateRow == EXPECTED_UPDATE_COUNT;
	}

	/**
	 * 削除する担任・学生ユーザを1件DBに追加します。
	 *
	 * @param deletedAt 削除時刻
	 * @param userId 削除するユーザID
	 * @return 正常に追加された場合はtrue
	 * @throws IncorrectResultSizeDataAccessException 更新件数が期待値と異なる場合にスローされます。
	 */
	public Boolean insertDeleteTeacherStudentUserOne(Timestamp deletedAt, String userId)
			throws IncorrectResultSizeDataAccessException {
		// パラメータを格納するためのマップを作成
		Map<String, Object> params = dbdc.mapInputValues(SQL_INSERT_DELETE_TEACHER_STUDENT_ONE, deletedAt, userId);

		// SQL_INSERT_DELETE_TEACHER_STUDENT_ONEクエリを実行し、結果を取得
		int updateRow = jdbc.update(SQL_INSERT_DELETE_TEACHER_STUDENT_ONE, params);

		// 更新結果を返却
		return updateRow == EXPECTED_UPDATE_COUNT;
	}

	/**
	 * ユーザ状態を更新します。
	 *
	 * <p>
	 * 更新件数が期待値と異なる場合は例外が発生します。
	 * </p>
	 *
	 * @param userId ユーザID
	 * @param state ユーザ状態
	 * @return 正常に更新が完了した場合はtrue
	 * @throws IncorrectResultSizeDataAccessException 更新件数が期待値と異なる場合
	 */
	public Boolean updateUserStateOne(String userId, String state) throws IncorrectResultSizeDataAccessException {
		// パラメータを格納するためのマップを作成
		Map<String, Object> params = dbdc.mapInputValues(SQL_UPDATE_USER_STATUS_ONE, state, userId);

		// ユーザの状態を更新
		int updateRow = jdbc.update(SQL_UPDATE_USER_STATUS_ONE, params);

		// 更新結果を返却
		return updateRow == EXPECTED_UPDATE_COUNT;
	}

	/**
	 * ユーザを1件DBで更新します。
	 * パスワードを含む場合と含まない場合を条件で分けて実行します。
	 *
	 * @param userData 更新したいユーザデータ
	 * @param includePassword パスワードを含めて更新するかのフラグ
	 * @return 正常に更新された場合はtrue
	 * @throws IncorrectResultSizeDataAccessException 更新件数が期待値と異なる場合にスローされます。
	 */
	public Boolean updateUserOne(UserData userData, Boolean includePassword)
			throws IncorrectResultSizeDataAccessException {
		// 実行するSQLとパラメータを格納する変数を定義
		String sql;
		Map<String, Object> params;
		Timestamp updatedAt = dbdc.getNowTime();

		if (includePassword) {
			// パスワードを含めた更新
			String password = passwordEncoder.encode(userData.getPassword()); // パスワードをエンコード
			sql = SQL_UPDATE_USER_ONE_WITH_PASSWORD;
			// パラメータを設定
			params = dbdc.mapInputValues(sql, password, userData.getUserName(),
					userData.getUserStatus(), userData.getUserType(), updatedAt, userData.getUserId());
		} else {
			// パスワードを含まない更新
			sql = SQL_UPDATE_USER_ONE_WITHOUT_PASSWORD;
			// パラメータを設定
			params = dbdc.mapInputValues(sql, userData.getUserName(), userData.getUserStatus(),
					userData.getUserType(), updatedAt, userData.getUserId());
		}

		// SQLクエリを実行し、更新件数を取得
		int updateRow = jdbc.update(sql, params);

		// 更新結果を返却
		return updateRow == EXPECTED_UPDATE_COUNT;
	}

	/**
	 * 担任・学生ユーザを1件DBで更新します。
	 *
	 * @param userData 更新したいユーザデータ
	 * @return 正常に更新された場合はtrue
	 * @throws IncorrectResultSizeDataAccessException 更新件数が期待値と異なる場合にスローされます。
	 */
	public Boolean updateTeacherStudentOne(UserData userData) throws IncorrectResultSizeDataAccessException {
		// パラメータを格納するためのマップを作成
		Map<String, Object> params = dbdc.mapInputValues(
				SQL_UPDATE_TEACHER_STUDENT_ONE,
				userData.getDepartment(),
				userData.getGrade(),
				userData.getClassName(),
				userData.getAttendanceNumber(),
				userData.getStudentId(),
				userData.getUserId());

		// SQL_UPDATE_TEACHER_STUDENT_ONEクエリを実行し、結果を取得
		int updateRow = jdbc.update(SQL_UPDATE_TEACHER_STUDENT_ONE, params);

		// 更新結果を返却
		return updateRow == EXPECTED_UPDATE_COUNT;
	}

	/**
	 * ユーザを1件DBから削除します。
	 *
	 * @param userId 削除したいユーザID
	 * @return 正常に削除された場合はtrue
	 * @throws IncorrectResultSizeDataAccessException 更新件数が期待値と異なる場合にスローされます。
	 */
	public Boolean deleteUserOne(String userId) throws IncorrectResultSizeDataAccessException {
		// パラメータを格納するためのマップを作成
		Map<String, Object> params = dbdc.mapInputValues(SQL_DELETE_USER_ONE, userId);

		// SQL_DELETE_USER_ONEクエリを実行し、結果を取得
		int updateRow = jdbc.update(SQL_DELETE_USER_ONE, params);

		// 更新結果を返却
		return updateRow == EXPECTED_UPDATE_COUNT;
	}

	/**
	 * データベースから取得したユーザ情報を格納します。
	 *
	 * <p>
	 * 基本のユーザ情報を格納し、ユーザタイプが担任または学生の場合は追加情報も設定します。
	 * </p>
	 *
	 * @param resultData SQLで取得したデータ（マップ形式）
	 * @param queryList SQLで取得した項目名のリスト
	 * @param outPassword パスワードを含めるかのフラグ (true: 含める)
	 * @param userFlg 担任や学生の情報を含めるかのフラグ (true: 含める)
	 * @return 格納されたユーザ情報オブジェクト
	 */
	private UserData packageUser(Map<String, Object> resultData, String[] queryList, Boolean outPassword,
			Boolean userFlg) {
		// ユーザ情報オブジェクトを作成
		UserData userData = new UserData();

		// 基本情報を設定
		userData.setUserId(dbdc.getStringValue(resultData, queryList[0])); // ユーザIDを設定
		if (outPassword) { // パスワードを含める場合
			userData.setPassword(dbdc.getStringValue(resultData, queryList[1])); // パスワードを設定
		}
		userData.setUserName(dbdc.getStringValue(resultData, queryList[2])); // ユーザ名を設定
		userData.setUserStatus(dbdc.getStringValue(resultData, queryList[3])); // ユーザ状態を設定
		userData.setUserType(dbdc.getStringValue(resultData, queryList[4])); // ユーザタイプを設定
		userData.setCreatedByUserId(dbdc.getStringValue(resultData, queryList[5])); // 作成者IDを設定
		userData.setCreatedAt(dbdc.getTimestampValue(resultData, queryList[6])); // 作成日時を設定
		userData.setUpdateAt(dbdc.getTimestampValue(resultData, queryList[7])); // 更新日時を設定

		// ユーザタイプが担任または学生の場合、追加情報を設定
		Integer userType = Integer.parseInt(userData.getUserType());
		if (userFlg && userType <= 1) {
			packageTeacherStudentUser(resultData, queryList, userData);
		}

		// 完成したユーザ情報を返却
		return userData;
	}

	/**
	 * 担任または学生の追加情報を格納します。
	 *
	 * @param resultData SQLで取得したデータ（マップ形式）
	 * @param queryList SQLで取得した項目名のリスト
	 * @param userData 格納対象のユーザ情報オブジェクト
	 * @return 完成したユーザ情報オブジェクト
	 */
	private UserData packageTeacherStudentUser(Map<String, Object> resultData, String[] queryList,
			UserData userData) {
		// 担任または学生の追加情報を設定
		userData.setDepartment(dbdc.getStringValue(resultData, queryList[8])); // 学科情報を設定
		userData.setGrade(dbdc.getIntegerValue(resultData, queryList[9])); // 学年を設定
		userData.setClassName(dbdc.getStringValue(resultData, queryList[10])); // クラス名を設定
		userData.setAttendanceNumber(dbdc.getStringValue(resultData, queryList[11])); // 出席番号を設定
		userData.setStudentId(dbdc.getIntegerValue(resultData, queryList[12])); // 学籍番号を設定

		// 完成したユーザ情報を返却
		return userData;
	}

}
