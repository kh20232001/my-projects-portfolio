package com.api.domain.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

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
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.test.context.junit4.SpringRunner;

import com.api.domain.models.dbdata.UserData;
import com.api.domain.models.displaydata.UserDataDisplay;
import com.api.domain.models.displaydata.UserDetailDisplay;
import com.api.domain.models.entities.UserDeleteEntity;
import com.api.domain.models.forms.UserForm;
import com.api.domain.models.forms.UserUpdateForm;
import com.api.domain.repositories.CsvRepository;
import com.api.domain.repositories.JobSearchRepository;
import com.api.domain.repositories.UserRepository;
import com.api.jobpal.common.base.DBDataConversion;

@RunWith(SpringRunner.class)
@SpringBootTest
class UserServiceTest {

	@Autowired
	private UserService target;

	// 下の階層をモック化
	@SpyBean
	private UserRepository userRepository;

	@SpyBean
	private JobSearchRepository jobSearchRepository;

	@SpyBean
	private NotificationService notificationService;

	@SpyBean
	private CsvRepository csvRepository;

	@SpyBean
	private DBDataConversion dbdc;

	@Autowired
	private Logger log;

	@Test
	void ユーザ情報の全件取得テスト() {

		// 0.モック設定
		List<UserData> mockUserDataList = new ArrayList<>();
		UserData mockData = new UserData.Builder()
				.setUserId("student@example.com")
				.setPassword("securePass")
				.setUserName("学生 太郎")
				.setUserStatus("0") // 有効
				.setUserType("0") // 学生
				.setDepartment("S")
				.setGrade(3)
				.setClassName("A1")
				.setAttendanceNumber("01")
				.setStudentId(20231234)
				.setCreatedByUserId("system")
				.setCreatedAt(Timestamp.valueOf("2024-11-01 10:00:00"))
				.setUpdatedAt(Timestamp.valueOf("2024-11-05 15:30:00"))
				.build();

		mockUserDataList.add(mockData);

		doReturn(mockUserDataList).when(userRepository).selectTeacherStudentAll(anyBoolean());

		// 1.テストデータ設定
		// 2.テスト対象メソッド実行
		List<UserDataDisplay> resultList = target.getUserList();

		// 3.テスト結果確認
		UserDataDisplay result = resultList.get(0);
		assertEquals(0, result.getNumber());
		assertEquals("student@example.com", result.getUserId());
		assertEquals("securePass", result.getPassword());
		assertEquals("学生 太郎", result.getUserName());
		assertEquals("S3A1", result.getUserClass());
		assertEquals(1, result.getClassNumber());
		assertEquals(20231234, result.getSchoolNumber());
		assertEquals("0", result.getGrant());
		assertEquals("VALID", result.getStatus());
		assertEquals("system", result.getCreateUserId());
		assertEquals(Timestamp.valueOf("2024-11-01 10:00:00"), result.getCreateDatetime());
		// 4.ログ確認
		log.info("ユーザ情報の全件取得テスト " + result.toString());
	}

	@Test
	void ユーザ情報の全件取得テストDataAccessExceptionの発生() {

		// 0.モック設定
		List<UserData> mockUserDataList = new ArrayList<>();
		UserData mockData = new UserData.Builder()
				.setUserId("student@example.com")
				.setPassword("securePass")
				.setUserName("学生 太郎")
				.setUserStatus("0") // 有効
				.setUserType("0") // 学生
				.setDepartment("S")
				.setGrade(3)
				.setClassName("A1")
				.setAttendanceNumber("01")
				.setStudentId(20231234)
				.setCreatedByUserId("system")
				.setCreatedAt(Timestamp.valueOf("2024-11-01 10:00:00"))
				.setUpdatedAt(Timestamp.valueOf("2024-11-05 15:30:00"))
				.build();

		mockUserDataList.add(mockData);

		doThrow(new DataAccessException("Simulated database error") {
		}).when(userRepository).selectTeacherStudentAll(anyBoolean());

		// 1.テストデータ設定
		// 2.テスト対象メソッド実行
		List<UserDataDisplay> resultList = target.getUserList();

		// 3.テスト結果確認
		assertNull(resultList);
		// 4.ログ確認
		log.info("ユーザ情報の全件取得テストDataAccessExceptionの発生 " + resultList);
	}

