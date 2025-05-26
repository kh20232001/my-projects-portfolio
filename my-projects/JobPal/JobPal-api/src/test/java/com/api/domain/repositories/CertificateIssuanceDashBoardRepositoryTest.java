package com.api.domain.repositories;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import com.api.domain.models.dbdata.CertificateData;
import com.api.domain.models.dbdata.CertificateIssuanceDashBoardData;
import com.api.domain.models.dbdata.CertificateIssuanceDashBoardDetailData;
import com.api.domain.models.dbdata.MailingData;
import com.api.jobpal.common.base.DBDataConversion;

@RunWith(SpringRunner.class)
@SpringBootTest
class CertificateIssuanceDashBoardRepositoryTest {
	@Autowired
	private CertificateIssuanceDashBoardRepository repository;
	@SpyBean
	private CertificateIssuanceRepository cir;
	@SpyBean
	private NamedParameterJdbcTemplate jdbc;

	@SpyBean
	private DBDataConversion dbdc;

	@Autowired
	private Logger log;

	@Test
	public void testSelectCertificateIssuanceDashBoard正常管理者郵送() {
		// モックの結果データ
		List<Map<String, Object>> mockResultList = new ArrayList<>();
		Map<String, Object> resultData = Map.ofEntries(
				Map.entry("certificate_issue_id", "CI001"),
				Map.entry("student_user_id", "student001@hcs.ac.jp"),
				Map.entry("student_user_name", "山田太郎"),
				Map.entry("student_id", 101),
				Map.entry("status", "1"),
				Map.entry("media_type", "2"), // 郵送
				Map.entry("latest_date", Date.valueOf("2024-03-15")),
				Map.entry("total_fee", 5000),
				Map.entry("certificate_quantity", 2),
				Map.entry("certificate_weight", 10), // 重量 10g
				Map.entry("certificate_id", "C001"),
				Map.entry("certificate_name", "卒業証明書"));

		// queryList (カラム名)
		String[] queryList = {
				"certificate_issue_id", // [0]
				"student_user_id", // [1]
				"student_user_name", // 使用されていない（直接キー指定）
				"student_id", // [3]
				"status", // [4]
				"media_type", // [5]
				"latest_date", // 使用されていない（直接キー指定）
				"total_fee", // 使用されていない（直接キー指定）
				"certificate_quantity", // 使用されていない（直接キー指定）
				"certificate_weight" // 使用されていない（直接キー指定）
		};

		mockResultList.add(resultData);
		mockResultList.add(resultData);

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

		// テスト実行
		List<CertificateIssuanceDashBoardData> result = repository.selectCertificateIssuanceDashBoard();

		// 検証
		assertNotNull(result);
		assertEquals(1, result.size());

		CertificateIssuanceDashBoardData dashboardData1 = result.get(0);
		assertEquals("CI001", dashboardData1.getCertificateIssueId());
		assertEquals("student001@hcs.ac.jp", dashboardData1.getStudentUserId());
		assertEquals("山田太郎", dashboardData1.getStudentUserName());
		assertEquals("1", dashboardData1.getStatus());
		assertEquals(2, dashboardData1.getCertificateList().size());

		log.info("selectCertificateIssuanceDashBoard正常テスト成功: result={}", result);
	}

