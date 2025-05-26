package com.api.domain.repositories;

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
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import com.api.domain.models.dbdata.CertificateData;
import com.api.domain.models.dbdata.CertificateIssuanceDetailData;
import com.api.domain.models.dbdata.CertificateIssuanceEntity;
import com.api.domain.models.dbdata.MailingData;
import com.api.jobpal.common.base.DBDataConversion;

@RunWith(SpringRunner.class)
@SpringBootTest
class CertificateIssuanceRepositoryTest {
	@Autowired
	private CertificateIssuanceRepository repository;

	@SpyBean
	private NamedParameterJdbcTemplate jdbc;

	@SpyBean
	private DBDataConversion dbdc;

	@Autowired
	private Logger log;

	@Test
	public void testInsertCertificateIssuanceOne正常() {
		CertificateIssuanceEntity entity = new CertificateIssuanceEntity();
		entity.setCertificateIssueId("CI001");
		entity.setStudentUserId("S001");
		entity.setMediaType("P");
		entity.setTeacherUserId("T001");

		doReturn(1).when(jdbc).update(anyString(), anyMap());

		Boolean result = repository.insertCertificateIssuanceOne(entity);

		assertTrue(result);
		log.info("証明書発行追加テスト成功: entity={}", entity);
	}

	@Test
	public void testInsertCertificateIssuanceOne異常() {
		CertificateIssuanceEntity entity = new CertificateIssuanceEntity();
		entity.setCertificateIssueId("CI001");
		entity.setStudentUserId("S001");
		entity.setMediaType("P");
		entity.setTeacherUserId("T001");

		doReturn(0).when(jdbc).update(anyString(), anyMap());

		Boolean result = repository.insertCertificateIssuanceOne(entity);

		assertFalse(result);
		log.info("証明書発行追加テスト失敗: entity={}", entity);
	}

	@Test
	public void testSelectCertificateIssueUserId正常() {
		// モックデータの準備
		String certificateIssueId = "CI_2024_00001";
		List<Map<String, Object>> mockResult = new ArrayList<>();
		Map<String, Object> row = Map.of(
				"student_user_id", "student001",
				"teacher_user_id", "teacher001",
				"office_user_id", "office001");
		mockResult.add(row);

		// カラム名のモック

		// モックの設定
		doReturn(mockResult).when(jdbc).queryForList(anyString(), anyMap());

		// テスト対象のメソッドを実行
		Map<String, String> result = repository.selectCertificateIssueUserId(certificateIssueId);

		// 結果のアサーション
		assertNotNull(result);
		assertEquals("student001", result.get("student"));
		assertEquals("teacher001", result.get("teacher"));
		assertEquals("office001", result.get("office"));

		log.info("証明書発行ユーザ取得テスト成功: certificateIssueId={}, result={}", certificateIssueId, result);
	}

	@Test
	public void testSelectCertificateIssueUserId空のリスト正常() {
		//	 モックデータの準備（空データ）
		String certificateIssueId = "CI001";
		List<Map<String, Object>> mockResult = new ArrayList<>();

		// モックの設定
		doReturn(mockResult).when(jdbc).queryForList(anyString(), anyMap());
		// テスト対象のメソッドを実行
		Map<String, String> result = repository.selectCertificateIssueUserId(certificateIssueId);

		// 結果のアサーション
		assertNotNull(result);
		assertTrue(result.isEmpty());

		log.info("証明書発行ユーザ取得テスト成功（データなし）: certificateIssueId={}, result={}", certificateIssueId, result);
	}

	@Test
	public void testSelectCertificateIssuanceStatusOne正常() {
		String certificateIssuanceId = "CI001";
		String expectedStatus = "2";

		doReturn(expectedStatus).when(jdbc).queryForObject(anyString(), anyMap(), eq(String.class));

		String result = repository.selectCertificateIssuanceStatusOne(certificateIssuanceId);

		assertEquals(expectedStatus, result);
		log.info("証明書発行状態取得テスト成功: certificateIssuanceId={}, status={}", certificateIssuanceId, result);
	}

	@Test
	public void testSelectCertificateIssuanceStatusOne空リスト正常() {
		String certificateIssuanceId = "CI001";

		doThrow(new EmptyResultDataAccessException(1)).when(jdbc)
				.queryForObject(anyString(), anyMap(), eq(String.class));
		String result = repository.selectCertificateIssuanceStatusOne(certificateIssuanceId);

		assertNull(result);
		log.info("証明書発行状態取得テスト成功（データなし）: certificateIssuanceId={}", certificateIssuanceId);
	}

