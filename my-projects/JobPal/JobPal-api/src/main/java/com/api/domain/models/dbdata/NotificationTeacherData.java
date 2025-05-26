package com.api.domain.models.dbdata;

/**
 * 1件分の通知担任情報を表すモデルクラスです。
 *
 * <p>
 * 各項目は通知に関連する担任のデータを保持します。<br>
 * データベース定義を参照して、各フィールドの仕様を確認してください。
 * </p>
 *
 */
public class NotificationTeacherData {

	/**
	 * ユーザID（メールアドレス）。
	 * 主キーで必須入力。メールアドレス形式。
	 */
	private String teacherUserId;

	/**
	 * 学科。
	 */
	private String department;

	/**
	 * 学年。
	 */
	private Integer grade;

	/**
	 * クラス。
	 */
	private String className;

	public NotificationTeacherData(String teacherUserId, String department, Integer grade, String className) {
		this.teacherUserId = teacherUserId;
		this.department = department;
		this.grade = grade;
		this.className = className;
	}

	public NotificationTeacherData() {

	}

	// GetterおよびSetterメソッド
	public String getTeacherUserId() {
		return teacherUserId;
	}

	public void setTeacherUserId(String teacherUserId) {
		this.teacherUserId = teacherUserId;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public Integer getGrade() {
		return grade;
	}

	public void setGrade(Integer grade) {
		this.grade = grade;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}
}
