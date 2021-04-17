package ru.butakov.teseratelegrambot;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


@SpringBootApplication(scanBasePackages = "ru.butakov.teseratelegrambot.*")
public class TeseraTelegramBotApplication {
    public static BlockingQueue<Update> updateBlockingQueue = new LinkedBlockingQueue<>();

    public static void main(String[] args) {

        SpringApplication.run(TeseraTelegramBotApplication.class, args);
    }
}
