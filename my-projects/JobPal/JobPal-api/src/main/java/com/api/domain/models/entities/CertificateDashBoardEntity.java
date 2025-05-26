package com.api.domain.models.entities;

import java.util.ArrayList;
import java.util.List;

import com.api.domain.models.displaydata.CertificateDisplayData;

/**
 * 証明書発行のダッシュボード表示に必要な入力値を保持するエンティティクラスです。
 *
 * <p>
 * 各項目のデータ仕様は基本設計書を参照してください。
 */
public class CertificateDashBoardEntity {

	/**
	 * ユーザ名
	 */
	private String userName;

	/**
	 * 通知の件数
	 */
	private int alertCnt;

	/**
	 * ダッシュボードリスト
	 */
	private List<CertificateDisplayData> dashBoardList;

	public CertificateDashBoardEntity() {
	}

	public CertificateDashBoardEntity(String userName) {
		this.setUserName(userName);
		this.setAlertCnt(0);
		this.setDashBoardList(new ArrayList<>());
	}

	// gettter
	public String getUserName() {
		return userName;
	}

	public int getAlertCnt() {
		return alertCnt;
	}

	public List<CertificateDisplayData> getDashBoardList() {
		return dashBoardList;
	}

	// setter
	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setAlertCnt(int alertCnt) {
		this.alertCnt = alertCnt;
	}

	public void setDashBoardList(List<CertificateDisplayData> dashBoardList) {
		this.dashBoardList = dashBoardList;

	}

	@Override
	public String toString() {
		return "CertificateDashBoardEntity [userName=" + userName + ", alertCnt=" + alertCnt + ", dashBoardList="
				+ dashBoardList + "]";
	}

}
