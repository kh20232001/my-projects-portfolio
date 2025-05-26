package com.api.domain.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

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
import com.api.jobpal.common.base.DBDataConversion;

@RunWith(SpringRunner.class)
@SpringBootTest
class NotificationServiceTest {

	@Autowired
	private NotificationService target;

	// 下の階層をモック化
	@SpyBean
	private UserRepository userRepository;

	@SpyBean
	private NotificationRepository notificationRepository;

	@SpyBean
	private NamedParameterJdbcTemplate jdbc;

	@SpyBean
	private DBDataConversion dbdc;

	@Autowired
	private Logger log;

	@Test
	void 全ユーザーの就職活動申請の取得テスト再通知がある場合() {
		// 0.モック設定
		List<NotificationDashBoardData> mockNotificationJobSearchList = new ArrayList<>();

		JobSearchDashBoardData mockJobSearchDashBoardData = new JobSearchDashBoardData();
		mockJobSearchDashBoardData.setUserName("山田 太郎");
		mockJobSearchDashBoardData.setJobSearchStatus("11");
		mockJobSearchDashBoardData.setStartTime(Timestamp.valueOf("2024-12-01 09:00:00"));
		mockJobSearchDashBoardData.setCompanyName("株式会社テスト");
		mockJobSearchDashBoardData.setEventCategory("0");
		mockJobSearchDashBoardData.setResult("2");
		mockJobSearchDashBoardData.setSchoolCheckFlag(true);
		mockJobSearchDashBoardData.setEndTime(Timestamp.valueOf("2024-12-01 11:00:00"));
		mockJobSearchDashBoardData.setJobSearchId("JOB12345");
		mockJobSearchDashBoardData.setDepartment("T");
		mockJobSearchDashBoardData.setGrade(3);
		mockJobSearchDashBoardData.setClassName("A2");
		mockJobSearchDashBoardData.setAttendanceNumber(12);
		mockJobSearchDashBoardData.setUserId("test@hcs.ac.jp");
		mockJobSearchDashBoardData.setMaxUpdatedAt(Timestamp.valueOf("2024-12-01 12:00:00"));

		NotificationDashBoardData mockDashBoardData = new NotificationDashBoardData();
		mockDashBoardData.setCertificateIssuanceDashBoardData(null);
		mockDashBoardData.setJobSearchDashBoardData(mockJobSearchDashBoardData);
		mockDashBoardData.setAssignedUserId("student@hcs.ac.jp");
		mockDashBoardData.setJobSearchCertificateCategory("1");
		mockDashBoardData.setNotifiationId("N001");
		mockDashBoardData.setNotificationCreatedAt(Timestamp.valueOf("2024-01-09 12:00:00"));
		mockDashBoardData.setResendFlag(true);

		mockNotificationJobSearchList.add(mockDashBoardData);

		List<NotificationDashBoardData> mockNotificationCertificateList = new ArrayList<>();

		// 証明書データのリストを作成
		List<CertificateData> certificateList = new ArrayList<>();

		// 証明書データを設定
		CertificateData cert1 = new CertificateData();
		cert1.setCertificateId("2");
		cert1.setCertificateName("1");
		cert1.setCertificateQuantity(2);
		cert1.setCertificateFee(500);
		cert1.setCertificateWeight(50);
		certificateList.add(cert1);

		// 証明書発行ダッシュボードデータを設定
		CertificateIssuanceDashBoardData mocDashBoardData = new CertificateIssuanceDashBoardData();
		mocDashBoardData.setCertificateIssueId("ISSUE001");
		mocDashBoardData.setStudentUserId("user@example.com");
		mocDashBoardData.setStudentUserName("山田 太郎");
		mocDashBoardData.setStudentId(12345);
		mocDashBoardData.setStatus("6");
		mocDashBoardData.setMediaType("2");
		mocDashBoardData.setLatestDate(Date.valueOf("2024-11-27"));
		mocDashBoardData.setCertificateList(certificateList);
		mocDashBoardData.setTotalAmount(1000);
		mocDashBoardData.setTotalWeight(14);
		mocDashBoardData.setTotalMailFee(700); // 仮の送料

		mockDashBoardData = new NotificationDashBoardData();
		mockDashBoardData.setCertificateIssuanceDashBoardData(mocDashBoardData);
		mockDashBoardData.setJobSearchDashBoardData(null);
		mockDashBoardData.setAssignedUserId("student@hcs.ac.jp");
		mockDashBoardData.setJobSearchCertificateCategory("1");
		mockDashBoardData.setNotifiationId("N003");
		mockDashBoardData.setNotificationCreatedAt(Timestamp.valueOf("2024-01-09 12:00:00"));
		mockDashBoardData.setResendFlag(true);

		mockNotificationCertificateList.add(mockDashBoardData);

		doReturn("山田 太郎").when(userRepository).selectUserName(anyString());
		doReturn(mockNotificationJobSearchList).when(notificationRepository).getNotificationJobSearchList(anyString());
		doReturn(mockNotificationCertificateList).when(notificationRepository)
				.getNotificationCertificateIssuanceList(anyString());

		// 1.テストデータ設定
		String userId = "test@hcs.ac.jp";

		// 2.テスト対象メソッド実行
		AlertDisplayEntity result = target.getAlertAll(userId);

		DashBoardDisplayData dashboadData = result.getDashBoardList().get(0);

		CertificateDisplayData certificateData = result.getCertificateList().get(0);
		CertificateDisplayItem resultItem1 = certificateData.getCertificateList().get(0);

		// 3.テスト結果確認
		assertEquals("山田 太郎", result.getUserName());
		assertEquals("JOB12345", dashboadData.getJobHuntId());
		assertEquals("test@hcs.ac.jp", dashboadData.getUserId());
		assertEquals("山田 太郎", dashboadData.getUserName());
		assertEquals("T3A212", dashboadData.getSchoolClassNumber());
		assertEquals("承認待ち", dashboadData.getJobStateId());
		assertEquals("株式会社テスト", dashboadData.getCompany());
		assertEquals("説明会_単", dashboadData.getEventCategory());
		assertEquals("継続（合格）", dashboadData.getResult());
		assertTrue(dashboadData.getSchoolCheck());
		assertEquals("12/01", dashboadData.getDate());
		assertEquals("09:00", dashboadData.getStartTime());
		assertEquals("11:00", dashboadData.getFinishTime());
		assertEquals(0, dashboadData.getEventCategoryPriority());
		assertEquals(11, dashboadData.getStateIdPriority());
		assertEquals(Long.valueOf(20241201090000L), dashboadData.getStartTimePriority());
		assertTrue(dashboadData.isReNotifyFlag());
		assertEquals("ISSUE001", certificateData.getCertificateIssueId());
		assertEquals("user@example.com", certificateData.getUserId());
		assertEquals("山田 太郎", certificateData.getUserName());
		assertEquals(12345, certificateData.getSchoolClassNumber());
		assertEquals("完了", certificateData.getCertificateStateName());
		assertEquals("11/27", certificateData.getDate());
		assertEquals(1000, certificateData.getTotalAmount());
		assertEquals("郵送", certificateData.getMediaName());
		assertEquals(6, certificateData.getStateIdPriority());
		assertEquals(20241127000000L, certificateData.getStartTimePriority());
		assertTrue(certificateData.isReNotifyFlag());
		assertEquals("2", resultItem1.getCertificateId());
		assertEquals("卒業", resultItem1.getCertificateName());
		assertEquals(2, resultItem1.getCount());

		// 4.ログ確認
		log.info("全ユーザーの就職活動申請の取得テスト再通知がある場合 " + result.toString());
	}

