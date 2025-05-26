package com.api.domain.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.domain.models.dbdata.JobSearchDashBoardData;
import com.api.domain.models.displaydata.CsvDisplayData;
import com.api.domain.models.displaydata.DashBoardDisplayData;
import com.api.domain.models.entities.DashBoardDisplayEntity;
import com.api.domain.models.entities.DashBoardEntity;
import com.api.domain.repositories.CsvRepository;
import com.api.domain.repositories.JobSearchDashBoardRepository;
import com.api.domain.repositories.NotificationRepository;
import com.api.jobpal.common.base.Util;

/**
 * ダッシュボード表示に関連するビジネスロジックを管理するサービスクラスです。
 *
 * <p>
 * 以下の処理を行います。
 * <ul>
 * <li>全ユーザの申請データ取得と表示データ構築</li>
 * <li>指定ユーザの申請データ取得と表示データ構築</li>
 * <li>ダッシュボードデータのCSV表示データ追加</li>
 * </ul>
 * </p>
 *
 * <p>
 * 処理が継続できない場合は、例外を内部で処理し、適切な結果を返却します。<br>
 * 呼び出し元は返却されたデータを基に適切な処理を行ってください。
 * </p>
 */

@Service
public class DashBoardService {
	/**
	 * 就職活動ダッシュボードリポジトリ。
	 */
	@Autowired
	private JobSearchDashBoardRepository dashBoardRepository;
	/**
	 * 通知リポジトリ。
	 */
	@Autowired
	private NotificationRepository notificationRepository;
	/**
	 * CSVリポジトリ。
	 */
	@Autowired
	private CsvRepository csvRepository;

	/**
	 * 全ユーザーの申請データを取得し、ダッシュボードに表示するデータを構築します。
	 *
	 * @param userId   ユーザID
	 * @param userType ユーザ種別 ("2"が管理者を表します)　
	 * @return DashBoardDisplayEntity ダッシュボード表示用エンティティ
	 */
	public DashBoardDisplayEntity getDashBoardAll(String userId, String userType) {

		// 管理者フラグを判定
		Boolean adminFlg = "2".equals(userType);

		// ダッシュボード用のデータを取得
		DashBoardEntity dashBoardEntity = dashBoardRepository.selectDashBoardAll(adminFlg);

		DashBoardDisplayEntity dashBoardDisplayEntity = new DashBoardDisplayEntity();
		dashBoardDisplayEntity.setDashBoardList(new ArrayList<>());

		// ダッシュボードデータを表示用データに変換
		for (JobSearchDashBoardData dashBoardData : dashBoardEntity.getDashBoardList()) {
			DashBoardDisplayData data = convertToDashBoardDisplayData(dashBoardData);
			dashBoardDisplayEntity.getDashBoardList().add(data);
		}

		// 通知件数を取得
		int alertCnt = notificationRepository.getNotificationCount(userId);
		dashBoardDisplayEntity.setAlertCnt(alertCnt);

		// CSV表示用データを追加
		dashBoardDisplayEntity.setCsvList(new ArrayList<>());
		dashBoardDisplayEntity = addCsvData(dashBoardDisplayEntity, userId);

		return dashBoardDisplayEntity;
	}

	/**
	 * ダッシュボードデータを表示用データ形式に変換します。
	 *
	 * @param dashBoardData ダッシュボードデータ
	 * @return DashBoardDisplayData 表示用データ
	 */
	private DashBoardDisplayData convertToDashBoardDisplayData(JobSearchDashBoardData dashBoardData) {
		String className = dashBoardData.getDepartment() + dashBoardData.getGrade() + dashBoardData.getClassName();
		String classNumber = Util.formatToTwoDigits(dashBoardData.getAttendanceNumber());
		String schoolClassNumber = className + classNumber;

		return new DashBoardDisplayData.Builder()
				.setJobHuntId(dashBoardData.getJobSearchId())
				.setUserId(dashBoardData.getUserId())
				.setUserName(dashBoardData.getUserName())
				.setSchoolClassNumber(schoolClassNumber)
				.setJobStateId(Util.getJobStateShortName(dashBoardData.getJobSearchStatus()))
				.setCompany(dashBoardData.getCompanyName())
				.setEventCategory(Util.getEventCategoryDescription(dashBoardData.getEventCategory()))
				.setResult(Util.getResultStatus(dashBoardData.getResult()))
				.setSchoolCheck(dashBoardData.getSchoolCheckFlag())
				.setDate(Util.formatDate(dashBoardData.getStartTime(), "MM/dd"))
				.setStartTime(Util.formatDate(dashBoardData.getStartTime(), "HH:mm"))
				.setFinishTime(Util.formatDate(dashBoardData.getEndTime(), "HH:mm"))
				.setEventCategoryPriority(Integer.valueOf(dashBoardData.getEventCategory()))
				.setStateIdPriority(Integer.valueOf(dashBoardData.getJobSearchStatus()))
				.setStartTimePriority(Long.valueOf(Util.formatDate(dashBoardData.getStartTime(), "yyyyMMddHHmmss")))
				.build();
	}

