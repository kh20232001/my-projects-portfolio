package com.api.domain.services;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.api.domain.models.data.ZipCodeData;
import com.api.domain.models.entities.ZipCodeEntity;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 郵便番号APIの業務ロジックを実現するクラスです。
 *
 * <p>
 * 以下の処理を行います。
 * <ul>
 * <li>指定された郵便番号を基に、郵便番号APIから住所情報を取得</li>
 * <li>取得したデータをエンティティ形式に変換</li>
 * <li>通信失敗時やデータ変換失敗時にエラーメッセージを設定</li>
 * </ul>
 * </p>
 *
 * <p>
 * <strong>呼び出し元にnullが返却されることはありません。</strong>
 * 必ずエラーメッセージを設定してレスポンスを返却します。
 * </p>
 *
 * <p>
 * 例外処理を内部で行い、呼び出し元に例外をスローしません。
 * </p>
 */

@Service
@Transactional
public class ZipCodeService {

	/**
	 * RestTemplateのインスタンス。
	 */
	@Autowired
	private RestTemplate restTemplate;

	/**
	 * エンドポイント
	 */
	private static final String URL = "https://zipcloud.ibsnet.co.jp/api/search?zipcode={zipcode}";

	/**
	 * 郵便番号から検索結果形式に変換します。
	 *
	 * <p>
	 * 検索に失敗した場合は、エラーメッセージのみ設定されます。
	 *
	 * @param zipcode 郵便番号の文字列(7桁)を格納(null不可)
	 * @return 郵便番号検索のレスポンス
	 */
	public ZipCodeEntity execute(String zipcode) {
		// レスポンス(json形式)を取得
		String json = request(zipcode);
		// 結果(箱)を生成
		ZipCodeEntity zipCodeEntity = new ZipCodeEntity();
		// レスポンスを結果(箱)に変換
		convert(json, zipCodeEntity);

		return zipCodeEntity;
	}

	private void convert(String json, ZipCodeEntity zipCodeEntity) {

		// 変換ライブラリの生成
		ObjectMapper mapper = new ObjectMapper();

		try {
			// レスポンス(json)を構造体へ変換
			JsonNode node = mapper.readTree(json);

			// 「status」ノードを取得し文字列に変換
			String status = node.get("status").asText();
			zipCodeEntity.setStatus(status);

			// 「message」ノードを取得し文字列に変換
			String message = node.get("message").asText();
			zipCodeEntity.setMessage(message);

			// 「results」ノード(配列)を取得し繰り返す
			for (JsonNode result : node.get("results")) {
				ZipCodeData zipCodeData = new ZipCodeData();

				zipCodeData.setZipcode(result.get("zipcode").asText());
				zipCodeData.setPrefcode(result.get("prefcode").asText());
				zipCodeData.setAddress1(result.get("address1").asText());
				zipCodeData.setAddress2(result.get("address2").asText());
				zipCodeData.setAddress3(result.get("address3").asText());
				zipCodeData.setKana1(result.get("kana1").asText());
				zipCodeData.setKana2(result.get("kana2").asText());
				zipCodeData.setKana3(result.get("kana3").asText());

				// 配列の末尾に追加
				zipCodeEntity.getList().add(zipCodeData);
			}
		} catch (IOException e) {
			// エラーメッセージを設定
			zipCodeEntity.setErrorMessage("通信に失敗しました");
		}
	}

	/**
	 * 指定された郵便番号を基にAPIリクエストを送信し、レスポンスを取得します。
	 *
	 * <p>
	 * 指定された郵便番号を使用して、指定URLにHTTP GETリクエストを送信します。<br>
	 * レスポンスはJSON形式の文字列として返却されます。
	 * </p>
	 *
	 * @param zipcode 郵便番号（リクエストパラメータとして使用されます）
	 * @return APIレスポンスのJSON文字列
	 */
	private String request(String zipcode) {
		return restTemplate.getForObject(URL, String.class, zipcode);
	}

}
