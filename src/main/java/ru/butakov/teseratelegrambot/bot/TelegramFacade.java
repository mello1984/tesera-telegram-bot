package ru.butakov.teseratelegrambot.bot;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.butakov.teseratelegrambot.bot.handlers.CallbackManager;
import ru.butakov.teseratelegrambot.bot.handlers.HandlerManager;
import ru.butakov.teseratelegrambot.bot.handlers.messagehandlers.BotCommand;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class TelegramFacade {
    @Autowired
    HandlerManager handlerManager;
    @Autowired
    CallbackManager callbackManager;

    public BotApiMethod<?> handleUpdate(Update update) {
        SendMessage replyMessage = null;
        if (update.hasCallbackQuery()) {
            log.info("New callbackQuery from User:{}, with data: {}",
                    update.getCallbackQuery().getFrom().getId(), update.getCallbackQuery().getData());
            replyMessage = callbackManager
                    .getCallbackQueryHandler(update.getCallbackQuery())
                    .handleCallbackQuery(update.getCallbackQuery());
        }

        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            log.info("New message from User:{}, with text: {}", message.getChatId(), message.getText());
            replyMessage = handlerManager.handleInputMessage(message);
        }
        return replyMessage;
    }
}
