package com.api.domain.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.test.context.junit4.SpringRunner;

import com.api.domain.models.dbdata.UserData;
import com.api.domain.models.displaydata.NewJobSerachDisplay;
import com.api.domain.models.forms.NewJobApplicationForm;
import com.api.domain.repositories.JobSearchApplicationRepository;
import com.api.domain.repositories.JobSearchRepository;
import com.api.domain.repositories.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
class NewJobApplicationServiceTest {

	@Autowired
	private NewJobApplicationService target;

	@SpyBean
	private UserRepository newUserRepository;

	@SpyBean
	private JobSearchRepository jobApplicationRepository;

	@SpyBean
	private NotificationService notificationService;

	@SpyBean
	private JobSearchApplicationRepository jsar;

	@Autowired
	private Logger log;

	@Test
	void 全ユーザーの就職活動申請の取得テスト() {
		// 0.モック設定
		UserData mockData = new UserData.Builder()
				.setUserName("test")
				.setDepartment("T")
				.setGrade(1)
				.setClassName("S2")
				.setAttendanceNumber("01")
				.setStudentId(20240101)
				.build();

		doReturn(mockData).when(newUserRepository).selectTeacherStudentOne(anyString(), anyBoolean());

		// 1.テストデータ設定
		String userId = "test@hcs.ac.jp";

		// 2.テスト対象メソッド実行
		NewJobSerachDisplay result = target.getUserData(userId);

		// 3.テスト結果確認
		assertNotNull(result);
		assertEquals("test", result.getUserName());
		assertEquals("T1S2", result.getUserClass());
		assertEquals(1, result.getClassNumber());
		assertEquals(20240101, result.getSchoolNumber());

		// 4.ログ確認
		log.info("全ユーザーの就職活動申請の取得テスト " + result);
	}

	@Test
	void 就職活動申請の追加テスト() {

		// 0.モック設定
		String mockUserId = "test@hcs.ac.jp";

		String mockTeacherUserId = "teacher@hcs.ac.jp";

		doReturn(mockUserId).when(jobApplicationRepository).getJobSearchId();
		doReturn(true).when(jobApplicationRepository).insertJobSearch(anyString(), anyString());
		doReturn(true).when(jsar).insertJobSearchApplication(any());
		doReturn(mockTeacherUserId).when(newUserRepository).selectTeacherUserId(anyString());
		doReturn(true).when(notificationService).insertJobsearchNotification(anyString(), anyString(), anyBoolean());

		// 1.テストデータ設定
		NewJobApplicationForm form = new NewJobApplicationForm();
		form.setUserId("test@hcs.ac.jp");
		form.setCompany("testCompany");
		form.setEventCategory("testEventCategory");
		form.setLocationType("testLocationType");
		form.setStartTime("2023-10-01 10:00:00");
		form.setFinishTime("2023-10-01 12:00:00");
		form.setTardyLeaveTime("2023-10-01 11:00:00");
		form.setRemarks("testRemarks");
		form.setSchoolCheck(false);
		form.setTardinessAbsenceType("testTardinessAbsenceType");

		// 2.テスト対象メソッド実行
		boolean result = target.addNewJobApplication(form);

		// 3.テスト結果確認
		assertTrue(result);

		// 4.ログ確認
		log.info("就職活動申請の追加テスト " + result);
	}

