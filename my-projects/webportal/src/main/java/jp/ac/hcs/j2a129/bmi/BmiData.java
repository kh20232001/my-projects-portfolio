package jp.ac.hcs.j2a129.bmi;

/**
 * BMI情報を格納するためのエンティティクラスです。
 * <p>
 * このクラスはBMI値、画像のパス、コメントを保持します。
 * </p>
 * @author 春田和也
 */
public class BmiData {

	/** BMI値（小数点第3位まで） */
	private String ans;

	/** 画像のパス */
	private String path;

	/** コメント */
	private String comment;

	/**
	 * Ansのgetメソッドです。
	 * @return ansの値
	 */
	public String getAns() {
		return ans;
	}

	/**
	 * Ansのsetメソッドです。
	 * @param ans ansの入力値
	 */
	public void setAns(String ans) {
		this.ans = ans;
	}

	/**
	 * Pathのgetメソッドです。
	 * @return path pathの値
	 */
	public String getPath() {
		return path;
	}

	/**
	 * Pathのsetメソッドです。
	 * @param path pathの入力値
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * Commentのgetメソッドです。
	 * @return comment commentの値
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * Commentのsetメソッドです。
	 * @param comment commentの入力値
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}
}