	@Test
	void 全ユーザーの就職活動申請の取得テスト再通知がない場合() {
		// 0.モック設定
		List<NotificationDashBoardData> mockNotificationJobSearchList = new ArrayList<>();

		JobSearchDashBoardData mockJobSearchDashBoardData = new JobSearchDashBoardData();
		mockJobSearchDashBoardData.setUserName("山田 太郎");
		mockJobSearchDashBoardData.setJobSearchStatus("11");
		mockJobSearchDashBoardData.setStartTime(Timestamp.valueOf("2024-12-01 09:00:00"));
		mockJobSearchDashBoardData.setCompanyName("株式会社テスト");
		mockJobSearchDashBoardData.setEventCategory("0");
		mockJobSearchDashBoardData.setResult("2");
		mockJobSearchDashBoardData.setSchoolCheckFlag(true);
		mockJobSearchDashBoardData.setEndTime(Timestamp.valueOf("2024-12-01 11:00:00"));
		mockJobSearchDashBoardData.setJobSearchId("JOB12345");
		mockJobSearchDashBoardData.setDepartment("T");
		mockJobSearchDashBoardData.setGrade(3);
		mockJobSearchDashBoardData.setClassName("A2");
		mockJobSearchDashBoardData.setAttendanceNumber(12);
		mockJobSearchDashBoardData.setUserId("test@hcs.ac.jp");
		mockJobSearchDashBoardData.setMaxUpdatedAt(Timestamp.valueOf("2024-12-01 12:00:00"));

		NotificationDashBoardData mockDashBoardData = new NotificationDashBoardData();
		mockDashBoardData.setCertificateIssuanceDashBoardData(null);
		mockDashBoardData.setJobSearchDashBoardData(mockJobSearchDashBoardData);
		mockDashBoardData.setAssignedUserId("student@hcs.ac.jp");
		mockDashBoardData.setJobSearchCertificateCategory("1");
		mockDashBoardData.setNotifiationId("N001");
		mockDashBoardData.setNotificationCreatedAt(Timestamp.valueOf("2024-01-09 12:00:00"));
		mockDashBoardData.setResendFlag(false);

		mockNotificationJobSearchList.add(mockDashBoardData);

		List<NotificationDashBoardData> mockNotificationCertificateList = new ArrayList<>();

		// 証明書データのリストを作成
		List<CertificateData> certificateList = new ArrayList<>();

		// 証明書データを設定
		CertificateData cert1 = new CertificateData();
		cert1.setCertificateId("2");
		cert1.setCertificateName("1");
		cert1.setCertificateQuantity(2);
		cert1.setCertificateFee(500);
		cert1.setCertificateWeight(50);
		certificateList.add(cert1);

		// 証明書発行ダッシュボードデータを設定
		CertificateIssuanceDashBoardData mocDashBoardData = new CertificateIssuanceDashBoardData();
		mocDashBoardData.setCertificateIssueId("ISSUE001");
		mocDashBoardData.setStudentUserId("user@example.com");
		mocDashBoardData.setStudentUserName("山田 太郎");
		mocDashBoardData.setStudentId(12345);
		mocDashBoardData.setStatus("6");
		mocDashBoardData.setMediaType("2");
		mocDashBoardData.setLatestDate(Date.valueOf("2024-11-27"));
		mocDashBoardData.setCertificateList(certificateList);
		mocDashBoardData.setTotalAmount(1000);
		mocDashBoardData.setTotalWeight(14);
		mocDashBoardData.setTotalMailFee(700); // 仮の送料

		mockDashBoardData = new NotificationDashBoardData();
		mockDashBoardData.setCertificateIssuanceDashBoardData(mocDashBoardData);
		mockDashBoardData.setJobSearchDashBoardData(null);
		mockDashBoardData.setAssignedUserId("student@hcs.ac.jp");
		mockDashBoardData.setJobSearchCertificateCategory("1");
		mockDashBoardData.setNotifiationId("N003");
		mockDashBoardData.setNotificationCreatedAt(Timestamp.valueOf("2024-01-09 12:00:00"));
		mockDashBoardData.setResendFlag(false);

		mockNotificationCertificateList.add(mockDashBoardData);

		doReturn("山田 太郎").when(userRepository).selectUserName(anyString());
		doReturn(mockNotificationJobSearchList).when(notificationRepository).getNotificationJobSearchList(anyString());
		doReturn(mockNotificationCertificateList).when(notificationRepository)
				.getNotificationCertificateIssuanceList(anyString());

		// 1.テストデータ設定
		String userId = "test@hcs.ac.jp";

		// 2.テスト対象メソッド実行
		AlertDisplayEntity result = target.getAlertAll(userId);

		DashBoardDisplayData dashboadData = result.getDashBoardList().get(0);

		CertificateDisplayData certificateData = result.getCertificateList().get(0);
		CertificateDisplayItem resultItem1 = certificateData.getCertificateList().get(0);

		// 3.テスト結果確認
		assertEquals("山田 太郎", result.getUserName());
		assertEquals("JOB12345", dashboadData.getJobHuntId());
		assertEquals("test@hcs.ac.jp", dashboadData.getUserId());
		assertEquals("山田 太郎", dashboadData.getUserName());
		assertEquals("T3A212", dashboadData.getSchoolClassNumber());
		assertEquals("承認待ち", dashboadData.getJobStateId());
		assertEquals("株式会社テスト", dashboadData.getCompany());
		assertEquals("説明会_単", dashboadData.getEventCategory());
		assertEquals("継続（合格）", dashboadData.getResult());
		assertTrue(dashboadData.getSchoolCheck());
		assertEquals("12/01", dashboadData.getDate());
		assertEquals("09:00", dashboadData.getStartTime());
		assertEquals("11:00", dashboadData.getFinishTime());
		assertEquals(0, dashboadData.getEventCategoryPriority());
		assertEquals(11, dashboadData.getStateIdPriority());
		assertEquals(Long.valueOf(20241201090000L), dashboadData.getStartTimePriority());
		assertFalse(dashboadData.isReNotifyFlag());
		assertEquals("ISSUE001", certificateData.getCertificateIssueId());
		assertEquals("user@example.com", certificateData.getUserId());
		assertEquals("山田 太郎", certificateData.getUserName());
		assertEquals(12345, certificateData.getSchoolClassNumber());
		assertEquals("完了", certificateData.getCertificateStateName());
		assertEquals("11/27", certificateData.getDate());
		assertEquals(1000, certificateData.getTotalAmount());
		assertEquals("郵送", certificateData.getMediaName());
		assertEquals(6, certificateData.getStateIdPriority());
		assertEquals(20241127000000L, certificateData.getStartTimePriority());
		assertFalse(certificateData.isReNotifyFlag());
		assertEquals("2", resultItem1.getCertificateId());
		assertEquals("卒業", resultItem1.getCertificateName());
		assertEquals(2, resultItem1.getCount());

		// 4.ログ確認
		log.info("全ユーザーの就職活動申請の取得テスト再通知がない場合 " + result.toString());
	}