	@Test
	void ユーザ情報を1件取得テスト() {

		// 0.モック設定
		UserData mockData = new UserData.Builder()
				.setUserId("student@example.com")
				.setPassword("securePass")
				.setUserName("学生 太郎")
				.setUserStatus("0") // 有効
				.setUserType("0") // 学生
				.setDepartment("S")
				.setGrade(3)
				.setClassName("A1")
				.setAttendanceNumber("01")
				.setStudentId(20231234)
				.setCreatedByUserId("system")
				.setCreatedAt(Timestamp.valueOf("2024-11-01 10:00:00"))
				.setUpdatedAt(Timestamp.valueOf("2024-11-05 15:30:00"))
				.build();

		doReturn(mockData).when(userRepository).selectTeacherStudentOne(anyString(), anyBoolean());

		// 1.テストデータ設定
		String userId = "student@example.com";

		// 2.テスト対象メソッド実行
		UserDetailDisplay result = target.getUser(userId);

		// 3.テスト結果確認
		assertNull(result.getNumber());
		assertEquals("student@example.com", result.getUserId());
		assertEquals("securePass", result.getPassword());
		assertEquals("学生 太郎", result.getUserName());
		assertEquals("S3A1", result.getUserClass());
		assertEquals(1, result.getClassNumber());
		assertEquals(20231234, result.getSchoolNumber());
		assertEquals("0", result.getGrant());
		assertEquals("VALID", result.getStatus());
		assertEquals("system", result.getCreateUserId());
		assertEquals(Timestamp.valueOf("2024-11-01 10:00:00"), result.getCreateDatetime());
		// 4.ログ確認
		log.info("ユーザ情報を1件取得テスト " + result.toString());
	}

	@Test
	void ユーザ情報を1件取得テストDataAccessExceptionの発生() {

		// 0.モック設定
		doThrow(new DataAccessException("") {
		}).when(userRepository).selectTeacherStudentOne(anyString(), anyBoolean());

		// 1.テストデータ設定
		String userId = "student@example.com";

		// 2.テスト対象メソッド実行
		UserDetailDisplay result = target.getUser(userId);

		// 3.テスト結果確認
		assertNull(result);
		// 4.ログ確認
		log.info("ユーザ情報を1件取得テストDataAccessExceptionの発生 " + result);
	}

	@Test
	void ユーザ情報を1件追加テスト() {

		// 0.モック設定
		doReturn(false).when(userRepository).existsUser(anyString());
		doReturn(true).when(userRepository).insertUserOne(any());
		doReturn(true).when(userRepository).insertTeacherStudentUserOne(any());

		// 1.テストデータ設定
		UserForm userForm = new UserForm();
		userForm.setUserId("user@example.com");
		userForm.setPassword("securePassword123");
		userForm.setUserName("山田 太郎");
		userForm.setUserClass("S2A1");
		userForm.setClassNumber(15);
		userForm.setSchoolNumber(12345);
		userForm.setGrant("0");
		userForm.setStatus("VALID"); // 有効状態
		userForm.setCreateUserId("admin@example.com");

		// 2.テスト対象メソッド実行
		Boolean result = target.insertOne(userForm);

		// 3.テスト結果確認
		assertTrue(result);
		// 4.ログ確認
		log.info("ユーザ情報を1件追加テスト " + result);
	}

	@Test
	void ユーザ情報を1件追加テストexistsUserで同じユーザIDが見つかった場合() {

		// 0.モック設定
		doReturn(true).when(userRepository).existsUser(anyString());

		// 1.テストデータ設定
		UserForm userForm = new UserForm();
		userForm.setUserId("user@example.com");
		userForm.setPassword("securePassword123");
		userForm.setUserName("山田 太郎");
		userForm.setUserClass("S2A1");
		userForm.setClassNumber(15);
		userForm.setSchoolNumber(12345);
		userForm.setGrant("0");
		userForm.setStatus("VALID"); // 有効状態
		userForm.setCreateUserId("admin@example.com");

		// 2.テスト対象メソッド実行
		Boolean result = target.insertOne(userForm);

		// 3.テスト結果確認
		assertFalse(result);
		// 4.ログ確認
		log.info("ユーザ情報を1件追加テストexistsUserで同じユーザIDが見つかった場合 " + result);
	}