	@Test
	public void testInsertCertificateIssuanceDetailOne正常() {
		String certificateIssuanceId = "CI001";
		CertificateIssuanceDetailData detailData = new CertificateIssuanceDetailData();
		detailData.setCertificateId("CERT001");
		detailData.setCertificateQuantity(2);

		doReturn(1).when(jdbc).update(anyString(), anyMap());

		Boolean result = repository.insertCertificateIssuanceDetailOne(certificateIssuanceId, detailData);

		assertTrue(result);
		log.info("証明書発行詳細追加テスト成功: certificateIssuanceId={}, detailData={}", certificateIssuanceId, detailData);
	}

	@Test
	public void testInsertCertificateIssuanceDetailOne異常() {
		String certificateIssuanceId = "CI001";
		CertificateIssuanceDetailData detailData = new CertificateIssuanceDetailData();
		detailData.setCertificateId("CERT001");
		detailData.setCertificateQuantity(2);

		doReturn(0).when(jdbc).update(anyString(), anyMap());

		Boolean result = repository.insertCertificateIssuanceDetailOne(certificateIssuanceId, detailData);

		assertFalse(result);
		log.info("証明書発行詳細追加テスト失敗: certificateIssuanceId={}, detailData={}", certificateIssuanceId, detailData);
	}

	@Test
	public void testUpdateCertificateIssuanceStatusOne正常() {
		String certificateIssueId = "CI001";
		String status = "2";

		doReturn(1).when(jdbc).update(anyString(), anyMap());

		Boolean result = repository.updateCertificateIssuanceStatusOne(certificateIssueId, status);

		assertTrue(result);
		log.info("証明書発行状態更新テスト成功: certificateIssueId={}, status={}", certificateIssueId, status);
	}

	@Test
	public void testUpdateCertificateIssuanceStatusOne異常() {
		String certificateIssueId = "CI001";
		String status = "2";

		doReturn(0).when(jdbc).update(anyString(), anyMap());

		Boolean result = repository.updateCertificateIssuanceStatusOne(certificateIssueId, status);

		assertFalse(result);
		log.info("証明書発行状態更新テスト失敗: certificateIssueId={}, status={}", certificateIssueId, status);
	}

	@Test
	public void testDeleteCertificateIssuanceOne正常() {
		String certificateIssueId = "CI001";

		doReturn(1).when(jdbc).update(anyString(), anyMap());

		Boolean result = repository.deleteCertificateIssuanceOne(certificateIssueId);

		assertTrue(result);
		log.info("証明書発行削除テスト成功: certificateIssueId={}", certificateIssueId);
	}

	@Test
	public void testDeleteCertificateIssuanceOne異常() {
		String certificateIssueId = "CI001";

		doReturn(0).when(jdbc).update(anyString(), anyMap());

		Boolean result = repository.deleteCertificateIssuanceOne(certificateIssueId);

		assertFalse(result);
		log.info("証明書発行削除テスト失敗: certificateIssueId={}", certificateIssueId);
	}

	@Test
	public void testSelectCertificateIssuanceApprovalDateOne正常() {
		List<Map<String, Object>> mockResult = new ArrayList<>();
		Map<String, Object> data = Map.of("certificate_issue_id", "CI001", "approval_date", Date.valueOf("2024-01-01"));
		mockResult.add(data);

		doReturn(mockResult).when(jdbc).queryForList(anyString(), anyMap());

		List<Map<String, Date>> result = repository.selectCertificateIssuanceApprovalDateOne();

		assertEquals(1, result.size());
		assertEquals("CI001", result.get(0).keySet().iterator().next());
		assertEquals(Date.valueOf("2024-01-01"), result.get(0).values().iterator().next());
		log.info("証明書発行承認日取得テスト成功: result={}", result);
	}

	@Test
	public void testSelectCertificateIssuanceApprovalDateOneからデータ正常() {
		doReturn(new ArrayList<>()).when(jdbc).queryForList(anyString(), anyMap());

		List<Map<String, Date>> result = repository.selectCertificateIssuanceApprovalDateOne();

		assertTrue(result.isEmpty());
		log.info("証明書発行承認日取得テスト成功（データなし）: result={}", result);
	}