	@Test
	void 就職活動の通知データを挿入テスト既存データがある場合() {
		// 0.モック設定
		doReturn(true).when(notificationRepository).deleteNotificationJobSearch(anyString());
		doReturn("N001").when(notificationRepository).getNotificationId();
		doReturn(true).when(notificationRepository).insertNotification(anyString(), anyString(),
				anyBoolean(), anyString());
		doReturn(true).when(notificationRepository)
				.insertNotificationJobSearch(anyString(), anyString());

		// 1.テストデータ設定
		String userId = "test@hcs.ac.jp";
		String jobSearchId = "JS001";

		// 2.テスト対象メソッド実行
		boolean result = target.insertJobsearchNotification(userId, jobSearchId);

		// 3.テスト結果確認
		assertTrue(result);

		// 4.ログ確認
		log.info("就職活動の通知データを挿入テスト既存データがない場合 " + result);
	}

	@Test
	void 就職活動の通知データを挿入テスト既存データがない場合() {
		// 0.モック設定
		doReturn(true).when(notificationRepository).deleteNotificationJobSearch(anyString());
		doReturn("N001").when(notificationRepository).getNotificationId();
		doReturn(true).when(notificationRepository).insertNotification(anyString(), anyString(),
				anyBoolean(), anyString());
		doReturn(true).when(notificationRepository)
				.insertNotificationJobSearch(anyString(), anyString());

		// 1.テストデータ設定
		String userId = "test@hcs.ac.jp";
		String jobSearchId = "JS001";
		Boolean isFirst = true;

		// 2.テスト対象メソッド実行
		boolean result = target.insertJobsearchNotification(userId, jobSearchId, isFirst);

		// 3.テスト結果確認
		assertTrue(result);

		// 4.ログ確認
		log.info("就職活動の通知データを挿入テスト既存データがない場合 " + result);
	}