	@Test
	void ユーザ情報を1件追加テストinsertUserOneが失敗した場合() {

		// 0.モック設定
		doReturn(false).when(userRepository).existsUser(anyString());
		doReturn(false).when(userRepository).insertUserOne(any());

		// 1.テストデータ設定
		UserForm userForm = new UserForm();
		userForm.setUserId("user@example.com");
		userForm.setPassword("securePassword123");
		userForm.setUserName("山田 太郎");
		userForm.setUserClass("S2A1");
		userForm.setClassNumber(15);
		userForm.setSchoolNumber(12345);
		userForm.setGrant("0");
		userForm.setStatus("VALID"); // 有効状態
		userForm.setCreateUserId("admin@example.com");

		// 2.テスト対象メソッド実行
		Boolean result = target.insertOne(userForm);

		// 3.テスト結果確認
		assertFalse(result);
		// 4.ログ確認
		log.info("ユーザ情報を1件追加テストinsertUserOneが失敗した場合 " + result);
	}

	@Test
	void ユーザ情報を1件追加テストinsertTeacherStudentUserOneが失敗した場合() {

		// 0.モック設定
		doReturn(false).when(userRepository).existsUser(anyString());
		doReturn(true).when(userRepository).insertUserOne(any());
		doReturn(false).when(userRepository).insertTeacherStudentUserOne(any());

		// 1.テストデータ設定
		UserForm userForm = new UserForm();
		userForm.setUserId("user@example.com");
		userForm.setPassword("securePassword123");
		userForm.setUserName("山田 太郎");
		userForm.setUserClass("S2A1");
		userForm.setClassNumber(15);
		userForm.setSchoolNumber(12345);
		userForm.setGrant("0");
		userForm.setStatus("VALID"); // 有効状態
		userForm.setCreateUserId("admin@example.com");

		// 2.テスト対象メソッド実行
		Boolean result = target.insertOne(userForm);

		// 3.テスト結果確認
		assertFalse(result);
		// 4.ログ確認
		log.info("ユーザ情報を1件追加テストinsertTeacherStudentUserOneが失敗した場合 " + result);
	}

	@Test
	void ユーザ情報を１件更新テストパスワードがある場合() {

		// 0.モック設定
		doReturn(true).when(userRepository).updateUserOne(any(), anyBoolean());
		doReturn(true).when(userRepository).updateTeacherStudentOne(any());

		// 1.テストデータ設定
		UserUpdateForm userForm = new UserUpdateForm();
		userForm.setUserId("user@example.com");
		userForm.setPassword("securePassword123");
		userForm.setUserName("山田 太郎");
		userForm.setUserClass("S2A1");
		userForm.setClassNumber(15);
		userForm.setSchoolNumber(12345);
		userForm.setGrant("0");
		userForm.setStatus("VALID"); // 有効状態

		// 2.テスト対象メソッド実行
		Boolean result = target.updateOne(userForm);

		// 3.テスト結果確認
		assertTrue(result);
		// 4.ログ確認
		log.info("ユーザ情報を1件追加テストパスワードがある場合 " + result);
	}

	@Test
	void ユーザ情報を１件更新テストパスワードがない場合() {

		// 0.モック設定
		doReturn(true).when(userRepository).updateUserOne(any(), anyBoolean());
		doReturn(true).when(userRepository).updateTeacherStudentOne(any());

		// 1.テストデータ設定
		UserUpdateForm userForm = new UserUpdateForm();
		userForm.setUserId("user@example.com");
		userForm.setPassword("");
		userForm.setUserName("山田 太郎");
		userForm.setUserClass("S2A1");
		userForm.setClassNumber(15);
		userForm.setSchoolNumber(12345);
		userForm.setGrant("0");
		userForm.setStatus("VALID"); // 有効状態

		// 2.テスト対象メソッド実行
		Boolean result = target.updateOne(userForm);

		// 3.テスト結果確認
		assertTrue(result);
		// 4.ログ確認
		log.info("ユーザ情報を1件追加テストパスワードがない場合 " + result);
	}

