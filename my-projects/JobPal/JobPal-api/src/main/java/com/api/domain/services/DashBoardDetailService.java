package com.api.domain.services;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.api.domain.models.dbdata.ExamReportData;
import com.api.domain.models.dbdata.JobSearchDashBoardDetailData;
import com.api.domain.models.dbdata.JobSearchReportData;
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
import com.api.jobpal.common.base.Util;

/**
 * ダッシュボード詳細データに関連するビジネスロジックを管理するサービスクラスです。
 *
 * <p>
 * 以下の処理を行います。
 * <ul>
 * <li>申請詳細データの取得と表示形式への変換</li>
 * <li>申請データの更新（受験報告、申請書状態、詳細データ）</li>
 * <li>申請データの削除</li>
 * <li>AIを用いた受験報告の評価</li>
 * </ul>
 * </p>
 *
 * <p>
 * 処理が継続できない場合、適切なエラーハンドリングを行い、呼び出し元がエラー状態を判定できるようにします。<br>
 * 呼び出し元は返却された結果に基づいて適切な処理を実施してください。
 * </p>
 */
@Service
public class DashBoardDetailService {
	/**
	 * 就職活動ダッシュボードリポジトリ。
	 */
	@Autowired
	private JobSearchDashBoardRepository dashBoardRepository;
	/**
	 * 就職活動リポジトリ。
	 */
	@Autowired
	private JobSearchRepository jobSearchRepository;
	/**
	 * 就職活動申請リポジトリ。
	 */
	@Autowired
	private JobSearchApplicationRepository applicationRepository;
	/**
	 * 試験報告リポジトリ。
	 */
	@Autowired
	private ExamReportRepository jobExamRepository;
	/**
	 * 就職活動報告リポジトリ。
	 */
	@Autowired
	private JobSearchReportRepository jobSearchReportRepository;
	/**
	 * ユーザリポジトリ。
	 */
	@Autowired
	private UserRepository newUserRepository;
	/**
	 * 通知サービス。
	 */
	@Autowired
	private NotificationService notificationService;
	/**
	 * データベースデータ変換ユーティリティ。
	 */
	@Autowired
	private DBDataConversion dbdc;
	/**
	 * RESTテンプレート。
	 */
	@Autowired
	private RestTemplate restTemplate;

	/**
	 * 指定された申請IDに基づき、申請詳細データを取得します。
	 *
	 * @param jobHantId 申請ID
	 * @return DetailDisplayData 申請詳細表示用データ
	 */
	public DetailDisplayData getDashBoardDetail(String jobHantId) {
		// ダッシュボードデータを取得
		JobSearchDashBoardDetailData dashBoardDetailData = dashBoardRepository
				.selectJobSearchDashBoardDetail(jobHantId);
		// データをDetailDisplayData形式に変換
		return copyRefillData(dashBoardDetailData);
	}

	/**
	 * ダッシュボード詳細データをDetailDisplayData形式に変換します。
	 *
	 * @param dashBoardDetailData ダッシュボード詳細データ
	 * @return DetailDisplayData
	 */
	private DetailDisplayData copyRefillData(JobSearchDashBoardDetailData dashBoardDetailData) {
		String jobHuntId = dashBoardDetailData.getJobSearchId();
		String userName = dashBoardDetailData.getUserName();
		String jobStateId = Util.getJobStateName(dashBoardDetailData.getJobSearchStatus());
		String userClass = Util.formatUserClass(dashBoardDetailData.getDepartment(), dashBoardDetailData.getGrade(),
				dashBoardDetailData.getClassName());
		String classNumber = dashBoardDetailData.getAttendanceNumber();
		Integer schoolNumber = Util.getValidStudentId(dashBoardDetailData.getStudentId());
		String company = dashBoardDetailData.getCompanyName();
		String eventCategory = Util.getEventCategoryDescription(dashBoardDetailData.getEventCategory());
		String location = dashBoardDetailData.getLocation();
		String locationType = Util.getLocationTypeDescription(dashBoardDetailData.getLocationType());
		Boolean schoolCheck = dashBoardDetailData.getSchoolCheckFlag();
		Boolean schoolCheckedFlag = dashBoardDetailData.getSchoolCheckedFlag();
		String startTime = Util.formatDate(dashBoardDetailData.getStartTime(), "MM/dd HH:mm");
		Timestamp updateTime = dashBoardDetailData.getMaxUpdatedAt();
		String tardinessAbsenceType = Util
				.getTardinessAbsenceDescription(dashBoardDetailData.getTardinessAbsenceType());
		String reportContent = dashBoardDetailData.getReportContent();
		String result = Util.getResultStatus(dashBoardDetailData.getResult());
		String supportedCnt = Util.formatToString(dashBoardDetailData.getExamOpponentCount());
		String jobTitle = dashBoardDetailData.getExamOpponentPosition();
		Integer examCategory = Util.parseAttendanceNumber(dashBoardDetailData.getExamCount());
		String examContent = Util.getExamCategoryStatus(dashBoardDetailData.getExamType());
		String examinationContent = dashBoardDetailData.getExamContent();
		String thoughts = dashBoardDetailData.getImpressions();
		String remarks = dashBoardDetailData.getRemarks();
		Timestamp tradyLeaveDate = dashBoardDetailData.getEndTime();
		String[] remarksList = new String[1];
		if (remarks != null) {
			remarksList = remarks.split(" ");
		}
		// 遅刻早退がある場合
		if (remarksList.length > 1) {
			remarks = remarksList[1];
			// 遅刻早退時間の取得
			tradyLeaveDate = Timestamp.valueOf(remarksList[0] + " 00:00:00");
		}
		String predResult = evaluateExamReport(dashBoardDetailData.getImpressions());
		String finishTime = Util.formatDate(dashBoardDetailData.getEndTime(), "MM/dd HH:mm");
		String tardyLeaveTime = Util.getTardyLeaveTime(tradyLeaveDate,
				dashBoardDetailData.getTardyLeaveTime());
		DetailDisplayData detailDisplayData = new DetailDisplayData.Builder()
				.setJobHuntId(jobHuntId)
				.setUserName(userName)
				.setJobStateId(jobStateId)
				.setUserClass(userClass)
				.setClassNumber(classNumber)
				.setSchoolNumber(schoolNumber)
				.setCompany(company)
				.setEventCategory(eventCategory)
				.setLocation(location)
				.setLocationType(locationType)
				.setSchoolCheck(schoolCheck)
				.setSchoolCheckedFlag(schoolCheckedFlag)
				.setStartTime(startTime)
				.setFinishTime(finishTime)
				.setUpdateTime(updateTime)
				.setTardinessAbsenceType(tardinessAbsenceType)
				.setReportContent(reportContent)
				.setResult(result)
				.setSupportedCnt(supportedCnt)
				.setJobTitle(jobTitle)
				.setExamCategory(examCategory)
				.setExamContent(examContent)
				.setEcaminationContent(examinationContent)
				.setThoughts(thoughts)
				.setTardyLeaveTime(tardyLeaveTime)
				.setRemarks(remarks)
				.setPredResult(predResult)
				.build();
		return detailDisplayData;
	}

