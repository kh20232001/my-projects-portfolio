package com.api.domain.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;

import com.api.domain.models.dbdata.CertificateData;
import com.api.domain.models.dbdata.CertificateIssuanceDashBoardData;
import com.api.domain.models.dbdata.CertificateIssuanceDetailData;
import com.api.domain.models.dbdata.CertificateIssuanceEntity;
import com.api.domain.models.dbdata.MailingData;
import com.api.domain.models.displaydata.CertificateDisplayData;
import com.api.domain.models.displaydata.CertificateDisplayItem;
import com.api.domain.models.displaydata.CertificateFeeData;
import com.api.domain.models.displaydata.PostalData;
import com.api.domain.models.entities.CertificateDashBoardEntity;
import com.api.domain.models.entities.CertificateFeeAndWeightEntity;
import com.api.domain.models.forms.CertificateDetailDataForm;
import com.api.domain.models.forms.CertificateInsertForm;
import com.api.domain.repositories.CertificateIssuanceDashBoardRepository;
import com.api.domain.repositories.CertificateIssuanceRepository;
import com.api.domain.repositories.NotificationRepository;
import com.api.domain.repositories.UserRepository;
import com.api.jobpal.common.base.Util;

/**
 * 証明書発行ダッシュボードに関連する操作を管理するサービスクラスです。
 *
 * <p>
 * 主な機能:
 * <ul>
 * <li>証明書発行申請の取得（全体および個別ユーザー）</li>
 * <li>新規証明書発行申請の作成</li>
 * <li>既存証明書発行申請の変更</li>
 * <li>証明書料金と重量データの取得</li>
 * <li>証明書発行データの表示形式変換</li>
 * </ul>
 * </p>
 *
 * <p>
 * <strong>エラー処理:</strong>
 * 処理が継続できない場合、エラーを適切に処理し、呼び出し元が結果を判定できるよう設計されています。<br>
 * 呼び出し元は戻り値を利用して、必要なエラー処理を行うよう注意してください。
 * </p>
 */

@Service
public class CertificateDashBoardService {
	/**
	 * 証明書発行ダッシュボードリポジトリ。
	 */
	@Autowired
	private CertificateIssuanceDashBoardRepository dashBoardRepository;

	/**
	 * 証明書発行リポジトリ。
	 */
	@Autowired
	private CertificateIssuanceRepository certificateRepository;

	/**
	 * ユーザリポジトリ。
	 */
	@Autowired
	private UserRepository newUserRepository;

	/**
	 * 通知リポジトリ。
	 */
	@Autowired
	private NotificationRepository notificationRepository;

	/**
	 * 証明書ダッシュボード詳細サービス。
	 */
	@Autowired
	private CertificateDashBoardDetailService certificateService;

	/**
	 * 通知サービス。
	 */
	@Autowired
	private NotificationService notificationService;

	/**
	 * 全ユーザーの証明書発行申請を取得します。
	 *
	 * @param userId ユーザID
	 * @return CertificateDashBoardEntity ダッシュボード表示用エンティティ
	 * @throws Exception 処理エラー
	 */
	public CertificateDashBoardEntity getDashBoardAll(String userId) throws Exception {

		// ダッシュボードデータの取得
		List<CertificateIssuanceDashBoardData> certificateDatas = dashBoardRepository
				.selectCertificateIssuanceDashBoard();

		CertificateDashBoardEntity dashBoardEntity = new CertificateDashBoardEntity();
		dashBoardEntity.setDashBoardList(new ArrayList<>());

		for (CertificateIssuanceDashBoardData certificateData : certificateDatas) {
			// 各データをCertificateDisplayDataに変換
			CertificateDisplayData certificateDisplayData = transformCertificateData(certificateData);
			dashBoardEntity.getDashBoardList().add(certificateDisplayData);
		}

		// 通知件数を設定
		int alertCnt = notificationRepository.getNotificationCount(userId);
		dashBoardEntity.setAlertCnt(alertCnt);

		return dashBoardEntity;
	}

	/**
	 * ユーザーごとの証明書発行申請を取得します。
	 *
	 * @param userId   ユーザID
	 * @param userType ユーザータイプ
	 * @return CertificateDashBoardEntity ダッシュボード表示用エンティティ
	 * @throws Exception 処理エラー
	 */
	public CertificateDashBoardEntity getDashBoardUser(String userId, String userType) throws Exception {

		// 指定ユーザーのダッシュボードデータを取得
		List<CertificateIssuanceDashBoardData> certificateDatas = dashBoardRepository
				.selectCertificateIssuanceDashBoard(userId, userType);

		CertificateDashBoardEntity dashBoardEntity = new CertificateDashBoardEntity();
		dashBoardEntity.setDashBoardList(new ArrayList<>());

		for (CertificateIssuanceDashBoardData certificateData : certificateDatas) {
			// データをCertificateDisplayData形式に変換
			CertificateDisplayData certificateDisplayData = transformCertificateData(certificateData);
			dashBoardEntity.getDashBoardList().add(certificateDisplayData);
		}

		// 通知件数を設定
		int alertCnt = notificationRepository.getNotificationCount(userId);
		dashBoardEntity.setAlertCnt(alertCnt);

		return dashBoardEntity;
	}

