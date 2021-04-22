package ru.butakov.teseratelegrambot.bot.handlers;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import ru.butakov.teseratelegrambot.bot.handlers.callbackquery.CallbackCommand;
import ru.butakov.teseratelegrambot.bot.handlers.callbackquery.CallbackQueryHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class CallbackManager {
    Map<CallbackCommand, CallbackQueryHandler> handlersMap = new HashMap<>();

    public CallbackManager(List<CallbackQueryHandler> callbackQueryHandlers) {
        callbackQueryHandlers.forEach(handler -> handlersMap.put(handler.getCallbackCommand(), handler));
    }

    public SendMessage handleCallbackQuery(CallbackQuery callbackQuery){
        return getCallbackQueryHandler(callbackQuery).handleCallbackQuery(callbackQuery);
    }

    private CallbackQueryHandler getCallbackQueryHandler(CallbackQuery callbackQuery) {
        return handlersMap.get(CallbackCommand.SUBSCRIBE);
//        return switch (callbackQuery.getData()) {
//            case "News_On", "News_Off", "Article_On", "Article_Off", "Journal_On", "Journal_Off", "Thought_On", "Thought_Off",
//                    "Comment_On","Comment_Off", "Game_On", "Game_Off" -> handlersMap.get(CallbackCommand.SUBSCRIBE);
//            default -> throw new IllegalStateException("Unexpected value: " + callbackQuery.getData());
//        };
    }

}