	@Test
	void 就職活動申請の追加テストinsertJobSearchの失敗() {

		// 0.モック設定
		String mockUserId = "test@hcs.ac.jp";

		String mockTeacherUserId = "teacher@hcs.ac.jp";

		doReturn(mockUserId).when(jobApplicationRepository).getJobSearchId();
		doReturn(false).when(jobApplicationRepository).insertJobSearch(anyString(), anyString());
		doReturn(true).when(jsar).insertJobSearchApplication(any());
		doReturn(mockTeacherUserId).when(newUserRepository).selectTeacherUserId(anyString());
		doReturn(true).when(notificationService).insertJobsearchNotification(anyString(), anyString(), anyBoolean());

		// 1.テストデータ設定
		NewJobApplicationForm form = new NewJobApplicationForm();
		form.setUserId("test@hcs.ac.jp");
		form.setCompany("testCompany");
		form.setEventCategory("testEventCategory");
		form.setLocationType("testLocationType");
		form.setStartTime("2023-10-01 10:00:00");
		form.setFinishTime("2023-10-01 12:00:00");
		form.setTardyLeaveTime("2023-10-01 11:00:00");
		form.setRemarks("testRemarks");
		form.setSchoolCheck(false);
		form.setTardinessAbsenceType("testTardinessAbsenceType");

		// 2.テスト対象メソッド実行
		boolean result = target.addNewJobApplication(form);

		// 3.テスト結果確認
		assertFalse(result);

		// 4.ログ確認
		log.info("就職活動申請の追加テストinsertJobSearchの失敗 " + result);
	}

	@Test
	void 就職活動申請の追加テストinsertJobSearchApplicationの失敗() {

		// 0.モック設定
		String mockUserId = "test@hcs.ac.jp";

		String mockTeacherUserId = "teacher@hcs.ac.jp";

		doReturn(mockUserId).when(jobApplicationRepository).getJobSearchId();
		doReturn(true).when(jobApplicationRepository).insertJobSearch(anyString(), anyString());
		doReturn(false).when(jsar).insertJobSearchApplication(any());
		doReturn(mockTeacherUserId).when(newUserRepository).selectTeacherUserId(anyString());
		doReturn(true).when(notificationService).insertJobsearchNotification(anyString(), anyString(), anyBoolean());

		// 1.テストデータ設定
		NewJobApplicationForm form = new NewJobApplicationForm();
		form.setUserId("test@hcs.ac.jp");
		form.setCompany("testCompany");
		form.setEventCategory("testEventCategory");
		form.setLocationType("testLocationType");
		form.setStartTime("2023-10-01 10:00:00");
		form.setFinishTime("2023-10-01 12:00:00");
		form.setTardyLeaveTime("2023-10-01 11:00:00");
		form.setRemarks("testRemarks");
		form.setSchoolCheck(false);
		form.setTardinessAbsenceType("testTardinessAbsenceType");

		// 2.テスト対象メソッド実行
		boolean result = target.addNewJobApplication(form);

		// 3.テスト結果確認
		assertFalse(result);

		// 4.ログ確認
		log.info("就職活動申請の追加テストinsertJobSearchApplicationの失敗 " + result);
	}

	@Test
	void 就職活動申請の追加テストIncorrectResultSizeDataAccessExceptionの発生() {

		// 0.モック設定
		String mockUserId = "test@hcs.ac.jp";

		String mockTeacherUserId = "teacher@hcs.ac.jp";

		doReturn(mockUserId).when(jobApplicationRepository).getJobSearchId();
		doThrow(new IncorrectResultSizeDataAccessException(1)).when(jobApplicationRepository)
				.insertJobSearch(anyString(), anyString());
		doReturn(true).when(jsar).insertJobSearchApplication(any());
		doReturn(mockTeacherUserId).when(newUserRepository).selectTeacherUserId(anyString());
		doReturn(true).when(notificationService).insertJobsearchNotification(anyString(), anyString(), anyBoolean());

		// 1.テストデータ設定
		NewJobApplicationForm form = new NewJobApplicationForm();
		form.setUserId("test@hcs.ac.jp");
		form.setCompany("testCompany");
		form.setEventCategory("testEventCategory");
		form.setLocationType("testLocationType");
		form.setStartTime("2023-10-01 10:00:00");
		form.setFinishTime("2023-10-01 12:00:00");
		form.setTardyLeaveTime("2023-10-01 11:00:00");
		form.setRemarks("testRemarks");
		form.setSchoolCheck(false);
		form.setTardinessAbsenceType("testTardinessAbsenceType");

		// 2.テスト対象メソッド実行
		boolean result = target.addNewJobApplication(form);

		// 3.テスト結果確認
		assertFalse(result);

		// 4.ログ確認
		log.info("就職活動申請の追加テストIncorrectResultSizeDataAccessExceptionの発生 " + result);
	}

