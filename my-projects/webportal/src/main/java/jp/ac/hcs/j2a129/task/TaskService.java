package jp.ac.hcs.j2a129.task;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.ac.hcs.j2a129.WebConfig;

@Transactional
@Service
public class TaskService {

	@Autowired
	private TaskRepository taskRepository;

	/**
	 * 入力項目を力チェックをして返すメソッドです。
	 * @param title タイトル
	 * @param limit 時間制限
	 * @return 入力チェックをした結果を返却する（false：正常値）
	 */
	public boolean validate(String title, String limit) {

		try {
			// 入力されたパラメータをDouble型で取得する

			// titleの入力値の範囲チェック
			if (title.length() > 50 || title.length() <= 0 || title.equals("")) {
				throw new Exception();
			}
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			// titleの入力値の範囲チェック
			format.parse(limit);
			if (limit.equals("")) {
				throw new Exception();
			}
		} catch (Exception c) {
			// エラーがあった場合は、trueを返す。
			return true;
		}

		return false;
	}

	/**
	 * ユーザIDに合致するタスク一覧を取得します。
	 * 
	 * <p> DBエラーが発生した場合は、空のタスク一覧を設定して呼び出しもヘ返却します。
	 * 
	 * @param userId ユーザID(null不可)
	 * @return タスク一覧 
	 */
	//	public TaskEntity selectAll(String userId) {
	//
	//		List<Map<String, Object>> resultSet = taskRepository.findAll(userId);
	//		TaskEntity taskEntity = mappingResult(resultSet);
	//		return taskEntity;
	//	}
	//
	//	private TaskEntity mappingResult(List<Map<String, Object>> resultList) {
	//		TaskEntity entity = new TaskEntity();
	//
	//		for (Map<String, Object> map : resultList) {
	//			TaskData data = new TaskData();
	//			data.setId((Integer) map.get("id"));
	//			data.setUserId((String) map.get("user_id"));
	//			data.setTitle((String) map.get("title"));
	//			data.setLimitDay((Date) map.get("limit_day"));
	//			if (!((String) map.get("priority")).isEmpty()) {
	//				if (((String) map.get("priority")).equals("HIGH")) {
	//					data.setPriority("高");
	//				} else if (((String) map.get("priority")).equals("LOW")) {
	//					data.setPriority("低");
	//				} else {
	//					data.setPriority("中");
	//				}
	//			}
	//			if (!((String) map.get("complate")).isEmpty()) {
	//				data.setComplate((Boolean) map.get("complate"));
	//			}
	//			entity.getTaskList().add(data);
	//		}
	//		return entity;
	//	}

	/**
	 * ユーザIDに合致するタスク一覧をページネーションで取得します。
	 * 
	 * <p> DBエラーが発生した場合は、空のタスク一覧を設定して呼び出しもヘ返却します。
	 * 
	 * @param userId ユーザID(null不可)
	 * @return タスク一覧 
	 */
	public TaskEntity selectPageNethion(String userId, int pagenation, int now) {

		List<Map<String, Object>> resultSet = taskRepository.findAll(userId);
		TaskEntity taskEntity = mappingResultPageNethion(resultSet, pagenation, now);
		return taskEntity;
	}

	private TaskEntity mappingResultPageNethion(List<Map<String, Object>> resultList, int pagenation, int now) {
		TaskEntity entity = new TaskEntity();
		int start = (now - 1) * pagenation;
		int finish = (now * pagenation);

		if (finish > resultList.size()) {
			finish = resultList.size();
			if (finish % pagenation == 0) {
				start = finish - pagenation;
				now = start % pagenation + 1;
			} else {
				start = finish - (finish % pagenation);
				now = Math.floorDiv(start, pagenation) + 1;
			}

		}
		if (start < 0) {
			start = 0;
		}

		for (int i = start; i < finish; i++) {
			Map<String, Object> map = resultList.get(i);
			TaskData data = new TaskData();
			data.setId((Integer) map.get("id"));
			data.setUserId((String) map.get("user_id"));
			data.setTitle((String) map.get("title"));
			data.setLimitDay((Date) map.get("limit_day"));
			if (((String) map.get("priority")).equals("HIGH")) {
				data.setPriority("高");
			} else if (((String) map.get("priority")).equals("LOW")) {
				data.setPriority("低");
			} else {
				data.setPriority("中");
			}
			data.setComplate((Boolean) map.get("complate"));

			entity.getTaskList().add(data);
		}
		entity.setNow(now);
		return entity;
	}

	/**
	 * タスクを保存します。
	 * 
	 * <p> DBエラーが発生した場合は、呼び出しもとに失敗の通知を行います。
	 *@param userId ユーザID(null不可)
	 *@param title タイトル(null不可)
	 *@param limitday 期限日(null不可)
	 *@return 成功可否
	 */
	public boolean insert(String userId, String title, String limitday, String priority) {
		// TaskData型へ変換する
		TaskData taskData = refillToData(userId, title, limitday, priority);
		try {
			taskRepository.save(taskData);
		} catch (SQLException e) {
			return false;
		}
		return true;
	}

	private TaskData refillToData(String userId, String title, String limitDay, String priority) {
		TaskData taskData = new TaskData();
		taskData.setUserId(userId);
		taskData.setTitle(title);
		taskData.setPriority(priority);
		// 初期値は未完了状態とする
		taskData.setComplate(false);

		// String型からDate型へ変換する
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			taskData.setLimitDay((Date) format.parse(limitDay));

		} catch (ParseException e) {
			throw new IllegalArgumentException();
		}
		return taskData;

	}

	/**
	 * タスクを完了状態にします。
	 * 
	 * <p>
	 * DBエラーが発生した場合は、呼び出しもとに失敗の通知を行います。
	 * 
	 * @param id タスクID
	 * @return 成功可否
	 */
	public boolean complate(String id) {
		// TaskData型へ変換する
		try {
			taskRepository.update(Integer.parseInt(id));
		} catch (SQLException e) {
			return false;
		}
		return true;
	}

	/**
	 * タスクを削除します。
	 * 
	 * <p>
	 * DBエラーが発生した場合は、呼び出しもとに失敗の通知を行います。
	 * 
	 * @param id タスクID
	 * @return 成功可否
	 */
	public boolean delete(String id) {
		// TaskData型へ変換する
		try {
			taskRepository.delete(Integer.parseInt(id));
		} catch (SQLException e) {
			return false;
		}
		return true;
	}

	public ResponseEntity<byte[]> taskListCsvOut(String userId) {

		// サーバに一時的にCSVファイルを生成
		taskRepository.fileOut(userId);

		try {
			// CSVファイルをサーバから読み込み
			byte[] bytes = getFile(WebConfig.OUTPUT_PATH + WebConfig.FILENAME_TASK_CSV);
			// CSVファイルのダウンロード用ヘッダー情報設定
			HttpHeaders header = generateHeader();
			return new ResponseEntity<byte[]>(bytes, header, HttpStatus.OK);
		} catch (IOException e) {
			return new ResponseEntity<byte[]>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private HttpHeaders generateHeader() {
		HttpHeaders header = new HttpHeaders();
		header.add("Contexnt-Type", "text/csv; charset=UTF-8");
		header.setContentDispositionFormData("filename", WebConfig.FILENAME_TASK_CSV);
		return header;
	}

	private byte[] getFile(String fileName) throws IOException {
		FileSystem fs = FileSystems.getDefault();
		Path p = fs.getPath(fileName);
		byte[] bytes = Files.readAllBytes(p);
		return bytes;
	}

}
