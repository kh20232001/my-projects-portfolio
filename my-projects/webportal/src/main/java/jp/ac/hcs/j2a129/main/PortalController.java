package jp.ac.hcs.j2a129.main;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *インデックスを表示するためのコントローラクラスです。
 * @author 春田和也
 */
@Controller
public class PortalController {
	/**
	 * インデックスを表示する。
	 * @return インデックスを表示するパス
	 */
	@GetMapping("/")
	public String index() {
		return "index";
	}
}
