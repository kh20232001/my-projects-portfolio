package com.api.domain.repositories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.api.domain.models.dbdata.CertificateData;
import com.api.domain.models.dbdata.CertificateIssuanceDashBoardData;
import com.api.domain.models.dbdata.CertificateIssuanceDashBoardDetailData;
import com.api.domain.models.dbdata.MailingData;
import com.api.jobpal.common.base.DBDataConversion;

/**
 * 証明書発行ダッシュボードに関わるDBアクセスを実現するクラスです。
 *
 * <p>
 * 以下の処理を行います。
 * <ul>
 * <li>証明書発行ダッシュボードの全件取得（管理者、学生、担任、事務用）</li>
 * <li>証明書発行ダッシュボードの詳細取得</li>
 * <li>証明書のリスト取得</li>
 * <li>取得データのオブジェクト格納処理</li>
 * </ul>
 * <p>
 * 処理が継続できない場合は、呼び出し元へ例外をスローします。<br>
 * <strong>呼び出し元では適切な例外処理を行ってください。</strong>
 */
@Repository
public class CertificateIssuanceDashBoardRepository {

	/**
	 * SQL 証明書発行ダッシュボード全件取得(管理者用)
	 */
	private static String SQL_SELECT_CERTIFICATE_ISSUANCE_DASHBOARD_ADMIN = "SELECT CIT.certificate_issue_id, " +
			"CIT.student_user_id, " +
			"UM.user_name AS student_user_name, " +
			"TSUM.student_id, " +
			"CIT.status, " +
			"CIT.media_type, " +
			"GREATEST( " +
			"COALESCE(CIT.application_date, '1900-01-01'), " +
			"COALESCE(CIT.approval_date, '1900-01-01'), " +
			"COALESCE(PCIT.delivery_due_date, '1900-01-01'), " +
			"COALESCE(PCIT.delivery_date, '1900-01-01'), " +
			"COALESCE(MCIT.post_date, '1900-01-01') " +
			") AS latest_date, " +
			"SUM(CIDT.certificate_quantity * CM.certificate_fee) AS total_fee, " +
			"CIDT.certificate_quantity, " +
			"CM.certificate_id, " +
			"CM.certificate_name, " +
			"CM.certificate_weight " +
			"FROM CERTIFICATE_ISSUANCE_T CIT " +
			"LEFT OUTER JOIN USER_M UM ON CIT.student_user_id = UM.user_id " +
			"LEFT OUTER JOIN TEACHER_STUDENT_USER_M TSUM ON CIT.student_user_id = TSUM.user_id " +
			"LEFT OUTER JOIN PAPER_CERTIFICATE_ISSUANCE_T PCIT ON CIT.certificate_issue_id = PCIT.certificate_issue_id "
			+
			"LEFT OUTER JOIN MAILING_CERTIFICATE_ISSUANCE_T MCIT ON CIT.certificate_issue_id = MCIT.certificate_issue_id "
			+
			"LEFT OUTER JOIN MAILING_M MM ON MCIT.postal_payment_id = MM.postal_payment_id " +
			"LEFT OUTER JOIN CERTIFICATE_ISSUANCE_DETAIL_T CIDT ON CIT.certificate_issue_id = CIDT.certificate_issue_id "
			+
			"LEFT OUTER JOIN CERTIFICATE_M CM ON CIDT.certificate_id = CM.certificate_id " +
			"GROUP BY CIT.certificate_issue_id, CIT.student_user_id, UM.user_name, " +
			"TSUM.student_id, CIT.status, CIT.media_type, MM.postal_fee, MM.postal_max_weight, latest_date, " +
			"CIDT.certificate_quantity, CM.certificate_id,CM.certificate_weight, CM.certificate_name";
	/**
	 * SQL 証明書発行ダッシュボード全件取得 (担任用)
	 */
	private static String SQL_SELECT_CERTIFICATE_ISSUANCE_DASHBOARD_TEACHER = "SELECT CIT.certificate_issue_id, " +
			"CIT.student_user_id, " +
			"UM.user_name AS student_user_name, " +
			"TSUM.student_id, " +
			"CIT.status, " +
			"CIT.media_type, " +
			"GREATEST( " +
			"COALESCE(CIT.application_date, '1900-01-01'), " +
			"COALESCE(CIT.approval_date, '1900-01-01'), " +
			"COALESCE(PCIT.delivery_due_date, '1900-01-01'), " +
			"COALESCE(PCIT.delivery_date, '1900-01-01'), " +
			"COALESCE(MCIT.post_date, '1900-01-01') " +
			") AS latest_date, " +
			"SUM(CIDT.certificate_quantity * CM.certificate_fee) AS total_fee, " +
			"CIDT.certificate_quantity, " +
			"CM.certificate_id, " +
			"CM.certificate_name, " +
			"CM.certificate_weight " +
			"FROM CERTIFICATE_ISSUANCE_T CIT " +
			"LEFT OUTER JOIN USER_M UM ON CIT.student_user_id = UM.user_id " +
			"LEFT OUTER JOIN TEACHER_STUDENT_USER_M TSUM ON CIT.student_user_id = TSUM.user_id " +
			"LEFT OUTER JOIN PAPER_CERTIFICATE_ISSUANCE_T PCIT ON CIT.certificate_issue_id = PCIT.certificate_issue_id "
			+
			"LEFT OUTER JOIN MAILING_CERTIFICATE_ISSUANCE_T MCIT ON CIT.certificate_issue_id = MCIT.certificate_issue_id "
			+
			"LEFT OUTER JOIN MAILING_M MM ON MCIT.postal_payment_id = MM.postal_payment_id " +
			"LEFT OUTER JOIN CERTIFICATE_ISSUANCE_DETAIL_T CIDT ON CIT.certificate_issue_id = CIDT.certificate_issue_id "
			+
			"LEFT OUTER JOIN CERTIFICATE_M CM ON CIDT.certificate_id = CM.certificate_id " +
			"WHERE CIT.teacher_user_id = :teacherUserId " +
			"GROUP BY CIT.certificate_issue_id, CIT.student_user_id, UM.user_name, " +
			"TSUM.student_id, CIT.status, CIT.media_type, MM.postal_fee, MM.postal_max_weight, latest_date, " +
			"CIDT.certificate_quantity, CM.certificate_id,CM.certificate_weight, CM.certificate_name";

