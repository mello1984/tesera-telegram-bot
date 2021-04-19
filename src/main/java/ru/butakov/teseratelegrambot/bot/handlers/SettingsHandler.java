package ru.butakov.teseratelegrambot.bot.handlers;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.butakov.teseratelegrambot.bot.BotCommand;
import ru.butakov.teseratelegrambot.bot.SendMessageFormat;
import ru.butakov.teseratelegrambot.entity.ObjectType;
import ru.butakov.teseratelegrambot.entity.User;
import ru.butakov.teseratelegrambot.service.ObjectTypeService;
import ru.butakov.teseratelegrambot.service.ReplyMessageService;
import ru.butakov.teseratelegrambot.service.UserService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SettingsHandler implements InputMessageHandler {
    @Autowired
    ReplyMessageService replyMessageService;
    @Autowired
    SendMessageFormat sendMessageFormat;
    @Autowired
    UserService userService;
    @Autowired
    ObjectTypeService objectTypeService;

    @Override
    public SendMessage handle(Message message) {
        String replyText = replyMessageService.getMessage("reply.settings", getBaseUserSubscriptions(message.getChatId()));
        SendMessage replyMessage = sendMessageFormat.getSendMessageBaseFormat(message.getChatId());
        replyMessage.setText(replyText);
        return replyMessage;
    }

    @Override
    public BotCommand getHandlerCommand() {
        return BotCommand.SETTINGS;
    }

    private Object[] getBaseUserSubscriptions(long chatId) {
        User user = userService.findUserById(chatId);
        Set<ObjectType> userObjectTypes = user.getObjectTypes();

        List<String> result = new ArrayList<>();
        result.add (userObjectTypes.contains(objectTypeService.getObjectType("News")) ? "On" : "Off");
        result.add (userObjectTypes.contains(objectTypeService.getObjectType("Article")) ? "On" : "Off");
        result.add (userObjectTypes.contains(objectTypeService.getObjectType("Journal")) ? "On" : "Off");
        result.add (userObjectTypes.contains(objectTypeService.getObjectType("News")) ? "On" : "Off");
        return result.toArray();
    }
}
