package com.api.domain.services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.domain.models.dbdata.UserData;
import com.api.domain.models.displaydata.CertificateCsvData;
import com.api.domain.models.displaydata.JobSearchData;
import com.api.domain.models.displaydata.UserDataDisplay;
import com.api.domain.models.displaydata.UserDetailDisplay;
import com.api.domain.models.entities.UserDeleteEntity;
import com.api.domain.models.forms.UserForm;
import com.api.domain.models.forms.UserUpdateForm;
import com.api.domain.repositories.CsvRepository;
import com.api.domain.repositories.JobSearchRepository;
import com.api.domain.repositories.UserRepository;
import com.api.jobpal.common.base.DBDataConversion;
import com.api.jobpal.common.base.Util;

/**
 * ユーザ情報を管理するサービスクラスです。
 *
 * <p>
 * 以下の処理を行います。
 * <ul>
 * <li>ユーザ情報の取得（全件/個別）</li>
 * <li>ユーザ情報の登録</li>
 * <li>ユーザ情報の更新</li>
 * <li>ユーザ情報の削除</li>
 * <li>削除ユーザ情報のCSVデータ取得</li>
 * </ul>
 * </p>
 *
 * <p>
 * 処理が継続できない場合は、呼び出し元へ例外をスローすることはありません。<br>
 * 必ず結果を返却する形式となっており、エラーハンドリングを内部で実施します。
 * </p>
 *
 * <p>
 * <strong>呼び出し元では、返却された結果を元に適切なハンドリングを行ってください。</strong>
 * </p>
 */

@Transactional
@Service
public class UserService {
	/**
	 * ユーザ情報を操作するリポジトリ
	 */
	@Autowired
	private UserRepository userRepository;

	/**
	 * 就職活動リポジトリ。
	 */
	@Autowired
	private JobSearchRepository jobSearchRepository;

	/**
	 * CSVデータを操作するリポジトリ
	 */
	@Autowired
	private CsvRepository csvRepository;

	/**
	 * データベースデータの変換を担当するユーティリティ
	 */
	@Autowired
	private DBDataConversion dbdc;

	/**
	 * 通知サービス。
	 */
	@Autowired
	private NotificationService notificationService;

	/**
	 * ユーザ情報を全件取得します。
	 *
	 * <p>
	 * エラーによって取得できない場合は空のユーザ情報が返却されます。
	 *
	 * @return resultList ユーザ情報リスト
	 */
	public List<UserDataDisplay> getUserList() {
		List<UserData> newUserDataList;
		List<UserDataDisplay> userDataDisplayList = new ArrayList<>();
		try {
			newUserDataList = userRepository.selectTeacherStudentAll(false);
		} catch (DataAccessException e) {
			return null;
		}
		for (int i = 0; i < newUserDataList.size(); i++) {
			UserData userData = newUserDataList.get(i);

			// 整形処理を参考にして各フィールドを設定
			String userId = userData.getUserId();
			String password = userData.getPassword();
			String userName = userData.getUserName();
			String userClass = Util.formatUserClass(userData.getDepartment(), userData.getGrade(),
					userData.getClassName());
			Integer classNumber = Util.parseAttendanceNumber(userData.getAttendanceNumber());
			Integer schoolNumber = Util.getValidStudentId(userData.getStudentId());
			String grant = userData.getUserType();
			String status = Util.getStatusName(userData.getUserStatus());
			String createUserId = Util.getValidCreateUserId(userData.getCreatedByUserId());
			Timestamp createDatetime = userData.getCreatedAt();

			// UserDataDisplayのビルダーを使用して新しいインスタンスを生成
			UserDataDisplay userDataDisplay = new UserDataDisplay.Builder()
					.setNumber(i) // 添え字をnumberフィールドに設定
					.setUserId(userId)
					.setPassword(password)
					.setUserName(userName)
					.setUserClass(userClass)
					.setClassNumber(classNumber)
					.setSchoolNumber(schoolNumber)
					.setGrant(grant)
					.setStatus(status)
					.setCreateUserId(createUserId)
					.setCreateDatetime(createDatetime)
					.build();

			userDataDisplayList.add(userDataDisplay);
		}
		return userDataDisplayList;
	}

