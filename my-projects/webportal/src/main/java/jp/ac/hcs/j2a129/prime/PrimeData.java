package jp.ac.hcs.j2a129.prime;

import java.util.List;

import lombok.Data;

/**
 * 素数情報を格納するためのエンティティクラスです。
 * <p>
 * このクラスは素数のリストを保持します。
 * </p>
 * @author 春田和也
 */
@Data
public class PrimeData {

	/** 素数のリスト */
	private List<Integer> ans;
}