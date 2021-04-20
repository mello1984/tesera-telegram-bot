package ru.butakov.teseratelegrambot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.butakov.teseratelegrambot.bot.WebHookTeseraBot;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Service
@Slf4j
public class MessageSenderService {
    private final static BlockingQueue<BotApiMethod<?>> updateBlockingQueue = new LinkedBlockingQueue<>();
    @Autowired
    WebHookTeseraBot teseraBot;

    @Scheduled(fixedDelayString = "${telegrambot.updateperiod}")
    private void sendUpdateFromQueue() throws InterruptedException {
        BotApiMethod<?> botApiMethod = updateBlockingQueue.take();
        try {
            teseraBot.execute(botApiMethod);
        } catch (TelegramApiException e) {
            log.warn("Exception in sending message: " + botApiMethod.toString());
            log.warn("Exception in sending message", e);
        }
    }

    public boolean offerBotApiMethodToQueue(BotApiMethod<?> botApiMethod) {
        return updateBlockingQueue.offer(botApiMethod);
    }
}