	/**
	 * ユーザーの受験報告を更新します。
	 *
	 * @param detailFormForUpdate 更新データフォーム
	 * @return 更新成功時true, 失敗時false
	 */
	public boolean updateDashBoardDetail(DetailFormForUpdate detailFormForUpdate) {
		// 更新用のデータを作成
		ExamReportData jobExamData = refillDate(detailFormForUpdate);
		String jobSearchId = jobExamData.getJobSearchId();
		try {
			// 既存データがある場合は更新、なければ挿入
			boolean exists = dbdc.existsJobSearchId(jobSearchId, "ER");
			if (exists) {
				return jobExamRepository.updateExamReport(jobExamData);
			} else {
				return jobExamRepository.insertExamReport(jobExamData);
			}
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 更新フォームからExamReportDataを作成します。
	 *
	 * @param detailFormForUpdate 更新フォーム
	 * @return ExamReportData 更新データ
	 */
	private ExamReportData refillDate(DetailFormForUpdate detailFormForUpdate) {
		return new ExamReportData.Builder()
				.setExamOpponentCount(detailFormForUpdate.getSupportedCnt())
				.setExamOpponentPosition(detailFormForUpdate.getJobTitle())
				.setExamCount(Util.formatToString(detailFormForUpdate.getExamCategory()))
				.setExamType(Util.getExamCategoryCode(detailFormForUpdate.getExamContentId()))
				.setExamContent(detailFormForUpdate.getExaminationContent())
				.setImpressions(detailFormForUpdate.getThoughts())
				.setJobSearchId(detailFormForUpdate.getJobHuntId())
				.build();
	}

	/**
	 * 申請書の状態を更新するメソッド
	 *
	 * @param jobHuntId  申請書ID
	 * @param JobStateId 申請書状態ID
	 * @return true|false
	 */
	public boolean jobStateUpdate(String jobHuntId, String jobStateId, Integer buttonId, boolean schoolCheck) {
		Integer state_num = Integer.valueOf(Util.getJobStateCode(jobStateId).get(0));
		// 状態の更新
		try {
			String userId = jobSearchRepository.selectJobSearchUserId(jobHuntId);
			switch (buttonId) {
			// 承認ボタン
			case 0 -> {
				if (schoolCheck && state_num == 11) {
					applicationRepository.updateSchoolCheckedOne(jobHuntId);
				}
				state_num++;
				if (state_num == 33) {
					break;
				}
				if (state_num % 10 == 3) {
					state_num += 8;
					break;
				}
				if (state_num % 10 == 4) {
					state_num += 7;
					break;
				}
			}
			// 取り下げボタン
			case 1 -> {
				state_num = Math.floorDiv(state_num, 10) * 10 + 3;
				if (state_num == 33) {
					state_num++;
				}
			}
			// 差戻しボタン
			case 2 -> {
				state_num = Math.floorDiv(state_num, 10) * 10 + 3;
				if (state_num == 33) {
					state_num++;
				}
			}
			// コース担当承認ボタン
			case 3 -> {
				state_num = 21;
			}
			}

			Integer eventCategory = Integer.valueOf(applicationRepository.selectEventCategory(jobHuntId));
			if (state_num == 21 && !(eventCategory >= 2 && eventCategory <= 4)) {
				state_num = 31;
			}

			boolean isSuccess = jobSearchRepository.updateJobSearchStatus(state_num.toString(), jobHuntId);
			if (!isSuccess) {
				return false;
			}
			if (state_num == 33) {
				// 通知データの削除
				return notificationService.deleteJobsearchNotification(jobHuntId);
			} else if (state_num == 12) {
				// 学生への通知データの追加
				isSuccess = notificationService.insertJobsearchNotification(jobHuntId, userId);
				if (!isSuccess) {
					return false;
				}
				userId = newUserRepository.selectTeacherUserId(userId);
				// 担任への通知データの追加
				return notificationService.insertJobsearchNotification(jobHuntId, userId, true);
			} else {
				// 通知データの追加
				return notificationService.insertJobsearchNotification(jobHuntId, userId);
			}
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 指定された申請IDのデータを削除します。
	 *
	 * @param jobHuntId 申請ID
	 * @return 削除成功時true, 失敗時false
	 */
	public boolean deleteOne(String jobHuntId) {
		try {
			// 申請書の削除
			boolean isSuccess = jobSearchRepository.deleteJobSearchStatus(jobHuntId);
			if (!isSuccess) {
				return false;
			}
			// 通知データの削除
			return notificationService.deleteJobsearchNotification(jobHuntId);
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 申請書の状態を更新するメソッド
	 *
	 * @param jobHuntId
	 * @return true|false
	 */
	public boolean advancedJobStateId(String jobHuntId) {
		try {
			String jobStateId = jobSearchRepository.selectJobSearchStatus(jobHuntId);
			String advancedJobStateId = incrementSecondChar(jobStateId);
			// 状態の更新
			boolean isSuccess = jobSearchRepository.updateJobSearchStatus(advancedJobStateId, jobHuntId);
			if (!isSuccess) {
				return false;
			}
			// 通知データの追加
			String userId = newUserRepository.selectTeacherUserId(jobSearchRepository.selectJobSearchUserId(jobHuntId));
			return notificationService.insertJobsearchNotification(jobHuntId, userId);
		} catch (IncorrectResultSizeDataAccessException e) {
			return false;
		} catch (IllegalArgumentException e) {
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 指定された文字列の二文字目を 2 にした新しい文字列を返します。
	 *
	 * @param input 元の文字列（例: "E1", "R1"）
	 * @return 二文字目を 2 した新しい文字列（例: "E2", "R2"）
	 */
	private String incrementSecondChar(String input) {
		if (input == null || input.length() < 2) {
			throw new IllegalArgumentException("Input string must be at least 2 characters long");
		}
		// 一文字目を取得
		char firstChar = input.charAt(0);
		// 新しい文字列を構築
		return firstChar + String.valueOf(2);
	}

	/**
	 * 就職活動報告の更新
	 *
	 * @param detailFormForReport
	 * @return
	 */
	public boolean reportDashBoardDetail(DetailFormForReport detailFormForReport) {
		JobSearchReportData jobSearchReportData = new JobSearchReportData.Builder()
				.setJobSearchId(detailFormForReport.getJobHuntId())
				.setReportContent(detailFormForReport.getReportContent())
				.setResult(Util.getResultStatusCode(detailFormForReport.getResult()))
				.build();
		// 報告のデータの追加
		try {
			boolean result = dbdc.existsJobSearchId(jobSearchReportData.getJobSearchId(), "JR");
			if (result) {
				jobSearchReportRepository.updateJobSearchReport(jobSearchReportData);
			} else {
				jobSearchReportRepository.insertJobSearchReport(jobSearchReportData);
			}
		} catch (IncorrectResultSizeDataAccessException e) {
			return false;
		} catch (Exception ex) {
			return false;
		}
		return true;
	}

	/**
	 * AIを用いて受験報告を評価します。
	 *
	 * @param examReport 受験報告のテキスト
	 * @return "合格", "不合格", またはエラーメッセージ
	 */
	private String evaluateExamReport(String examReport) {
		if (examReport == null || examReport.isEmpty()) {
			return null;
		}
		try {
			String json = request(examReport);
			return convert(json);
		} catch (Exception e) {
			return "エラーが発生しました。";
		}
	}

	/**
	 * 外部AIサービスにリクエストを送信します。
	 *
	 * @param examReport 受験報告テキスト
	 * @return AIサービスのレスポンス
	 */
	private String request(String examReport) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> request = new HttpEntity<>("{\"text\":\"" + examReport + "\"}", headers);
		return restTemplate.postForObject("http://127.0.0.1:8000/jobdetail", request, String.class);
	}

	/**
	 * AIサービスからのJSONレスポンスを解析します。
	 *
	 * @param json JSONレスポンス
	 * @return 解析結果の文字列
	 */
	private String convert(String json) {
		return json.replace("\"", "");
	}
}