	@Test
	void 就職活動申請の追加テストDataAccessExceptionの発生() {

		// 0.モック設定
		String mockUserId = "test@hcs.ac.jp";

		String mockTeacherUserId = "teacher@hcs.ac.jp";

		doReturn(mockUserId).when(jobApplicationRepository).getJobSearchId();
		doThrow(new DataAccessException("Simulated database error") {
		}).when(jobApplicationRepository).insertJobSearch(anyString(), anyString());
		doReturn(true).when(jsar).insertJobSearchApplication(any());
		doReturn(mockTeacherUserId).when(newUserRepository).selectTeacherUserId(anyString());
		doReturn(true).when(notificationService).insertJobsearchNotification(anyString(), anyString(), anyBoolean());

		// 1.テストデータ設定
		NewJobApplicationForm form = new NewJobApplicationForm();
		form.setUserId("test@hcs.ac.jp");
		form.setCompany("testCompany");
		form.setEventCategory("testEventCategory");
		form.setLocationType("testLocationType");
		form.setStartTime("2023-10-01 10:00:00");
		form.setFinishTime("2023-10-01 12:00:00");
		form.setTardyLeaveTime("2023-10-01 11:00:00");
		form.setRemarks("testRemarks");
		form.setSchoolCheck(false);
		form.setTardinessAbsenceType("testTardinessAbsenceType");

		// 2.テスト対象メソッド実行
		boolean result = target.addNewJobApplication(form);

		// 3.テスト結果確認
		assertFalse(result);

		// 4.ログ確認
		log.info("就職活動申請の追加テストDataAccessExceptionの発生 " + result);
	}

	@Test
	void 就職活動申請の追加テストExceptionの発生() {

		// 0.モック設定
		String mockUserId = "test@hcs.ac.jp";

		String mockTeacherUserId = "teacher@hcs.ac.jp";

		doReturn(mockUserId).when(jobApplicationRepository).getJobSearchId();
		doReturn(true).when(jobApplicationRepository).insertJobSearch(anyString(), anyString());
		doReturn(true).when(jsar).insertJobSearchApplication(any());
		doReturn(mockTeacherUserId).when(newUserRepository).selectTeacherUserId(anyString());
		doThrow(new NullPointerException()).when(notificationService).insertJobsearchNotification(anyString(),
				anyString(),
				anyBoolean());

		// 1.テストデータ設定
		NewJobApplicationForm form = new NewJobApplicationForm();
		form.setUserId("test@hcs.ac.jp");
		form.setCompany("testCompany");
		form.setEventCategory("testEventCategory");
		form.setLocationType("testLocationType");
		form.setStartTime("2023-10-01 10:00:00");
		form.setFinishTime("2023-10-01 12:00:00");
		form.setTardyLeaveTime("2023-10-01 11:00:00");
		form.setRemarks("testRemarks");
		form.setSchoolCheck(false);
		form.setTardinessAbsenceType("testTardinessAbsenceType");

		// 2.テスト対象メソッド実行
		boolean result = target.addNewJobApplication(form);

		// 3.テスト結果確認
		assertFalse(result);

		// 4.ログ確認
		log.info("就職活動申請の追加テストExceptionの発生 " + result);
	}

