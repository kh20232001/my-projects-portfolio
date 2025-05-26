package com.api.domain.repositories;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.api.domain.models.dbdata.CertificateData;
import com.api.domain.models.dbdata.CertificateIssuanceDetailData;
import com.api.domain.models.dbdata.CertificateIssuanceEntity;
import com.api.domain.models.dbdata.MailingData;
import com.api.jobpal.common.base.DBDataConversion;

/**
 * 証明書発行に関わるDBアクセスを実現するクラスです。
 *
 * <p>
 * 以下の処理を行います。
 * <ul>
 * <li>証明書発行情報、状態、最大IDの取得</li>
 * <li>郵送情報、証明書リストの取得</li>
 * <li>証明書発行情報、詳細情報の追加</li>
 * <li>紙・電子・郵送証明書発行情報の追加</li>
 * <li>証明書発行状態、媒体区分、詳細情報の更新</li>
 * <li>証明書発行の削除</li>
 * </ul>
 * <p>
 * 処理が継続できない場合は、呼び出し元へ例外をスローします。<br>
 * <strong>呼び出し元では適切な例外処理を行ってください。</strong>
 */

@Repository
public class CertificateIssuanceRepository {

	/**
	 * SQL 証明書発行最大IDを取得
	 */
	private static final String SQL_SELECT_CERTIFICATE_ISSUANCE_USER_ID = "SELECT "
			+ "student_user_id,"
			+ "teacher_user_id,"
			+ "office_user_id "
			+ "FROM "
			+ "CERTIFICATE_ISSUANCE_T "
			+ "WHERE "
			+ "CERTIFICATE_ISSUE_ID = :CertificateIssueId";

	private static final String SQL_SELECT_CERTIFICATE_ISSUANCE_ID_MAX = "SELECT " +
			"MAX(certificate_issue_id) AS max_id " +
			"FROM " +
			"CERTIFICATE_ISSUANCE_T";

	/**
	 * SQL 証明書発行状態を取得
	 */
	private static String SQL_SELECT_CERTIFICATE_ISSUANCE_STATUS_ONE = "SELECT " +
			"status " +
			"FROM " +
			"CERTIFICATE_ISSUANCE_T " +
			"WHERE " +
			"certificate_issue_id = :certificateIssueId";
	/**
	 * SQL 証明書発行状態を取得
	 */
	private static String SQL_SELECT_CERTIFICATE_ISSUANCE_APPROVAL_DATE_ONE = "SELECT " +
			"certificate_issue_id, " +
			"approval_date " +
			"FROM " +
			"CERTIFICATE_ISSUANCE_T " +
			"WHERE " +
			"status = '1'";

	/**
	 * SQL 郵送料金IDの最大値を取得
	 */
	private static final String SQL_SELECT_POSTAL_PAYMENT_ID_MAX = "SELECT " +
			"MAX(postal_payment_id) AS max_id " +
			"FROM " +
			"MAILING_M";

	/**
	 * SQL 全証明書情報を取得
	 */
	private static final String SQL_SELECT_CERTIFICATE_ALL = "SELECT " +
			"certificate_id, " +
			"certificate_name, " +
			"certificate_fee, " +
			"certificate_weight " +
			"FROM " +
			"CERTIFICATE_M";

	/**
	 * SQL 最新の郵送料金情報を取得
	 */
	private static final String SQL_SELECT_POSTAL_ONE = "SELECT " +
			"postal_fee, " +
			"postal_max_weight " +
			"FROM " +
			"MAILING_M " +
			"WHERE " +
			"postal_payment_id = (" +
			"SELECT " +
			"MAX(postal_payment_id) " +
			"FROM " +
			"MAILING_M" +
			");";

	/**
	 * SQL 証明書発行情報を1件追加
	 */
	private static String SQL_INSERT_CERTIFICATE_ISSUANCE_ONE = "INSERT INTO " +
			"CERTIFICATE_ISSUANCE_T (" +
			"certificate_issue_id, " +
			"status, " +
			"student_user_id, " +
			"application_date, " +
			"media_type, " +
			"teacher_user_id " +
			") VALUES (" +
			":certificateIssueId , " +
			":status , " +
			":studentUserId , " +
			":applicationDate , " +
			":mediaType , " +
			":teacherUserId )";

	/**
	 * SQL 証明書発行詳細情報を1件追加
	 */
	private static String SQL_INSERT_CERTIFICATE_ISSUANCE_DETAIL_ONE = "INSERT INTO " +
			"CERTIFICATE_ISSUANCE_DETAIL_T (" +
			"certificate_issue_id, " +
			"certificate_id, " +
			"certificate_quantity " +
			") VALUES (" +
			":certificateIssueId , " +
			":certificateId , " +
			":certificateQuantity )";

