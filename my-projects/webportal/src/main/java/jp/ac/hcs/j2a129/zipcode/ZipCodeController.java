package jp.ac.hcs.j2a129.zipcode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 郵便番号検索を行うためのコントローラークラスです。
 * 
 * <p>
 * このクラスはSpring MVCフレームワークでリクエストを処理し、郵便番号検索機能の画像表示を担当します。
 * </p>
 * 
 * <p>入力チェックを実施し、正しい郵便番号のみ処理します。<br>
 * クライアント側でも入力チェックを実施することを推奨します。
 *
 *@author 春田和也
 */
@Controller
public class ZipCodeController {
	@Autowired
	private ZipCodeService zipCodeService;

	/**
	 * 郵便番号入力画面を表示します。
	 * 
	 * @return 郵便番号入力画面へのパス
	 */
	@GetMapping("/zip")
	public String getZipcode() {
		return "zipcode/input";
	}

	/**
	 * 郵便番号検索を行い、結果を表示するためのリクエストハンドラです。
	 * 
	 * <p>本機能は、郵便番号検索APIを内部で呼び出して結果を表示します。<br>
	 * 仕様については、下記のドキュメントを参照してください。
	 * <p> https://zipcloud.ibsnet.co.jp/doc/api
	 * 
	 * @param zipcode 郵便番号
	 * @param model モデルオブジェクト
	 * @return 郵便番号検索結果を表示する画面のパス
	 */
	@PostMapping("/zip")
	public String postZipcode(@RequestParam("zipcode") String zipcode, Model model) {
		if (zipCodeService.validate(zipcode)) {
			return "zipcode/input";
		}
		// 検索結果を取得
		ZipCodeEntity zipcodeEntity = zipCodeService.execute(zipcode);
		// 結果を画面に設定	
		if (zipcodeEntity.getList().size() == 0) {
			return "zipcode/input";
		}
		model.addAttribute("results", zipcodeEntity);
		return "zipcode/result";
	}
}
