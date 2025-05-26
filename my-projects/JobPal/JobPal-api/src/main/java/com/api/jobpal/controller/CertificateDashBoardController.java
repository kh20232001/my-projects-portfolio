package com.api.jobpal.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.domain.models.entities.CertificateDashBoardEntity;
import com.api.domain.models.entities.CertificateFeeAndWeightEntity;
import com.api.domain.models.forms.CertificateDashBoardForm;
import com.api.domain.models.forms.CertificateInsertForm;
import com.api.domain.repositories.UserRepository;
import com.api.domain.services.CertificateDashBoardService;
import com.api.jobpal.common.base.BaseResponse;
import com.api.jobpal.common.base.ResponseMessage;
import com.api.jobpal.common.base.Util;

/**
 * 証明書ダッシュボードに関連するコントローラークラスです。
 *
 * <p>
 * 以下の操作を提供します:
 * <ul>
 * <li>証明書ダッシュボードデータの取得（全体・ユーザー別）</li>
 * <li>証明書料金・重量データの取得</li>
 * <li>新規証明書発行の作成</li>
 * <li>既存証明書発行データの更新</li>
 * </ul>
 * </p>
 *
 * <p>
 * <strong>注意点:</strong>
 * 入力データのバリデーション結果は必ずチェックする必要があります。<br>
 * バリデーションエラー時には 400 Bad Request を返却し、サーバーエラー時には 500 Internal Server Error を返却します。
 * </p>
 *
 * <p>
 * エラーレスポンスを適切に処理してください。<br>
 * バリデーションエラー時にはクライアント側で修正対応を行う必要があります。
 * </p>
 */
@RestController
@RequestMapping("certificate")
public class CertificateDashBoardController {

	/**
	 * ロガーオブジェクト。
	 */
	private static final Logger logger = LoggerFactory.getLogger(CertificateDashBoardController.class);

	/**
	 * 証明書ダッシュボードのサービス層。
	 */
	@Autowired
	private CertificateDashBoardService dashBoardService;

	/**
	 * ユーザリポジトリ。
	 */
	@Autowired
	private UserRepository userRepository;

	/**
	 * ダッシュボード全体のデータを取得します。
	 *
	 * <p>
	 * リクエストに基づいてダッシュボードのデータを取得し、レスポンスとして返却します。<br>
	 * 入力チェックエラーが発生した場合は、400 Bad Requestを返します。<br>
	 * ユーザの権限によって取得するデータを切り替えます。
	 * </p>
	 *
	 * @param dashBoardForm 取得対象のユーザ情報が含まれたリクエストデータ
	 * @param bindingResult 入力データのバリデーション結果
	 * @return ダッシュボードのデータまたはエラー情報
	 */
	/**
	 * ユーザの証明書ダッシュボードデータを取得します。
	 *
	 * <p>
	 * 指定されたフォームデータを基に、管理者または一般ユーザのダッシュボード情報を取得します。<br>
	 * 入力チェックに失敗した場合は400 Bad Requestを返却し、エラー発生時はデフォルトのエンティティを返却します。
	 * </p>
	 *
	 * @param dashBoardForm ダッシュボード取得のためのリクエストデータ
	 * @param bindingResult バリデーション結果
	 * @return ダッシュボードデータまたはエラーレスポンス
	 */
	@PostMapping
	public BaseResponse getDashBoardAll(@RequestBody @Validated CertificateDashBoardForm dashBoardForm,
			BindingResult bindingResult) {

		// 入力チェックエラー処理
		if (bindingResult.hasErrors()) {
			logger.warn("入力チェックエラー: {}", bindingResult);
			return BaseResponse.badRequest();
		}

		CertificateDashBoardEntity dashBoardEntity;
		String Grant = dashBoardForm.getGrant(); // ユーザ権限
		String userName = "名無しさん"; // デフォルトユーザ名

		try {
			// ユーザ名を取得
			userName = userRepository.selectUserName(dashBoardForm.getUserId());

			// ユーザ権限に応じてダッシュボードデータを取得
			if ("2".equals(Grant)) {
				// 管理者権限の場合
				dashBoardEntity = dashBoardService.getDashBoardAll(dashBoardForm.getUserId());
			} else {
				// 一般ユーザ権限の場合
				dashBoardEntity = dashBoardService.getDashBoardUser(dashBoardForm.getUserId(),
						Util.getUserTypeNameById(Grant));
			}

			// ユーザ名をエンティティにセット
			dashBoardEntity.setUserName(userName);

			// 成功レスポンスを返却
			return BaseResponse.success(dashBoardEntity, ResponseMessage.SUCCESS);

		} catch (Exception ex) {
			// エラー処理: デフォルトエンティティを作成して返却
			dashBoardEntity = new CertificateDashBoardEntity(userName);
			logger.error("ダッシュボードデータ取得エラー: ", ex);
			return BaseResponse.success(dashBoardEntity, ResponseMessage.SUCCESS);
		}
	}