	/**
	 * SQL 紙証明書発行情報を1件追加
	 */
	private static String SQL_INSERT_PAPER_CERTIFICATE_ISSUANCE_ONE = "INSERT INTO " +
			"PAPER_CERTIFICATE_ISSUANCE_T (" +
			"certificate_issue_id, " +
			"delivery_due_date " +
			") VALUES (" +
			":certificateIssueId , " +
			":deliveryDueDate )";

	/**
	 * SQL 電子証明書発行情報を1件追加
	 */
	private static String SQL_INSERT_ELECTRONIC_CERTIFICATE_ISSUANCE_ONE = "INSERT INTO " +
			"ELECTRONIC_CERTIFICATE_ISSUANCE_T (" +
			"certificate_issue_id, " +
			"recipient_email_address " +
			") VALUES (" +
			":certificateIssueId , " +
			":recipientEmailAddress )";

	/**
	 * SQL 郵送証明書発行情報を1件追加
	 */
	private static String SQL_INSERT_MAILING_CERTIFICATE_ISSUANCE_ONE = "INSERT INTO " +
			"MAILING_CERTIFICATE_ISSUANCE_T (" +
			"certificate_issue_id, " +
			"postal_payment_id, " +
			"recipient_name, " +
			"recipient_furigana, " +
			"recipient_address " +
			") VALUES (" +
			":certificateIssueId , " +
			":postalPaymentId , " +
			":recipientName , " +
			":recipientFurigana , " +
			":recipientAddress )";

	/**
	 * SQL 新規郵送情報を1件追加
	 */
	private static String SQL_INSERT_MAILING_ONE = "INSERT INTO " +
			"MAILING_M (" +
			"postal_payment_id, " +
			"postal_fee, " +
			"postal_max_weight " +
			") VALUES (" +
			":postalPaymentId , " +
			":postalFee , " +
			":postalMaxWeight )";

	/**
	 * SQL 証明書状態を更新
	 */
	private static String SQL_UPDATE_CERTIFICATE_ISSUANCE_STATUS_ONE = "UPDATE " +
			"CERTIFICATE_ISSUANCE_T " +
			"SET " +
			"status = :status " +
			"WHERE " +
			"certificate_issue_id = :certificateIssueId";

	/**
	 * SQL 証明書担任承認フラグを更新
	 */
	private static String SQL_UPDATE_CERTIFICATE_ISSUANCE_TEACHER_CHECK_FLAG_ONE = "UPDATE " +
			"CERTIFICATE_ISSUANCE_T " +
			"SET " +
			"teacher_check_flag = :teacherCheckFlag " +
			"WHERE " +
			"certificate_issue_id = :certificateIssueId";

	/**
	 * SQL 証明書事務部追加情報を更新
	 */
	private static String SQL_UPDATE_CERTIFICATE_ISSUANCE_OFFICE_USER_ID_ONE = "UPDATE " +
			"CERTIFICATE_ISSUANCE_T " +
			"SET " +
			"office_user_id = :officeUserId " +
			"WHERE " +
			"certificate_issue_id = :certificateIssueId";

	/**
	 * SQL 証明書完了日付を更新
	 */
	private static String SQL_UPDATE_CERTIFICATE_ISSUANCE_APPROVAL_DATE_ONE = "UPDATE " +
			"CERTIFICATE_ISSUANCE_T " +
			"SET " +
			"approval_date = :approvalDate " +
			"WHERE " +
			"certificate_issue_id = :certificateIssueId";

	/**
	 * SQL 紙証明書発行の受け渡し日時を更新
	 */
	private static String SQL_UPDATE_PAPER_CERTIFICATE_ISSUANCE_DELIVERY_DATE_ONE = "UPDATE " +
			"PAPER_CERTIFICATE_ISSUANCE_T " +
			"SET " +
			"delivery_date = :deliveryDate " +
			"WHERE " +
			"certificate_issue_id = :certificateIssueId";

	/**
	 * SQL 郵送証明書発行情報を更新
	 */
	private static String SQL_UPDATE_MAILING_CERTIFICATE_ISSUANCE_ONE = "UPDATE " +
			"MAILING_CERTIFICATE_ISSUANCE_T " +
			"SET " +
			"recipient_name = :recipientName , " +
			"recipient_furigana = :recipientFurigana , " +
			"recipient_address = :recipientAddress " +
			"WHERE " +
			"certificate_issue_id = :certificateIssueId";

