package jp.ac.hcs.j2a129.dog;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 犬画像取得の業務ロジッククラスです。
 * 
 * <p>
 * このクラスはDog APIを利用して犬の画像情報を取得します。
 * </p>
 * @author 春田和也
 */
@Service
public class DogService {
	/* Dog APIのエンドポイント */
	//	private static final String URL = "https://dog.ceo/api/breeds/image/random";

	@Autowired
	private RestTemplate restTemplate;

	/**
	 * 犬の画像を取得します。
	 * @param dogPath 犬の画像のPath
	 * @return 犬API情報を格納したDogEntityオブジェクト
	 */
	public DogData exec(String dogPath) {
		String URL = "https://dog.ceo/api/breed/" + dogPath + "/images/random";
		// レスポンスを文字列として格納
		String json = restTemplate.getForObject(URL, String.class);

		// 変換ライブラリの生成
		ObjectMapper mapper = new ObjectMapper();

		//犬API情報を格納するクラスを生成
		DogData dogData = new DogData();
		try {
			//レスポンス（json）を構造体へ変換
			JsonNode node = mapper.readTree(json);

			//「status」ノードを取得し文字列に変換
			String status = node.get("status").asText();
			dogData.setStatus(status);

			// 「mwssage」ノードを取得し文字列に変換
			String message = node.get("message").asText();
			dogData.setMessage(message);
		} catch (IOException e) {
			// エラーメッセージを設定
			dogData.setErrorMessage("通信に失敗しました");
		}
		return dogData;
	}
}
