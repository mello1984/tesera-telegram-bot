package ru.butakov.teseratelegrambot.bot.handlers.callbackquery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import ru.butakov.teseratelegrambot.bot.SendMessageFormat;
import ru.butakov.teseratelegrambot.entity.User;
import ru.butakov.teseratelegrambot.service.ObjectTypeService;
import ru.butakov.teseratelegrambot.service.ReplyMessageService;
import ru.butakov.teseratelegrambot.service.UserService;

@Component
public class BaseSubscriptionCommandQuery implements CallbackQueryHandler {
    @Autowired
    UserService userService;
    @Autowired
    ObjectTypeService objectTypeService;
    @Autowired
    ReplyMessageService replyMessageService;
    @Autowired
    SendMessageFormat sendMessageFormat;

    @Override
    public SendMessage handleCallbackQuery(CallbackQuery callbackQuery) {
        System.out.println(callbackQuery.getData());
        long chatId = callbackQuery.getMessage().getChatId();
        User user = userService.findUserById(chatId);
        switch (callbackQuery.getData()) {
            case "newsOn" -> user.getObjectTypes().add(objectTypeService.getObjectType("News"));
            case "newsOff" -> user.getObjectTypes().remove(objectTypeService.getObjectType("News"));
            case "articleOn" -> user.getObjectTypes().add(objectTypeService.getObjectType("Article"));
            case "articleOff" -> user.getObjectTypes().remove(objectTypeService.getObjectType("Article"));
            case "journalOn" -> user.getObjectTypes().add(objectTypeService.getObjectType("Journal"));
            case "journalOff" -> user.getObjectTypes().remove(objectTypeService.getObjectType("Journal"));
            case "thoughtOn" -> user.getObjectTypes().add(objectTypeService.getObjectType("Thought"));
            case "thoughtOff" -> user.getObjectTypes().remove(objectTypeService.getObjectType("Thought"));
            case "commentOn" -> user.getObjectTypes().add(objectTypeService.getObjectType("Comment"));
            case "commentOff" -> user.getObjectTypes().remove(objectTypeService.getObjectType("Comment"));
            case "gameOn" -> user.getObjectTypes().add(objectTypeService.getObjectType("Game"));
            case "gameOff" -> user.getObjectTypes().remove(objectTypeService.getObjectType("Game"));
        }
        userService.saveUser(user);
        SendMessage reply = sendMessageFormat.getSendMessageBaseFormat(chatId);
        String text = replyMessageService.getMessage("reply.settings.basesubscription");
        reply.setText(text);
        return reply;
    }

    @Override
    public CallbackCommand getCallbackCommand() {
        return CallbackCommand.SUBSCRIBE;
    }
}