	@Test
	void ユーザ情報を１件更新テストupdateUserOneが失敗した場合() {

		// 0.モック設定
		doReturn(false).when(userRepository).updateUserOne(any(), anyBoolean());

		// 1.テストデータ設定
		UserUpdateForm userForm = new UserUpdateForm();
		userForm.setUserId("user@example.com");
		userForm.setPassword("securePassword123");
		userForm.setUserName("山田 太郎");
		userForm.setUserClass("S2A1");
		userForm.setClassNumber(15);
		userForm.setSchoolNumber(12345);
		userForm.setGrant("0");
		userForm.setStatus("VALID"); // 有効状態

		// 2.テスト対象メソッド実行
		Boolean result = target.updateOne(userForm);

		// 3.テスト結果確認
		assertFalse(result);
		// 4.ログ確認
		log.info("ユーザ情報を1件追加テストupdateUserOneが失敗した場合 " + result);
	}

	@Test
	void ユーザ情報を１件更新テストupdateTeacherStudentOneが失敗した場合() {

		// 0.モック設定
		doReturn(true).when(userRepository).updateUserOne(any(), anyBoolean());
		doReturn(false).when(userRepository).updateTeacherStudentOne(any());

		// 1.テストデータ設定
		UserUpdateForm userForm = new UserUpdateForm();
		userForm.setUserId("user@example.com");
		userForm.setPassword("securePassword123");
		userForm.setUserName("山田 太郎");
		userForm.setUserClass("S2A1");
		userForm.setClassNumber(15);
		userForm.setSchoolNumber(12345);
		userForm.setGrant("0");
		userForm.setStatus("VALID"); // 有効状態

		// 2.テスト対象メソッド実行
		Boolean result = target.updateOne(userForm);

		// 3.テスト結果確認
		assertFalse(result);
		// 4.ログ確認
		log.info("ユーザ情報を1件追加テストupdateTeacherStudentOneが失敗した場合 " + result);
	}

	@Test
	void ユーザ情報を１件更新テストIncorrectResultSizeDataAccessExceptionが発生() {

		// 0.モック設定
		doThrow(new IncorrectResultSizeDataAccessException(1)).when(userRepository).updateUserOne(any(), anyBoolean());

		// 1.テストデータ設定
		UserUpdateForm userForm = new UserUpdateForm();
		userForm.setUserId("user@example.com");
		userForm.setPassword("securePassword123");
		userForm.setUserName("山田 太郎");
		userForm.setUserClass("S2A1");
		userForm.setClassNumber(15);
		userForm.setSchoolNumber(12345);
		userForm.setGrant("0");
		userForm.setStatus("VALID"); // 有効状態

		// 2.テスト対象メソッド実行
		Boolean result = target.updateOne(userForm);

		// 3.テスト結果確認
		assertFalse(result);
		// 4.ログ確認
		log.info("ユーザ情報を1件追加テストIncorrectResultSizeDataAccessExceptionが発生 " + result);
	}

	@Test
	void ユーザを一件削除テスト() {

		// 0.モック設定
		doReturn(Timestamp.valueOf("2024-12-12 12:00:00")).when(dbdc).getNowTime();
		doReturn("0").when(userRepository).selectUserType(anyString());
		doReturn(true).when(userRepository).insertDeleteUserOne(any(), anyString());
		doReturn(true).when(userRepository).insertDeleteTeacherStudentUserOne(any(), anyString());
		doReturn(true).when(userRepository).deleteUserOne(anyString());

		// 1.テストデータ設定
		String userId = "user@example.com";

		// 2.テスト対象メソッド実行
		Boolean result = target.deleteOne(userId);

		// 3.テスト結果確認
		assertTrue(result);
		// 4.ログ確認
		log.info("ユーザを一件削除テスト " + result);
	}

	@Test
	void ユーザを一件削除テストinsertDeleteUserOneが失敗した場合() {

		// 0.モック設定
		doReturn(Timestamp.valueOf("2024-12-12 12:00:00")).when(dbdc).getNowTime();
		doReturn("0").when(userRepository).selectUserType(anyString());
		doReturn(false).when(userRepository).insertDeleteUserOne(any(), anyString());

		// 1.テストデータ設定
		String userId = "user@example.com";

		// 2.テスト対象メソッド実行
		Boolean result = target.deleteOne(userId);

		// 3.テスト結果確認
		assertFalse(result);
		// 4.ログ確認
		log.info("ユーザを一件削除テストinsertDeleteUserOneが失敗した場合 " + result);
	}

