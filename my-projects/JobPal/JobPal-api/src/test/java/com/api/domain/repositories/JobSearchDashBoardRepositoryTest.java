package com.api.domain.repositories;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.sql.Time;
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

import com.api.domain.models.dbdata.JobSearchDashBoardDetailData;
import com.api.domain.models.entities.DashBoardEntity;
import com.api.jobpal.common.base.DBDataConversion;

@RunWith(SpringRunner.class)
@SpringBootTest
class JobSearchDashBoardRepositoryTest {
	@Autowired
	private JobSearchDashBoardRepository repository;

	@SpyBean
	private NamedParameterJdbcTemplate jdbc;

	@SpyBean
	private DBDataConversion dbdc;
	@SpyBean
	private CertificateIssuanceDashBoardRepository CIDBR;
	@Autowired
	private Logger log;

	@Test
	public void testSelectDashBoardAll正常管理者() {
		// モックの結果データ
		List<Map<String, Object>> mockResultList = new ArrayList<>();
		Map<String, Object> mockData = Map.ofEntries(
				Map.entry("user_name", "管理者"),
				Map.entry("job_search_status", "進行中"),
				Map.entry("start_time", Timestamp.valueOf("2024-01-01 09:00:00")),
				Map.entry("company_name", "株式会社テスト"),
				Map.entry("event_category", "説明会"),
				Map.entry("result", "合格"),
				Map.entry("school_check_flag", true),
				Map.entry("end_time", Timestamp.valueOf("2024-01-01 11:00:00")),
				Map.entry("job_search_id", "JS001"),
				Map.entry("department", "情報学科"),
				Map.entry("grade", 3),
				Map.entry("class_name", "A"),
				Map.entry("attendance_number", 1),
				Map.entry("user_id", "admin@hcs.ac.jp"),
				Map.entry("max_updated_at", Timestamp.valueOf("2024-01-01 12:00:00")));

		mockResultList.add(mockData);

		// モックの振る舞いを定義
		doReturn(mockResultList).when(jdbc).queryForList(anyString(), anyMap());
		doReturn(new String[] { "user_name", "job_search_status", "start_time", "company_name", "event_category",
				"result",
				"school_check_flag", "end_time", "job_search_id", "department", "grade", "class_name",
				"attendance_number",
				"user_id", "max_updated_at" })
						.when(dbdc).extractColumnNames(anyString());

		// テスト実行
		DashBoardEntity result = repository.selectDashBoardAll(true);

		// 検証
		assertNotNull(result);
		assertEquals(1, result.getDashBoardList().size());
		assertEquals("管理者", result.getDashBoardList().get(0).getUserName());
		log.info("selectDashBoardAll正常管理者テスト成功: result={}", result);
	}

	@Test
	public void testSelectDashBoardStudent正常() {
		// モックの結果データ
		List<Map<String, Object>> mockResultList = new ArrayList<>();
		Map<String, Object> mockData = Map.ofEntries(
				Map.entry("user_name", "学生A"),
				Map.entry("job_search_status", "内定"),
				Map.entry("start_time", Timestamp.valueOf("2024-02-01 09:00:00")),
				Map.entry("company_name", "テスト企業"),
				Map.entry("event_category", "面接"),
				Map.entry("result", "採用"),
				Map.entry("school_check_flag", false),
				Map.entry("end_time", Timestamp.valueOf("2024-02-01 10:00:00")),
				Map.entry("job_search_id", "JS002"),
				Map.entry("department", "情報工学科"),
				Map.entry("grade", 4),
				Map.entry("class_name", "B"),
				Map.entry("attendance_number", 10),
				Map.entry("user_id", "student@hcs.ac.jp"),
				Map.entry("max_updated_at", Timestamp.valueOf("2024-02-01 11:00:00")));

		mockResultList.add(mockData);

		// モックの振る舞いを定義
		doReturn(mockResultList).when(jdbc).queryForList(anyString(), anyMap());
		doReturn(new String[] { "user_name", "job_search_status", "start_time", "company_name", "event_category",
				"result",
				"school_check_flag", "end_time", "job_search_id", "department", "grade", "class_name",
				"attendance_number",
				"user_id", "max_updated_at" })
						.when(dbdc).extractColumnNames(anyString());

		// テスト実行
		DashBoardEntity result = repository.selectDashBoardStudent("student@hcs.ac.jp");

		// 検証
		assertNotNull(result);
		assertEquals(1, result.getDashBoardList().size());
		assertEquals("学生A", result.getDashBoardList().get(0).getUserName());
		log.info("selectDashBoardStudent正常テスト成功: result={}", result);
	}