	@Test
	void 就職活動申請の変更テスト() {
		// 0.モック設定
		String mockUserId = "test@hcs.ac.jp";

		String mockTeacherUserId = "teacher@hcs.ac.jp";

		doReturn(mockUserId).when(jobApplicationRepository).getJobSearchId();
		doReturn(true).when(jsar).updateJobSearchApplication(any());
		doReturn(true).when(jobApplicationRepository).updateJobSearchStatus(anyString(), anyString());
		doReturn(mockTeacherUserId).when(newUserRepository).selectTeacherUserId(anyString());
		doReturn(true).when(notificationService).insertJobsearchNotification(anyString(), anyString());

		// 1.テストデータ設定
		NewJobApplicationForm form = new NewJobApplicationForm();
		form.setJobHuntId("JS_2024_00001");
		form.setUserId("test@hcs.ac.jp");
		form.setCompany("testCompany");
		form.setEventCategory("testEventCategory");
		form.setLocationType("testLocationType");
		form.setStartTime("2023-10-01 10:00:00");
		form.setFinishTime("2023-10-01 12:00:00");
		form.setTardyLeaveTime("2023-10-01 11:00:00");
		form.setRemarks("testRemarks");
		form.setSchoolCheck(false);
		form.setTardinessAbsenceType("testTardinessAbsenceType");

		// 2.テスト対象メソッド実行
		boolean result = target.changeNewJobApplication(form);

		// 3.テスト結果確認
		assertTrue(result);

		// 4.ログ確認
		log.info("就職活動申請の変更テスト " + result);
	}

	@Test
	void 就職活動申請の変更テストupdateJobSearchApplicationの失敗() {
		// 0.モック設定
		String mockUserId = "test@hcs.ac.jp";

		String mockTeacherUserId = "teacher@hcs.ac.jp";

		doReturn(mockUserId).when(jobApplicationRepository).getJobSearchId();
		doReturn(false).when(jsar).updateJobSearchApplication(any());
		doReturn(true).when(jobApplicationRepository).updateJobSearchStatus(anyString(), anyString());
		doReturn(mockTeacherUserId).when(newUserRepository).selectTeacherUserId(anyString());
		doReturn(true).when(notificationService).insertJobsearchNotification(anyString(), anyString());

		// 1.テストデータ設定
		NewJobApplicationForm form = new NewJobApplicationForm();
		form.setJobHuntId("JS_2024_00001");
		form.setUserId("test@hcs.ac.jp");
		form.setCompany("testCompany");
		form.setEventCategory("testEventCategory");
		form.setLocationType("testLocationType");
		form.setStartTime("2023-10-01 10:00:00");
		form.setFinishTime("2023-10-01 12:00:00");
		form.setTardyLeaveTime("2023-10-01 11:00:00");
		form.setRemarks("testRemarks");
		form.setSchoolCheck(false);
		form.setTardinessAbsenceType("testTardinessAbsenceType");

		// 2.テスト対象メソッド実行
		boolean result = target.changeNewJobApplication(form);

		// 3.テスト結果確認
		assertFalse(result);

		// 4.ログ確認
		log.info("就職活動申請の変更テストupdateJobSearchApplicationの失敗 " + result);
	}

	@Test
	void 就職活動申請の変更テストupdateJobSearchStatusの失敗() {
		// 0.モック設定
		String mockUserId = "test@hcs.ac.jp";

		String mockTeacherUserId = "teacher@hcs.ac.jp";

		doReturn(mockUserId).when(jobApplicationRepository).getJobSearchId();
		doReturn(true).when(jsar).updateJobSearchApplication(any());
		doReturn(false).when(jobApplicationRepository).updateJobSearchStatus(anyString(), anyString());
		doReturn(mockTeacherUserId).when(newUserRepository).selectTeacherUserId(anyString());
		doReturn(true).when(notificationService).insertJobsearchNotification(anyString(), anyString());

		// 1.テストデータ設定
		NewJobApplicationForm form = new NewJobApplicationForm();
		form.setJobHuntId("JS_2024_00001");
		form.setUserId("test@hcs.ac.jp");
		form.setCompany("testCompany");
		form.setEventCategory("testEventCategory");
		form.setLocationType("testLocationType");
		form.setStartTime("2023-10-01 10:00:00");
		form.setFinishTime("2023-10-01 12:00:00");
		form.setTardyLeaveTime("2023-10-01 11:00:00");
		form.setRemarks("testRemarks");
		form.setSchoolCheck(false);
		form.setTardinessAbsenceType("testTardinessAbsenceType");

		// 2.テスト対象メソッド実行
		boolean result = target.changeNewJobApplication(form);

		// 3.テスト結果確認
		assertFalse(result);

		// 4.ログ確認
		log.info("就職活動申請の変更テストupdateJobSearchStatusの失敗 " + result);
	}