	@Test
	public void testSelectCertificateIssuanceDashBoard正常管理者郵送以外() {
		// モックの結果データ
		List<Map<String, Object>> mockResultList = new ArrayList<>();
		Map<String, Object> resultData = Map.ofEntries(
				Map.entry("certificate_issue_id", "CI001"),
				Map.entry("student_user_id", "student001@hcs.ac.jp"),
				Map.entry("student_user_name", "山田太郎"),
				Map.entry("student_id", 101),
				Map.entry("status", "1"),
				Map.entry("media_type", "0"), // 郵送
				Map.entry("latest_date", Date.valueOf("2024-03-15")),
				Map.entry("total_fee", 5000),
				Map.entry("certificate_quantity", 2),
				Map.entry("certificate_weight", 10), // 重量 10g
				Map.entry("certificate_id", "C001"),
				Map.entry("certificate_name", "卒業証明書"));

		// queryList (カラム名)
		String[] queryList = {
				"certificate_issue_id", // [0]
				"student_user_id", // [1]
				"student_user_name", // 使用されていない（直接キー指定）
				"student_id", // [3]
				"status", // [4]
				"media_type", // [5]
				"latest_date", // 使用されていない（直接キー指定）
				"total_fee", // 使用されていない（直接キー指定）
				"certificate_quantity", // 使用されていない（直接キー指定）
				"certificate_weight" // 使用されていない（直接キー指定）
		};

		mockResultList.add(resultData);
		mockResultList.add(resultData);
		resultData = Map.ofEntries(
				Map.entry("certificate_issue_id", "CI002"),
				Map.entry("student_user_id", "student001@hcs.ac.jp"),
				Map.entry("student_user_name", "山田太郎"),
				Map.entry("student_id", 101),
				Map.entry("status", "1"),
				Map.entry("media_type", "0"), // 郵送
				Map.entry("latest_date", Date.valueOf("2024-03-15")),
				Map.entry("total_fee", 5000),
				Map.entry("certificate_quantity", 2),
				Map.entry("certificate_weight", 10), // 重量 10g
				Map.entry("certificate_id", "C001"),
				Map.entry("certificate_name", "卒業証明書"));
		mockResultList.add(resultData);

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

		// テスト実行
		List<CertificateIssuanceDashBoardData> result = repository.selectCertificateIssuanceDashBoard();

		// 検証
		assertNotNull(result);
		assertEquals(2, result.size());

		CertificateIssuanceDashBoardData dashboardData1 = result.get(0);
		assertEquals("CI001", dashboardData1.getCertificateIssueId());
		assertEquals("student001@hcs.ac.jp", dashboardData1.getStudentUserId());
		assertEquals("山田太郎", dashboardData1.getStudentUserName());
		assertEquals("1", dashboardData1.getStatus());
		assertEquals(2, dashboardData1.getCertificateList().size());

		log.info("selectCertificateIssuanceDashBoard正常テスト成功: result={}", result);
	}

	@Test
	public void testSelectCertificateIssuanceDashBoard管理者データなし() {
		// モックの振る舞いを設定（結果なし）
		doReturn(new ArrayList<>()).when(jdbc).queryForList(anyString(), anyMap());
		doReturn(new String[0]).when(dbdc).extractColumnNames(anyString());
		doReturn(new MailingData()).when(cir).selectMailing();

		// テスト実行
		List<CertificateIssuanceDashBoardData> result = repository.selectCertificateIssuanceDashBoard();

		// 検証
		assertNotNull(result);
		assertTrue(result.isEmpty());
		log.info("selectCertificateIssuanceDashBoardデータなしテスト成功: result={}", result);
	}

	@Test
	public void testSelectCertificateIssuanceDashBoard正常その他郵送() {
		// モックの結果データ
		List<Map<String, Object>> mockResultList = new ArrayList<>();
		Map<String, Object> resultData = Map.ofEntries(
				Map.entry("certificate_issue_id", "CI001"),
				Map.entry("student_user_id", "student001@hcs.ac.jp"),
				Map.entry("student_user_name", "山田太郎"),
				Map.entry("student_id", 101),
				Map.entry("status", "1"),
				Map.entry("media_type", "2"), // 郵送
				Map.entry("latest_date", Date.valueOf("2024-03-15")),
				Map.entry("total_fee", 5000),
				Map.entry("certificate_quantity", 2),
				Map.entry("certificate_weight", 10), // 重量 10g
				Map.entry("certificate_id", "C001"),
				Map.entry("certificate_name", "卒業証明書"));

		// queryList (カラム名)
		String[] queryList = {
				"certificate_issue_id", // [0]
				"student_user_id", // [1]
				"student_user_name", // 使用されていない（直接キー指定）
				"student_id", // [3]
				"status", // [4]
				"media_type", // [5]
				"latest_date", // 使用されていない（直接キー指定）
				"total_fee", // 使用されていない（直接キー指定）
				"certificate_quantity", // 使用されていない（直接キー指定）
				"certificate_weight" // 使用されていない（直接キー指定）
		};

		mockResultList.add(resultData);
		mockResultList.add(resultData);

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

		// テスト実行
		List<CertificateIssuanceDashBoardData> result = repository
				.selectCertificateIssuanceDashBoard("student001@hcs.ac.jp", "学生");

		// 検証
		assertNotNull(result);
		assertEquals(1, result.size());

		CertificateIssuanceDashBoardData dashboardData1 = result.get(0);
		assertEquals("CI001", dashboardData1.getCertificateIssueId());
		assertEquals("student001@hcs.ac.jp", dashboardData1.getStudentUserId());
		assertEquals("山田太郎", dashboardData1.getStudentUserName());
		assertEquals("1", dashboardData1.getStatus());
		assertEquals(2, dashboardData1.getCertificateList().size());

		log.info("selectCertificateIssuanceDashBoard正常テスト成功: result={}", result);
	}

