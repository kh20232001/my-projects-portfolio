package com.api.domain.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import com.api.domain.models.dbdata.CertificateData;
import com.api.domain.models.dbdata.CertificateIssuanceDashBoardDetailData;
import com.api.domain.models.displaydata.CertificateDisplayDetail;
import com.api.domain.models.forms.CertificateStateUpdateForm;
import com.api.domain.repositories.CertificateIssuanceDashBoardRepository;
import com.api.domain.repositories.CertificateIssuanceRepository;
import com.api.domain.repositories.UserRepository;
import com.api.jobpal.common.base.DBDataConversion;

@RunWith(SpringRunner.class)
@SpringBootTest
class CertificateDashBoardDetailServiceTest {

	@Autowired
	private CertificateDashBoardDetailService target;

	// 下の階層をモック化
	@SpyBean
	private NamedParameterJdbcTemplate jdbc;

	@SpyBean
	private DBDataConversion dbdc;

	@Autowired
	private Logger log;

	@SpyBean
	private CertificateIssuanceDashBoardRepository dashBoardRepository;

	@SpyBean
	private CertificateIssuanceRepository issuanceRepository;

	@SpyBean
	private NotificationService notificationService;

	@SpyBean
	private UserRepository userRepository;

	@Test
	void testUpdateDashBoardDetail正常_証明書の詳細データ取得() {
		// モック設定
		List<CertificateData> mockCertificateList = new ArrayList<>();

		CertificateData mockDetail1 = new CertificateData();
		mockDetail1.setCertificateId("1");
		mockDetail1.setCertificateName("成績証明書");
		mockDetail1.setCertificateQuantity(2);
		mockDetail1.setCertificateFee(500);
		mockDetail1.setCertificateWeight(2000);

		mockCertificateList.add(mockDetail1);

		// メインの証明書発行ダッシュボードデータを設定
		CertificateIssuanceDashBoardDetailData mockDashBoardData = new CertificateIssuanceDashBoardDetailData();
		mockDashBoardData.setCertificateIssueId("ISSUE001");
		mockDashBoardData.setStudentUserId("user@example.com");
		mockDashBoardData.setDepartment("T");
		mockDashBoardData.setGrade("3");
		mockDashBoardData.setClassName("A2");
		mockDashBoardData.setAttendanceNumber(15);
		mockDashBoardData.setStudentUserName("山田 太郎");
		mockDashBoardData.setStudentId(20242222);
		mockDashBoardData.setStatus("6");
		mockDashBoardData.setOfficeUserId("OFFICE001");
		mockDashBoardData.setOfficeUserName("佐藤 花子");
		mockDashBoardData.setRecipientName("佐藤 三郎");
		mockDashBoardData.setRecipientFurigana("サトウ サブロウ");
		mockDashBoardData.setRecipientAddress("100-0001 東京都千代田区1丁目 2-3");
		mockDashBoardData.setMediaType("2");
		mockDashBoardData.setApplicationDate(Date.valueOf("2024-11-01"));
		mockDashBoardData.setApprovalDate(Date.valueOf("2024-11-10"));
		mockDashBoardData.setDeliveryDueDate(Date.valueOf("2024-11-20"));
		mockDashBoardData.setDeliveryDate(Date.valueOf("2024-11-21"));
		mockDashBoardData.setPostDate(Date.valueOf("2024-11-22"));
		mockDashBoardData.setPostalFee(500);
		mockDashBoardData.setPostalMaxWeight(2000);
		mockDashBoardData.setCertificateList(null);

		doReturn(mockDashBoardData).when(dashBoardRepository).selectCertificateIssuanceDashBoardDetail(anyString());
		doReturn(mockCertificateList).when(dashBoardRepository).selectCertificate(anyString());

		// テスト用のデータ設定
		String certificateIssueId = "ISSUE001";

		// 実行
		CertificateDisplayDetail result = target.getDashBoardDetail(certificateIssueId);

		// 検証
		assertEquals("ISSUE001", result.getCertificateIssueId());
		assertEquals("user@example.com", result.getUserId());
		assertEquals("T3A2", result.getUserClass());
		assertEquals(15, result.getClassNumber());
		assertEquals("山田 太郎", result.getUserName());
		assertEquals(20242222, result.getSchoolClassNumber());
		assertEquals("完了", result.getCertificateStateName());
		assertEquals("佐藤 花子", result.getOfficeUserName());
		assertEquals("佐藤", result.getLastName());
		assertEquals("三郎", result.getFirstName());
		assertEquals("サトウ", result.getLastNameKana());
		assertEquals("サブロウ", result.getFirstNameKana());
		assertEquals("郵送", result.getMediaName());
		assertEquals("100-0001", result.getZipCode());
		assertEquals("東京都千代田区1丁目", result.getAddress());
		assertEquals("2-3", result.getAfterAddress());
		assertEquals("0", result.getCertificateList().get(0).getCertificateId());
		assertEquals("在学", result.getCertificateList().get(0).getCertificateName());
		assertEquals(0, result.getCertificateList().get(0).getCount());
		assertEquals("1", result.getCertificateList().get(1).getCertificateId());
		assertEquals("成績", result.getCertificateList().get(1).getCertificateName());
		assertEquals(2, result.getCertificateList().get(1).getCount());
		assertEquals("2", result.getCertificateList().get(2).getCertificateId());
		assertEquals("卒業", result.getCertificateList().get(2).getCertificateName());
		assertEquals(0, result.getCertificateList().get(2).getCount());
		assertEquals("3", result.getCertificateList().get(3).getCertificateId());
		assertEquals("健康", result.getCertificateList().get(3).getCertificateName());
		assertEquals(0, result.getCertificateList().get(3).getCount());

		log.info("testUpdateDashBoardDetail正常_承認テスト成功: result={}", result);
	}

