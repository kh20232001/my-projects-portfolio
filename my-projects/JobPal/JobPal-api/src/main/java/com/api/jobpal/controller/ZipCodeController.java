package com.api.jobpal.controller;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.domain.models.entities.ZipCodeEntity;
import com.api.domain.services.ZipCodeService;
import com.api.jobpal.common.base.BaseResponse;
import com.api.jobpal.common.base.ResponseMessage;

/**
 * 郵便番号検索コントローラークラス。
 *
 * <p>
 * このクラスは、ユーザが入力した郵便番号から対応する住所情報を取得する機能を提供します。
 * </p>
 *
 * <p>
 * <strong>主な機能:</strong>
 * <ul>
 * <li>郵便番号検索APIの利用</li>
 * <li>住所データと住所カナの返却</li>
 * <li>入力された郵便番号の形式チェック</li>
 * </ul>
 * </p>
 *
 * <p>
 * <strong>仕様:</strong>
 * <ul>
 * <li>入力された郵便番号は7桁の数値文字列である必要があります。</li>
 * <li>不正な形式の郵便番号に対しては400 Bad Requestを返します。</li>
 * <li>郵便番号検索APIのレスポンスを基に、住所データを整形して返却します。</li>
 * <li>エラー発生時には500 Internal Server Errorを返却します。</li>
 * </ul>
 * </p>
 *
 * <p>
 * <ul>
 * <li>7桁の正しい形式で郵便番号をリクエストすること。</li>
 * <li>レスポンスデータを基に、住所入力フィールドを更新すること。</li>
 * </ul>
 * </p>
 */
@RestController
@RequestMapping("zip")
public class ZipCodeController {

	/**
	 * ロガー。
	 */
	@Autowired
	private Logger logger;

	/**
	 * 郵便番号サービス。
	 */
	@Autowired
	private ZipCodeService zipCodeService;

	/**
	 * 郵便番号から住所を返却します。
	 *
	 * <p>
	 * 本機能は、郵便番号検索APIを内部で呼び出して結果を返却します。<br>
	 * 仕様については、下記のドキュメントを参照してください。<br>
	 * <a href="https://zipcloud.ibsnet.co.jp/doc/api">zip cloud</a>
	 * </p>
	 *
	 * @param zipCode 郵便番号の文字列(7桁)を格納(null不可)
	 * @return 処理結果(null不可)
	 */
	@GetMapping
	public BaseResponse getAddress(@RequestParam("zipCode") String zipCode) {
		// 入力値のバリデーション（郵便番号は7桁である必要がある）
		if (zipCode == null || !zipCode.matches("\\d{7}")) {
			logger.warn("不正な郵便番号形式: {}", zipCode);
			return BaseResponse.badRequest();
		}

		try {
			// 郵便番号検索サービスを呼び出して結果を取得
			ZipCodeEntity entity = zipCodeService.execute(zipCode);

			// 正常に取得できた場合、成功レスポンスを返却
			return BaseResponse.success(entity, ResponseMessage.SUCCESS);
		} catch (Exception e) {
			// 例外が発生した場合、エラーログを記録し、内部サーバーエラーを返却
			logger.error("郵便番号検索エラー: {}", e.getMessage(), e);
			return BaseResponse.internalServerError(ResponseMessage.INTERNAL_SERVER_ERROR_MESSAGE);
		}
	}

}
