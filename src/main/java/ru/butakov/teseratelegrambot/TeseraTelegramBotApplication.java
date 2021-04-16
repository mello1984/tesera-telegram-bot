package ru.butakov.teseratelegrambot;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;


@SpringBootApplication(scanBasePackages = "ru.butakov.teseratelegrambot.*")
public class TeseraTelegramBotApplication {

    public static void main(String[] args) {

        SpringApplication.run(TeseraTelegramBotApplication.class, args);
    }
}