	/**
	 * 新規証明書申請を作成します。
	 *
	 * @param certificateInsertForm 証明書挿入フォーム
	 * @return 成功時true, 失敗時false
	 */
	public boolean createNewCertificate(CertificateInsertForm certificateInsertForm) {
		try {
			// 担任のユーザーIDを取得
			String teacherId = newUserRepository.selectTeacherUserId(certificateInsertForm.getUserId());

			// 証明書発行エンティティを作成
			CertificateIssuanceEntity certificateIssuanceEntity = refillCertificateInsert(certificateInsertForm,
					teacherId);

			// 証明書発行IDを取得
			String certificateIssueId = certificateIssuanceEntity.getCertificateIssueId();

			// 証明書発行データの挿入
			boolean isSuccess = certificateRepository.insertCertificateIssuanceOne(certificateIssuanceEntity);

			if (!isSuccess) {
				// 失敗した場合はfalseを返却
				return false;
			}

			// 詳細データの挿入
			for (CertificateIssuanceDetailData certificateIssuanceDetailData : certificateIssuanceEntity
					.getCertificateIssuanceDetailList()) {
				isSuccess = certificateRepository.insertCertificateIssuanceDetailOne(certificateIssueId,
						certificateIssuanceDetailData);
				if (!isSuccess) {
					// 失敗した場合はfalseを返却
					return false;
				}
			}

			// 媒体タイプに応じたデータの挿入
			if ("1".equals(certificateIssuanceEntity.getMediaType())) {
				isSuccess = certificateRepository.insertElectronicCertificateIssuanceOne(certificateIssueId,
						certificateInsertForm.getUserId());
			} else if ("2".equals(certificateIssuanceEntity.getMediaType())) {
				String postalPaymentId = certificateRepository.selectPostalPaymentIdMax();
				isSuccess = certificateRepository.insertMailingCertificateIssuanceOne(certificateIssuanceEntity,
						postalPaymentId);
			}

			if (!isSuccess) {
				// 失敗した場合はfalseを返却
				return false;
			}

			// 通知データの挿入
			String userId = newUserRepository.selectTeacherUserId(certificateInsertForm.getUserId());
			// 正常終了の場合、true を返却 (通知データの挿入が失敗した場合は false)
			return notificationService.insertCertificateNotification(certificateIssueId, userId, true);
		} catch (IncorrectResultSizeDataAccessException e) {
			// データベース挿入中にエラーが発生した場合、false を返却
			return false;
		} catch (Exception e) {
			// その他のエラーが発生した場合、false を返却
			return false;
		}
	}

	/**
	 * 証明書の変更を行います。
	 *
	 * @param certificateInsertForm 証明書挿入フォーム
	 * @return 成功時true, 失敗時false
	 */
	public boolean updateCertificate(CertificateInsertForm certificateInsertForm) throws Exception {
		String certificateIssueId = certificateInsertForm.getCertificateIssueId();
		// 証明書の削除
		boolean isSuccess = certificateService.deleteOne(certificateIssueId);
		if (!isSuccess) {
			return false;
		}
		// 証明書発行IDの再取得
		certificateIssueId = certificateRepository.getCertificateIssuanceId();
		certificateInsertForm.setCertificateIssueId(certificateIssueId);
		// 証明書の新規作成 (失敗時はfalseを返す)
		return createNewCertificate(certificateInsertForm);
	}

