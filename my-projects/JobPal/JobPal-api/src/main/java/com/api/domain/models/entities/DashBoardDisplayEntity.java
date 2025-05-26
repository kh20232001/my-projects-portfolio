package com.api.domain.models.entities;

import java.util.List;

import com.api.domain.models.displaydata.CsvDisplayData;
import com.api.domain.models.displaydata.DashBoardDisplayData;

/**
 * 画面に表示するダッシュボードの情報を管理するエンティティクラスです。
 *
 * <p>
 * DBとController間を本クラスでモデル化します。<br>
 * DBからダッシュボードの情報が取得できない場合は、リストが空となります。
 * <p>
 * <strong>リストにnullは含まれません</strong>
 */
public class DashBoardDisplayEntity {

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
	private List<DashBoardDisplayData> dashBoardList;

	/**
	 * ダッシュボードリスト
	 */
	private List<CsvDisplayData> csvList;

	// gettter
	public String getUserName() {
		return userName;
	}

	public int getAlertCnt() {
		return alertCnt;
	}

	public List<DashBoardDisplayData> getDashBoardList() {
		return dashBoardList;
	}

	public List<CsvDisplayData> getCsvList() {
		return csvList;
	}

	// setter
	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setAlertCnt(int alertCnt) {
		this.alertCnt = alertCnt;
	}

	public void setDashBoardList(List<DashBoardDisplayData> dashBoardList) {
		this.dashBoardList = dashBoardList;
	}

	public void setCsvList(List<CsvDisplayData> csvList) {
		this.csvList = csvList;
	}
}
