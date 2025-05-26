package com.api.domain.repositories;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

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

import com.api.jobpal.common.base.DBDataConversion;

@RunWith(SpringRunner.class)
@SpringBootTest
class CsvRepositoryTest {
	@Autowired
	private CsvRepository repository;

	@SpyBean
	private NamedParameterJdbcTemplate jdbc;

	@SpyBean
	private DBDataConversion dbdc;

	@Autowired
	private Logger log;

	@Test
	public void testSelectJobSearchIdList正常() {
		// 0. Mock setup
		List<Map<String, Object>> mockResult = List.of(
				Map.of("job_search_id", "JS001"),
				Map.of("job_search_id", "JS002"));

		doReturn(mockResult).when(jdbc).queryForList(anyString(), anyMap());

		// 1. Ready
		String userId = "U001";

		// 2. Do
		List<String> result = repository.selectJobSearchIdList(userId);

		// 3. Check
		assertEquals(List.of("JS001", "JS002"), result);

		// 4. Log
		log.info("就職活動ID取得テスト成功: userId={}, jobSearchIds={}", userId, result);
	}

	@Test
	public void testSelectJobSearchIdList異常() {
		// 0. Mock setup
		doReturn(List.of()).when(jdbc).queryForList(anyString(), anyMap());
		// 1. Ready
		String userId = "U001";

		// 2. Do
		List<String> result = repository.selectJobSearchIdList(userId);

		// 3. Check
		assertTrue(result.isEmpty());

		// 4. Log
		log.info("就職活動ID取得テスト成功(空データ): userId={}", userId);
	}

	@Test
	public void testSelectCertificateIssuanceIdList正常() {
		// 0. Mock setup
		List<Map<String, Object>> mockResult = List.of(
				Map.of("certificate_issue_id", "CI001"),
				Map.of("certificate_issue_id", "CI002"));
		doReturn(mockResult).when(jdbc).queryForList(anyString(), anyMap());

		// 1. Ready
		String userId = "U001";

		// 2. Do
		List<String> result = repository.selectCertificateIssuanceIdList(userId);

		// 3. Check
		assertEquals(List.of("CI001", "CI002"), result);

		// 4. Log
		log.info("証明書発行ID取得テスト成功: userId={}, certificateIssuanceIds={}", userId, result);
	}

	@Test
	public void testSelectCertificateIssuanceIdList異常() {
		// 0. Mock setup
		doReturn(List.of()).when(jdbc).queryForList(anyString(), anyMap());

		// 1. Ready
		String userId = "U001";

		// 2. Do
		List<String> result = repository.selectCertificateIssuanceIdList(userId);

		// 3. Check
		assertTrue(result.isEmpty());

		// 4. Log
		log.info("証明書発行ID取得テスト成功(空データ): userId={}", userId);
	}

	@Test
	public void testSelectCsvActivity_Success() {
		// 0. Mock setup

		doReturn(5).when(jdbc).queryForObject(anyString(), anyMap(), eq(Integer.class));
		// 1. Ready
		String userId = "U001";

		// 2. Do
		Integer result = repository.selectCsvActivity(userId);

		// 3. Check
		assertEquals(5, result);

		// 4. Log
		log.info("活動データ件数取得テスト成功: userId={}, recordCount={}", userId, result);
	}

	@Test
	public void testSelectCsvActivity異常() {
		// 0. Mock setup

		doThrow(new EmptyResultDataAccessException(1)).when(jdbc).queryForObject(anyString(), anyMap(),
				eq(Integer.class));

		// 1. Ready
		String userId = "U001";

		// 2. Do
		Integer result = repository.selectCsvActivity(userId);

		// 3. Check
		assertEquals(0, result);

		// 4. Log
		log.info("活動データ件数取得テスト成功(データなし): userId={}", userId);
	}

	@Test
	public void testSelectCsvActivityFinish正常() {
		// 0. Mock setup
		doReturn(10).when(jdbc).queryForObject(anyString(), anyMap(), eq(Integer.class));

		// 1. Ready
		String userId = "U001";

		// 2. Do
		Integer result = repository.selectCsvActivityFinish(userId);

		// 3. Check
		assertEquals(10, result);

		// 4. Log
		log.info("完了済み活動データ件数取得テスト成功: userId={}, recordCount={}", userId, result);
	}

	@Test
	public void testSelectCsvActivityFinish異常() {
		// 0. Mock setup
		doThrow(new EmptyResultDataAccessException(1)).when(jdbc).queryForObject(anyString(), anyMap(),
				eq(Integer.class));

		// 1. Ready
		String userId = "U001";

		// 2. Do
		Integer result = repository.selectCsvActivityFinish(userId);

		// 3. Check
		assertEquals(0, result);

		// 4. Log
		log.info("完了済み活動データ件数取得テスト成功(データなし): userId={}", userId);
	}