	/**
	 * ダッシュボード表示エンティティにCSV表示データを追加します。
	 *
	 * @param dashBoardDisplayEntity 表示用エンティティ
	 * @param userId                 ユーザID
	 * @return DashBoardDisplayEntity 更新後の表示用エンティティ
	 */
	private DashBoardDisplayEntity addCsvData(DashBoardDisplayEntity dashBoardDisplayEntity, String userId) {
		Integer studentsInAction = csvRepository.selectCsvActivity(userId);
		Integer studentsOfEnd = csvRepository.selectCsvActivityFinish(userId);
		Integer activityLocationInTokyo = csvRepository.selectCsvTokyo(userId);
		Integer activityLocationInSapporo = csvRepository.selectCsvSapporo(userId);
		Integer activityLocationInOther = csvRepository.selectCsvOthers(userId);
		Integer activityFormInOnline = 0;
		Integer activityFormInLocal = 0;
		Integer activityFormInOther = 0;

		// 活動形式に応じたデータをカウント
		List<String> csvLocationList = csvRepository.selectCsvLocationList(userId);
		for (String csvLocation : csvLocationList) {
			switch (csvLocation) {
			case "自宅" -> activityFormInLocal++;
			case "学校" -> activityFormInOther++;
			default -> activityFormInOnline++;
			}
		}

		CsvDisplayData csvDisplayData = new CsvDisplayData.Builder()
				.studentsInAction(studentsInAction)
				.studentsOfEnd(studentsOfEnd)
				.activityLocationInTokyo(activityLocationInTokyo)
				.activityLocationInSapporo(activityLocationInSapporo)
				.activityLocationInOther(activityLocationInOther)
				.activityFormInOnline(activityFormInOnline)
				.activityFormInLocal(activityFormInLocal)
				.activityFormInOther(activityFormInOther)
				.build();

		dashBoardDisplayEntity.getCsvList().add(csvDisplayData);
		return dashBoardDisplayEntity;
	}

	/**
	 * 指定ユーザの申請データを取得し、ダッシュボードに表示するデータを構築します。
	 *
	 * @param userId ユーザID
	 * @return DashBoardDisplayEntity ダッシュボード表示用エンティティ
	 */
	public DashBoardDisplayEntity getDashBoardUser(String userId) {

		// 指定ユーザのダッシュボードデータを取得
		DashBoardEntity dashBoardEntity = dashBoardRepository.selectDashBoardStudent(userId);

		DashBoardDisplayEntity dashBoardDisplayEntity = new DashBoardDisplayEntity();
		dashBoardDisplayEntity.setDashBoardList(new ArrayList<>());

		// ダッシュボードデータを表示用データに変換
		for (JobSearchDashBoardData dashBoardData : dashBoardEntity.getDashBoardList()) {
			DashBoardDisplayData data = convertToDashBoardDisplayData(dashBoardData);
			dashBoardDisplayEntity.getDashBoardList().add(data);
		}

		// 通知件数を取得
		int alertCnt = notificationRepository.getNotificationCount(userId);
		dashBoardDisplayEntity.setAlertCnt(alertCnt);

		// CSV表示用データを追加（現在の実装では空）
		dashBoardDisplayEntity.setCsvList(null);

		return dashBoardDisplayEntity;
	}
}