	@Test
	public void testSelectCertificateIssuanceDashBoard正常その他郵送以外() {
		// モックの結果データ
		List<Map<String, Object>> mockResultList = new ArrayList<>();
		Map<String, Object> resultData = Map.ofEntries(
				Map.entry("certificate_issue_id", "CI001"),
				Map.entry("student_user_id", "student001@hcs.ac.jp"),
				Map.entry("student_user_name", "山田太郎"),
				Map.entry("student_id", 101),
				Map.entry("status", "1"),
				Map.entry("media_type", "0"), // 郵送
				Map.entry("latest_date", Date.valueOf("2024-03-15")),
				Map.entry("total_fee", 5000),
				Map.entry("certificate_quantity", 2),
				Map.entry("certificate_weight", 10), // 重量 10g
				Map.entry("certificate_id", "C001"),
				Map.entry("certificate_name", "卒業証明書"));

		// queryList (カラム名)
		String[] queryList = {
				"certificate_issue_id", // [0]
				"student_user_id", // [1]
				"student_user_name", // 使用されていない（直接キー指定）
				"student_id", // [3]
				"status", // [4]
				"media_type", // [5]
				"latest_date", // 使用されていない（直接キー指定）
				"total_fee", // 使用されていない（直接キー指定）
				"certificate_quantity", // 使用されていない（直接キー指定）
				"certificate_weight" // 使用されていない（直接キー指定）
		};

		mockResultList.add(resultData);
		mockResultList.add(resultData);

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

		// テスト実行
		List<CertificateIssuanceDashBoardData> result = repository
				.selectCertificateIssuanceDashBoard("student001@hcs.ac.jp", "学生");

		// 検証
		assertNotNull(result);
		assertEquals(1, result.size());

		CertificateIssuanceDashBoardData dashboardData1 = result.get(0);
		assertEquals("CI001", dashboardData1.getCertificateIssueId());
		assertEquals("student001@hcs.ac.jp", dashboardData1.getStudentUserId());
		assertEquals("山田太郎", dashboardData1.getStudentUserName());
		assertEquals("1", dashboardData1.getStatus());
		assertEquals(2, dashboardData1.getCertificateList().size());

		log.info("selectCertificateIssuanceDashBoard正常テスト成功: result={}", result);
	}

	@Test
	public void testSelectCertificateIssuanceDashBoardその他データなし() {
		// モックの振る舞いを設定（結果なし）
		doReturn(new ArrayList<>()).when(jdbc).queryForList(anyString(), anyMap());
		doReturn(new String[0]).when(dbdc).extractColumnNames(anyString());
		doReturn(new MailingData()).when(cir).selectMailing();

		// テスト実行
		List<CertificateIssuanceDashBoardData> result = repository
				.selectCertificateIssuanceDashBoard("student001@hcs.ac.jp", "学生");

		// 検証
		assertNotNull(result);
		assertTrue(result.isEmpty());
		log.info("selectCertificateIssuanceDashBoardデータなしテスト成功: result={}", result);
	}

