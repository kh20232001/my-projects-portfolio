package com.api.domain.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.test.context.junit4.SpringRunner;

import com.api.domain.models.dbdata.CertificateData;
import com.api.domain.models.dbdata.CertificateIssuanceDashBoardData;
import com.api.domain.models.dbdata.MailingData;
import com.api.domain.models.displaydata.CertificateDisplayData;
import com.api.domain.models.displaydata.CertificateDisplayItem;
import com.api.domain.models.displaydata.CertificateFeeData;
import com.api.domain.models.entities.CertificateDashBoardEntity;
import com.api.domain.models.entities.CertificateFeeAndWeightEntity;
import com.api.domain.models.forms.CertificateDetailDataForm;
import com.api.domain.models.forms.CertificateInsertForm;
import com.api.domain.repositories.CertificateIssuanceDashBoardRepository;
import com.api.domain.repositories.CertificateIssuanceRepository;
import com.api.domain.repositories.NotificationRepository;
import com.api.domain.repositories.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
class CertificateDashBoardServiceTest {

	@Autowired
	private CertificateDashBoardService target;

	// 下の階層をモック化
	@SpyBean
	private CertificateIssuanceDashBoardRepository dashBoardRepository;

	@SpyBean
	private CertificateIssuanceRepository certificateRepository;

	@SpyBean
	private UserRepository newUserRepository;

	@SpyBean
	private NotificationRepository notificationRepository;

	@SpyBean
	private CertificateDashBoardDetailService certificateService;

	@SpyBean
	private NotificationService notificationService;

	@Autowired
	private Logger log;

