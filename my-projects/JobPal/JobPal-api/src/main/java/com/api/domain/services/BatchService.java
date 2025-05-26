package com.api.domain.services;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.api.domain.models.dbdata.CertificateIssuanceDashBoardData;
import com.api.domain.models.dbdata.JobSearchDashBoardData;
import com.api.domain.models.entities.DashBoardEntity;
import com.api.domain.repositories.CertificateIssuanceDashBoardRepository;
import com.api.domain.repositories.CertificateIssuanceRepository;
import com.api.domain.repositories.JobSearchDashBoardRepository;
import com.api.domain.repositories.NotificationRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * バッチ処理を管理するサービスクラスです。
 *
 * <p>
 * 主な機能:
 * <ul>
 * <li>定期的な再通知設定</li>
 * <li>支払期限の過ぎた証明書申請の削除</li>
 * <li>証明書削除時の通知メール送信</li>
 * </ul>
 * </p>
 *
 * <p>
 * <strong>エラー処理:</strong>
 * 処理中にエラーが発生した場合はログに記録され、呼び出し元は結果を判定することができます。<br>
 * 各メソッドの戻り値を使用して成功または失敗を判定してください。
 * </p>
 */
@Service
public class BatchService {
	/**
	 * 就職活動ダッシュボードリポジトリ。
	 */
	@Autowired
	private JobSearchDashBoardRepository jobSearchDashBoardRepository;
	/**
	 * 証明書発行ダッシュボードリポジトリ。
	 */
	@Autowired
	private CertificateIssuanceDashBoardRepository certificateIssuanceDashBoardRepository;
	/**
	 * 証明書発行リポジトリ。
	 */
	@Autowired
	private CertificateIssuanceRepository issuanceRepository;
	/**
	 * 通知リポジトリ。
	 */
	@Autowired
	private NotificationRepository notificationRepository;
	/**
	 * RestTemplateのインスタンス。
	 */
	@Autowired
	private RestTemplate restTemplate;
	/**
	 * ロガー。
	 */
	@Autowired
	Logger logger;

	/**
	 * 毎日0時にバッチ処理を実行します。
	 * このスケジュールはCron形式で指定しています。
	 */
	@Scheduled(cron = "1 1/1 * * * ?")
	public void executeBatch() {
		logger.info("Batch process started at: " + LocalDateTime.now());
		// バッチ処理のロジックを記述
		performBatchLogic();
		logger.info("Batch process completed at: " + LocalDateTime.now());
	}

	private void performBatchLogic() {
		logger.info("Performing batch logic...");
		boolean isSuccess = paymentDueCheck();
		if (!isSuccess) {
			logger.error("Failed to process payment due check.");
		}
		isSuccess = resendCheack();
		if (isSuccess) {
			logger.info("Batch logic completed successfully.");
		} else {
			logger.error("Batch logic failed.");
		}
	}

