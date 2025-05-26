package com.api.domain.models.forms;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 就職活動状態の変更情報を保持するクラスです。
 *
 * <p>
 * 各項目のデータ仕様は基本設計書を参照してください。
 *
 */
public class JobStateUpdateForm {

	/**
	 * 就職活動ID
	 *
	 * 空白禁止
	 */
	@NotBlank(message = "{require_check}")
	private String jobHuntId;

	/**
	 * 状態ID
	 *
	 * 空白禁止
	 */
	@NotBlank(message = "{require_check}")
	private String jobStateId;

	/**
	 * ボタンID（0~2）
	 *
	 * 空白禁止
	 * 0：承認、1：取り下げ、2：差戻し、3：コース担当承認
	 */
	@NotNull(message = "{require_check}")
	@Min(value = 0, message = "ボタンIDは0以上である必要があります")
	@Max(value = 3, message = "ボタンIDは2以下である必要があります")
	private int buttonId;

	/**
	 * 学校申込チェック
	 *
	 * 空白禁止
	 */
	@NotNull(message = "{require_check}")
	private Boolean schoolCheck;

	// gettter
	public String getJobHuntId() {
		return jobHuntId;
	}

	public String getJobStateId() {
		return jobStateId;
	}

	public int getButtonId() {
		return buttonId;
	}

	public boolean isSchoolCheck() {
		return schoolCheck;
	}

	// setter
	public void setJobHuntId(String jobHuntId) {
		this.jobHuntId = jobHuntId;
	}

	public void setJobStateId(String jobStateId) {
		this.jobStateId = jobStateId;
	}

	public void setButtonId(int buttonId) {
		this.buttonId = buttonId;
	}

	public void setSchoolCheck(boolean schoolCheck) {
		this.schoolCheck = schoolCheck;
	}

}