	@Test
	void ユーザを一件削除テストinsertDeleteTeacherStudentUserOneが失敗した場合() {

		// 0.モック設定
		doReturn(Timestamp.valueOf("2024-12-12 12:00:00")).when(dbdc).getNowTime();
		doReturn("0").when(userRepository).selectUserType(anyString());
		doReturn(true).when(userRepository).insertDeleteUserOne(any(), anyString());
		doReturn(false).when(userRepository).insertDeleteTeacherStudentUserOne(any(), anyString());

		// 1.テストデータ設定
		String userId = "user@example.com";

		// 2.テスト対象メソッド実行
		Boolean result = target.deleteOne(userId);

		// 3.テスト結果確認
		assertFalse(result);
		// 4.ログ確認
		log.info("ユーザを一件削除テストinsertDeleteTeacherStudentUserOneが失敗した場合 " + result);
	}

	@Test
	void ユーザを一件削除テストdeleteUserOneが失敗した場合() {

		// 0.モック設定
		doReturn(Timestamp.valueOf("2024-12-12 12:00:00")).when(dbdc).getNowTime();
		doReturn("0").when(userRepository).selectUserType(anyString());
		doReturn(true).when(userRepository).insertDeleteUserOne(any(), anyString());
		doReturn(true).when(userRepository).insertDeleteTeacherStudentUserOne(any(), anyString());
		doReturn(false).when(userRepository).deleteUserOne(anyString());

		// 1.テストデータ設定
		String userId = "user@example.com";

		// 2.テスト対象メソッド実行
		Boolean result = target.deleteOne(userId);

		// 3.テスト結果確認
		assertFalse(result);
		// 4.ログ確認
		log.info("ユーザを一件削除テストdeleteUserOneが失敗した場合 " + result);
	}

	@Test
	void ユーザを一件削除テストIncorrectResultSizeDataAccessExceptionが発生() {

		// 0.モック設定
		doThrow(new IncorrectResultSizeDataAccessException(1)).when(dbdc).getNowTime();

		// 1.テストデータ設定
		String userId = "user@example.com";

		// 2.テスト対象メソッド実行
		Boolean result = target.deleteOne(userId);

		// 3.テスト結果確認
		assertFalse(result);
		// 4.ログ確認
		log.info("ユーザを一件削除テストIncorrectResultSizeDataAccessExceptionが発生 " + result);
	}

	@Test
	void ユーザIDに関連する削除ユーザ情報のCSVデータを取得テスト() {

		// 0.モック設定
		List<String> mockJobSearchList = new ArrayList<>();
		mockJobSearchList.add("JS001");

		List<String> mockCertificateList = new ArrayList<>();
		mockCertificateList.add("CI001");

		doReturn("田中 征四郎").when(userRepository).selectUserName(anyString());
		doReturn(mockJobSearchList).when(csvRepository).selectJobSearchIdList(anyString());
		doReturn("21").when(jobSearchRepository).selectJobSearchStatus(anyString());
		doReturn(true).when(jobSearchRepository).updateJobSearchStatus(anyString(), anyString());
		doReturn(true).when(notificationService).deleteJobsearchNotification(anyString());
		doReturn(mockCertificateList).when(csvRepository).selectCertificateIssuanceIdList(anyString());
		doReturn(true).when(notificationService).deleteCertificateNotification(anyString());
		doReturn(true).when(userRepository).insertDeleteTeacherStudentUserOne(any(), anyString());
		doReturn(true).when(userRepository).deleteUserOne(anyString());

		// 1.テストデータ設定
		String userId = "user@example.com";

		// 2.テスト対象メソッド実行
		Map<String, List<UserDeleteEntity>> resultMap = target.getDeleteUserCsv(userId);
		UserDeleteEntity result = resultMap.get("csvList").get(0);

		// 3.テスト結果確認
		assertEquals("user@example.com", result.getUserId());
		assertEquals("田中 征四郎", result.getUserName());
		assertEquals("JS001", result.getJob().get(0).getJobHuntId());
		assertEquals("CI001", result.getCertificate().get(0).getCertificateIssueId());

		// 4.ログ確認
		log.info("ユーザIDに関連する削除ユーザ情報のCSVデータを取得テスト " + result);
	}

