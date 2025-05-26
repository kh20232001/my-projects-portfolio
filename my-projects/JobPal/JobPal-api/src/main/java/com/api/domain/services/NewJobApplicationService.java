package com.api.domain.services;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;

import com.api.domain.models.dbdata.JobSearchApplicationData;
import com.api.domain.models.dbdata.UserData;
import com.api.domain.models.displaydata.NewJobSerachDisplay;
import com.api.domain.models.forms.NewJobApplicationForm;
import com.api.domain.repositories.JobSearchApplicationRepository;
import com.api.domain.repositories.JobSearchRepository;
import com.api.domain.repositories.UserRepository;
import com.api.jobpal.common.base.Util;

/**
 * 新規就職活動申請を管理するサービスクラスです。
 *
 * <p>
 * 以下の機能を提供します。
 * <ul>
 * <li>ユーザ情報の取得</li>
 * <li>新規就職活動申請の登録</li>
 * <li>就職活動申請の変更</li>
 * <li>日時文字列の変換および検証</li>
 * </ul>
 * </p>
 *
 * <p>
 * 主にフロントエンドから受け取ったデータを基に、必要な処理を実行し、データベースとの連携を行います。
 * また、通知サービスを活用して、関連する通知データの生成・登録を行います。
 * </p>
 */

@Service
public class NewJobApplicationService {

	/*
	 * 就職活動リポジトリ
	 */
	@Autowired
	private JobSearchRepository jobApplicationRepository;

	/*
	 * ユーザリポジトリ
	 */
	@Autowired
	private UserRepository newUserRepository;

	/*
	 * 通知サービス
	 */
	@Autowired
	private NotificationService notificationService;

	/*
	 * 就職活動申請リポジトリ
	 */
	@Autowired
	private JobSearchApplicationRepository jsar;

	/**
	 * 指定されたユーザIDに基づき、ユーザ情報を整形して返却します。
	 *
	 * <p>
	 * ユーザの基本情報（名前、学級、出席番号、学籍番号）を取得し、
	 * ダッシュボード表示用のエンティティに変換します。
	 * </p>
	 *
	 * @param userId ユーザID
	 * @return NewJobSerachDisplay ダッシュボード表示用エンティティ
	 */
	public NewJobSerachDisplay getUserData(String userId) {

		// ユーザ情報を取得（担任・学生専用データ）
		UserData userData = newUserRepository.selectTeacherStudentOne(userId, false);

		// 各項目を整形
		String userName = userData.getUserName();
		String userClass = Util.formatUserClass(userData.getDepartment(), userData.getGrade(), userData.getClassName());
		Integer classNumber = Util.parseAttendanceNumber(userData.getAttendanceNumber());
		Integer schoolNumber = Util.getValidStudentId(userData.getStudentId());

		// 整形した情報をビルダーを用いてエンティティに設定
		return new NewJobSerachDisplay.Builder()
				.setUserName(userName)
				.setUserClass(userClass)
				.setClassNumber(classNumber)
				.setSchoolNumber(schoolNumber)
				.build();
	}