	@Test
	public void testSelectCertificateIssuanceDashBoardDetail正常() {
		Map<String, Object> mockData = Map.ofEntries(
				Map.entry("certificate_issue_id", "CI003"),
				Map.entry("student_user_id", "student003@hcs.ac.jp"),
				Map.entry("department", "情報工学科"),
				Map.entry("grade", "3"),
				Map.entry("class_name", "A"),
				Map.entry("attendance_number", 15),
				Map.entry("student_user_name", "田中一郎"),
				Map.entry("student_id", 101),
				Map.entry("status", "2"),
				Map.entry("office_user_name", "事務担当者"),
				Map.entry("recipient_name", "田中一郎"),
				Map.entry("recipient_furigana", "タナカイチロウ"),
				Map.entry("recipient_address", "東京都千代田区"),
				Map.entry("media_type", "郵送"),
				Map.entry("application_date", Date.valueOf("2024-03-01")),
				Map.entry("approval_date", Date.valueOf("2024-03-05")),
				Map.entry("delivery_due_date", Date.valueOf("2024-03-10")),
				Map.entry("delivery_date", Date.valueOf("2024-03-12")),
				Map.entry("post_date", Date.valueOf("2024-03-15")),
				Map.entry("postal_fee", 800),
				Map.entry("postal_max_weight", 200));

		String[] queryList = {
				"certificate_issue_id", // [0]
				"student_user_id", // [1]
				"department", // [2]
				"grade", // [3]
				"class_name", // [4]
				"attendance_number", // [5]
				"student_user_name", // [6] (直接キーを指定しているため使われない場合もあり)
				"student_id", // [7]
				"status", // [8]
				"office_user_name", // [9] (直接キーを指定しているため使われない場合もあり)
				"office_user_name", // [10]
				"recipient_name", // [11]
				"recipient_furigana", // [12]
				"recipient_address", // [13]
				"media_type", // [14]
				"application_date", // [15]
				"approval_date", // [16]
				"delivery_due_date", // [17]
				"delivery_date", // [18]
				"post_date", // [19]
				"postal_fee", // [20]
				"postal_max_weight" // [21]
		};

		// モックの振る舞いを定義
		doReturn(List.of(mockData)).when(jdbc).queryForList(anyString(), anyMap());
		doReturn(queryList)
				.when(dbdc).extractColumnNames(anyString());

		// テスト実行
		CertificateIssuanceDashBoardDetailData result = repository.selectCertificateIssuanceDashBoardDetail("CI003");

		// 検証
		assertNotNull(result);
		assertEquals("CI003", result.getCertificateIssueId());
		assertEquals("田中一郎", result.getStudentUserName());
		log.info("selectCertificateIssuanceDashBoardDetail正常テスト成功: result={}", result);
	}

	@Test
	public void testSelectCertificate正常() {
		// モックの結果データ
		List<Map<String, Object>> mockResultList = new ArrayList<>();
		Map<String, Object> mockData1 = Map.of(
				"certificate_id", "C001",
				"certificate_quantity", 2,
				"certificate_name", "卒業証明書",
				"certificate_fee", 300,
				"certificate_weight", 20);
		Map<String, Object> mockData2 = Map.of(
				"certificate_id", "C002",
				"certificate_quantity", 1,
				"certificate_name", "成績証明書",
				"certificate_fee", 500,
				"certificate_weight", 30);
		mockResultList.add(mockData1);
		mockResultList.add(mockData2);

		// モックの振る舞いを定義
		doReturn(mockResultList).when(jdbc).queryForList(anyString(), anyMap());
		doReturn(new String[] { "certificate_id", "certificate_quantity", "certificate_name",
				"certificate_fee", "certificate_weight" })
						.when(dbdc).extractColumnNames(anyString());

		// テスト実行
		List<CertificateData> result = repository.selectCertificate("CI001");

		// 検証
		assertNotNull(result);
		assertEquals(2, result.size());

		// 1つ目のデータ検証
		CertificateData data1 = result.get(0);
		assertEquals("C001", data1.getCertificateId());
		assertEquals(2, data1.getCertificateQuantity());
		assertEquals("卒業証明書", data1.getCertificateName());
		assertEquals(300, data1.getCertificateFee());
		assertEquals(20, data1.getCertificateWeight());

		// 2つ目のデータ検証
		CertificateData data2 = result.get(1);
		assertEquals("C002", data2.getCertificateId());
		assertEquals(1, data2.getCertificateQuantity());
		assertEquals("成績証明書", data2.getCertificateName());
		assertEquals(500, data2.getCertificateFee());
		assertEquals(30, data2.getCertificateWeight());

		log.info("selectCertificate正常テスト成功: result={}", result);
	}

	@Test
	public void testSelectCertificateIssuanceDashBoardDetail結果なし() {
		// モックの振る舞いを定義（結果が空の場合）
		doReturn(new ArrayList<>()).when(jdbc).queryForList(anyString(), anyMap());

		// テスト実行
		CertificateIssuanceDashBoardDetailData result = repository.selectCertificateIssuanceDashBoardDetail("CI002");

		// 検証
		assertNull(result);
		log.info("selectCertificateIssuanceDashBoardDetail結果なしテスト成功: result={}", result);
	}

	@Test
	public void testSelectCertificateIssuanceDashBoard入力エラー() {
		// テスト実行（入力エラー: 不正なユーザ区分）
		List<CertificateIssuanceDashBoardData> result = repository.selectCertificateIssuanceDashBoard("user001", "不正");

		// 検証
		assertNotNull(result);
		assertTrue(result.isEmpty());
		log.info("selectCertificateIssuanceDashBoard入力エラーテスト成功: result={}", result);
	}
}