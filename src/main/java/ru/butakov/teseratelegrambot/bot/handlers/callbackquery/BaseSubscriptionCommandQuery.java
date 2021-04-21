package ru.butakov.teseratelegrambot.bot.handlers.callbackquery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import ru.butakov.teseratelegrambot.bot.SendMessageFormat;
import ru.butakov.teseratelegrambot.entity.ObjectType;
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
        long chatId = callbackQuery.getMessage().getChatId();
        User user = userService.findUserByIdOrCreateNewUser(chatId);

        String[] command = callbackQuery.getData().split("_");
        ObjectType objectType = objectTypeService.getObjectType(command[0]);
        if (command[1].equals("On")) user.getObjectTypes().add(objectType);
        else user.getObjectTypes().remove(objectType);

        userService.saveUser(user);
        String replyText = replyMessageService.getMessage("reply.settings.basesubscription");
        SendMessage replyMessage = sendMessageFormat.getSendMessageBaseFormat(chatId);
        replyMessage.setText(replyText);
        return replyMessage;
    }

    @Override
    public CallbackCommand getCallbackCommand() {
        return CallbackCommand.SUBSCRIBE;
    }
}
