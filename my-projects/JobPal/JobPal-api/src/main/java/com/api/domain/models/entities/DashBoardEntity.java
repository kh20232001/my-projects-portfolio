package com.api.domain.models.entities;

import java.util.List;

import com.api.domain.models.dbdata.JobSearchDashBoardData;

/**
 * ダッシュボードの情報を管理するエンティティクラスです。
 *
 * <p>
 * DBとController間を本クラスでモデル化します。<br>
 * DBからダッシュボードの情報が取得できない場合は、リストが空となります。
 * <p>
 * <strong>リストにnullは含まれません</strong>
 */
public class DashBoardEntity {

	/**
	 * ダッシュボードリスト
	 */
	private List<JobSearchDashBoardData> dashBoardList;

	/**
	 * 通知の件数
	 */
	private int alertCnt;

	// gettter
	public List<JobSearchDashBoardData> getDashBoardList() {
		return dashBoardList;
	}

	public int getAlertCnt() {
		return alertCnt;
	}

	// setter
	public void setDashBoardList(List<JobSearchDashBoardData> dashBoardList) {
		this.dashBoardList = dashBoardList;
	}

	public void setAlertCnt(int alertCnt) {
		this.alertCnt = alertCnt;
	}

}