	@Test
	void 就職活動申請の変更テストIncorrectResultSizeDataAccessExceptionの発生() {
		// 0.モック設定
		String mockUserId = "test@hcs.ac.jp";

		String mockTeacherUserId = "teacher@hcs.ac.jp";

		doReturn(mockUserId).when(jobApplicationRepository).getJobSearchId();
		doThrow(new IncorrectResultSizeDataAccessException(1)).when(jsar).updateJobSearchApplication(any());
		doReturn(true).when(jobApplicationRepository).updateJobSearchStatus(anyString(), anyString());
		doReturn(mockTeacherUserId).when(newUserRepository).selectTeacherUserId(anyString());
		doReturn(true).when(notificationService).insertJobsearchNotification(anyString(), anyString());

		// 1.テストデータ設定
		NewJobApplicationForm form = new NewJobApplicationForm();
		form.setJobHuntId("JS_2024_00001");
		form.setUserId("test@hcs.ac.jp");
		form.setCompany("testCompany");
		form.setEventCategory("testEventCategory");
		form.setLocationType("testLocationType");
		form.setStartTime("2023-10-01 10:00:00");
		form.setFinishTime("2023-10-01 12:00:00");
		form.setTardyLeaveTime("2023-10-01 11:00:00");
		form.setRemarks("testRemarks");
		form.setSchoolCheck(false);
		form.setTardinessAbsenceType("testTardinessAbsenceType");

		// 2.テスト対象メソッド実行
		boolean result = target.changeNewJobApplication(form);

		// 3.テスト結果確認
		assertFalse(result);

		// 4.ログ確認
		log.info("就職活動申請の変更テストIncorrectResultSizeDataAccessExceptionの発生 " + result);
	}

	@Test
	void 就職活動申請の変更テストDataAccessExceptionの発生() {
		// 0.モック設定
		String mockUserId = "test@hcs.ac.jp";

		String mockTeacherUserId = "teacher@hcs.ac.jp";

		doReturn(mockUserId).when(jobApplicationRepository).getJobSearchId();
		doThrow(new DataAccessException("Simulated database error") {
		}).when(jsar).updateJobSearchApplication(any());
		doReturn(true).when(jobApplicationRepository).updateJobSearchStatus(anyString(), anyString());
		doReturn(mockTeacherUserId).when(newUserRepository).selectTeacherUserId(anyString());
		doReturn(true).when(notificationService).insertJobsearchNotification(anyString(), anyString());

		// 1.テストデータ設定
		NewJobApplicationForm form = new NewJobApplicationForm();
		form.setJobHuntId("JS_2024_00001");
		form.setUserId("test@hcs.ac.jp");
		form.setCompany("testCompany");
		form.setEventCategory("testEventCategory");
		form.setLocationType("testLocationType");
		form.setStartTime("2023-10-01 10:00:00");
		form.setFinishTime("2023-10-01 12:00:00");
		form.setTardyLeaveTime("2023-10-01 11:00:00");
		form.setRemarks("testRemarks");
		form.setSchoolCheck(false);
		form.setTardinessAbsenceType("testTardinessAbsenceType");

		// 2.テスト対象メソッド実行
		boolean result = target.changeNewJobApplication(form);

		// 3.テスト結果確認
		assertFalse(result);

		// 4.ログ確認
		log.info("就職活動申請の変更テストDataAccessExceptionの発生 " + result);
	}