	@Test
	void ユーザIDに関連する削除ユーザ情報のCSVデータを取得テストdeleteCertificateNotificationが失敗した場合() {

		// 0.モック設定
		List<String> mockJobSearchList = new ArrayList<>();
		mockJobSearchList.add("JS001");

		List<String> mockCertificateList = new ArrayList<>();
		mockCertificateList.add("CI001");

		doReturn("田中 征四郎").when(userRepository).selectUserName(anyString());
		doReturn(mockJobSearchList).when(csvRepository).selectJobSearchIdList(anyString());
		doReturn("21").when(jobSearchRepository).selectJobSearchStatus(anyString());
		doReturn(true).when(jobSearchRepository).updateJobSearchStatus(anyString(), anyString());
		doReturn(true).when(notificationService).deleteJobsearchNotification(anyString());
		doReturn(mockCertificateList).when(csvRepository).selectCertificateIssuanceIdList(anyString());
		doReturn(false).when(notificationService).deleteCertificateNotification(anyString());

		// 1.テストデータ設定
		String userId = "user@example.com";

		// 2.テスト対象メソッド実行
		Map<String, List<UserDeleteEntity>> resultMap = target.getDeleteUserCsv(userId);

		// 3.テスト結果確認
		assertNull(resultMap);

		// 4.ログ確認
		log.info("ユーザIDに関連する削除ユーザ情報のCSVデータを取得テストdeleteCertificateNotificationが失敗した場合 " + resultMap);
	}

	@Test
	void ユーザIDに関連する削除ユーザ情報のCSVデータを取得テストdeleteJobsearchNotificationが失敗した場合() {

		// 0.モック設定
		List<String> mockJobSearchList = new ArrayList<>();
		mockJobSearchList.add("JS001");

		List<String> mockCertificateList = new ArrayList<>();
		mockCertificateList.add("CI001");

		doReturn("田中 征四郎").when(userRepository).selectUserName(anyString());
		doReturn(mockJobSearchList).when(csvRepository).selectJobSearchIdList(anyString());
		doReturn("21").when(jobSearchRepository).selectJobSearchStatus(anyString());
		doReturn(true).when(jobSearchRepository).updateJobSearchStatus(anyString(), anyString());
		doReturn(false).when(notificationService).deleteJobsearchNotification(anyString());

		// 1.テストデータ設定
		String userId = "user@example.com";

		// 2.テスト対象メソッド実行
		Map<String, List<UserDeleteEntity>> resultMap = target.getDeleteUserCsv(userId);

		// 3.テスト結果確認
		assertNull(resultMap);

		// 4.ログ確認
		log.info("ユーザIDに関連する削除ユーザ情報のCSVデータを取得テストdeleteJobsearchNotificationが失敗した場合 " + resultMap);
	}

	@Test
	void ユーザIDに関連する削除ユーザ情報のCSVデータを取得テストupdateJobSearchStatusが失敗した場合() {

		// 0.モック設定
		List<String> mockJobSearchList = new ArrayList<>();
		mockJobSearchList.add("JS001");

		List<String> mockCertificateList = new ArrayList<>();
		mockCertificateList.add("CI001");

		doReturn("田中 征四郎").when(userRepository).selectUserName(anyString());
		doReturn(mockJobSearchList).when(csvRepository).selectJobSearchIdList(anyString());
		doReturn("21").when(jobSearchRepository).selectJobSearchStatus(anyString());
		doReturn(false).when(jobSearchRepository).updateJobSearchStatus(anyString(), anyString());

		// 1.テストデータ設定
		String userId = "user@example.com";

		// 2.テスト対象メソッド実行
		Map<String, List<UserDeleteEntity>> resultMap = target.getDeleteUserCsv(userId);

		// 3.テスト結果確認
		assertNull(resultMap);

		// 4.ログ確認
		log.info("ユーザIDに関連する削除ユーザ情報のCSVデータを取得テストupdateJobSearchStatusが失敗した場合 " + resultMap);
	}

}
