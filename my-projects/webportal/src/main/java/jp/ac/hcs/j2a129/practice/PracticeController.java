package jp.ac.hcs.j2a129.practice;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 道場のコントローラクラスです。
 * 道場の内容を処理し、出力します。
 * @author 春田和也
 */
@Controller
public class PracticeController {
	/**
	 * アルゴリズムの道場画面を表示します。
	 * 
	 * @return アルゴリズム道場画面へのパス
	 */
	@GetMapping("/dojo")
	public String getDojo() {
		return "practice/dojo";
	}

	/**
	 * Level0の処理を行います。
	 * 
	 * @return アルゴリズム道場画面へのパス
	 */
	@PostMapping("/level0")
	public String level0() {
		String local = "jouhou";
		String domain = "hcs.ac.jp";
		// コンソールに出力
		System.out.println(local + "@" + domain);

		return "practice/dojo";
	}

	/**
	 * Level1の処理を行います。
	 * @param model HTMLに値を渡すオブジェクト
	 * @return アルゴリズム道場結果画面へのパス
	 */
	@PostMapping("/level1")
	public String level1(Model model) {
		String input = "180 2";
		// 入力されている数字を取得
		int value1 = Integer.parseInt(input.substring(0, input.lastIndexOf(" ")));
		int value2 = Integer.parseInt(input.substring(input.lastIndexOf(" ") + 1));
		// 取得した数字をかけ100を足す
		int ans = value1 * value2 + 100;
		// 計算結果を文字列に変換
		String result = String.valueOf(ans);

		model.addAttribute("ans", result);
		return "practice/result";
	}

	/**
	 * Level2の処理を行います。
	 * @param model HTMLに値を渡すオブジェクト
	 * @return アルゴリズム道場画面へのパス
	 */
	@PostMapping("/level2")
	public String level2(Model model) {
		String input = "namae";
		// 一番最後の文字を取得
		String result = input.substring(input.length() - 1);

		model.addAttribute("ans", result);
		return "practice/result";
	}

	/**
	 * Level3処理を行います。
	 * @param input 入力文字列
	 * @param model HTMLに値を渡すオブジェクト
	 * @return アルゴリズム道場画面へのパス
	 */
	@PostMapping("/level3")
	public String level3(@RequestParam(name = "level3") String input, Model model) {
		// 入力された文字からaiueoとAIUEOを空文字に置き換える
		String result = input.replaceAll("a", "")
				.replaceAll("i", "")
				.replaceAll("u", "")
				.replaceAll("e", "")
				.replaceAll("o", "")
				.replaceAll("A", "")
				.replaceAll("I", "")
				.replaceAll("U", "")
				.replaceAll("E", "")
				.replaceAll("O", "");

		model.addAttribute("ans", result);
		return "practice/result";
	}

	/**
	 * Level4処理を行います。
	 * @param input 入力文字列
	 * @param model HTMLに値を渡すオブジェクト
	 * @return アルゴリズム道場画面へのパス
	 */
	@PostMapping("/level4")
	public String level4(@RequestParam(name = "level4") String input, Model model) {

		String[] s = input.split("");
		// 左から3つの記号が一致している(trueの場合一致)
		boolean isLeft = (s[0].equals(s[1])) && (s[0].equals(s[2]));
		// 中央の3つの記号が一致している(trueの場合一致)
		boolean isMedian = (s[1].equals(s[2])) && (s[1].equals(s[3]));
		// 右から3つの記号が一致している(trueの場合一致)
		boolean isRight = (s[4].equals(s[3])) && (s[4].equals(s[2]));
		String result = "";

		if (isLeft || isMedian || isRight) {
			// 一致している記号を取得
			result = s[2];
		} else {
			//いずれも一致しない場合はdraw
			result = "draw";
		}
		model.addAttribute("ans", result);
		return "practice/result";
	}

	/**
	 * Level5処理を行います。
	 * @param input 入力文字列
	 * @param model HTMLに値を渡すオブジェクト
	 * @return アルゴリズム道場画面へのパス
	 */
	@PostMapping("/level5")
	public String level5(@RequestParam(name = "level5") String input, Model model) {

		String[] strs = input.split("\r\n");
		final int H = Integer.parseInt(strs[0].split(" ")[0]);
		final int W = Integer.parseInt(strs[0].split(" ")[1]);
		String result = "";
		// 場面の左上から順番にSを検索する
		for (int y = 1; y <= H; y++) {
			for (int x = 1; x < W; x++) {
				if (strs[y].split("")[x].equals("S")) {
					if (check(strs, y, x, H, W)) {
						result = "YES";
					} else {
						result = "NO";
					}
				}
			}
		}

		model.addAttribute("ans", result);
		return "practice/result";
	}

	private static boolean check(String[] strs, int y, int x, int H, int W) {
		boolean result = false;
		// 現在の座標が端かどうかチェック
		if (y <= 0 || y >= H || x <= 0 || x >= W - 1) {
			return true;
		}
		//下方向に進むことができるか
		if (strs[y + 1].split("")[x].equals(".")) {
			String tmp = strs[y + 1];
			strs[y + 1] = changeStr(strs[y + 1], x);
			result = check(strs, y + 1, x, H, W);
			if (result) {
				return true;
			}
			strs[y + 1] = tmp;

		}
		//上方向に進むことができるか
		if (strs[y - 1].split("")[x].equals(".")) {
			String tmp = strs[y - 1];
			strs[y - 1] = changeStr(strs[y - 1], x);
			result = check(strs, y - 1, x, H, W);
			if (result) {
				return true;
			}
			strs[y - 1] = tmp;
		}
		//右方向に進むことができるか
		if (strs[y].split("")[x + 1].equals(".")) {
			String tmp = strs[y];
			strs[y] = changeStr(strs[y], x + 1);
			result = check(strs, y, x + 1, H, W);
			if (result) {
				return true;
			}
			strs[y] = tmp;
		}
		//左方向に進むことができるか
		if (strs[y].split("")[x - 1].equals(".")) {
			String tmp = strs[y];
			strs[y] = changeStr(strs[y], x - 1);
			result = check(strs, y, x - 1, H, W);
			if (result) {
				return true;
			}
			strs[y] = tmp;
		}
		return result;
	}

	private static String changeStr(String str, int x) {
		StringBuilder newString = new StringBuilder(str);
		newString.setCharAt(x, '*');
		str = newString.toString();
		return str;
	}

	/**
	 * Level6処理を行います。
	 * @param input 入力文字列
	 * @param model HTMLに値を渡すオブジェクト
	 * @return アルゴリズム道場画面へのパス
	 */
	@PostMapping("/level6")
	public String level6(@RequestParam(name = "level6") String input, Model model) {

		String[] strs = input.split("\r\n");
		final int H = Integer.parseInt(strs[0].split(" ")[0]);
		final int W = Integer.parseInt(strs[0].split(" ")[1]);
		String result = "";
		// 場面の左上から順番にSを検索する
		for (int y = 1; y <= H; y++) {
			for (int x = 1; x < W; x++) {
				if (strs[y].split("")[x].equals("S")) {
					if (check(strs, y, x, H, W)) {
						result = "YES";
					} else {
						result = "NO";
					}
				}
			}
		}

		model.addAttribute("ans", result);
		return "practice/result";
	}
}
