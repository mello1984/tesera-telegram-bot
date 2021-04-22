package ru.butakov.teseratelegrambot;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@SpringBootApplication(scanBasePackages = "ru.butakov.teseratelegrambot.*")
@EnableScheduling
@Slf4j
public class TeseraTelegramBotApplication {

    public static void main(String[] args) {
        log.info("Start application");
        SpringApplication.run(TeseraTelegramBotApplication.class, args);
    }
}

//TODO: [?] Защита от ddos
//TODO: [?] Подписка на игры из профиля пользователя