	@Test
	void 就職活動申請の変更テストExceptionの発生() {
		// 0.モック設定
		String mockUserId = "test@hcs.ac.jp";

		String mockTeacherUserId = "teacher@hcs.ac.jp";

		doReturn(mockUserId).when(jobApplicationRepository).getJobSearchId();
		doReturn(true).when(jsar).updateJobSearchApplication(any());
		doReturn(true).when(jobApplicationRepository).updateJobSearchStatus(anyString(), anyString());
		doReturn(mockTeacherUserId).when(newUserRepository).selectTeacherUserId(anyString());
		doThrow(new NullPointerException()).when(notificationService).insertJobsearchNotification(anyString(),
				anyString());

		// 1.テストデータ設定
		NewJobApplicationForm form = new NewJobApplicationForm();
		form.setJobHuntId("JS_2024_00001");
		form.setUserId("test@hcs.ac.jp");
		form.setCompany("testCompany");
		form.setEventCategory("testEventCategory");
		form.setLocationType("testLocationType");
		form.setStartTime("2023-10-01 10:00:00");
		form.setFinishTime("2023-10-01 12:00:00");
		form.setTardyLeaveTime("2023-10-01 11:00:00");
		form.setRemarks("testRemarks");
		form.setSchoolCheck(false);
		form.setTardinessAbsenceType("testTardinessAbsenceType");

		// 2.テスト対象メソッド実行
		boolean result = target.changeNewJobApplication(form);

		// 3.テスト結果確認
		assertFalse(result);

		// 4.ログ確認
		log.info("就職活動申請の変更テストExceptionの発生 " + result);
	}

	@Test
	void 就職活動申請の変更テストchangeToTimestampの文字分割() {
		// 0.モック設定
		String mockUserId = "test@hcs.ac.jp";

		String mockTeacherUserId = "teacher@hcs.ac.jp";

		doReturn(mockUserId).when(jobApplicationRepository).getJobSearchId();
		doReturn(true).when(jsar).updateJobSearchApplication(any());
		doReturn(true).when(jobApplicationRepository).updateJobSearchStatus(anyString(), anyString());
		doReturn(mockTeacherUserId).when(newUserRepository).selectTeacherUserId(anyString());
		doReturn(true).when(notificationService).insertJobsearchNotification(anyString(), anyString());

		// 1.テストデータ設定
		NewJobApplicationForm form = new NewJobApplicationForm();
		form.setJobHuntId("JS_2024_00001");
		form.setUserId("test@hcs.ac.jp");
		form.setCompany("testCompany");
		form.setEventCategory("testEventCategory");
		form.setLocationType("testLocationType");
		form.setStartTime("10/01 12:00");
		form.setFinishTime("10/01 12:00");
		form.setTardyLeaveTime("10/01 11:00");
		form.setRemarks("testRemarks");
		form.setSchoolCheck(false);
		form.setTardinessAbsenceType("testTardinessAbsenceType");

		// 2.テスト対象メソッド実行
		boolean result = target.changeNewJobApplication(form);

		// 3.テスト結果確認
		assertTrue(result);

		// 4.ログ確認
		log.info("就職活動申請の変更テストchangeToTimestampの文字分割 " + result);
	}

	@Test
	void 就職活動申請の変更テストchangeToTimestampの空白区切りが一つ以上ある場合() {
		// 0.モック設定
		String mockUserId = "test@hcs.ac.jp";

		String mockTeacherUserId = "teacher@hcs.ac.jp";

		doReturn(mockUserId).when(jobApplicationRepository).getJobSearchId();
		doReturn(true).when(jsar).updateJobSearchApplication(any());
		doReturn(true).when(jobApplicationRepository).updateJobSearchStatus(anyString(), anyString());
		doReturn(mockTeacherUserId).when(newUserRepository).selectTeacherUserId(anyString());
		doReturn(true).when(notificationService).insertJobsearchNotification(anyString(), anyString());

		// 1.テストデータ設定
		NewJobApplicationForm form = new NewJobApplicationForm();
		form.setJobHuntId("JS_2024_00001");
		form.setUserId("test@hcs.ac.jp");
		form.setCompany("testCompany");
		form.setEventCategory("testEventCategory");
		form.setLocationType("testLocationType");
		form.setStartTime("10/01 12:00 test");
		form.setFinishTime("10/01 12:00");
		form.setTardyLeaveTime("10/01 11:00");
		form.setRemarks("testRemarks");
		form.setSchoolCheck(false);
		form.setTardinessAbsenceType("testTardinessAbsenceType");

		// 2.テスト対象メソッド実行
		boolean result = target.changeNewJobApplication(form);

		// 3.テスト結果確認
		assertFalse(result);

		// 4.ログ確認
		log.info("就職活動申請の変更テストchangeToTimestampの文字分割 " + result);
	}

