package jp.ac.hcs.j2a129.junit;

import org.springframework.stereotype.Service;

/**
 *エキスパートテストを提供するサービスクラスです。
 *<p>
 *このクラスはエキスパートのテストロジックを提供します。
 *</p>
 *@author 春田和也
 */
@Service
public class ExpertService {

	/**
	 * 【追加課題①】
	 * 与えられた整数 x の階乗を計算するメソッド
	 *
	 * @param x 階乗を計算する対象の整数 (例 3) 
	 * @return x の階乗 (例 6) 
	 */
	public int factorial(int x) {
		int result = 1;

		for (int i = 1; i <= x; i++) {
			result *= i;
		}

		return result;
	}

	/**
	 * 【追加課題②】
	 * 配列内の最大値を検索するメソッド
	 *
	 * @param list 整数の配列 (例 {6,5,9,4,0}) 
	 * @return 配列内の最大値 (例 9) 
	 * @throws IllegalArgumentException 配列が空の場合、例外が発生します
	 */
	public int max(int[] list) {
		if (list.length == 0) {
			throw new IllegalArgumentException("配列が空です。");
		}
		int maxValue = list[0];

		for (int i = 1; i < list.length; i++) {
			if (list[i] > maxValue) {
				maxValue = list[i];
			}
		}

		return maxValue;
	}

	/**
	 * 【追加課題③】
	 * 2つの整数配列に共通して存在する要素の数を検索するメソッド
	 *
	 * @param list1 1つ目の整数配列 (例 {6,5,9,4,0}) 
	 * @param list2 2つ目の整数配列 (例 {2,5,3,4,1}) 
	 * @return 2つの配列に共通して存在する要素の数 (例 2) 
	 */
	public int common(int[] list1, int[] list2) {
		int count = 0;

		for (int i = 0; i < list1.length; i++) {
			for (int j = 0; j < list2.length; j++) {
				if (list1[i] == list2[j]) {
					count++;
				}
			}
		}

		return count;
	}

	/**
	 * 【追加課題④】
	 * バブルソートアルゴリズムを使用して整数配列を昇順に並べ替えるメソッド
	 * ※このメソッドにはバグ(不具合)があります. 修正した上で単体テストを作成してください
	 *
	 * @param list 並べ替え対象の整数配列 (例 {2,5,3,4,1}) 
	 * @return バブルソートによって昇順に並べ替えられた整数配列 (例 {1,2,3,4,5}) 
	 */
	public int[] bubbleSort(int[] list) {
		for (int i = 0; i < list.length - 1; i++) {
			for (int j = list.length - 1; j > 0; j--) {
				if (list[j - 1] > list[j]) {
					// 要素の交換
					int temp = list[j - 1];
					list[j - 1] = list[j];
					list[j] = temp;
				}
			}
		}
		return list;
	}

}