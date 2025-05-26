package jp.ac.hcs.j2a129.zipcode;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 郵便番号APIの業務ロジックを実現するクラスです。
 * 
 * <p>処理が継続できなくなった場合は、呼び出しもとにスローせずに
 * めっそど内で例外処理を行い、エラーメッセージを設定します。
 * <strong>呼び出しもとにnullが返却されることはありません。</strong>
 * @see <a href="https://zipcloud.ibsnet.co.jp/doc/api">郵便番号検索API</a>
 * @author 春田和也
 */
@Service
public class ZipCodeService {
	@Autowired
	private RestTemplate restTemplate;

	/** エンドポイント */
	private static final String URL = "https://zipcloud.ibsnet.co.jp/api/search?zipcode={zipcode}";

	/**
	 * 郵便番号の妥当性をチェックし、結果を取得します。
	 * 
	 * @param ZipCode 郵便番号
	 * @return 郵便番号妥当性結果（trueの場合、妥当性に問題あり）
	 */
	public boolean validate(String zipCode) {
		try {
			// 入力された値を正規表現でチェックする
			if (!(zipCode.matches("^[0-9]{7}$"))) {
				throw new Exception();
			}
		} catch (Exception c) {
			// エラーがあった場合は、trueを返す。
			return true;
		}

		return false;
	}

	/**
	 * 郵便番号検索を実行し、結果を取得します。
	 * 
	 * @param ZipCode 郵便番号
	 * @return 郵便番号検索結果を格納したZipCodeEntityオブジェクト
	 */
	public ZipCodeEntity execute(String ZipCode) {
		// レスポンス（json形式）を取得
		String json = request(ZipCode);
		// 結果（箱）を生成
		ZipCodeEntity zipCodeEntity = new ZipCodeEntity();
		// レスポンスを結果（箱）に変換
		convert(json, zipCodeEntity);

		return zipCodeEntity;

	}

	private void convert(String json, ZipCodeEntity zipCodeEntity) {
		// 変換ライブラリの生成
		ObjectMapper mapper = new ObjectMapper();

		try {
			// レスポンス（json）を構造体へ変換
			JsonNode node = mapper.readTree(json);

			// 「status」 ノードを取得し文字列に変換
			String status = node.get("status").asText();
			zipCodeEntity.setStatus(status);

			// 「message」 ノードを取得し文字列に変換
			String message = node.get("message").asText();
			zipCodeEntity.setStatus(message);

			// 「results」 ノード(配列)を取得し繰り返す
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

	private String request(String zipCode) {
		String json = restTemplate.getForObject(URL, String.class, zipCode);
		return json;
	}

}
