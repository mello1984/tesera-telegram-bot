package ru.butakov.teseratelegrambot.bot;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import ru.butakov.teseratelegrambot.bot.handlers.InputMessageHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HandlerManager {
    Map<BotCommand, InputMessageHandler> handlersMap = new HashMap<>();

    public HandlerManager(List<InputMessageHandler> messageHandlers) {
        messageHandlers.forEach(handler -> handlersMap.put(handler.getHandlerCommand(), handler));
    }

    public InputMessageHandler getMessageHandler(BotCommand command){
        return handlersMap.get(command);
    }
}
