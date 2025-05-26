package com.api.domain.services;

import static org.junit.jupiter.api.Assertions.*;
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
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.api.domain.models.dbdata.JobSearchDashBoardDetailData;
import com.api.domain.models.displaydata.DetailDisplayData;
import com.api.domain.models.forms.DetailFormForReport;
import com.api.domain.models.forms.DetailFormForUpdate;
import com.api.domain.repositories.ExamReportRepository;
import com.api.domain.repositories.JobSearchApplicationRepository;
import com.api.domain.repositories.JobSearchDashBoardRepository;
import com.api.domain.repositories.JobSearchReportRepository;
import com.api.domain.repositories.JobSearchRepository;
import com.api.domain.repositories.UserRepository;
import com.api.jobpal.common.base.DBDataConversion;

@RunWith(SpringRunner.class)
@SpringBootTest
class DashBoardDetailServiceTest {

	@Autowired
	private DashBoardDetailService target;

	// 下の階層をモック化
	@SpyBean
	private JobSearchDashBoardRepository dashBoardRepository;

	@SpyBean
	private JobSearchRepository jobSearchRepository;

	@SpyBean
	private JobSearchApplicationRepository applicationRepository;

	@SpyBean
	private ExamReportRepository jobExamRepository;

	@SpyBean
	private JobSearchReportRepository jobSearchReportRepository;

	@SpyBean
	private UserRepository newUserRepository;

	@SpyBean
	private NotificationService notificationService;

	@SpyBean
	private DBDataConversion dbdc;

	@SpyBean
	private RestTemplate restTemplate;

	@Autowired
	private Logger log;

	@Test
	void 就職活動申請詳細データの取得テスト() {
		// 0.モック設定
		JobSearchDashBoardDetailData mockDetailData = new JobSearchDashBoardDetailData();
		mockDetailData.setUserName("John Doe");
		mockDetailData.setJobSearchId("JS_2024_00001");
		mockDetailData.setStudentUserId("student@hcs.ac.jp");
		mockDetailData.setJobSearchStatus("33");
		mockDetailData.setJobApplicationId("JA_2024_00001");
		mockDetailData.setStartTime(Timestamp.valueOf("2024-03-01 09:00:00"));
		mockDetailData.setCompanyName("ABC株式会社");
		mockDetailData.setEventCategory("2");
		mockDetailData.setLocationType("1");
		mockDetailData.setLocation("東京都港区");
		mockDetailData.setSchoolCheckFlag(false);
		mockDetailData.setSchoolCheckedFlag(null);
		mockDetailData.setTardinessAbsenceType("0");
		mockDetailData.setTardyLeaveTime(null);
		mockDetailData.setEndTime(Timestamp.valueOf("2024-03-01 17:00:00"));
		mockDetailData.setRemarks(null);
		mockDetailData.setJobReportId("JR_2024_00001");
		mockDetailData.setReportContent("面接結果: 合格");
		mockDetailData.setResult("2");
		mockDetailData.setExamReportId("ER_2024_00001");
		mockDetailData.setExamOpponentCount(5);
		mockDetailData.setExamOpponentPosition("マネージャー");
		mockDetailData.setExamCount("1");
		mockDetailData.setExamType("1");
		mockDetailData.setExamContent("適性検査をしました。");
		mockDetailData.setImpressions("難しかったが良い経験だった。");
		mockDetailData.setDepartment("S");
		mockDetailData.setGrade(3);
		mockDetailData.setClassName("A2");
		mockDetailData.setAttendanceNumber("01");
		mockDetailData.setStudentId(30456789);
		mockDetailData.setMaxUpdatedAt(Timestamp.valueOf("2024-03-02 10:00:00"));

		doReturn(mockDetailData).when(dashBoardRepository).selectJobSearchDashBoardDetail("JR_2024_00001");
		doReturn("合格").when(restTemplate).postForObject(anyString(), any(), any());
		// 1.テストデータの準備
		String jobHantId = "JR_2024_00001";
		// 2.テスト対象メソッドの実行
		DetailDisplayData detailDisplayData = target.getDashBoardDetail(jobHantId);
		// 3.テスト結果確認
		assertEquals("John Doe", detailDisplayData.getUserName());
		assertEquals("JS_2024_00001", detailDisplayData.getJobHuntId());
		assertEquals("完了", detailDisplayData.getJobStateId());
		assertEquals("S3A2", detailDisplayData.getUserClass());
		assertEquals("01", detailDisplayData.getClassNumber());
		assertEquals(30456789, detailDisplayData.getSchoolNumber());
		assertEquals("ABC株式会社", detailDisplayData.getCompany());
		assertEquals("試験_面接", detailDisplayData.getEventCategory());
		assertEquals("東京都港区", detailDisplayData.getLocation());
		assertEquals(false, detailDisplayData.getSchoolCheck());
		assertEquals("03/01 09:00", detailDisplayData.getStartTime());
		assertEquals(Timestamp.valueOf("2024-03-02 10:00:00"), detailDisplayData.getUpdateTime());
		assertEquals("なし", detailDisplayData.getTardinessAbsenceType());
		assertEquals("面接結果: 合格", detailDisplayData.getReportContent());
		assertEquals("継続（合格）", detailDisplayData.getResult());
		assertEquals("5", detailDisplayData.getSupportedCnt());
		assertEquals("マネージャー", detailDisplayData.getJobTitle());
		assertEquals(1, detailDisplayData.getExamCategory());
		assertEquals("筆記・作文", detailDisplayData.getExamContent());
		assertEquals("適性検査をしました。", detailDisplayData.getEcaminationContent());
		assertEquals("難しかったが良い経験だった。", detailDisplayData.getThoughts());
		assertEquals(null, detailDisplayData.getRemarks());
		assertEquals("合格", detailDisplayData.getPredResult());
		assertEquals("03/01 17:00", detailDisplayData.getFinishTime());
		assertEquals(null, detailDisplayData.getTardyLeaveTime());
		// 4.ログ確認
		log.info("就職活動申請詳細データの取得テスト " + detailDisplayData.toString());
	}

