package com.api.domain.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.api.domain.models.dbdata.JobSearchDashBoardData;
import com.api.domain.models.displaydata.CsvDisplayData;
import com.api.domain.models.displaydata.DashBoardDisplayData;
import com.api.domain.models.entities.DashBoardDisplayEntity;
import com.api.domain.models.entities.DashBoardEntity;
import com.api.domain.repositories.CsvRepository;
import com.api.domain.repositories.JobSearchDashBoardRepository;
import com.api.domain.repositories.NotificationRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
class DashBoardServiceTest {

	@Autowired
	private DashBoardService target;

	// 下の階層をモック化
	@SpyBean
	private JobSearchDashBoardRepository dashBoardRepository;

	@SpyBean
	private NotificationRepository notificationRepository;

	@SpyBean
	private CsvRepository csvRepository;

	@Autowired
	private Logger log;

	@Test
	void 全ユーザーの就職活動申請の取得テスト() {
		// 0.モック設定
		JobSearchDashBoardData mockJobSearchDashBoardData = new JobSearchDashBoardData();
		mockJobSearchDashBoardData.setUserName("山田 太郎");
		mockJobSearchDashBoardData.setJobSearchStatus("11");
		mockJobSearchDashBoardData.setStartTime(Timestamp.valueOf("2024-12-01 09:00:00"));
		mockJobSearchDashBoardData.setCompanyName("株式会社テスト");
		mockJobSearchDashBoardData.setEventCategory("0");
		mockJobSearchDashBoardData.setResult("2");
		mockJobSearchDashBoardData.setSchoolCheckFlag(true);
		mockJobSearchDashBoardData.setEndTime(Timestamp.valueOf("2024-12-01 11:00:00"));
		mockJobSearchDashBoardData.setJobSearchId("JOB12345");
		mockJobSearchDashBoardData.setDepartment("T");
		mockJobSearchDashBoardData.setGrade(3);
		mockJobSearchDashBoardData.setClassName("A2");
		mockJobSearchDashBoardData.setAttendanceNumber(12);
		mockJobSearchDashBoardData.setUserId("test@hcs.ac.jp");
		mockJobSearchDashBoardData.setMaxUpdatedAt(Timestamp.valueOf("2024-12-01 12:00:00"));

		DashBoardEntity mockDashBoardEntity = new DashBoardEntity();
		mockDashBoardEntity.setDashBoardList(new ArrayList<>());
		mockDashBoardEntity.getDashBoardList().add(mockJobSearchDashBoardData);
		mockDashBoardEntity.setAlertCnt(0);

		List<String> mockCsvLocationList = new ArrayList<>();
		mockCsvLocationList.add("自宅");
		mockCsvLocationList.add("学校");
		mockCsvLocationList.add("その他");

		doReturn(mockDashBoardEntity).when(dashBoardRepository).selectDashBoardAll(anyBoolean());
		doReturn(0).when(notificationRepository).getNotificationCount(anyString());
		doReturn(1).when(csvRepository).selectCsvActivity(anyString());
		doReturn(1).when(csvRepository).selectCsvActivityFinish(anyString());
		doReturn(1).when(csvRepository).selectCsvTokyo(anyString());
		doReturn(1).when(csvRepository).selectCsvSapporo(anyString());
		doReturn(1).when(csvRepository).selectCsvOthers(anyString());
		doReturn(mockCsvLocationList).when(csvRepository).selectCsvLocationList(anyString());

		// 1.テストデータ設定
		String userId = "test@hcs.ac.jp";
		String userType = "1";

		// 2.テスト対象メソッド実行
		DashBoardDisplayEntity result = target.getDashBoardAll(userId, userType);

		DashBoardDisplayData dashBoardResult = result.getDashBoardList().get(0);
		CsvDisplayData csvResult = result.getCsvList().get(0);

		// 3.テスト結果確認
		assertEquals(null, result.getUserName());
		assertEquals(0, result.getAlertCnt());
		assertEquals("JOB12345", dashBoardResult.getJobHuntId());
		assertEquals("test@hcs.ac.jp", dashBoardResult.getUserId());
		assertEquals("山田 太郎", dashBoardResult.getUserName());
		assertEquals("T3A212", dashBoardResult.getSchoolClassNumber());
		assertEquals("承認待ち", dashBoardResult.getJobStateId());
		assertEquals("株式会社テスト", dashBoardResult.getCompany());
		assertEquals("説明会_単", dashBoardResult.getEventCategory());
		assertEquals("継続（合格）", dashBoardResult.getResult());
		assertTrue(dashBoardResult.getSchoolCheck());
		assertEquals("12/01", dashBoardResult.getDate());
		assertEquals("09:00", dashBoardResult.getStartTime());
		assertEquals("11:00", dashBoardResult.getFinishTime());
		assertEquals(0, dashBoardResult.getEventCategoryPriority());
		assertEquals(11, dashBoardResult.getStateIdPriority());
		assertEquals(Long.valueOf(20241201090000L), dashBoardResult.getStartTimePriority());
		assertFalse(dashBoardResult.isReNotifyFlag());
		assertEquals(1, csvResult.getActivityFormInLocal());
		assertEquals(1, csvResult.getStudentsInAction());
		assertEquals(1, csvResult.getStudentsOfEnd());
		assertEquals(1, csvResult.getActivityLocationInTokyo());
		assertEquals(1, csvResult.getActivityLocationInSapporo());
		assertEquals(1, csvResult.getActivityLocationInOther());
		assertEquals(1, csvResult.getActivityFormInOnline());
		assertEquals(1, csvResult.getActivityFormInLocal());
		assertEquals(1, csvResult.getActivityFormInOther());

		// 4.ログ確認
		log.info("全ユーザーの就職活動申請の取得テスト " + result.toString());
	}

