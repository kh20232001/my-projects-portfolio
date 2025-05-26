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
class ExpertServiceTest {
	@Autowired
	private ExpertService target;

	@Test
	void testFactorial() {
		// 1.ready
		int x = 8;
		// 以下は予測値
		int expected = 40320;

		// 2.do
		int result = target.factorial(x);

		// 3.check
		assertEquals(expected, result);

		// 4.log
		log.info("結果：" + result);
	}

	@Test
	void testMax() {
		// 1.ready
		int[] x = { 75, 88, 82 };

		// 以下は予測値
		int expected = 88;

		// 2.do
		int result = target.max(x);

		// 3.check
		assertEquals(expected, result);

		// 4.log
		log.info("結果：" + result);
	}

	@Test
	void testMax2() {
		// 1.ready
		int[] x = new int[0];

		// 2.do ＆ 3.check
		assertThrows(IllegalArgumentException.class, () -> target.max(x));

		// 4.log
		log.info("結果：IllegalArgumentException");
	}

	@Test
	void testCommon() {
		int[] x = { 6, 5, 9, 4, 0 };
		int[] y = { 2, 5, 3, 4, 1 };
		// 以下は予測値
		int expected = 2;

		// 2.do
		int result = target.common(x, y);

		// 3.check
		assertEquals(expected, result);

		// 4.log
		log.info("結果：" + result);

	}

	@Test
	void testBubbleSort() {
		int[] x = { 6, 5, 9, 4, 0 };
		int[] expected = { 0, 4, 5, 6, 9 };
		int[] result = target.bubbleSort(x);
		// 3.check

		assertArrayEquals(expected, result);

		// 4.log
		log.info("結果：" + result);
	}

}
