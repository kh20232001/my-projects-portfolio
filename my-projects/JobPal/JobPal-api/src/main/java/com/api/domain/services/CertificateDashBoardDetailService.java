package com.api.domain.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;

import com.api.domain.models.dbdata.CertificateData;
import com.api.domain.models.dbdata.CertificateIssuanceDashBoardDetailData;
import com.api.domain.models.displaydata.CertificateDisplayDetail;
import com.api.domain.models.displaydata.CertificateDisplayItem;
import com.api.domain.models.forms.CertificateStateUpdateForm;
import com.api.domain.repositories.CertificateIssuanceDashBoardRepository;
import com.api.domain.repositories.CertificateIssuanceRepository;
import com.api.domain.repositories.UserRepository;
import com.api.jobpal.common.base.Util;

/**
 * 証明書発行ダッシュボードの詳細情報を管理するサービスクラスです。
 *
 * <p>
 * 主な機能:
 * <ul>
 * <li>証明書発行申請の詳細情報の取得</li>
 * <li>証明書発行申請の状態更新</li>
 * <li>証明書発行申請の削除</li>
 * </ul>
 * </p>
 *
 * <p>
 * <strong>エラー処理:</strong>
 * 処理中に発生したエラーについては、呼び出し元が結果を判定し適切に対処できるよう設計されています。<br>
 * 戻り値を使用して成功または失敗を判定してください。
 * </p>
 */
@Service
public class CertificateDashBoardDetailService {

	/**
	 * 証明書発行ダッシュボードリポジトリ。
	 */
	@Autowired
	private CertificateIssuanceDashBoardRepository dashBoardRepository;

	/**
	 * 証明書発行リポジトリ。
	 */
	@Autowired
	private CertificateIssuanceRepository issuanceRepository;

	/**
	 * ユーザリポジトリ。
	 */
	@Autowired
	private UserRepository newUserRepository;

	/**
	 * 通知サービス。
	 */
	@Autowired
	private NotificationService notificationService;

	/**
	 * 指定された証明書発行IDの詳細データを取得します。
	 *
	 * @param certificateIssuanceId 証明書発行ID
	 * @return CertificateDisplayDetail 証明書詳細情報
	 */
	public CertificateDisplayDetail getDashBoardDetail(String certificateIssuanceId) {
		// 証明書発行詳細データを取得
		CertificateIssuanceDashBoardDetailData dashBoardDetailData = dashBoardRepository
				.selectCertificateIssuanceDashBoardDetail(certificateIssuanceId);

		// データを加工してCertificateDisplayDetail形式に変換
		return refillCertificateDisplayDetail(dashBoardDetailData);
	}

	/**
	 * 取得した証明書発行データを表示用に変換します。
	 *
	 * @param dashBoardDetailData 証明書発行詳細データ
	 * @return CertificateDisplayDetail 変換後の証明書詳細データ
	 */
	private CertificateDisplayDetail refillCertificateDisplayDetail(
			CertificateIssuanceDashBoardDetailData dashBoardDetailData) {

		// 基本情報の取得とフォーマット処理
		String certificateIssueId = dashBoardDetailData.getCertificateIssueId();
		String userId = dashBoardDetailData.getStudentUserId();
		String userClass = Util.formatUserClass(
				dashBoardDetailData.getDepartment(),
				dashBoardDetailData.getGrade() != null
						? Integer.valueOf(dashBoardDetailData.getGrade())
						: null,
				dashBoardDetailData.getClassName());
		Integer classNumber = dashBoardDetailData.getAttendanceNumber();
		String userName = dashBoardDetailData.getStudentUserName();
		Integer schoolClassNumber = dashBoardDetailData.getStudentId();
		String certificateStateName = Util.getCertificateStatusDescription(dashBoardDetailData.getStatus());
		String officeUserName = dashBoardDetailData.getOfficeUserName();
		String mediaName = Util.getMediaTypeById(dashBoardDetailData.getMediaType());
		List<CertificateDisplayItem> certificateList = refillCertificateItemList(certificateIssueId);

		// 郵送の場合の宛先情報の処理
		String lastName = null, firstName = null, lastNameKana = null, firstNameKana = null;
		String zipCode = null, address = null, afterAddress = null;

		if ("郵送".equals(mediaName)) {
			// 受取人情報を分割して設定
			String[] recipientName = dashBoardDetailData.getRecipientName().split(" ");
			lastName = recipientName[0];
			firstName = recipientName[1];

			String[] recipientFurigana = dashBoardDetailData.getRecipientFurigana().split(" ");
			lastNameKana = recipientFurigana[0];
			firstNameKana = recipientFurigana[1];

			String[] recipientAddress = dashBoardDetailData.getRecipientAddress().split(" ");
			zipCode = recipientAddress[0];
			address = recipientAddress[1];
			afterAddress = recipientAddress[2];
		}

		// CertificateDisplayDetailを生成
		return new CertificateDisplayDetail.Builder()
				.certificateIssueId(certificateIssueId)
				.userId(userId)
				.userClass(userClass)
				.classNumber(classNumber)
				.userName(userName)
				.schoolClassNumber(schoolClassNumber)
				.certificateStateName(certificateStateName)
				.officeUserName(officeUserName)
				.lastName(lastName)
				.firstName(firstName)
				.lastNameKana(lastNameKana)
				.firstNameKana(firstNameKana)
				.certificateList(certificateList)
				.mediaName(mediaName)
				.zipCode(zipCode)
				.address(address)
				.afterAddress(afterAddress)
				.build();
	}

