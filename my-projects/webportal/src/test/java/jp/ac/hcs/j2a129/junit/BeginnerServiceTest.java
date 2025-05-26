package jp.ac.hcs.j2a129.junit;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
class BeginnerServiceTest {
	// テスト対象のクラスを定義
	@Autowired
	private BeginnerService target;

	@Test
	void addメソッドに1と2を設定して3を出力する() {
		// 1.ready
		int x = 1;
		int y = 2;
		// 以下は予測値
		int expected = 3;

		// 2.do
		int result = target.add(x, y);

		// 3.check
		assertEquals(expected, result);

		// 4.log
		log.info("結果：" + result);
	}

	@Test
	void evenOddメソッドに10を設定して偶数と出力する() {
		// 1.ready
		int x = 10;
		// 以下は予測値
		String expected = "偶数";

		// 2.do
		String result = target.evenOdd(x);

		// 3.check
		assertEquals(expected, result);

		// 4.log
		log.info("結果：" + result);
	}

	@Test
	void evenOddメソッドに9を設定して奇数と出力する() {
		// 1.ready
		int x = 9;
		// 以下は予測値
		String expected = "奇数";

		// 2.do
		String result = target.evenOdd(x);

		// 3.check
		assertEquals(expected, result);

		// 4.log
		log.info("結果：" + result);
	}

	@Test
	void sumメソッドに10を設定して55と出力する() {
		// 1.ready
		int x = 10;
		// 以下は予測値
		int expected = 55;

		// 2.do
		int result = target.sum(x);

		// 3.check
		assertEquals(expected, result);

		// 4.log
		log.info("結果：" + result);
	}

	@Test
	void powerメソッドに5と2を設定して25と出力する() {
		// 1.ready
		int x = 5;
		int y = 2;
		// 以下は予測値
		double expected = 25.0;

		// 2.do
		double result = target.power(x, y);

		// 3.check
		assertEquals(expected, result);

		// 4.log
		log.info("結果：" + result);

	}

	@Test
	void powerメソッドに0を底0を指数として例外をスローする() {
		// 1.ready
		int x = 0;
		int y = 0;

		// 2.do ＆ 3.check
		assertThrows(IllegalArgumentException.class, () -> target.power(x, y));

		// 4.log
		log.info("結果：IllegalArgumentException");
	}

	@Test
	void powerメソッドに1を底0を指数として例外をスローする() {
		// 1.ready
		int x = 1;
		int y = 0;

		// 2.do ＆ 3.check
		assertThrows(IllegalArgumentException.class, () -> target.power(x, y));

		// 4.log
		log.info("結果：IllegalArgumentException");
	}

	@Test
	void powerメソッドに0を底1を指数として例外をスローする() {
		// 1.ready
		int x = 0;
		int y = 1;

		// 2.do ＆ 3.check
		assertThrows(IllegalArgumentException.class, () -> target.power(x, y));

		// 4.log
		log.info("結果：IllegalArgumentException");
	}

}