	@Test
	public void testSelectPostalPaymentIdMax正常() {
		// モックの設定: 正常な最大IDが返却されるケース
		doReturn("PM001").when(jdbc).queryForObject(anyString(), anyMap(), eq(String.class));

		// テスト対象メソッドの呼び出し
		String result = repository.selectPostalPaymentIdMax();

		// 検証
		assertNotNull(result);
		assertEquals("PM001", result);
		System.out.println("最大郵送ID取得テスト成功: result=" + result);
	}

	@Test
	public void testSelectPostalPaymentIdMaxデータなし() {
		// モックの設定: データが存在しない場合
		doThrow(EmptyResultDataAccessException.class).when(jdbc).queryForObject(anyString(), anyMap(),
				eq(String.class));

		// テスト対象メソッドの呼び出し
		String result = repository.selectPostalPaymentIdMax();

		// 検証
		assertNull(result);
		System.out.println("最大郵送ID取得テスト成功（データなし）: result=" + result);
	}

	@Test
	public void testSelectCertificateAll正常() {
		// モックの設定: 正常なデータが返却されるケース
		List<Map<String, Object>> mockResult = new ArrayList<>();
		mockResult.add(Map.of(
				"certificate_id", "C001",
				"certificate_name", "証明書A",
				"certificate_fee", 1000,
				"certificate_weight", 50));
		mockResult.add(Map.of(
				"certificate_id", "C002",
				"certificate_name", "証明書B",
				"certificate_fee", 2000,
				"certificate_weight", 60));
		doReturn(mockResult).when(jdbc).queryForList(anyString(), anyMap());
		doReturn(new String[] { "certificate_id", "certificate_name", "certificate_fee", "certificate_weight" })
				.when(dbdc).extractColumnNames(anyString());

		// テスト対象メソッドの呼び出し
		List<CertificateData> result = repository.selectCertificateAll();

		// 検証
		assertEquals(2, result.size());
		assertEquals("C001", result.get(0).getCertificateId());
		assertEquals("証明書A", result.get(0).getCertificateName());
		assertEquals(1000, result.get(0).getCertificateFee());
		assertEquals(50, result.get(0).getCertificateWeight());
		assertEquals("C002", result.get(1).getCertificateId());
		assertEquals("証明書B", result.get(1).getCertificateName());
		assertEquals(2000, result.get(1).getCertificateFee());
		assertEquals(60, result.get(1).getCertificateWeight());
		System.out.println("証明書一覧取得テスト成功: result=" + result);
	}

	@Test
	public void testSelectCertificateAllデータなし() {
		// モックの設定: データが存在しない場合
		doReturn(new ArrayList<>()).when(jdbc).queryForList(anyString(), anyMap());
		doReturn(new String[] { "certificate_id", "certificate_name", "certificate_fee", "certificate_weight" })
				.when(dbdc).extractColumnNames(anyString());

		// テスト対象メソッドの呼び出し
		List<CertificateData> result = repository.selectCertificateAll();

		// 検証
		assertTrue(result.isEmpty());
		System.out.println("証明書一覧取得テスト成功（データなし）: result=" + result);
	}

	@Test
	public void testSelectMailing正常() {
		Map<String, Object> mockResult = Map.of("postal_fee", 500, "postal_max_weight", 2000);

		doReturn(List.of(mockResult)).when(jdbc).queryForList(anyString(), anyMap());

		MailingData result = repository.selectMailing();

		assertNotNull(result);
		assertEquals(500, result.getPostalFee());
		assertEquals(2000, result.getPostalMaxWeight());
		log.info("最新郵送情報取得テスト成功: result={}", result);
	}

	@Test
	public void testSelectMailingデータなし() {
		doReturn(new ArrayList<>()).when(jdbc).queryForList(anyString(), anyMap());

		MailingData result = repository.selectMailing();

		assertNull(result);
		log.info("最新郵送情報取得テスト成功（データなし）: result={}", result);
	}

	@Test
	public void testGetCertificateIssuanceId正常() {

		String result = repository.getCertificateIssuanceId();

		assertNotNull(result);
		log.info("新しい証明書発行ID生成テスト成功: result={}", result);
	}