	@Test
	void 就職活動申請詳細データの取得テストユーザ情報がない場合() {
		// 0.モック設定
		JobSearchDashBoardDetailData mockDetailData = new JobSearchDashBoardDetailData();
		mockDetailData.setUserName(null);
		mockDetailData.setJobSearchId("JS_2024_00001");
		mockDetailData.setStudentUserId(null);
		mockDetailData.setJobSearchStatus("33");
		mockDetailData.setJobApplicationId("JA_2024_00001");
		mockDetailData.setStartTime(Timestamp.valueOf("2024-03-01 09:00:00"));
		mockDetailData.setCompanyName("ABC株式会社");
		mockDetailData.setEventCategory("2");
		mockDetailData.setLocationType("1");
		mockDetailData.setLocation("東京都港区");
		mockDetailData.setSchoolCheckFlag(false);
		mockDetailData.setSchoolCheckedFlag(null);
		mockDetailData.setTardinessAbsenceType("0");
		mockDetailData.setTardyLeaveTime(null);
		mockDetailData.setEndTime(Timestamp.valueOf("2024-03-01 17:00:00"));
		mockDetailData.setRemarks(null);
		mockDetailData.setJobReportId("JR_2024_00001");
		mockDetailData.setReportContent("面接結果: 合格");
		mockDetailData.setResult("2");
		mockDetailData.setExamReportId("ER_2024_00001");
		mockDetailData.setExamOpponentCount(5);
		mockDetailData.setExamOpponentPosition("マネージャー");
		mockDetailData.setExamCount("1");
		mockDetailData.setExamType("1");
		mockDetailData.setExamContent("適性検査をしました。");
		mockDetailData.setImpressions("難しかったが良い経験だった。");
		mockDetailData.setDepartment(null);
		mockDetailData.setGrade(null);
		mockDetailData.setClassName(null);
		mockDetailData.setAttendanceNumber(null);
		mockDetailData.setStudentId(null);
		mockDetailData.setMaxUpdatedAt(Timestamp.valueOf("2024-03-02 10:00:00"));

		doReturn(mockDetailData).when(dashBoardRepository).selectJobSearchDashBoardDetail("JR_2024_00001");
		doReturn("合格").when(restTemplate).postForObject(anyString(), any(), any());
		// 1.テストデータの準備
		String jobHantId = "JR_2024_00001";
		// 2.テスト対象メソッドの実行
		DetailDisplayData detailDisplayData = target.getDashBoardDetail(jobHantId);
		// 3.テスト結果確認
		assertEquals(null, detailDisplayData.getUserName());
		assertEquals("JS_2024_00001", detailDisplayData.getJobHuntId());
		assertEquals("完了", detailDisplayData.getJobStateId());
		assertEquals(null, detailDisplayData.getUserClass());
		assertEquals(null, detailDisplayData.getClassNumber());
		assertEquals(null, detailDisplayData.getSchoolNumber());
		assertEquals("ABC株式会社", detailDisplayData.getCompany());
		assertEquals("試験_面接", detailDisplayData.getEventCategory());
		assertEquals("東京都港区", detailDisplayData.getLocation());
		assertEquals(false, detailDisplayData.getSchoolCheck());
		assertEquals("03/01 09:00", detailDisplayData.getStartTime());
		assertEquals(Timestamp.valueOf("2024-03-02 10:00:00"), detailDisplayData.getUpdateTime());
		assertEquals("なし", detailDisplayData.getTardinessAbsenceType());
		assertEquals("面接結果: 合格", detailDisplayData.getReportContent());
		assertEquals("継続（合格）", detailDisplayData.getResult());
		assertEquals("5", detailDisplayData.getSupportedCnt());
		assertEquals("マネージャー", detailDisplayData.getJobTitle());
		assertEquals(1, detailDisplayData.getExamCategory());
		assertEquals("筆記・作文", detailDisplayData.getExamContent());
		assertEquals("適性検査をしました。", detailDisplayData.getEcaminationContent());
		assertEquals("難しかったが良い経験だった。", detailDisplayData.getThoughts());
		assertEquals(null, detailDisplayData.getRemarks());
		assertEquals("合格", detailDisplayData.getPredResult());
		assertEquals("03/01 17:00", detailDisplayData.getFinishTime());
		assertEquals(null, detailDisplayData.getTardyLeaveTime());
		// 4.ログ確認
		log.info("就職活動申請詳細データの取得テストユーザ情報がない場合 " + detailDisplayData.toString());
	}

