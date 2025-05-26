package jp.ac.hcs.j2a129.dog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * ワンちゃんの画像表示を行うためのコントローラクラスです。
 * <p>
 * このクラスはSpring MVCフレームワークでリクエストを処理し、画面表示を担当します。
 * </p>
 * @author 春田和也
 */
@Controller
public class DogController {

	@Autowired
	private DogService dogService;

	/**
	 * Dog入力画面へのリクエストハンドラです。
	 * @return Dog入力画面へのパス
	 */
	@GetMapping("/dog")
	public String getBmi() {
		// 画面のみを返却
		return "dog/input";
	}

	/**
	 * DogAPIを利用し、結果を表示するためのリクエストハンドラです。
	 * @param model モデルオブジェクト
	 * @param dogPath 犬の画像のPath
	 * @return ワンちゃん画像結果を表示する画面パス
	 */
	@PostMapping("/dog")
	public String PostDog(Model model,
			@RequestParam(name = "dog") String dogPath) {
		// データを取得
		DogData dogData = dogService.exec(dogPath);
		// データをモデルオブジェクトに設定
		model.addAttribute("result", dogData);
		// 画面を返却
		return "dog/result";
	}
}
