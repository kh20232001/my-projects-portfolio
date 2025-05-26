package jp.ac.hcs.j2a129.weather;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 天気予報の業務ロジックを実現するクラスです。
 * 
 * <p>処理が継続できなくなった場合は、呼び出しもとにスローせずに
 * メソッド内で例外処理を行い、エラーメッセージを設定します。
 * <strong>呼び出し元にnullが返却されることはありません。</strong>
 * 
 * @author 春田和也
 * 
 */
@Transactional
@Service
public class WeatherService {

	@Autowired
	RestTemplate restTemplate;

	/** エンドポイント */
	private static final String URL = "https://weather.tsukumijima.net/api/forecast?city={cityCode}";

	/**
	 * 郵便番号の妥当性をチェックし、結果を取得します。
	 * 
	 * @param cityCode 郵便番号
	 * @return citycode妥当性結果（trueの場合、妥当性に問題あり）
	 */
	boolean validate(String cityCode) {
		try {
			// 入力された値を正規表現でチェックする
			if (!(cityCode.matches("^[0-9]{6}$"))) {
				throw new Exception();
			}
		} catch (Exception c) {
			// エラーがあった場合は、trueを返す。
			return true;
		}

		return false;
	}

	/**
	 * 都市コードから検索結果形式に変換します。
	 * 
	 * <p>検索に失敗した場合は、エラーメッセージの実が設定されます。
	 * 
	 * @param cityCode 都市コードの文字列(6桁)を格納(null不可)
	 * @return 天気予報結果を格納したWeatherCodeEntityオブジェクト
	 */
	public WeatherEntity execute(String citycode) {
		// レスポンス（json形式）を取得
		String json = request(citycode);
		// 結果（箱）を生成
		WeatherEntity weatherEntity = new WeatherEntity();
		// レスポンスを結果（箱）に変換
		convert(json, weatherEntity);
		return weatherEntity;
	}

	private void convert(String json, WeatherEntity weatherEntity) {
		// 変換ライブラリの生成
		ObjectMapper mapper = new ObjectMapper();

		try {
			// レスポンス（json）を構造体へ変換
			JsonNode node = mapper.readTree(json);

			// 「title」 ノードを取得し文字列に変換
			String title = node.get("title").asText();
			weatherEntity.setTitle(title);

			// 「message」 ノードを取得し文字列に変換
			String description = node.get("description").get("bodyText").asText();
			//			String description = "aaaa";
			weatherEntity.setDescription(description);

			// 「results」 ノード(配列)を取得し繰り返す
			for (JsonNode forecast : node.get("forecasts")) {
				WeatherData weatherData = new WeatherData();
				weatherData.setDateLabel(forecast.get("date").asText());
				weatherData.setTelop(forecast.get("telop").asText());
				weatherData.setImage(forecast.get("image").get("url").asText());
				// 配列の末尾に追加
				weatherEntity.getForecasts().add(weatherData);
			}
		} catch (IOException e) {
			// エラーメッセージを設定
			weatherEntity.setErrorMessage("通信に失敗しました");
		}
	}

	private String request(String cityCode) {
		// 札幌の都市コード
		//		String cityCode = "016010";

		String json = restTemplate.getForObject(URL, String.class, cityCode);
		return json;
	}
}