	/**
	 * 新規申請書を追加するメソッド
	 *
	 * <p>
	 * フロントエンドから送信された新規申請データフォームを基に、就職活動および申請情報をデータベースに登録し、
	 * 必要に応じて通知データを挿入します。
	 * </p>
	 *
	 * @param newJobApplicationForm フロントエンドから送信された新規申請データフォーム
	 * @return true 登録成功、false 登録失敗
	 */
	public Boolean addNewJobApplication(NewJobApplicationForm newJobApplicationForm) {

		try {
			// 開始時間と終了時間を Timestamp 型に変換
			Timestamp endTime = Timestamp.valueOf(newJobApplicationForm.getFinishTime());
			Timestamp startTime = Timestamp.valueOf(newJobApplicationForm.getStartTime());

			// 遅刻早退時間を変換
			String tardyLeaveTimeString = newJobApplicationForm.getTardyLeaveTime();
			Time tardyLeaveTime = null;
			String remarks = newJobApplicationForm.getRemarks();
			if (tardyLeaveTimeString != null && !tardyLeaveTimeString.isEmpty()) {
				Timestamp tardyLeaveTimestamp = Timestamp.valueOf(tardyLeaveTimeString);
				tardyLeaveTime = new Time(tardyLeaveTimestamp.getTime());
				String tardyLeaveTimeDateString = tardyLeaveTimeString.split(" ")[0];
				remarks = tardyLeaveTimeDateString + " " + remarks;
			}

			// 就職活動IDを生成
			String jobSearchId = jobApplicationRepository.getJobSearchId();

			// フォームデータを基に新規申請データを構築
			JobSearchApplicationData newJobApplicationData = new JobSearchApplicationData.Builder()
					.setJobSearchId(jobSearchId)
					.setStartTime(startTime)
					.setCompanyName(newJobApplicationForm.getCompany())
					.setEventCategory(Util.getEventCategoryCode(newJobApplicationForm.getEventCategory()))
					.setLocationType(Util.getLocationTypeCode(newJobApplicationForm.getLocationType()))
					.setLocation(newJobApplicationForm.getLocation())
					.setSchoolCheckFlag(newJobApplicationForm.isSchoolCheck())
					.setSchoolCheckedFlag(false) // 初期状態は未承認
					.setTardinessAbsenceType(
							Util.getTardinessAbsenceCode(newJobApplicationForm.getTardinessAbsenceType()))
					.setTardyLeaveTime(tardyLeaveTime)
					.setEndTime(endTime)
					.setRemarks(remarks)
					.build();

			// 就職活動申請データを挿入
			Boolean isSuccess = jobApplicationRepository.// 就職活動をデータベースに登録
					insertJobSearch(jobSearchId, newJobApplicationForm.getUserId());

			// データベース挿入が失敗した場合、false を返却
			if (!isSuccess) {
				return false;
			}

			// 就職活動申請をデータベースに登録
			isSuccess = jsar.insertJobSearchApplication(newJobApplicationData);
			// データベース挿入が失敗した場合、false を返却
			if (!isSuccess) {
				return false;
			}

			// 通知データを挿入
			String userId = newUserRepository.selectTeacherUserId(newJobApplicationForm.getUserId());
			// 正常終了の場合、true を返却 (通知データの挿入が失敗した場合は false)
			return notificationService.insertJobsearchNotification(jobSearchId, userId, true);

		} catch (IncorrectResultSizeDataAccessException e) {
			// データベース挿入中にエラーが発生した場合、false を返却
			return false;
		} catch (DataAccessException e) {
			// データベース挿入中にエラーが発生した場合、false を返却
			return false;
		} catch (Exception e) {
			// その他のエラーが発生した場合、false を返却
			return false;
		}
	}