	/**
	 * SQL 郵送証明書発行の郵送日付を更新
	 */
	private static String SQL_UPDATE_MAILING_CERTIFICATE_ISSUANCE_POST_DATE_ONE = "UPDATE " +
			"MAILING_CERTIFICATE_ISSUANCE_T " +
			"SET " +
			"post_date = :postDate " +
			"WHERE " +
			"certificate_issue_id = :certificateIssueId";

	/**
	 * SQL 証明書発行区分を更新
	 */
	private static String SQL_UPDATE_CERTIFICATE_ISSUANCE_MEDIA_TYPE_ONE = "UPDATE " +
			"CERTIFICATE_ISSUANCE_T " +
			"SET " +
			"media_type = :mediaType " +
			"WHERE " +
			"certificate_issue_id = :certificateIssueId";

	/**
	 * SQL 証明書発行詳細を更新
	 */
	private static String SQL_UPDATE_CERTIFICATE_ISSUANCE_DETAIL_ONE = "UPDATE " +
			"CERTIFICATE_ISSUANCE_DETAIL_T " +
			"SET " +
			"certificate_quantity = :certificateQuantity " +
			"WHERE " +
			"certificate_issue_id = :certificateIssueId " +
			"AND certificate_id = :certificateId";

	/**
	 * SQL 証明書発行情報を1件削除（物理削除）
	 */
	private static String SQL_DELETE_CERTIFICATE_ISSUANCE_ONE = "DELETE FROM " +
			"CERTIFICATE_ISSUANCE_T " +
			"WHERE " +
			"certificate_issue_id = :certificateIssueId";

	/**
	 * 予想更新件数(ハードコーディング防止用)
	 */
	private static final int EXPECTED_UPDATE_COUNT = 1;

	/**
	 * 証明書発行に関連する日付情報の更新SQLクエリを保持するテーブルのマップ。
	 */
	private static final Map<String, String> DATE_TABLE_MAP = new HashMap<>();

	static {
		// 紙証明書の発行日を更新するSQLクエリ
		DATE_TABLE_MAP.put("P", SQL_UPDATE_PAPER_CERTIFICATE_ISSUANCE_DELIVERY_DATE_ONE);
		// 郵送証明書の発送日を更新するSQLクエリ
		DATE_TABLE_MAP.put("M", SQL_UPDATE_MAILING_CERTIFICATE_ISSUANCE_POST_DATE_ONE);
		// 証明書発行の承認日を更新するSQLクエリ
		DATE_TABLE_MAP.put("A", SQL_UPDATE_CERTIFICATE_ISSUANCE_APPROVAL_DATE_ONE);
	}

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
	 * 証明書発行の状態を取得します。
	 *
	 * <p>
	 * 指定された証明書発行IDに対応する証明書発行状態をデータベースから取得します。
	 * </p>
	 *
	 * @param certificateIssuanceId 証明書発行ID
	 * @return 証明書発行状態。該当データが存在しない場合はnullを返却します。
	 */
	public String selectCertificateIssuanceStatusOne(String certificateIssuanceId) {
		// パラメータを設定
		Map<String, Object> params = dbdc.mapInputValues(SQL_SELECT_CERTIFICATE_ISSUANCE_STATUS_ONE,
				certificateIssuanceId);

		// SQL_SELECT_CERTIFICATE_ISSUANCE_STATUS_ONEクエリを実行し、結果を取得
		try {
			return jdbc.queryForObject(SQL_SELECT_CERTIFICATE_ISSUANCE_STATUS_ONE, params, String.class);
		} catch (EmptyResultDataAccessException e) {
			// 該当するデータが存在しない場合はnullを返却
			return null;
		}
	}

	/**
	 * 最大郵送IDを取得します。
	 *
	 * <p>
	 * 郵送証明書発行トランや郵送マスタを保存する際にこの値が必要です。
	 * </p>
	 *
	 * @return 最大郵送ID。該当データが存在しない場合はnullを返却します。
	 */
	public String selectPostalPaymentIdMax() {
		// パラメータを設定（特に不要なため空マップ）
		Map<String, Object> params = new HashMap<>();

		// SQL_SELECT_POSTAL_PAYMENT_ID_MAXクエリを実行し、結果を取得
		try {
			return jdbc.queryForObject(SQL_SELECT_POSTAL_PAYMENT_ID_MAX, params, String.class);
		} catch (EmptyResultDataAccessException e) {
			// 該当するデータが存在しない場合はnullを返却
			return null;
		}
	}