	@Test
	void testUpdateDashBoardDetail正常_ユーザデータがない場合の証明書の詳細データ取得() {
		// モック設定
		List<CertificateData> mockCertificateList = new ArrayList<>();

		CertificateData mockDetail1 = new CertificateData();
		mockDetail1.setCertificateId("1");
		mockDetail1.setCertificateName("成績証明書");
		mockDetail1.setCertificateQuantity(2);
		mockDetail1.setCertificateFee(500);
		mockDetail1.setCertificateWeight(2000);

		mockCertificateList.add(mockDetail1);

		// メインの証明書発行ダッシュボードデータを設定
		CertificateIssuanceDashBoardDetailData mockDashBoardData = new CertificateIssuanceDashBoardDetailData();
		mockDashBoardData.setCertificateIssueId("ISSUE001");
		mockDashBoardData.setStudentUserId(null);
		mockDashBoardData.setDepartment(null);
		mockDashBoardData.setGrade(null);
		mockDashBoardData.setClassName(null);
		mockDashBoardData.setAttendanceNumber(null);
		mockDashBoardData.setStudentUserName(null);
		mockDashBoardData.setStudentId(null);
		mockDashBoardData.setStatus("6");
		mockDashBoardData.setOfficeUserId("OFFICE001");
		mockDashBoardData.setOfficeUserName("佐藤 花子");
		mockDashBoardData.setRecipientName("佐藤 三郎");
		mockDashBoardData.setRecipientFurigana("サトウ サブロウ");
		mockDashBoardData.setRecipientAddress("100-0001 東京都千代田区1丁目 2-3");
		mockDashBoardData.setMediaType("2");
		mockDashBoardData.setApplicationDate(Date.valueOf("2024-11-01"));
		mockDashBoardData.setApprovalDate(Date.valueOf("2024-11-10"));
		mockDashBoardData.setDeliveryDueDate(Date.valueOf("2024-11-20"));
		mockDashBoardData.setDeliveryDate(Date.valueOf("2024-11-21"));
		mockDashBoardData.setPostDate(Date.valueOf("2024-11-22"));
		mockDashBoardData.setPostalFee(500);
		mockDashBoardData.setPostalMaxWeight(2000);
		mockDashBoardData.setCertificateList(null);

		doReturn(mockDashBoardData).when(dashBoardRepository).selectCertificateIssuanceDashBoardDetail(anyString());
		doReturn(mockCertificateList).when(dashBoardRepository).selectCertificate(anyString());

		// テスト用のデータ設定
		String certificateIssueId = "ISSUE001";

		// 実行
		CertificateDisplayDetail result = target.getDashBoardDetail(certificateIssueId);

		// 検証
		assertEquals("ISSUE001", result.getCertificateIssueId());
		assertEquals(null, result.getUserId());
		assertEquals(null, result.getUserClass());
		assertEquals(null, result.getClassNumber());
		assertEquals(null, result.getUserName());
		assertEquals(null, result.getSchoolClassNumber());
		assertEquals("完了", result.getCertificateStateName());
		assertEquals("佐藤 花子", result.getOfficeUserName());
		assertEquals("佐藤", result.getLastName());
		assertEquals("三郎", result.getFirstName());
		assertEquals("サトウ", result.getLastNameKana());
		assertEquals("サブロウ", result.getFirstNameKana());
		assertEquals("郵送", result.getMediaName());
		assertEquals("100-0001", result.getZipCode());
		assertEquals("東京都千代田区1丁目", result.getAddress());
		assertEquals("2-3", result.getAfterAddress());
		assertEquals("0", result.getCertificateList().get(0).getCertificateId());
		assertEquals("在学", result.getCertificateList().get(0).getCertificateName());
		assertEquals(0, result.getCertificateList().get(0).getCount());
		assertEquals("1", result.getCertificateList().get(1).getCertificateId());
		assertEquals("成績", result.getCertificateList().get(1).getCertificateName());
		assertEquals(2, result.getCertificateList().get(1).getCount());
		assertEquals("2", result.getCertificateList().get(2).getCertificateId());
		assertEquals("卒業", result.getCertificateList().get(2).getCertificateName());
		assertEquals(0, result.getCertificateList().get(2).getCount());
		assertEquals("3", result.getCertificateList().get(3).getCertificateId());
		assertEquals("健康", result.getCertificateList().get(3).getCertificateName());
		assertEquals(0, result.getCertificateList().get(3).getCount());

		log.info("testUpdateDashBoardDetail正常_承認テスト成功: result={}", result);
	}

