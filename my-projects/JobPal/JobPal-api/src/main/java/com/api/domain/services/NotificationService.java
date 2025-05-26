package com.api.domain.services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.domain.models.dbdata.CertificateData;
import com.api.domain.models.dbdata.CertificateIssuanceDashBoardData;
import com.api.domain.models.dbdata.JobSearchDashBoardData;
import com.api.domain.models.dbdata.NotificationDashBoardData;
import com.api.domain.models.displaydata.CertificateDisplayData;
import com.api.domain.models.displaydata.CertificateDisplayItem;
import com.api.domain.models.displaydata.DashBoardDisplayData;
import com.api.domain.models.entities.AlertDisplayEntity;
import com.api.domain.repositories.NotificationRepository;
import com.api.domain.repositories.UserRepository;
import com.api.jobpal.common.base.Util;

/**
 * 通知関連のロジックを提供するサービスクラスです。
 *
 * <p>
 * 以下の処理を提供します。
 * <ul>
 * <li>ユーザに関連する通知データの取得</li>
 * <li>通知データの整形とフロントエンド表示用データへの変換</li>
 * <li>通知データの挿入および削除</li>
 * </ul>
 * </p>
 *
 * <p>
 * 各種申請（証明書、就職活動など）に関する通知データを取得し、フロントエンドで表示できる形式で整形します。
 * 通知データの再送や新規作成にも対応しています。
 * </p>
 */

@Service
public class NotificationService {

	/**
	 * 通知リポジトリ。
	 */
	@Autowired
	private NotificationRepository notificationRepository;

	/**
	 * ユーザリポジトリ。
	 */
	@Autowired
	private UserRepository userRepository;

	/**
	 * 指定されたユーザIDの全通知データを取得します。
	 *
	 * @param userId ユーザID
	 * @return AlertDisplayEntity 通知情報を保持するエンティティ
	 */
	public AlertDisplayEntity getAlertAll(String userId) {
		// 通知エンティティの初期化
		AlertDisplayEntity alertDisplayEntity = new AlertDisplayEntity();

		// ユーザ名を取得してエンティティに設定
		alertDisplayEntity.setUserName(userRepository.selectUserName(userId));

		// 就職活動関連の通知データ取得
		List<NotificationDashBoardData> notificationJobSearchList = notificationRepository
				.getNotificationJobSearchList(userId);
		alertDisplayEntity = getJobSearchAllForAlert(alertDisplayEntity, notificationJobSearchList);

		// 証明書関連の通知データ取得
		List<NotificationDashBoardData> notificationCertificateList = notificationRepository
				.getNotificationCertificateIssuanceList(userId);
		alertDisplayEntity = getCertificateAllForAlert(alertDisplayEntity, notificationCertificateList);

		return alertDisplayEntity;
	}

	/**
	 * 通知データから証明書情報を取得し、アラート表示エンティティに設定します。
	 *
	 * <p>
	 * 指定された通知データリストを元に、証明書情報を整形してアラート表示用のデータを構築します。<br>
	 * 再通知フラグが立っているデータは、リストの先頭に追加されます。
	 * </p>
	 *
	 * @param alertDisplayEntity アラート表示エンティティ
	 * @param notificationCertificateList 通知証明書データのリスト
	 * @return 整形後のアラート表示エンティティ
	 */
	public AlertDisplayEntity getCertificateAllForAlert(AlertDisplayEntity alertDisplayEntity,
			List<NotificationDashBoardData> notificationCertificateList) {

		// 再通知対象データの一時保存リスト
		List<CertificateDisplayData> overTimeList = new ArrayList<>();
		// 証明書データの初期化
		alertDisplayEntity.setCertificateList(new ArrayList<>());

		// 通知証明書データリストをループ
		for (NotificationDashBoardData notificationCertificate : notificationCertificateList) {
			CertificateIssuanceDashBoardData certificateData = notificationCertificate
					.getCertificateIssuanceDashBoardData();

			// 各証明書情報の取得と整形
			String certificateIssueId = certificateData.getCertificateIssueId();
			String userId = certificateData.getStudentUserId();
			String userName = certificateData.getStudentUserName();
			Integer schoolClassNumber = certificateData.getStudentId();
			String certificateStateName = Util.getCertificateStatusDescription(certificateData.getStatus());
			String date = Util.formatDate(certificateData.getLatestDate(), "MM/dd");
			List<CertificateDisplayItem> certificateList = convertCertificateList(certificateData.getCertificateList());
			Integer totalAmount = certificateData.getTotalAmount();
			String mediaName = Util.getMediaTypeById(certificateData.getMediaType());
			Integer stateIdPriority = Integer.valueOf(certificateData.getStatus());
			Long startTimePriority = Long.parseLong(Util.formatDate(certificateData.getLatestDate(), "yyyyMMddHHmmss"));
			Boolean reNotifyFlag = notificationCertificate.getResendFlag();

			// 証明書情報オブジェクトを作成
			CertificateDisplayData certificateDisplayData = new CertificateDisplayData.Builder()
					.certificateIssueId(certificateIssueId)
					.userId(userId)
					.userName(userName)
					.schoolClassNumber(schoolClassNumber)
					.certificateStateName(certificateStateName)
					.date(date)
					.certificateList(certificateList)
					.totalAmount(totalAmount)
					.mediaName(mediaName)
					.stateIdPriority(stateIdPriority)
					.startTimePriority(startTimePriority)
					.reNotifyFlag(reNotifyFlag)
					.build();

			// 再通知フラグによる振り分け
			if (reNotifyFlag) {
				overTimeList.add(certificateDisplayData);
			} else {
				alertDisplayEntity.getCertificateList().add(certificateDisplayData);
			}
		}

		// 再通知リストを先頭に追加
		alertDisplayEntity.getCertificateList().addAll(0, overTimeList);
		return alertDisplayEntity;
	}