	/**
	 * 証明書発行に関するユーザ情報を取得します。
	 *
	 * <p>
	 * 指定された証明書発行IDに基づいて、学生、担任、事務局のユーザ情報を取得し、
	 * キーと値のペア形式で返却します。
	 * </p>
	 *
	 * @param certificateIssueId 証明書発行ID
	 * @return 証明書発行に関連するユーザ情報のマップ。データが存在しない場合は空のマップを返却します。
	 */
	public Map<String, String> selectCertificateIssueUserId(String certificateIssueId) {
		// パラメータを設定
		Map<String, Object> params = dbdc.mapInputValues(SQL_SELECT_CERTIFICATE_ISSUANCE_USER_ID, certificateIssueId);

		// SQLクエリを実行し、結果を取得
		List<Map<String, Object>> result = jdbc.queryForList(SQL_SELECT_CERTIFICATE_ISSUANCE_USER_ID, params);

		// 結果が空の場合は空のマップを返却
		if (result.isEmpty()) {
			return new HashMap<>();
		}

		// カラム名を抽出
		String[] queryList = dbdc.extractColumnNames(SQL_SELECT_CERTIFICATE_ISSUANCE_USER_ID);

		// 結果をマップに変換
		Map<String, String> resultMap = new HashMap<>();
		resultMap.put("student", dbdc.getStringValue(result.get(0), queryList[0]));
		resultMap.put("teacher", dbdc.getStringValue(result.get(0), queryList[1]));
		resultMap.put("office", dbdc.getStringValue(result.get(0), queryList[2]));

		// マップを返却
		return resultMap;
	}

	/**
	 * 全証明書情報を取得します。
	 *
	 * <p>
	 * データベースから全ての証明書情報を取得し、リスト形式で返却します。
	 * </p>
	 *
	 * @return 全証明書情報のリスト。データが存在しない場合は空のリストを返却します。
	 */
	public List<CertificateData> selectCertificateAll() {
		// パラメータを設定（空マップ）
		Map<String, Object> params = new HashMap<>();

		// SQL_SELECT_CERTIFICATE_ALLクエリを実行し、結果を取得
		List<Map<String, Object>> result = jdbc.queryForList(SQL_SELECT_CERTIFICATE_ALL, params);

		// カラム名を抽出
		String[] queryList = dbdc.extractColumnNames(SQL_SELECT_CERTIFICATE_ALL);

		// 結果をCertificateDataリストに変換
		return result.stream().map(data -> {
			CertificateData certificateData = new CertificateData();
			certificateData.setCertificateId(dbdc.getStringValue(data, queryList[0]));
			certificateData.setCertificateName(dbdc.getStringValue(data, queryList[1]));
			certificateData.setCertificateFee(dbdc.getIntegerValue(data, queryList[2]));
			certificateData.setCertificateWeight(dbdc.getIntegerValue(data, queryList[3]));
			return certificateData;
		}).collect(Collectors.toList());
	}

	/**
	 *  証明書発行IDと承認日を含むマップのリストを取得します。
	 *
	 * <p>
	 * 証明書発行IDと承認日を含むマップのリスト形式で返却します。
	 * データが存在しない場合は空のリストを返却します。
	 * </p>
	 *
	 * @return 証明書発行IDと承認日を含むマップのリスト。
	 */
	public List<Map<String, Date>> selectCertificateIssuanceApprovalDateOne() {
		// パラメータを設定（空マップ）
		Map<String, Object> params = new HashMap<>();

		// SQL_SELECT_CERTIFICATE_ISSUANCE_APPROVAL_DATE_ONEクエリを実行し、結果を取得
		List<Map<String, Object>> result = jdbc.queryForList(SQL_SELECT_CERTIFICATE_ISSUANCE_APPROVAL_DATE_ONE, params);

		// 結果をString型のリストに変換
		List<Map<String, Date>> dateMapList = new ArrayList<>();
		for (Map<String, Object> map : result) {
			// 証明書発行IDと承認日を格納するマップ
			Map<String, Date> transformedMap = new HashMap<>();
			String certificateIssueId = dbdc.getStringValue(map, "certificate_issue_id");
			Date approvalDate = dbdc.getDateValue(map, "approval_date");
			// データをマップに追加
			transformedMap.put(certificateIssueId, approvalDate);

			// 結果リストに追加
			dateMapList.add(transformedMap);
		}

		// 結果を返却
		return dateMapList;
	}

