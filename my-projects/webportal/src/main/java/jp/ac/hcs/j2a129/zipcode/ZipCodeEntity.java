package jp.ac.hcs.j2a129.zipcode;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * 郵便番号検索のエンティティクラスです。
 * 
 * <p>各項目のデータ仕様については、APIの仕様を参照してください。
 * 1ルの郵便番号に複数の住所が紐づくこともあるため、リスト構造となっています。
 * </p>
 * @author 春田和也
 */
@Data
public class ZipCodeEntity {
	/* ステータス */
	private String status;

	/* メッセージ */
	private String message;

	/* 郵便番号のリスト */
	private List<ZipCodeData> list = new ArrayList<ZipCodeData>();

	/* エラーメッセージ（表示用） */
	private String errorMessage;

}
