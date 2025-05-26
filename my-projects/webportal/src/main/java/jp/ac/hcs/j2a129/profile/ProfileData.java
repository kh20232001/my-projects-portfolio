package jp.ac.hcs.j2a129.profile;

import lombok.Data;

/**
 * 1件分のプロフィール情報です。
 * 
 * <p>各データ構造については、データベース定義をご覧ください。
 * @author 春田和也
 */
@Data
public class ProfileData {
	/**
	 * タスクID：主キー、SQLにて自動採番
	 */
	private int id;

	private String userId;

	private String userName;

	private String userKana;

	private String classNumber;

	private String image;

	private String commentText;

}