	/**
	 * ユーザ情報を1件取得します
	 *
	 * @param userId ユーザID(null不可)
	 * @return userData ユーザ情報
	 */
	public UserDetailDisplay getUser(String userId) {
		UserData userData;
		try {
			userData = userRepository.selectTeacherStudentOne(userId, false);
		} catch (DataAccessException e) {
			return null;
		}

		// 整形処理を参考にして各フィールドを設定
		String password = userData.getPassword();
		String userName = userData.getUserName();
		String userClass = Util.formatUserClass(userData.getDepartment(), userData.getGrade(), userData.getClassName());
		Integer classNumber = Util.parseAttendanceNumber(userData.getAttendanceNumber());
		Integer schoolNumber = Util.getValidStudentId(userData.getStudentId());
		String grant = userData.getUserType();
		String status = Util.getStatusName(userData.getUserStatus());
		String createUserId = Util.getValidCreateUserId(userData.getCreatedByUserId());
		Timestamp createDatetime = userData.getCreatedAt();

		UserDetailDisplay userDetail = new UserDetailDisplay.Builder(userName)
				.setUserId(userId)
				.setPassword(password)
				.setUserClass(userClass)
				.setClassNumber(classNumber)
				.setSchoolNumber(schoolNumber)
				.setGrant(grant)
				.setStatus(status)
				.setCreateUserId(createUserId)
				.setCreateDatetime(createDatetime)
				.build();

		return userDetail;
	}

	/**
	 * UserFormをuserDataへ変換します。
	 *
	 * <p>
	 * <strong>このメソッドは入力チェックを実施したうえで呼び出すこと</strong>
	 *
	 * @param userForm 入力データ(null不可)
	 * @return userData ユーザ情報
	 */
	private UserData refillToData(UserForm userForm) {

		// 整形処理を参考にして各フィールドを設定
		String userId = userForm.getUserId();
		String password = userForm.getPassword();
		String userName = userForm.getUserName();
		String[] userClass = Util.splitString(userForm.getUserClass().toUpperCase());
		String status = Util.getStatusCode(userForm.getStatus());
		String userType = userForm.getGrant();
		String createUserId = Util.getValidCreateUserId(userForm.getCreateUserId());
		String Department = userClass[0];
		Integer Grade = Integer.valueOf(userClass[1]);
		String ClassName = userClass[2];
		String attendanceNumber = Util.formatToTwoDigits(userForm.getClassNumber());
		Integer StudentId = userForm.getSchoolNumber();

		UserData userData = new UserData.Builder()
				.setUserId(userId)
				.setPassword(password)
				.setUserName(userName)
				.setUserStatus(status)
				.setUserType(userType)
				.setCreatedByUserId(createUserId)
				.setDepartment(Department)
				.setGrade(Grade)
				.setClassName(ClassName)
				.setAttendanceNumber(attendanceNumber)
				.setStudentId(StudentId)
				.build();
		return userData;
	}

	/**
	 * ユーザ情報を1件追加します。
	 *
	 * <p>
	 * 呼び出し元はBooleanの戻り値にて、処理の結果を判定することができます。
	 *
	 * @param userForm ユーザ入力データ(null不可)
	 * @return 正常終了の場合はtrue, その他の場合はfalse
	 */
	public Boolean insertOne(UserForm userForm) {
		UserData userData = refillToData(userForm);

		// 既に存在するユーザIDの場合はfalse
		Boolean isSuccess = userRepository.existsUser(userForm.getUserId());
		if (isSuccess) {
			return false;
		}

		// ユーザ情報をDBに登録
		isSuccess = userRepository.insertUserOne(userData);
		if (!isSuccess) {
			return false;
		}

		// ユーザ区分が担任または学生の場合、追加情報を登録
		if (Integer.parseInt(userData.getUserType()) <= 1) {
			isSuccess = userRepository.insertTeacherStudentUserOne(userData);
		}

		// 正常に登録が完了した場合はtrueを返却
		return isSuccess;
	}

	/**
	 * ユーザ情報を１件更新します。
	 *
	 * <p>
	 * 呼び出し元はBooleanの戻り値にて、処理の結果を判定することができます。
	 *
	 * @param userDataForm ユーザ入力データ(null不可)
	 * @return 正常終了の場合はtrue, その他の場合はfalse
	 */
	public Boolean updateOne(UserUpdateForm userDataForm) {
		UserData userData = new UserData.Builder()
				.setUserId(userDataForm.getUserId())
				.setPassword(userDataForm.getPassword())
				.setUserName(userDataForm.getUserName())
				.setUserStatus(Util.getStatusCode(userDataForm.getStatus()))
				.setUserType(userDataForm.getGrant())
				.setDepartment(Util.splitString(userDataForm.getUserClass())[0])
				.setGrade(Integer.valueOf(Util.splitString(userDataForm.getUserClass())[1]))
				.setClassName(Util.splitString(userDataForm.getUserClass())[2])
				.setAttendanceNumber(Util.formatToTwoDigits(userDataForm.getClassNumber()))
				.setStudentId(userDataForm.getSchoolNumber())
				.build();
		try {
			boolean isSuccess;
			// パスワードが空文字であるかを判定
			if (userData.getPassword().isBlank()) {
				isSuccess = updateUser(userData, false);
			} else {
				isSuccess = updateUser(userData, true);
			}

			return isSuccess;

		} catch (IncorrectResultSizeDataAccessException e) {
			return false;
		}
	}

