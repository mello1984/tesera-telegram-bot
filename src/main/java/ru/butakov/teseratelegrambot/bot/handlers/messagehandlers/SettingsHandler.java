package ru.butakov.teseratelegrambot.bot.handlers.messagehandlers;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.butakov.teseratelegrambot.entity.ObjectType;
import ru.butakov.teseratelegrambot.entity.User;
import ru.butakov.teseratelegrambot.service.ObjectTypeService;
import ru.butakov.teseratelegrambot.service.UserService;
import ru.butakov.teseratelegrambot.utils.Emojis;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SettingsHandler extends AbstractHandler {
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
        result.add(Emojis.NEWS.toString());
        result.add(userObjectTypes.contains(objectTypeService.getObjectType("News")) ? Emojis.ENABLED.toString() : Emojis.DISABLED.toString());
        result.add(Emojis.ARTICLE.toString());
        result.add(userObjectTypes.contains(objectTypeService.getObjectType("Article")) ? Emojis.ENABLED.toString() : Emojis.DISABLED.toString());
        result.add(Emojis.JOURNAL.toString());
        result.add(userObjectTypes.contains(objectTypeService.getObjectType("Journal")) ? Emojis.ENABLED.toString() : Emojis.DISABLED.toString());
        result.add(Emojis.THOUGHT.toString());
        result.add(userObjectTypes.contains(objectTypeService.getObjectType("Thought")) ? Emojis.ENABLED.toString() : Emojis.DISABLED.toString());
        result.add(Emojis.COMMENT.toString());
        result.add(userObjectTypes.contains(objectTypeService.getObjectType("Game")) ? Emojis.ENABLED.toString() : Emojis.DISABLED.toString());
        result.add(Emojis.COMMENT.toString());
        result.add(userObjectTypes.contains(objectTypeService.getObjectType("Comment")) ? Emojis.ENABLED.toString() : Emojis.DISABLED.toString());
        return result.toArray();
    }

    public SendMessage setInlineButtons(SendMessage sendMessage) {
        User user = userService.findUserByIdOrCreateNewUser(Long.parseLong(sendMessage.getChatId()));
        Set<ObjectType> userObjectTypes = user.getObjectTypes();
        InlineKeyboardButton buttonNews = !userObjectTypes.contains(objectTypeService.getObjectType("News")) ?
                new InlineKeyboardButton("Новости: вкл", null, "newsOn", null, null, null, null, null) :
                new InlineKeyboardButton("Новости: выкл", null, "newsOff", null, null, null, null, null);

        InlineKeyboardButton buttonArticle = !userObjectTypes.contains(objectTypeService.getObjectType("Article")) ?
                new InlineKeyboardButton("Статьи: вкл", null, "articleOn", null, null, null, null, null) :
                new InlineKeyboardButton("Статьи: выкл", null, "articleOff", null, null, null, null, null);

        InlineKeyboardButton buttonJournal = !userObjectTypes.contains(objectTypeService.getObjectType("Journal")) ?
                new InlineKeyboardButton("Журналы: вкл", null, "journalOn", null, null, null, null, null) :
                new InlineKeyboardButton("Журналы: выкл", null, "journalOff", null, null, null, null, null);

        InlineKeyboardButton buttonThought = !userObjectTypes.contains(objectTypeService.getObjectType("Thought")) ?
                new InlineKeyboardButton("Мысли: вкл", null, "thoughtOn", null, null, null, null, null) :
                new InlineKeyboardButton("Мысли: выкл", null, "thoughtOff", null, null, null, null, null);
        InlineKeyboardButton buttonComment = !userObjectTypes.contains(objectTypeService.getObjectType("Comment")) ?
                new InlineKeyboardButton("Комм.(все): вкл", null, "commentOn", null, null, null, null, null) :
                new InlineKeyboardButton("Комм.(все): выкл", null, "commentOff", null, null, null, null, null);
        InlineKeyboardButton buttonGame = !userObjectTypes.contains(objectTypeService.getObjectType("Game")) ?
                new InlineKeyboardButton("Игры: вкл", null, "gameOn", null, null, null, null, null) :
                new InlineKeyboardButton("Игры: выкл", null, "gameOff", null, null, null, null, null);

        List<InlineKeyboardButton> keyboardRow1 = new ArrayList<>();
        keyboardRow1.add(buttonNews);
        keyboardRow1.add(buttonArticle);
        keyboardRow1.add(buttonJournal);

        List<InlineKeyboardButton> keyboardRow2 = new ArrayList<>();
        keyboardRow2.add(buttonThought);
        keyboardRow2.add(buttonGame);
        keyboardRow2.add(buttonComment);

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(keyboardRow1);
        keyboard.add(keyboardRow2);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup(keyboard);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        return sendMessage;
    }
}