	@Test
	void testUpdateDashBoardDetail正常_承認() {
		// モック設定
		CertificateStateUpdateForm form = new CertificateStateUpdateForm();
		form.setCertificateIssueId("CI001");
		form.setButtonId(0); // 承認ボタン
		form.setCertificateStateName("原紙");

		List<String> mockOfficeUserList = new ArrayList<>();
		mockOfficeUserList.add("office@example.com");

		doReturn(Map.of("student", "S001", "office", "O001"))
				.when(issuanceRepository).selectCertificateIssueUserId(anyString());
		doReturn(true).when(issuanceRepository).updateCertificateIssuanceStatusOne(anyString(), anyString());
		doReturn(true).when(notificationService).insertCertificateNotification(anyString(), anyString());
		doReturn(mockOfficeUserList).when(userRepository).selectOfficeUserIdList();
		doReturn(true).when(notificationService).insertCertificateNotification(anyString(), anyString(), anyBoolean());
		// 実行
		boolean result = target.updateDashBoardDetail(form);

		// 検証
		assertTrue(result);

		log.info("testUpdateDashBoardDetail正常_承認テスト成功: result={}", result);
	}

	@Test
	void testUpdateDashBoardDetail異常_学生への通知データの追加でエラー() {
		// モック設定
		CertificateStateUpdateForm form = new CertificateStateUpdateForm();
		form.setCertificateIssueId("CI001");
		form.setButtonId(0); // 承認ボタン
		form.setCertificateStateName("原紙");

		List<String> mockOfficeUserList = new ArrayList<>();
		mockOfficeUserList.add("office@example.com");

		doReturn(Map.of("student", "S001", "office", "O001"))
				.when(issuanceRepository).selectCertificateIssueUserId(anyString());
		doReturn(true).when(issuanceRepository).updateCertificateIssuanceStatusOne(anyString(), anyString());
		doReturn(false).when(notificationService).insertCertificateNotification(anyString(), anyString());
		doReturn(mockOfficeUserList).when(userRepository).selectOfficeUserIdList();
		doReturn(true).when(notificationService).insertCertificateNotification(anyString(), anyString(), anyBoolean());
		// 実行
		boolean result = target.updateDashBoardDetail(form);

		// 検証
		assertFalse(result);

		log.info("testUpdateDashBoardDetail異常_学生への通知データの追加でエラー: result={}", result);
	}

