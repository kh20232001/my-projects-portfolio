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
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import com.api.domain.models.dbdata.JobSearchReportData;
import com.api.jobpal.common.base.DBDataConversion;

@RunWith(SpringRunner.class)
@SpringBootTest
class JobSearchReportRepositoryTest {
	@Autowired
	private JobSearchReportRepository repository;

	@SpyBean
	private NamedParameterJdbcTemplate jdbc;

	@SpyBean
	private DBDataConversion dbdc;

	@Autowired
	private Logger log;

	@Test
	public void testInsertJobSearchReport正常() {
		// 0. Mock setup
		JobSearchReportData mockData = new JobSearchReportData();
		mockData.setReportContent("Test Report Content");
		mockData.setResult("Success");
		mockData.setJobSearchId("JS001");

		doReturn(1).when(jdbc).update(anyString(), anyMap());

		// 1. Ready
		JobSearchReportData jobSearchReportData = mockData;

		// 2. Do
		Boolean result = repository.insertJobSearchReport(jobSearchReportData);

		// 3. Check
		assertTrue(result);

		// 4. Log
		log.info("就職活動報告登録テスト成功");
	}

	@Test
	public void testInsertJobSearchReport_Failure() {
		// 0. Mock setup

		JobSearchReportData mockData = new JobSearchReportData();
		mockData.setReportContent("Test Report Content");
		mockData.setResult("Success");
		mockData.setJobSearchId("JS001");

		doReturn(0).when(jdbc).update(anyString(), anyMap());

		// 1. Ready
		JobSearchReportData jobSearchReportData = mockData;

		// 2. Do
		Boolean result = repository.insertJobSearchReport(jobSearchReportData);

		// 3. Check
		assertFalse(result);

		// 4. Log
		log.info("就職活動報告登録テスト失敗");
	}

	@Test
	public void testUpdateJobSearchReport正常() {
		// 0. Mock setup

		JobSearchReportData mockData = new JobSearchReportData();
		mockData.setReportContent("Updated Report Content");
		mockData.setResult("Failure");
		mockData.setJobSearchId("JS001");

		doReturn(1).when(jdbc).update(anyString(), anyMap());

		// 1. Ready
		JobSearchReportData jobSearchReportData = mockData;

		// 2. Do
		Boolean result = repository.updateJobSearchReport(jobSearchReportData);

		// 3. Check
		assertTrue(result);

		// 4. Log
		log.info("就職活動報告更新テスト成功");
	}

	@Test
	public void testUpdateJobSearchReport異常() {

		JobSearchReportData mockData = new JobSearchReportData();
		mockData.setReportContent("Updated Report Content");
		mockData.setResult("Failure");
		mockData.setJobSearchId("JS001");

		doReturn(0).when(jdbc).update(anyString(), anyMap());

		// 1. Ready
		JobSearchReportData jobSearchReportData = mockData;

		// 2. Do
		Boolean result = repository.updateJobSearchReport(jobSearchReportData);

		// 3. Check
		assertFalse(result);

		// 4. Log
		log.info("就職活動報告更新テスト失敗");
	}

}