	/**
	 * 最新の郵送情報を取得します。
	 *
	 * <p>
	 * データベースから最新の郵送料金と最大重量の情報を取得し、MailingDataオブジェクトとして返却します。
	 * </p>
	 *
	 * @return 最新の郵送情報。データが存在しない場合はnullを返却します。
	 */
	public MailingData selectMailing() {
		// パラメータを設定（空マップ）
		Map<String, Object> params = new HashMap<>();

		// SQL_SELECT_POSTAL_ONEクエリを実行し、結果を取得
		try {
			Map<String, Object> result = jdbc.queryForList(SQL_SELECT_POSTAL_ONE, params).get(0);

			// カラム名を抽出
			String[] queryList = dbdc.extractColumnNames(SQL_SELECT_POSTAL_ONE);

			// MailingDataオブジェクトを作成してデータを設定
			MailingData mailingData = new MailingData();
			mailingData.setPostalFee(dbdc.getIntegerValue(result, queryList[0]));
			mailingData.setPostalMaxWeight(dbdc.getIntegerValue(result, queryList[1]));

			return mailingData;
		} catch (IndexOutOfBoundsException e) {
			// データが存在しない場合はnullを返却
			return null;
		}
	}

	/**
	 * 新しい証明書発行IDを生成します。
	 *
	 * <p>
	 * データベース内の最大証明書発行IDを取得し、それに基づいて新しい証明書発行IDを生成します。
	 * </p>
	 *
	 * @return 新しく生成された証明書発行ID。
	 */
	public String getCertificateIssuanceId() {
		// SQL_SELECT_CERTIFICATE_ISSUANCE_ID_MAXクエリを実行し、結果を取得
		return dbdc.getLargerId(SQL_SELECT_CERTIFICATE_ISSUANCE_ID_MAX, "CI");
	}

	/**
	 * 証明書発行情報を1件追加します。
	 *
	 * <p>
	 * 指定された証明書発行情報をデータベースに追加します。更新件数が期待値と異なる場合は
	 * falseを返却します。
	 * </p>
	 *
	 * @param certificateIssuanceEntity 証明書発行情報を保持するエンティティ。
	 *                                   以下の値を保持する必要があります:
	 *                                   <ul>
	 *                                   <li>certificateIssuanceId</li>
	 *                                   <li>student_user_id</li>
	 *                                   <li>media_type</li>
	 *                                   <li>teacher_user_id</li>
	 *                                   </ul>
	 * @return 処理成功時はtrue、失敗時はfalseを返却します。
	 */
	public Boolean insertCertificateIssuanceOne(CertificateIssuanceEntity certificateIssuanceEntity) {
		// 現在の日付を取得
		Date today = dbdc.getNowDate();

		// SQLクエリのパラメータを設定
		Map<String, Object> params = dbdc.mapInputValues(
				SQL_INSERT_CERTIFICATE_ISSUANCE_ONE,
				certificateIssuanceEntity.getCertificateIssueId(),
				0, // 初期ステータス
				certificateIssuanceEntity.getStudentUserId(),
				today,
				certificateIssuanceEntity.getMediaType(),
				certificateIssuanceEntity.getTeacherUserId());

		// SQLクエリを実行して更新件数を取得
		int updateRow = jdbc.update(SQL_INSERT_CERTIFICATE_ISSUANCE_ONE, params);

		// 更新結果を返却
		return updateRow == EXPECTED_UPDATE_COUNT;

	}

	/**
	 * 証明書発行詳細情報を1件追加します。
	 *
	 * <p>
	 * 指定された証明書発行IDと証明書発行詳細情報をデータベースに追加します。
	 * </p>
	 *
	 * @param certificateIssuanceId 証明書発行ID
	 * @param certificateIssuanceDetailData 証明書発行詳細情報を保持するデータオブジェクト。
	 *                                      以下の値を保持する必要があります:
	 *                                      <ul>
	 *                                      <li>certificateId</li>
	 *                                      <li>certificateQuantity</li>
	 *                                      </ul>
	 * @return 処理成功時はtrue、失敗時はfalseを返却します。
	 */
	public Boolean insertCertificateIssuanceDetailOne(
			String certificateIssuanceId,
			CertificateIssuanceDetailData certificateIssuanceDetailData) {
		// SQLクエリのパラメータを設定
		Map<String, Object> params = dbdc.mapInputValues(
				SQL_INSERT_CERTIFICATE_ISSUANCE_DETAIL_ONE,
				certificateIssuanceId,
				certificateIssuanceDetailData.getCertificateId(),
				certificateIssuanceDetailData.getCertificateQuantity());

		// SQLクエリを実行して更新件数を取得
		int updateRow = jdbc.update(SQL_INSERT_CERTIFICATE_ISSUANCE_DETAIL_ONE, params);

		// 更新結果を返却
		return updateRow == EXPECTED_UPDATE_COUNT;
	}