	/**
	 * 証明書発行の詳細リストをリスト形式に変換します。
	 *
	 * @param certificateList 証明書発行データリスト
	 * @return List<CertificateDisplayItem> 変換後のリスト
	 */
	private List<CertificateDisplayItem> refillCertificateItemList(String certificateIssuanceId) {

		List<CertificateData> certificateList = dashBoardRepository.selectCertificate(certificateIssuanceId);
		List<CertificateDisplayItem> certificateDetailList = new ArrayList<>();
		Map<String, Integer> certificateMap = new HashMap<>();

		// nullチェックとデータマップへの格納
		if (!certificateList.isEmpty()) {
			for (CertificateData certificate : certificateList) {
				certificateMap.put(certificate.getCertificateId(), certificate.getCertificateQuantity());
			}
		}

		// 証明書IDが0〜3の範囲を処理
		for (int i = 0; i <= 3; i++) {
			String certificateId = String.valueOf(i);

			int count = certificateMap.getOrDefault(certificateId, 0);

			CertificateDisplayItem certificateDetail = new CertificateDisplayItem.Builder()
					.certificateId(certificateId)
					.certificateName(Util.getShortCertificateNameById(certificateId))
					.count(count)
					.build();

			certificateDetailList.add(certificateDetail);
		}
		return certificateDetailList;
	}

	/**
	 * 証明書申請の状態を更新します。
	 *
	 * @param certificateStateUpdateForm 更新フォームデータ
	 * @return true 更新成功, false 更新失敗
	 */
	public boolean updateDashBoardDetail(CertificateStateUpdateForm certificateStateUpdateForm) {
		// 証明書発行IDの取得
		String certificateIssueId = certificateStateUpdateForm.getCertificateIssueId();
		Integer buttonId = certificateStateUpdateForm.getButtonId();
		String stateId = Util.getCertificateStatusCode(certificateStateUpdateForm.getCertificateStateName());

		Map<String, String> userIdList = issuanceRepository
				.selectCertificateIssueUserId(certificateIssueId);

		String userId = certificateStateUpdateForm.getUserId();
		// ボタンIDに基づく状態遷移
		switch (buttonId) {
			case 0 -> stateId = "1"; // 承認
			case 1, 2 -> stateId = "2"; // 取り下げまたは差戻し
			case 3 -> {
				stateId = "3";
				// 担当の事務を登録
				boolean isSuccess = issuanceRepository.updateCertificateIssuanceOfficeUserIdOne(certificateIssueId,
						userId);
				if (!isSuccess) {
					return false;
				}
			} // 受領
			case 4 -> {
				String mediaName = certificateStateUpdateForm.getMediaName();
				stateId = switch (mediaName) {
					case "郵送", "電子" -> "4";
					case "原紙" -> "5";
					default -> throw new IllegalArgumentException("Invalid mediaName: " + mediaName);
				};
			} // 発行
			case 5 -> stateId = "5"; // 送信
			case 6, 7 -> stateId = "6"; // 郵送、完了
			default -> throw new IllegalArgumentException("Invalid buttonId: " + buttonId);
		}

		// 状態を更新
		try {
			boolean isSuccess = issuanceRepository.updateCertificateIssuanceStatusOne(certificateIssueId,
					stateId);
			if (!isSuccess) {
				return false;
			}

			// 通知データの追加

			// 完了の場合
			if ("6".equals(stateId)) {
				// 通知データの削除
				return notificationService.deleteCertificateNotification(certificateIssueId);
				// 受取待ちの場合
			} else if ("5".equals(stateId)) {
				// 事務への通知データの追加
				isSuccess = notificationService.insertCertificateNotification(certificateIssueId, userId);
				if (!isSuccess) {
					return false;
				}
				// 学生への通知データの追加
				userId = userIdList.get("student");
				return notificationService.insertCertificateNotification(certificateIssueId, userId);
				// 支払待ちの場合
			} else if ("1".equals(stateId)) {
				// 学生への通知データの追加
				userId = userIdList.get("student");
				isSuccess = notificationService.insertCertificateNotification(certificateIssueId, userId);
				if (!isSuccess) {
					// 通知データの追加に失敗した場合
					return false;
				}
				// 全ての事務への通知データの追加
				List<String> officeList = newUserRepository.selectOfficeUserIdList();
				for (String office : officeList) {
					isSuccess = notificationService.insertCertificateNotification(certificateIssueId, office, true);
					if (!isSuccess) {
						// 通知データの追加に失敗した場合
						return false;
					}
				}
				return true;
				// その他の場合
			} else {
				// 通知データの追加
				return notificationService.insertCertificateNotification(certificateIssueId, userId);
			}
		} catch (IncorrectResultSizeDataAccessException e) {
			// データ挿入中にエラーが発生した場合
			return false;
		} catch (Exception e) {
			// その他のエラーが発生した場合
			return false;
		}
	}

	/**
	 * 指定された証明書申請を削除します。
	 *
	 * @param certificateIssueId 証明書発行ID
	 * @return true 削除成功, false 削除失敗
	 */
	public boolean deleteOne(String certificateIssueId) {
		try {
			// 通知データの削除
			boolean isSuccess = notificationService.deleteCertificateNotification(certificateIssueId);
			if (!isSuccess) {
				return false;
			}
			// 申請データの削除 (失敗時はfalseを返す)
			return issuanceRepository.deleteCertificateIssuanceOne(certificateIssueId);
		} catch (IncorrectResultSizeDataAccessException e) {
			return false;
		}
	}

}