	@Test
	void 就職活動申請の変更テストchangeToTimestampの日付部分の形式が正しくない場合() {
		// 0.モック設定
		String mockUserId = "test@hcs.ac.jp";

		String mockTeacherUserId = "teacher@hcs.ac.jp";

		doReturn(mockUserId).when(jobApplicationRepository).getJobSearchId();
		doReturn(true).when(jsar).updateJobSearchApplication(any());
		doReturn(true).when(jobApplicationRepository).updateJobSearchStatus(anyString(), anyString());
		doReturn(mockTeacherUserId).when(newUserRepository).selectTeacherUserId(anyString());
		doReturn(true).when(notificationService).insertJobsearchNotification(anyString(), anyString());

		// 1.テストデータ設定
		NewJobApplicationForm form = new NewJobApplicationForm();
		form.setJobHuntId("JS_2024_00001");
		form.setUserId("test@hcs.ac.jp");
		form.setCompany("testCompany");
		form.setEventCategory("testEventCategory");
		form.setLocationType("testLocationType");
		form.setStartTime("2024/10/01 12:00");
		form.setFinishTime("10/01 12:00");
		form.setTardyLeaveTime("10/01 11:00");
		form.setRemarks("testRemarks");
		form.setSchoolCheck(false);
		form.setTardinessAbsenceType("testTardinessAbsenceType");

		// 2.テスト対象メソッド実行
		boolean result = target.changeNewJobApplication(form);

		// 3.テスト結果確認
		assertFalse(result);

		// 4.ログ確認
		log.info("就職活動申請の変更テストchangeToTimestampの文字分割 " + result);
	}

	@Test
	void 就職活動申請の変更テストchangeToTimestampの時間部分の形式が正しくない場合() {
		// 0.モック設定
		String mockUserId = "test@hcs.ac.jp";

		String mockTeacherUserId = "teacher@hcs.ac.jp";

		doReturn(mockUserId).when(jobApplicationRepository).getJobSearchId();
		doReturn(true).when(jsar).updateJobSearchApplication(any());
		doReturn(true).when(jobApplicationRepository).updateJobSearchStatus(anyString(), anyString());
		doReturn(mockTeacherUserId).when(newUserRepository).selectTeacherUserId(anyString());
		doReturn(true).when(notificationService).insertJobsearchNotification(anyString(), anyString());

		// 1.テストデータ設定
		NewJobApplicationForm form = new NewJobApplicationForm();
		form.setJobHuntId("JS_2024_00001");
		form.setUserId("test@hcs.ac.jp");
		form.setCompany("testCompany");
		form.setEventCategory("testEventCategory");
		form.setLocationType("testLocationType");
		form.setStartTime("10/01 12:00:00");
		form.setFinishTime("10/01 12:00");
		form.setTardyLeaveTime("10/01 11:00");
		form.setRemarks("testRemarks");
		form.setSchoolCheck(false);
		form.setTardinessAbsenceType("testTardinessAbsenceType");

		// 2.テスト対象メソッド実行
		boolean result = target.changeNewJobApplication(form);

		// 3.テスト結果確認
		assertFalse(result);

		// 4.ログ確認
		log.info("就職活動申請の変更テストchangeToTimestampの文字分割 " + result);
	}
}