	@Test
	void 就職活動申請詳細データの取得テスト感想がない場合() {
		// 0.モック設定
		JobSearchDashBoardDetailData mockDetailData = new JobSearchDashBoardDetailData();
		mockDetailData.setUserName("John Doe");
		mockDetailData.setJobSearchId("JS_2024_00001");
		mockDetailData.setStudentUserId("student@hcs.ac.jp");
		mockDetailData.setJobSearchStatus("33");
		mockDetailData.setJobApplicationId("JA_2024_00001");
		mockDetailData.setStartTime(Timestamp.valueOf("2024-03-01 09:00:00"));
		mockDetailData.setCompanyName("ABC株式会社");
		mockDetailData.setEventCategory("2");
		mockDetailData.setLocationType("1");
		mockDetailData.setLocation("東京都港区");
		mockDetailData.setSchoolCheckFlag(false);
		mockDetailData.setSchoolCheckedFlag(null);
		mockDetailData.setTardinessAbsenceType("0");
		mockDetailData.setTardyLeaveTime(null);
		mockDetailData.setEndTime(Timestamp.valueOf("2024-03-01 17:00:00"));
		mockDetailData.setRemarks(null);
		mockDetailData.setJobReportId("JR_2024_00001");
		mockDetailData.setReportContent("面接結果: 合格");
		mockDetailData.setResult("2");
		mockDetailData.setExamReportId("ER_2024_00001");
		mockDetailData.setExamOpponentCount(5);
		mockDetailData.setExamOpponentPosition("マネージャー");
		mockDetailData.setExamCount("1");
		mockDetailData.setExamType("1");
		mockDetailData.setExamContent("適性検査をしました。");
		mockDetailData.setImpressions(null);
		mockDetailData.setDepartment("S");
		mockDetailData.setGrade(3);
		mockDetailData.setClassName("A2");
		mockDetailData.setAttendanceNumber("01");
		mockDetailData.setStudentId(30456789);
		mockDetailData.setMaxUpdatedAt(Timestamp.valueOf("2024-03-02 10:00:00"));

		doReturn(mockDetailData).when(dashBoardRepository).selectJobSearchDashBoardDetail("JR_2024_00001");
		doReturn("合格").when(restTemplate).postForObject(anyString(), any(), any());
		// 1.テストデータの準備
		String jobHantId = "JR_2024_00001";
		// 2.テスト対象メソッドの実行
		DetailDisplayData detailDisplayData = target.getDashBoardDetail(jobHantId);
		// 3.テスト結果確認
		assertEquals("John Doe", detailDisplayData.getUserName());
		assertEquals("JS_2024_00001", detailDisplayData.getJobHuntId());
		assertEquals("完了", detailDisplayData.getJobStateId());
		assertEquals("S3A2", detailDisplayData.getUserClass());
		assertEquals("01", detailDisplayData.getClassNumber());
		assertEquals(30456789, detailDisplayData.getSchoolNumber());
		assertEquals("ABC株式会社", detailDisplayData.getCompany());
		assertEquals("試験_面接", detailDisplayData.getEventCategory());
		assertEquals("東京都港区", detailDisplayData.getLocation());
		assertEquals(false, detailDisplayData.getSchoolCheck());
		assertEquals("03/01 09:00", detailDisplayData.getStartTime());
		assertEquals(Timestamp.valueOf("2024-03-02 10:00:00"), detailDisplayData.getUpdateTime());
		assertEquals("なし", detailDisplayData.getTardinessAbsenceType());
		assertEquals("面接結果: 合格", detailDisplayData.getReportContent());
		assertEquals("継続（合格）", detailDisplayData.getResult());
		assertEquals("5", detailDisplayData.getSupportedCnt());
		assertEquals("マネージャー", detailDisplayData.getJobTitle());
		assertEquals(1, detailDisplayData.getExamCategory());
		assertEquals("筆記・作文", detailDisplayData.getExamContent());
		assertEquals("適性検査をしました。", detailDisplayData.getEcaminationContent());
		assertEquals(null, detailDisplayData.getThoughts());
		assertEquals(null, detailDisplayData.getRemarks());
		assertEquals(null, detailDisplayData.getPredResult());
		assertEquals("03/01 17:00", detailDisplayData.getFinishTime());
		assertEquals(null, detailDisplayData.getTardyLeaveTime());
		// 4.ログ確認
		log.info("就職活動申請詳細データの取得テスト感想がない場合 " + detailDisplayData.toString());
	}

	@Test
	void 就職活動申請詳細データの取得テストAI機能の接続エラー場合() {
		// 0.モック設定
		JobSearchDashBoardDetailData mockDetailData = new JobSearchDashBoardDetailData();
		mockDetailData.setUserName("John Doe");
		mockDetailData.setJobSearchId("JS_2024_00001");
		mockDetailData.setStudentUserId("student@hcs.ac.jp");
		mockDetailData.setJobSearchStatus("33");
		mockDetailData.setJobApplicationId("JA_2024_00001");
		mockDetailData.setStartTime(Timestamp.valueOf("2024-03-01 09:00:00"));
		mockDetailData.setCompanyName("ABC株式会社");
		mockDetailData.setEventCategory("2");
		mockDetailData.setLocationType("1");
		mockDetailData.setLocation("東京都港区");
		mockDetailData.setSchoolCheckFlag(false);
		mockDetailData.setSchoolCheckedFlag(null);
		mockDetailData.setTardinessAbsenceType("0");
		mockDetailData.setTardyLeaveTime(null);
		mockDetailData.setEndTime(Timestamp.valueOf("2024-03-01 17:00:00"));
		mockDetailData.setRemarks(null);
		mockDetailData.setJobReportId("JR_2024_00001");
		mockDetailData.setReportContent("面接結果: 合格");
		mockDetailData.setResult("2");
		mockDetailData.setExamReportId("ER_2024_00001");
		mockDetailData.setExamOpponentCount(5);
		mockDetailData.setExamOpponentPosition("マネージャー");
		mockDetailData.setExamCount("1");
		mockDetailData.setExamType("1");
		mockDetailData.setExamContent("適性検査をしました。");
		mockDetailData.setImpressions("難しかったが良い経験だった。");
		mockDetailData.setDepartment("S");
		mockDetailData.setGrade(3);
		mockDetailData.setClassName("A2");
		mockDetailData.setAttendanceNumber("01");
		mockDetailData.setStudentId(30456789);
		mockDetailData.setMaxUpdatedAt(Timestamp.valueOf("2024-03-02 10:00:00"));

		doReturn(mockDetailData).when(dashBoardRepository).selectJobSearchDashBoardDetail(anyString());
		doThrow(new ResourceAccessException("接続エラー")).when(restTemplate).postForObject(anyString(), any(), any());

		// 1.テストデータの準備
		String jobHantId = "JR_2024_00001";
		// 2.テスト対象メソッドの実行
		DetailDisplayData detailDisplayData = target.getDashBoardDetail(jobHantId);
		// 3.テスト結果確認
		assertEquals("John Doe", detailDisplayData.getUserName());
		assertEquals("JS_2024_00001", detailDisplayData.getJobHuntId());
		assertEquals("完了", detailDisplayData.getJobStateId());
		assertEquals("S3A2", detailDisplayData.getUserClass());
		assertEquals("01", detailDisplayData.getClassNumber());
		assertEquals(30456789, detailDisplayData.getSchoolNumber());
		assertEquals("ABC株式会社", detailDisplayData.getCompany());
		assertEquals("試験_面接", detailDisplayData.getEventCategory());
		assertEquals("東京都港区", detailDisplayData.getLocation());
		assertEquals(false, detailDisplayData.getSchoolCheck());
		assertEquals("03/01 09:00", detailDisplayData.getStartTime());
		assertEquals(Timestamp.valueOf("2024-03-02 10:00:00"), detailDisplayData.getUpdateTime());
		assertEquals("なし", detailDisplayData.getTardinessAbsenceType());
		assertEquals("面接結果: 合格", detailDisplayData.getReportContent());
		assertEquals("継続（合格）", detailDisplayData.getResult());
		assertEquals("5", detailDisplayData.getSupportedCnt());
		assertEquals("マネージャー", detailDisplayData.getJobTitle());
		assertEquals(1, detailDisplayData.getExamCategory());
		assertEquals("筆記・作文", detailDisplayData.getExamContent());
		assertEquals("適性検査をしました。", detailDisplayData.getEcaminationContent());
		assertEquals("難しかったが良い経験だった。", detailDisplayData.getThoughts());
		assertEquals(null, detailDisplayData.getRemarks());
		assertEquals("エラーが発生しました。", detailDisplayData.getPredResult());
		assertEquals("03/01 17:00", detailDisplayData.getFinishTime());
		assertEquals(null, detailDisplayData.getTardyLeaveTime());
		// 4.ログ確認
		log.info("就職活動申請詳細データの取得テスト感想がない場合 " + detailDisplayData.toString());
	}

