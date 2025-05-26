package com.api.jobpal.controller;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.domain.models.displaydata.NewJobSerachDisplay;
import com.api.domain.models.forms.NewJobApplicationForm;
import com.api.domain.services.NewJobApplicationService;
import com.api.jobpal.common.base.BaseResponse;
import com.api.jobpal.common.base.ResponseMessage;

/**
 * 新規申請書作成機能を提供するコントローラークラスです。
 *
 * <p>
 * <strong>主な機能:</strong>
 * <ul>
 * <li>新規申請書作成画面用ユーザデータの取得</li>
 * <li>新規申請書の登録</li>
 * <li>既存申請書の更新</li>
 * </ul>
 * </p>
 *
 * <p>
 * <strong>仕様:</strong>
 * <ul>
 * <li>学生ユーザが対象</li>
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
 * <li>データ整合性を確保するため、バリデーションエラー時の処理を徹底</li>
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
@RequestMapping("new")
public class NewJobApplicationController {
	/**
	 * ロガー。
	 */
	@Autowired
	private Logger logger;

	/**
	 * 新規就職活動申請サービス。
	 */
	@Autowired
	private NewJobApplicationService newJobApplicationService;

	/**
	 * 就職活動申請作成画面で表示するユーザデータを返却します。
	 *
	 * <p>
	 * 指定されたユーザIDに関連する就職活動申請画面用のユーザデータを取得して返却します。
	 * </p>
	 *
	 * @param userId ユーザID（必須）
	 * @return ユーザデータを含むレスポンス
	 */
	@GetMapping("/{id}")
	public BaseResponse getUserDetail(@PathVariable(value = "id") String userId) {
		// ユーザデータを取得
		NewJobSerachDisplay newJobSerachDisplay = newJobApplicationService.getUserData(userId);

		// 成功レスポンスを返却
		return BaseResponse.success(newJobSerachDisplay, ResponseMessage.SUCCESS);
	}

	/**
	 * 新規申請書を作成する
	 *
	 * <p>
	 * 学生ユーザのみ利用可能。フロントエンドから送信されたデータを基に、データベースへ新規申請を登録します。
	 * </p>
	 *
	 * @param newJobApplicationForm 新規申請データ（必須）
	 * @param bindingResult         入力チェック結果
	 * @return 作成成功/失敗のレスポンス
	 */
	@PostMapping
	public BaseResponse addJobSearchApplication(@RequestBody @Validated NewJobApplicationForm newJobApplicationForm,
			BindingResult bindingResult) {

		// 入力チェックエラーの処理
		if (bindingResult.hasErrors()) {
			logger.warn("入力エラー: {}", bindingResult.toString());
			return BaseResponse.badRequest();
		}

		// 新規申請処理の実行
		boolean isSuccess = newJobApplicationService.addNewJobApplication(newJobApplicationForm);

		// 処理結果に応じたレスポンスを返却
		if (isSuccess) {
			return BaseResponse.success(null, ResponseMessage.SUCCESS);
		} else {
			return BaseResponse.internalServerError();
		}
	}

	/**
	 * 申請書の内容を変更する
	 *
	 * <p>
	 * 学生ユーザのみ利用可能。フロントエンドから送信されたデータを基に、既存の申請データを更新します。
	 * </p>
	 *
	 * @param newJobApplicationForm 更新する申請データ（必須）
	 * @param bindingResult         入力チェック結果
	 * @return 更新成功/失敗のレスポンス
	 */
	@PutMapping
	public BaseResponse changeJobSearchApplication(@RequestBody @Validated NewJobApplicationForm newJobApplicationForm,
			BindingResult bindingResult) {

		// 入力チェックエラーの処理
		if (bindingResult.hasErrors()) {
			logger.warn("入力エラー: {}", bindingResult.toString());
			return BaseResponse.badRequest();
		}

		// 更新処理の実行
		boolean isSuccess = newJobApplicationService.changeNewJobApplication(newJobApplicationForm);

		// 処理結果に応じたレスポンスを返却
		if (isSuccess) {
			return BaseResponse.success(null, ResponseMessage.SUCCESS);
		} else {
			return BaseResponse.internalServerError();
		}
	}

}
