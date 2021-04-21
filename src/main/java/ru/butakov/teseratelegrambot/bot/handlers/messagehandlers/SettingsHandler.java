package ru.butakov.teseratelegrambot.bot.handlers.messagehandlers;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SettingsHandler extends AbstractHandler {
    @Autowired
    UserService userService;
    @Autowired
    ObjectTypeService objectTypeService;
    @Value("${tesera.object.type.news}")
    String teseraObjectTypeNews;
    @Value("${tesera.object.type.article}")
    String teseraObjectTypeArticle;
    @Value("${tesera.object.type.journal}")
    String teseraObjectTypeJournal;
    @Value("${tesera.object.type.thought}")
    String teseraObjectTypeThought;
    @Value("${tesera.object.type.game}")
    String teseraObjectTypeGame;
    @Value("${tesera.object.type.comment}")
    String teseraObjectTypeComment;

    @Override
    public SendMessage handle(Message message) {
        String replyText = replyMessageService.getMessage("reply.settings", getBaseUserSubscriptionsTextLine(message.getChatId()));
        SendMessage replyMessage = sendMessageFormat.getSendMessageBaseFormat(message.getChatId());
        replyMessage.setText(replyText);
        return setInlineButtons(replyMessage);
    }

    @Override
    public BotCommand getHandlerCommand() {
        return BotCommand.SETTINGS;
    }

    private Object[] getBaseUserSubscriptionsTextLine(long chatId) {
        User user = userService.findUserByIdOrCreateNewUser(chatId);
        Set<ObjectType> userObjectTypes = user.getObjectTypes();

        List<String> resultList = new ArrayList<>();
        addSubscriptionStringToResultList(userObjectTypes, resultList, teseraObjectTypeNews, Emojis.NEWS);
        addSubscriptionStringToResultList(userObjectTypes, resultList, teseraObjectTypeArticle, Emojis.ARTICLE);
        addSubscriptionStringToResultList(userObjectTypes, resultList, teseraObjectTypeJournal, Emojis.JOURNAL);
        addSubscriptionStringToResultList(userObjectTypes, resultList, teseraObjectTypeThought, Emojis.THOUGHT);
        addSubscriptionStringToResultList(userObjectTypes, resultList, teseraObjectTypeGame, Emojis.COMMENT);
        addSubscriptionStringToResultList(userObjectTypes, resultList, teseraObjectTypeComment, Emojis.COMMENT);

        return resultList.toArray();
    }

    private void addSubscriptionStringToResultList(Set<ObjectType> userObjectTypes, List<String> result, String type, Emojis emoji) {
        result.add(emoji.toString());
        result.add(userObjectTypes.contains(objectTypeService.getObjectType(type)) ? Emojis.ENABLED.toString() : Emojis.DISABLED.toString());
    }

    private SendMessage setInlineButtons(SendMessage sendMessage) {
        User user = userService.findUserByIdOrCreateNewUser(Long.parseLong(sendMessage.getChatId()));
        Set<ObjectType> userObjectTypes = user.getObjectTypes();
        InlineKeyboardButton buttonNews = getInlineKeyboardButton(userObjectTypes, teseraObjectTypeNews, replyMessageService.getMessage("reply.type.news"));
        InlineKeyboardButton buttonArticle = getInlineKeyboardButton(userObjectTypes, teseraObjectTypeArticle, replyMessageService.getMessage("reply.type.article"));
        InlineKeyboardButton buttonJournal = getInlineKeyboardButton(userObjectTypes, teseraObjectTypeJournal, replyMessageService.getMessage("reply.type.journal"));
        InlineKeyboardButton buttonThought = getInlineKeyboardButton(userObjectTypes, teseraObjectTypeThought, replyMessageService.getMessage("reply.type.thought"));
        InlineKeyboardButton buttonComment = getInlineKeyboardButton(userObjectTypes, teseraObjectTypeComment, replyMessageService.getMessage("reply.type.comment"));
        InlineKeyboardButton buttonGame = getInlineKeyboardButton(userObjectTypes, teseraObjectTypeNews, replyMessageService.getMessage("reply.type.game"));

        List<InlineKeyboardButton> keyboardRow1 = new ArrayList<>(Arrays.asList(buttonNews, buttonArticle, buttonJournal));
        List<InlineKeyboardButton> keyboardRow2 = new ArrayList<>(Arrays.asList(buttonThought, buttonGame, buttonComment));
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>(Arrays.asList(keyboardRow1, keyboardRow2));

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup(keyboard);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        return sendMessage;
    }

    private InlineKeyboardButton getInlineKeyboardButton(Set<ObjectType> userObjectTypes, String type, String buttonText) {
        return !userObjectTypes.contains(objectTypeService.getObjectType(type)) ?
                new InlineKeyboardButton(buttonText + ": вкл", null, type + "_On", null, null, null, null, null) :
                new InlineKeyboardButton(buttonText + ": выкл", null, type + "_Off", null, null, null, null, null);
    }
}
