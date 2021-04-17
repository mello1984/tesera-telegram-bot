package ru.butakov.teseratelegrambot.config;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.butakov.teseratelegrambot.bot.WebHookTeseraBot;

@Configuration
@PropertySource("classpath:bot.properties")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SpringConfig {
    @Value("${bot.token}")
    String botToken;
    @Value("${bot.name}")
    String botName;
    @Value("${bot.webHookPath}")
    String botWebHookPath;

    @Bean
    public WebHookTeseraBot getWebHookTeseraBot() {
        WebHookTeseraBot bot = new WebHookTeseraBot(botToken, botName, botWebHookPath);
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(bot, new SetWebhook());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        return bot;
    }

    @Bean("restTemplate")
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

}
