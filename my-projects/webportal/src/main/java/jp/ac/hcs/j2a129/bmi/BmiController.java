package jp.ac.hcs.j2a129.bmi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * BMI計算を行うためのコントローラクラスです。
 * <p>
 * このクラスはSpring MVCフレームワークでリクエストを処理し、BMI計算の画面表示を担当します。
 * </p>
 * 
 * <p>入力チェックを実施し、正しい値のみ処理します。<br>
 * クライアント側でも入力チェックを実施することを推奨します。
 * 
 * @author 春田和也
 */
@Controller
public class BmiController {

	@Autowired
	private BmiService bmiService;

	/**
	 * BMI入力画面へのリクエストハンドラです。
	 * @return BMI入力画面へのパス
	 */
	@GetMapping("/bmi")
	public String getBmi() {
		// 画面のみを返却
		return "bmi/input";
	}

	/**
	 * BMI機能を利用し、結果を表示するためのリクエストハンドラです。
	 * @param model モデルオブジェクト
	 * @param height 身長(cm)
	 * @param weight 体重(kg)
	 * @return BMI計算結果を表示する画面パス
	 */
	@PostMapping("/bmi")
	public String postBmi(Model model,
			@RequestParam(name = "cm") String height,
			@RequestParam(name = "kg") String weight) {
		boolean isValid = bmiService.validate(height, weight);
		if (isValid) {
			// 入力チェックエラーの場合、前の画面へ
			return "bmi/input";
		}
		// データを取得
		BmiData data = bmiService.exec(height, weight);
		// データをモデルオブジェクトに設定
		model.addAttribute("bmi", data);
		//画面を返却
		return "bmi/result";
	}

}
