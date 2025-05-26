package jp.ac.hcs.j2a129.weather;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * 天気予報検索のレスポンスです。
 * 
 * <p>各項目のデータ仕様については、APIの仕様を参照して下さい。
 * 予報日は今日/明日/明後日の3つが配列で取得できるため、リスト構造となっている。
 * 
 * @author 春田和也
 */
@Data
public class WeatherEntity {
	/** タイトル */
	private String title;

	/** 説明文 */
	private String description;

	/** 天気情報のリスト */
	private List<WeatherData> forecasts = new ArrayList<WeatherData>();

	/** エラーメッセージ（表示用） */
	private String errorMessage;

}
