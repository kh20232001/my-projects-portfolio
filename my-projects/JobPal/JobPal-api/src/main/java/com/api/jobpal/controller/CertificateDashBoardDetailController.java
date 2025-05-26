package com.api.jobpal.controller;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.domain.models.displaydata.CertificateDisplayDetail;
import com.api.domain.models.forms.CertificateStateUpdateForm;
import com.api.domain.services.CertificateDashBoardDetailService;
import com.api.jobpal.common.base.BaseResponse;
import com.api.jobpal.common.base.ResponseMessage;

/**
 * 証明書発行ダッシュボードの詳細表示に関するコントローラークラスです。
 *
 * <p>
 * 提供される主な機能:
 * <ul>
 * <li>証明書詳細データの取得</li>
 * <li>証明書状態の更新</li>
 * <li>証明書データの削除</li>
 * </ul>
 * </p>
 *
 * <p>
 * <strong>注意点:</strong>
 * 各メソッドでは入力データのバリデーション結果を確認し、不正な場合は 400 Bad Request を返却します。<br>
 * 処理中にエラーが発生した場合は 500 Internal Server Error を返却します。
 * </p>
 *
 * <ul>
 * <li>クライアント側での入力チェックを推奨</li>
 * <li>エラーレスポンスの適切な処理</li>
 * </ul>
 * </p>
 */
@RestController
@RequestMapping("certificatedetail")
public class CertificateDashBoardDetailController {

	/**
	 * ロガー。
	 */
	@Autowired
	private Logger logger;

	/**
	 * 証明書発行ダッシュボード詳細サービス。
	 */
	@Autowired
	private CertificateDashBoardDetailService dashBoardDetailService;

	/**
	 * 指定された証明書発行IDに対応する詳細情報を取得します。
	 *
	 * <p>
	 * 証明書発行IDに基づき、詳細情報を取得して返却します。<br>
	 * 対象の詳細情報が存在しない場合、エラーを返却します。
	 * </p>
	 *
	 * @param certificateIssuanceId 証明書発行ID（必須）
	 * @return 証明書の詳細情報
	 */
	@GetMapping("/{id}")
	public BaseResponse getUserDetail(@PathVariable(value = "id") String certificateIssuanceId) {
		// サービス層から詳細情報を取得
		CertificateDisplayDetail displayDetailEntity = dashBoardDetailService.getDashBoardDetail(certificateIssuanceId);

		// 取得した詳細情報を成功レスポンスとして返却
		return BaseResponse.success(displayDetailEntity, ResponseMessage.SUCCESS);
	}

	/**
	 * 証明書の状態を更新します。
	 *
	 * <p>
	 * フロントエンドから送信された証明書状態更新フォームに基づき、証明書の状態を変更します。<br>
	 * 入力データが不正な場合は 400 Bad Request を返却します。<br>
	 * 処理成功時には操作に応じた成功メッセージを返却します。
	 * </p>
	 *
	 * @param certificateStateUpdateForm 証明書状態更新フォーム
	 * @param bindingResult               入力データのバリデーション結果
	 * @return 処理結果メッセージ
	 */
	@PutMapping
	public BaseResponse getJobStateUpdate(@RequestBody @Validated CertificateStateUpdateForm certificateStateUpdateForm,
			BindingResult bindingResult) {

		// 入力データのバリデーションチェック
		if (bindingResult.hasErrors()) {
			logger.warn("バリデーションエラー: {}", bindingResult.toString());
			return BaseResponse.badRequest();
		}

		// サービス層で証明書の状態を更新
		boolean isSuccess = dashBoardDetailService.updateDashBoardDetail(certificateStateUpdateForm);

		if (isSuccess) {
			// ボタンIDに応じた成功メッセージを設定
			String message;
			message = switch (certificateStateUpdateForm.getButtonId()) {
			case 0 -> ResponseMessage.SUCCESS_GRANT;
			case 1 -> ResponseMessage.SUCCESS_CANCEL;
			case 2 -> ResponseMessage.SUCCESS_REJECT;
			case 3 -> ResponseMessage.SUCCESS_RECEIPT;
			case 4 -> ResponseMessage.SUCCESS_ISSUE;
			case 5 -> ResponseMessage.SUCCESS_SEND;
			case 6 -> ResponseMessage.SUCCESS_POSTAL;
			case 7 -> ResponseMessage.SUCCESS_FINISH;
			default -> throw new IllegalArgumentException("無効なボタンID: " + certificateStateUpdateForm.getButtonId());
			};

			// 処理成功レスポンスを返却
			return BaseResponse.success(null, message);
		} else {
			// 更新失敗時のエラーログを記録
			logger.error("証明書状態更新に失敗しました: ボタンID = {}", certificateStateUpdateForm.getButtonId());
			return BaseResponse.internalServerError();
		}
	}

	/**
	 * 指定された証明書発行IDを削除します。
	 *
	 * <p>
	 * フロントエンドから送信された証明書発行IDに基づき、証明書情報を削除します。<br>
	 * 処理成功時には成功メッセージを返却します。
	 * </p>
	 *
	 * @param certificateIssueId 証明書発行ID（必須）
	 * @return 処理結果メッセージ
	 */
	@DeleteMapping
	public BaseResponse deleteJobExam(@RequestParam(name = "certificateIssueId") String certificateIssueId) {
		// サービス層で証明書情報を削除
		boolean isSuccess = dashBoardDetailService.deleteOne(certificateIssueId);

		if (isSuccess) {
			// 成功時のログを記録
			logger.info("証明書発行ID [{}] の削除に成功しました。", certificateIssueId);
			return BaseResponse.success(null, ResponseMessage.SUCCESS);
		} else {
			// 失敗時のエラーログを記録
			logger.error("証明書発行ID [{}] の削除に失敗しました。", certificateIssueId);
			return BaseResponse.internalServerError();
		}
	}

}
