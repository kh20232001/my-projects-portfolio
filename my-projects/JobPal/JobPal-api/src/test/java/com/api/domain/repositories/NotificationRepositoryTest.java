package com.api.domain.repositories;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import com.api.domain.models.dbdata.MailingData;
import com.api.domain.models.dbdata.NotificationDashBoardData;
import com.api.domain.models.dbdata.NotificationTeacherData;
import com.api.jobpal.common.base.DBDataConversion;

@RunWith(SpringRunner.class)
@SpringBootTest
class NotificationRepositoryTest {
	@Autowired
	private NotificationRepository repository;

	@SpyBean
	private CertificateIssuanceRepository cir;

	@SpyBean
	private NamedParameterJdbcTemplate jdbc;

	@SpyBean
	private DBDataConversion dbdc;
	@SpyBean
	private CertificateIssuanceDashBoardRepository CIDBR;
	@Autowired
	private Logger log;

	@Test
	public void testGetNotificationCount正常() {
		doReturn(5).when(jdbc).queryForObject(anyString(), anyMap(), eq(Integer.class));

		Integer result = repository.getNotificationCount("USER001");

		assertEquals(5, result);
		log.info("通知件数取得テスト成功: result={}", result);
	}

	@Test
	public void testGetNotificationCountゼロ件() {
		doReturn(0).when(jdbc).queryForObject(anyString(), anyMap(), eq(Integer.class));

		Integer result = repository.getNotificationCount("USER001");

		assertEquals(0, result);
		log.info("通知件数取得テスト成功（ゼロ件）: result={}", result);
	}

	@Test
	public void testGetNotificationJobSearchList正常() {
		List<Map<String, Object>> mockResult = new ArrayList<>();
		Map<String, Object> data = Map.of("notification_id", "N001", "job_search_id", "JS001", "resend_flag", true);
		mockResult.add(data);

		doReturn(mockResult).when(jdbc).queryForList(anyString(), anyMap());

		List<NotificationDashBoardData> result = repository.getNotificationJobSearchList("USER001");

		assertEquals(1, result.size());
		assertEquals("N001", result.get(0).getNotifiationId());
		assertTrue(result.get(0).getResendFlag());
		log.info("通知就職活動リスト取得テスト成功: result={}", result);
	}

	@Test
	public void testGetNotificationJobSearchListデータなし() {
		doReturn(new ArrayList<>()).when(jdbc).queryForList(anyString(), anyMap());

		List<NotificationDashBoardData> result = repository.getNotificationJobSearchList("USER001");

		assertTrue(result.isEmpty());
		log.info("通知就職活動リスト取得テスト成功（データなし）: result={}", result);
	}

	@Test
	public void testInsertNotification正常() {
		doReturn(1).when(jdbc).update(anyString(), anyMap());

		Boolean result = repository.insertNotification("N001", "USER001", true, "CATEGORY");

		assertTrue(result);
		log.info("通知作成テスト成功: result={}", result);
	}

	@Test
	public void testInsertNotification失敗() {
		doReturn(0).when(jdbc).update(anyString(), anyMap());

		Boolean result = repository.insertNotification("N001", "USER001", true, "CATEGORY");

		assertFalse(result);
		log.info("通知作成テスト失敗: result={}", result);
	}

	@Test
	public void testDeleteNotificationJobSearchUser正常() {
		doReturn(1).when(jdbc).update(anyString(), anyMap());

		Boolean result = repository.deleteNotificationJobSearchUser("USER001", "JS001");

		assertTrue(result);
		log.info("就職活動通知削除テスト成功: result={}", result);
	}

	@Test
	public void testDeleteNotificationJobSearchUser失敗() {
		doReturn(0).when(jdbc).update(anyString(), anyMap());

		Boolean result = repository.deleteNotificationJobSearchUser("USER001", "JS001");

		assertFalse(result);
		log.info("就職活動通知削除テスト失敗: result={}", result);
	}

	@Test
	public void testUpdateNotificationJobSearch正常() {
		doReturn(1).when(jdbc).update(anyString(), anyMap());

		Boolean result = repository.updateNotificationJobSearch("JS001");

		assertTrue(result);
		log.info("就職活動通知更新テスト成功: result={}", result);
	}

	@Test
	public void testUpdateNotificationJobSearch失敗() {
		doReturn(0).when(jdbc).update(anyString(), anyMap());

		Boolean result = repository.updateNotificationJobSearch("JS001");

		assertFalse(result);
		log.info("就職活動通知更新テスト失敗: result={}", result);
	}

