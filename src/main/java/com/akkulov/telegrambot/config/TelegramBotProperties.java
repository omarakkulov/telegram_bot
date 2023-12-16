package com.akkulov.telegrambot.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

/**
 * Данные телеграм бота.
 */
@PropertySource("classpath:bot-config.properties")
@ConfigurationProperties(prefix = "bot")
@Getter
@Setter
public class TelegramBotProperties {

  private String username;
  private String token;
}