	/**
	 * 指定された通知リストから就職活動に関連するデータを整形します。
	 *
	 * <p>
	 * 通知リストを基に就職活動のデータを整形し、アラート表示用エンティティを構築します。<br>
	 * 再通知フラグが立っているデータは、リストの先頭に追加されます。
	 * </p>
	 *
	 * @param alertDisplayEntity        アラート表示エンティティ
	 * @param notificationJobSearchList 就職活動関連の通知リスト
	 * @return 整形されたアラート表示エンティティ
	 */
	public AlertDisplayEntity getJobSearchAllForAlert(AlertDisplayEntity alertDisplayEntity,
			List<NotificationDashBoardData> notificationJobSearchList) {

		// 再通知対象データの一時保存リスト
		List<DashBoardDisplayData> overTimeList = new ArrayList<>();
		// 就職活動データリストの初期化
		alertDisplayEntity.setDashBoardList(new ArrayList<>());

		// 通知就職活動リストをループ処理
		for (NotificationDashBoardData notificationJobSearch : notificationJobSearchList) {
			JobSearchDashBoardData jobSearchData = notificationJobSearch.getJobSearchDashBoardData();

			// 各就職活動情報の取得と整形
			String jobSearchId = jobSearchData.getJobSearchId();
			String userId = jobSearchData.getUserId();
			String userName = jobSearchData.getUserName();
			String department = jobSearchData.getDepartment();
			Integer grade = jobSearchData.getGrade();
			String className = jobSearchData.getClassName();
			Integer attendanceNumber = jobSearchData.getAttendanceNumber();
			String status = jobSearchData.getJobSearchStatus();
			String companyName = jobSearchData.getCompanyName();
			String eventCategory = jobSearchData.getEventCategory();
			String result = jobSearchData.getResult();
			Boolean schoolCheck = jobSearchData.getSchoolCheckFlag();
			Timestamp startTime = jobSearchData.getStartTime();
			Timestamp endTime = jobSearchData.getEndTime();
			Boolean reNotifyFlag = notificationJobSearch.getResendFlag();

			// 整形されたデータを構築
			DashBoardDisplayData data = new DashBoardDisplayData.Builder()
					.setJobHuntId(jobSearchId)
					.setUserId(userId)
					.setUserName(userName)
					.setSchoolClassNumber(department + grade + className + Util.formatToTwoDigits(attendanceNumber))
					.setJobStateId(Util.getJobStateShortName(status))
					.setCompany(companyName)
					.setEventCategory(Util.getEventCategoryDescription(eventCategory))
					.setResult(Util.getResultStatus(result))
					.setSchoolCheck(schoolCheck)
					.setDate(Util.formatDate(startTime, "MM/dd"))
					.setStartTime(Util.formatDate(startTime, "HH:mm"))
					.setFinishTime(Util.formatDate(endTime, "HH:mm"))
					.setEventCategoryPriority(Integer.valueOf(eventCategory))
					.setStateIdPriority(Integer.valueOf(status))
					.setStartTimePriority(Long.valueOf(Util.formatDate(startTime, "yyyyMMddHHmmss")))
					.setReNotifyFlag(reNotifyFlag)
					.build();

			// 再通知フラグによる振り分け
			if (reNotifyFlag) {
				overTimeList.add(data);
			} else {
				alertDisplayEntity.getDashBoardList().add(data);
			}
		}

		// 再通知リストを先頭に追加
		alertDisplayEntity.getDashBoardList().addAll(0, overTimeList);

		return alertDisplayEntity;
	}