	@Test
	void 全ユーザーの証明書申請の取得テスト() {
		// 0.モック設定
		List<CertificateIssuanceDashBoardData> mockCertificateDatas = new ArrayList<>();

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

		mockCertificateDatas.add(mocDashBoardData);

		doReturn(mockCertificateDatas).when(dashBoardRepository).selectCertificateIssuanceDashBoard();
		doReturn(0).when(notificationRepository).getNotificationCount(anyString());

		// 1.テストデータ設定
		String userId = "test@hcs.ac.jp";

		// 2.テスト対象メソッド実行
		CertificateDashBoardEntity resultEntity = new CertificateDashBoardEntity();
		try {
			resultEntity = target.getDashBoardAll(userId);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 3.テスト結果確認
		assertEquals(null, resultEntity.getUserName());
		assertEquals(0, resultEntity.getAlertCnt());
		CertificateDisplayData resultData = resultEntity.getDashBoardList().get(0);
		assertEquals("ISSUE001", resultData.getCertificateIssueId());
		assertEquals("user@example.com", resultData.getUserId());
		assertEquals("山田 太郎", resultData.getUserName());
		assertEquals(12345, resultData.getSchoolClassNumber());
		assertEquals("完了", resultData.getCertificateStateName());
		assertEquals("11/27", resultData.getDate());
		assertEquals(1000, resultData.getTotalAmount());
		assertEquals("郵送", resultData.getMediaName());
		assertEquals(6, resultData.getStateIdPriority());
		assertEquals(20241127000000L, resultData.getStartTimePriority());
		assertFalse(resultData.isReNotifyFlag());
		List<CertificateDisplayItem> actualCertificateList = resultData.getCertificateList();
		assertEquals("2", actualCertificateList.get(0).getCertificateId());
		assertEquals("卒業", actualCertificateList.get(0).getCertificateName());
		assertEquals(2, actualCertificateList.get(0).getCount());

		// 4.ログ確認
		log.info("全ユーザーの証明書申請の取得テスト " + resultEntity.toString());
	}

	@Test
	void ユーザーの証明書申請の取得テスト() {
		// 0.モック設定
		List<CertificateIssuanceDashBoardData> mockCertificateDatas = new ArrayList<>();

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

		mockCertificateDatas.add(mocDashBoardData);

		doReturn(mockCertificateDatas).when(dashBoardRepository).selectCertificateIssuanceDashBoard(anyString(),
				anyString());
		doReturn(0).when(notificationRepository).getNotificationCount(anyString());

		// 1.テストデータ設定
		String userId = "test@hcs.ac.jp";
		String userType = "2";

		// 2.テスト対象メソッド実行
		CertificateDashBoardEntity resultEntity = new CertificateDashBoardEntity();
		try {
			resultEntity = target.getDashBoardUser(userId, userType);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 3.テスト結果確認
		assertEquals(null, resultEntity.getUserName());
		assertEquals(0, resultEntity.getAlertCnt());
		CertificateDisplayData resultData = resultEntity.getDashBoardList().get(0);
		assertEquals("ISSUE001", resultData.getCertificateIssueId());
		assertEquals("user@example.com", resultData.getUserId());
		assertEquals("山田 太郎", resultData.getUserName());
		assertEquals(12345, resultData.getSchoolClassNumber());
		assertEquals("完了", resultData.getCertificateStateName());
		assertEquals("11/27", resultData.getDate());
		assertEquals(1000, resultData.getTotalAmount());
		assertEquals("郵送", resultData.getMediaName());
		assertEquals(6, resultData.getStateIdPriority());
		assertEquals(20241127000000L, resultData.getStartTimePriority());
		assertFalse(resultData.isReNotifyFlag());
		List<CertificateDisplayItem> actualCertificateList = resultData.getCertificateList();
		assertEquals("2", actualCertificateList.get(0).getCertificateId());
		assertEquals("卒業", actualCertificateList.get(0).getCertificateName());
		assertEquals(2, actualCertificateList.get(0).getCount());

		// 4.ログ確認
		log.info("全ユーザーの就職活動申請の取得テスト " + resultEntity.toString());
	}

	@Test
	void 新規証明書申請の作成テスト郵送の場合() {
		// 0.モック設定
		doReturn("test@example.com").when(newUserRepository).selectTeacherUserId(anyString());
		doReturn("ISSUE001").when(certificateRepository).getCertificateIssuanceId();
		doReturn(true).when(certificateRepository).insertCertificateIssuanceOne(any());
		doReturn(true).when(certificateRepository).insertCertificateIssuanceDetailOne(anyString(), any());
		doReturn("1").when(certificateRepository).selectPostalPaymentIdMax();
		doReturn(true).when(certificateRepository).insertMailingCertificateIssuanceOne(any(), anyString());
		doReturn(true).when(notificationService).insertCertificateNotification(anyString(), anyString(), anyBoolean());

		List<CertificateDetailDataForm> certificateList = new ArrayList<>();
		CertificateDetailDataForm form1 = new CertificateDetailDataForm.Builder()
				.certificateId("1") // 証明書ID（0~3のいずれか）
				.certificateName("成績証明書")
				.count(2)
				.build();

		certificateList.add(form1);

		CertificateInsertForm certificateInsertForm = new CertificateInsertForm.Builder()
				.userId("user@example.com")
				.lastName("山田")
				.firstName("太郎")
				.lastNameKana("ヤマダ")
				.firstNameKana("タロウ")
				.certificateList(certificateList)
				.mediaName("郵送")
				.zipCode("123-4567")
				.address("東京都千代田区1丁目")
				.afterAddress("1-1")
				.certificateIssueId(null)
				.build();

		// 2.テスト対象メソッド実行
		boolean result = target.createNewCertificate(certificateInsertForm);

		// 3.テスト結果確認
		assertTrue(result);

		// 4.ログ確認
		log.info("新規証明書申請の作成テスト郵送の場合 " + result);
	}

	@Test
	void 新規証明書申請の作成テスト電子の場合() {
		// 0.モック設定
		doReturn("test@example.com").when(newUserRepository).selectTeacherUserId(anyString());
		doReturn("ISSUE001").when(certificateRepository).getCertificateIssuanceId();
		doReturn(true).when(certificateRepository).insertCertificateIssuanceOne(any());
		doReturn(true).when(certificateRepository).insertCertificateIssuanceDetailOne(anyString(), any());
		doReturn(true).when(certificateRepository).insertElectronicCertificateIssuanceOne(anyString(), anyString());
		doReturn(true).when(notificationService).insertCertificateNotification(anyString(), anyString(), anyBoolean());

		List<CertificateDetailDataForm> certificateList = new ArrayList<>();
		CertificateDetailDataForm form1 = new CertificateDetailDataForm.Builder()
				.certificateId("1") // 証明書ID（0~3のいずれか）
				.certificateName("成績証明書")
				.count(2)
				.build();

		certificateList.add(form1);

		CertificateInsertForm certificateInsertForm = new CertificateInsertForm.Builder()
				.userId("user@example.com")
				.lastName("山田")
				.firstName("太郎")
				.lastNameKana("ヤマダ")
				.firstNameKana("タロウ")
				.certificateList(certificateList)
				.mediaName("電子")
				.zipCode(null)
				.address(null)
				.afterAddress(null)
				.certificateIssueId(null)
				.build();

		// 2.テスト対象メソッド実行
		boolean result = target.createNewCertificate(certificateInsertForm);

		// 3.テスト結果確認
		assertTrue(result);

		// 4.ログ確認
		log.info("新規証明書申請の作成テスト電子の場合 " + result);
	}

	@Test
	void 新規証明書申請の作成テストinsertCertificateIssuanceOneが失敗した場合() {
		// 0.モック設定
		doReturn("test@example.com").when(newUserRepository).selectTeacherUserId(anyString());
		doReturn("ISSUE001").when(certificateRepository).getCertificateIssuanceId();
		doReturn(false).when(certificateRepository).insertCertificateIssuanceOne(any());
		doReturn(true).when(certificateRepository).insertCertificateIssuanceDetailOne(anyString(), any());
		doReturn(true).when(certificateRepository).insertElectronicCertificateIssuanceOne(anyString(), anyString());
		doReturn(true).when(notificationService).insertCertificateNotification(anyString(), anyString(), anyBoolean());

		List<CertificateDetailDataForm> certificateList = new ArrayList<>();
		CertificateDetailDataForm form1 = new CertificateDetailDataForm.Builder()
				.certificateId("1") // 証明書ID（0~3のいずれか）
				.certificateName("成績証明書")
				.count(2)
				.build();

		certificateList.add(form1);

		CertificateInsertForm certificateInsertForm = new CertificateInsertForm.Builder()
				.userId("user@example.com")
				.lastName("山田")
				.firstName("太郎")
				.lastNameKana("ヤマダ")
				.firstNameKana("タロウ")
				.certificateList(certificateList)
				.mediaName("電子")
				.zipCode(null)
				.address(null)
				.afterAddress(null)
				.certificateIssueId(null)
				.build();

		// 2.テスト対象メソッド実行
		boolean result = target.createNewCertificate(certificateInsertForm);

		// 3.テスト結果確認
		assertFalse(result);

		// 4.ログ確認
		log.info("新規証明書申請の作成テストinsertCertificateIssuanceOneが失敗した場合 " + result);
	}

	@Test
	void 新規証明書申請の作成テストinsertCertificateIssuanceDetailOneが失敗した場合() {
		// 0.モック設定
		doReturn("test@example.com").when(newUserRepository).selectTeacherUserId(anyString());
		doReturn("ISSUE001").when(certificateRepository).getCertificateIssuanceId();
		doReturn(true).when(certificateRepository).insertCertificateIssuanceOne(any());
		doReturn(false).when(certificateRepository).insertCertificateIssuanceDetailOne(anyString(), any());
		doReturn(true).when(certificateRepository).insertElectronicCertificateIssuanceOne(anyString(), anyString());
		doReturn(true).when(notificationService).insertCertificateNotification(anyString(), anyString(), anyBoolean());

		List<CertificateDetailDataForm> certificateList = new ArrayList<>();
		CertificateDetailDataForm form1 = new CertificateDetailDataForm.Builder()
				.certificateId("1") // 証明書ID（0~3のいずれか）
				.certificateName("成績証明書")
				.count(2)
				.build();

		certificateList.add(form1);

		CertificateInsertForm certificateInsertForm = new CertificateInsertForm.Builder()
				.userId("user@example.com")
				.lastName("山田")
				.firstName("太郎")
				.lastNameKana("ヤマダ")
				.firstNameKana("タロウ")
				.certificateList(certificateList)
				.mediaName("電子")
				.zipCode(null)
				.address(null)
				.afterAddress(null)
				.certificateIssueId(null)
				.build();

		// 2.テスト対象メソッド実行
		boolean result = target.createNewCertificate(certificateInsertForm);

		// 3.テスト結果確認
		assertFalse(result);

		// 4.ログ確認
		log.info("新規証明書申請の作成テストinsertCertificateIssuanceDetailOneが失敗した場合 " + result);
	}

	@Test
	void 新規証明書申請の作成テストinsertElectronicCertificateIssuanceOneが失敗した場合() {
		// 0.モック設定
		doReturn("test@example.com").when(newUserRepository).selectTeacherUserId(anyString());
		doReturn("ISSUE001").when(certificateRepository).getCertificateIssuanceId();
		doReturn(true).when(certificateRepository).insertCertificateIssuanceOne(any());
		doReturn(true).when(certificateRepository).insertCertificateIssuanceDetailOne(anyString(), any());
		doReturn(false).when(certificateRepository).insertElectronicCertificateIssuanceOne(anyString(), anyString());
		doReturn(true).when(notificationService).insertCertificateNotification(anyString(), anyString(), anyBoolean());

		List<CertificateDetailDataForm> certificateList = new ArrayList<>();
		CertificateDetailDataForm form1 = new CertificateDetailDataForm.Builder()
				.certificateId("1") // 証明書ID（0~3のいずれか）
				.certificateName("成績証明書")
				.count(2)
				.build();

		certificateList.add(form1);

		CertificateInsertForm certificateInsertForm = new CertificateInsertForm.Builder()
				.userId("user@example.com")
				.lastName("山田")
				.firstName("太郎")
				.lastNameKana("ヤマダ")
				.firstNameKana("タロウ")
				.certificateList(certificateList)
				.mediaName("電子")
				.zipCode(null)
				.address(null)
				.afterAddress(null)
				.certificateIssueId(null)
				.build();

		// 2.テスト対象メソッド実行
		boolean result = target.createNewCertificate(certificateInsertForm);

		// 3.テスト結果確認
		assertFalse(result);

		// 4.ログ確認
		log.info("新規証明書申請の作成テストinsertElectronicCertificateIssuanceOneが失敗した場合 " + result);
	}

	@Test
	void 新規証明書申請の作成テストinsertCertificateNotificationが失敗した場合() {
		// 0.モック設定
		doReturn("test@example.com").when(newUserRepository).selectTeacherUserId(anyString());
		doReturn("ISSUE001").when(certificateRepository).getCertificateIssuanceId();
		doReturn(true).when(certificateRepository).insertCertificateIssuanceOne(any());
		doReturn(true).when(certificateRepository).insertCertificateIssuanceDetailOne(anyString(), any());
		doReturn(true).when(certificateRepository).insertElectronicCertificateIssuanceOne(anyString(), anyString());
		doReturn(false).when(notificationService).insertCertificateNotification(anyString(), anyString(), anyBoolean());

		List<CertificateDetailDataForm> certificateList = new ArrayList<>();
		CertificateDetailDataForm form1 = new CertificateDetailDataForm.Builder()
				.certificateId("1") // 証明書ID（0~3のいずれか）
				.certificateName("成績証明書")
				.count(2)
				.build();

		certificateList.add(form1);

		CertificateInsertForm certificateInsertForm = new CertificateInsertForm.Builder()
				.userId("user@example.com")
				.lastName("山田")
				.firstName("太郎")
				.lastNameKana("ヤマダ")
				.firstNameKana("タロウ")
				.certificateList(certificateList)
				.mediaName("電子")
				.zipCode(null)
				.address(null)
				.afterAddress(null)
				.certificateIssueId(null)
				.build();

		// 2.テスト対象メソッド実行
		boolean result = target.createNewCertificate(certificateInsertForm);

		// 3.テスト結果確認
		assertFalse(result);

		// 4.ログ確認
		log.info("新規証明書申請の作成テストinsertCertificateNotificationが失敗した場合 " + result);
	}

	@Test
	void 新規証明書申請の作成テストIncorrectResultSizeDataAccessExceptionの発生() {
		// 0.モック設定
		doThrow(new IncorrectResultSizeDataAccessException(1)).when(newUserRepository).selectTeacherUserId(anyString());

		List<CertificateDetailDataForm> certificateList = new ArrayList<>();
		CertificateDetailDataForm form1 = new CertificateDetailDataForm.Builder()
				.certificateId("1") // 証明書ID（0~3のいずれか）
				.certificateName("成績証明書")
				.count(2)
				.build();

		certificateList.add(form1);

		CertificateInsertForm certificateInsertForm = new CertificateInsertForm.Builder()
				.userId("user@example.com")
				.lastName("山田")
				.firstName("太郎")
				.lastNameKana("ヤマダ")
				.firstNameKana("タロウ")
				.certificateList(certificateList)
				.mediaName("電子")
				.zipCode(null)
				.address(null)
				.afterAddress(null)
				.certificateIssueId(null)
				.build();

		// 2.テスト対象メソッド実行
		boolean result = target.createNewCertificate(certificateInsertForm);

		// 3.テスト結果確認
		assertFalse(result);

		// 4.ログ確認
		log.info("新規証明書申請の作成テストIncorrectResultSizeDataAccessExceptionの発生 " + result);
	}

	@Test
	void 新規証明書申請の作成テストExceptionの発生() {
		// 0.モック設定
		doThrow(new RuntimeException()).when(newUserRepository).selectTeacherUserId(anyString());

		List<CertificateDetailDataForm> certificateList = new ArrayList<>();
		CertificateDetailDataForm form1 = new CertificateDetailDataForm.Builder()
				.certificateId("1") // 証明書ID（0~3のいずれか）
				.certificateName("成績証明書")
				.count(2)
				.build();

		certificateList.add(form1);

		CertificateInsertForm certificateInsertForm = new CertificateInsertForm.Builder()
				.userId("user@example.com")
				.lastName("山田")
				.firstName("太郎")
				.lastNameKana("ヤマダ")
				.firstNameKana("タロウ")
				.certificateList(certificateList)
				.mediaName("電子")
				.zipCode(null)
				.address(null)
				.afterAddress(null)
				.certificateIssueId(null)
				.build();

		// 2.テスト対象メソッド実行
		boolean result = target.createNewCertificate(certificateInsertForm);

		// 3.テスト結果確認
		assertFalse(result);

		// 4.ログ確認
		log.info("新規証明書申請の作成テストExceptionの発生 " + result);
	}

	@Test
	void 証明書の変更テスト() {
		// 0.モック設定
		doReturn(true).when(certificateService).deleteOne(anyString());
		doReturn("test@example.com").when(newUserRepository).selectTeacherUserId(anyString());
		doReturn("ISSUE001").when(certificateRepository).getCertificateIssuanceId();
		doReturn(true).when(certificateRepository).insertCertificateIssuanceOne(any());
		doReturn(true).when(certificateRepository).insertCertificateIssuanceDetailOne(anyString(), any());
		doReturn("1").when(certificateRepository).selectPostalPaymentIdMax();
		doReturn(true).when(certificateRepository).insertMailingCertificateIssuanceOne(any(), anyString());
		doReturn(true).when(notificationService).insertCertificateNotification(anyString(), anyString(), anyBoolean());

		List<CertificateDetailDataForm> certificateList = new ArrayList<>();
		CertificateDetailDataForm form1 = new CertificateDetailDataForm.Builder()
				.certificateId("1") // 証明書ID（0~3のいずれか）
				.certificateName("成績証明書")
				.count(2)
				.build();

		certificateList.add(form1);

		CertificateInsertForm certificateInsertForm = new CertificateInsertForm.Builder()
				.userId("user@example.com")
				.lastName("山田")
				.firstName("太郎")
				.lastNameKana("ヤマダ")
				.firstNameKana("タロウ")
				.certificateList(certificateList)
				.mediaName("郵送")
				.zipCode("123-4567")
				.address("東京都千代田区1丁目")
				.afterAddress("1-1")
				.certificateIssueId("ISSUE001")
				.build();

		// 2.テスト対象メソッド実行
		boolean result;
		try {
			result = target.updateCertificate(certificateInsertForm);
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		// 3.テスト結果確認
		assertTrue(result);

		// 4.ログ確認
		log.info("証明書の変更テスト " + result);
	}

	@Test
	void 証明書の変更テストdeleteOneが失敗した場合() {
		// 0.モック設定
		doReturn(false).when(certificateService).deleteOne(anyString());

		List<CertificateDetailDataForm> certificateList = new ArrayList<>();
		CertificateDetailDataForm form1 = new CertificateDetailDataForm.Builder()
				.certificateId("1") // 証明書ID（0~3のいずれか）
				.certificateName("成績証明書")
				.count(2)
				.build();

		certificateList.add(form1);

		CertificateInsertForm certificateInsertForm = new CertificateInsertForm.Builder()
				.userId("user@example.com")
				.lastName("山田")
				.firstName("太郎")
				.lastNameKana("ヤマダ")
				.firstNameKana("タロウ")
				.certificateList(certificateList)
				.mediaName("郵送")
				.zipCode("123-4567")
				.address("東京都千代田区1丁目")
				.afterAddress("1-1")
				.certificateIssueId("ISSUE001")
				.build();

		// 2.テスト対象メソッド実行
		boolean result;
		try {
			result = target.updateCertificate(certificateInsertForm);
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		// 3.テスト結果確認
		assertFalse(result);

		// 4.ログ確認
		log.info("証明書の変更テストdeleteOneが失敗した場合 " + result);
	}

	@Test
	void 証明書料金と重量データを取得テスト() {
		// 0.モック設定
		MailingData mockMailingData = new MailingData();
		mockMailingData.setPostalFee(150);
		mockMailingData.setPostalMaxWeight(50);

		List<CertificateData> mockCertificateDatas = new ArrayList<>();
		CertificateData cert1 = new CertificateData();
		cert1.setCertificateId("CERT001");
		cert1.setCertificateQuantity(2);
		cert1.setCertificateName("卒業証明書");
		cert1.setCertificateFee(250);
		cert1.setCertificateWeight(10);

		mockCertificateDatas.add(cert1);

		doReturn(mockMailingData).when(certificateRepository).selectMailing();
		doReturn(mockCertificateDatas).when(certificateRepository).selectCertificateAll();

		// 2.テスト対象メソッド実行
		CertificateFeeAndWeightEntity resultEntiry = target.getCertificateFeeAndWeight();

		// 3.テスト結果確認
		CertificateFeeData result = resultEntiry.getCertificateList().get(0);
		assertEquals("CERT001", result.getCertificateId());
		assertEquals(10, result.getWeight());
		assertEquals(250, result.getFee());
		assertEquals(150, resultEntiry.getPostal().getPostalFee());
		assertEquals(50, resultEntiry.getPostal().getPostalMaxWeight());

		// 4.ログ確認
		log.info("証明書料金と重量データを取得テスト " + result);
	}

}