	@Test
	void testUpdateDashBoardDetail異常_全ての事務への通知データの追加() {
		// モック設定
		CertificateStateUpdateForm form = new CertificateStateUpdateForm();
		form.setCertificateIssueId("CI001");
		form.setButtonId(0); // 承認ボタン
		form.setCertificateStateName("原紙");

		List<String> mockOfficeUserList = new ArrayList<>();
		mockOfficeUserList.add("office@example.com");

		doReturn(Map.of("student", "S001", "office", "O001"))
				.when(issuanceRepository).selectCertificateIssueUserId(anyString());
		doReturn(true).when(issuanceRepository).updateCertificateIssuanceStatusOne(anyString(), anyString());
		doReturn(true).when(notificationService).insertCertificateNotification(anyString(), anyString());
		doReturn(mockOfficeUserList).when(userRepository).selectOfficeUserIdList();
		doReturn(false).when(notificationService).insertCertificateNotification(anyString(), anyString(), anyBoolean());
		// 実行
		boolean result = target.updateDashBoardDetail(form);

		// 検証
		assertFalse(result);

		log.info("testUpdateDashBoardDetail異常_全ての事務への通知データの追加: result={}", result);
	}

	@Test
	void testUpdateDashBoardDetail正常_発行_原紙() {
		// モック設定
		CertificateStateUpdateForm form = new CertificateStateUpdateForm.Builder()
				.userId("test@example.com")
				.certificateIssueId("CI001")
				.certificateStateName("発行待ち")
				.buttonId(4) // 発行ボタン
				.mediaName("原紙")
				.build();

		doReturn(Map.of("student", "S001", "office", "O001"))
				.when(issuanceRepository).selectCertificateIssueUserId(anyString());
		doReturn(true).when(issuanceRepository).updateCertificateIssuanceStatusOne(anyString(), anyString());
		doReturn(true).when(notificationService).insertCertificateNotification(anyString(), anyString());

		// 実行
		boolean result = target.updateDashBoardDetail(form);

		// 検証
		assertTrue(result);

		log.info("testUpdateDashBoardDetail正常_発行_原紙テスト成功: result={}", result);
	}

	@Test
	void testUpdateDashBoardDetail正常_発行_郵送() {
		// モック設定
		CertificateStateUpdateForm form = new CertificateStateUpdateForm.Builder()
				.userId("test@example.com")
				.certificateIssueId("CI001")
				.certificateStateName("発行待ち")
				.buttonId(4) // 発行ボタン
				.mediaName("郵送")
				.build();

		doReturn(Map.of("student", "S001", "office", "O001"))
				.when(issuanceRepository).selectCertificateIssueUserId(anyString());
		doReturn(true).when(issuanceRepository).updateCertificateIssuanceStatusOne(anyString(), anyString());
		doReturn(true).when(notificationService).insertCertificateNotification(anyString(), anyString());

		// 実行
		boolean result = target.updateDashBoardDetail(form);

		// 検証
		assertTrue(result);

		log.info("testUpdateDashBoardDetail正常_発行_郵送テスト成功: result={}", result);
	}

	@Test
	void testUpdateDashBoardDetail正常_完了() {
		// モック設定
		CertificateStateUpdateForm form = new CertificateStateUpdateForm.Builder()
				.userId("test@example.com")
				.certificateIssueId("CI001")
				.certificateStateName("受取待ち")
				.buttonId(6) // 発行ボタン
				.mediaName("原紙")
				.build();

		doReturn(Map.of("student", "S001", "office", "O001"))
				.when(issuanceRepository).selectCertificateIssueUserId(anyString());
		doReturn(true).when(issuanceRepository).updateCertificateIssuanceStatusOne(anyString(), anyString());
		doReturn(true).when(notificationService).deleteCertificateNotification(anyString());

		// 実行
		boolean result = target.updateDashBoardDetail(form);

		// 検証
		assertTrue(result);
		log.info("testUpdateDashBoardDetail正常_完了テスト成功: result={}", result);
	}