	@Test
	public void testInsertPaperCertificateIssuanceOne正常() {
		doReturn(1).when(jdbc).update(anyString(), anyMap());

		Boolean result = repository.insertPaperCertificateIssuanceOne("CI001", Date.valueOf("2024-01-01"));

		assertTrue(result);
		log.info("紙証明書発行情報追加テスト成功: result={}", result);
	}

	@Test
	public void testInsertPaperCertificateIssuanceOne失敗() {
		doReturn(0).when(jdbc).update(anyString(), anyMap());

		Boolean result = repository.insertPaperCertificateIssuanceOne("CI001", Date.valueOf("2024-01-01"));

		assertFalse(result);
		log.info("紙証明書発行情報追加テスト失敗: result={}", result);
	}

	@Test
	public void testInsertElectronicCertificateIssuanceOne正常() {
		doReturn(1).when(jdbc).update(anyString(), anyMap());

		Boolean result = repository.insertElectronicCertificateIssuanceOne("CI001", "USER001");

		assertTrue(result);
		log.info("電子証明書発行情報追加テスト成功: result={}", result);
	}

	@Test
	public void testInsertElectronicCertificateIssuanceOne失敗() {
		doReturn(0).when(jdbc).update(anyString(), anyMap());

		Boolean result = repository.insertElectronicCertificateIssuanceOne("CI001", "USER001");

		assertFalse(result);
		log.info("電子証明書発行情報追加テスト失敗: result={}", result);
	}

	@Test
	public void testInsertMailingCertificateIssuanceOne正常() {
		CertificateIssuanceEntity entity = new CertificateIssuanceEntity();
		entity.setCertificateIssueId("CI001");
		entity.setRecipientName("山田太郎");
		entity.setRecipientFurigana("ヤマダタロウ");
		entity.setRecipientAddress("東京都千代田区");

		doReturn(1).when(jdbc).update(anyString(), anyMap());

		Boolean result = repository.insertMailingCertificateIssuanceOne(entity, "POST001");

		assertTrue(result);
		log.info("郵送証明書発行情報追加テスト成功: result={}", result);
	}

	@Test
	public void testInsertMailingCertificateIssuanceOne失敗() {
		CertificateIssuanceEntity entity = new CertificateIssuanceEntity();
		entity.setCertificateIssueId("CI001");
		entity.setRecipientName("山田太郎");
		entity.setRecipientFurigana("ヤマダタロウ");
		entity.setRecipientAddress("東京都千代田区");

		doReturn(0).when(jdbc).update(anyString(), anyMap());

		Boolean result = repository.insertMailingCertificateIssuanceOne(entity, "POST001");

		assertFalse(result);
		log.info("郵送証明書発行情報追加テスト失敗: result={}", result);
	}

	@Test
	public void testInsertMailingOne正常() {
		doReturn(1).when(jdbc).update(anyString(), anyMap());

		Boolean result = repository.insertMailingOne(500, 2000);

		assertTrue(result);
		log.info("郵送情報追加テスト成功: result={}", result);
	}

	@Test
	public void testInsertMailingOne失敗() {
		doReturn(0).when(jdbc).update(anyString(), anyMap());

		Boolean result = repository.insertMailingOne(500, 2000);

		assertFalse(result);
		log.info("郵送情報追加テスト失敗: result={}", result);
	}

	@Test
	public void testUpdateCertificateIssuanceTeacherCheckFlagOne正常() {
		doReturn(1).when(jdbc).update(anyString(), anyMap());

		Boolean result = repository.updateCertificateIssuanceTeacherCheckFlagOne("CI001", true);

		assertTrue(result);
		log.info("担任承認フラグ更新テスト成功: result={}", result);
	}

	@Test
	public void testUpdateCertificateIssuanceTeacherCheckFlagOne失敗() {
		doReturn(0).when(jdbc).update(anyString(), anyMap());

		Boolean result = repository.updateCertificateIssuanceTeacherCheckFlagOne("CI001", true);

		assertFalse(result);
		log.info("担任承認フラグ更新テスト失敗: result={}", result);
	}

	@Test
	public void testUpdateCertificateIssuanceOfficeUserIdOne正常() {
		doReturn(1).when(jdbc).update(anyString(), anyMap());

		Boolean result = repository.updateCertificateIssuanceOfficeUserIdOne("CI001", "OFF001");

		assertTrue(result);
		log.info("事務部情報更新テスト成功: result={}", result);
	}

