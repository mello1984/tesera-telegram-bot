package ru.butakov.teseratelegrambot.config;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import ru.butakov.teseratelegrambot.bot.LongPollingTeseraBot;

@Configuration
@PropertySource("classpath:bot.properties")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SpringConfig {
    @Value("${bot.token}")
    String botToken;
    @Value("${bot.name}")
    String botName;

    @Bean
    public LongPollingTeseraBot getBot() {
        return new LongPollingTeseraBot(botToken, botName);
    }


}
