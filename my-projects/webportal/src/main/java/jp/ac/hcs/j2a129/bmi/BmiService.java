package jp.ac.hcs.j2a129.bmi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

/**
 *BMI計算の業務ロジックを提供するサービスクラスです。
 *<p>
 *このクラスは身長と体重を受け取り、BMI情報を計算して返すビジネスロジックを提供します。
 *</p>
 *@author 春田和也
 */
@Service
public class BmiService {
	/**
	 * 身長と体重を受け取り、入力チェックをして返すメソッドです。
	 * @param height 身長（単位：センチメートル）
	 * @param weight 体重（単位：キログラム）
	 * @return 入力チェックをした結果を返却する（false：正常値）
	 */
	public boolean validate(String height, String weight) {
		try {
			// 入力されたパラメータをDouble型で取得する
			Double heightDouble = Double.parseDouble(height);
			Double weightDouble = Double.parseDouble(weight);
			// heightの入力値の範囲チェック
			if (!(30.0 <= heightDouble && 250.0 >= heightDouble)) {
				throw new Exception();
			}
			// weightの入力値の範囲チェック
			if (!(5.0 <= weightDouble && 200.0 >= weightDouble)) {
				throw new Exception();
			}
		} catch (Exception c) {
			// エラーがあった場合は、trueを返す。
			return true;
		}

		return false;
	}

	/**
	 * 身長と体重を受け取り、BMI情報を計算して返すメソッドです。
	 * @param height 身長（単位：センチメートル）
	 * @param weigh 体重（単位：キログラム）
	 * @return BMI情報を格納したBmiEntityオブジェクト
	 */

	public BmiData exec(String height, String weigh) {
		//BMI像法を格納するクラスを生成
		BmiData data = new BmiData();
		// 計算結果を格納
		String ans = calc(height, weigh);
		data.setAns(ans);
		// コメントを格納
		String comment = judge(ans);
		data.setComment(comment);
		// 画像パスを格納
		String img = img(ans);
		data.setPath(img);
		return data;
	}

	private String calc(String height, String weigh) {
		// 身長をセンチメートルからメートルへ変換
		double m = Double.parseDouble(height) / 100;
		// BMIを計算
		double bmi = Double.parseDouble(weigh) / (Integer.parseInt(height) * m) * 100000;
		// 小数第3位まで切り捨て
		String ans = String.valueOf(Math.floor(bmi) / 1000);
		return ans;
	}

	private String judge(String ans) {
		// BMIをDouble型に変換
		Double bmi = Double.parseDouble(ans);
		// BMIの範囲をリストで定義
		List<Double> bmiList = new ArrayList<Double>(
				Arrays.asList(16.0, 17.0, 18.5, 25.0, 30.0, 35.0, 40.0));
		// コメントをリストで定義
		List<String> commentList = new ArrayList<String>(
				Arrays.asList("痩せ過ぎ", "痩せ", "痩せぎみ", "普通体重", "前肥満", "肥満度(1度)", "肥満度(2度)", "肥満度(3度)"));
		// forで比較をし、コメントをセットしていく。
		String comment = "";
		for (int i = 0; i < bmiList.size(); i++) {
			if (bmi < bmiList.get(i)) {
				comment = commentList.get(i);
				break;
			} else {
				comment = commentList.get(7);
			}
		}
		return comment;
	}

	private String img(String ans) {
		// BMIをDouble型に変換
		Double value = Double.parseDouble(ans);
		String img = "";
		// BMIによって画像を変換する。
		if (value < 18.5) {
			img = "/img/bmi/gari.png";
		} else if (value < 25.0) {
			img = "/img/bmi/normal.png";
		} else {
			img = "/img/bmi/puni.png";
		}
		return img;
	}
}