	@Test
	public void testDeleteNotificationCertificateIssuance正常() {
		doReturn(1).when(jdbc).update(anyString(), anyMap());

		Boolean result = repository.deleteNotificationCertificateIssuance("CI001");

		assertTrue(result);
		log.info("証明書発行通知削除テスト成功: result={}", result);
	}

	@Test
	public void testDeleteNotificationCertificateIssuance失敗() {
		doReturn(0).when(jdbc).update(anyString(), anyMap());

		Boolean result = repository.deleteNotificationCertificateIssuance("CI001");

		assertFalse(result);
		log.info("証明書発行通知削除テスト失敗: result={}", result);
	}

	@Test
	public void testGetNotificationCertificateIssuanceList正常郵送() {
		// モックデータの設定
		List<Map<String, Object>> mockResultList = new ArrayList<>();
		// map (ダミーデータ)
		Map<String, Object> mockData = Map.ofEntries(
				Map.entry("certificate_issue_id", "CI001"),
				Map.entry("certificate_quantity", 2),
				Map.entry("certificate_weight", 10),
				Map.entry("total_fee", 200),
				Map.entry("student_user_id", "S001"),
				Map.entry("student_user_name", "山田太郎"),
				Map.entry("media_type", "2"), // 郵送タイプ
				Map.entry("application_date", new Date(System.currentTimeMillis())),
				Map.entry("approval_date", new Date(System.currentTimeMillis())),
				Map.entry("delivery_due_date", new Date(System.currentTimeMillis())),
				Map.entry("delivery_date", new Date(System.currentTimeMillis())),
				Map.entry("post_date", new Date(System.currentTimeMillis())),
				Map.entry("postal_fee", 500),
				Map.entry("postal_max_weight", 20),
				Map.entry("department", "情報システム学科"),
				Map.entry("grade", 2),
				Map.entry("class_name", "A"),
				Map.entry("attendance_number", 15),
				Map.entry("status", "1"),
				Map.entry("certificate_id", "C001"),
				Map.entry("certificate_name", "卒業証明書"),
				Map.entry("notification_id", "N001"),
				Map.entry("recipient_name", "受取人 太郎"),
				Map.entry("recipient_furigana", "ウケトリタロウ"),
				Map.entry("recipient_address", "東京都新宿区西新宿"),
				Map.entry("notification_timestamp", new Timestamp(System.currentTimeMillis())));

		// queryList (カラム名)
		String[] queryList = {
				"certificate_id",
				"certificate_quantity",
				"certificate_weight",
				"total_fee",
				"student_user_name",
				"media_type",
				"application_date",
				"approval_date",
				"delivery_due_date",
				"delivery_date",
				"post_date",
				"postal_fee",
				"postal_max_weight",
				"department",
				"grade",
				"class_name",
				"attendance_number",
				"status",
				"certificate_issue_id",
				"certificate_name",
				"notification_id",
				"student_user_id",
				"recipient_name",
				"recipient_furigana",
				"notification_timestamp",
				"recipient_address"
		};

		mockResultList.add(mockData);
		mockResultList.add(mockData);

		MailingData mockMailingData = new MailingData();
		mockMailingData.setPostalFee(500);
		mockMailingData.setPostalMaxWeight(20);
		CertificateIssuanceDashBoardData dummyData = new CertificateIssuanceDashBoardData();
		dummyData.setTotalWeight(10);
		dummyData.setTotalAmount(100);
		dummyData.setCertificateList(new ArrayList<>());
		// モックの振る舞いを定義
		doReturn(mockResultList).when(jdbc).queryForList(anyString(), anyMap());
		doReturn(queryList).when(dbdc).extractColumnNames(anyString());
		doReturn(mockMailingData).when(cir).selectMailing();
		doReturn(new CertificateData()).when(CIDBR).packageCertificateDashBoard(anyMap());
		doReturn(dummyData).when(CIDBR)
				.packageCertificateIssuanceDashBoard(anyMap(), any(String[].class));

		// テスト実行
		List<NotificationDashBoardData> result = repository.getNotificationCertificateIssuanceList("user001");

		// 検証
		assertNotNull(result);
		assertEquals(1, result.size());

		log.info("testGetNotificationCertificateIssuanceList正常: result={}", result);
	}

