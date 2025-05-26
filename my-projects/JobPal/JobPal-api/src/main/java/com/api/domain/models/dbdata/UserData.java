package com.api.domain.models.dbdata;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 1件分のユーザ情報を表すモデルクラスです。
 *
 * <p>
 * 各項目はユーザの詳細な属性を保持します。<br>
 * データベース定義を参照して、各項目の仕様を確認してください。
 * </p>
 *
 */

public class UserData {
	/**
	 * 管理者・事務部用
	 * @param userId
	 * @param password
	 * @param userName
	 * @param userStatus
	 * @param userType
	 * @param createdByUserId
	 * @param createdAt
	 * @param updatedAt
	 */
	public UserData(String userId, String password, String userName, String userStatus, String userType,
			String createdByUserId, Timestamp createdAt, Timestamp updatedAt) {
		super();
		this.userId = userId;
		this.password = password;
		this.userName = userName;
		this.userStatus = userStatus;
		this.userType = userType;
		this.createdByUserId = createdByUserId;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	/**
	 * 担任・学生用
	 * @param userId
	 * @param password
	 * @param userName
	 * @param userStatus
	 * @param userType
	 * @param createdByUserId
	 * @param createdAt
	 * @param updatedAt
	 * @param department
	 * @param grade
	 * @param className
	 * @param attendanceNumber
	 * @param studentId
	 */
	public UserData(String userId, String password, String userName, String userStatus, String userType,
			String createdByUserId, Timestamp createdAt, Timestamp updatedAt, String department, Integer grade,
			String className, String attendanceNumber, Integer studentId) {
		super();
		this.userId = userId;
		this.password = password;
		this.userName = userName;
		this.userStatus = userStatus;
		this.userType = userType;
		this.createdByUserId = createdByUserId;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.department = department;
		this.grade = grade;
		this.className = className;
		this.attendanceNumber = attendanceNumber;
		this.studentId = studentId;
	}

	public UserData() {

	}

	/**
	 * ユーザID（メールアドレス）
	 * 主キー、必須入力、メールアドレス形式
	 */
	private String userId;

	/**
	 * パスワード
	 * 必須入力、長さ4から100桁まで、半角英数字のみ
	 */
	private String password;

	/**
	 * ユーザ名
	 * 必須入力
	 */
	private String userName;

	/**
	 * アカウント有効性
	 * - 0 ： 有効
	 * - 1 ： ロック中
	 * - 2 ： 卒業
	 * - 3 ： 削除
	 * - 4 ： 無効
	 *
	 */
	private String userStatus;
	/**
	 * 権限
	 * - 0 : 学生
	 * - 1 : 担任
	 * - 2 : 管理
	 * - 3 : 事務部
	 * 必須入力
	 */
	private String userType;

	/**
	 * 作成ユーザーID
	 */
	private String createdByUserId;

	/**
	 * 作成日時
	 */
	private Timestamp createdAt;

	/**
	 * 更新日時
	 */
	private Timestamp updatedAt;
	/**
	 * 学科
	 */
	private String department;
	/**
	 * 学年
	 */
	private Integer grade;
	/**
	 * クラス
	 */
	private String className;
	/**
	 * 出席番号
	 */
	private String attendanceNumber;
	/**
	 * 学籍番号
	 */
	private Integer studentId;

	// GetterおよびSetterメソッド
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
	}

	public String getUserStatus() {
		return userStatus;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getUserType() {
		return userType;
	}

	public void setCreatedByUserId(String createdByUserId) {
		this.createdByUserId = createdByUserId;
	}

	public String getCreatedByUserId() {
		return createdByUserId;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setUpdateAt(Timestamp updateAt) {
		this.updatedAt = updateAt;
	}

	public Timestamp getUpdatedAt() {
		return updatedAt;
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

	public void setGrade(int grade) {
		this.grade = grade;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getAttendanceNumber() {
		return attendanceNumber;
	}

	public void setAttendanceNumber(String attendanceNumber) {
		this.attendanceNumber = attendanceNumber;
	}

	public Integer getStudentId() {
		return studentId;
	}

	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}

	public Timestamp convertStringToTimestamp(String dateString) {
		try {
			// 指定したフォーマットに合わせてSimpleDateFormatを設定
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date parsedDate = dateFormat.parse(dateString);

			// DateオブジェクトをTimestampに変換
			return new Timestamp(parsedDate.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}

	}

	public String convertTimestampToString(Timestamp timestamp) {
		// 指定したフォーマットでSimpleDateFormatを設定
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		// Timestampをフォーマットされた文字列に変換
		return dateFormat.format(timestamp);
	}

	// プライベートコンストラクタ（ビルダーからのみ呼び出し可能）
	private UserData(Builder builder) {
		this.userId = builder.userId;
		this.password = builder.password;
		this.userName = builder.userName;
		this.userStatus = builder.userStatus;
		this.userType = builder.userType;
		this.createdByUserId = builder.createdByUserId;
		this.createdAt = builder.createdAt;
		this.updatedAt = builder.updatedAt;
		this.department = builder.department;
		this.grade = builder.grade;
		this.className = builder.className;
		this.attendanceNumber = builder.attendanceNumber;
		this.studentId = builder.studentId;
	}

	// Builderクラス
	public static class Builder {
		private String userId;
		private String password;
		private String userName;
		private String userStatus;
		private String userType;
		private String createdByUserId;
		private Timestamp createdAt;
		private Timestamp updatedAt;
		private String department;
		private Integer grade;
		private String className;
		private String attendanceNumber;
		private Integer studentId;

		public Builder setUserId(String userId) {
			this.userId = userId;
			return this;
		}

		public Builder setPassword(String password) {
			this.password = password;
			return this;
		}

		public Builder setUserName(String userName) {
			this.userName = userName;
			return this;
		}

		public Builder setUserStatus(String userStatus) {
			this.userStatus = userStatus;
			return this;
		}

		public Builder setUserType(String userType) {
			this.userType = userType;
			return this;
		}

		public Builder setCreatedByUserId(String createdByUserId) {
			this.createdByUserId = createdByUserId;
			return this;
		}

		public Builder setCreatedAt(Timestamp createdAt) {
			this.createdAt = createdAt;
			return this;
		}

		public Builder setUpdatedAt(Timestamp updatedAt) {
			this.updatedAt = updatedAt;
			return this;
		}

		public Builder setDepartment(String department) {
			this.department = department;
			return this;
		}

		public Builder setGrade(Integer grade) {
			this.grade = grade;
			return this;
		}

		public Builder setClassName(String className) {
			this.className = className;
			return this;
		}

		public Builder setAttendanceNumber(String attendanceNumber) {
			this.attendanceNumber = attendanceNumber;
			return this;
		}

		public Builder setStudentId(Integer studentId) {
			this.studentId = studentId;
			return this;
		}

		public UserData build() {
			return new UserData(this);
		}
	}

	@Override
	public String toString() {
		return "User{" +
				"userId='" + userId + '\'' +
				", password='" + password + '\'' +
				", userName='" + userName + '\'' +
				", userStatus=" + userStatus +
				", userType=" + userType +
				", createdByUserId='" + createdByUserId + '\'' +
				", createdAt=" + createdAt +
				", updatedAt=" + updatedAt +
				", department='" + department + '\'' +
				", grade=" + grade +
				", className='" + className + '\'' +
				", attendanceNumber=" + attendanceNumber +
				", studentId='" + studentId +
				'}';
	}

}
