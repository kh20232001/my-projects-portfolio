package com.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Spring Bootの起動クラス.
 */
@SpringBootApplication
@EnableScheduling // スケジューリングを有効化
public class ApiApplication extends SpringBootServletInitializer {

  private static final Logger logger = LoggerFactory.getLogger(ApiApplication.class);

  /**
   * Spring Bootの起動.
   *
   * @param args 起動引数
   */
  public static void main(String[] args) {
    SpringApplication.run(ApiApplication.class, args);
    logger.info("Application has started successfully.");
  }

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    logger.info("Configuring application for WAR deployment.");
    return application.sources(ApiApplication.class);
  }
}