	/**
	 * SQL 証明書発行ダッシュボード全件取得（学生用）
	 */
	private static String SQL_SELECT_CERTIFICATE_ISSUANCE_DASHBOARD_STUDENT = "SELECT CIT.certificate_issue_id, " +
			"CIT.student_user_id, " +
			"UM.user_name AS student_user_name, " +
			"TSUM.student_id, " +
			"CIT.status, " +
			"CIT.media_type, " +
			"GREATEST( " +
			"COALESCE(CIT.application_date, '1900-01-01'), " +
			"COALESCE(CIT.approval_date, '1900-01-01'), " +
			"COALESCE(PCIT.delivery_due_date, '1900-01-01'), " +
			"COALESCE(PCIT.delivery_date, '1900-01-01'), " +
			"COALESCE(MCIT.post_date, '1900-01-01') " +
			") AS latest_date, " +
			"SUM(CIDT.certificate_quantity * CM.certificate_fee) AS total_fee, " +
			"CIDT.certificate_quantity, " +
			"CM.certificate_id, " +
			"CM.certificate_name, " +
			"CM.certificate_weight " +
			"FROM CERTIFICATE_ISSUANCE_T CIT " +
			"LEFT OUTER JOIN USER_M UM ON CIT.student_user_id = UM.user_id " +
			"LEFT OUTER JOIN TEACHER_STUDENT_USER_M TSUM ON CIT.student_user_id = TSUM.user_id " +
			"LEFT OUTER JOIN PAPER_CERTIFICATE_ISSUANCE_T PCIT ON CIT.certificate_issue_id = PCIT.certificate_issue_id "
			+
			"LEFT OUTER JOIN MAILING_CERTIFICATE_ISSUANCE_T MCIT ON CIT.certificate_issue_id = MCIT.certificate_issue_id "
			+
			"LEFT OUTER JOIN MAILING_M MM ON MCIT.postal_payment_id = MM.postal_payment_id " +
			"LEFT OUTER JOIN CERTIFICATE_ISSUANCE_DETAIL_T CIDT ON CIT.certificate_issue_id = CIDT.certificate_issue_id "
			+
			"LEFT OUTER JOIN CERTIFICATE_M CM ON CIDT.certificate_id = CM.certificate_id " +
			"WHERE CIT.student_user_id = :studentUserId " +
			"GROUP BY CIT.certificate_issue_id, CIT.student_user_id, UM.user_name, " +
			"TSUM.student_id, CIT.status, CIT.media_type, MM.postal_fee, MM.postal_max_weight, latest_date, " +
			"CIDT.certificate_quantity, CM.certificate_id,CM.certificate_weight, CM.certificate_name";

