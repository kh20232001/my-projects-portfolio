package jp.ac.hcs.j2a129.profile;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.extern.log4j.Log4j2;

/**
 * 
 *プロフィールを行うためのコントローラクラスです。
 * <p>
 * このクラスはSpring MVCフレームワークでリクエストを処理し、プロフィール画面表示を担当します。
 * </p>
 * 
 * <p>入力チェックを実施し、正しい入力情報のみ処理します。<br>
 * クライアント側でも入力チェックを実施することを推奨します。
 * 
 * @author 春田和也
 */
@Log4j2
@Controller
public class ProfileController {

	/** プロフィールのロジッククラス */
	@Autowired
	private ProfileService profileService;

	/**
	* ログイン中のユーザに紐づく、プロフィール画面を表示します。
	* 
	* <p>本機能は、プロフィール画面を提供します。
	* 
	* @param principal ログイン中のユーザ情報を格納（null不可）
	* @param model Viewに値を渡すオブジェクト（null不可）
	* @return プロフィール画面へのパス(null不可)
	*/
	@GetMapping("/change")
	public String getProfile(Principal principal, Model model) {
		int count = profileService.profileResultCount(principal);
		if (count == 1) {
			ProfileData profileData = profileService.profileResult(principal);

			model.addAttribute("profile", profileData);
			return "profile/main";
		} else if (count >= 2) {
			profileService.delete(principal.getName());
		}
		return "profile/input";

	}

	/**
	 * プロフィール情報を保存するためのリクエストハンドラです。
	 * @param name ユーザ名
	 * @param kana ユーザ名のかな
	 * @param classNumber クラス番号
	 * @param url 画像のurl
	 * @param comment ユーザのコメント
	 * @param principal ログイン中のユーザ情報を格納
	 * @param model モデルオブジェクト
	 * @return getProfileのパス
	 */
	@PostMapping("/profile/input")
	public String postInputProfile(@RequestParam(name = "name") String name,
			@RequestParam(name = "kana") String kana, @RequestParam(name = "classNo") String classNumber,
			@RequestParam(name = "url") String url, @RequestParam(name = "comment") String comment, Principal principal,
			Model model) {

		boolean result = profileService.validate(name, kana, classNumber, url, comment);
		if (result) {
			return getProfile(principal, model);
		}
		boolean isSuccess = profileService.insert(principal.getName(), name, kana, classNumber, url, comment);
		if (isSuccess) {
			log.info("[" + principal.getName() + "] PROCESSING: 成功");
			model.addAttribute("message", "登録されました。");
		} else {
			log.info("[" + principal.getName() + "] PROCESSING: 失敗");
		}
		return getProfile(principal, model);
	}

	/**
	 * プロフィール情報を更新画面へ移動するクラスです。
	 * @param principal ログイン中のユーザ情報を格納
	 * @param model モデルオブジェクト
	 * @return プロフィールのアップデート画面パス
	 */
	@GetMapping("/profile/update")
	public String getUpdateProfile(Principal principal, Model model) {
		ProfileData profileData = profileService.profileResult(principal);
		System.out.println(profileData);
		model.addAttribute("profile", profileData);
		return "profile/update";
	}

	/**
	 * 
	 * プロフィール情報を更新するためのリクエストハンドラです。
	 * @param name ユーザ名
	 * @param kana ユーザ名のかな
	 * @param classNumber クラス番号
	 * @param url 画像のurl
	 * @param comment ユーザのコメント
	 * @param principal ログイン中のユーザ情報を格納
	 * @param model モデルオブジェクト
	 * @return getProfileのパス
	 */
	@PostMapping("/profile/update")
	public String postUpdateProfile(@RequestParam(name = "name") String name,
			@RequestParam(name = "kana") String kana, @RequestParam(name = "classNo") String classNumber,
			@RequestParam(name = "url") String url, @RequestParam(name = "comment") String comment, Principal principal,
			Model model) {

		boolean result = profileService.validate(name, kana, classNumber, url, comment);
		if (result) {
			return getUpdateProfile(principal, model);
		}
		boolean isSuccess = profileService.update(principal.getName(), name, kana, classNumber, url, comment);
		if (isSuccess) {
			log.info("[" + principal.getName() + "] PROCESSING: 成功");
			model.addAttribute("message", "登録されました。");
		} else {
			log.info("[" + principal.getName() + "] PROCESSING: 失敗");
		}
		return getProfile(principal, model);
	}
}