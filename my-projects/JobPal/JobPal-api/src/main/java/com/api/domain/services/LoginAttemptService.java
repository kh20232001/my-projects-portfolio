package com.api.domain.services;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

/**
* ログイン試行管理サービスクラスです。
*
* <p>
* 以下の機能を提供します。
* <ul>
* <li>ログイン試行回数のカウントアップ</li>
* <li>ログイン試行回数のリセット</li>
* <li>ユーザのロック状態の判定</li>
* <li>現在の試行回数の取得</li>
* </ul>
* </p>
*
* <p>
* キャッシュを利用してログイン試行回数を一時的に保存し、一定時間後に自動的にクリアします。
* キャッシュの有効期限は設定に基づいて調整されます。
* </p>
*
*
*/
@Service
public class LoginAttemptService {
	// 最大試行回数
	private static final int MAX_ATTEMPT = 5;
	// ロック時間
	private static final int LOCK_DURATION_MINUTES = 1;
	// Guavaキャッシュでログイン試行回数を管理
	private final LoadingCache<String, Integer> attemptsCache;

	/**
	 * ログイン試行サービスのコンストラクタ。
	 *
	 * <p>
	 * ユーザのログイン試行回数をキャッシュするための仕組みを初期化します。<br>
	 * キャッシュは一定時間後に自動的にクリアされ、デフォルト値は0です。
	 * </p>
	 *
	 * <p>
	 * キャッシュの有効期限は {@link #LOCK_DURATION_MINUTES} で指定されます。
	 * </p>
	 */
	public LoginAttemptService() {
		this.attemptsCache = CacheBuilder.newBuilder()
				.expireAfterWrite(LOCK_DURATION_MINUTES, TimeUnit.MINUTES) // キャッシュの有効期限
				.build(new CacheLoader<>() {
					@Override
					public Integer load(String key) {
						// 初期値として0を返す
						return 0;
					}
				});
	}

	/**
	 * 指定されたユーザIDのログイン試行回数をリセットします。
	 *
	 * <p>
	 * キャッシュから指定されたユーザIDに関連付けられた試行回数のデータを無効化します。<br>
	 * この操作により、次回の試行時に初期値（通常は0）が使用されます。
	 * </p>
	 *
	 * @param userId ログイン試行回数をリセットする対象のユーザID
	 */
	public void resetAttempts(String userId) {
		attemptsCache.invalidate(userId);
	}

	/**
	 * 指定されたユーザIDのログイン失敗回数をカウントアップします。
	 *
	 * <p>
	 * キャッシュ内に保存されている試行回数を取得し、1を加算した値を再度キャッシュに保存します。<br>
	 * もしキャッシュにデータが存在しない場合、初期値（通常は0）からカウントを開始します。
	 * </p>
	 *
	 * @param userId ログイン失敗回数を増加させる対象のユーザID
	 */
	public void loginFailed(String userId) {
		int attempts = 0;
		try {
			attempts = attemptsCache.get(userId);
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		attempts++;
		attemptsCache.put(userId, attempts);
	}

	/**
	 * 指定されたユーザIDがロックされているかを確認します。
	 *
	 * <p>
	 * ユーザIDに紐づくログイン試行回数が最大許容回数を超えている場合、ロック状態と判断します。<br>
	 * キャッシュの取得に失敗した場合、ロックされていない（false）として扱います。
	 * </p>
	 *
	 * @param userId ロック状態を確認する対象のユーザID
	 * @return ロックされている場合はtrue、そうでない場合はfalse
	 */
	public Boolean isBlocked(String userId) {
		try {
			return attemptsCache.get(userId) >= MAX_ATTEMPT;
		} catch (ExecutionException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 指定されたユーザIDに紐づく現在のログイン試行回数を取得します。
	 *
	 * <p>
	 * キャッシュに保存されている試行回数を返却します。<br>
	 * キャッシュの取得に失敗した場合、試行回数は0として扱います。
	 * </p>
	 *
	 * @param userId 試行回数を取得する対象のユーザID
	 * @return 現在の試行回数。キャッシュの取得に失敗した場合は0。
	 */
	public int getAttemptCount(String userId) {
		try {
			return attemptsCache.get(userId);
		} catch (ExecutionException e) {
			e.printStackTrace();
			return 0;
		}
	}
}
