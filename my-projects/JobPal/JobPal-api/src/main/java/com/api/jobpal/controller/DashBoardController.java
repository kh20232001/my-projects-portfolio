package com.api.jobpal.controller;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.domain.models.entities.DashBoardDisplayEntity;
import com.api.domain.models.forms.DashBoardForm;
import com.api.domain.repositories.UserRepository;
import com.api.domain.services.DashBoardService;
import com.api.jobpal.common.base.BaseResponse;
import com.api.jobpal.common.base.ResponseMessage;

/**
 * ダッシュボード表示機能を提供するコントローラークラスです。
 *
 * <p>
 * <strong>主な機能:</strong>
 * <ul>
 * <li>管理者および担任のダッシュボード情報取得</li>
 * <li>学生の個別ダッシュボード情報取得</li>
 * </ul>
 * </p>
 *
 * <p>
 * <strong>仕様:</strong>
 * <ul>
 * <li>管理者および担任の場合、すべての申請情報を取得</li>
 * <li>学生の場合、個人の申請情報のみを取得</li>
 * </ul>
 * </p>
 *
 * <p>
 * <strong>注意点:</strong>
 * <ul>
 * <li>入力データのバリデーションに失敗した場合は400 Bad Requestを返却</li>
 * <li>エラー発生時には適切なレスポンスを返却</li>
 * </ul>
 * </p>
 *
 * <p>
 * <ul>
 * <li>クライアント側での入力データの整合性チェック</li>
 * <li>レスポンスデータの適切な処理</li>
 * </ul>
 * </p>
 */

@RestController
@RequestMapping("home")
public class DashBoardController {

	/**
	 * ロガー。
	 */
	@Autowired
	private Logger logger;

	/**
	 * ダッシュボードサービス。
	 */
	@Autowired
	private DashBoardService dashBoardService;

	/**
	 * ユーザリポジトリ。
	 */
	@Autowired
	private UserRepository userRepository;

	/**
	 * ダッシュボード画面を表示します。
	 *
	 * <p>
	 * - 管理者および担任の場合: 全件の申請情報を表示します。<br>
	 * - 学生の場合: 個人の申請情報のみを表示します。
	 * </p>
	 *
	 * @param dashBoardForm 操作しているユーザの情報（必須）
	 * @param bindingResult 入力バリデーションの結果
	 * @return ダッシュボードのJSONレスポンス
	 */
	@PostMapping
	public BaseResponse getDashBoardAll(@RequestBody @Validated DashBoardForm dashBoardForm,
			BindingResult bindingResult) {

		// 入力チェックエラーの場合
		if (bindingResult.hasErrors()) {
			logger.warn("入力エラー: {}", bindingResult.toString());
			return BaseResponse.badRequest();
		}

		DashBoardDisplayEntity dashBoardDisplayEntity;
		String grant = dashBoardForm.getGrant(); // ユーザ権限を取得

		// 学生ユーザの場合
		if ("0".equals(grant)) {
			// 学生個人のダッシュボード情報を取得
			dashBoardDisplayEntity = dashBoardService.getDashBoardUser(dashBoardForm.getUserId());
		} else {
			// 学生以外（管理者または担任）の場合、全件のダッシュボード情報を取得
			dashBoardDisplayEntity = dashBoardService.getDashBoardAll(dashBoardForm.getUserId(), grant);
		}

		// ユーザ名を取得し、ダッシュボード情報に設定
		dashBoardDisplayEntity.setUserName(userRepository.selectUserName(dashBoardForm.getUserId()));

		// 成功レスポンスを返却
		return BaseResponse.success(dashBoardDisplayEntity, ResponseMessage.SUCCESS);
	}

}