	/**
	 * 証明書申請挿入用データを作成します。
	 *
	 * @param certificateInsertForm 証明書挿入フォーム
	 * @param teacherUserId         担任のユーザーID
	 * @return 証明書発行エンティティ
	 */
	private CertificateIssuanceEntity refillCertificateInsert(CertificateInsertForm certificateInsertForm,
			String teacherUserId) {

		// 証明書詳細リストの生成
		List<CertificateIssuanceDetailData> certificateIssuanceDetailList = new ArrayList<>();
		for (CertificateDetailDataForm detailDataForm : certificateInsertForm.getCertificateList()) {
			if (detailDataForm.getCount() != 0) {
				CertificateIssuanceDetailData detailData = new CertificateIssuanceDetailData();
				detailData.setCertificateId(detailDataForm.getCertificateId());
				detailData.setCertificateQuantity(detailDataForm.getCount());
				certificateIssuanceDetailList.add(detailData);
			}
		}

		// 郵送の場合の受取人情報の設定
		String recipientName = null, recipientFurigana = null, recipientAddress = null;
		if ("郵送".equals(certificateInsertForm.getMediaName())) {
			recipientName = certificateInsertForm.getLastName() + " " + certificateInsertForm.getFirstName();
			recipientFurigana = certificateInsertForm.getLastNameKana() + " "
					+ certificateInsertForm.getFirstNameKana();
			recipientAddress = certificateInsertForm.getZipCode() + " " + certificateInsertForm.getAddress() + " "
					+ certificateInsertForm.getAfterAddress();
		}

		String certificateIssueId = certificateRepository.getCertificateIssuanceId();

		// エンティティをビルド
		return new CertificateIssuanceEntity.Builder()
				.certificateIssueId(certificateIssueId)
				.studentUserId(certificateInsertForm.getUserId())
				.mediaType(Util.getIdByMediaType(certificateInsertForm.getMediaName()))
				.teacherUserId(teacherUserId)
				.certificateIssuanceDetailList(certificateIssuanceDetailList)
				.recipientName(recipientName)
				.recipientFurigana(recipientFurigana)
				.recipientAddress(recipientAddress)
				.build();
	}

	/**
	 * 証明書料金と重量データを取得します。
	 *
	 * @return CertificateFeeAndWeightEntity 証明書料金と重量データ
	 */
	public CertificateFeeAndWeightEntity getCertificateFeeAndWeight() {

		// 郵送データの取得
		MailingData mailingData = certificateRepository.selectMailing();

		// 全証明書データの取得
		List<CertificateData> certificateDatas = certificateRepository.selectCertificateAll();

		// 証明書料金と重量のリストを作成
		List<CertificateFeeData> certificateList = new ArrayList<>();
		for (CertificateData certificateData : certificateDatas) {
			CertificateFeeData certificateFeeData = new CertificateFeeData.Builder()
					.setCertificateId(certificateData.getCertificateId())
					.setWeight(certificateData.getCertificateWeight())
					.setFee(certificateData.getCertificateFee())
					.build();
			certificateList.add(certificateFeeData);
		}

		// 郵送データの作成
		PostalData postalData = new PostalData.Builder()
				.setPostalMaxWeight(mailingData.getPostalMaxWeight())
				.setPostalFee(mailingData.getPostalFee())
				.build();

		// エンティティを作成
		return new CertificateFeeAndWeightEntity.Builder()
				.setCertificateList(certificateList)
				.setPostal(postalData)
				.build();
	}

	/**
	 * CertificateIssuanceDashBoardDataをCertificateDisplayDataに変換するヘルパーメソッド。
	 *
	 * <p>
	 * 証明書発行ダッシュボードデータから、表示用証明書データへ変換します。<br>
	 * 各証明書の詳細情報をリスト化し、表示用のデータオブジェクトを構築します。
	 * </p>
	 *
	 * @param certificateData 証明書発行ダッシュボードデータ
	 * @return CertificateDisplayData 表示用の証明書データ
	 */
	private CertificateDisplayData transformCertificateData(CertificateIssuanceDashBoardData certificateData) {
		List<CertificateDisplayItem> certificateDetailList = new ArrayList<>();
		for (CertificateData certificate : certificateData.getCertificateList()) {
			CertificateDisplayItem certificateDetail = new CertificateDisplayItem.Builder()
					.certificateId(certificate.getCertificateId())
					.certificateName(Util.getShortCertificateNameById(certificate.getCertificateId()))
					.count(certificate.getCertificateQuantity())
					.build();
			certificateDetailList.add(certificateDetail);
		}
		return new CertificateDisplayData.Builder()
				.certificateIssueId(certificateData.getCertificateIssueId())
				.userId(certificateData.getStudentUserId())
				.userName(certificateData.getStudentUserName())
				.schoolClassNumber(certificateData.getStudentId())
				.certificateStateName(Util.getShortCertificateStatusDescription(certificateData.getStatus()))
				.date(Util.formatDate(certificateData.getLatestDate(), "MM/dd"))
				.certificateList(certificateDetailList)
				.totalAmount(certificateData.getTotalAmount())
				.mediaName(Util.getMediaTypeById(certificateData.getMediaType()))
				.stateIdPriority(Integer.valueOf(certificateData.getStatus()))
				.startTimePriority(Long.parseLong(Util.formatDate(certificateData.getLatestDate(), "yyyyMMddHHmmss")))
				.reNotifyFlag(false)
				.build();
	}

}
