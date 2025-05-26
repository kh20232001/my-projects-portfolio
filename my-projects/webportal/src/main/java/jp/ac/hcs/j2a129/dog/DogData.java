package jp.ac.hcs.j2a129.dog;

import lombok.Data;

/**
 * 犬の情報を格納するためのエンティティクラスです。
 * <p>
 * このクラスは犬画像のURL、APIの結果ステータス、処理失敗時のエラーメッセージを保持します。
 * </p>
 * @author 春田和也
 */
@Data
public class DogData {

	/* 犬画像のURL */
	private String message;

	/*APIの結果ステータス */
	private String status;

	/* 処理失敗時のエラーメッセージ */
	private String errorMessage;

}