	@Test
	void 指定ユーザの就職活動申請の取得テスト() {
		// 0.モック設定
		JobSearchDashBoardData mockJobSearchDashBoardData = new JobSearchDashBoardData();
		mockJobSearchDashBoardData.setUserName("山田 太郎");
		mockJobSearchDashBoardData.setJobSearchStatus("11");
		mockJobSearchDashBoardData.setStartTime(Timestamp.valueOf("2024-12-01 09:00:00"));
		mockJobSearchDashBoardData.setCompanyName("株式会社テスト");
		mockJobSearchDashBoardData.setEventCategory("0");
		mockJobSearchDashBoardData.setResult("2");
		mockJobSearchDashBoardData.setSchoolCheckFlag(true);
		mockJobSearchDashBoardData.setEndTime(Timestamp.valueOf("2024-12-01 11:00:00"));
		mockJobSearchDashBoardData.setJobSearchId("JOB12345");
		mockJobSearchDashBoardData.setDepartment("T");
		mockJobSearchDashBoardData.setGrade(3);
		mockJobSearchDashBoardData.setClassName("A2");
		mockJobSearchDashBoardData.setAttendanceNumber(12);
		mockJobSearchDashBoardData.setUserId("test@hcs.ac.jp");
		mockJobSearchDashBoardData.setMaxUpdatedAt(Timestamp.valueOf("2024-12-01 12:00:00"));

		List<JobSearchDashBoardData> mockDataList = new ArrayList<>();
		mockDataList.add(mockJobSearchDashBoardData);

		DashBoardEntity mockDashBoardEntity = new DashBoardEntity();
		mockDashBoardEntity.setDashBoardList(new ArrayList<>());
		mockDashBoardEntity.getDashBoardList().add(mockJobSearchDashBoardData);
		mockDashBoardEntity.setAlertCnt(0);

		doReturn(mockDashBoardEntity).when(dashBoardRepository).selectDashBoardStudent(anyString());
		doReturn(0).when(notificationRepository).getNotificationCount(anyString());

		// 1.テストデータ設定
		String userId = "test@hcs.ac.jp";

		// 2.テスト対象メソッド実行
		DashBoardDisplayEntity result = target.getDashBoardUser(userId);

		DashBoardDisplayData dashBoardResult = result.getDashBoardList().get(0);
		List<CsvDisplayData> csvResult = result.getCsvList();

		// 3.テスト結果確認
		assertEquals(null, result.getUserName());
		assertEquals(0, result.getAlertCnt());
		assertEquals("JOB12345", dashBoardResult.getJobHuntId());
		assertEquals("test@hcs.ac.jp", dashBoardResult.getUserId());
		assertEquals("山田 太郎", dashBoardResult.getUserName());
		assertEquals("T3A212", dashBoardResult.getSchoolClassNumber());
		assertEquals("承認待ち", dashBoardResult.getJobStateId());
		assertEquals("株式会社テスト", dashBoardResult.getCompany());
		assertEquals("説明会_単", dashBoardResult.getEventCategory());
		assertEquals("継続（合格）", dashBoardResult.getResult());
		assertTrue(dashBoardResult.getSchoolCheck());
		assertEquals("12/01", dashBoardResult.getDate());
		assertEquals("09:00", dashBoardResult.getStartTime());
		assertEquals("11:00", dashBoardResult.getFinishTime());
		assertEquals(0, dashBoardResult.getEventCategoryPriority());
		assertEquals(11, dashBoardResult.getStateIdPriority());
		assertEquals(Long.valueOf(20241201090000L), dashBoardResult.getStartTimePriority());
		assertFalse(dashBoardResult.isReNotifyFlag());
		assertNull(csvResult);

		// 4.ログ確認
		log.info("指定ユーザの就職活動申請の取得テスト " + result.toString());
	}

}