	/**
	 * ユーザ情報を更新します。
	 *
	 * <p>
	 * 指定されたユーザ情報を基に、データベース内のユーザ情報を更新します。<br>
	 * ユーザ区分が担任または学生の場合は、関連する追加情報も更新されます。
	 * </p>
	 *
	 * @param userData    更新対象のユーザ情報
	 * @param outPassword パスワードを含めて更新するかのフラグ
	 * @return 更新が成功した場合は`true`、失敗した場合は`false`
	 */
	private Boolean updateUser(UserData userData, Boolean outPassword) {
		// 基本ユーザ情報を更新
		Boolean isSuccess = userRepository.updateUserOne(userData, outPassword);

		// ユーザ区分が担任または学生の場合、関連情報を更新
		if (Integer.parseInt(userData.getUserType()) <= 1) {
			if (!isSuccess) {
				// 基本情報の更新が失敗した場合はfalseを返却
				return false;
			}
			isSuccess = userRepository.updateTeacherStudentOne(userData);
		}

		// 正常に更新された場合はtrueを返却
		return isSuccess;
	}

	/**
	 * ユーザを一件削除します
	 *
	 * <p>
	 * 論理削除となります
	 * </p>
	 *
	 * @param userId ユーザID(null不可)
	 * @return 正常終了の場合はtrue, その他の場合はfalse
	 */
	public Boolean deleteOne(String userId) {
		try {
			// 現在のタイムスタンプを取得
			Timestamp deletedAt = dbdc.getNowTime();

			// ユーザ区分を取得
			Integer userType = Integer.valueOf(userRepository.selectUserType(userId));

			// 削除テーブルにユーザ情報を追加
			Boolean isSuccess = userRepository.insertDeleteUserOne(deletedAt, userId);

			if (!isSuccess) {
				return false;
			}

			// ユーザ区分が担任または学生の場合、関連情報も削除テーブルに追加
			if (userType <= 1) {
				isSuccess = userRepository.insertDeleteTeacherStudentUserOne(deletedAt, userId);
				if (!isSuccess) {
					return false;
				}
			}

			// ユーザ情報を削除
			return userRepository.deleteUserOne(userId);

		} catch (IncorrectResultSizeDataAccessException e) {
			return false;
		}
	}

	/**
	 * 指定されたユーザIDに関連する削除ユーザ情報のCSVデータを取得します。
	 *
	 * <p>
	 * 指定されたユーザIDを基に、関連する就職活動データおよび証明書発行データを取得し、
	 * 削除ユーザ情報として構築します。<br>
	 * 構築されたデータはCSV形式で出力するための準備データとして返却されます。
	 * </p>
	 *
	 * @param userId 削除するユーザID
	 * @return 削除ユーザ情報を保持するMap。キーは`"csvList"`、値は削除ユーザ情報のリスト。
	 */
	public Map<String, List<UserDeleteEntity>> getDeleteUserCsv(String userId) {

		// ユーザ名を取得
		String userName = userRepository.selectUserName(userId);

		// 就職活動IDリストを取得し、就職活動データリストを構築
		List<String> jobSearchList = csvRepository.selectJobSearchIdList(userId);
		List<JobSearchData> jobList = new ArrayList<>();
		for (String jobSearchId : jobSearchList) {
			JobSearchData jobSearchData = new JobSearchData();
			jobSearchData.setJobHuntId(jobSearchId);
			String status = jobSearchRepository.selectJobSearchStatus(jobSearchId);
			boolean isSuccess = jobSearchRepository.updateJobSearchStatus("00", jobSearchId);
			if (!isSuccess) {
				return null;
			}
			if (!("33".equals(status) || "00".equals(status))) {
				isSuccess = notificationService.deleteJobsearchNotification(jobSearchId);
				if (!isSuccess) {
					return null;
				}
			}
			jobList.add(jobSearchData);
		}

		// 証明書発行IDリストを取得し、証明書発行データリストを構築
		List<String> certificateList = csvRepository.selectCertificateIssuanceIdList(userId);
		List<CertificateCsvData> certificateDataList = new ArrayList<>();
		for (String certificateIssueId : certificateList) {
			CertificateCsvData certificateCsvData = new CertificateCsvData();
			certificateCsvData.setCertificateIssueId(certificateIssueId);
			boolean isSuccess = notificationService.deleteCertificateNotification(certificateIssueId);
			if (!isSuccess) {
				return null;
			}
			certificateDataList.add(certificateCsvData);
		}

		// 削除ユーザ情報エンティティを構築
		UserDeleteEntity userDeleteEntity = new UserDeleteEntity.Builder()
				.setUserId(userId)
				.setUserName(userName)
				.setJob(jobList)
				.setCertificate(certificateDataList)
				.build();

		// Mapにデータを格納
		List<UserDeleteEntity> userDeleteEntityList = new ArrayList<>();
		userDeleteEntityList.add(userDeleteEntity);
		Map<String, List<UserDeleteEntity>> csvList = new HashMap<>();
		csvList.put("csvList", userDeleteEntityList);

		return csvList;
	}
}