	/**
	 * 証明書に関連する重量、発行料金、郵送の許容量、郵送料を取得します。
	 *
	 * <p>
	 * サービス層から証明書の詳細情報を取得し、成功時にはそのデータをレスポンスとして返却します。<br>
	 * エラー発生時には500 Internal Server Errorを返却します。
	 * </p>
	 *
	 * @return 成功時：証明書の詳細情報、失敗時：エラーレスポンス
	 */
	@GetMapping("/new")
	public BaseResponse getCertificateDetails() {
		try {
			// サービス層から証明書の詳細情報を取得
			CertificateFeeAndWeightEntity certificateFeeAndWeightEntity = dashBoardService.getCertificateFeeAndWeight();

			// 成功レスポンスを返却
			return BaseResponse.success(certificateFeeAndWeightEntity, ResponseMessage.SUCCESS);
		} catch (Exception ex) {
			// エラーログの記録
			logger.error("証明書詳細情報取得エラー: ", ex);

			// 失敗レスポンスを返却
			return BaseResponse.internalServerError();
		}
	}

	/**
	 * 新規証明書発行の作成を行います。
	 *
	 * <p>
	 * フロントエンドから送信された証明書情報を元に、新しい証明書発行データを作成します。<br>
	 * 入力データが不正な場合は 400 Bad Request を返却します。<br>
	 * サーバー側でエラーが発生した場合は 500 Internal Server Error を返却します。
	 * </p>
	 *
	 * @param certificateInsertForm 新規証明書情報
	 * @param bindingResult         入力データのバリデーション結果
	 * @return 成功時：証明書の作成結果、失敗時：エラーレスポンス
	 */
	@PostMapping("/new")
	public BaseResponse createNewCertificate(@RequestBody @Validated CertificateInsertForm certificateInsertForm,
			BindingResult bindingResult) {

		// 入力データのバリデーションチェック
		if (bindingResult.hasErrors()) {
			logger.warn("バリデーションエラー: {}", bindingResult.toString());
			return BaseResponse.badRequest();
		}

		try {
			// 証明書作成処理
			boolean result = dashBoardService.createNewCertificate(certificateInsertForm);
			if (result) {
				// 成功レスポンスを返却
				return BaseResponse.success(null, ResponseMessage.SUCCESS);
			} else {
				// 作成失敗時のレスポンス
				return BaseResponse.internalServerError();
			}
		} catch (Exception ex) {
			// エラー発生時のログ記録
			logger.error("証明書作成エラー: ", ex);
			return BaseResponse.internalServerError();
		}
	}

	/**
	 * 既存の証明書発行データを更新します。
	 *
	 * <p>
	 * フロントエンドから送信された証明書情報を元に、証明書発行データを更新します。<br>
	 * 入力データが不正な場合は 400 Bad Request を返却します。<br>
	 * サーバー側でエラーが発生した場合は 500 Internal Server Error を返却します。
	 * </p>
	 *
	 * @param certificateInsertForm 証明書更新情報
	 * @param bindingResult         入力データのバリデーション結果
	 * @return 成功時：証明書の更新結果、失敗時：エラーレスポンス
	 */
	@PutMapping("/new")
	public BaseResponse updateCertificate(@RequestBody @Validated CertificateInsertForm certificateInsertForm,
			BindingResult bindingResult) {

		// 入力データのバリデーションチェック
		if (bindingResult.hasErrors()) {
			logger.warn("バリデーションエラー: {}", bindingResult.toString());
			return BaseResponse.badRequest();
		}

		try {
			// 証明書更新処理
			boolean isSuccess = dashBoardService.updateCertificate(certificateInsertForm);
			if (isSuccess) {
				// 成功レスポンスを返却
				return BaseResponse.success(null, ResponseMessage.SUCCESS);
			} else {
				// 更新失敗時のレスポンス
				return BaseResponse.internalServerError();
			}
		} catch (Exception ex) {
			// エラー発生時のログ記録
			logger.error("証明書更新エラー: ", ex);
			return BaseResponse.internalServerError();
		}
	}

}