	@Test
	public void testUpdateCertificateIssuanceOfficeUserIdOne失敗() {
		doReturn(0).when(jdbc).update(anyString(), anyMap());

		Boolean result = repository.updateCertificateIssuanceOfficeUserIdOne("CI001", "OFF001");

		assertFalse(result);
		log.info("事務部情報更新テスト失敗: result={}", result);
	}

	@Test
	public void testUpdateDateOne正常() {
		doReturn(1).when(jdbc).update(anyString(), anyMap());

		Boolean result = repository.updateDateOne("CI001", "P");

		assertTrue(result);
		log.info("日付更新テスト成功: result={}", result);
	}

	@Test
	public void testUpdateDateOne異常() {
		doReturn(0).when(jdbc).update(anyString(), anyMap());

		Boolean result = repository.updateDateOne("CI001", "P");

		assertFalse(result);
		log.info("日付更新テスト成功: result={}", result);
	}

	@Test
	public void testUpdateDateOne入力エラー() {
		Boolean result = repository.updateDateOne("CI001", "X");

		assertFalse(result);
		log.info("日付更新テスト失敗（入力エラー）: result={}", result);
	}

	@Test
	public void testUpdateCertificateIssuanceMediaTypeOne正常() {
		doReturn(1).when(jdbc).update(anyString(), anyMap());

		Boolean result = repository.updateCertificateIssuanceMediaTypeOne("CI001", "M");

		assertTrue(result);
		log.info("媒体区分更新テスト成功: result={}", result);
	}

	@Test
	public void testUpdateCertificateIssuanceMediaTypeOne失敗() {
		doReturn(0).when(jdbc).update(anyString(), anyMap());

		Boolean result = repository.updateCertificateIssuanceMediaTypeOne("CI001", "M");

		assertFalse(result);
		log.info("媒体区分更新テスト失敗: result={}", result);
	}

	@Test
	public void testUpdateCertificateIssuanceDetailOne正常() {
		doReturn(1).when(jdbc).update(anyString(), anyMap());

		CertificateIssuanceDetailData detailData = new CertificateIssuanceDetailData();
		detailData.setCertificateId("C001");
		detailData.setCertificateQuantity(2);

		Boolean result = repository.updateCertificateIssuanceDetailOne("CI001", detailData);

		assertTrue(result);
		log.info("証明書発行詳細更新テスト成功: result={}", result);
	}

	@Test
	public void testUpdateCertificateIssuanceDetailOne失敗() {
		doReturn(0).when(jdbc).update(anyString(), anyMap());

		CertificateIssuanceDetailData detailData = new CertificateIssuanceDetailData();
		detailData.setCertificateId("C001");
		detailData.setCertificateQuantity(2);

		Boolean result = repository.updateCertificateIssuanceDetailOne("CI001", detailData);

		assertFalse(result);
		log.info("証明書発行詳細更新テスト失敗: result={}", result);
	}

	@Test
	public void testUpdateMailingCertificateIssuanceOne正常() {
		doReturn(1).when(jdbc).update(anyString(), anyMap());

		CertificateIssuanceEntity issuanceEntity = new CertificateIssuanceEntity();
		issuanceEntity.setCertificateIssueId("CI001");
		issuanceEntity.setRecipientName("田中太郎");
		issuanceEntity.setRecipientFurigana("タナカタロウ");
		issuanceEntity.setRecipientAddress("東京都渋谷区");

		Boolean result = repository.updateMailingCertificateIssuanceOne(issuanceEntity);

		assertTrue(result);
		log.info("郵送証明書発行更新テスト成功: result={}", result);
	}

	@Test
	public void testUpdateMailingCertificateIssuanceOne失敗() {
		doReturn(0).when(jdbc).update(anyString(), anyMap());

		CertificateIssuanceEntity issuanceEntity = new CertificateIssuanceEntity();
		issuanceEntity.setCertificateIssueId("CI001");
		issuanceEntity.setRecipientName("田中太郎");
		issuanceEntity.setRecipientFurigana("タナカタロウ");
		issuanceEntity.setRecipientAddress("東京都渋谷区");

		Boolean result = repository.updateMailingCertificateIssuanceOne(issuanceEntity);

		assertFalse(result);
		log.info("郵送証明書発行更新テスト失敗: result={}", result);
	}

}
