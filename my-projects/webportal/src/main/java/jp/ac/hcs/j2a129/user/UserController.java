package jp.ac.hcs.j2a129.user;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * ユーザ管理機能を表す。
 * 
 * <p>本機能は、ユーザ管理に関わるCEUDを実現します。
 * 
 * <p>入力チェックを実施し、正しいデータ項目のみ処理します。
 * クライアント側でも入力チェックを実施することを推奨します。
 * 
 * @author 春田和也
 */

@Controller
public class UserController {

	@Autowired
	private UserService userService;

	/**
	 * ユーザ一覧画面を表示します。
	 * 
	 * <p>本機能は、ユーザ管理機能の一覧機能を提供します。
	 * <p><strong>この機能は管理者もしくは上位ロールのユーザのみが利用できます</strong>
	 * 
	 * @param model {@code Model} オブジェクト
	 * @return ユーザーリスト表示用ビュー名
	 */
	@GetMapping("user/list")
	public String getUserList(Model model) {

		UserEntity userEntity = userService.getUserList();
		model.addAttribute("result", userEntity);

		return "user/list";
	}

	/**
	 * 【管理者】ユーザ追加画面を表示します。
	 * 
	 * <p><strong>この機能は管理者ロールのユーザのみが利用できます</strong>
	 * 
	 * @param userForm 入力値（null不可）
	 * @param model Viewに値を渡すオブジェクト（null不可）
	 * @return ユーザ追加画面（null不可）
	 */
	@GetMapping("/user/insert")
	public String getUserCreate(UserForm userForm, Model model) {
		// 前回の入力内容を設定
		model.addAttribute("userForm", userForm);

		return "user/insert";
	}

	@PostMapping("/user/insert")
	public String createUser(@Validated UserForm form, BindingResult bindingResult, Principal principal,
			Model model) {

		// 入力チェックでエラーの場合、目の画面に戻る
		if (bindingResult.hasErrors()) {
			return getUserCreate(form, model);
		}

		boolean isSucess = userService.insertOne(form);
		if (isSucess) {
			model.addAttribute("message", "ユーザを登録しました");
		} else {
			model.addAttribute("errorMessage", "ユーザ登録に失敗しました。操作をやり直してください。");
		}

		return getUserList(model);
	}

	/**
	 *【管理者】ユーザ詳細画面を表示します。
	 * @param updateUserForm ユーザー情報の更新フォーム（null不可）
	 * @param model {`code Model}オブジェクト（null不可）
	 * @return ユーザ追加画面（null不可）
	 */
	@GetMapping("/user/detail")
	public String getUserDetail(UpdateUserForm updateUserForm, Model model) {
		boolean isEmpty = userService.getUser(updateUserForm);
		if (isEmpty) {
			// ユーザ情報の取得ができない場合は、一覧画面に戻る
			model.addAttribute("errorMessage", "情報の取得に失敗しました。操作をやり直してください。");
			return getUserList(model);
		}
		model.addAttribute("UpdateUserForm", updateUserForm);

		return "user/detail";
	}

	/**
	 *【管理者】ユーザ詳細画面を表示します。
	 * @param updateUserForm ユーザー情報の削除のフォーム（null不可）
	 * @param model {@code Model}オブジェクト（null不可）
	 * @return ユーザ追加画面（null不可）
	 */
	@PostMapping("/user/update")
	public String updateUser(@Validated UpdateUserForm updateUserForm, BindingResult bindingResult, Model model) {

		// 入力チェックでエラーの場合、前の画面に戻る
		if (bindingResult.hasErrors()) {
			return getUserDetail(updateUserForm, model);
		}

		boolean isSuccess = userService.updateOne(updateUserForm);
		if (isSuccess) {
			model.addAttribute("message", "ユーザを更新しました");
		} else {
			// ユーザ情報の取得ができない場合は、一覧画面に戻る
			model.addAttribute("errorMessage", "ユーザ更新に失敗しました。操作をやり直してください。");
		}

		return getUserList(model);
	}

	/**
	 *【管理者】ユーザ詳細画面を表示します。
	 * @param updateUserForm ユーザー情報の更新フォーム（null不可）
	 * @param model {@code Model}オブジェクト（null不可）
	 * @return ユーザ追加画面（null不可）
	 */
	@PostMapping("/user/delete")
	public String deleteUser(UpdateUserForm updateUserForm, Model model) {

		boolean isSuccess = userService.deleteOne(updateUserForm);
		if (isSuccess) {
			model.addAttribute("message", "ユーザを削除しました");
		} else {
			// ユーザ情報の取得ができない場合は、一覧画面に戻る
			model.addAttribute("errorMessage", "ユーザ削除に失敗しました。操作をやり直してください。");
		}

		return getUserList(model);
	}

	@GetMapping("user/delete")
	public String getUserDeleteList(Model model) {

		UserEntity userEntity = userService.getUserList();
		model.addAttribute("userEntity", userEntity);

		return "user/delete";
	}

	@PostMapping("/user/delete/bulk")
	public String deleteBulkUser(DeleteUserForm deleteUserForm, Model model) {
		System.out.println(deleteUserForm.getUsers());
		boolean isSuccess = userService.deleteSelect(deleteUserForm);
		if (isSuccess) {
			model.addAttribute("message", "選択したユーザーを削除しました。");
		} else {
			model.addAttribute("errorMessage", "ユーザー一括削除に失敗しました。操作をやり直してください。");
		}
		return getUserList(model);
	}

}
