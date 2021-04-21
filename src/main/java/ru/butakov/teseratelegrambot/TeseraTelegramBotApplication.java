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

    public static void main(String[] args) {
        SpringApplication.run(TeseraTelegramBotApplication.class, args);
    }
}

//TODO: [*] Защита от ddos
//TODO: [*] Подписка на игры - pagination для отписки
//TODO: [*] Логирование
//TODO: [*] Все сообщения вынести в messages
//------------------------------------------------------------------
//TODO: [+] Подписка на игры
//TODO: [+] Публикации и комментарии - текст сообщения
//TODO: [+] Исправить текст отображения игр
//TODO: [+] Проверить создание пользователя при первом сообщении
//TODO: [+] Добавить эмодзи
//TODO: [-] Подписка на пользователей - наверное нет смысла