	@Test
	void 就職活動申請詳細データの取得テスト遅刻早退がある場合() {
		// 0.モック設定
		JobSearchDashBoardDetailData mockDetailData = new JobSearchDashBoardDetailData();
		mockDetailData.setUserName("John Doe");
		mockDetailData.setJobSearchId("JS_2024_00001");
		mockDetailData.setStudentUserId("student@hcs.ac.jp");
		mockDetailData.setJobSearchStatus("33");
		mockDetailData.setJobApplicationId("JA_2024_00001");
		mockDetailData.setStartTime(Timestamp.valueOf("2024-03-01 09:00:00"));
		mockDetailData.setCompanyName("ABC株式会社");
		mockDetailData.setEventCategory("2");
		mockDetailData.setLocationType("1");
		mockDetailData.setLocation("東京都港区");
		mockDetailData.setSchoolCheckFlag(false);
		mockDetailData.setSchoolCheckedFlag(null);
		mockDetailData.setTardinessAbsenceType("2");
		mockDetailData.setTardyLeaveTime(Time.valueOf("08:00:00"));
		mockDetailData.setEndTime(Timestamp.valueOf("2024-03-01 17:00:00"));
		mockDetailData.setRemarks("2024-03-01 研修");
		mockDetailData.setJobReportId("JR_2024_00001");
		mockDetailData.setReportContent("面接結果: 合格");
		mockDetailData.setResult("2");
		mockDetailData.setExamReportId("ER_2024_00001");
		mockDetailData.setExamOpponentCount(5);
		mockDetailData.setExamOpponentPosition("マネージャー");
		mockDetailData.setExamCount("1");
		mockDetailData.setExamType("1");
		mockDetailData.setExamContent("適性検査をしました。");
		mockDetailData.setImpressions("難しかったが良い経験だった。");
		mockDetailData.setDepartment("S");
		mockDetailData.setGrade(3);
		mockDetailData.setClassName("A2");
		mockDetailData.setAttendanceNumber("01");
		mockDetailData.setStudentId(30456789);
		mockDetailData.setMaxUpdatedAt(Timestamp.valueOf("2024-03-02 10:00:00"));

		doReturn(mockDetailData).when(dashBoardRepository).selectJobSearchDashBoardDetail(anyString());
		doReturn("合格").when(restTemplate).postForObject(anyString(), any(), any());
		// 1.テストデータの準備
		String jobHantId = "JR_2024_00001";
		// 2.テスト対象メソッドの実行
		DetailDisplayData detailDisplayData = target.getDashBoardDetail(jobHantId);
		// 3.テスト結果確認
		assertEquals("John Doe", detailDisplayData.getUserName());
		assertEquals("JS_2024_00001", detailDisplayData.getJobHuntId());
		assertEquals("完了", detailDisplayData.getJobStateId());
		assertEquals("S3A2", detailDisplayData.getUserClass());
		assertEquals("01", detailDisplayData.getClassNumber());
		assertEquals(30456789, detailDisplayData.getSchoolNumber());
		assertEquals("ABC株式会社", detailDisplayData.getCompany());
		assertEquals("試験_面接", detailDisplayData.getEventCategory());
		assertEquals("東京都港区", detailDisplayData.getLocation());
		assertEquals(false, detailDisplayData.getSchoolCheck());
		assertEquals("03/01 09:00", detailDisplayData.getStartTime());
		assertEquals(Timestamp.valueOf("2024-03-02 10:00:00"), detailDisplayData.getUpdateTime());
		assertEquals("早退", detailDisplayData.getTardinessAbsenceType());
		assertEquals("面接結果: 合格", detailDisplayData.getReportContent());
		assertEquals("継続（合格）", detailDisplayData.getResult());
		assertEquals("5", detailDisplayData.getSupportedCnt());
		assertEquals("マネージャー", detailDisplayData.getJobTitle());
		assertEquals(1, detailDisplayData.getExamCategory());
		assertEquals("筆記・作文", detailDisplayData.getExamContent());
		assertEquals("適性検査をしました。", detailDisplayData.getEcaminationContent());
		assertEquals("難しかったが良い経験だった。", detailDisplayData.getThoughts());
		assertEquals("研修", detailDisplayData.getRemarks());
		assertEquals("合格", detailDisplayData.getPredResult());
		assertEquals("03/01 17:00", detailDisplayData.getFinishTime());
		assertEquals("03/01 08:00", detailDisplayData.getTardyLeaveTime());
		// 4.ログ確認
		log.info("就職活動申請詳細データの取得テスト遅刻早退がある場合 " + detailDisplayData.toString());
	}