	/**
	 * 紙証明書発行情報を1件追加します。
	 *
	 * <p>
	 * 事務部が承認を完了した際に郵送ではない場合に実行されます。<br>
	 * 証明書発行IDと受取期限日を指定して、データベースに新しい紙証明書発行情報を登録します。
	 * </p>
	 *
	 * @param certificateIssuanceId 証明書発行ID
	 * @param deliveryDueDate 受取期限日
	 * @return 処理が成功した場合は`true`、失敗した場合は`false`を返却します。
	 */
	public Boolean insertPaperCertificateIssuanceOne(String certificateIssuanceId, Date deliveryDueDate) {
		Map<String, Object> params = dbdc.mapInputValues(
				SQL_INSERT_PAPER_CERTIFICATE_ISSUANCE_ONE,
				certificateIssuanceId,
				deliveryDueDate);

		// SQLクエリを実行して更新件数を取得
		int updateRow = jdbc.update(SQL_INSERT_PAPER_CERTIFICATE_ISSUANCE_ONE, params);

		// 更新結果を返却
		return updateRow == EXPECTED_UPDATE_COUNT;
	}

	/**
	 * 電子証明書発行情報を1件追加します。
	 *
	 * <p>
	 * 証明書発行を作成した際に区分が電子だった場合に実行されます。<br>
	 * 証明書発行IDとユーザIDを指定して、データベースに新しい電子証明書発行情報を登録します。
	 * </p>
	 *
	 * @param certificateIssuanceId 証明書発行ID
	 * @param userId ユーザID
	 * @return 処理が成功した場合は`true`、失敗した場合は`false`を返却します。
	 */
	public Boolean insertElectronicCertificateIssuanceOne(String certificateIssuanceId, String userId) {
		Map<String, Object> params = dbdc.mapInputValues(
				SQL_INSERT_ELECTRONIC_CERTIFICATE_ISSUANCE_ONE,
				certificateIssuanceId,
				userId);

		// SQLクエリを実行して更新件数を取得
		int updateRow = jdbc.update(SQL_INSERT_ELECTRONIC_CERTIFICATE_ISSUANCE_ONE, params);

		// 更新結果を返却
		return updateRow == EXPECTED_UPDATE_COUNT;
	}

	/**
	 * 郵送証明書発行情報を1件追加します。
	 *
	 * <p>
	 * 証明書発行を作成した際に区分が郵送だった場合に実行されます。<br>
	 * 証明書発行エンティティと郵送料金IDを指定して、データベースに新しい郵送証明書発行情報を登録します。
	 * </p>
	 *
	 * @param certificateIssuanceEntity 証明書発行エンティティ。以下の値を保持する必要があります:
	 *                                   <ul>
	 *                                   <li>certificateIssueId</li>
	 *                                   <li>recipientName</li>
	 *                                   <li>recipientFurigana</li>
	 *                                   <li>recipientAddress</li>
	 *                                   </ul>
	 * @param postalPaymentId 郵送料金ID（他のメソッドで取得）。
	 * @return 処理が成功した場合は`true`、失敗した場合は`false`を返却します。
	 */
	public Boolean insertMailingCertificateIssuanceOne(CertificateIssuanceEntity certificateIssuanceEntity,
			String postalPaymentId) {
		Map<String, Object> params = dbdc.mapInputValues(
				SQL_INSERT_MAILING_CERTIFICATE_ISSUANCE_ONE,
				certificateIssuanceEntity.getCertificateIssueId(),
				postalPaymentId,
				certificateIssuanceEntity.getRecipientName(),
				certificateIssuanceEntity.getRecipientFurigana(),
				certificateIssuanceEntity.getRecipientAddress());

		// SQLクエリを実行して更新件数を取得
		int updateRow = jdbc.update(SQL_INSERT_MAILING_CERTIFICATE_ISSUANCE_ONE, params);

		// 更新結果を返却
		return updateRow == EXPECTED_UPDATE_COUNT;
	}

	/**
	 * 新規郵送情報を1件追加します。
	 *
	 * <p>
	 * 指定された郵送料金と郵送最大重量をデータベースに追加します。
	 * </p>
	 *
	 * @param postalFee 郵送価格
	 * @param postalMaxWeight 郵送最大重量
	 * @return 処理成功時はtrue、失敗時はfalseを返却します。
	 */
	public Boolean insertMailingOne(Integer postalFee, Integer postalMaxWeight) {
		// 新規ID作成
		Integer postalPaymentId = Integer.valueOf(selectPostalPaymentIdMax()) + 1;

		// SQLクエリのパラメータを設定
		Map<String, Object> params = dbdc.mapInputValues(
				SQL_INSERT_MAILING_ONE,
				postalPaymentId,
				postalFee,
				postalMaxWeight);
		int updateRow = jdbc.update(SQL_INSERT_MAILING_ONE, params);

		// 更新結果を返却
		return updateRow == EXPECTED_UPDATE_COUNT;
	}

