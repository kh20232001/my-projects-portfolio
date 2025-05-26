package jp.ac.hcs.j2a129.weather;

import lombok.Data;

/**
 * 1件分の天気情報です。
 * 
 * <p>レスポンスフィールドのforecasts内のデータを管理します。
 * @author 春田和也
 * 
 */
@Data
public class WeatherData {
	/** 予報日 */
	private String dateLabel;

	/** 天気 */
	private String telop;

	/** 天気の画像 */
	private String image;
}
