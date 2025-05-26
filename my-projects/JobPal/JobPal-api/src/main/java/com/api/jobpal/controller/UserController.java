package com.api.jobpal.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
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

import com.api.domain.models.displaydata.UserDataDisplay;
import com.api.domain.models.displaydata.UserDetailDisplay;
import com.api.domain.models.entities.UserDeleteEntity;
import com.api.domain.models.forms.UserForm;
import com.api.domain.models.forms.UserUpdateForm;
import com.api.domain.services.UserService;
import com.api.jobpal.common.base.BaseResponse;
import com.api.jobpal.common.base.ResponseMessage;

/**
 * ユーザ管理機能を提供するコントローラークラスです。
 *
 * <p>
 * <strong>主な機能:</strong>
 * <ul>
 * <li>ユーザ一覧の取得</li>
 * <li>ユーザ詳細情報の取得</li>
 * <li>新規ユーザの追加</li>
 * <li>既存ユーザの更新</li>
 * <li>ユーザの削除</li>
 * </ul>
 * </p>
 *
 * <p>
 * <strong>仕様:</strong>
 * <ul>
 * <li>入力データのバリデーションを実施し、不正なデータは処理対象外</li>
 * <li>操作権限に応じたロールベースの制御を実装</li>
 * <li>成功時には適切なレスポンスを返却</li>
 * <li>エラー発生時には詳細なエラーログを記録</li>
 * </ul>
 * </p>
 *
 * <p>
 * <strong>注意点:</strong>
 * <ul>
 * <li>管理者および上位権限のみ特定の機能にアクセス可能</li>
 * <li>クライアント側でも入力チェックを実施することを推奨</li>
 * </ul>
 * </p>
 *
 * <p>
 * <ul>
 * <li>適切なリクエストデータを提供</li>
 * <li>レスポンスデータを基にUIを更新</li>
 * </ul>
 * </p>
 */
@RestController
@RequestMapping("user")
public class UserController {

	/**
	 * ロガー。
	 */
	@Autowired
	private Logger logger;

	/**
	 * ユーザサービス。
	 */
	@Autowired
	private UserService userService;

	/**
	 * 【管理者・上位のみ】ユーザ一覧を返却します。
	 *
	 * <p>
	 * 本機能は、ユーザ管理機能の一覧機能を提供します。<br>
	 * <strong>この機能は管理者もしくは上位ロールのユーザのみが利用できます</strong>
	 * </p>
	 *
	 * @return ユーザ一覧(null不可)
	 */
	@GetMapping
	public BaseResponse getUserList() {
		// ユーザ一覧の取得
		List<UserDataDisplay> list = userService.getUserList();
		return BaseResponse.success(list, ResponseMessage.SUCCESS);
	}

	/**
	 * 【管理者】ユーザ詳細を返却します。
	 *
	 * @param userId ユーザID(null不可)
	 * @return ユーザ詳細(null不可)
	 */
	@GetMapping("/{userId}")
	public BaseResponse getUserDetail(@PathVariable(value = "userId") String userId) {
		// 指定されたユーザIDの詳細情報を取得
		UserDetailDisplay data = userService.getUser(userId);
		return BaseResponse.success(data, ResponseMessage.SUCCESS);
	}

	/**
	 * 【管理者】ユーザを追加します。
	 *
	 * <p>
	 * 本機能は、ユーザ管理機能の追加機能を提供します。<br>
	 * <strong>この機能は管理者ロールのユーザのみが利用できます</strong>
	 * </p>
	 *
	 * @param form          入力値(null不可)
	 * @param bindingResult バリデーション結果を保持(null不可)
	 * @return 処理結果(null不可)
	 */
	@PostMapping
	public BaseResponse createUser(@RequestBody @Validated UserForm form, BindingResult bindingResult) {

		// 入力チェックエラー
		if (bindingResult.hasErrors()) {
			logger.warn("入力エラー: {}", bindingResult.toString());
			return BaseResponse.badRequest();
		}

		// ユーザ追加処理
		boolean isSuccess = userService.insertOne(form);
		if (isSuccess) {
			// パスワードをレスポンスに含めないためnullに設定
			form.setPassword(null);
			return BaseResponse.success(null, ResponseMessage.SUCCESS);
		} else {
			return BaseResponse.internalServerError(ResponseMessage.ADD_USER_ERROR_MESSAGE);
		}
	}

	/**
	 * 【管理者】ユーザを更新します。
	 *
	 * <p>
	 * 本機能は、ユーザ管理機能の更新機能を提供します。<br>
	 * <strong>この機能は管理者ロールのユーザのみが利用できます</strong>
	 * </p>
	 *
	 * @param userFormForUpdate ユーザ更新の入力値(null不可)
	 * @param bindingResult     バリデーションの結果(null不可)
	 * @return 処理結果(null不可)
	 */
	@PutMapping
	public BaseResponse updateUser(@RequestBody @Validated UserUpdateForm userFormForUpdate,
			BindingResult bindingResult) {

		// 入力チェックエラー
		if (bindingResult.hasErrors()) {
			logger.warn("入力エラー: {}", bindingResult.toString());
			return BaseResponse.badRequest();
		}

		// ユーザ更新処理
		boolean isSuccess = userService.updateOne(userFormForUpdate);
		if (isSuccess) {
			// パスワードをレスポンスに含めないためnullに設定
			userFormForUpdate.setPassword(null);
			return BaseResponse.success(null, ResponseMessage.SUCCESS);
		} else {
			return BaseResponse.internalServerError();
		}
	}

	/**
	 * 【管理者】ユーザを削除します。
	 *
	 * <p>
	 * 本機能は、ユーザ管理機能の削除機能を提供します。<br>
	 * <strong>この機能は管理者ロールのユーザのみが利用できます</strong>
	 * </p>
	 *
	 * @param userId ユーザID(null不可)
	 * @return 処理結果(null不可)
	 */
	@DeleteMapping
	public BaseResponse deleteUser(@RequestParam(name = "id") String userId, @AuthenticationPrincipal User user) {

		if (userId == null || userId.isEmpty() || userId.equals(user.getUsername())) {
			return BaseResponse.statusNotAllowed();
		}

		// ユーザ削除に伴う関連データの取得（CSV用）
		Map<String, List<UserDeleteEntity>> csvList = userService.getDeleteUserCsv(userId);

		// ユーザ削除処理
		boolean isSuccess = userService.deleteOne(userId);
		if (isSuccess) {
			return BaseResponse.success(csvList, ResponseMessage.SUCCESS);
		} else {
			return BaseResponse.internalServerError();
		}
	}

}
