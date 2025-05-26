package com.api.jobpal.controller;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.domain.models.entities.AlertDisplayEntity;
import com.api.domain.models.forms.AlertForm;
import com.api.domain.services.NotificationService;
import com.api.jobpal.common.base.BaseResponse;
import com.api.jobpal.common.base.ResponseMessage;

/**
 * 通知機能を提供するコントローラークラスです。
 *
 * <p>
 * <strong>主な機能:</strong>
 * <ul>
 * <li>ユーザーの権限に応じた通知データの取得</li>
 * <li>管理者および担任ユーザーの場合: 全通知データを取得</li>
 * <li>学生ユーザーの場合: 個人の通知データのみ取得</li>
 * </ul>
 * </p>
 *
 * <p>
 * <strong>仕様:</strong>
 * <ul>
 * <li>リクエストデータに基づいて通知情報を動的に生成</li>
 * <li>入力データのバリデーションを行い、不正データは400 Bad Requestを返却</li>
 * <li>成功時には通知データを含むレスポンスを返却</li>
 * <li>エラー時には適切なエラーメッセージを返却</li>
 * </ul>
 * </p>
 *
 * <p>
 * <strong>注意点:</strong>
 * <ul>
 * <li>クライアント側で入力チェックを事前に実施することを推奨</li>
 * <li>バリデーションエラー時には詳細なログを記録</li>
 * </ul>
 * </p>
 *
 * <p>
 * <ul>
 * <li>適切なリクエストデータを提供</li>
 * <li>レスポンスデータに基づいた画面の更新およびエラー処理</li>
 * </ul>
 * </p>
 */

@RestController
@RequestMapping("alert")
public class NortificationController {
	/**
	 * ロガー。
	 */
	@Autowired
	private Logger logger;

	/**
	 * 通知サービス。
	 */
	@Autowired
	private NotificationService alertService;

	/**
	 * 通知画面を表示する
	 *
	 * <p>
	 * 指定されたユーザの権限に応じて、通知情報を取得し返却します。<br>
	 * - 管理者および担任の場合: 全件の通知申請を取得<br>
	 * - 学生の場合: 学生個人の通知申請のみ取得
	 * </p>
	 *
	 * @param alertForm     ユーザ情報を含むフォーム（必須）
	 * @param bindingResult 入力チェック結果
	 * @return 通知情報を含むレスポンス
	 */
	@PostMapping
	public BaseResponse getDashBoardAll(@RequestBody @Validated AlertForm alertForm, BindingResult bindingResult) {

		// 入力チェックエラーの処理
		if (bindingResult.hasErrors()) {
			logger.warn("入力エラー: {}", bindingResult.toString());
			return BaseResponse.badRequest();
		}

		AlertDisplayEntity alertDisplayEntity;
		String grant = alertForm.getGrant(); // ユーザ権限

		// 学生の場合
		if ("0".equals(grant)) {
			alertDisplayEntity = alertService.getAlertAll(alertForm.getUserId());
		} else {
			// 管理者または担任の場合
			alertDisplayEntity = alertService.getAlertAll(alertForm.getUserId());
		}

		// 成功レスポンスを返却
		return BaseResponse.success(alertDisplayEntity, ResponseMessage.SUCCESS);
	}

}
