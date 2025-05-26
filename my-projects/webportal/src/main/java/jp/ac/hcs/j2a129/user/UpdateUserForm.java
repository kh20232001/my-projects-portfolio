package jp.ac.hcs.j2a129.user;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * ユーザ作成画面の入力値を保持するクラスです。
 * 
 * <p>各項目のデータ仕様は基本設計書w参照してください。
 * 
 * @author 春田和也
 */
@Data
public class UpdateUserForm {

	/** ユーザID（メールアドレス）*/
	@NotBlank(message = "{require_check}")
	@Email(message = "{email_check}")
	private String userId;
	/** 名前 */
	@Length(min = 2, max = 50, message = "{length_check}")
	private String userName;

	/** パスワード(平文) */
	@Pattern(regexp = "^$|.{6,}", message = "6文字以上で入力してください")
	private String password;

	/** 権限 */
	@Pattern(regexp = "^(ROLE_ADMIN|ROLE_TOP|ROLE_GENERAL)$", message = "項目を選択してください")
	private String role;

	/** 権限 */
	@Pattern(regexp = "^(true|false)$", message = "項目を選択してください")
	private String enabled;
}