	/**
	 * SQL 証明書発行ダッシュボード全件取得（事務用）
	 */
	private static String SQL_SELECT_CERTIFICATE_ISSUANCE_DASHBOARD_OFFICE = "SELECT CIT.certificate_issue_id, " +
			"CIT.student_user_id, " +
			"UM.user_name AS student_user_name, " +
			"TSUM.student_id, " +
			"CIT.status, " +
			"CIT.media_type, " +
			"GREATEST(COALESCE(CIT.application_date, '1900-01-01'), " +
			"COALESCE(CIT.approval_date, '1900-01-01'), " +
			"COALESCE(PCIT.delivery_due_date, '1900-01-01'), " +
			"COALESCE(PCIT.delivery_date, '1900-01-01'), " +
			"COALESCE(MCIT.post_date, '1900-01-01')) AS latest_date, " +
			"SUM(CIDT.certificate_quantity * CM.certificate_fee) AS total_fee, " +
			"CIDT.certificate_quantity, " +
			"CM.certificate_id, " +
			"CM.certificate_name, " +
			"CM.certificate_weight " +
			"FROM CERTIFICATE_ISSUANCE_T CIT " +
			"LEFT OUTER JOIN USER_M UM ON CIT.student_user_id = UM.user_id " +
			"LEFT OUTER JOIN TEACHER_STUDENT_USER_M TSUM ON CIT.student_user_id = TSUM.user_id " +
			"LEFT OUTER JOIN PAPER_CERTIFICATE_ISSUANCE_T PCIT ON CIT.certificate_issue_id = PCIT.certificate_issue_id "
			+
			"LEFT OUTER JOIN MAILING_CERTIFICATE_ISSUANCE_T MCIT ON CIT.certificate_issue_id = MCIT.certificate_issue_id "
			+
			"LEFT OUTER JOIN MAILING_M MM ON MCIT.postal_payment_id = MM.postal_payment_id " +
			"LEFT OUTER JOIN CERTIFICATE_ISSUANCE_DETAIL_T CIDT ON CIT.certificate_issue_id = CIDT.certificate_issue_id "
			+
			"LEFT OUTER JOIN CERTIFICATE_M CM ON CIDT.certificate_id = CM.certificate_id " +
			"WHERE (CIT.office_user_id = :officeUserId " +
			"OR CIT.status = '1') " +
			"GROUP BY CIT.certificate_issue_id, CIT.student_user_id, UM.user_name, " +
			"TSUM.student_id, CIT.status, CIT.media_type, MM.postal_fee, MM.postal_max_weight, latest_date, " +
			"CIDT.certificate_quantity, CM.certificate_id,CM.certificate_weight, CM.certificate_name";
	/**
	 * SQL 証明書発行ダッシュボードの詳細を取得
	 */
	private static final String SQL_SELECT_CERTIFICATE_ISSUANCE_DASHBOARD_DETAIL = "SELECT " +
			"CIT.certificate_issue_id, " +
			"CIT.student_user_id, " +
			"TSUM.department, " +
			"TSUM.grade, " +
			"TSUM.class_name, " +
			"TSUM.attendance_number, " +
			"UM.user_name AS student_user_name, " +
			"TSUM.student_id, " +
			"CIT.status, " +
			"CIT.office_user_id, " +
			"OUM.user_name AS office_user_name, " +
			"MCIT.recipient_name, " +
			"MCIT.recipient_furigana, " +
			"MCIT.recipient_address, " +
			"CIT.media_type, " +
			"CIT.application_date, " +
			"CIT.approval_date, " +
			"PCIT.delivery_due_date, " +
			"PCIT.delivery_date, " +
			"MCIT.post_date, " +
			"MM.postal_fee, " +
			"MM.postal_max_weight " +
			"FROM " +
			"CERTIFICATE_ISSUANCE_T CIT " +
			"LEFT OUTER JOIN " +
			"TEACHER_STUDENT_USER_M TSUM ON CIT.student_user_id = TSUM.user_id " +
			"LEFT OUTER JOIN " +
			"USER_M UM ON CIT.student_user_id = UM.user_id " +
			"LEFT OUTER JOIN " +
			"USER_M OUM ON CIT.office_user_id = OUM.user_id " +
			"LEFT OUTER JOIN " +
			"MAILING_CERTIFICATE_ISSUANCE_T MCIT ON CIT.certificate_issue_id = MCIT.certificate_issue_id " +
			"LEFT OUTER JOIN " +
			"PAPER_CERTIFICATE_ISSUANCE_T PCIT ON CIT.certificate_issue_id = PCIT.certificate_issue_id " +
			"LEFT OUTER JOIN " +
			"MAILING_M MM ON MCIT.postal_payment_id = MM.postal_payment_id " +
			"WHERE " +
			"CIT.certificate_issue_id = :certificateIssueId";