	@Test
	void 就職活動の通知データを挿入テストdeleteNotificationJobSearch失敗した場合() {
		// 0.モック設定
		doReturn(false).when(notificationRepository).deleteNotificationJobSearch(anyString());

		// 1.テストデータ設定
		String userId = "test@hcs.ac.jp";
		String jobSearchId = "JS001";

		// 2.テスト対象メソッド実行
		boolean result = target.insertJobsearchNotification(userId, jobSearchId);

		// 3.テスト結果確認
		assertFalse(result);

		// 4.ログ確認
		log.info("就職活動の通知データを挿入テストdeleteNotificationJobSearch失敗した場合 " + result);
	}

	@Test
	void 就職活動の通知データを挿入テストinsertNotificationが失敗した場合() {
		// 0.モック設定
		doReturn(true).when(notificationRepository).deleteNotificationJobSearch(anyString());
		doReturn("N001").when(notificationRepository).getNotificationId();
		doReturn(false).when(notificationRepository).insertNotification(anyString(), anyString(),
				anyBoolean(), anyString());

		// 1.テストデータ設定
		String userId = "test@hcs.ac.jp";
		String jobSearchId = "JS001";
		Boolean isFirst = true;

		// 2.テスト対象メソッド実行
		boolean result = target.insertJobsearchNotification(userId, jobSearchId, isFirst);

		// 3.テスト結果確認
		assertFalse(result);

		// 4.ログ確認
		log.info("就職活動の通知データを挿入テストinsertNotificationが失敗した場合 " + result);
	}

