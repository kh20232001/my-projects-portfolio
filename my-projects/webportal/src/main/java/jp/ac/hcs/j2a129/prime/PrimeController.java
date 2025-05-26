package jp.ac.hcs.j2a129.prime;

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
public class PrimeController {

	@Autowired
	private PrimeService primeService;

	/**
	 * Prime入力画面へのリクエストハンドラです。
	 * @return Prime入力画面へのパス
	 */
	@GetMapping("/prime")
	public String getPrime() {
		// 画面のみを返却
		return "prime/input";
	}

	/**
	 * Primeの結果を表示するためのリクエストハンドラです。
	 * @param model モデルオブジェクト
	 * @param value 素数の表示の上限値
	 * @return 素数結果を表示する画面パス
	 */
	@PostMapping("/prime")
	public String PostPrime(Model model,
			@RequestParam(name = "value") String value) {
		// データを取得
		System.out.println(value);
		PrimeData primeData = primeService.exec(value);

		model.addAttribute("result", primeData);
		// 画面を返却
		return "prime/result";
	}
}
