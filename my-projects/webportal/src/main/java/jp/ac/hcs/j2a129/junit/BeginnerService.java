package jp.ac.hcs.j2a129.junit;

import org.springframework.stereotype.Service;

/**
 *ビギナーテストを提供するサービスクラスです。
 *<p>
 *このクラスはビギナーのテストロジックを提供します。
 *</p>
 *@author 春田和也
 */
@Service
public class BeginnerService {

	/**
	 * 【規定課題①】
	 * x と y を加算するメソッド
	 * 
	 * @param x 1つ目の整数 (例 1) 
	 * @param y 2つ目の整数 (例 2) 
	 * @return x と y を加算した結果 (例 3) 
	 */
	public int add(int x, int y) {
		int result = x + y;
		return result;
	}

	/**
	 * 【規定課題②】
	 * 与えられた整数が偶数か奇数かを判定するメソッド
	 *
	 * @param x 判定対象の整数 (例 10) 
	 * @return "偶数" または "奇数" (例 偶数) 
	 */
	public String evenOdd(int x) {
		if (x % 2 == 0) {
			return "偶数";
		} else {
			return "奇数";
		}
	}

	/**
	 * 【規定課題③】
	 * 1から与えられた整数 x までの和を計算するメソッド
	 *
	 * @param x 計算対象の整数 (例 10) 
	 * @return 1から x までの和 (例 55) 
	 */
	public int sum(int x) {
		int sum = 0;

		for (int i = 1; i <= x; i++) {
			sum += i;
		}

		return sum;
	}

	/**
	 * 【規定課題④】
	 * x を y 乗する計算を行うメソッド
	 *
	 * @param x 底となる正の整数 (例 2) 
	 * @param y 指数となる正の整数 (例 3) 
	 * @return x を y 乗した結果 (例 8) 
	 * @throws IllegalArgumentException xとyはどちらかが正の整数でければ例外が発生します
	 */
	public double power(int x, int y) {
		if (x <= 0 || y <= 0) {
			throw new IllegalArgumentException("xとyは正の整数でなければなりません。");
		}
		double result = 1.0;

		for (int i = 0; i < y; i++) {
			result *= x;
		}

		return result;
	}
}