	@Test
	void testUpdateDashBoardDetail異常_状態更新失敗() {
		// モック設定
		CertificateStateUpdateForm form = new CertificateStateUpdateForm.Builder()
				.userId("test@example.com")
				.certificateIssueId("CI001")
				.certificateStateName("担任承認待ち")
				.buttonId(0) // 承認ボタン
				.mediaName("原紙")
				.build();

		doReturn(Map.of("student", "S001", "office", "O001"))
				.when(issuanceRepository).selectCertificateIssueUserId(anyString());
		doReturn(false).when(issuanceRepository).updateCertificateIssuanceStatusOne(anyString(), anyString());

		// 実行
		boolean result = target.updateDashBoardDetail(form);

		// 検証
		assertFalse(result);

		log.info("testUpdateDashBoardDetail異常_状態更新失敗: result={}", result);
	}

	@Test
	void testUpdateDashBoardDetail異常_通知挿入失敗() {
		// モック設定
		CertificateStateUpdateForm form = new CertificateStateUpdateForm.Builder()
				.userId("test@example.com")
				.certificateIssueId("CI001")
				.certificateStateName("発行待ち")
				.buttonId(4) // 発行ボタン
				.mediaName("郵送")
				.build();

		doReturn(Map.of("student", "S001", "office", "O001"))
				.when(issuanceRepository).selectCertificateIssueUserId(anyString());
		doReturn(true).when(issuanceRepository).updateCertificateIssuanceStatusOne(anyString(), anyString());
		doReturn(false).when(notificationService).insertCertificateNotification(anyString(), anyString());

		// 実行
		boolean result = target.updateDashBoardDetail(form);

		// 検証
		assertFalse(result);

		log.info("testUpdateDashBoardDetail異常_通知挿入失敗: result={}", result);
	}

	@Test
	void testUpdateDashBoardDetail異常_無効なButtonId() {
		// モック設定
		CertificateStateUpdateForm form = new CertificateStateUpdateForm();
		form.setCertificateIssueId("CI001");
		form.setButtonId(99); // 無効なButtonId

		// 実行と検証
		IllegalArgumentException exception = assertThrows(
				IllegalArgumentException.class,
				() -> target.updateDashBoardDetail(form));
		assertEquals("Invalid buttonId: 99", exception.getMessage());
		log.info("testUpdateDashBoardDetail異常_無効なButtonIdテスト成功: result={}", exception.getMessage());

	}

	@Test
	void testUpdateDashBoardDetail異常_無効なMediaName() {
		// モック設定
		CertificateStateUpdateForm form = new CertificateStateUpdateForm();
		form.setCertificateIssueId("CI001");
		form.setButtonId(4); // 発行ボタン
		form.setMediaName("無効なメディア名"); // 無効なMediaName

		doReturn(Map.of("student", "S001", "office", "O001"))
				.when(issuanceRepository).selectCertificateIssueUserId(anyString());

		// 実行と検証
		IllegalArgumentException exception = assertThrows(
				IllegalArgumentException.class,
				() -> target.updateDashBoardDetail(form));
		assertEquals("Invalid mediaName: 無効なメディア名", exception.getMessage());
		log.info("testUpdateDashBoardDetail異常_無効なMediaName: result={}", exception.getMessage());

	}

	@Test
	void testUpdateDashBoardDetail異常_通知データ削除でIncorrectResultSizeDataAccessException() {
		// モック設定
		CertificateStateUpdateForm form = new CertificateStateUpdateForm();
		form.setCertificateIssueId("CI001");
		form.setButtonId(6); // 完了ボタン

		doReturn(Map.of("student", "S001", "office", "O001"))
				.when(issuanceRepository).selectCertificateIssueUserId(anyString());
		doReturn(true).when(issuanceRepository).updateCertificateIssuanceStatusOne(anyString(), anyString());
		doThrow(new IncorrectResultSizeDataAccessException(1)).when(notificationService)
				.deleteCertificateNotification(anyString());

		// 実行
		boolean result = target.updateDashBoardDetail(form);

		// 検証
		assertFalse(result);
		log.info("testUpdateDashBoardDetail異常_通知データ削除でIncorrectResultSizeDataAccessException: result={}", result);

	}