	@Test
	public void testGetNotificationCertificateIssuanceList正常郵送以外() {
		// モックデータの設定
		List<Map<String, Object>> mockResultList = new ArrayList<>();
		// map (ダミーデータ)
		Map<String, Object> mockData = Map.ofEntries(
				Map.entry("certificate_issue_id", "CI001"),
				Map.entry("certificate_quantity", 2),
				Map.entry("certificate_weight", 10),
				Map.entry("total_fee", 200),
				Map.entry("student_user_id", "S001"),
				Map.entry("student_user_name", "山田太郎"),
				Map.entry("media_type", "2"), // 郵送タイプ
				Map.entry("application_date", new Date(System.currentTimeMillis())),
				Map.entry("approval_date", new Date(System.currentTimeMillis())),
				Map.entry("delivery_due_date", new Date(System.currentTimeMillis())),
				Map.entry("delivery_date", new Date(System.currentTimeMillis())),
				Map.entry("post_date", new Date(System.currentTimeMillis())),
				Map.entry("postal_fee", 500),
				Map.entry("postal_max_weight", 20),
				Map.entry("department", "情報システム学科"),
				Map.entry("grade", 2),
				Map.entry("class_name", "A"),
				Map.entry("attendance_number", 15),
				Map.entry("status", "1"),
				Map.entry("certificate_id", "C001"),
				Map.entry("certificate_name", "卒業証明書"),
				Map.entry("notification_id", "N001"),
				Map.entry("recipient_name", "受取人 太郎"),
				Map.entry("recipient_furigana", "ウケトリタロウ"),
				Map.entry("recipient_address", "東京都新宿区西新宿"),
				Map.entry("notification_timestamp", new Timestamp(System.currentTimeMillis())));

		// queryList (カラム名)
		String[] queryList = {
				"certificate_id",
				"certificate_quantity",
				"certificate_weight",
				"total_fee",
				"student_user_name",
				"media_type",
				"application_date",
				"approval_date",
				"delivery_due_date",
				"delivery_date",
				"post_date",
				"postal_fee",
				"postal_max_weight",
				"department",
				"grade",
				"class_name",
				"attendance_number",
				"status",
				"certificate_issue_id",
				"certificate_name",
				"notification_id",
				"student_user_id",
				"recipient_name",
				"recipient_furigana",
				"notification_timestamp",
				"recipient_address"
		};
		mockResultList.add(mockData);
		mockResultList.add(mockData);

		MailingData mockMailingData = new MailingData();
		mockMailingData.setPostalFee(500);
		mockMailingData.setPostalMaxWeight(20);
		CertificateIssuanceDashBoardData dummyData = new CertificateIssuanceDashBoardData();
		dummyData.setTotalWeight(10);
		dummyData.setTotalAmount(100);
		dummyData.setCertificateList(new ArrayList<>());
		// モックの振る舞いを定義
		doReturn(mockResultList).when(jdbc).queryForList(anyString(), anyMap());
		doReturn(queryList).when(dbdc).extractColumnNames(anyString());
		doReturn(mockMailingData).when(cir).selectMailing();
		doReturn(new CertificateData()).when(CIDBR).packageCertificateDashBoard(anyMap());
		doReturn(dummyData).when(CIDBR)
				.packageCertificateIssuanceDashBoard(anyMap(), any(String[].class));

		// テスト実行
		List<NotificationDashBoardData> result = repository.getNotificationCertificateIssuanceList("user001");

		// 検証
		assertNotNull(result);
		assertEquals(1, result.size());

		log.info("testGetNotificationCertificateIssuanceList正常: result={}", result);
	}

	@Test
	public void testGetNotificationCertificateIssuanceListデータなし() {
		// モックで空リストを返す設定
		doReturn(new ArrayList<>()).when(jdbc).queryForList(anyString(), anyMap());
		doReturn(new MailingData()).when(cir).selectMailing();

		// テスト実行
		List<NotificationDashBoardData> result = repository.getNotificationCertificateIssuanceList("user001");

		// 検証
		assertNotNull(result);
		assertTrue(result.isEmpty());

		log.info("testGetNotificationCertificateIssuanceListデータなし: result={}", result);
	}

	@Test
	public void testGetNotificationClassTeacher正常() {
		List<Map<String, Object>> mockResult = new ArrayList<>();
		Map<String, Object> data = Map.of("assigned_teacher_user_id", "TEACHER001");
		mockResult.add(data);

		doReturn(mockResult).when(jdbc).queryForList(anyString(), anyMap());

		List<String> result = repository.getNotificationClassTeacher("DEPT001", 3, "CLASS001");

		assertEquals(1, result.size());
		assertEquals("TEACHER001", result.get(0));
		log.info("担任通知対象クラス取得テスト成功: result={}", result);
	}

	@Test
	public void testGetNotificationClassTeacherデータなし() {
		doReturn(new ArrayList<>()).when(jdbc).queryForList(anyString(), anyMap());

		List<String> result = repository.getNotificationClassTeacher("DEPT001", 3, "CLASS001");

		assertTrue(result.isEmpty());
		log.info("担任通知対象クラス取得テスト成功（データなし）: result={}", result);
	}