	@Test
	public void testSelectJobSearchDashBoardDetail正常() {
		// モックの結果データ
		Map<String, Object> mockData = Map.ofEntries(
				Map.entry("user_name", "学生A"),
				Map.entry("job_search_id", "JS001"),
				Map.entry("student_user_id", "student001@hcs.ac.jp"),
				Map.entry("job_search_status", "内定"),
				Map.entry("job_application_id", "JA001"),
				Map.entry("start_time", Timestamp.valueOf("2024-01-01 09:00:00")),
				Map.entry("company_name", "テスト株式会社"),
				Map.entry("event_category", "面接"),
				Map.entry("location_type", "オンライン"),
				Map.entry("location", "リモート"),
				Map.entry("school_check_flag", true),
				Map.entry("school_checked_flag", false),
				Map.entry("tardiness_absence_type", "遅刻"),
				Map.entry("tardy_leave_time", Time.valueOf("09:15:00")),
				Map.entry("end_time", Timestamp.valueOf("2024-01-01 10:00:00")),
				Map.entry("remarks", "特になし"),
				Map.entry("job_report_id", "RP001"),
				Map.entry("report_content", "テスト内容"),
				Map.entry("result", "合格"),
				Map.entry("exam_report_id", "EX001"),
				Map.entry("exam_opponent_count", 5),
				Map.entry("exam_opponent_position", "マネージャー"),
				Map.entry("exam_count", "1回目"),
				Map.entry("exam_type", "技術試験"),
				Map.entry("exam_content", "プログラミングテスト"),
				Map.entry("impressions", "良い経験でした"),
				Map.entry("department", "情報学科"),
				Map.entry("grade", 4),
				Map.entry("class_name", "Aクラス"),
				Map.entry("attendance_number", "10"),
				Map.entry("student_id", 1001),
				Map.entry("max_updated_at", Timestamp.valueOf("2024-01-01 12:00:00")));
		String[] queryList = {
				"user_name",
				"job_search_id",
				"student_user_id",
				"job_search_status",
				"job_application_id",
				"start_time",
				"company_name",
				"event_category",
				"location_type",
				"location",
				"school_check_flag",
				"school_checked_flag",
				"tardiness_absence_type",
				"tardy_leave_time",
				"end_time",
				"remarks",
				"job_report_id",
				"report_content",
				"result",
				"exam_report_id",
				"exam_opponent_count",
				"exam_opponent_position",
				"exam_count",
				"exam_type",
				"exam_content",
				"impressions",
				"department",
				"grade",
				"class_name",
				"attendance_number",
				"student_id",
				"max_updated_at"
		};

		// モックの振る舞いを定義
		doReturn(List.of(mockData)).when(jdbc).queryForList(anyString(), anyMap());
		doReturn(queryList)
				.when(dbdc).extractColumnNames(anyString());

		// テスト実行
		JobSearchDashBoardDetailData result = repository.selectJobSearchDashBoardDetail("JS003");

		// 検証
		assertNotNull(result);
		assertEquals("学生A", result.getUserName());
		assertEquals("JS001", result.getJobSearchId());
		log.info("selectJobSearchDashBoardDetail正常テスト成功: result={}", result);
	}

}
