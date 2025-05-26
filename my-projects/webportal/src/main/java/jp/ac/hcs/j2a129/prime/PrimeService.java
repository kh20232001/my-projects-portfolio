package jp.ac.hcs.j2a129.prime;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

/**
 * 素数取得の業務ロジッククラスです。
 * 
 * <p>
 * このクラスは素数を取得します。
 * </p>
 * @author 春田和也
 */
@Service
public class PrimeService {
	/**
	 * 数字の範囲を受け取り、範囲内の素数を計算して返すメソッドです。
	 * @param range 素数の上限値
	 * @return 素数を格納したBmiEntityオブジェクト
	 */
	public PrimeData exec(String range) {
		// BMI情報を格納するクラス生成
		PrimeData data = new PrimeData();
		int max = Integer.parseInt(range);
		List<Integer> ans = new ArrayList<Integer>();
		// 計算結果を格納
		for (int num = 2; num <= max; num++) {
			Integer result = calc(num);
			if (result != -1) {
				ans.add(result);
			}
		}

		data.setAns(ans);

		System.out.println(ans);

		return data;

	}

	/**
	 * 数字の範囲の素数を計算し、返すメソッドです。
	 * @param range　範囲
	 * @return num
	 */
	private Integer calc(int num) {
		// 素数を計算
		for (int i = 2; i < num; i++) {
			if (num % i == 0) {
				return -1;
			}
		}

		return num;
	}

}