	@Test
	void 受験報告を更新テスト既存データがない場合() {
		// 0.モック設定
		try {
			doReturn(false).when(dbdc).existsJobSearchId(anyString(), anyString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		doReturn(true).when(jobExamRepository).insertExamReport(any());

		// 1.テストデータの準備
		DetailFormForUpdate form = new DetailFormForUpdate();
		form.setJobHuntId("JOB12345");
		form.setSupportedCnt(3);
		form.setJobTitle("ソフトウェアエンジニア");
		form.setExamCategory(1);
		form.setExamContentId("1");
		form.setExaminationContent("技術面接");
		form.setThoughts("良い経験になった");

		// 2.テスト対象メソッドの実行
		boolean result = target.updateDashBoardDetail(form);

		// 3.テスト結果確認
		assertTrue(result);

		// 4.ログ確認
		log.info("受験報告を更新テスト既存データがない場合 " + result);
	}

	@Test
	void 受験報告を更新テスト既存データのある場合() {
		// 0.モック設定
		try {
			doReturn(true).when(dbdc).existsJobSearchId(anyString(), anyString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		doReturn(true).when(jobExamRepository).updateExamReport(any());

		// 1.テストデータの準備
		DetailFormForUpdate form = new DetailFormForUpdate();
		form.setJobHuntId("JOB12345");
		form.setSupportedCnt(3);
		form.setJobTitle("ソフトウェアエンジニア");
		form.setExamCategory(1);
		form.setExamContentId("1");
		form.setExaminationContent("技術面接");
		form.setThoughts("良い経験になった");

		// 2.テスト対象メソッドの実行
		boolean result = target.updateDashBoardDetail(form);

		// 3.テスト結果確認
		assertTrue(result);

		// 4.ログ確認
		log.info("受験報告を更新テスト既存データのある場合 " + result);
	}

	@Test
	void 受験報告を更新テストExceptionの発生() {
		// 0.モック設定
		try {
			doReturn(true).when(dbdc).existsJobSearchId(anyString(), anyString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		doReturn(true).when(jobExamRepository).updateExamReport(any());

		// 1.テストデータの準備
		DetailFormForUpdate form = new DetailFormForUpdate();
		form.setJobHuntId(null);
		form.setSupportedCnt(3);
		form.setJobTitle("ソフトウェアエンジニア");
		form.setExamCategory(1);
		form.setExamContentId("1");
		form.setExaminationContent("技術面接");
		form.setThoughts("良い経験になった");

		// 2.テスト対象メソッドの実行
		boolean result = target.updateDashBoardDetail(form);

		// 3.テスト結果確認
		assertFalse(result);

		// 4.ログ確認
		log.info("受験報告を更新テストExceptionの発生 " + result);
	}

	@Test
	void 就職活動申請の状態を更新テスト担任承認待ち状態の承認ボタンの場合() {
		// 0.モック設定
		doReturn("test@hsc.ac.jp").when(jobSearchRepository).selectJobSearchUserId(anyString());
		doReturn("2").when(applicationRepository).selectEventCategory(anyString());
		doReturn(true).when(applicationRepository).updateSchoolCheckedOne(anyString());
		doReturn(true).when(jobSearchRepository).updateJobSearchStatus(anyString(), anyString());
		doReturn(true).when(notificationService).insertJobsearchNotification(anyString(), anyString());
		doReturn("test@hsc.ac.jp").when(newUserRepository).selectTeacherUserId(anyString());
		doReturn(true).when(notificationService).insertJobsearchNotification(anyString(), anyString(), anyBoolean());

		// 1.テストデータの準備
		String jobHuntId = "";
		String jobStateId = "担任承認待ち";
		Integer buttonId = 0;
		boolean schoolCheck = true;

		// 2.テスト対象メソッドの実行
		boolean result = target.jobStateUpdate(jobHuntId, jobStateId, buttonId, schoolCheck);

		// 3.テスト結果確認
		assertTrue(result);

		// 4.ログ確認
		log.info("就職活動申請の状態を更新テスト担任承認待ち状態の承認ボタンの場合 " + result);
	}

	@Test
	void 就職活動申請の状態を更新テスト受験報告承認待ち状態の承認ボタンの場合() {
		// 0.モック設定
		doReturn("test@hsc.ac.jp").when(jobSearchRepository).selectJobSearchUserId(anyString());
		doReturn("2").when(applicationRepository).selectEventCategory(anyString());
		doReturn(true).when(jobSearchRepository).updateJobSearchStatus(anyString(), anyString());
		doReturn(true).when(notificationService).insertJobsearchNotification(anyString(), anyString());

		// 1.テストデータの準備
		String jobHuntId = "";
		String jobStateId = "受験報告承認待ち";
		Integer buttonId = 0;
		boolean schoolCheck = false;

		// 2.テスト対象メソッドの実行
		boolean result = target.jobStateUpdate(jobHuntId, jobStateId, buttonId, schoolCheck);

		// 3.テスト結果確認
		assertTrue(result);

		// 4.ログ確認
		log.info("就職活動申請の状態を更新テスト受験報告承認待ち状態の承認ボタンの場合 " + result);
	}

	@Test
	void 就職活動申請の状態を更新テスト申請差戻し状態の承認ボタンの場合() {
		// 0.モック設定
		doReturn("test@hsc.ac.jp").when(jobSearchRepository).selectJobSearchUserId(anyString());
		doReturn("2").when(applicationRepository).selectEventCategory(anyString());
		doReturn(true).when(jobSearchRepository).updateJobSearchStatus(anyString(), anyString());
		doReturn(true).when(notificationService).insertJobsearchNotification(anyString(), anyString());

		// 1.テストデータの準備
		String jobHuntId = "";
		String jobStateId = "申請差戻し";
		Integer buttonId = 0;
		boolean schoolCheck = false;

		// 2.テスト対象メソッドの実行
		boolean result = target.jobStateUpdate(jobHuntId, jobStateId, buttonId, schoolCheck);

		// 3.テスト結果確認
		assertTrue(result);

		// 4.ログ確認
		log.info("就職活動申請の状態を更新テスト申請差戻し状態の承認ボタンの場合 " + result);
	}

	@Test
	void 就職活動申請の状態を更新テスト報告承認待ち状態の承認ボタンの場合() {
		// 0.モック設定
		doReturn("test@hsc.ac.jp").when(jobSearchRepository).selectJobSearchUserId(anyString());
		doReturn("2").when(applicationRepository).selectEventCategory(anyString());
		doReturn(true).when(jobSearchRepository).updateJobSearchStatus(anyString(), anyString());
		doReturn(true).when(notificationService).deleteJobsearchNotification(anyString());

		// 1.テストデータの準備
		String jobHuntId = "";
		String jobStateId = "報告承認待ち";
		Integer buttonId = 0;
		boolean schoolCheck = false;

		// 2.テスト対象メソッドの実行
		boolean result = target.jobStateUpdate(jobHuntId, jobStateId, buttonId, schoolCheck);

		// 3.テスト結果確認
		assertTrue(result);

		// 4.ログ確認
		log.info("就職活動申請の状態を更新テスト報告承認待ち状態の承認ボタンの場合 " + result);
	}

	@Test
	void 就職活動申請の状態を更新テスト完了状態の取り下げボタンの場合() {
		// 0.モック設定
		doReturn("test@hsc.ac.jp").when(jobSearchRepository).selectJobSearchUserId(anyString());
		doReturn("2").when(applicationRepository).selectEventCategory(anyString());
		doReturn(true).when(jobSearchRepository).updateJobSearchStatus(anyString(), anyString());
		doReturn(true).when(notificationService).insertJobsearchNotification(anyString(), anyString());

		// 1.テストデータの準備
		String jobHuntId = "";
		String jobStateId = "完了";
		Integer buttonId = 1;
		boolean schoolCheck = false;

		// 2.テスト対象メソッドの実行
		boolean result = target.jobStateUpdate(jobHuntId, jobStateId, buttonId, schoolCheck);

		// 3.テスト結果確認
		assertTrue(result);

		// 4.ログ確認
		log.info("就職活動申請の状態を更新テスト完了状態の取り下げボタンの場合 " + result);
	}

	@Test
	void 就職活動申請の状態を更新テスト完了状態の差戻しボタンの場合() {
		// 0.モック設定
		doReturn("test@hsc.ac.jp").when(jobSearchRepository).selectJobSearchUserId(anyString());
		doReturn("2").when(applicationRepository).selectEventCategory(anyString());
		doReturn(true).when(jobSearchRepository).updateJobSearchStatus(anyString(), anyString());
		doReturn(true).when(notificationService).insertJobsearchNotification(anyString(), anyString());

		// 1.テストデータの準備
		String jobHuntId = "";
		String jobStateId = "完了";
		Integer buttonId = 2;
		boolean schoolCheck = false;

		// 2.テスト対象メソッドの実行
		boolean result = target.jobStateUpdate(jobHuntId, jobStateId, buttonId, schoolCheck);

		// 3.テスト結果確認
		assertTrue(result);

		// 4.ログ確認
		log.info("就職活動申請の状態を更新テスト完了状態の取り下げボタンの場合 " + result);
	}

	@Test
	void 就職活動申請の状態を更新テスト承認待ち状態のコース担当承認ボタンの場合() {
		// 0.モック設定
		doReturn("test@hsc.ac.jp").when(jobSearchRepository).selectJobSearchUserId(anyString());
		doReturn("2").when(applicationRepository).selectEventCategory(anyString());
		doReturn(true).when(jobSearchRepository).updateJobSearchStatus(anyString(), anyString());
		doReturn(true).when(notificationService).insertJobsearchNotification(anyString(), anyString());

		// 1.テストデータの準備
		String jobHuntId = "";
		String jobStateId = "コース担当承認待ち";
		Integer buttonId = 3;
		boolean schoolCheck = false;

		// 2.テスト対象メソッドの実行
		boolean result = target.jobStateUpdate(jobHuntId, jobStateId, buttonId, schoolCheck);

		// 3.テスト結果確認
		assertTrue(result);

		// 4.ログ確認
		log.info("就職活動申請の状態を更新テストコース担当承認待ち状態のコース担当承認ボタンの場合 " + result);
	}

	@Test
	void 就職活動申請の状態を更新テスト承認待ち状態のコース担当承認ボタンで説明会の場合() {
		// 0.モック設定
		doReturn("test@hsc.ac.jp").when(jobSearchRepository).selectJobSearchUserId(anyString());
		doReturn("1").when(applicationRepository).selectEventCategory(anyString());
		doReturn(true).when(jobSearchRepository).updateJobSearchStatus(anyString(), anyString());
		doReturn(true).when(notificationService).insertJobsearchNotification(anyString(), anyString());

		// 1.テストデータの準備
		String jobHuntId = "";
		String jobStateId = "コース担当承認待ち";
		Integer buttonId = 3;
		boolean schoolCheck = false;

		// 2.テスト対象メソッドの実行
		boolean result = target.jobStateUpdate(jobHuntId, jobStateId, buttonId, schoolCheck);

		// 3.テスト結果確認
		assertTrue(result);

		// 4.ログ確認
		log.info("就職活動申請の状態を更新テストコース担当承認待ち状態のコース担当承認ボタンで説明会の場合 " + result);
	}

	@Test
	void 就職活動申請の状態を更新テストupdateJobSearchStatusが失敗したの場合() {
		// 0.モック設定
		doReturn("test@hsc.ac.jp").when(jobSearchRepository).selectJobSearchUserId(anyString());
		doReturn("2").when(applicationRepository).selectEventCategory(anyString());
		doReturn(false).when(jobSearchRepository).updateJobSearchStatus(anyString(), anyString());
		doReturn(true).when(notificationService).insertJobsearchNotification(anyString(), anyString());

		// 1.テストデータの準備
		String jobHuntId = "";
		String jobStateId = "受験報告承認待ち";
		Integer buttonId = 0;
		boolean schoolCheck = false;

		// 2.テスト対象メソッドの実行
		boolean result = target.jobStateUpdate(jobHuntId, jobStateId, buttonId, schoolCheck);

		// 3.テスト結果確認
		assertFalse(result);

		// 4.ログ確認
		log.info("就職活動申請の状態を更新テストupdateJobSearchStatusが失敗したの場合 " + result);
	}

	@Test
	void 就職活動申請の状態を更新テストinsertJobsearchNotificationが失敗したの場合() {
		// 0.モック設定
		doReturn("test@hsc.ac.jp").when(jobSearchRepository).selectJobSearchUserId(anyString());
		doReturn("2").when(applicationRepository).selectEventCategory(anyString());
		doReturn(true).when(jobSearchRepository).updateJobSearchStatus(anyString(), anyString());
		doReturn(false).when(notificationService).insertJobsearchNotification(anyString(), anyString());

		// 1.テストデータの準備
		String jobHuntId = "";
		String jobStateId = "受験報告承認待ち";
		Integer buttonId = 0;
		boolean schoolCheck = false;

		// 2.テスト対象メソッドの実行
		boolean result = target.jobStateUpdate(jobHuntId, jobStateId, buttonId, schoolCheck);

		// 3.テスト結果確認
		assertFalse(result);

		// 4.ログ確認
		log.info("就職活動申請の状態を更新テスト受験報告insertJobsearchNotificationが失敗したの場合 " + result);
	}

	@Test
	void 就職活動申請の状態を更新テストinsertJobsearchNotificationで失敗した場合() {
		// 0.モック設定
		doReturn("test@hsc.ac.jp").when(jobSearchRepository).selectJobSearchUserId(anyString());
		doReturn("2").when(applicationRepository).selectEventCategory(anyString());
		doReturn(true).when(applicationRepository).updateSchoolCheckedOne(anyString());
		doReturn(true).when(jobSearchRepository).updateJobSearchStatus(anyString(), anyString());
		doReturn(false).when(notificationService).insertJobsearchNotification(anyString(), anyString());
		doReturn("test@hsc.ac.jp").when(newUserRepository).selectTeacherUserId(anyString());
		doReturn(true).when(notificationService).insertJobsearchNotification(anyString(), anyString(), anyBoolean());

		// 1.テストデータの準備
		String jobHuntId = "";
		String jobStateId = "担任承認待ち";
		Integer buttonId = 0;
		boolean schoolCheck = true;

		// 2.テスト対象メソッドの実行
		boolean result = target.jobStateUpdate(jobHuntId, jobStateId, buttonId, schoolCheck);

		// 3.テスト結果確認
		assertFalse(result);

		// 4.ログ確認
		log.info("就職活動申請の状態を更新テストinsertJobsearchNotificationで失敗した場合 " + result);
	}

	@Test
	void 就職活動申請の状態を更新テストExceptionが発生() {
		// 0.モック設定
		doReturn("test@hsc.ac.jp").when(jobSearchRepository).selectJobSearchUserId(anyString());
		doThrow(new IncorrectResultSizeDataAccessException(1)).when(applicationRepository)
				.updateSchoolCheckedOne(anyString());
		doReturn(true).when(jobSearchRepository).updateJobSearchStatus(anyString(), anyString());
		doReturn(false).when(notificationService).insertJobsearchNotification(anyString(), anyString());
		doReturn("test@hsc.ac.jp").when(newUserRepository).selectTeacherUserId(anyString());
		doReturn(true).when(notificationService).insertJobsearchNotification(anyString(), anyString(), anyBoolean());

		// 1.テストデータの準備
		String jobHuntId = "";
		String jobStateId = "担任承認待ち";
		Integer buttonId = 0;
		boolean schoolCheck = true;

		// 2.テスト対象メソッドの実行
		boolean result = target.jobStateUpdate(jobHuntId, jobStateId, buttonId, schoolCheck);

		// 3.テスト結果確認
		assertFalse(result);

		// 4.ログ確認
		log.info("就職活動申請の状態を更新テストinsertJobsearchNotificationで失敗した場合 " + result);
	}

	@Test
	void 就職活動申請の削除テスト() {
		// 0.モック設定
		doReturn(true).when(jobSearchRepository).deleteJobSearchStatus(anyString());
		doReturn(true).when(notificationService).deleteJobsearchNotification(anyString());

		// 1.テストデータの準備
		String jobHuntId = "";

		// 2.テスト対象メソッドの実行
		boolean result = target.deleteOne(jobHuntId);

		// 3.テスト結果確認
		assertTrue(result);

		// 4.ログ確認
		log.info("就職活動申請の削除テスト " + result);
	}

	@Test
	void 就職活動申請の削除テストdeleteJobSearchStatusが失敗した場合() {
		// 0.モック設定
		doReturn(false).when(jobSearchRepository).deleteJobSearchStatus(anyString());
		doReturn(true).when(notificationService).deleteJobsearchNotification(anyString());

		// 1.テストデータの準備
		String jobHuntId = "";

		// 2.テスト対象メソッドの実行
		boolean result = target.deleteOne(jobHuntId);

		// 3.テスト結果確認
		assertFalse(result);

		// 4.ログ確認
		log.info("就職活動申請の削除テストdeleteJobSearchStatusが失敗した場合 " + result);
	}

	@Test
	void 就職活動申請の削除テストExceptionが発生() {
		// 0.モック設定
		doThrow(new IncorrectResultSizeDataAccessException(1)).when(jobSearchRepository)
				.deleteJobSearchStatus(anyString());
		doReturn(true).when(notificationService).deleteJobsearchNotification(anyString());

		// 1.テストデータの準備
		String jobHuntId = "";

		// 2.テスト対象メソッドの実行
		boolean result = target.deleteOne(jobHuntId);

		// 3.テスト結果確認
		assertFalse(result);

		// 4.ログ確認
		log.info("就職活動申請の削除テストdeleteJobSearchStatusが失敗した場合 " + result);
	}

	@Test
	void 受験報告の際にする状態の変更テスト() {
		// 0.モック設定
		doReturn("11").when(jobSearchRepository).selectJobSearchStatus(anyString());
		doReturn(true).when(jobSearchRepository).updateJobSearchStatus(anyString(), anyString());
		doReturn("test1@hsc.ac.jp").when(jobSearchRepository).selectJobSearchUserId(anyString());
		doReturn("test@hsc.ac.jp").when(newUserRepository).selectTeacherUserId(anyString());
		doReturn(true).when(notificationService).insertJobsearchNotification(anyString(), anyString());
		doReturn(true).when(notificationService).insertJobsearchNotification(anyString(), anyString(), anyBoolean());

		// 1.テストデータの準備
		String jobHuntId = "";

		// 2.テスト対象メソッドの実行
		boolean result = target.advancedJobStateId(jobHuntId);

		// 3.テスト結果確認
		assertTrue(result);

		// 4.ログ確認
		log.info("受験報告の際にする状態の変更テスト " + result);
	}

	@Test
	void 受験報告の際にする状態の変更テストupdateJobSearchStatusで失敗した場合() {
		// 0.モック設定
		doReturn("11").when(jobSearchRepository).selectJobSearchStatus(anyString());
		doReturn(false).when(jobSearchRepository).updateJobSearchStatus(anyString(), anyString());
		doReturn("test1@hsc.ac.jp").when(jobSearchRepository).selectJobSearchUserId(anyString());
		doReturn("test@hsc.ac.jp").when(newUserRepository).selectTeacherUserId(anyString());
		doReturn(true).when(notificationService).insertJobsearchNotification(anyString(), anyString());
		doReturn(true).when(notificationService).insertJobsearchNotification(anyString(), anyString(), anyBoolean());

		// 1.テストデータの準備
		String jobHuntId = "";

		// 2.テスト対象メソッドの実行
		boolean result = target.advancedJobStateId(jobHuntId);

		// 3.テスト結果確認
		assertFalse(result);

		// 4.ログ確認
		log.info("受験報告の際にする状態の変更テストupdateJobSearchStatusで失敗した場合 " + result);
	}

	@Test
	void 受験報告の際にする状態の変更テストステータスがnullの場合() {
		// 0.モック設定
		doReturn(null).when(jobSearchRepository).selectJobSearchStatus(anyString());
		doReturn(true).when(jobSearchRepository).updateJobSearchStatus(anyString(), anyString());
		doReturn("test1@hsc.ac.jp").when(jobSearchRepository).selectJobSearchUserId(anyString());
		doReturn("test@hsc.ac.jp").when(newUserRepository).selectTeacherUserId(anyString());
		doReturn(true).when(notificationService).insertJobsearchNotification(anyString(), anyString());

		// 1.テストデータの準備
		String jobHuntId = "";

		// 2.テスト対象メソッドの実行
		boolean result = target.advancedJobStateId(jobHuntId);

		// 3.テスト結果確認
		assertFalse(result);

		// 4.ログ確認
		log.info("受験報告の際にする状態の変更テストステータスがnullの場合 " + result);
	}

	@Test
	void 受験報告の際にする状態の変更テストIncorrectResultSizeDataAccessExceptionが発生() {
		// 0.モック設定
		doReturn("11").when(jobSearchRepository)
				.selectJobSearchStatus(anyString());
		doReturn(true).when(jobSearchRepository).updateJobSearchStatus(anyString(), anyString());
		doReturn("test1@hsc.ac.jp").when(jobSearchRepository).selectJobSearchUserId(anyString());
		doThrow(new IncorrectResultSizeDataAccessException(1)).when(newUserRepository).selectTeacherUserId(anyString());
		doReturn(true).when(notificationService).insertJobsearchNotification(anyString(), anyString());

		// 1.テストデータの準備
		String jobHuntId = "";

		// 2.テスト対象メソッドの実行
		boolean result = target.advancedJobStateId(jobHuntId);

		// 3.テスト結果確認
		assertFalse(result);

		// 4.ログ確認
		log.info("受験報告の際にする状態の変更テストIncorrectResultSizeDataAccessExceptionが発生 " + result);
	}

	@Test
	void 受験報告の際にする状態の変更テストExceptionが発生() {
		// 0.モック設定
		doReturn("11").when(jobSearchRepository)
				.selectJobSearchStatus(anyString());
		doReturn(true).when(jobSearchRepository).updateJobSearchStatus(anyString(), anyString());
		doReturn("test1@hsc.ac.jp").when(jobSearchRepository).selectJobSearchUserId(anyString());
		doReturn("test@hsc.ac.jp").when(newUserRepository).selectTeacherUserId(anyString());
		doThrow(new NullPointerException()).when(notificationService).insertJobsearchNotification(anyString(),
				anyString());

		// 1.テストデータの準備
		String jobHuntId = "";

		// 2.テスト対象メソッドの実行
		boolean result = target.advancedJobStateId(jobHuntId);

		// 3.テスト結果確認
		assertFalse(result);

		// 4.ログ確認
		log.info("受験報告の際にする状態の変更テストExceptionが発生 " + result);
	}

	@Test
	void 就職活動報告の更新テスト既存データが存在する場合() {
		// 0.モック設定
		try {
			doReturn(true).when(dbdc).existsJobSearchId(anyString(), anyString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		doReturn(true).when(jobSearchReportRepository).updateJobSearchReport(any());

		// 1.テストデータの準備
		DetailFormForReport form = new DetailFormForReport();
		form.setJobHuntId("JOB12345");
		form.setReportContent("面接結果: 合格");
		form.setResult("合格");

		// 2.テスト対象メソッドの実行
		boolean result = target.reportDashBoardDetail(form);

		// 3.テスト結果確認
		assertTrue(result);

		// 4.ログ確認
		log.info("就職活動報告の更新テスト既存データが存在する場合 " + result);
	}

	@Test
	void 就職活動報告の更新テスト既存データが存在しない場合() {
		// 0.モック設定
		try {
			doReturn(false).when(dbdc).existsJobSearchId(anyString(), anyString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		doReturn(true).when(jobSearchReportRepository).insertJobSearchReport(any());

		// 1.テストデータの準備
		DetailFormForReport form = new DetailFormForReport();
		form.setJobHuntId("JOB12345");
		form.setReportContent("面接結果: 合格");
		form.setResult("合格");

		// 2.テスト対象メソッドの実行
		boolean result = target.reportDashBoardDetail(form);

		// 3.テスト結果確認
		assertTrue(result);

		// 4.ログ確認
		log.info("就職活動報告の更新テスト既存データが存在しない場合 " + result);
	}

	@Test
	void 就職活動報告の更新テストIncorrectResultSizeDataAccessExceptionが発生() {
		// 0.モック設定
		try {
			doThrow(new NullPointerException()).when(dbdc).existsJobSearchId(anyString(), anyString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		doReturn(true).when(jobSearchReportRepository).insertJobSearchReport(any());

		// 1.テストデータの準備
		DetailFormForReport form = new DetailFormForReport();
		form.setJobHuntId("JOB12345");
		form.setReportContent("面接結果: 合格");
		form.setResult("合格");

		// 2.テスト対象メソッドの実行
		boolean result = target.reportDashBoardDetail(form);

		// 3.テスト結果確認
		assertFalse(result);

		// 4.ログ確認
		log.info("就職活動報告の更新テストIncorrectResultSizeDataAccessExceptionが発生 " + result);
	}

	@Test
	void 就職活動報告の更新テストExceptionが発生() {
		// 0.モック設定
		try {
			doThrow(new IncorrectResultSizeDataAccessException(1)).when(dbdc).existsJobSearchId(anyString(),
					anyString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		doReturn(true).when(jobSearchReportRepository).insertJobSearchReport(any());

		// 1.テストデータの準備
		DetailFormForReport form = new DetailFormForReport();
		form.setJobHuntId("JOB12345");
		form.setReportContent("面接結果: 合格");
		form.setResult("合格");

		// 2.テスト対象メソッドの実行
		boolean result = target.reportDashBoardDetail(form);

		// 3.テスト結果確認
		assertFalse(result);

		// 4.ログ確認
		log.info("就職活動報告の更新テストIncorrectResultSizeDataAccessExceptionが発生 " + result);
	}

}