	/**
	 * 新規申請書を変更するメソッド
	 *
	 * @param newJobApplicationForm フロントエンドから送信された新規申請データフォーム
	 * @return true 成功、false 失敗
	 */
	public Boolean changeNewJobApplication(NewJobApplicationForm newJobApplicationForm) {
		try {
			// 開始時間と終了時間を Timestamp 型に変換
			Timestamp endTime = changeToTimestamp(newJobApplicationForm.getFinishTime());
			Timestamp startTime = changeToTimestamp(newJobApplicationForm.getStartTime());

			// 遅刻早退時間を変換
			String tardyLeaveTimeString = newJobApplicationForm.getTardyLeaveTime();
			Time tardyLeaveTime = null;
			String remarks = newJobApplicationForm.getRemarks();
			if (tardyLeaveTimeString != null && !tardyLeaveTimeString.isEmpty() && !"".equals(tardyLeaveTimeString)) {
				Timestamp tardyLeaveTimestamp = changeToTimestamp(tardyLeaveTimeString);
				tardyLeaveTime = new Time(tardyLeaveTimestamp.getTime());
				String tardyLeaveTimeDateString = String.valueOf(tardyLeaveTimestamp).split(" ")[0];
				remarks = tardyLeaveTimeDateString + " " + newJobApplicationForm.getRemarks();
			}
			String jobSearchId = newJobApplicationForm.getJobHuntId();

			// フォームデータを基に新規申請データを構築
			JobSearchApplicationData newJobApplicationData = new JobSearchApplicationData.Builder()
					.setJobSearchId(jobSearchId)
					.setStartTime(startTime)
					.setCompanyName(newJobApplicationForm.getCompany())
					.setEventCategory(Util.getEventCategoryCode(newJobApplicationForm.getEventCategory()))
					.setLocationType(Util.getLocationTypeCode(newJobApplicationForm.getLocationType()))
					.setLocation(newJobApplicationForm.getLocationType())
					.setSchoolCheckFlag(newJobApplicationForm.isSchoolCheck())
					.setSchoolCheckedFlag(false) // 初期状態は未承認
					.setTardinessAbsenceType(
							Util.getTardinessAbsenceCode(newJobApplicationForm.getTardinessAbsenceType()))
					.setTardyLeaveTime(tardyLeaveTime)
					.setEndTime(endTime)
					.setRemarks(remarks)
					.build();

			// 就職活動申請をデータベースに登録
			Boolean isSuccess = jsar.updateJobSearchApplication(newJobApplicationData);
			// データベース挿入が失敗した場合、false を返却
			if (!isSuccess) {
				return false;
			}

			// 就職活動申請を担任承認待ちに変更
			isSuccess = jobApplicationRepository.updateJobSearchStatus("11", jobSearchId);
			if (!isSuccess) {
				return false;
			}

			// 通知データを挿入
			String userId = newUserRepository.selectTeacherUserId(newJobApplicationForm.getUserId());
			// 正常終了の場合、true を返却 (通知データの挿入が失敗した場合は false)
			return notificationService.insertJobsearchNotification(jobSearchId, userId);

		} catch (IncorrectResultSizeDataAccessException e) {
			// データベース挿入中にエラーが発生した場合、false を返却
			return false;
		} catch (DataAccessException e) {
			// データベース挿入中にエラーが発生した場合、false を返却
			return false;
		} catch (Exception e) {
			// その他のエラーが発生した場合、false を返却
			return false;
		}
	}

	/**
	 * 文字列をTimestamp型に変換します。
	 *
	 * <p>
	 * 入力文字列が有効な日時形式である場合はそのまま変換します。<br>
	 * 日付形式が省略されている場合は、現在の年を補完して変換を行います。
	 * </p>
	 *
	 * @param time 変換対象の日時文字列
	 * @return 変換後のTimestamp型オブジェクト
	 * @throws IllegalArgumentException 入力文字列の形式が正しくない場合
	 */
	private Timestamp changeToTimestamp(String time) {

		// 正規表現に一致するかを確認
		if (isValidDateFormat(time)) {
			return Timestamp.valueOf(time);
		}
		// 現在の年を取得
		int currentYear = LocalDateTime.now().getYear();

		// 文字列を分割
		String[] dateTimeParts = time.split(" ");
		if (dateTimeParts.length != 2) {
			throw new IllegalArgumentException("入力文字列の形式が正しくありません: " + time);
		}

		// 日付部分をさらに分割
		String[] dateParts = dateTimeParts[0].split("/");
		if (dateParts.length != 2) {
			throw new IllegalArgumentException("日付部分の形式が正しくありません: " + dateTimeParts[0]);
		}

		// 時間部分をさらに分割
		String[] timeParts = dateTimeParts[1].split(":");
		if (timeParts.length != 2) {
			throw new IllegalArgumentException("時間部分の形式が正しくありません: " + dateTimeParts[1]);
		}

		// 数値に変換
		int month = Integer.parseInt(dateParts[0]);
		int day = Integer.parseInt(dateParts[1]);
		int hour = Integer.parseInt(timeParts[0]);
		int minute = Integer.parseInt(timeParts[1]);

		// LocalDateTimeを作成
		LocalDateTime parsedDateTime = LocalDateTime.of(currentYear, month, day, hour, minute);

		// Timestampに変換
		return Timestamp.valueOf(parsedDateTime);
	}

	/**
	 * 指定された文字列が有効な日時形式かどうかを検証します。
	 *
	 * <p>
	 * 有効な形式: yyyy-MM-dd HH:mm:ss
	 * </p>
	 *
	 * @param dateStr 検証対象の文字列
	 * @return true: 有効な形式, false: 無効な形式
	 */
	private Boolean isValidDateFormat(String dateStr) {
		// yyyy-MM-dd HH:mm:ss に対応する正規表現
		String regex = "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$";

		// 正規表現に一致するかを確認
		return dateStr != null && dateStr.matches(regex);
	}
}