	/**
	 * 証明書リストをディスプレイ用のリストに変換します。
	 *
	 * <p>
	 * 各証明書データを元に、表示用のデータ項目へと整形してリストとして返却します。<br>
	 * 証明書IDに基づいて短縮された証明書名を取得し、整形データに含めます。
	 * </p>
	 *
	 * @param certificateList 証明書データのリスト
	 * @return 表示用の証明書データリスト
	 */
	private List<CertificateDisplayItem> convertCertificateList(List<CertificateData> certificateList) {
		List<CertificateDisplayItem> displayItems = new ArrayList<>();

		// 証明書データリストをループ処理し、整形データを作成
		for (CertificateData certificate : certificateList) {
			displayItems.add(new CertificateDisplayItem.Builder()
					.certificateId(certificate.getCertificateId()) // 証明書IDを設定
					.certificateName(Util.getShortCertificateNameById(certificate.getCertificateId())) // 短縮証明書名を設定
					.count(certificate.getCertificateQuantity()) // 証明書の発行枚数を設定
					.build());
		}

		return displayItems;
	}

	/**
	 * 就職活動の通知データを挿入します。
	 *
	 * @param jobSearchId 就職活動ID
	 * @param userId      ユーザID
	 * @return 挿入に成功した場合はtrue、それ以外はfalse
	 */
	public Boolean insertJobsearchNotification(String jobSearchId, String userId) {
		return insertJobsearchNotification(jobSearchId, userId, false);
	}

	/**
	 * 就職活動の通知データを挿入します。
	 *
	 * @param jobSearchId 就職活動ID
	 * @param userId      ユーザID
	 * @param isFirst     通知データが存在する場合はtrue、それ以外はfalse
	 * @return 挿入に成功した場合はtrue、それ以外はfalse
	 */
	public Boolean insertJobsearchNotification(String jobSearchId, String userId, Boolean isFirst) {
		Boolean isSuccess;
		// 通知データが存在する場合、削除
		if (!isFirst) {
			isSuccess = deleteJobsearchNotification(jobSearchId);
			if (!isSuccess) {
				return false;
			}
		}
		// 新規通知ID取得
		String notificationId = notificationRepository.getNotificationId();
		// 通知データを挿入
		isSuccess = notificationRepository.insertNotification(notificationId, userId, false, "0");
		if (!isSuccess) {
			return false;
		}
		// 通知データの挿入に成功した場合、trueを返却
		return notificationRepository.insertNotificationJobSearch(notificationId, jobSearchId);
	}

	/**
	 * 就職活動の通知データを削除します。
	 *
	 * @param jobSearchId 就職活動ID
	 * @return true: 削除成功, false: 削除失敗
	 */
	public Boolean deleteJobsearchNotification(String jobSearchId) {
		// 通知データが存在する場合、削除
		return notificationRepository.deleteNotificationJobSearch(jobSearchId);
	}

	/**
	 * 証明書発行の通知データを挿入します。
	 *
	 * @param jobSearchId 証明書発行ID
	 * @param userId      ユーザID
	 * @return 挿入に成功した場合はtrue、それ以外はfalse
	 */
	public Boolean insertCertificateNotification(String certificateIssuanceId, String userId) {
		return insertCertificateNotification(certificateIssuanceId, userId, false);
	}

	/**
	 * 証明書発行の通知データを挿入します。
	 *
	 * @param jobSearchId 証明書発行ID
	 * @param userId      ユーザID
	 * @param isFirst     通知データが存在する場合はtrue、それ以外はfalse
	 * @return 挿入に成功した場合はtrue、それ以外はfalse
	 */
	public Boolean insertCertificateNotification(String certificateIssuanceId, String userId, Boolean isFirst) {
		Boolean isSuccess;
		// 通知データが存在する場合、削除
		if (!isFirst) {
			isSuccess = deleteCertificateNotification(certificateIssuanceId);
			if (!isSuccess) {
				return false;
			}
		}

		String notificationId = notificationRepository.getNotificationId();
		// 通知データを挿入
		isSuccess = notificationRepository.insertNotification(notificationId, userId, false, "1");
		if (!isSuccess) {
			return false;
		}

		// 通知データの挿入に成功した場合、trueを返却
		return notificationRepository.insertNotificationCertificateIssuance(notificationId, certificateIssuanceId);
	}

	/**
	 * 証明書発行の通知データを削除します。
	 *
	 * @param certificateIssuanceId 証明書発行ID
	 * @return true: 削除成功, false: 削除失敗
	 */
	public Boolean deleteCertificateNotification(String certificateIssuanceId) {
		// 通知データが存在する場合、削除
		return notificationRepository.deleteNotificationCertificateIssuance(certificateIssuanceId);
	}
}
