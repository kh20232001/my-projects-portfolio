package jp.ac.hcs.j2a129.prime;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
class PrimeServiceTest {
	// テスト対象のクラスを定義
	@Autowired
	private PrimeService target;

	@Test
	void testExec() {
		// 1.ready
		String x = "19";
		// 以下は予測値
		PrimeData expected = new PrimeData();
		List<Integer> list = new ArrayList<Integer>(Arrays.asList(2, 3, 5, 7, 11, 13, 17, 19));
		expected.setAns(list);

		// 2.do
		PrimeData result = target.exec(x);

		// 3.check
		assertIterableEquals(expected.getAns(), result.getAns());

		// 4.log
		log.info("結果：" + result);
	}

}
