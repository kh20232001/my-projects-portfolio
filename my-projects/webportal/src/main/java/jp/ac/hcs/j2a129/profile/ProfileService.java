package jp.ac.hcs.j2a129.profile;

import java.security.Principal;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class ProfileService {

	@Autowired
	private ProfileRepository profileRepository;

	public boolean validate(String name, String kana, String classNo, String url, String comment) {
		if (name.length() <= 0 || name.length() > 50) {
			return true;
		}
		if (kana.length() <= 0 || kana.length() > 50) {
			return true;
		}
		if (classNo.length() <= 0 || classNo.length() > 10) {
			return true;
		}
		if (url.length() <= 0 || url.length() > 1000) {
			return true;
		}
		if (comment.length() <= 0 || comment.length() > 50) {
			return true;
		}
		return false;
	}

	public int profileResultCount(Principal principal) {
		int resultCount = profileRepository.findProfileCount(principal.getName());
		return resultCount;
	}

	public ProfileData profileResult(Principal principal) {
		List<Map<String, Object>> resultList = profileRepository.findProfile(principal.getName());
		ProfileData profileData = mappingResult(resultList);
		return profileData;
	}

	private ProfileData mappingResult(List<Map<String, Object>> resultList) {

		ProfileData data = new ProfileData();
		for (Map<String, Object> map : resultList) {
			data.setId((Integer) map.get("p_id"));
			data.setUserId((String) map.get("user_id"));
			data.setUserName((String) map.get("user_name"));
			data.setUserKana((String) map.get("user_kana"));
			data.setClassNumber((String) map.get("class_number"));
			data.setImage((String) map.get("image"));
			data.setCommentText((String) map.get("comment_text"));
		}
		return data;
	}

	public boolean insert(String userId, String name, String kana, String classNumber, String url, String comment) {
		//profileData型へ変換する
		ProfileData profileData = refillToData(userId, name, kana, classNumber, url, comment);

		try {
			profileRepository.save(profileData);
		} catch (SQLException e) {
			return false;
		}
		return true;
	}

	private ProfileData refillToData(String userId, String name, String kana, String classNumber, String url,
			String comment) {
		ProfileData profileData = new ProfileData();
		profileData.setUserId(userId);
		profileData.setUserName(name);
		profileData.setUserKana(kana);
		profileData.setClassNumber(classNumber);
		profileData.setImage(url);
		profileData.setCommentText(comment);
		;
		return profileData;
	}

	public boolean update(String userId, String name, String kana, String classNumber, String url, String comment) {
		//profileData型へ変換する
		ProfileData profileData = refillToData(userId, name, kana, classNumber, url, comment);

		try {
			profileRepository.update(profileData);
		} catch (SQLException e) {
			return false;
		}
		return true;
	}

	public boolean delete(String userId) {

		try {
			profileRepository.delete(userId);
		} catch (SQLException e) {
			return false;
		}
		return true;
	}
}
