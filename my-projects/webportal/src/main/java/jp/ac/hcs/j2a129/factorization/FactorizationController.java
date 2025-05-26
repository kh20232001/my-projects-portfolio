package jp.ac.hcs.j2a129.factorization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 素数の画像表示を行うためのコントローラクラスです。
 * <p>
 * このクラスはSpring MVCフレームワークでリクエストを処理し、画面表示を担当します。
 * </p>
 * @author 春田和也
 */
@Controller
public class FactorizationController {

	@Autowired
	private FactorizationService factorizationService;

	/**
	 * Prime入力画面へのリクエストハンドラです。
	 * @return Prime入力画面へのパス
	 */
	@GetMapping("/factorization")
	public String getFactorization() {
		// 画面のみを返却
		return "factorization/input";
	}

	/**
	 * Primeの結果を表示するためのリクエストハンドラです。
	 * @param model モデルオブジェクト
	 * @param value 素数の求める上限値
	 * @return ワンちゃん画像結果を表示する画面パス
	 */
	@PostMapping("/factorization")
	public String PostFactorization(Model model,
			@RequestParam(name = "value") String value) {
		// データを取得
		System.out.println(value);
		FactorizationData factorizationData = factorizationService.exec(value);

		model.addAttribute("result", factorizationData);
		// 画面を返却
		return "factorization/result";
	}
}
