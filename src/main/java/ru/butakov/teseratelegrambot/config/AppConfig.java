package ru.butakov.teseratelegrambot.config;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.butakov.teseratelegrambot.bot.WebHookTeseraBot;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;


@Configuration
@PropertySources({
        @PropertySource("classpath:private.properties"),
        @PropertySource("classpath:server.properties")
})
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class AppConfig {

    @Bean
    public WebHookTeseraBot webHookTeseraBot(
            @Value("${bot.token}") String botToken,
            @Value("${bot.name}") String botName,
            @Value("${telegrambot.registerwebhook.path}") String telegramSetWebhookPath,
            @Value("${server.use_self_signed_certificate}") boolean useSelfSignedCertificate,
            @Value("${bot.webHookPath:/}") String botWebHookPath,
            @Value("${bot.ip:}") String ipAddress,
            @Value("${server.port:}") String port,
            @Value("${bot.certificate:}") String certificate,
            @Autowired RestTemplate restTemplate
    ) {
        return useSelfSignedCertificate ?
                getBotWithSelfSignedCertificate(botToken, botName, ipAddress, port, certificate) :
                getBotWithoutSelfSignedCertificate(botToken, botName, botWebHookPath, telegramSetWebhookPath, restTemplate);
    }

    private WebHookTeseraBot getBotWithoutSelfSignedCertificate(String botToken, String botName, String botWebHookPath,
                                                                String telegramSetWebhookPath, RestTemplate restTemplate) {
        WebHookTeseraBot bot = new WebHookTeseraBot(botToken, botName, botWebHookPath);

        String url = String.format(telegramSetWebhookPath, botToken, botWebHookPath);
        ResponseEntity<Object> response = restTemplate.exchange(
                url, HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                });

        log.info("Create WebHookTeseraBot without self-signed certificate, telegram answer: {}", response);
        return bot;
    }

    private WebHookTeseraBot getBotWithSelfSignedCertificate(String botToken, String botName,
                                                             String ipAddress, String port, String certificate) {
        WebHookTeseraBot bot = new WebHookTeseraBot(botToken, botName, "/");
        try {
            log.info("Create WebHookTeseraBot, try register self-signed certificate");
            String url = ipAddress + ":" + port;
            InputStream resource = new ClassPathResource(certificate).getInputStream();
            InputFile certificateFile = new InputFile(resource, certificate);
            int maxConnections = 40;
            List<String> allowedUpdates = Collections.emptyList();
            Boolean dropPendingUpdates = null;
            SetWebhook webhook = new SetWebhook(url, certificateFile, maxConnections, allowedUpdates, ipAddress, dropPendingUpdates);
            bot.setWebhook(webhook);
            log.info("Register webhook successful");
        } catch (TelegramApiException | IOException e) {
            log.warn("Exception on register bot", e);
        }
        return bot;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(4);
        return scheduler;
    }
}
