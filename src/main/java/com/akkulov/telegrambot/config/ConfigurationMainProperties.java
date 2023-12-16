package com.akkulov.telegrambot.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

/**
 * Общий класс с пропертями.
 */
@Import({
    TelegramBotProperties.class
})
@Configuration
@PropertySources({
    @PropertySource("classpath:application-config.properties")
})
public class ConfigurationMainProperties {

}
