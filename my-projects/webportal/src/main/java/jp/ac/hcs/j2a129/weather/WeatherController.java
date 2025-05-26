package jp.ac.hcs.j2a129.weather;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 天気予報検索を行うためのコントローラクラスです。
 * 
 * <p>
 * このクラスはSpring MVCフレームワークでリクエストを処理し、天気予報検索機能の画面表示を担当します。
 * </p>
 * 
 * <p>入力チェックを実施し、正しい市区町村コードのみ処理します。<br>
 * クライアント側でも入力チェックを実施することを推奨します。
 * 
 * @author 春田和也
 * 
 */
@Controller
public class WeatherController {

	@Autowired
	private WeatherService weatherService;

	/**
	 * 郵便番号入力画面を表示します。
	 * 
	 * @return 天気予報検索結果画面へのパス
	 */
	@GetMapping("/weather")
	public String getWeather() {
		return "weather/input";
	}

	/**
	 * 都市コードをもとに該当地域の天気予報を検索し、結果画面を表示します。
	 * 
	 * <p>本機能は、天気予報API（livedoor 天気互換）を内部で呼び出して結果を表示します。
	 * 仕様については、下記のドキュメントを参照してください。
	 * <p>https://weather.tsukumijima.net/
	 * 
	 * @param citycode 都市コードの文字列(6桁)を格納
	 * @param model Viewに値を渡すオブジェクト
	 * @return 天気予報検索結果画面へのパス
	 */
	@PostMapping("/weather")
	public String postWeather(@RequestParam("citycode") String citycode, Model model) {
		if (weatherService.validate(citycode)) {
			return "weather/input";
		}
		// 検索結果を取得
		WeatherEntity weatherEntity = weatherService.execute(citycode);
		// 結果を画面に設定	
		if (weatherEntity.getForecasts().size() == 0) {
			return "zipcode/input";
		}
		model.addAttribute("results", weatherEntity);
		return "weather/result";
	}
}
