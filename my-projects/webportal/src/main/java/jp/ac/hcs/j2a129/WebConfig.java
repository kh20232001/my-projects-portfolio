package jp.ac.hcs.j2a129;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.client.RestTemplate;

/**
 * システム全体の設定を行うための管理クラスです。
 * 
 * <p>DIの設定やシステム環境設定、システム全体にかかわる定数を
 * 設定するために利用し、その他の設定に関しては
 * application.propertiesファイルに記述します。
 * 
 * @author 春田和也
 * 
 */
@Configuration
public class WebConfig {

	/** ファイル出力パス */
	public static final String OUTPUT_PATH = "target/";

	/** CSVファイル名 */
	public static final String FILENAME_TASK_CSV = "tasklist.csv";

	/**
	 * RestTemplateライブラリのインスタンスを生成します。
	 * 
	 * <p>こちらはDIのために利用されることを想定しています。
	 * <p><strong><code>controller</code>や<code>service</code>から呼び出さないでください。</strong><br>
	 * 設定することで、<code>@Autowired</code> が設定されたプロパティへ自動的にインスタンスが設定されます。
	 * 
	 * @return RestTemplateインスタンス
	 */
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	/**
	 * MessageSorceライブラリのインスタンスを生成します。
	 * 
	 * <p>こちらはDIのために利用されることを想定しています。
	 * <p><strong><code>controller</code>や<code>service</code>から呼び出さないでください。</strong><br>
	 * 設定することで、<code>@Autowired</code>が設定されたプロパティへ自動的にインスタンスが設定されます。
	 * 
	 * @return MessageSourceインスタンス
	 */
	@Bean
	public MessageSource messageSource() {

		//メッセージプロパティのファイル設定
		ReloadableResourceBundleMessageSource bean = new ReloadableResourceBundleMessageSource();
		bean.setBasename("classpath:messages");
		bean.setDefaultEncoding("UTF-8");

		return bean;
	}

	/**
	 * LocalVailidFactoryBeanライブラリのインスタンスを生成します。
	 * 
	 * <p>こちらはDIのために利用されることを想定しています。
	 * <p><strong><code>controller</code>や<code>service</code>から呼び出さないでください。</strong><br>
	 * 設定することで、<code>@Autowired</code>が設定されたプロパティヘ自動的にインスタンスが生成されます。
	 * 
	 * @return LocalValidatoryFactorBeanインスタンス
	 */
	@Bean
	public LocalValidatorFactoryBean localValidatorFactorBean() {

		// バリデーションのメッセージ設定
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.setValidationMessageSource(messageSource());

		return localValidatorFactoryBean;
	}
}
