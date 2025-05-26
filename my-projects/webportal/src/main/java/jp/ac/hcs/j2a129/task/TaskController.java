package jp.ac.hcs.j2a129.task;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
public class TaskController {

	/** タスク管理の業務ロジッククラス */
	@Autowired
	private TaskService taskService;

	/** ログ用 GET(/task) */
	private static final String GET_TASK = "GET:/task";
	/** ログ用 POST(/task) */
	private static final String POST_TASK_INSERT = "POST:/task/insert";

	/**
	 * ログイン中のユーザに紐づく、タスク一覧画面を表示します。
	 * 
	 * <p>本機能は、タスク管理機能の一覧機能を提供します。
	 * 
	 * @param principal ログイン中のユーザ情報を格納（null不可）
	 * @param model Viewに値を渡すオブジェクト（null不可）
	 * @return タスク一覧画面へのパス(null不可)
	 */
	@GetMapping("/task")
	public String getTaskList(Principal principal, Model model) {
		//		TaskEntity taskEntity = taskService.selectAll(principal.getName());
		int now = 1;
		int pagenation = 3;
		TaskEntity taskEntity = taskService.selectPageNethion(principal.getName(), pagenation, now);

		log.info("[" + principal.getName() + "] PEOCESSING：" + GET_TASK);
		model.addAttribute("taskEntity", taskEntity);
		model.addAttribute("pagenation", pagenation);
		return "task/list";
	}

	@GetMapping("/task/pagenation")
	public String getTaskList(@RequestParam(name = "postCount") String pagenation,
			@RequestParam(name = "nowCount") String now, Principal principal, Model model) {
		if (Integer.parseInt(now) - 1 < 0) {
			now = String.valueOf(Integer.parseInt(now) + 1);
		}
		TaskEntity taskEntity = taskService.selectPageNethion(principal.getName(), Integer.parseInt(pagenation),
				Integer.parseInt(now));

		log.info("[" + principal.getName() + "] PEOCESSING：" + GET_TASK);
		model.addAttribute("taskEntity", taskEntity);
		model.addAttribute("pagenation", pagenation);
		return "task/list";
	}

	/**
	 * 入力されたタスクをDBへ登録します。
	 * 
	 * <p>本機能は、タスク管理機能の登録機能を提供します。
	 * 
	 * @param title タイトルの文字列を格納(null 不可)
	 * @param limit 期限の文字列を格納(null 不可)
	 * @param princial ログイン中のユーザ情報を格納(null不可)
	 * @param model Viewに値を渡すオブジェクト(null不可)
	 * @return タスク一覧画面へのパス(null不可)
	 */
	@PostMapping("/task/insert")
	public String insertTask(@RequestParam(name = "title", required = false) String title,
			@RequestParam(name = "limit", required = false) String limit,
			@RequestParam(name = "priority", required = false) String priority,
			Principal principal, Model model) {
		limit = limit.replaceAll("T", " ");
		System.out.println(limit);
		if (taskService.validate(title, limit)) {
			model.addAttribute("errorMessage", "入力値に不備があります");
			return getTaskList(principal, model);
		}
		boolean isSuccess = taskService.insert(principal.getName(), title, limit, priority);
		if (isSuccess) {
			log.info("[" + principal.getName() + "] PROCESSING: " + POST_TASK_INSERT + "成功");
			model.addAttribute("message", "登録されました。");
		} else {
			log.info("[" + principal.getName() + "] PROCESSING: " + POST_TASK_INSERT + "失敗");
		}
		return getTaskList(principal, model);
	}

	/**
	 * 指定されたタスクIDの状態を完了に変更します。
	 * 
	 * <p>本機能は、タスク管理機能の登録機能を提供します。
	 * 
	 * @param id タスクIDの文字列を格納(null 不可)
	 * @param princial ログイン中のユーザ情報を格納(null不可)
	 * @param model Viewに値を渡すオブジェクト(null不可)
	 * @return タスク一覧画面へのパス(null不可) 
	 */
	@PostMapping("/task/complate")
	public String complateTask(@RequestParam(name = "id") String id,
			Principal principal, Model model) {
		boolean isSuccess = taskService.complate(id);
		if (isSuccess) {
			log.info("[" + principal.getName() + "] PROCESSING: " + POST_TASK_INSERT + "成功");
			model.addAttribute("message", "更新されました。");
		} else {
			log.info("[" + principal.getName() + "] PROCESSING: " + POST_TASK_INSERT + "失敗");
		}
		return getTaskList(principal, model);
	}

	/**
	 * 指定されたタスクIDの状態をDBから変更します。
	 * 
	 * <p>本機能は、タスク管理機能の登録機能を提供します。
	 * 
	 * @param id タスクIDの文字列を格納(null 不可)
	 * @param princial ログイン中のユーザ情報を格納(null不可)
	 * @param model Viewに値を渡すオブジェクト(null不可)
	 * @return タスク一覧画面へのパス(null不可) 
	 */
	@PostMapping("/task/delete")
	public String deleteTask(@RequestParam(name = "id") String id,
			Principal principal, Model model) {
		boolean isSuccess = taskService.delete(id);
		if (isSuccess) {
			log.info("[" + principal.getName() + "] PROCESSING: " + POST_TASK_INSERT + "成功");
			model.addAttribute("message", "削除されました。");
		} else {
			log.info("[" + principal.getName() + "] PROCESSING: " + POST_TASK_INSERT + "失敗");
		}
		return getTaskList(principal, model);
	}

	/**
	 * ログイン中のユーザに紐づく、タスク全件CSVファイルに出力します。
	 * 
	 * <p>本機能は、タスク管理機能のCSV出力機能を提供します。
	 * 
	 * @param princial ログイン中のユーザ情報を格納(null不可)
	 * @param model Viewに値を渡すオブジェクト(null不可)
	 * @return CSVファイル 
	 */
	@PostMapping("/task/csv")
	public ResponseEntity<byte[]> getTaskCsv(
			Principal principal, Model model) {
		// CSVファイルをサーバ上に作成
		ResponseEntity<byte[]> csv = taskService.taskListCsvOut(principal.getName());

		// CSVファイルを端末へ送信
		return csv;
	}

}
