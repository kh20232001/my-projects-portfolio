package jp.ac.hcs.j2a129.user;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * ユーザサービスクラスです。
 * ユーザー関連の操作を提供します。
 * @author 春田和也
 */

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	/**
	 * ユーザーリストを取得するメソッドです。
	 * @return ユーザーリストのエンティティ
	 */
	public UserEntity getUserList() {
		// データベースからすべてのユーザーデータを取得
		List<Map<String, Object>> resultList = userRepository.selectAll();
		// 取得したユーザをエンティティにマッピング
		UserEntity userEntity = mappingSelectResult(resultList);
		//ユーザーリストのエンティティを返す
		return userEntity;
	}

	public boolean insertOne(UserForm userForm) {
		UserData userData = refillToData(userForm);
		try {
			userRepository.insert(userData);
		} catch (SQLException e) {
			return false;
		}
		return true;
	}

	private UserEntity mappingSelectResult(List<Map<String, Object>> resultList) {
		UserEntity entity = new UserEntity();

		for (Map<String, Object> map : resultList) {
			UserData data = new UserData();
			data.setUserId((String) map.get("user_id"));
			data.setUserName((String) map.get("user_name"));
			data.setRole((String) map.get("role"));
			data.setEnabled((boolean) map.get("enabled"));
			entity.getUserList().add(data);
		}
		return entity;
	}

	private UserData refillToData(UserForm userForm) {
		UserData userData = new UserData();
		userData.setUserId(userForm.getUserId());
		// パスワードは暗号化する
		final String encryptedPassword = passwordEncoder.encode(userForm.getPassword());
		userData.setPassword(encryptedPassword);
		userData.setUserName(userForm.getUserName());
		userData.setRole(userForm.getRole());
		// ユーザ作成時は、デフォルト有効とする
		userData.setEnabled(true);
		return userData;
	}

	/**
	 * ユーザデータを取得し、指定されたユーザーフォームに更新します。
	 * ユーザーIDと更新するユーザフォームを引数として受け取ります。
	 * @param userId ユーザーデータを取得するユーザーID
	 * @param updateUserForm　更新するユーザーフォーム
	 * @return
	 */
	public boolean getUser(UpdateUserForm updateUserForm) {
		// データベースから指定されたユーザーIDのユーザーデータを取得
		List<Map<String, Object>> resultList = userRepository.selectOne(updateUserForm.getUserId());
		// 取得したユーザーデータを指定されたユーザーフォームにマッピング
		boolean result = mappingUpdateResult(resultList, updateUserForm);
		return result;
	}

	private boolean mappingUpdateResult(List<Map<String, Object>> resultList, UpdateUserForm updateUserForm) {
		// ユーザ情報が空であるかをフラグで管理
		boolean isEmpty = true;

		for (Map<String, Object> map : resultList) {
			updateUserForm.setUserId((String) map.get("user_id"));
			updateUserForm.setUserName((String) map.get("user_name"));
			updateUserForm.setRole((String) map.get("role"));
			updateUserForm.setEnabled(String.valueOf(map.get("enabled")));
			isEmpty = false;
		}
		return isEmpty;
	}

	public boolean updateOne(UpdateUserForm userDataForm) {
		// ユーザーフォームのデータをユーザーデータに変換
		UserData userData = refillToData(userDataForm);
		try {
			// パスワードが空であるかどうかで条件分岐
			if (userData.getPassword().isBlank()) {
				// パスワードが空の場合は、パスワードを更新せずにユーザーデータを更新
			} else {
				// パスワードが空でない場合は、パスワードを含めてユーザーデータを更新
				userRepository.updateWithPassword(userData);
			}
		} catch (SQLException e) {
			// 更新時にエラーが発生した場合はfalseを返す
			return false;
		}
		//更新が成功した場合はtrueを返す
		return true;
	}

	private UserData refillToData(UpdateUserForm updateUserForm) {
		UserData userData = new UserData();
		userData.setUserId(updateUserForm.getUserId());
		// パスワードは暗号化する
		final String encryptedPassword = passwordEncoder.encode(updateUserForm.getPassword());
		userData.setPassword(encryptedPassword);
		userData.setUserName(updateUserForm.getUserName());
		userData.setRole(updateUserForm.getRole());
		// boolean型へ変換
		boolean isEnabled = Boolean.valueOf(updateUserForm.getEnabled());
		userData.setEnabled(isEnabled);

		return userData;
	}

	public boolean deleteOne(UpdateUserForm updateUserForm) {
		try {
			String userId = updateUserForm.getUserId();
			userRepository.delete(userId);
		} catch (SQLException e) {
			// 更新時にエラーが発生した場合はfalseを返す
			return false;
		}
		//更新が成功した場合はtrueを返す
		return true;
	}

	public boolean deleteSelect(DeleteUserForm deleteUserForm) {
		List<String> users = new ArrayList<String>();
		users = Arrays.asList(deleteUserForm.getUsers().split(","));

		try {

			userRepository.deleteSelect(users);
		} catch (SQLException e) {
			// 更新時にエラーが発生した場合はfalseを返す
			return false;
		}
		//更新が成功した場合はtrueを返す
		return true;
	}

}
