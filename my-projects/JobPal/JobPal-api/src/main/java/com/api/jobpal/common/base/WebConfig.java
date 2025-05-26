package com.api.jobpal.common.base;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.client.RestTemplate;

/**
 * システム全体の設定を行うための管理クラス.
 *
 * <p>
 * DIの設定やシステム環境設定、システム全体に関わる定数を設定するために利用する.
 * <p>
 * その他の設定に関してはapplication.propertiesファイルに記述する.
 *
 *  */
@Configuration
public class WebConfig {

    /** アクセストークンの有効期限(1日) */
    public static final long EXPIRATION_TIME_FOR_ACCESS = 1000L * 60L * 60L * 24;

    /** アクセストークンの送受信に使用するヘッダー名 */
    public static final String AUTHORIZATION_HEADER_NAME = "Authorization";

    /** トークンのプレフィックス */
    public static final String TOKEN_PREFIX = "Bearer ";

    /** ユーザIDを示すクレーム名 */
    public static final String JWT_USER_ID_CLAIM_NAME = "userId";

    /** トークンの権限を示すクレーム名 */
    public static final String JWT_AUTHORITY_CLAIM_NAME = "grantId";

    /**
     * 使用するパスワードエンコーダーのインスタンスを生成.
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * ObjectMapperのインスタンスを生成.
     */
    @Bean
    Logger logger() {
        return LoggerFactory.getLogger("API_LOG");
    }

    /**
     * ObjectMapperのインスタンスを生成.
     */
    @Bean
    ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    /**
     * RestTemplateのインスタンスを生成.
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    /**
     * MessageSourceのインスタンスを生成.
     */
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource bean = new ReloadableResourceBundleMessageSource();
        bean.setBasename("classpath:messages");
        bean.setDefaultEncoding("UTF-8");

        return bean;
    }

    /**
     * LocalValidatorFactoryBeanのインスタンスを生成.
     */
    @Bean
    public LocalValidatorFactoryBean localValidatorFactoryBean() {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource());

        return bean;
    }

}
