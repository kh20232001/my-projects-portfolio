package com.api.domain.models.displaydata;

/**
 * CSVに必要な統計データを保持するクラスです。
 *
 * <p>
 * このクラスは、活動中の学生や活動場所、活動形式に関する統計情報を管理します。
 * 各項目のデータ仕様は基本設計書を参照してください。
 * </p>
 */
public class CsvDisplayData {

	// フィールド定義

	/**
	 * 活動中の学生
	 */
	private Integer studentsInAction;

	/**
	 * 活動終了の学生
	 */
	private Integer studentsOfEnd;

	/**
	 * 東京での活動場所
	 */
	private Integer activityLocationInTokyo;

	/**
	 * 札幌での活動場所
	 */
	private Integer activityLocationInSapporo;

	/**
	 * その他の場所での活動
	 */
	private Integer activityLocationInOther;

	/**
	 * オンラインでの活動形式
	 */
	private Integer activityFormInOnline;

	/**
	 * 現地での活動形式
	 */
	private Integer activityFormInLocal;

	/**
	 * その他の活動形式
	 */
	private Integer activityFormInOther;

	// ゲッターとセッター

	public Integer getStudentsInAction() {
		return studentsInAction;
	}

	public void setStudentsInAction(Integer studentsInAction) {
		this.studentsInAction = studentsInAction;
	}

	public Integer getStudentsOfEnd() {
		return studentsOfEnd;
	}

	public void setStudentsOfEnd(Integer studentsOfEnd) {
		this.studentsOfEnd = studentsOfEnd;
	}

	public Integer getActivityLocationInTokyo() {
		return activityLocationInTokyo;
	}

	public void setActivityLocationInTokyo(Integer activityLocationInTokyo) {
		this.activityLocationInTokyo = activityLocationInTokyo;
	}

	public Integer getActivityLocationInSapporo() {
		return activityLocationInSapporo;
	}

	public void setActivityLocationInSapporo(Integer activityLocationInSapporo) {
		this.activityLocationInSapporo = activityLocationInSapporo;
	}

	public Integer getActivityLocationInOther() {
		return activityLocationInOther;
	}

	public void setActivityLocationInOther(Integer activityLocationInOther) {
		this.activityLocationInOther = activityLocationInOther;
	}

	public Integer getActivityFormInOnline() {
		return activityFormInOnline;
	}

	public void setActivityFormInOnline(Integer activityFormInOnline) {
		this.activityFormInOnline = activityFormInOnline;
	}

	public Integer getActivityFormInLocal() {
		return activityFormInLocal;
	}

	public void setActivityFormInLocal(Integer activityFormInLocal) {
		this.activityFormInLocal = activityFormInLocal;
	}

	public Integer getActivityFormInOther() {
		return activityFormInOther;
	}

	public void setActivityFormInOther(Integer activityFormInOther) {
		this.activityFormInOther = activityFormInOther;
	}

	// ビルダークラス

	/**
	 * CsvDisplayDataのインスタンスを構築するためのビルダークラスです。
	 */
	public static class Builder {
		private Integer studentsInAction;
		private Integer studentsOfEnd;
		private Integer activityLocationInTokyo;
		private Integer activityLocationInSapporo;
		private Integer activityLocationInOther;
		private Integer activityFormInOnline;
		private Integer activityFormInLocal;
		private Integer activityFormInOther;

		public Builder studentsInAction(Integer studentsInAction) {
			this.studentsInAction = studentsInAction;
			return this;
		}

		public Builder studentsOfEnd(Integer studentsOfEnd) {
			this.studentsOfEnd = studentsOfEnd;
			return this;
		}

		public Builder activityLocationInTokyo(Integer activityLocationInTokyo) {
			this.activityLocationInTokyo = activityLocationInTokyo;
			return this;
		}

		public Builder activityLocationInSapporo(Integer activityLocationInSapporo) {
			this.activityLocationInSapporo = activityLocationInSapporo;
			return this;
		}

		public Builder activityLocationInOther(Integer activityLocationInOther) {
			this.activityLocationInOther = activityLocationInOther;
			return this;
		}

		public Builder activityFormInOnline(Integer activityFormInOnline) {
			this.activityFormInOnline = activityFormInOnline;
			return this;
		}

		public Builder activityFormInLocal(Integer activityFormInLocal) {
			this.activityFormInLocal = activityFormInLocal;
			return this;
		}

		public Builder activityFormInOther(Integer activityFormInOther) {
			this.activityFormInOther = activityFormInOther;
			return this;
		}

		/**
		 * CsvDisplayDataのインスタンスを構築します。
		 *
		 * @return CsvDisplayDataのインスタンス
		 */
		public CsvDisplayData build() {
			CsvDisplayData stats = new CsvDisplayData();
			stats.setStudentsInAction(this.studentsInAction);
			stats.setStudentsOfEnd(this.studentsOfEnd);
			stats.setActivityLocationInTokyo(this.activityLocationInTokyo);
			stats.setActivityLocationInSapporo(this.activityLocationInSapporo);
			stats.setActivityLocationInOther(this.activityLocationInOther);
			stats.setActivityFormInOnline(this.activityFormInOnline);
			stats.setActivityFormInLocal(this.activityFormInLocal);
			stats.setActivityFormInOther(this.activityFormInOther);
			return stats;
		}
	}

	@Override
	public String toString() {
		return "CsvDisplayData{" +
				"studentsInAction=" + studentsInAction +
				", studentsOfEnd=" + studentsOfEnd +
				", activityLocationInTokyo=" + activityLocationInTokyo +
				", activityLocationInSapporo=" + activityLocationInSapporo +
				", activityLocationInOther=" + activityLocationInOther +
				", activityFormInOnline=" + activityFormInOnline +
				", activityFormInLocal=" + activityFormInLocal +
				", activityFormInOther=" + activityFormInOther +
				'}';
	}
}