	@Test
	void testUpdateDashBoardDetail異常_その他の例外発生() {
		// モック設定
		CertificateStateUpdateForm form = new CertificateStateUpdateForm();
		form.setCertificateIssueId("CI001");
		form.setButtonId(0); // 承認ボタン

		doReturn(Map.of("student", "S001", "office", "O001"))
				.when(issuanceRepository).selectCertificateIssueUserId(anyString());
		doThrow(new RuntimeException("予期しないエラー")).when(issuanceRepository)
				.updateCertificateIssuanceStatusOne(anyString(), anyString());

		// 実行
		boolean result = target.updateDashBoardDetail(form);

		// 検証
		assertFalse(result);
		log.info("testUpdateDashBoardDetail異常_その他の例外発生: result={}", result);

	}

	@Test
	void testUpdateDashBoardDetail正常_取り下げまたは差戻し() {
		// モック設定
		CertificateStateUpdateForm form = new CertificateStateUpdateForm.Builder()
				.userId("test@example.com")
				.certificateIssueId("CI001")
				.certificateStateName("担任承認待ち")
				.buttonId(1) // 取り下げボタン
				.mediaName("原紙")
				.build();

		doReturn(Map.of("student", "S001", "office", "O001"))
				.when(issuanceRepository).selectCertificateIssueUserId(anyString());
		doReturn(true).when(issuanceRepository).updateCertificateIssuanceStatusOne(anyString(), anyString());
		doReturn(true).when(notificationService).insertCertificateNotification(anyString(), anyString());

		// 実行
		boolean result = target.updateDashBoardDetail(form);
		// 検証
		assertTrue(result);
		log.info("testUpdateDashBoardDetail正常_取り下げまたは差戻し: result={}", result);
	}

	@Test
	void testUpdateDashBoardDetail正常_受領() {
		// モック設定
		CertificateStateUpdateForm form = new CertificateStateUpdateForm.Builder()
				.userId("office@example.com")
				.certificateIssueId("CI001")
				.certificateStateName("支払待ち")
				.buttonId(3) // 受領ボタン
				.mediaName("原紙")
				.build();

		List<String> mockOfficeUserList = new ArrayList<>();
		mockOfficeUserList.add("office@example.com");

		doReturn(Map.of("student", "S001", "office", "O001"))
				.when(issuanceRepository).selectCertificateIssueUserId(anyString());
		doReturn(true).when(issuanceRepository).updateCertificateIssuanceOfficeUserIdOne(anyString(), anyString());
		doReturn(true).when(issuanceRepository).updateCertificateIssuanceStatusOne(anyString(), anyString());
		doReturn(true).when(notificationService).insertCertificateNotification(anyString(), anyString());

		// 実行
		boolean result = target.updateDashBoardDetail(form);

		// 検証
		assertTrue(result);
		log.info("testUpdateDashBoardDetail正常_受領: result={}", result);
	}

	@Test
	void testUpdateDashBoardDetail異常_担当の事務を登録時にエラー() {
		// モック設定
		CertificateStateUpdateForm form = new CertificateStateUpdateForm.Builder()
				.userId("office@example.com")
				.certificateIssueId("CI001")
				.certificateStateName("支払待ち")
				.buttonId(3) // 受領ボタン
				.mediaName("原紙")
				.build();

		List<String> mockOfficeUserList = new ArrayList<>();
		mockOfficeUserList.add("office@example.com");

		doReturn(Map.of("student", "S001", "office", "O001"))
				.when(issuanceRepository).selectCertificateIssueUserId(anyString());
		doReturn(false).when(issuanceRepository).updateCertificateIssuanceOfficeUserIdOne(anyString(), anyString());

		// 実行
		boolean result = target.updateDashBoardDetail(form);

		// 検証
		assertFalse(result);
		log.info("testUpdateDashBoardDetail異常_担当の事務を登録時にエラー: result={}", result);
	}

