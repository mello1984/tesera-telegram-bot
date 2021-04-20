package ru.butakov.teseratelegrambot.bot.handlers;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import ru.butakov.teseratelegrambot.bot.handlers.messagehandlers.BotCommand;
import ru.butakov.teseratelegrambot.bot.handlers.messagehandlers.InputMessageHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HandlerManager {
    Map<BotCommand, InputMessageHandler> handlersMap = new HashMap<>();

    public HandlerManager(List<InputMessageHandler> messageHandlers) {
        messageHandlers.forEach(handler -> handlersMap.put(handler.getHandlerCommand(), handler));
    }

    public InputMessageHandler getMessageHandler(BotCommand command){
        return handlersMap.get(command);
    }
}