	@Test
	void 就職活動の通知データを挿入テストinsertNotificationJobSearchが失敗した場合() {
		// 0.モック設定
		doReturn(true).when(notificationRepository).deleteNotificationJobSearch(anyString());
		doReturn("N001").when(notificationRepository).getNotificationId();
		doReturn(true).when(notificationRepository).insertNotification(anyString(), anyString(),
				anyBoolean(), anyString());
		doReturn(false).when(notificationRepository)
				.insertNotificationJobSearch(anyString(), anyString());

		// 1.テストデータ設定
		String userId = "test@hcs.ac.jp";
		String jobSearchId = "JS001";
		Boolean isFirst = true;

		// 2.テスト対象メソッド実行
		boolean result = target.insertJobsearchNotification(userId, jobSearchId, isFirst);

		// 3.テスト結果確認
		assertFalse(result);

		// 4.ログ確認
		log.info("就職活動の通知データを挿入テストinsertNotificationJobSearchが失敗した場合 " + result);
	}

	@Test
	void 証明書の通知データを挿入テスト既存データがある場合() {
		// 0.モック設定
		doReturn(true).when(notificationRepository).deleteNotificationCertificateIssuance(anyString());
		doReturn("N001").when(notificationRepository).getNotificationId();
		doReturn(true).when(notificationRepository).insertNotification(anyString(), anyString(),
				anyBoolean(), anyString());
		doReturn(true).when(notificationRepository)
				.insertNotificationCertificateIssuance(anyString(), anyString());

		// 1.テストデータ設定
		String userId = "test@hcs.ac.jp";
		String jobSearchId = "JS001";

		// 2.テスト対象メソッド実行
		boolean result = target.insertCertificateNotification(userId, jobSearchId);

		// 3.テスト結果確認
		assertTrue(result);

		// 4.ログ確認
		log.info("証明書の通知データを挿入テスト既存データがある場合 " + result);
	}

