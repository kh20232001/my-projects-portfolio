package jp.ac.hcs.j2a129.bmi;

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
class BmiServiceTest {
	// テスト対象のクラスを定義
	@Autowired
	private BmiService target;

	@Test
	void testValidate() {
		// 1.ready
		String x = "1";
		String y = "2";
		// 以下は予測値
		boolean expected = true;

		// 2.do
		boolean result = target.validate(x, y);

		// 3.check
		assertEquals(expected, result);

		// 4.log
		log.info("結果：" + result);
	}

	@Test
	void testValidate2() {
		// 1.ready
		String x = "30";
		String y = "2";
		// 以下は予測値
		boolean expected = true;

		// 2.do
		boolean result = target.validate(x, y);

		// 3.check
		assertEquals(expected, result);

		// 4.log
		log.info("結果：" + result);
	}

	@Test
	void testValidate3() {
		// 1.ready
		String x = "251";
		String y = "2";
		// 以下は予測値
		boolean expected = true;

		// 2.do
		boolean result = target.validate(x, y);

		// 3.check
		assertEquals(expected, result);

		// 4.log
		log.info("結果：" + result);
	}

	@Test
	void testValidate4() {
		// 1.ready
		String x = "120";
		String y = "60";
		// 以下は予測値
		boolean expected = false;

		// 2.do
		boolean result = target.validate(x, y);

		// 3.check
		assertEquals(expected, result);

		// 4.log
		log.info("結果：" + result);
	}

	@Test
	void testValidate5() {
		// 1.ready
		String x = "120";
		String y = "201";
		// 以下は予測値
		boolean expected = true;

		// 2.do
		boolean result = target.validate(x, y);

		// 3.check
		assertEquals(expected, result);

		// 4.log
		log.info("結果：" + result);
	}

	@Test
	void testExec() {
		// 1.ready
		String x = "140";
		String y = "31.20";
		// 以下は予測値
		String expectedBmi = "15.918";
		String expectedComment = "痩せ過ぎ";
		String expectedPath = "/img/bmi/gari.png";

		// 2.do
		BmiData result = target.exec(x, y);
		System.out.println(result.getAns());
		System.out.println(result.getComment());
		System.out.println(result.getPath());
		// 3.check
		assertEquals(expectedBmi, result.getAns());
		assertEquals(expectedComment, result.getComment());
		assertEquals(expectedPath, result.getPath());

		// 4.log
		log.info("結果：" + result);
	}

	@Test
	void testExec2() {
		// 1.ready
		String x = "140";
		String y = "33.124";
		// 以下は予測値
		String expectedBmi = "16.9";
		String expectedComment = "痩せ";
		String expectedPath = "/img/bmi/gari.png";

		// 2.do
		BmiData result = target.exec(x, y);
		System.out.println(result.getAns());
		System.out.println(result.getComment());
		System.out.println(result.getPath());
		// 3.check
		assertEquals(expectedBmi, result.getAns());
		assertEquals(expectedComment, result.getComment());
		assertEquals(expectedPath, result.getPath());

		// 4.log
		log.info("結果：" + result);
	}

	@Test
	void testExec3() {
		// 1.ready
		String x = "140";
		String y = "36.064";
		// 以下は予測値
		String expectedBmi = "18.4";
		String expectedComment = "痩せぎみ";
		String expectedPath = "/img/bmi/gari.png";

		// 2.do
		BmiData result = target.exec(x, y);
		System.out.println(result.getAns());
		System.out.println(result.getComment());
		System.out.println(result.getPath());
		// 3.check
		assertEquals(expectedBmi, result.getAns());
		assertEquals(expectedComment, result.getComment());
		assertEquals(expectedPath, result.getPath());

		// 4.log
		log.info("結果：" + result);
	}

	@Test
	void testExec4() {
		// 1.ready
		String x = "140";
		String y = "48.804";
		// 以下は予測値
		String expectedBmi = "24.9";
		String expectedComment = "普通体重";
		String expectedPath = "/img/bmi/normal.png";

		// 2.do
		BmiData result = target.exec(x, y);
		System.out.println(result.getAns());
		System.out.println(result.getComment());
		System.out.println(result.getPath());
		// 3.check
		assertEquals(expectedBmi, result.getAns());
		assertEquals(expectedComment, result.getComment());
		assertEquals(expectedPath, result.getPath());

		// 4.log
		log.info("結果：" + result);
	}

	@Test
	void testExec5() {
		// 1.ready
		String x = "140";
		String y = "58.604";
		// 以下は予測値
		String expectedBmi = "29.9";
		String expectedComment = "前肥満";
		String expectedPath = "/img/bmi/puni.png";

		// 2.do
		BmiData result = target.exec(x, y);
		System.out.println(result.getAns());
		System.out.println(result.getComment());
		System.out.println(result.getPath());
		// 3.check
		assertEquals(expectedBmi, result.getAns());
		assertEquals(expectedComment, result.getComment());
		assertEquals(expectedPath, result.getPath());

		// 4.log
		log.info("結果：" + result);
	}

	@Test
	void testExec6() {
		// 1.ready
		String x = "140";
		String y = "68.404";
		// 以下は予測値
		String expectedBmi = "34.9";
		String expectedComment = "肥満度(1度)";
		String expectedPath = "/img/bmi/puni.png";

		// 2.do
		BmiData result = target.exec(x, y);
		System.out.println(result.getAns());
		System.out.println(result.getComment());
		System.out.println(result.getPath());
		// 3.check
		assertEquals(expectedBmi, result.getAns());
		assertEquals(expectedComment, result.getComment());
		assertEquals(expectedPath, result.getPath());

		// 4.log
		log.info("結果：" + result);
	}

	@Test
	void testExec7() {
		// 1.ready
		String x = "140";
		String y = "78.204";
		// 以下は予測値
		String expectedBmi = "39.9";
		String expectedComment = "肥満度(2度)";
		String expectedPath = "/img/bmi/puni.png";

		// 2.do
		BmiData result = target.exec(x, y);
		System.out.println(result.getAns());
		System.out.println(result.getComment());
		System.out.println(result.getPath());
		// 3.check
		assertEquals(expectedBmi, result.getAns());
		assertEquals(expectedComment, result.getComment());
		assertEquals(expectedPath, result.getPath());

		// 4.log
		log.info("結果：" + result);
	}

	@Test
	void testExec8() {
		// 1.ready
		String x = "140";
		String y = "78.596";
		// 以下は予測値
		String expectedBmi = "40.1";
		String expectedComment = "肥満度(3度)";
		String expectedPath = "/img/bmi/puni.png";

		// 2.do
		BmiData result = target.exec(x, y);
		System.out.println(result.getAns());
		System.out.println(result.getComment());
		System.out.println(result.getPath());
		// 3.check
		assertEquals(expectedBmi, result.getAns());
		assertEquals(expectedComment, result.getComment());
		assertEquals(expectedPath, result.getPath());

		// 4.log
		log.info("結果：" + result);
	}

}
