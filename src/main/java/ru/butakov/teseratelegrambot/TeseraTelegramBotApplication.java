package ru.butakov.teseratelegrambot;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@SpringBootApplication(scanBasePackages = "ru.butakov.teseratelegrambot.*")
@EnableScheduling
public class TeseraTelegramBotApplication {
    public static BlockingQueue<BotApiMethod<?>> updateBlockingQueue = new LinkedBlockingQueue<>();

    public static void main(String[] args) {

        SpringApplication.run(TeseraTelegramBotApplication.class, args);
    }
}