	/**
	 * 証明書発行状態を更新します。
	 *
	 * <p>
	 * 各状態が変更された際に実行します。
	 * </p>
	 *
	 * @param certificateIssueId 証明書発行ID（条件）
	 * @param status 証明書発行状態
	 * @return 処理成功時はtrue、失敗時はfalseを返却します。
	 */
	public Boolean updateCertificateIssuanceStatusOne(String certificateIssueId, String status) {
		// SQLクエリのパラメータを設定
		Map<String, Object> params = dbdc.mapInputValues(
				SQL_UPDATE_CERTIFICATE_ISSUANCE_STATUS_ONE,
				status,
				certificateIssueId);
		int updateRow = jdbc.update(SQL_UPDATE_CERTIFICATE_ISSUANCE_STATUS_ONE, params);

		// 更新結果を返却
		return updateRow == EXPECTED_UPDATE_COUNT;

	}

	/**
	 * 証明書担任承認フラグを更新します。
	 *
	 * <p>
	 * 担任の承認が変更された際に実行します。
	 * </p>
	 *
	 * @param certificateIssueId 証明書発行ID（条件）
	 * @param teacherCheckFlag 担任承認チェック（true: 承認）
	 * @return 処理成功時はtrue、失敗時はfalseを返却します。
	 */
	public Boolean updateCertificateIssuanceTeacherCheckFlagOne(String certificateIssueId, Boolean teacherCheckFlag) {
		// SQLクエリのパラメータを設定
		Map<String, Object> params = dbdc.mapInputValues(
				SQL_UPDATE_CERTIFICATE_ISSUANCE_TEACHER_CHECK_FLAG_ONE,
				teacherCheckFlag,
				certificateIssueId);
		int updateRow = jdbc.update(SQL_UPDATE_CERTIFICATE_ISSUANCE_TEACHER_CHECK_FLAG_ONE, params);

		// 更新結果を返却
		return updateRow == EXPECTED_UPDATE_COUNT;

	}

	/**
	 * 証明書事務部情報を更新します。
	 *
	 * <p>
	 * 事務部が承認した際に実行します。
	 * </p>
	 *
	 * @param certificateIssueId 証明書発行ID（条件）
	 * @param officeUserId 事務部ユーザID
	 * @return 処理成功時はtrue、失敗時はfalseを返却します。
	 */
	public Boolean updateCertificateIssuanceOfficeUserIdOne(String certificateIssueId, String officeUserId) {
		// SQLクエリのパラメータを設定
		Map<String, Object> params = dbdc.mapInputValues(
				SQL_UPDATE_CERTIFICATE_ISSUANCE_OFFICE_USER_ID_ONE,
				officeUserId,
				certificateIssueId);
		int updateRow = jdbc.update(SQL_UPDATE_CERTIFICATE_ISSUANCE_OFFICE_USER_ID_ONE, params);

		// 更新結果を返却
		return updateRow == EXPECTED_UPDATE_COUNT;

	}

	/**
	 * 紙証明書発行受渡日、郵送証明書発行郵送日、証明書支払日を更新します。
	 *
	 * @param certificateIssueId 証明書発行ID（条件）
	 * @param dateType 日付の登録区分（P: 紙証明書発行受渡日、M: 郵送証明書発行郵送日、A: 証明書支払日更新）
	 * @return 処理成功時はtrue、失敗時はfalseを返却します。
	 */
	public Boolean updateDateOne(String certificateIssueId, String dateType) {
		// 入力値をチェック
		if (!DATE_TABLE_MAP.containsKey(dateType)) {
			// throw new Exception("入力値エラーです。");
			return false;
		}

		// SQLクエリと現在の日付を取得
		String sql = DATE_TABLE_MAP.get(dateType);
		Date today = dbdc.getNowDate();

		// SQLクエリのパラメータを設定
		Map<String, Object> params = dbdc.mapInputValues(sql, today, certificateIssueId);

		// SQLクエリを実行して更新件数を取得
		int updateRow = jdbc.update(sql, params);

		// 更新結果を返却
		return updateRow == EXPECTED_UPDATE_COUNT;

	}

