package com.api.domain.repositories;

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
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import com.api.domain.models.dbdata.UserData;
import com.api.jobpal.common.base.DBDataConversion;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NewUserRepositoryTest {

	@Autowired
	private UserRepository repository;

	@SpyBean
	private NamedParameterJdbcTemplate jdbc;

	@SpyBean
	private DBDataConversion dbdc;

	@SpyBean
	private PasswordEncoder passwordEncoder;

	@Autowired
	private Logger log;

	private final String TEST_USER_ID = "testUser";
	private final String TEST_USER_NAME = "Test User";
	private final String TEST_USER_STATUS = "ACTIVE";
	private final String TEST_USER_TYPE = "1";
	private final String TEST_PASSWORD = "password";
	private final String TEST_DEPARTMENT = "CS";
	private final String TEST_GRADE = "2";
	private final String TEST_CLASS_NAME = "A";
	private final String TEST_ATTENDANCE_NUMBER = "001";
	private final Integer TEST_STUDENT_ID = 123;

	private final Timestamp TEST_TIMESTAMP = Timestamp.valueOf("2024-01-01 00:00:00");

	@Test
	public void testSelectTeacherUserId正常() {
		String studentUserId = "S001";
		String expectedTeacherUserId = "T001";
		doReturn(expectedTeacherUserId).when(jdbc).queryForObject(anyString(), anyMap(), eq(String.class));

		String result = repository.selectTeacherUserId(studentUserId);

		assertEquals(expectedTeacherUserId, result);
		log.info("担任ユーザID取得テスト成功: studentUserId={}, teacherUserId={}", studentUserId, result);
	}

	@Test
	public void testSelectTeacherUserId_NoData() {
		String studentUserId = "S001";

		doThrow(new EmptyResultDataAccessException(1)).when(jdbc).queryForObject(anyString(), anyMap(),
				eq(String.class));

		String result = repository.selectTeacherUserId(studentUserId);

		assertNull(result);
		log.info("担任ユーザID取得テスト成功（データなし）: studentUserId={}", studentUserId);
	}

	@Test
	public void testSelectJobSearchIdList正常() {
		// 0. Mock setup
		List<Map<String, Object>> mockResult = List.of(
				Map.of("user_id", "U001"),
				Map.of("user_id", "U002"));

		doReturn(mockResult).when(jdbc).queryForList(anyString(), anyMap());

		// 2. Do
		List<String> result = repository.selectOfficeUserIdList();

		// 3. Check
		assertEquals(List.of("U001", "U002"), result);

		// 4. Log
		log.info("officeuserId取得テスト成功: userId={}", result);
	}

	@Test
	public void testSelectJobSearchIdList異常() {
		// 0. Mock setup
		doReturn(List.of()).when(jdbc).queryForList(anyString(), anyMap());
		// 1. Ready

		// 2. Do
		List<String> result = repository.selectOfficeUserIdList();

		// 3. Check
		assertTrue(result.isEmpty());

		// 4. Log
		log.info("officeuserId取得テスト成功(空データ)");
	}

	@Test
	public void testSelectUserAll正常() {
		// 0. Mock setup
		List<Map<String, Object>> mockResult = List.of(
				Map.of("user_id", TEST_USER_ID, "user_name", TEST_USER_NAME, "user_status", TEST_USER_STATUS,
						"user_type", TEST_USER_TYPE, "created_user_id", TEST_USER_ID, "created_at", TEST_TIMESTAMP,
						"updated_at", TEST_TIMESTAMP));
		String[] mockColumns = { "user_id", "encrypted_password", "user_name", "user_status", "user_type",
				"created_user_id", "created_at",
				"updated_at" };

		doReturn(mockResult).when(jdbc).queryForList(anyString(), anyMap());
		doReturn(mockColumns).when(dbdc).extractColumnNames(anyString());

		// 1. Ready
		Boolean includePassword = false;

		// 2. Do
		List<UserData> result = repository.selectUserAll(includePassword);

		// 3. Check
		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals(TEST_USER_ID, result.get(0).getUserId());

		// 4. Log
		log.info("Test passed for selectUserAll");
	}

	@Test
	public void testSelectUserAllnull挿入異常() {
		// 0. Mock setup
		List<Map<String, Object>> mockResult = List.of(
				Map.of("user_id", TEST_USER_ID, "user_name", TEST_USER_NAME, "user_status", TEST_USER_STATUS,
						"user_type", TEST_USER_TYPE, "created_user_id", TEST_USER_ID, "created_at", TEST_TIMESTAMP,
						"updated_at", TEST_TIMESTAMP));
		String[] mockColumns = { "user_id", "encrypted_password", "user_name", "user_status", "user_type",
				"created_user_id", "created_at",
				"updated_at" };

		doReturn(mockResult).when(jdbc).queryForList(anyString(), anyMap());
		doReturn(mockColumns).when(dbdc).extractColumnNames(anyString());

		// 1. Ready
		Boolean includePassword = null;

		// 2. Do
		List<UserData> result = repository.selectUserAll(includePassword);

		// 3. Check
		assertNull(result); // 結果がnullではないことを確認

		// 4. Log
		log.info("Test passed for selectUserAll");
	}

	@Test
	public void testSelectUserAll返却値が空による異常() {
		// 0. Mock setup
		List<String> emptyList = new ArrayList<>(); // 空リストを設定
		String[] mockColumns = { "user_id", "encrypted_password", "user_name", "user_status", "user_type",
				"created_user_id", "created_at", "updated_at" };

		doReturn(emptyList).when(jdbc).queryForList(anyString(), anyMap()); // 空リストを返すモック設定
		doReturn(mockColumns).when(dbdc).extractColumnNames(anyString()); // モックカラムを返す

		// 1. Ready
		Boolean includePassword = true;

		// 2. Do
		List<UserData> result = repository.selectUserAll(includePassword);

		// 3. Check
		assertNull(result); // 結果がnullではないことを確認

		// 4. Log
		log.info("Test passed for selectUserAll when empty result");
	}

	@Test
	public void testSelectTeacherStudentAll正常() {
		// 0. Mock setup
		List<Map<String, Object>> mockResult = List.of(
				Map.ofEntries(
						Map.entry("user_id", TEST_USER_ID),
						Map.entry("encrypted_password", TEST_PASSWORD),
						Map.entry("user_name", TEST_USER_NAME),
						Map.entry("user_status", TEST_USER_STATUS),
						Map.entry("user_type", TEST_USER_TYPE),
						Map.entry("created_user_id", TEST_USER_ID),
						Map.entry("created_at", TEST_TIMESTAMP),
						Map.entry("updated_at", TEST_TIMESTAMP),
						Map.entry("department", TEST_DEPARTMENT),
						Map.entry("grade", TEST_GRADE),
						Map.entry("class_name", TEST_CLASS_NAME),
						Map.entry("attendance_number", TEST_ATTENDANCE_NUMBER),
						Map.entry("student_id", TEST_STUDENT_ID)));
		String[] mockColumns = { "user_id", "encrypted_password", "user_name", "user_status", "user_type",
				"created_user_id",
				"created_at", "updated_at", "department", "grade", "class_name", "attendance_number", "student_id" };

		doReturn(mockResult).when(jdbc).queryForList(anyString(), anyMap());
		doReturn(mockColumns).when(dbdc).extractColumnNames(anyString());

		// 1. Ready
		Boolean includePassword = false;

		// 2. Do
		List<UserData> result = repository.selectTeacherStudentAll(includePassword);

		// 3. Check
		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals(TEST_DEPARTMENT, result.get(0).getDepartment());

		// 4. Log
		log.info("Test passed for selectTeacherStudentAll");
	}

	@Test
	public void testSelectTeacherStudentAllnull挿入異常() {
		// 0. Mock setup
		List<Map<String, Object>> mockResult = List.of(
				Map.ofEntries(
						Map.entry("user_id", TEST_USER_ID),
						Map.entry("encrypted_password", TEST_PASSWORD),
						Map.entry("user_name", TEST_USER_NAME),
						Map.entry("user_status", TEST_USER_STATUS),
						Map.entry("user_type", TEST_USER_TYPE),
						Map.entry("created_user_id", TEST_USER_ID),
						Map.entry("created_at", TEST_TIMESTAMP),
						Map.entry("updated_at", TEST_TIMESTAMP),
						Map.entry("department", TEST_DEPARTMENT),
						Map.entry("grade", TEST_GRADE),
						Map.entry("class_name", TEST_CLASS_NAME),
						Map.entry("attendance_number", TEST_ATTENDANCE_NUMBER),
						Map.entry("student_id", TEST_STUDENT_ID)));
		String[] mockColumns = { "user_id", "encrypted_password", "user_name", "user_status", "user_type",
				"created_user_id",
				"created_at", "updated_at", "department", "grade", "class_name", "attendance_number", "student_id" };

		doReturn(mockResult).when(jdbc).queryForList(anyString(), anyMap());
		doReturn(mockColumns).when(dbdc).extractColumnNames(anyString());

		// 1. Ready
		Boolean includePassword = null;

		// 2. Do
		List<UserData> result = repository.selectTeacherStudentAll(includePassword);

		// 3. Check
		assertNull(result); // 結果がnullではないことを確認

		// 4. Log
		log.info("Test passed for selectTeacherStudentAll");
	}

	@Test
	public void testSelectTeacherStudentAll返却値が空による異常() {
		// 0. Mock setup
		List<String> emptyList = new ArrayList<>();
		String[] mockColumns = { "user_id", "encrypted_password", "user_name", "user_status", "user_type",
				"created_user_id",
				"created_at", "updated_at", "department", "grade", "class_name", "attendance_number", "student_id" };

		doReturn(emptyList).when(jdbc).queryForList(anyString(), anyMap());
		doReturn(mockColumns).when(dbdc).extractColumnNames(anyString());

		// 1. Ready
		Boolean includePassword = false;

		// 2. Do
		List<UserData> result = repository.selectTeacherStudentAll(includePassword);

		// 3. Check
		assertNull(result); // 結果がnullではないことを確認

		// 4. Log
		log.info("Test passed for selectTeacherStudentAll");
	}

	@Test
	public void testSelectUserOne正常() {
		// 0. Mock setup
		List<Map<String, Object>> mockResult = List.of(
				Map.of("user_id", TEST_USER_ID, "user_name", TEST_USER_NAME, "user_status", TEST_USER_STATUS,
						"user_type", TEST_USER_TYPE, "created_user_id", TEST_USER_ID, "created_at", TEST_TIMESTAMP,
						"updated_at", TEST_TIMESTAMP));
		String[] mockColumns = { "user_id", "encrypted_password", "user_name", "user_status", "user_type",
				"created_user_id", "created_at",
				"updated_at" };

		doReturn(mockResult).when(jdbc).queryForList(anyString(), anyMap());
		doReturn(mockColumns).when(dbdc).extractColumnNames(anyString());

		// 1. Ready
		Boolean includePassword = true;

		// 2. Do
		UserData result = repository.selectUserOne(TEST_USER_ID, includePassword);

		// 3. Check
		assertNotNull(result);
		assertEquals(TEST_USER_ID, result.getUserId());

		// 4. Log
		log.info("Test passed for selectUserOne");
	}

	@Test
	public void testSelectUserOne返却値が空による異常() {
		// 0. Mock setup
		List<String> emptyList = new ArrayList<>();
		String[] mockColumns = { "user_id", "encrypted_password", "user_name", "user_status", "user_type",
				"created_user_id", "created_at",
				"updated_at" };

		doReturn(emptyList).when(jdbc).queryForList(anyString(), anyMap());
		doReturn(mockColumns).when(dbdc).extractColumnNames(anyString());

		// 1. Ready
		Boolean includePassword = true;

		// 2. Do
		UserData result = repository.selectUserOne(TEST_USER_ID, includePassword);

		// 3. Check
		assertNull(result); // 結果がnullではないことを確認

		// 4. Log
		log.info("Test passed for selectUserOne");
	}

	@Test
	public void testSelectUserOnenull挿入異常() {
		// 0. Mock setup
		List<Map<String, Object>> mockResult = List.of(
				Map.of("user_id", TEST_USER_ID, "user_name", TEST_USER_NAME, "user_status", TEST_USER_STATUS,
						"user_type", TEST_USER_TYPE, "created_user_id", TEST_USER_ID, "created_at", TEST_TIMESTAMP,
						"updated_at", TEST_TIMESTAMP));
		String[] mockColumns = { "user_id", "encrypted_password", "user_name", "user_status", "user_type",
				"created_user_id", "created_at",
				"updated_at" };

		doReturn(mockResult).when(jdbc).queryForList(anyString(), anyMap());
		doReturn(mockColumns).when(dbdc).extractColumnNames(anyString());

		// 1. Ready
		Boolean includePassword = null;

		// 2. Do
		UserData result = repository.selectUserOne(TEST_USER_ID, includePassword);

		// 3. Check
		assertNull(result); // 結果がnullではないことを確認

		// 4. Log
		log.info("Test passed for selectUserOne");
	}

	@Test
	public void testUpdateUserStateOne正常() {
		// 0. Mock setup
		doReturn(1).when(jdbc).update(anyString(), anyMap());

		// 1. Ready
		String newState = "INACTIVE";

		// 2. Do
		Boolean result = repository.updateUserStateOne(TEST_USER_ID, newState);

		// 3. Check
		assertTrue(result);

		// 4. Log
		log.info("Test passed for updateUserStateOne");
	}

	@Test
	public void testUpdateUserStateOne異常() {
		// 0. Mock setup
		doReturn(0).when(jdbc).update(anyString(), anyMap());

		// 1. Ready
		String newState = "INACTIVE";

		// 2. Do
		Boolean result = repository.updateUserStateOne(TEST_USER_ID, newState);

		// 3. Check
		assertFalse(result);

		// 4. Log
		log.info("Test passed for updateUserStateOne");
	}

	@Test
	public void testExistsUser異常でfalseを返却() {
		// 0. Mock setup
		doThrow(new EmptyResultDataAccessException("Intentional error", 0)).when(jdbc).queryForObject(anyString(),
				anyMap(), eq(Integer.class));

		// 1. Ready
		String userId = TEST_USER_ID;

		// 2. Do
		Boolean result = repository.existsUser(userId);

		// 3. Check
		assertFalse(result);

		// 4. Log
		log.info("Test passed for existsUser");
	}

	@Test
	public void testExistsUser正常でtrueを返却() {
		// 0. Mock setup
		doReturn(5).when(jdbc).queryForObject(anyString(), anyMap(), eq(Integer.class));

		// 1. Ready
		String userId = TEST_USER_ID;

		// 2. Do
		Boolean result = repository.existsUser(userId);

		// 3. Check
		assertTrue(result);

		// 4. Log
		log.info("Test passed for existsUser");
	}

	@Test
	public void testExistsUser正常でfalseを返却() {
		// 0. Mock setup
		doReturn(0).when(jdbc).queryForObject(anyString(), anyMap(), eq(Integer.class));

		// 1. Ready
		String userId = TEST_USER_ID;

		// 2. Do
		Boolean result = repository.existsUser(userId);

		// 3. Check
		assertFalse(result);

		// 4. Log
		log.info("Test passed for existsUser");
	}

	@Test
	public void testInsertUserOne正常() {
		// 0. Mock setup
		doReturn(1).when(jdbc).update(anyString(), anyMap());

		// 1. Ready
		UserData userData = new UserData();
		userData.setUserId(TEST_USER_ID);
		userData.setPassword(TEST_PASSWORD);
		userData.setUserName(TEST_USER_NAME);
		userData.setUserStatus(TEST_USER_STATUS);
		userData.setUserType(TEST_USER_TYPE);
		userData.setCreatedByUserId(TEST_USER_ID);
		userData.setCreatedAt(TEST_TIMESTAMP);
		userData.setUpdateAt(TEST_TIMESTAMP);

		// 2. Do
		Boolean result = repository.insertUserOne(userData);

		// 3. Check
		assertTrue(result);

		// 4. Log
		log.info("Test passed for insertUserOne");
	}

	@Test
	public void testInsertUserOne異常() {
		// 0. Mock setup
		doReturn(0).when(jdbc).update(anyString(), anyMap());

		// 1. Ready
		UserData userData = new UserData();
		userData.setUserId(TEST_USER_ID);
		userData.setPassword(TEST_PASSWORD);
		userData.setUserName(TEST_USER_NAME);
		userData.setUserStatus(TEST_USER_STATUS);
		userData.setUserType(TEST_USER_TYPE);
		userData.setCreatedByUserId(TEST_USER_ID);
		userData.setCreatedAt(TEST_TIMESTAMP);
		userData.setUpdateAt(TEST_TIMESTAMP);

		// 2. Do
		Boolean result = repository.insertUserOne(userData);

		// 3. Check
		assertFalse(result);

		// 4. Log
		log.info("Test passed for insertUserOne");
	}

	@Test
	public void testUpdateTeacherStudentOne正常() {
		// 0. Mock setup
		doReturn(1).when(jdbc).update(anyString(), anyMap());

		// 1. Ready
		UserData userData = new UserData();
		userData.setDepartment(TEST_DEPARTMENT);
		userData.setGrade(Integer.parseInt(TEST_GRADE));
		userData.setClassName(TEST_CLASS_NAME);
		userData.setAttendanceNumber(TEST_ATTENDANCE_NUMBER);
		userData.setStudentId(TEST_STUDENT_ID);
		userData.setUserId(TEST_USER_ID);

		// 2. Do
		Boolean result = repository.updateTeacherStudentOne(userData);

		// 3. Check
		assertTrue(result);

		// 4. Log
		log.info("Test passed for updateTeacherStudentOne");
	}

	@Test
	public void testUpdateTeacherStudentOne異常() {
		// 0. Mock setup
		doReturn(0).when(jdbc).update(anyString(), anyMap());

		// 1. Ready
		UserData userData = new UserData();
		userData.setDepartment(TEST_DEPARTMENT);
		userData.setGrade(Integer.parseInt(TEST_GRADE));
		userData.setClassName(TEST_CLASS_NAME);
		userData.setAttendanceNumber(TEST_ATTENDANCE_NUMBER);
		userData.setStudentId(TEST_STUDENT_ID);
		userData.setUserId(TEST_USER_ID);

		// 2. Do
		Boolean result = repository.updateTeacherStudentOne(userData);

		// 3. Check
		assertFalse(result);

		// 4. Log
		log.info("Test passed for updateTeacherStudentOne");
	}

	@Test
	public void testDeleteUserOne正常() {
		// 0. Mock setup
		doReturn(1).when(jdbc).update(anyString(), anyMap());

		// 1. Ready
		String userId = TEST_USER_ID;

		// 2. Do
		Boolean result = repository.deleteUserOne(userId);

		// 3. Check
		assertTrue(result);

		// 4. Log
		log.info("Test passed for deleteUserOne");
	}

	@Test
	public void testDeleteUserOne異常() {
		// 0. Mock setup
		doReturn(0).when(jdbc).update(anyString(), anyMap());

		// 1. Ready
		String userId = TEST_USER_ID;

		// 2. Do
		Boolean result = repository.deleteUserOne(userId);

		// 3. Check
		assertFalse(result);

		// 4. Log
		log.info("Test passed for deleteUserOne");
	}

	@Test
	public void testUpdateUserOne正常WithPassword() {
		// 0. Mock setup
		String encodedPassword = "encodedPassword";
		doReturn(encodedPassword).when(passwordEncoder).encode(TEST_PASSWORD);

		doReturn(1).when(jdbc).update(anyString(), anyMap());

		// 1. Ready
		UserData userData = new UserData();
		userData.setUserId(TEST_USER_ID);
		userData.setPassword(TEST_PASSWORD);
		userData.setUserName(TEST_USER_NAME);
		userData.setUserStatus(TEST_USER_STATUS);
		userData.setUserType(TEST_USER_TYPE);

		// 2. Do
		Boolean result = repository.updateUserOne(userData, true);

		// 3. Check
		assertTrue(result);

		// 4. Log
		log.info("Test passed for updateUserOne with includePassword = true");
	}

	@Test
	public void testUpdateUserOne正常WithoutPassword() {
		// 0. Mock setup
		doReturn(1).when(jdbc).update(anyString(), anyMap());

		// 1. Ready
		UserData userData = new UserData();
		userData.setUserId(TEST_USER_ID);
		userData.setUserName(TEST_USER_NAME);
		userData.setUserStatus(TEST_USER_STATUS);
		userData.setUserType(TEST_USER_TYPE);

		// 2. Do
		Boolean result = repository.updateUserOne(userData, false);

		// 3. Check
		assertTrue(result);

		// 4. Log
		log.info("Test passed for updateUserOne with includePassword = false");
	}

	@Test
	public void testUpdateUserOne異常() {
		// 0. Mock setup
		doReturn(0).when(jdbc).update(anyString(), anyMap()); // Update count is 0

		// 1. Ready
		UserData userData = new UserData();
		userData.setUserId(TEST_USER_ID);
		userData.setUserName(TEST_USER_NAME);
		userData.setUserStatus(TEST_USER_STATUS);
		userData.setUserType(TEST_USER_TYPE);

		// 2. Do
		Boolean result = repository.updateUserOne(userData, false);

		// 3. Check
		assertFalse(result);

		// 4. Log
		log.info("Test failed for updateUserOne due to mismatched update count");
	}

	@Test
	public void testSelectUserType正常() {
		// 0. Mock setup
		String expectedUserType = "1";
		doReturn("1").when(jdbc).queryForObject(anyString(), anyMap(), eq(String.class));

		// 1. Ready
		String userId = TEST_USER_ID;

		// 2. Do
		String result = repository.selectUserType(userId);

		// 3. Check
		assertNotNull(result);
		assertEquals(expectedUserType, result);

		// 4. Log
		log.info("Test passed for selectUserType with userId: {}", userId);
	}

	@Test
	public void testSelectUserType異常() {
		// 0. Mock setup
		doThrow(new EmptyResultDataAccessException(1)).when(jdbc).queryForObject(anyString(), anyMap(),
				eq(String.class));

		// 1. Ready
		String userId = TEST_USER_ID;

		// 2. Do
		String result = repository.selectUserType(userId);

		// 3. Check
		assertNull(result);

		// 4. Log
		log.info("Test passed for selectUserType with no result for userId: {}", userId);
	}

	@Test
	public void testSelectUserName正常() {
		// 0. Mock setup
		String expectedUserName = "山田太郎";
		doReturn(expectedUserName).when(jdbc).queryForObject(anyString(), anyMap(), eq(String.class));

		// 1. Ready
		String userId = TEST_USER_ID;

		// 2. Do
		String result = repository.selectUserName(userId);

		// 3. Check
		assertNotNull(result);
		assertEquals(expectedUserName, result);

		// 4. Log
		log.info("ユーザネーム取得テスト成功: userId={}, userName={}", userId, result);
	}

	@Test
	public void testSelectUserName結果なし() {
		// 0. Mock setup
		doThrow(new EmptyResultDataAccessException(1)).when(jdbc).queryForObject(anyString(), anyMap(),
				eq(String.class));

		// 1. Ready
		String userId = TEST_USER_ID;

		// 2. Do
		String result = repository.selectUserName(userId);

		// 3. Check
		assertNull(result);

		// 4. Log
		log.info("ユーザネーム取得テスト成功（該当データなし）: userId={}", userId);
	}

	@Test
	public void testSelectTeacherStudentOne正常() {
		// 0. Mock setup
		List<Map<String, Object>> mockResult = List.of(
				Map.ofEntries(
						Map.entry("user_id", TEST_USER_ID),
						Map.entry("encrypted_password", TEST_PASSWORD),
						Map.entry("user_name", TEST_USER_NAME),
						Map.entry("user_status", TEST_USER_STATUS),
						Map.entry("user_type", TEST_USER_TYPE),
						Map.entry("created_user_id", TEST_USER_ID),
						Map.entry("created_at", TEST_TIMESTAMP),
						Map.entry("updated_at", TEST_TIMESTAMP),
						Map.entry("department", TEST_DEPARTMENT),
						Map.entry("grade", TEST_GRADE),
						Map.entry("class_name", TEST_CLASS_NAME),
						Map.entry("attendance_number", TEST_ATTENDANCE_NUMBER),
						Map.entry("student_id", TEST_STUDENT_ID)));
		String[] mockColumns = { "user_id", "encrypted_password", "user_name", "user_status", "user_type",
				"created_user_id",
				"created_at", "updated_at", "department", "grade", "class_name", "attendance_number", "student_id" };

		doReturn(mockResult).when(jdbc).queryForList(anyString(), anyMap());
		doReturn(mockColumns).when(dbdc).extractColumnNames(anyString());

		// 1. Ready
		String userId = TEST_USER_ID;
		Boolean outPassword = true;

		// 2. Do
		UserData result = repository.selectTeacherStudentOne(userId, outPassword);

		// 3. Check
		assertNotNull(result);
		assertEquals(TEST_USER_ID, result.getUserId());
		assertEquals(TEST_USER_NAME, result.getUserName());
		assertEquals(TEST_DEPARTMENT, result.getDepartment());
		assertEquals(Integer.parseInt(TEST_GRADE), result.getGrade());
		assertEquals(TEST_CLASS_NAME, result.getClassName());
		assertEquals(TEST_ATTENDANCE_NUMBER, result.getAttendanceNumber());
		assertEquals(TEST_STUDENT_ID, result.getStudentId());
		if (outPassword) {
			assertEquals(TEST_PASSWORD, result.getPassword());
		}

		// 4. Log
		log.info("担任・学生情報取得テスト成功: userId={}, userName={}, department={}", userId, result.getUserName(),
				result.getDepartment());
	}

	@Test
	public void testSelectTeacherStudentOne結果なし() {
		// 0. Mock setup
		doReturn(List.of()).when(jdbc).queryForList(anyString(), anyMap());

		// 1. Ready
		String userId = TEST_USER_ID;
		Boolean outPassword = true;

		// 2. Do
		UserData result = repository.selectTeacherStudentOne(userId, outPassword);

		// 3. Check
		assertNull(result);

		// 4. Log
		log.info("担任・学生情報取得テスト成功（該当データなし）: userId={}", userId);
	}

	@Test
	public void testSelectTeacherStudentOnenullを挿入() {
		// 1. Ready
		String userId = TEST_USER_ID;
		Boolean outPassword = null;

		// 2. Do
		UserData result = repository.selectTeacherStudentOne(userId, outPassword);

		// 3. Check
		assertNull(result);

		// 4. Log
		log.info("担任・学生情報取得テスト成功（パスワードフラグがnull）: userId={}", userId);
	}

	@Test
	public void testInsertTeacherStudentUserOne正常() {
		// 0. Mock setup

		doReturn(1).when(jdbc).update(anyString(), anyMap());

		// 1. Ready
		UserData userData = new UserData();
		userData.setUserId(TEST_USER_ID);
		userData.setDepartment(TEST_DEPARTMENT);
		userData.setGrade(Integer.parseInt(TEST_GRADE));
		userData.setClassName(TEST_CLASS_NAME);
		userData.setAttendanceNumber(TEST_ATTENDANCE_NUMBER);
		userData.setStudentId(TEST_STUDENT_ID);

		// 2. Do
		Boolean result = repository.insertTeacherStudentUserOne(userData);

		// 3. Check
		assertTrue(result);

		// 4. Log
		log.info("担任・学生ユーザ登録テスト成功: userId={}, department={}", userData.getUserId(), userData.getDepartment());
	}

	@Test
	public void testInsertTeacherStudentUserOne異常() {
		// 0. Mock setup

		doReturn(0).when(jdbc).update(anyString(), anyMap()); // Update count is 0

		// 1. Ready
		UserData userData = new UserData();
		userData.setUserId(TEST_USER_ID);
		userData.setDepartment(TEST_DEPARTMENT);
		userData.setGrade(Integer.parseInt(TEST_GRADE));
		userData.setClassName(TEST_CLASS_NAME);
		userData.setAttendanceNumber(TEST_ATTENDANCE_NUMBER);
		userData.setStudentId(TEST_STUDENT_ID);

		// 2. Do
		Boolean result = repository.insertTeacherStudentUserOne(userData);

		// 3. Check
		assertFalse(result);

		// 4. Log
		log.info("担任・学生ユーザ登録テスト失敗: userId={}, department={}", userData.getUserId(), userData.getDepartment());
	}

	@Test
	public void testInsertDeleteTeacherStudentUserOne正常() {
		// 0. Mock setup

		doReturn(1).when(jdbc).update(anyString(), anyMap());

		// 1. Ready
		Timestamp deletedAt = TEST_TIMESTAMP;
		String userId = TEST_USER_ID;

		// 2. Do
		Boolean result = repository.insertDeleteTeacherStudentUserOne(deletedAt, userId);

		// 3. Check
		assertTrue(result);

		// 4. Log
		log.info("削除担任・学生ユーザ登録テスト成功: userId={}, deletedAt={}", userId, deletedAt);
	}

	@Test
	public void testInsertDeleteTeacherStudentUserOne異常() {
		// 0. Mock setup
		doReturn(0).when(jdbc).update(anyString(), anyMap()); // Update count is 0

		// 1. Ready
		Timestamp deletedAt = TEST_TIMESTAMP;
		String userId = TEST_USER_ID;

		// 2. Do
		Boolean result = repository.insertDeleteTeacherStudentUserOne(deletedAt, userId);

		// 3. Check
		assertFalse(result);

		// 4. Log
		log.info("削除担任・学生ユーザ登録テスト失敗: userId={}, deletedAt={}", userId, deletedAt);
	}

	@Test
	public void testInsertDeleteUserOne_Success() {
		// 0. Mock setup
		doReturn(1).when(jdbc).update(anyString(), anyMap());

		// 1. Ready
		Timestamp deletedAt = TEST_TIMESTAMP;
		String userId = TEST_USER_ID;

		// 2. Do
		Boolean result = repository.insertDeleteUserOne(deletedAt, userId);

		// 3. Check
		assertTrue(result);

		// 4. Log
		log.info("削除ユーザ登録テスト成功: userId={}, deletedAt={}", userId, deletedAt);
	}

	@Test
	public void testInsertDeleteUserOne_Failure() {
		// 0. Mock setup
		doReturn(0).when(jdbc).update(anyString(), anyMap()); // Update count is 0

		// 1. Ready
		Timestamp deletedAt = TEST_TIMESTAMP;
		String userId = TEST_USER_ID;

		// 2. Do
		Boolean result = repository.insertDeleteUserOne(deletedAt, userId);

		// 3. Check
		assertFalse(result);

		// 4. Log
		log.info("削除ユーザ登録テスト失敗: userId={}, deletedAt={}", userId, deletedAt);
	}

}
