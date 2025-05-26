package com.api.jobpal.controller;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.domain.models.displaydata.DetailDisplayData;
import com.api.domain.models.forms.DetailFormForReport;
import com.api.domain.models.forms.DetailFormForUpdate;
import com.api.domain.models.forms.JobStateUpdateForm;
import com.api.domain.services.DashBoardDetailService;
import com.api.jobpal.common.base.BaseResponse;
import com.api.jobpal.common.base.ResponseMessage;

/**
 * ダッシュボード詳細機能を提供するコントローラークラスです。
 *
 * <p>
 * <strong>主な機能:</strong>
 * <ul>
 * <li>就職活動詳細情報の取得</li>
 * <li>就職活動受験報告の更新</li>
 * <li>就職活動状態の更新</li>
 * <li>就職活動申請書の削除</li>
 * </ul>
 * </p>
 *
 * <p>
 * <strong>仕様:</strong>
 * <ul>
 * <li>入力データのバリデーションを行い、不正データは400 Bad Requestを返却</li>
 * <li>成功時は適切なレスポンスメッセージを返却</li>
 * <li>エラー時には内部サーバエラーレスポンスを返却</li>
 * </ul>
 * </p>
 *
 * <p>
 * <strong>注意点:</strong>
 * <ul>
 * <li>クライアント側で入力チェックを実施することを推奨</li>
 * <li>更新処理はデータ整合性を保つよう設計</li>
 * </ul>
 * </p>
 *
 * <p>
 * <ul>
 * <li>適切なリクエストデータの送信</li>
 * <li>レスポンスデータに基づいた適切な処理</li>
 * </ul>
 * </p>
 */

@RestController
@RequestMapping("homedetail")
public class DashBoardDetailController {
	/**
	 * ロガー。
	 */
	@Autowired
	private Logger logger;

	/**
	 * ダッシュボード詳細サービス。
	 */
	@Autowired
	private DashBoardDetailService dashBoardDetailService;

	/**
	 * 就職活動詳細情報を返却します。
	 *
	 * @param jobHantId 就職活動ID（必須）
	 * @return 指定された就職活動の詳細データ
	 */
	@GetMapping("/{jobHantId}")
	public BaseResponse getUserDetail(@PathVariable(value = "jobHantId") String jobHantId) {
		DetailDisplayData data = dashBoardDetailService.getDashBoardDetail(jobHantId);
		return BaseResponse.success(data, ResponseMessage.SUCCESS);
	}

	/**
	 * 就職活動受験報告を更新します。
	 *
	 * @param detailFormForUpdate 更新対象の就職活動詳細データ（必須）
	 * @return 更新結果
	 */
	@PostMapping
	public BaseResponse getUserDetailAdd(@RequestBody @Validated DetailFormForUpdate detailFormForUpdate,
			BindingResult bindingResult) {

		// 入力チェックエラーの処理
		if (bindingResult.hasErrors()) {
			logger.warn("入力エラー: {}", bindingResult.toString());
			return BaseResponse.badRequest();
		}

		// 詳細データの更新
		boolean isSuccess = dashBoardDetailService.updateDashBoardDetail(detailFormForUpdate);
		if (!isSuccess) {
			return BaseResponse.internalServerError();
		}

		// 就職活動状態の進行
		isSuccess = dashBoardDetailService.advancedJobStateId(detailFormForUpdate.getJobHuntId());
		if (isSuccess) {
			return BaseResponse.success(null, ResponseMessage.SUCCESS);
		} else {
			return BaseResponse.internalServerError();
		}
	}

	/**
	 * 就職活動報告を追加します。
	 *
	 * @param detailFormForReport 更新対象の就職活動報告データ（必須）
	 * @return 更新結果
	 */
	@PostMapping("/report")
	public BaseResponse getReportAdd(@RequestBody @Validated DetailFormForReport detailFormForReport,
			BindingResult bindingResult) {

		// 入力チェックエラーの処理
		if (bindingResult.hasErrors()) {
			logger.warn("入力エラー: {}", bindingResult.toString());
			return BaseResponse.badRequest();
		}

		// 報告データの更新
		boolean isSuccess = dashBoardDetailService.reportDashBoardDetail(detailFormForReport);
		if (!isSuccess) {
			return BaseResponse.success(null, ResponseMessage.SUCCESS);
		}

		// 就職活動状態の進行
		isSuccess = dashBoardDetailService.advancedJobStateId(detailFormForReport.getJobHuntId());
		if (isSuccess) {
			return BaseResponse.success(null, ResponseMessage.SUCCESS);
		} else {
			return BaseResponse.internalServerError();
		}
	}

	/**
	 * 就職活動状態を更新します。
	 *
	 * @param jobStateUpdateForm 就職活動状態更新フォーム（必須）
	 * @param bindingResult      入力バリデーション結果
	 * @return 更新結果
	 */
	@PutMapping
	public BaseResponse getJobStateUpdate(@RequestBody @Validated JobStateUpdateForm jobStateUpdateForm,
			BindingResult bindingResult) {

		// 入力チェックエラーの処理
		if (bindingResult.hasErrors()) {
			logger.warn("入力エラー: {}", bindingResult.toString());
			return BaseResponse.badRequest();
		}

		// 状態の更新処理
		boolean isSuccess = dashBoardDetailService.jobStateUpdate(
				jobStateUpdateForm.getJobHuntId(),
				jobStateUpdateForm.getJobStateId(),
				jobStateUpdateForm.getButtonId(),
				jobStateUpdateForm.isSchoolCheck());

		if (isSuccess) {
			String message = switch (jobStateUpdateForm.getButtonId()) {
			case 0 -> ResponseMessage.SUCCESS_GRANT;
			case 1 -> ResponseMessage.SUCCESS_CANCEL;
			case 2 -> ResponseMessage.SUCCESS_REJECT;
			default -> ResponseMessage.SUCCESS_GRANT;
			};
			return BaseResponse.success(null, message);
		} else {
			return BaseResponse.internalServerError();
		}
	}

	/**
	 * 就職活動申請書を削除します。
	 *
	 * @param jobHuntId 就職活動ID（必須）
	 * @return 削除結果
	 */
	@DeleteMapping
	public BaseResponse deleteJobExam(@RequestParam(name = "jobHuntId") String jobHuntId) {
		boolean isSuccess = dashBoardDetailService.deleteOne(jobHuntId);
		if (isSuccess) {
			return BaseResponse.success(null, ResponseMessage.SUCCESS);
		} else {
			return BaseResponse.internalServerError();
		}
	}

}
