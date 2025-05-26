package com.api.domain.models.entities;

import java.util.List;

import com.api.domain.models.displaydata.CertificateDisplayData;
import com.api.domain.models.displaydata.DashBoardDisplayData;

/**
 * 画面に表示する通知の情報を管理するエンティティクラスです。
 *
 * <p>
 * DBとController間を本クラスでモデル化します。<br>
 * DBから通知の情報が取得できない場合は、リストが空となります。
 * <p>
 * <strong>リストにnullは含まれません</strong>
 */
public class AlertDisplayEntity {

	/**
	 * ユーザ名
	 */
	private String userName;

	/**
	 * 画面に表示する通知リスト
	 */
	private List<CertificateDisplayData> certificateList;

	/**
	 * 画面に表示する通知リスト
	 */
	private List<DashBoardDisplayData> dashBoardList;

	// gettter
	public String getUserName() {
		return userName;
	}

	public List<CertificateDisplayData> getCertificateList() {
		return certificateList;
	}

	public List<DashBoardDisplayData> getDashBoardList() {
		return dashBoardList;
	}

	// setter
	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setCertificateList(List<CertificateDisplayData> certificateList) {
		this.certificateList = certificateList;
	}

	public void setDashBoardList(List<DashBoardDisplayData> dashBoardList) {
		this.dashBoardList = dashBoardList;
	}

	// ビルダークラス
	public static class Builder {
		private String userName;
		private List<CertificateDisplayData> certificateList;
		private List<DashBoardDisplayData> dashBoardList;

		public Builder setUserName(String userName) {
			this.userName = userName;
			return this;
		}

		public Builder setCertificateList(List<CertificateDisplayData> certificateList) {
			this.certificateList = certificateList;
			return this;
		}

		public Builder setDashBoardList(List<DashBoardDisplayData> dashBoardList) {
			this.dashBoardList = dashBoardList;
			return this;
		}

		public AlertDisplayEntity build() {
			AlertDisplayEntity entity = new AlertDisplayEntity();
			entity.setUserName(userName);
			entity.setCertificateList(certificateList);
			entity.setDashBoardList(dashBoardList);
			return entity;
		}
	}

	// ビルダーインスタンスを返す静的メソッド
	public static Builder builder() {
		return new Builder();
	}

}
