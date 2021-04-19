package ru.butakov.teseratelegrambot.bot.handlers;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import ru.butakov.teseratelegrambot.bot.handlers.callbackquery.CallbackCommand;
import ru.butakov.teseratelegrambot.bot.handlers.callbackquery.CallbackQueryHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CallbackManager {
    Map<CallbackCommand, CallbackQueryHandler> handlersMap = new HashMap<>();

    public CallbackManager(List<CallbackQueryHandler> callbackQueryHandlers) {
        callbackQueryHandlers.forEach(handler -> handlersMap.put(handler.getCallbackCommand(), handler));
    }

    public CallbackQueryHandler getCallbackQueryHandler(CallbackQuery callbackQuery) {
        CallbackQueryHandler handler = switch (callbackQuery.getData()) {
            case "newsOn", "newsOff", "articleOn", "articleOff", "journalOn", "journalOff", "thoughtOn", "thoughtOff" -> handlersMap.get(CallbackCommand.SUBSCRIBE);
            default -> throw new IllegalStateException("Unexpected value: " + callbackQuery.getData());
        };


        return handler;
    }

}