	@Test
	public void testSelectCsvSapporo正常() {
		// 0. Mock setup
		doReturn(7).when(jdbc).queryForObject(anyString(), anyMap(), eq(Integer.class));

		// 1. Ready
		String userId = "U001";

		// 2. Do
		Integer result = repository.selectCsvSapporo(userId);

		// 3. Check
		assertEquals(7, result);

		// 4. Log
		log.info("札幌地域データ件数取得テスト成功: userId={}, recordCount={}", userId, result);
	}

	@Test
	public void testSelectCsvSapporo異常() {
		// 0. Mock setup
		doThrow(new EmptyResultDataAccessException(1)).when(jdbc).queryForObject(anyString(), anyMap(),
				eq(Integer.class));

		// 1. Ready
		String userId = "U001";

		// 2. Do
		Integer result = repository.selectCsvSapporo(userId);

		// 3. Check
		assertEquals(0, result);

		// 4. Log
		log.info("札幌地域データ件数取得テスト成功(データなし): userId={}", userId);
	}

	@Test
	public void testSelectCsvTokyo正常() {
		// 0. Mock setup
		doReturn(15).when(jdbc).queryForObject(anyString(), anyMap(), eq(Integer.class));

		// 1. Ready
		String userId = "U001";

		// 2. Do
		Integer result = repository.selectCsvTokyo(userId);

		// 3. Check
		assertEquals(15, result);

		// 4. Log
		log.info("東京地域データ件数取得テスト成功: userId={}, recordCount={}", userId, result);
	}

	@Test
	public void testSelectCsvTokyo異常() {
		// 0. Mock setup
		doThrow(new EmptyResultDataAccessException(1)).when(jdbc).queryForObject(anyString(), anyMap(),
				eq(Integer.class));

		// 1. Ready
		String userId = "U001";

		// 2. Do
		Integer result = repository.selectCsvTokyo(userId);

		// 3. Check
		assertEquals(0, result);

		// 4. Log
		log.info("東京地域データ件数取得テスト成功(データなし): userId={}", userId);
	}

	@Test
	public void testSelectCsvOthers正常() {
		// 0. Mock setup
		doReturn(12).when(jdbc).queryForObject(anyString(), anyMap(), eq(Integer.class));

		// 1. Ready
		String userId = "U001";

		// 2. Do
		Integer result = repository.selectCsvOthers(userId);

		// 3. Check
		assertEquals(12, result);

		// 4. Log
		log.info("その他地域データ件数取得テスト成功: userId={}, recordCount={}", userId, result);
	}

	@Test
	public void testSelectCsvOthers異常() {
		// 0. Mock setup

		doThrow(new EmptyResultDataAccessException(1)).when(jdbc).queryForObject(anyString(), anyMap(),
				eq(Integer.class));

		// 1. Ready
		String userId = "U001";

		// 2. Do
		Integer result = repository.selectCsvOthers(userId);

		// 3. Check
		assertEquals(0, result);

		// 4. Log
		log.info("その他地域データ件数取得テスト成功(データなし): userId={}", userId);
	}

	@Test
	public void testSelectCsvLocationList正常() {
		// 0. Mock setup
		List<Map<String, Object>> mockResult = List.of(
				Map.of("location", "札幌"),
				Map.of("location", "東京"),
				Map.of("location", "大阪"));

		doReturn(mockResult).when(jdbc).queryForList(anyString(), anyMap());

		// 1. Ready
		String userId = "U001";

		// 2. Do
		List<String> result = repository.selectCsvLocationList(userId);

		// 3. Check
		assertEquals(3, result.size());
		assertEquals("札幌", result.get(0));
		assertEquals("東京", result.get(1));
		assertEquals("大阪", result.get(2));

		// 4. Log
		log.info("地域リスト取得テスト成功: userId={}, locations={}", userId, result);
	}

	@Test
	public void testSelectCsvLocationList空異常() {
		// 0. Mock setup
		List<Map<String, Object>> mockResult = List.of(); // 空のリストをモック

		doReturn(mockResult).when(jdbc).queryForList(anyString(), anyMap());

		// 1. Ready
		String userId = "U001";

		// 2. Do
		List<String> result = repository.selectCsvLocationList(userId);

		// 3. Check
		assertTrue(result.isEmpty());

		// 4. Log
		log.info("地域リスト取得テスト成功(データなし): userId={}, locations={}", userId, result);
	}

}
