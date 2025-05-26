package jp.ac.hcs.j2a129.task;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowCallbackHandler;

import jp.ac.hcs.j2a129.WebConfig;

/** 
 * 
 * タスク管理機能のCSV出力機能を実現します。
 * 
 * <p>ファイル出力先を事前に設定することで、所定のパスへCSVを出力します。
 * 
 * @author 春田和也
 * 
 */
public class TaskRowCallbackHandler implements RowCallbackHandler {

	@Override
	public void processRow(ResultSet rs) throws SQLException {

		// フォルダへアクセス
		File directory = new File(WebConfig.OUTPUT_PATH);
		if (!directory.exists()) {
			// フォルダが存在しない場合は作成する
			directory.mkdir();
		}

		// ファイルを作成
		File file = new File(WebConfig.OUTPUT_PATH + WebConfig.FILENAME_TASK_CSV);

		// ファイルへ書き込む
		try (FileWriter fw = new FileWriter(file.getAbsoluteFile());
				BufferedWriter bw = new BufferedWriter(fw)) {

			String header = "id" + ","
					+ "user_id" + ","
					+ "title" + ","
					+ "limit_day"
					+ "priority";

			bw.write(header);
			bw.newLine();

			do {
				// カンマ区切りでデータを並べた文字列を作成
				String content = rs.getInt("id") + ","
						+ rs.getString("user_id") + ","
						+ rs.getString("title") + ","
						+ rs.getDate("limit_day") + ","
						+ rs.getString("priority");
				bw.write(content);
				bw.newLine();
			} while (rs.next());

		} catch (IOException e) {
			// ファイル書き込み時で発生したエラー
			throw new SQLException(e);
		}

	}
}