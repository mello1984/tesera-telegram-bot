package ru.butakov.teseratelegrambot.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.butakov.teseratelegrambot.bot.WebHookTeseraBot;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MessageSenderService {
    private final static BlockingQueue<BotApiMethod<?>> updateBlockingQueue = new LinkedBlockingQueue<>();
    @Autowired
    WebHookTeseraBot teseraBot;

    @Scheduled(fixedDelayString = "${telegrambot.updateperiod}")
    private void sendUpdateFromQueue() throws InterruptedException {
        BotApiMethod<?> botApiMethod = updateBlockingQueue.take();
        try {
            teseraBot.execute(botApiMethod);
            if (botApiMethod instanceof SendMessage) {
                SendMessage sendMessage = (SendMessage) botApiMethod;
                log.info("Send message to User: {}", sendMessage.getChatId());
            } else log.info("BotApiMethod executed: {}", botApiMethod.toString());
        } catch (TelegramApiException e) {
            log.warn("Exception with executing botApiMethod, part1: {}", botApiMethod.toString(), e);
            updateBlockingQueue.offer(botApiMethod);
            Thread.sleep(500);
        }
    }

    public boolean offerBotApiMethodToQueue(BotApiMethod<?> botApiMethod) {
        return updateBlockingQueue.offer(botApiMethod);
    }
}
