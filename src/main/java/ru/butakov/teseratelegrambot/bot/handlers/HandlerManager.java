package ru.butakov.teseratelegrambot.bot.handlers;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
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

    public SendMessage handleInputMessage(Message message) {
        BotCommand command = switch (message.getText()) {
            case "/start" -> BotCommand.START;
            case "/settings" -> BotCommand.SETTINGS;
            case "/help" -> BotCommand.HELP;
            default -> BotCommand.UNKNONWN;
        };
        if (message.getText().startsWith("/search")) command = BotCommand.SEARCH;
        if (message.getText().startsWith("/game")) command = BotCommand.GAMESUBSCRIPTION;
        return handlersMap.get(command).handle(message);
    }
}
