package jp.ac.hcs.j2a129.factorization;

import lombok.Data;

/**
 * 素数情報を格納するためのエンティティクラスです。
 * <p>
 * このクラスは素数のリストを保持します。
 * </p>
 * @author 春田和也
 */
@Data
public class FactorizationData {

	/** 素数がすべて書いてある文字列 */
	private String ans;
}