	/**
	 * 証明書発行の媒体区分を更新します。
	 *
	 * @param certificateIssuanceId 証明書発行ID（条件）
	 * @param mediaType 紙・電子・郵送の区分
	 * @return 処理成功時はtrue、失敗時はfalseを返却します。
	 */
	public Boolean updateCertificateIssuanceMediaTypeOne(String certificateIssuanceId, String mediaType) {
		// SQLクエリのパラメータを設定
		Map<String, Object> params = dbdc.mapInputValues(
				SQL_UPDATE_CERTIFICATE_ISSUANCE_MEDIA_TYPE_ONE,
				mediaType,
				certificateIssuanceId);

		// SQLクエリを実行して更新件数を取得
		int updateRow = jdbc.update(SQL_UPDATE_CERTIFICATE_ISSUANCE_MEDIA_TYPE_ONE, params);

		// 更新結果を返却
		return updateRow == EXPECTED_UPDATE_COUNT;

	}

	/**
	 * 証明書発行詳細を更新します。
	 *
	 * <p>
	 * 差し戻し状態で更新し、変更を行う際に実行します。
	 * </p>
	 *
	 * @param certificateIssuanceId 証明書発行ID（条件）
	 * @param certificateIssuanceDetailData 証明書発行明細データ。
	 *                                      以下の値を保持する必要があります:
	 *                                      <ul>
	 *                                      <li>certificateQuantity</li>
	 *                                      <li>certificateId（条件）</li>
	 *                                      </ul>
	 * @return 処理成功時はtrue、失敗時はfalseを返却します。
	 */
	public Boolean updateCertificateIssuanceDetailOne(String certificateIssuanceId,
			CertificateIssuanceDetailData certificateIssuanceDetailData) {
		// SQLクエリのパラメータを設定
		Map<String, Object> params = dbdc.mapInputValues(
				SQL_UPDATE_CERTIFICATE_ISSUANCE_DETAIL_ONE,
				certificateIssuanceDetailData.getCertificateQuantity(),
				certificateIssuanceId,
				certificateIssuanceDetailData.getCertificateId());

		// SQLクエリを実行して更新件数を取得
		int updateRow = jdbc.update(SQL_UPDATE_CERTIFICATE_ISSUANCE_DETAIL_ONE, params);

		// 更新結果を返却
		return updateRow == EXPECTED_UPDATE_COUNT;

	}

	/**
	 * 郵送証明書発行を更新します。
	 *
	 * <p>
	 * 差し戻し状態で更新し、区分が郵送の場合に実行します。
	 * </p>
	 *
	 * @param certificateIssuanceEntity 証明書発行エンティティ。
	 *                                   以下の値を保持する必要があります:
	 *                                   <ul>
	 *                                   <li>recipientName: 受取人名</li>
	 *                                   <li>recipientFurigana: 受取人フリガナ</li>
	 *                                   <li>recipientAddress: 受取人住所</li>
	 *                                   <li>certificateIssueId（条件）: 証明書発行ID</li>
	 *                                   </ul>
	 * @return 処理成功時はtrue、失敗時はfalseを返却します。
	 */
	public Boolean updateMailingCertificateIssuanceOne(CertificateIssuanceEntity certificateIssuanceEntity) {
		// SQLクエリのパラメータを設定
		Map<String, Object> params = dbdc.mapInputValues(
				SQL_UPDATE_MAILING_CERTIFICATE_ISSUANCE_ONE,
				certificateIssuanceEntity.getRecipientName(), // 受取人名
				certificateIssuanceEntity.getRecipientFurigana(), // 受取人フリガナ
				certificateIssuanceEntity.getRecipientAddress(), // 受取人住所
				certificateIssuanceEntity.getCertificateIssueId() // 証明書発行ID（条件）
		);

		// SQLクエリを実行し、更新件数を取得
		int updateRow = jdbc.update(SQL_UPDATE_MAILING_CERTIFICATE_ISSUANCE_ONE, params);

		// 更新結果を返却
		return updateRow == EXPECTED_UPDATE_COUNT;

	}

	/**
	 * 証明書発行を削除します（物理削除）。
	 *
	 * <p>
	 * この処理を実行する際は、関連するメールを送信する必要があります。
	 * </p>
	 *
	 * @param certificateIssueId 証明書発行ID（条件）
	 * @return 処理成功時はtrue、失敗時はfalseを返却します。
	 */
	public Boolean deleteCertificateIssuanceOne(String certificateIssueId) {
		// SQLクエリのパラメータを設定
		Map<String, Object> params = dbdc.mapInputValues(SQL_DELETE_CERTIFICATE_ISSUANCE_ONE, certificateIssueId);

		// SQLクエリを実行し、削除件数を取得
		int updateRow = jdbc.update(SQL_DELETE_CERTIFICATE_ISSUANCE_ONE, params);

		// 更新結果を返却
		return updateRow == EXPECTED_UPDATE_COUNT;

	}

}
