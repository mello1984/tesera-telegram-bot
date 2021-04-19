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
import ru.butakov.teseratelegrambot.entity.ObjectType;
import ru.butakov.teseratelegrambot.entity.Publication;
import ru.butakov.teseratelegrambot.entity.User;
import ru.butakov.teseratelegrambot.service.MainService;
import ru.butakov.teseratelegrambot.service.ObjectTypeService;
import ru.butakov.teseratelegrambot.service.UserService;

import java.util.List;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class TelegramFacade {
    @Autowired
    WebHookTeseraBot teseraBot;
    @Autowired
    MainService mainService;
    @Autowired
    UserService userService;
    @Autowired
    ObjectTypeService objectTypeService;

    @Autowired
    HandlerManager handlerManager;
    @Autowired
    CallbackManager callbackManager;

    @Autowired
    SendMessageFormat sendMessageFormat;


    public BotApiMethod<?> handleUpdate(Update update) {
        SendMessage replyMessage = null;
        if (update.hasCallbackQuery()) {
            log.info("New callbackQuery from User:{}, with data: {}",
                    update.getCallbackQuery().getFrom().getId(), update.getCallbackQuery().getData());
            replyMessage = callbackManager.getCallbackQueryHandler(update.getCallbackQuery()).handleCallbackQuery(update.getCallbackQuery());
        }

        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            log.info("New message from User:{}, with text: {}", message.getChatId(), message.getText());
            replyMessage = handleInputMessage(message);
        }

//        List<Publication> publicationList = mainService.getPublicationList();
//        for (Publication p : publicationList) {
//            ObjectType objectType = objectTypeService.findObjectTypeByIdOrCreateNewObjectType(p.getObjectType());
//
//            for (User user : objectType.getUserSet()) {
//                SendMessage sendMessage = sendMessageFormat.getSendMessageBaseFormat(user.getChatId());
//                sendMessage.setText("PUBLICATION: " + p.toString());
//                teseraBot.send(sendMessage);
//            }
//        }

        return replyMessage;


    }

    private SendMessage handleInputMessage(Message message) {
        BotCommand command = switch (message.getText()) {
            case "/start" -> BotCommand.START;
            case "/settings" -> BotCommand.SETTINGS;
            case "/help" -> BotCommand.HELP;
            case "/get" -> BotCommand.GET;
            default -> BotCommand.UNKNONWN;
        };
        return handlerManager.getMessageHandler(command).handle(message);
    }
}