	@Test
	public void testCheckNotificationClassExists存在する() {
		doReturn(1).when(jdbc).queryForObject(anyString(), anyMap(), eq(Integer.class));

		NotificationTeacherData data = new NotificationTeacherData();
		data.setDepartment("DEPT001");
		data.setGrade(3);
		data.setClassName("CLASS001");
		data.setTeacherUserId("TEACHER001");

		Boolean result = repository.checkNotificationClassExists(data);

		assertTrue(result);
		log.info("通知クラス存在確認テスト成功（存在する）: result={}", result);
	}

	@Test
	public void testCheckNotificationClassExists存在しない() {
		doReturn(0).when(jdbc).queryForObject(anyString(), anyMap(), eq(Integer.class));

		NotificationTeacherData data = new NotificationTeacherData();
		data.setDepartment("DEPT001");
		data.setGrade(3);
		data.setClassName("CLASS001");
		data.setTeacherUserId("TEACHER001");

		Boolean result = repository.checkNotificationClassExists(data);

		assertFalse(result);
		log.info("通知クラス存在確認テスト成功（存在しない）: result={}", result);
	}

	@Test
	public void testGetNotificationId正常() {
		doReturn("N099").when(dbdc).getLargerId(anyString(), eq("N"));

		String result = repository.getNotificationId();

		assertEquals("N099", result);
		log.info("通知ID生成テスト成功: result={}", result);
	}

	@Test
	public void testInsertNotificationJobSearch正常() {
		// SQLの更新件数をモック
		doReturn(1).when(jdbc).update(anyString(), anyMap());

		String notificationId = "N001";
		String jobSearchId = "JS001";

		Boolean result = repository.insertNotificationJobSearch(notificationId, jobSearchId);

		assertTrue(result);
		log.info("通知就職活動作成テスト成功: notificationId={}, jobSearchId={}, result={}", notificationId, jobSearchId, result);
	}

	@Test
	public void testInsertNotificationJobSearch失敗() {
		// SQLの更新件数を0にモック
		doReturn(0).when(jdbc).update(anyString(), anyMap());

		String notificationId = "N001";
		String jobSearchId = "JS001";

		Boolean result = repository.insertNotificationJobSearch(notificationId, jobSearchId);

		assertFalse(result);
		log.info("通知就職活動作成テスト失敗: notificationId={}, jobSearchId={}, result={}", notificationId, jobSearchId, result);
	}

	@Test
	public void testInsertNotificationCertificateIssuance正常() {
		// SQLの更新件数をモック
		doReturn(1).when(jdbc).update(anyString(), anyMap());

		String notificationId = "N002";
		String certificateIssueId = "CI002";

		Boolean result = repository.insertNotificationCertificateIssuance(notificationId, certificateIssueId);

		assertTrue(result);
		log.info("通知証明書発行作成テスト成功: notificationId={}, certificateIssueId={}, result={}", notificationId,
				certificateIssueId, result);
	}

	@Test
	public void testInsertNotificationCertificateIssuance失敗() {
		// SQLの更新件数を0にモック
		doReturn(0).when(jdbc).update(anyString(), anyMap());

		String notificationId = "N002";
		String certificateIssueId = "CI002";

		Boolean result = repository.insertNotificationCertificateIssuance(notificationId, certificateIssueId);

		assertFalse(result);
		log.info("通知証明書発行作成テスト失敗: notificationId={}, certificateIssueId={}, result={}", notificationId,
				certificateIssueId, result);
	}

	@Test
	public void testDeleteNotificationCertificateIssuanceUser正常() {
		// SQLの更新件数をモック
		doReturn(1).when(jdbc).update(anyString(), anyMap());

		String userId = "U001";
		String certificateIssueId = "CI001";

		Boolean result = repository.deleteNotificationCertificateIssuanceUser(userId, certificateIssueId);

		assertTrue(result);
		log.info("証明書発行通知削除テスト成功: userId={}, certificateIssueId={}, result={}", userId, certificateIssueId, result);
	}

	@Test
	public void testDeleteNotificationCertificateIssuanceUser失敗() {
		// SQLの更新件数を0にモック
		doReturn(0).when(jdbc).update(anyString(), anyMap());

		String userId = "U001";
		String certificateIssueId = "CI001";

		Boolean result = repository.deleteNotificationCertificateIssuanceUser(userId, certificateIssueId);

		assertFalse(result);
		log.info("証明書発行通知削除テスト失敗: userId={}, certificateIssueId={}, result={}", userId, certificateIssueId, result);
	}

