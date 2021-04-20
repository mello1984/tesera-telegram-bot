package ru.butakov.teseratelegrambot.bot.handlers.messagehandlers;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.butakov.teseratelegrambot.bot.SendMessageFormat;
import ru.butakov.teseratelegrambot.entity.ObjectType;
import ru.butakov.teseratelegrambot.entity.User;
import ru.butakov.teseratelegrambot.service.ObjectTypeService;
import ru.butakov.teseratelegrambot.service.ReplyMessageService;
import ru.butakov.teseratelegrambot.service.UserService;

import java.util.ArrayList;
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
        setInlineButtons(replyMessage);
        return replyMessage;
    }

    @Override
    public BotCommand getHandlerCommand() {
        return BotCommand.SETTINGS;
    }

    private Object[] getBaseUserSubscriptions(long chatId) {
        User user = userService.findUserByIdOrCreateNewUser(chatId);
        Set<ObjectType> userObjectTypes = user.getObjectTypes();

        List<String> result = new ArrayList<>();
        result.add(userObjectTypes.contains(objectTypeService.getObjectType("News")) ? "On" : "Off");
        result.add(userObjectTypes.contains(objectTypeService.getObjectType("Article")) ? "On" : "Off");
        result.add(userObjectTypes.contains(objectTypeService.getObjectType("Journal")) ? "On" : "Off");
        result.add(userObjectTypes.contains(objectTypeService.getObjectType("Thought")) ? "On" : "Off");
        result.add(userObjectTypes.contains(objectTypeService.getObjectType("Comment")) ? "On" : "Off");
        return result.toArray();
    }

    public SendMessage setInlineButtons(SendMessage sendMessage) {
        User user = userService.findUserByIdOrCreateNewUser(Long.parseLong(sendMessage.getChatId()));
        Set<ObjectType> userObjectTypes = user.getObjectTypes();
        InlineKeyboardButton buttonNews = !userObjectTypes.contains(objectTypeService.getObjectType("News")) ?
                new InlineKeyboardButton("News: On", null, "newsOn", null, null, null, null, null) :
                new InlineKeyboardButton("News: Off", null, "newsOff", null, null, null, null, null);

        InlineKeyboardButton buttonArticle = !userObjectTypes.contains(objectTypeService.getObjectType("Article")) ?
                new InlineKeyboardButton("Article: On", null, "articleOn", null, null, null, null, null) :
                new InlineKeyboardButton("Article: Off", null, "articleOff", null, null, null, null, null);

        InlineKeyboardButton buttonJournal = !userObjectTypes.contains(objectTypeService.getObjectType("Journal")) ?
                new InlineKeyboardButton("Journal: On", null, "journalOn", null, null, null, null, null) :
                new InlineKeyboardButton("Journal: Off", null, "journalOff", null, null, null, null, null);

        InlineKeyboardButton buttonThought = !userObjectTypes.contains(objectTypeService.getObjectType("Thought")) ?
                new InlineKeyboardButton("Thought: On", null, "thoughtOn", null, null, null, null, null) :
                new InlineKeyboardButton("Thought: Off", null, "thoughtOff", null, null, null, null, null);
        InlineKeyboardButton buttonComment = !userObjectTypes.contains(objectTypeService.getObjectType("Comment")) ?
                new InlineKeyboardButton("Comment: On", null, "commentOn", null, null, null, null, null) :
                new InlineKeyboardButton("Comment: Off", null, "commentOff", null, null, null, null, null);
    InlineKeyboardButton buttonGame = !userObjectTypes.contains(objectTypeService.getObjectType("Game")) ?
                new InlineKeyboardButton("Game: On", null, "gameOn", null, null, null, null, null) :
                new InlineKeyboardButton("Game: Off", null, "gameOff", null, null, null, null, null);

        List<InlineKeyboardButton> keyboardRow1 = new ArrayList<>();
        keyboardRow1.add(buttonNews);
        keyboardRow1.add(buttonArticle);
        keyboardRow1.add(buttonGame);
        List<InlineKeyboardButton> keyboardRow2 = new ArrayList<>();
        keyboardRow2.add(buttonJournal);
        keyboardRow2.add(buttonThought);
        keyboardRow2.add(buttonComment);

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(keyboardRow1);
        keyboard.add(keyboardRow2);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup(keyboard);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        return sendMessage;
    }
}