	/**
	 * 再通知の設定をします。
	 *
	 * @return 二日前以降の場合はtrue、それ以外はfalse
	 */
	private boolean resendCheack() {
		try {
			// すべての就職活動申請を取得
			DashBoardEntity dashBoardEntity = jobSearchDashBoardRepository.selectDashBoardAll(true);
			List<JobSearchDashBoardData> dashBoardList = dashBoardEntity.getDashBoardList();
			// 再通知の設定をする
			for (JobSearchDashBoardData jobSearchData : dashBoardList) {
				String status = jobSearchData.getJobSearchStatus();
				// 状態が完了の場合、スキップ
				if ("33".equals(status)) {
					continue;
				}
				// 対象の時間の設定
				Timestamp Time = jobSearchData.getMaxUpdatedAt();
				if ("21".equals(status) || "23".equals(status)) {
					Time = jobSearchData.getEndTime();
				}
				// 時間が超過していた場合、再通知の設定
				if (isOverTime(Time)) {
					boolean isSuccess = notificationRepository
							.updateNotificationJobSearch(jobSearchData.getJobSearchId());
					if (!isSuccess) {
						return false;
					}
				}
			}
			// すべての証明書申請を取得
			List<CertificateIssuanceDashBoardData> certificateIssuanceDashBoardDatas = certificateIssuanceDashBoardRepository
					.selectCertificateIssuanceDashBoard();
			for (CertificateIssuanceDashBoardData certificateIssuanceDashBoardData : certificateIssuanceDashBoardDatas) {
				String status = certificateIssuanceDashBoardData.getStatus();
				// 状態が完了の場合、スキップ
				if ("6".equals(status)) {
					continue;
				}
				// 時間が超過していた場合、再通知の設定
				if (isOverTime(new Timestamp(certificateIssuanceDashBoardData.getLatestDate().getTime()))) {
					Boolean isSuccess = notificationRepository.updateNotificationCertificateIssuance(
							certificateIssuanceDashBoardData.getCertificateIssueId());
					if (!isSuccess) {
						return false;
					}
				}
			}
			return true;
			// エラーが発生した場合、falseを返す
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 指定された日時が現在時刻の二日前以降かを判定します。
	 *
	 * @param timestamp 判定対象の日時
	 * @return 二日前以降の場合はtrue、それ以外はfalse
	 */
	private boolean isOverTime(Timestamp timestamp) {
		LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Tokyo"));
		LocalDateTime targetDate = timestamp.toLocalDateTime();
		return ChronoUnit.DAYS.between(targetDate, now) >= 2;
	}

	/**
	 * 証明書の支払期限の過ぎた申請を削除し、メールで通達する
	 *
	 * @return 成功：ture 失敗：false
	 */
	private boolean paymentDueCheck() {
		try {
			// 証明書の支払期限が過ぎた申請を削除する
			List<Map<String, Date>> certificateList = issuanceRepository.selectCertificateIssuanceApprovalDateOne();
			// 本日の日付を取得
			Calendar today = Calendar.getInstance();
			today.set(Calendar.HOUR_OF_DAY, 0);
			today.set(Calendar.MINUTE, 0);
			today.set(Calendar.SECOND, 0);
			today.set(Calendar.MILLISECOND, 0);
			for (Map<String, Date> certificate : certificateList) {
				Date valueDate = certificate.values().iterator().next();
				// 指定された日付の2日後を計算
				Calendar valueDatePlusTwo = Calendar.getInstance();
				valueDatePlusTwo.setTime(valueDate);
				valueDatePlusTwo.add(Calendar.DAY_OF_YEAR, 2);
				// 日付を比較
				if (!today.getTime().after(valueDatePlusTwo.getTime())) {
					continue;
				}
				String certificateIssueId = certificate.keySet().iterator().next();
				Map<String, String> userIdList = issuanceRepository.selectCertificateIssueUserId(certificateIssueId);
				String from = userIdList.get("teacher");
				String to = userIdList.get("student");
				boolean isSuccess = notificationRepository.deleteNotificationCertificateIssuance(certificateIssueId);
				if (!isSuccess) {
					logger.error(
							"Failed to delete notification for certificate issuance with ID: " + certificateIssueId);
					continue;
				}
				isSuccess = issuanceRepository.deleteCertificateIssuanceOne(certificateIssueId);
				if (isSuccess) {
					isSuccess = sendCertificateDeletionNotification(from, to,
							certificateIssueId);
					if (isSuccess) {
						logger.info("Sent email notification for certificate deletion with ID: " + certificateIssueId);
					} else {
						logger.error("Failed to send email notification for certificate deletion with ID: "
								+ certificateIssueId);
					}
					logger.info("Deleted certificate issuance with ID: " + certificateIssueId);
				} else {
					logger.error("Failed to delete certificate issuance with ID: " + certificateIssueId);
				}
			}
			// 成功した場合、trueを返す
			return true;
		} catch (Exception e) {
			// エラーが発生した場合、falseを返す
			return false;
		}
	}

	/**
	 * メール送信APIを用いて証明書が削除されたことを通知します。
	 *
	 * @param from               返信先メールアドレス
	 * @param to                 受信者メールアドレス
	 * @param certificateIssueId 証明書発行ID
	 * @return 成功：ture 失敗：false
	 */
	private boolean sendCertificateDeletionNotification(String from, String to, String certificateIssueId) {
		if (from == null || from.isEmpty()) {
			return false;
		}
		if (to == null || to.isEmpty()) {
			return false;
		}
		try {
			String json = request(from, to, certificateIssueId);
			logger.info("Email notification response: " + json);
			return convert(json);
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 外部AIサービスにリクエストを送信します。
	 *
	 * @param examReport 受験報告テキスト
	 * @return AIサービスのレスポンス
	 */
	private String request(String from, String to, String certificateIssueId) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		// 件名、本文を設定
		String subject = "証明書の削除通知";
		String body = String.format("【申請番号：%s】証明書が削除されました。", certificateIssueId);
		// JSON形式のリクエストボディを作成
		String jsonRequest = String.format(
				"{\"from\":\"%s\", \"to\":\"%s\", \"subject\":\"%s\", \"body\":\"%s\"}",
				from, to, subject, body);
		HttpEntity<String> request = new HttpEntity<>(jsonRequest, headers);
		// メール送信APIのエンドポイント
		String apiUrl = "https://script.google.com/macros/s/AKfycbwCn_EU5ogoeKYJFNhh9ckCfdv__XtSWzEI6iEWfp6KpxtdfgBBkJiHCBROszJxShVL/exec";
		// POSTリクエストを送信してレスポンスを取得
		return restTemplate.postForObject(apiUrl, request, String.class);
	}

	/**
	 * AIサービスからのJSONレスポンスを解析します。
	 *
	 * @param json JSONレスポンス
	 * @return 解析結果の文字列
	 */
	private boolean convert(String json) {
		try {
			// JacksonのObjectMapperを使用してJSONレスポンスを解析
			ObjectMapper mapper = new ObjectMapper();
			JsonNode rootNode = mapper.readTree(json);
			// "status"と"message"フィールドを取得
			int status = rootNode.get("status").asInt();
			String message = rootNode.get("message").asText();
			// ステータスが200の場合はtrueを返す
			if (status == 200) {
				return true;
			}
			// ステータスが200以外の場合はfalseを返す
			logger.error("Failed to send email: " + message);
			return false;
		} catch (JsonProcessingException e) {
			// JSON解析エラー時の例外処理
			logger.error("Failed to parse JSON response: " + e.getMessage());
			return false;
		}
	}
}
