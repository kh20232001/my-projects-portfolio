package com.api.domain.repositories;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.sql.Time;
import java.sql.Timestamp;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import com.api.domain.models.dbdata.JobSearchApplicationData;
import com.api.jobpal.common.base.DBDataConversion;

@RunWith(SpringRunner.class)
@SpringBootTest
class JobSearchApplicationRepositoryTest {

	@Autowired
	private JobSearchApplicationRepository repository;

	@SpyBean
	private NamedParameterJdbcTemplate jdbc;

	@SpyBean
	private DBDataConversion dbdc;

	@Autowired
	private Logger log;

	private final Timestamp TEST_TIMESTAMP = Timestamp.valueOf("2024-01-01 00:00:00");
	private final Time TEST_TIME = Time.valueOf("00:00:00");

	@Test
	public void testInsertJobSearchApplication正常() {
		// 0. Mock setup
		Timestamp now = TEST_TIMESTAMP;
		JobSearchApplicationData mockData = new JobSearchApplicationData();
		mockData.setStartTime(now);
		mockData.setCompanyName("Test Company");
		mockData.setEventCategory("1");
		mockData.setLocationType("1");
		mockData.setLocation("Zoom");
		mockData.setSchoolCheckFlag(true);
		mockData.setSchoolCheckedFlag(false);
		mockData.setTardinessAbsenceType("0");
		mockData.setTardyLeaveTime(null);
		mockData.setEndTime(now);
		mockData.setRemarks("First Interview");
		mockData.setJobSearchId("JS_2024_00008");
		doReturn(1).when(jdbc).update(anyString(), anyMap());

		// 1. Ready
		JobSearchApplicationData jobSearchApplicationData = mockData;

		// 2. Do
		Boolean result = repository.insertJobSearchApplication(jobSearchApplicationData);

		// 3. Check
		assertTrue(result);

		// 4. Log
		log.info("就職活動申請登録テスト成功");
	}

	@Test
	public void testInsertJobSearchApplication異常() {
		// 0. Mock setup
		Timestamp now = TEST_TIMESTAMP;
		JobSearchApplicationData mockData = new JobSearchApplicationData();
		mockData.setStartTime(now);
		mockData.setCompanyName("Test Company");
		mockData.setEventCategory("Interview");
		mockData.setLocationType("Online");
		mockData.setLocation("Zoom");
		mockData.setSchoolCheckFlag(true);
		mockData.setSchoolCheckedFlag(false);
		mockData.setTardinessAbsenceType("None");
		mockData.setTardyLeaveTime(null);
		mockData.setEndTime(now);
		mockData.setRemarks("First Interview");
		mockData.setJobSearchId("JS001");
		doReturn(0).when(jdbc).update(anyString(), anyMap());

		// 1. Ready
		JobSearchApplicationData jobSearchApplicationData = mockData;

		// 2. Do
		Boolean result = repository.insertJobSearchApplication(jobSearchApplicationData);

		// 3. Check
		assertFalse(result);

		// 4. Log
		log.info("就職活動申請登録テスト失敗");
	}

	@Test
	public void testUpdateJobSearchApplication正常() {
		// 0. Mock setup
		Timestamp now = TEST_TIMESTAMP;

		JobSearchApplicationData mockData = new JobSearchApplicationData();
		mockData.setStartTime(now);
		mockData.setCompanyName("Updated Company");
		mockData.setEventCategory("Updated Interview");
		mockData.setLocationType("Offline");
		mockData.setLocation("Office");
		mockData.setSchoolCheckFlag(true);
		mockData.setSchoolCheckedFlag(true);
		mockData.setTardinessAbsenceType("Late");
		mockData.setTardyLeaveTime(TEST_TIME);
		mockData.setEndTime(now);
		mockData.setRemarks("Updated Remarks");
		mockData.setJobSearchId("JS001");

		doReturn(1).when(jdbc).update(anyString(), anyMap());

		// 1. Ready
		JobSearchApplicationData jobSearchApplicationData = mockData;

		// 2. Do
		Boolean result = repository.updateJobSearchApplication(jobSearchApplicationData);

		// 3. Check
		assertTrue(result);

		// 4. Log
		log.info("就職活動申請更新テスト成功");
	}

	@Test
	public void testUpdateJobSearchApplication異常() {
		// 0. Mock setup
		Timestamp now = TEST_TIMESTAMP;

		JobSearchApplicationData mockData = new JobSearchApplicationData();
		mockData.setStartTime(now);
		mockData.setCompanyName("Updated Company");
		mockData.setEventCategory("Updated Interview");
		mockData.setLocationType("Offline");
		mockData.setLocation("Office");
		mockData.setSchoolCheckFlag(true);
		mockData.setSchoolCheckedFlag(true);
		mockData.setTardinessAbsenceType("Late");
		mockData.setTardyLeaveTime(TEST_TIME);
		mockData.setEndTime(now);
		mockData.setRemarks("Updated Remarks");
		mockData.setJobSearchId("JS001");

		doReturn(0).when(jdbc).update(anyString(), anyMap());

		// 1. Ready
		JobSearchApplicationData jobSearchApplicationData = mockData;

		// 2. Do
		Boolean result = repository.updateJobSearchApplication(jobSearchApplicationData);

		// 3. Check
		assertFalse(result);

		// 4. Log
		log.info("就職活動申請更新テスト失敗");
	}

	@Test
	public void testUpdateSchoolCheckedOne正常() {
		// 0. Mock setup
		doReturn(1).when(jdbc).update(anyString(), anyMap());

		// 1. Ready
		String jobSearchId = "JS001";

		// 2. Do
		Boolean result = repository.updateSchoolCheckedOne(jobSearchId);

		// 3. Check
		assertTrue(result);

		// 4. Log
		log.info("学校とりまとめ名簿更新テスト成功");
	}

	@Test
	public void testUpdateSchoolCheckedOne異常() {
		// 0. Mock setup
		doReturn(0).when(jdbc).update(anyString(), anyMap());

		// 1. Ready
		String jobSearchId = "JS001";

		// 2. Do
		Boolean result = repository.updateSchoolCheckedOne(jobSearchId);

		// 3. Check
		assertFalse(result);

		// 4. Log
		log.info("学校とりまとめ名簿更新テスト成功");
	}

	@Test
	public void testSelectEventCategory正常() {
		// モックの結果データを設定
		String expectedEventCategory = "面接";
		doReturn(expectedEventCategory).when(jdbc).queryForObject(
				anyString(),
				anyMap(),
				eq(String.class));

		// テスト実行
		String result = repository.selectEventCategory("JS001");

		// 検証
		assertNotNull(result);
		assertEquals(expectedEventCategory, result);
		log.info("testSelectEventCategory正常テスト成功: result={}", result);
	}

	@Test
	public void testSelectEventCategoryデータなし() {
		// モックの振る舞いを設定（EmptyResultDataAccessExceptionをスロー）
		doThrow(EmptyResultDataAccessException.class).when(jdbc).queryForObject(
				anyString(),
				anyMap(),
				eq(String.class));

		// テスト実行
		String result = repository.selectEventCategory("JS002");

		// 検証
		assertNull(result);
		log.info("testSelectEventCategoryデータなしテスト成功: result=null");
	}

}
