package ru.butakov.teseratelegrambot.bot;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.butakov.teseratelegrambot.bot.handlers.StartHandler;
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


    public SendMessage handleUpdate(Update update) {
        long chatId = update.getMessage().getChatId();
        User user = userService.findUserByIdOrCreateNewUser(chatId);

        SendMessage replyMessage = null;
        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            log.info("New message from User:{},  with text: {}", message.getChatId(), message.getText());
            replyMessage = handleInputMessage(message);
        }




//        System.out.println(user.getObjectTypes());

//        List<Publication> publicationList = mainService.getPublicationList();
//        for (Publication p : publicationList) {
//
//            ObjectType objectType = objectTypeService.findObjectTypeByIdOrCreateNewObjectType(p.getObjectType());
//            System.out.println(objectType);
//
//            SendMessage sendMessage = new SendMessage(String.valueOf(chatId), p.toString());
//            teseraBot.send(sendMessage);
//        }


        return replyMessage;
    }

    private SendMessage handleInputMessage(Message message) {
        BotCommand command = switch (message.getText()) {
            case "/start" -> BotCommand.START;
            case "/settings" -> BotCommand.SETTINGS;
            case "/help" -> BotCommand.HELP;
            default -> BotCommand.UNKNONWN;
        };

        teseraBot.send(handlerManager.getMessageHandler(command).handle(message));
        return null;
    }


}