	@Test
	public void testUpdateNotificationCertificateIssuance正常() {
		// SQLの更新件数をモック
		doReturn(1).when(jdbc).update(anyString(), anyMap());

		String certificateIssueId = "CI001";

		Boolean result = repository.updateNotificationCertificateIssuance(certificateIssueId);

		assertTrue(result);
		log.info("証明書発行通知状態更新テスト成功: certificateIssueId={}, result={}", certificateIssueId, result);
	}

	@Test
	public void testUpdateNotificationCertificateIssuance失敗() {
		// SQLの更新件数を0にモック
		doReturn(0).when(jdbc).update(anyString(), anyMap());

		String certificateIssueId = "CI001";

		Boolean result = repository.updateNotificationCertificateIssuance(certificateIssueId);

		assertFalse(result);
		log.info("証明書発行通知状態更新テスト失敗: certificateIssueId={}, result={}", certificateIssueId, result);
	}

	@Test
	public void testDeleteNotificationJobSearch正常() {
		// SQLの更新件数をモック
		doReturn(1).when(jdbc).update(anyString(), anyMap());

		String jobSearchId = "JS001";

		Boolean result = repository.deleteNotificationJobSearch(jobSearchId);

		assertTrue(result);
		log.info("就職活動通知削除テスト成功: jobSearchId={}, result={}", jobSearchId, result);
	}

	@Test
	public void testDeleteNotificationJobSearch失敗() {
		// SQLの更新件数を0にモック
		doReturn(0).when(jdbc).update(anyString(), anyMap());

		String jobSearchId = "JS001";

		Boolean result = repository.deleteNotificationJobSearch(jobSearchId);

		assertFalse(result);
		log.info("就職活動通知削除テスト失敗: jobSearchId={}, result={}", jobSearchId, result);
	}

	@Test
	public void testInsertNotificationClass正常() {
		// SQLの更新件数をモック
		doReturn(1).when(jdbc).update(anyString(), anyMap());

		NotificationTeacherData data = new NotificationTeacherData("T001", "S", 3, "A1");

		Boolean result = repository.insertNotificationClass(data);

		assertTrue(result);
		log.info("通知クラス追加テスト成功: data={}, result={}", data, result);
	}

	@Test
	public void testInsertNotificationClass失敗() {
		// SQLの更新件数を0にモック
		doReturn(0).when(jdbc).update(anyString(), anyMap());

		NotificationTeacherData data = new NotificationTeacherData("T001", "S", 3, "A1");

		Boolean result = repository.insertNotificationClass(data);

		assertFalse(result);
		log.info("通知クラス追加テスト失敗: data={}, result={}", data, result);
	}

	@Test
	public void testDeleteNotificationClass正常() {
		// SQLの更新件数をモック
		doReturn(1).when(jdbc).update(anyString(), anyMap());

		String department = "情報学科";
		Integer grade = 3;
		String classCode = "Aクラス";

		Boolean result = repository.deleteNotificationClass(department, grade, classCode);

		assertTrue(result);
		log.info("通知クラス削除テスト成功: department={}, grade={}, classCode={}, result={}", department, grade, classCode,
				result);
	}

	@Test
	public void testDeleteNotificationClass失敗() {
		// SQLの更新件数を0にモック
		doReturn(0).when(jdbc).update(anyString(), anyMap());

		String department = "情報学科";
		Integer grade = 3;
		String classCode = "Aクラス";

		Boolean result = repository.deleteNotificationClass(department, grade, classCode);

		assertFalse(result);
		log.info("通知クラス削除テスト失敗: department={}, grade={}, classCode={}, result={}", department, grade, classCode,
				result);
	}

	@Test
	public void testDeleteNotificationClassTeacher正常() {
		// SQLの更新件数をモック
		doReturn(1).when(jdbc).update(anyString(), anyMap());

		NotificationTeacherData data = new NotificationTeacherData("T001", "S", 3, "A1");

		Boolean result = repository.deleteNotificationClassTeacher(data);

		assertTrue(result);
		log.info("通知クラス担任削除テスト成功: data={}, result={}", data, result);
	}

	@Test
	public void testDeleteNotificationClassTeacher失敗() {
		// SQLの更新件数を0にモック
		doReturn(0).when(jdbc).update(anyString(), anyMap());

		NotificationTeacherData data = new NotificationTeacherData("T001", "S", 3, "A1");

		Boolean result = repository.deleteNotificationClassTeacher(data);

		assertFalse(result);
		log.info("通知クラス担任削除テスト失敗: data={}, result={}", data, result);
	}

}
