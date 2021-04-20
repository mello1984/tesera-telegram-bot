package ru.butakov.teseratelegrambot.config;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.butakov.teseratelegrambot.bot.WebHookTeseraBot;

@Configuration
@PropertySources({@PropertySource("classpath:bot.properties"), @PropertySource("classpath:messages_ru_RU.properties")})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppConfig {
    @Value("${bot.token}")
    String botToken;
    @Value("${bot.name}")
    String botName;
    @Value("${bot.webHookPath}")
    String botWebHookPath;

    @Bean
    public WebHookTeseraBot webHookTeseraBot() {
        WebHookTeseraBot bot = new WebHookTeseraBot(botToken, botName, botWebHookPath);
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(bot, new SetWebhook());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        return bot;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public MessageSource messageSource(){
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Bean
    public TaskScheduler taskScheduler(){
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(4);
        return scheduler;
    }
}
