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

import com.api.domain.models.dbdata.ExamReportData;
import com.api.jobpal.common.base.DBDataConversion;

@RunWith(SpringRunner.class)
@SpringBootTest
class ExamReportRepositoryTest {
	@Autowired
	private ExamReportRepository repository;

	@SpyBean
	private NamedParameterJdbcTemplate jdbc;

	@SpyBean
	private DBDataConversion dbdc;

	@Autowired
	private Logger log;

	@Test
	public void testInsertExamReport正常() {
		// 0. Mock setup

		ExamReportData mockData = new ExamReportData();
		mockData.setExamOpponentCount(5);
		mockData.setExamOpponentPosition("Manager");
		mockData.setExamCount("2");
		mockData.setExamType("Technical");
		mockData.setExamContent("Programming Test");
		mockData.setImpressions("It was challenging.");
		mockData.setJobSearchId("JS001");

		doReturn(1).when(jdbc).update(anyString(), anyMap());

		// 1. Ready
		ExamReportData examReportData = mockData;

		// 2. Do
		Boolean result = repository.insertExamReport(examReportData);

		// 3. Check
		assertTrue(result);

		// 4. Log
		log.info("受験報告登録テスト成功");
	}

	@Test
	public void testInsertExamReport異常() {
		// 0. Mock setup
		ExamReportData mockData = new ExamReportData();
		mockData.setExamOpponentCount(5);
		mockData.setExamOpponentPosition("Manager");
		mockData.setExamCount("2");
		mockData.setExamType("Technical");
		mockData.setExamContent("Programming Test");
		mockData.setImpressions("It was challenging.");
		mockData.setJobSearchId("JS001");

		doReturn(0).when(jdbc).update(anyString(), anyMap());

		// 1. Ready
		ExamReportData examReportData = mockData;

		// 2. Do
		Boolean result = repository.insertExamReport(examReportData);

		// 3. Check
		assertFalse(result);

		// 4. Log
		log.info("受験報告登録テスト失敗");
	}

	@Test
	public void testUpdateExamReport正常() {
		// 0. Mock setup

		ExamReportData mockData = new ExamReportData();
		mockData.setExamOpponentCount(5);
		mockData.setExamOpponentPosition("Manager");
		mockData.setExamCount("2");
		mockData.setExamType("Technical");
		mockData.setExamContent("Programming Test");
		mockData.setImpressions("It was challenging.");
		mockData.setJobSearchId("JS001");

		doReturn(1).when(jdbc).update(anyString(), anyMap());

		// 1. Ready
		ExamReportData examReportData = mockData;

		// 2. Do
		Boolean result = repository.updateExamReport(examReportData);

		// 3. Check
		assertTrue(result);

		// 4. Log
		log.info("受験報告更新テスト成功");
	}

	@Test
	public void testUpdateExamReport異常() {
		// 0. Mock setup
		ExamReportData mockData = new ExamReportData();
		mockData.setExamOpponentCount(5);
		mockData.setExamOpponentPosition("Manager");
		mockData.setExamCount("2");
		mockData.setExamType("Technical");
		mockData.setExamContent("Programming Test");
		mockData.setImpressions("It was challenging.");
		mockData.setJobSearchId("JS001");

		doReturn(0).when(jdbc).update(anyString(), anyMap());

		// 1. Ready
		ExamReportData examReportData = mockData;

		// 2. Do
		Boolean result = repository.updateExamReport(examReportData);

		// 3. Check
		assertFalse(result);

		// 4. Log
		log.info("受験報告更新テスト失敗");
	}

}
