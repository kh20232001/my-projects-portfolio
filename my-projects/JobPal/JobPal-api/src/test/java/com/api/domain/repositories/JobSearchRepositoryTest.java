package com.api.domain.repositories;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

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
class JobSearchRepositoryTest {
	@Autowired
	private JobSearchRepository repository;

	@SpyBean
	private NamedParameterJdbcTemplate jdbc;

	@SpyBean
	private DBDataConversion dbdc;

	@Autowired
	private Logger log;

	@Test
	public void testSelectJobSearchStatus正常() {
		// 0. Mock setup
		String expectedStatus = "11";
		doReturn(expectedStatus).when(jdbc).queryForObject(anyString(), anyMap(), eq(String.class));

		// 1. Ready
		String jobSearchId = "JS001";

		// 2. Do
		String result = repository.selectJobSearchStatus(jobSearchId);

		// 3. Check
		assertEquals(expectedStatus, result);

		// 4. Log
		log.info("就職活動状態取得テスト成功");
	}

	@Test
	public void testSelectJobSearchStatus未取得() {
		// 0. Mock setup
		doThrow(EmptyResultDataAccessException.class).when(jdbc).queryForObject(anyString(), anyMap(),
				eq(String.class));
		// 1. Ready
		String jobSearchId = "JS001";

		// 2. Do
		String result = repository.selectJobSearchStatus(jobSearchId);

		// 3. Check
		assertNull(result);

		// 4. Log
		log.info("就職活動状態取得テスト失敗");
	}

	@Test
	public void testInsertJobSearch正常() {
		// 0. Mock setup
		doReturn(1).when(jdbc).update(anyString(), anyMap());

		// 1. Ready
		String jobSearchId = "JS001";
		String userId = "U001";

		// 2. Do
		Boolean result = repository.insertJobSearch(jobSearchId, userId);

		// 3. Check
		assertTrue(result);

		// 4. Log
		log.info("就職活動登録テスト成功");
	}

	@Test
	public void testInsertJobSearch異常() {
		// 0. Mock setup
		doReturn(0).when(jdbc).update(anyString(), anyMap());

		// 1. Ready
		String jobSearchId = "JS001";
		String userId = "U001";

		// 2. Do
		Boolean result = repository.insertJobSearch(jobSearchId, userId);

		// 3. Check
		assertFalse(result);

		// 4. Log
		log.info("就職活動登録テスト失敗");
	}

	@Test
	public void testUpdateJobSearchStatus正常() {
		// 0. Mock setup
		doReturn(1).when(jdbc).update(anyString(), anyMap());

		// 1. Ready
		String jobSearchStatus = "12";
		String jobSearchId = "JS001";

		// 2. Do
		Boolean result = repository.updateJobSearchStatus(jobSearchStatus, jobSearchId);

		// 3. Check
		assertTrue(result);

		// 4. Log
		log.info("就職活動状態更新テスト成功");
	}

	@Test
	public void testUpdateJobSearchStatus異常() {
		// 0. Mock setup
		doReturn(0).when(jdbc).update(anyString(), anyMap());

		// 1. Ready
		String jobSearchStatus = "12";
		String jobSearchId = "JS001";

		// 2. Do
		Boolean result = repository.updateJobSearchStatus(jobSearchStatus, jobSearchId);

		// 3. Check
		assertFalse(result);

		// 4. Log
		log.info("就職活動状態更新テスト失敗");
	}

	@Test
	public void testDeleteJobSearchStatus正常() {
		// 0. Mock setup
		doReturn(1).when(jdbc).update(anyString(), anyMap());

		// 1. Ready
		String jobSearchId = "JS001";

		// 2. Do
		Boolean result = repository.deleteJobSearchStatus(jobSearchId);

		// 3. Check
		assertTrue(result);

		// 4. Log
		log.info("就職活動削除状態更新テスト成功");
	}

	@Test
	public void testDeleteJobSearchStatus異常() {
		// 0. Mock setup
		doReturn(0).when(jdbc).update(anyString(), anyMap());

		// 1. Ready
		String jobSearchId = "JS001";

		// 2. Do
		Boolean result = repository.deleteJobSearchStatus(jobSearchId);

		// 3. Check
		assertFalse(result);

		// 4. Log
		log.info("就職活動削除状態更新テスト失敗");
	}

	@Test
	public void testGetJobSearchId_Success() {
		// 0. Mock setup
		String expectedId = "JS002";
		doReturn(expectedId).when(dbdc).getLargerId(anyString(), anyString());

		// 1. Ready
		// 特別な準備は必要なし

		// 2. Do
		String result = repository.getJobSearchId();

		// 3. Check
		assertEquals(expectedId, result);

		// 4. Log
		log.info("新規就職活動ID生成テスト成功");
	}

	@Test
	public void testSelectJobSearchUserId正常() {
		// モックデータの準備
		String jobSearchId = "JS_2024_00001";
		String expectedUserId = "user123";

		// モックの設定
		doReturn(expectedUserId).when(jdbc).queryForObject(anyString(), anyMap(), eq(String.class));

		// テスト対象のメソッドを実行
		String result = repository.selectJobSearchUserId(jobSearchId);

		// 結果のアサーション
		assertNotNull(result);
		assertEquals(expectedUserId, result);

		log.info("就職活動ユーザ取得テスト成功: jobSearchId={}, result={}", jobSearchId, result);
	}

	@Test
	public void testSelectJobSearchUserId_NoData() {
		// モックデータの準備
		String jobSearchId = "JS001";

		// モックの設定
		doThrow(new EmptyResultDataAccessException(1)).when(jdbc).queryForObject(anyString(), anyMap(),
				eq(String.class));

		// テスト対象のメソッドを実行
		String result = repository.selectJobSearchUserId(jobSearchId);

		// 結果のアサーション
		assertNull(result);

		log.info("就職活動ユーザ取得テスト成功（データなし）: jobSearchId={}, result={}", jobSearchId, result);
	}

}