	@Test
	void 証明書の通知データを挿入テスト既存データがない場合() {
		// 0.モック設定
		doReturn("N001").when(notificationRepository).getNotificationId();
		doReturn(true).when(notificationRepository).insertNotification(anyString(), anyString(),
				anyBoolean(), anyString());
		doReturn(true).when(notificationRepository)
				.insertNotificationCertificateIssuance(anyString(), anyString());

		// 1.テストデータ設定
		String userId = "test@hcs.ac.jp";
		String jobSearchId = "JS001";
		Boolean isFirst = true;

		// 2.テスト対象メソッド実行
		boolean result = target.insertCertificateNotification(userId, jobSearchId, isFirst);

		// 3.テスト結果確認
		assertTrue(result);

		// 4.ログ確認
		log.info("証明書の通知データを挿入テスト既存データがない場合 " + result);
	}

	@Test
	void 証明書の通知データを挿入テストdeleteNotificationCertificateIssuanceが失敗した場合() {
		// 0.モック設定
		doReturn(false).when(notificationRepository).deleteNotificationCertificateIssuance(anyString());

		// 1.テストデータ設定
		String userId = "test@hcs.ac.jp";
		String jobSearchId = "JS001";

		// 2.テスト対象メソッド実行
		boolean result = target.insertCertificateNotification(userId, jobSearchId);

		// 3.テスト結果確認
		assertFalse(result);

		// 4.ログ確認
		log.info("証明書の通知データを挿入テストdeleteNotificationCertificateIssuanceが失敗した場合 " + result);
	}

	@Test
	void 証明書の通知データを挿入テストinsertNotificationCertificateIssuanceが失敗した場合() {
		// 0.モック設定
		doReturn("N001").when(notificationRepository).getNotificationId();
		doReturn(true).when(notificationRepository).insertNotification(anyString(), anyString(),
				anyBoolean(), anyString());
		doReturn(false).when(notificationRepository)
				.insertNotificationCertificateIssuance(anyString(), anyString());

		// 1.テストデータ設定
		String userId = "test@hcs.ac.jp";
		String jobSearchId = "JS001";
		Boolean isFirst = true;

		// 2.テスト対象メソッド実行
		boolean result = target.insertCertificateNotification(userId, jobSearchId, isFirst);

		// 3.テスト結果確認
		assertFalse(result);

		// 4.ログ確認
		log.info("証明書の通知データを挿入テストinsertNotificationCertificateIssuanceが失敗した場合 " + result);
	}

	@Test
	void 証明書の通知データを挿入テストinsertNotificationが失敗した場合() {
		// 0.モック設定
		doReturn("N001").when(notificationRepository).getNotificationId();
		doReturn(false).when(notificationRepository).insertNotification(anyString(), anyString(),
				anyBoolean(), anyString());

		// 1.テストデータ設定
		String userId = "test@hcs.ac.jp";
		String jobSearchId = "JS001";
		Boolean isFirst = true;

		// 2.テスト対象メソッド実行
		boolean result = target.insertCertificateNotification(userId, jobSearchId, isFirst);

		// 3.テスト結果確認
		assertFalse(result);

		// 4.ログ確認
		log.info("証明書の通知データを挿入テストinsertNotificationが失敗した場合 " + result);
	}

}
