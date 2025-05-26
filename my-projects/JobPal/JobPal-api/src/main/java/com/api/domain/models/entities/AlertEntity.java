package com.api.domain.models.entities;

import java.util.List;

import com.api.domain.models.data.AlertData;

/**
 * 通知の情報を管理するエンティティクラスです。
 *
 * <p>
 * DBとController間を本クラスでモデル化します。<br>
 * DBから通知の情報が取得できない場合は、リストが空となります。
 * <p>
 * <strong>リストにnullは含まれません</strong>
 */
public class AlertEntity {

	/**
	 * 通知対象のダッシュボードリスト
	 */
	private List<AlertData> alertList;

	// gettter
	public List<AlertData> getAlertList() {
		return this.alertList;
	}

	// setter
	public void setAlertList(List<AlertData> alertList) {
		this.alertList = alertList;
	}

}
