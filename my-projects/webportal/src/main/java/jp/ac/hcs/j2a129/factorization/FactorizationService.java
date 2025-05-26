package jp.ac.hcs.j2a129.factorization;

import org.springframework.stereotype.Service;

/**
 * 素因数分解取得の業務ロジッククラスです。
 * 
 * <p>
 * このクラスは素因数分解を取得します。
 * </p>
 * @author 春田和也
 */
@Service
public class FactorizationService {
	/**
	 * 素因数分解を取得します。
	 * @param input 素因数分解の対象数値
	 * @return 素因数分解情報を格納したFactorizationEntityオブジェクト
	 */
	public FactorizationData exec(String input) {
		int value = Integer.parseInt(input);
		int resultValue = value;
		String result = "";
		//素数情報を格納するクラスを生成
		FactorizationData factorizationData = new FactorizationData();

		// 素数の判定
		for (int i = 2; i <= value; i++) {
			while (resultValue % i == 0) {
				resultValue /= i;
				result += String.valueOf(i) + "×";
			}

		}
		result = result.substring(0, result.length() - 1);
		// 素数を格納
		factorizationData.setAns(result);

		return factorizationData;
	}
}
