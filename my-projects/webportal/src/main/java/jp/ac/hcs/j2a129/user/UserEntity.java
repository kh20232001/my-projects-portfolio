package jp.ac.hcs.j2a129.user;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * ユーザ情報を管理するクラスです。
 * 
 * <p>DBとController間を本クラスでモデル化します。<br>
 * DBからタスク情報が取得できない場合は、リストが空となります。
 * <p><strong>リストにnullは含まれません</strong>
 * @author 春田和也
 */
@Data
public class UserEntity {

	/**	ユーザ情報のリスト */
	private List<UserData> userList = new ArrayList<UserData>();

	/** エラーメッセージ（表示用）*/
	private String errorMessage;

	/** メッセージ（表示用）*/
	private String message;
}