	private static final String SQL_SELECT_CERTIFICATE = "SELECT " +
			"CIDT.certificate_id, " +
			"CIDT.certificate_quantity, " +
			"CM.certificate_name, " +
			"CM.certificate_fee, " +
			"CM.certificate_weight " +
			"FROM " +
			"CERTIFICATE_ISSUANCE_DETAIL_T CIDT " +
			"INNER JOIN CERTIFICATE_M CM ON CIDT.certificate_id = CM.certificate_id " +
			"WHERE " +
			"CIDT.certificate_issue_id = :certificateIssueId";

	/**
	 * 各ユーザ種別に対応するSQLクエリを保持するテーブルのマップ。
	 */
	private static final Map<String, String> TABLE_MAP = new HashMap<>();

	static {
		// 学生に対応する証明書発行ダッシュボードのSQLクエリを設定
		TABLE_MAP.put("学生", SQL_SELECT_CERTIFICATE_ISSUANCE_DASHBOARD_STUDENT);
		// 担任に対応する証明書発行ダッシュボードのSQLクエリを設定
		TABLE_MAP.put("担任", SQL_SELECT_CERTIFICATE_ISSUANCE_DASHBOARD_TEACHER);
		// 事務に対応する証明書発行ダッシュボードのSQLクエリを設定
		TABLE_MAP.put("事務", SQL_SELECT_CERTIFICATE_ISSUANCE_DASHBOARD_OFFICE);
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
	 * 証明書発行に関するRepository
	 */
	@Autowired
	private CertificateIssuanceRepository cir;

	/**
	 * ダッシュボードのデータを全件取得します。(管理者用)
	 *
	 * <p>
	 * 管理者向けに、証明書発行のダッシュボードデータを全件取得します。<br>
	 * 各証明書発行データに郵送料や重量を加算して、一覧として返却します。
	 * </p>
	 *
	 * @return ダッシュボードデータのリスト
	 */
	public List<CertificateIssuanceDashBoardData> selectCertificateIssuanceDashBoard() {
		// ダッシュボードデータリストの初期化
		List<CertificateIssuanceDashBoardData> certificateIssuanceDashBoardDataList = new ArrayList<>();

		// クエリパラメータを設定（今回は空のマップ）
		Map<String, Object> params = new HashMap<>();

		// データベースからダッシュボードデータを取得
		List<Map<String, Object>> resultCertificateIssuanceDashBoardList = jdbc
				.queryForList(SQL_SELECT_CERTIFICATE_ISSUANCE_DASHBOARD_ADMIN, params);

		// クエリの列名を抽出
		String[] queryList = dbdc.extractColumnNames(SQL_SELECT_CERTIFICATE_ISSUANCE_DASHBOARD_ADMIN);

		// 郵送料および郵送許容量を取得
		MailingData mailingData = cir.selectMailing();
		Integer mailFee = mailingData.getPostalFee(); // 郵送料
		Integer mailWeight = mailingData.getPostalMaxWeight(); // 郵送可能重量

		// ダッシュボードデータを整形
		for (Map<String, Object> resultData : resultCertificateIssuanceDashBoardList) {
			String certificateIssueId = dbdc.getStringValue(resultData, queryList[0]);

			// 郵送区分フラグの判定
			Boolean mediaTypeFlg = "2".equals(dbdc.getStringValue(resultData, queryList[5]));

			// 既存データの検索
			CertificateIssuanceDashBoardData existingData = findExistingCertificateData(
					certificateIssuanceDashBoardDataList, certificateIssueId);

			if (existingData != null) {
				// 既存データがある場合、証明書データを追加および更新
				CertificateData certificateData = packageCertificateDashBoard(resultData);
				existingData.setUpTotalAmount(dbdc.getIntegerValue(resultData, "total_fee"));

				// 重量計算前後の差分を計算
				Integer beforeTotalWeight = existingData.getTotalWeight();
				existingData.setUpTotalWeight(dbdc.getIntegerValue(resultData, "certificate_quantity")
						* dbdc.getIntegerValue(resultData, "certificate_weight"));
				Integer afterTotalWeight = existingData.getTotalWeight();

				if (mediaTypeFlg) {
					// 郵送料の再計算
					Integer totalBeforeMailFee = (int) (mailFee * Math.ceil((double) beforeTotalWeight / mailWeight));
					Integer totalAfterMailFee = (int) (mailFee * Math.ceil((double) afterTotalWeight / mailWeight));
					existingData.setUpTotalAmount(totalAfterMailFee - totalBeforeMailFee);
				}

				// 証明書リストに追加
				existingData.getCertificateList().add(certificateData);
			} else {
				// 新規データの場合、証明書発行データを作成
				CertificateIssuanceDashBoardData newCertificateIssuanceDashBoardData = packageCertificateIssuanceDashBoard(
						resultData, queryList);

				if (mediaTypeFlg) {
					// 郵送料の計算
					Integer totalMailFee = (int) (mailFee
							* Math.ceil((double) newCertificateIssuanceDashBoardData.getTotalWeight() / mailWeight));
					newCertificateIssuanceDashBoardData.setUpTotalAmount(totalMailFee);
				}

				// ダッシュボードデータリストに追加
				certificateIssuanceDashBoardDataList.add(newCertificateIssuanceDashBoardData);
			}
		}

		// 完成したダッシュボードデータリストを返却
		return certificateIssuanceDashBoardDataList;
	}

	/**
	 * ダッシュボードを取得するメソッド
	 *
	 * <p>
	 * ユーザIDとユーザ区分に基づいて、証明書発行のダッシュボードデータを取得します。<br>
	 * 郵送料や重量計算を含めたデータ整形を行い、ダッシュボードリストを返却します。
	 * </p>
	 *
	 * @param userId   ユーザID
	 * @param userType ユーザ区分 (学生、担任、事務)
	 * @return ダッシュボードリスト。入力エラー時またはデータ取得に失敗した場合は空のリストを返却
	 */
	public List<CertificateIssuanceDashBoardData> selectCertificateIssuanceDashBoard(String userId, String userType) {
		// ダッシュボードデータリストを初期化
		List<CertificateIssuanceDashBoardData> certificateIssuanceDashBoardDataList = new ArrayList<>();

		// ユーザ区分がTABLE_MAPに存在しない場合、空リストを返却
		if (!TABLE_MAP.containsKey(userType)) {
			return certificateIssuanceDashBoardDataList;
		}

		// ユーザ区分に応じたSQLを取得
		String sql = TABLE_MAP.get(userType);

		// SQLに使用するパラメータを設定
		Map<String, Object> params = dbdc.mapInputValues(sql, userId);

		// データベースから結果を取得
		List<Map<String, Object>> resultCertificateIssuanceDashBoardList = jdbc.queryForList(sql, params);

		// クエリの列名を取得
		String[] queryList = dbdc.extractColumnNames(sql);

		// 郵送料および郵送許容量を取得
		MailingData mailingData = cir.selectMailing();
		Integer mailFee = mailingData.getPostalFee(); // 郵送料
		Integer mailWeight = mailingData.getPostalMaxWeight(); // 郵送可能重量

		// データを整形
		for (Map<String, Object> resultData : resultCertificateIssuanceDashBoardList) {
			String certificateIssueId = dbdc.getStringValue(resultData, queryList[0]);

			// 郵送区分チェック
			Boolean mediaTypeFlg = "2".equals(dbdc.getStringValue(resultData, queryList[5]));

			// 既存データの検索
			CertificateIssuanceDashBoardData existingData = findExistingCertificateData(
					certificateIssuanceDashBoardDataList, certificateIssueId);

			if (existingData != null) {
				// 既存データがある場合、証明書データを追加および更新
				CertificateData certificateData = packageCertificateDashBoard(resultData);
				existingData.setUpTotalAmount(dbdc.getIntegerValue(resultData, "total_fee"));

				// 重量計算前後の差分を計算
				Integer beforeTotalWeight = existingData.getTotalWeight();
				existingData.setUpTotalWeight(dbdc.getIntegerValue(resultData, "certificate_quantity")
						* dbdc.getIntegerValue(resultData, "certificate_weight"));
				Integer afterTotalWeight = existingData.getTotalWeight();

				if (mediaTypeFlg) {
					// 郵送料の再計算
					Integer totalBeforeMailFee = (int) (mailFee * Math.ceil((double) beforeTotalWeight / mailWeight));
					Integer totalAfterMailFee = (int) (mailFee * Math.ceil((double) afterTotalWeight / mailWeight));
					existingData.setUpTotalAmount(totalAfterMailFee - totalBeforeMailFee);
				}

				// 証明書リストにデータを追加
				existingData.getCertificateList().add(certificateData);
			} else {
				// 新規データの場合、証明書発行データを作成
				CertificateIssuanceDashBoardData newCertificateIssuanceDashBoardData = packageCertificateIssuanceDashBoard(
						resultData, queryList);

				if (mediaTypeFlg) {
					// 郵送料の計算
					Integer totalMailFee = (int) (mailFee
							* Math.ceil((double) newCertificateIssuanceDashBoardData.getTotalWeight() / mailWeight));
					newCertificateIssuanceDashBoardData.setUpTotalAmount(totalMailFee);
				}

				// ダッシュボードデータリストに追加
				certificateIssuanceDashBoardDataList.add(newCertificateIssuanceDashBoardData);
			}
		}

		// 完成したダッシュボードデータリストを返却
		return certificateIssuanceDashBoardDataList;
	}

	/**
	 * /**
	 * 証明書発行ダッシュボードの詳細データを取得します。
	 *
	 * @param certificateIssuanceId 証明書発行ID (null不可)
	 * @return 証明書発行ダッシュボードの詳細データ。取得失敗時はnullを返却。
	 */
	public CertificateIssuanceDashBoardDetailData selectCertificateIssuanceDashBoardDetail(
			String certificateIssuanceId) {

		// クエリのパラメータを設定
		Map<String, Object> params = dbdc.mapInputValues(SQL_SELECT_CERTIFICATE_ISSUANCE_DASHBOARD_DETAIL,
				certificateIssuanceId);

		// SQL_SELECT_CERTIFICATE_ISSUANCE_DASHBOARD_DETAILクエリを実行し、結果を取得
		List<Map<String, Object>> resultList = jdbc.queryForList(SQL_SELECT_CERTIFICATE_ISSUANCE_DASHBOARD_DETAIL,
				params);

		// 結果が存在しない場合はnullを返却
		if (resultList.isEmpty()) {
			return null;
		}

		// 結果の最初のデータを取得
		Map<String, Object> certificateIssuanceDashBoardDetail = resultList.get(0);

		// SQLからカラム名を取得
		String[] queryList = dbdc.extractColumnNames(SQL_SELECT_CERTIFICATE_ISSUANCE_DASHBOARD_DETAIL);

		// 詳細データを構築して返却
		return packageCertificateIssuanceDashBoardDetail(certificateIssuanceDashBoardDetail, queryList);
	}

	/**
	 * 証明書のリストを取得します。
	 *
	 * @param certificateIssuanceId 証明書発行ID (null不可)
	 * @return 証明書のリストデータ。取得失敗時は空のリストを返却。
	 */
	public List<CertificateData> selectCertificate(String certificateIssuanceId) {
		// 結果を格納するリスト
		List<CertificateData> certificateList = new ArrayList<>();

		// クエリのパラメータを設定
		Map<String, Object> params = dbdc.mapInputValues(SQL_SELECT_CERTIFICATE, certificateIssuanceId);

		// SQLを実行して結果を取得
		List<Map<String, Object>> certificateMap = jdbc.queryForList(SQL_SELECT_CERTIFICATE, params);

		// SQLからカラム名を取得
		String[] queryList = dbdc.extractColumnNames(SQL_SELECT_CERTIFICATE);

		// 取得結果をCertificateDataのリストに変換
		for (Map<String, Object> certificate : certificateMap) {
			CertificateData certificateData = packageCertificate(certificate, queryList);
			certificateList.add(certificateData);
		}

		// 取得したリストを返却
		return certificateList;
	}

	/**
	 * データベースで取得した証明書発行ダッシュボード情報を格納します。
	 *
	 * @param resultData SQLで取得したマップ
	 * @param queryList  SQLで取得した項目名
	 * @return 格納した証明書発行ダッシュボードデータ
	 */
	CertificateIssuanceDashBoardData packageCertificateIssuanceDashBoard(
			Map<String, Object> resultData, String[] queryList) {
		// 証明書発行ダッシュボードデータの初期化
		CertificateIssuanceDashBoardData certificateIssuanceDashBoardData = new CertificateIssuanceDashBoardData();

		// 各項目の設定
		certificateIssuanceDashBoardData.setCertificateIssueId(dbdc.getStringValue(resultData, queryList[0]));
		certificateIssuanceDashBoardData.setStudentUserId(dbdc.getStringValue(resultData, queryList[1]));
		certificateIssuanceDashBoardData.setStudentUserName(dbdc.getStringValue(resultData, "student_user_name"));

		// 学生IDの設定
		certificateIssuanceDashBoardData.setStudentId(dbdc.getIntegerValue(resultData, queryList[3]));

		// ステータス、メディアタイプ、最新日付の設定
		certificateIssuanceDashBoardData.setStatus(dbdc.getStringValue(resultData, queryList[4]));
		certificateIssuanceDashBoardData.setMediaType(dbdc.getStringValue(resultData, queryList[5]));
		certificateIssuanceDashBoardData.setLatestDate(dbdc.getDateValue(resultData, "latest_date"));

		// 合計金額の設定
		certificateIssuanceDashBoardData.setTotalAmount(dbdc.getIntegerValue(resultData, "total_fee"));
		certificateIssuanceDashBoardData.setTotalWeight(dbdc.getIntegerValue(resultData, "certificate_quantity")
				* dbdc.getIntegerValue(resultData, "certificate_weight"));
		// 証明書データのリストを作成し、設定
		CertificateData certificateData = packageCertificateDashBoard(resultData);
		List<CertificateData> certificateDataList = new ArrayList<>();
		certificateDataList.add(certificateData);
		certificateIssuanceDashBoardData.setCertificateList(certificateDataList);

		return certificateIssuanceDashBoardData;
	}

	/**
	 * データベースで取得した証明書発行ダッシュボード詳細情報を格納します。
	 *
	 * @param certificate SQLで取得したマップ
	 * @param queryList   SQLで取得した項目名
	 * @return 格納した証明書データ
	 */
	private CertificateData packageCertificate(Map<String, Object> certificate, String[] queryList) {
		// 証明書データの初期化
		CertificateData certificateData = new CertificateData();

		// 各フィールドを設定
		certificateData.setCertificateId(dbdc.getStringValue(certificate, queryList[0]));
		certificateData.setCertificateQuantity(dbdc.getIntegerValue(certificate, queryList[1]));
		certificateData.setCertificateName(dbdc.getStringValue(certificate, queryList[2]));
		certificateData.setCertificateFee(dbdc.getIntegerValue(certificate, queryList[3]));
		certificateData.setCertificateWeight(dbdc.getIntegerValue(certificate, queryList[4]));

		return certificateData;
	}

	/**
	 * データベースで取得した証明書発行ダッシュボード詳細情報を格納します。
	 *
	 * @param certificate SQLで取得したマップ
	 * @return 格納した証明書データ
	 */
	CertificateData packageCertificateDashBoard(Map<String, Object> certificate) {
		// 証明書データの初期化
		CertificateData certificateData = new CertificateData();

		// 各フィールドを設定
		certificateData.setCertificateQuantity(dbdc.getIntegerValue(certificate, "certificate_quantity"));
		certificateData.setCertificateId(dbdc.getStringValue(certificate, "certificate_id"));
		certificateData.setCertificateName(dbdc.getStringValue(certificate, "certificate_name"));

		return certificateData;
	}

	/**
	 * データベースで取得した証明書発行ダッシュボード詳細情報を格納します。
	 *
	 * @param certificateIssuanceDashBoardDetail SQLで取得したマップ
	 * @param queryList                          SQLで取得した項目名
	 * @return 格納した証明書発行ダッシュボード詳細データ
	 */
	private CertificateIssuanceDashBoardDetailData packageCertificateIssuanceDashBoardDetail(
			Map<String, Object> certificateIssuanceDashBoardDetail, String[] queryList) {
		CertificateIssuanceDashBoardDetailData detailData = new CertificateIssuanceDashBoardDetailData();

		// 文字列データの設定
		detailData.setCertificateIssueId(dbdc.getStringValue(certificateIssuanceDashBoardDetail, queryList[0]));
		detailData.setStudentUserId(dbdc.getStringValue(certificateIssuanceDashBoardDetail, queryList[1]));
		detailData.setDepartment(dbdc.getStringValue(certificateIssuanceDashBoardDetail, queryList[2]));
		detailData.setGrade(dbdc.getStringValue(certificateIssuanceDashBoardDetail, queryList[3]));
		detailData.setClassName(dbdc.getStringValue(certificateIssuanceDashBoardDetail, queryList[4]));
		detailData.setStudentUserName(dbdc.getStringValue(certificateIssuanceDashBoardDetail, "student_user_name"));
		detailData.setStatus(dbdc.getStringValue(certificateIssuanceDashBoardDetail, queryList[8]));
		detailData.setOfficeUserId(dbdc.getStringValue(certificateIssuanceDashBoardDetail, queryList[9]));
		detailData.setOfficeUserName(dbdc.getStringValue(certificateIssuanceDashBoardDetail, "office_user_name"));
		detailData.setRecipientName(dbdc.getStringValue(certificateIssuanceDashBoardDetail, queryList[11]));
		detailData.setRecipientFurigana(dbdc.getStringValue(certificateIssuanceDashBoardDetail, queryList[12]));
		detailData.setRecipientAddress(dbdc.getStringValue(certificateIssuanceDashBoardDetail, queryList[13]));
		detailData.setMediaType(dbdc.getStringValue(certificateIssuanceDashBoardDetail, queryList[14]));

		// 数値データの設定
		detailData.setAttendanceNumber(dbdc.getIntegerValue(certificateIssuanceDashBoardDetail, queryList[5]));
		detailData.setStudentId(dbdc.getIntegerValue(certificateIssuanceDashBoardDetail, queryList[7]));
		detailData.setPostalFee(dbdc.getIntegerValue(certificateIssuanceDashBoardDetail, queryList[20]));
		detailData.setPostalMaxWeight(dbdc.getIntegerValue(certificateIssuanceDashBoardDetail, queryList[21]));

		// 日付データの設定
		detailData.setApplicationDate(dbdc.getDateValue(certificateIssuanceDashBoardDetail, queryList[15]));
		detailData.setApprovalDate(dbdc.getDateValue(certificateIssuanceDashBoardDetail, queryList[16]));
		detailData.setDeliveryDueDate(dbdc.getDateValue(certificateIssuanceDashBoardDetail, queryList[17]));
		detailData.setDeliveryDate(dbdc.getDateValue(certificateIssuanceDashBoardDetail, queryList[18]));
		detailData.setPostDate(dbdc.getDateValue(certificateIssuanceDashBoardDetail, queryList[19]));

		return detailData;
	}

	/**
	 * 指定された通知リストから、同じ証明書発行IDを持つデータを検索します。
	 *
	 * @param dashBoardList      ダッシュボードデータのリスト
	 * @param certificateIssueId 検索したい証明書発行ID
	 * @return 一致するダッシュボードデータ。存在しない場合はnullを返却します。
	 */
	private CertificateIssuanceDashBoardData findExistingCertificateData(
			List<CertificateIssuanceDashBoardData> dashBoardList, String certificateIssueId) {
		for (CertificateIssuanceDashBoardData data : dashBoardList) {
			if (certificateIssueId.equals(data.getCertificateIssueId())) {
				return data;
			}
		}
		return null;
	}
}
