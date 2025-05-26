package com.api.domain.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.api.domain.models.data.ZipCodeData;
import com.api.domain.models.entities.ZipCodeEntity;

@RunWith(SpringRunner.class)
@SpringBootTest
class ZipCodeServiceTest {

	@Autowired
	private ZipCodeService target;

	// 下の階層をモック化
	@SpyBean
	private RestTemplate restTemplate;

	@Autowired
	private Logger log;

	@Test
	void 郵便番号から検索結果形式に変換テスト() {

		// 0.モック設定
		String mockJson = "{\n" +
				"\t\"message\":null,\n" +
				"\t\"results\":[\n" +
				"\t\t{\n" +
				"\t\t\t\"address1\":\"北海道\",\n" +
				"\t\t\t\"address2\":\"札幌市白石区\",\n" +
				"\t\t\t\"address3\":\"菊水六条\",\n" +
				"\t\t\t\"kana1\":\"ﾎｯｶｲﾄﾞｳ\",\n" +
				"\t\t\t\"kana2\":\"ｻｯﾎﾟﾛｼｼﾛｲｼｸ\",\n" +
				"\t\t\t\"kana3\":\"ｷｸｽｲ6ｼﾞｮｳ\",\n" +
				"\t\t\t\"prefcode\":\"1\",\n" +
				"\t\t\t\"zipcode\":\"0030806\"\n" +
				"\t\t}\n" +
				"\t],\n" +
				"\t\"status\":200\n" +
				"}";

		doReturn(mockJson).when(restTemplate).getForObject(any(), any(), anyString());

		// 1.テストデータ設定
		String zipcode = "0030806";
		// 2.テスト対象メソッド実行
		ZipCodeEntity resultEntity = target.execute(zipcode);

		// 3.テスト結果確認
		ZipCodeData result = resultEntity.getList().get(0);
		assertEquals("200", resultEntity.getStatus());
		assertEquals("null", resultEntity.getMessage());
		assertEquals("0030806", result.getZipcode());
		assertEquals("1", result.getPrefcode());
		assertEquals("北海道", result.getAddress1());
		assertEquals("札幌市白石区", result.getAddress2());
		assertEquals("菊水六条", result.getAddress3());
		assertEquals("ﾎｯｶｲﾄﾞｳ", result.getKana1());
		assertEquals("ｻｯﾎﾟﾛｼｼﾛｲｼｸ", result.getKana2());
		assertEquals("ｷｸｽｲ6ｼﾞｮｳ", result.getKana3());

		// 4.ログ確認
		log.info("郵便番号から検索結果形式に変換テスト " + resultEntity.toString());
	}

	@Test
	void 郵便番号から検索結果形式に変換テストIOExceptionが発生() {

		// 0.モック設定
		String mockJson = "{\n" +
				"\t\"message\":Invalid \\u001A character,\n" +
				"\t\"results\":[\n" +
				"\t\t{\n" +
				"\t\t\t\"address1\":\"北海道\",\n" +
				"\t\t\t\"address2\":\"札幌市白石区\",\n" +
				"\t\t\t\"address3\":\"菊水六条\",\n" +
				"\t\t\t\"kana1\":\"ﾎｯｶｲﾄﾞｳ\",\n" +
				"\t\t\t\"kana2\":\"ｻｯﾎﾟﾛｼｼﾛｲｼｸ\",\n" +
				"\t\t\t\"kana3\":\"ｷｸｽｲ6ｼﾞｮｳ\",\n" +
				"\t\t\t\"prefcode\":\"1\",\n" +
				"\t\t\t\"zipcode\":\"0030806\"\n" +
				"\t\t}\n" +
				"\t],\n" +
				"\t\"status\":200\n" +
				"}";

		doReturn(mockJson).when(restTemplate).getForObject(any(), any(), anyString());

		// 1.テストデータ設定
		String zipcode = "0030201";
		// 2.テスト対象メソッド実行
		ZipCodeEntity resultEntity = target.execute(zipcode);

		// 3.テスト結果確認
		assertEquals("通信に失敗しました", resultEntity.getErrorMessage());
		assertEquals(0, resultEntity.getList().size());
		assertNull(resultEntity.getMessage());
		assertNull(resultEntity.getStatus());

		// 4.ログ確認
		log.info("郵便番号から検索結果形式に変換テスト " + resultEntity.toString());
	}

}