	@Test
	void testUpdateDashBoardDetail正常_送信() {
		// モック設定
		CertificateStateUpdateForm form = new CertificateStateUpdateForm.Builder()
				.userId("office@example.com")
				.certificateIssueId("CI001")
				.certificateStateName("発行済み")
				.buttonId(5) // 送信ボタン
				.mediaName("電子")
				.build();

		doReturn(Map.of("student", "S001", "office", "O001"))
				.when(issuanceRepository).selectCertificateIssueUserId(anyString());
		doReturn(true).when(issuanceRepository).updateCertificateIssuanceStatusOne(anyString(), anyString());
		doReturn(true).when(notificationService).insertCertificateNotification(anyString(), anyString());

		// 実行
		boolean result = target.updateDashBoardDetail(form);

		// 検証
		assertTrue(result);
		log.info("testUpdateDashBoardDetail正常_送信: result={}", result);
	}

	@Test
	void testUpdateDashBoardDetail異常_事務への通知データの追加でエラー() {
		// モック設定
		CertificateStateUpdateForm form = new CertificateStateUpdateForm.Builder()
				.userId("office@example.com")
				.certificateIssueId("CI001")
				.certificateStateName("発行済み")
				.buttonId(5) // 送信ボタン
				.mediaName("電子")
				.build();

		doReturn(Map.of("student", "S001", "office", "O001"))
				.when(issuanceRepository).selectCertificateIssueUserId(anyString());
		doReturn(true).when(issuanceRepository).updateCertificateIssuanceStatusOne(anyString(), anyString());
		doReturn(false).when(notificationService).insertCertificateNotification(anyString(), anyString());

		// 実行
		boolean result = target.updateDashBoardDetail(form);

		// 検証
		assertFalse(result);
		log.info("testUpdateDashBoardDetail異常_事務への通知データの追加でエラー: result={}", result);
	}

	@Test
	void testUpdateDashBoardDetail正常_削除() {
		// モック設定
		doReturn(true).when(notificationService).deleteCertificateNotification(anyString());
		doReturn(true).when(issuanceRepository).deleteCertificateIssuanceOne(anyString());

		// テスト用のデータ設定
		String certificateIssueId = "CI001";

		// 実行
		boolean result = target.deleteOne(certificateIssueId);

		// 検証
		assertTrue(result);
		log.info("testUpdateDashBoardDetail正常_削除: result={}", result);
	}

	@Test
	void testUpdateDashBoardDetail異常_通知データの削除失敗() {
		// モック設定
		doReturn(false).when(notificationService).deleteCertificateNotification(anyString());
		doReturn(true).when(issuanceRepository).deleteCertificateIssuanceOne(anyString());

		// テスト用のデータ設定
		String certificateIssueId = "CI001";

		// 実行
		boolean result = target.deleteOne(certificateIssueId);

		// 検証
		assertFalse(result);
		log.info("testUpdateDashBoardDetail異常_通知データの削除失敗: result={}", result);
	}

	@Test
	void testUpdateDashBoardDetail異常_通知データの削除でエラー() {
		// モック設定
		doThrow(new IncorrectResultSizeDataAccessException(1)).when(notificationService)
				.deleteCertificateNotification(anyString());
		doReturn(true).when(issuanceRepository).deleteCertificateIssuanceOne(anyString());

		// テスト用のデータ設定
		String certificateIssueId = "CI001";

		// 実行
		boolean result = target.deleteOne(certificateIssueId);

		// 検証
		assertFalse(result);
		log.info("testUpdateDashBoardDetail異常_通知データの削除でエラー: result={}", result);
	}

	@Test
	void testUpdateDashBoardDetail異常_申請データの削除失敗() {
		// モック設定
		doReturn(true).when(notificationService).deleteCertificateNotification(anyString());
		doReturn(false).when(issuanceRepository).deleteCertificateIssuanceOne(anyString());

		// テスト用のデータ設定
		String certificateIssueId = "CI001";

		// 実行
		boolean result = target.deleteOne(certificateIssueId);

		// 検証
		assertFalse(result);
		log.info("testUpdateDashBoardDetail異常_申請データの削除失敗: result={}", result);
	}

}
