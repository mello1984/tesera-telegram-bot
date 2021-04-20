package ru.butakov.teseratelegrambot.bot.handlers.messagehandlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.butakov.teseratelegrambot.TeseraTelegramBotApplication;
import ru.butakov.teseratelegrambot.bot.SendMessageFormat;
import ru.butakov.teseratelegrambot.bot.WebHookTeseraBot;
import ru.butakov.teseratelegrambot.entity.*;
import ru.butakov.teseratelegrambot.model.CommentModel;
import ru.butakov.teseratelegrambot.model.PublicationModel;
import ru.butakov.teseratelegrambot.service.*;

import java.util.List;

@Component
public class GetHandler implements InputMessageHandler {
    @Autowired
    ReplyMessageService replyMessageService;
    @Autowired
    SendMessageFormat sendMessageFormat;

    @Autowired
    WebHookTeseraBot teseraBot;
    @Autowired
    MainService mainService;
    @Autowired
    UserService userService;
    @Autowired
    ObjectTypeService objectTypeService;
    @Autowired
    PublicationModel publicationModel;
    @Autowired
    CommentModel commentModel;
    @Autowired
    TeseraIdObjectService teseraIdObjectService;
    @Autowired
    MessageSenderService messageSenderService;

    @Override
    public SendMessage handle(Message message) {
        sendPublicationsAndComments(message);
        return null;
    }

    @Override
    public BotCommand getHandlerCommand() {
        return BotCommand.GET;
    }

    public void sendPublicationsAndComments(Message message) {
        int TEMP_SHIFT = 25; // for test only

        TeseraIdObject teseraIdObject = teseraIdObjectService.findTopByTeseraId();
        int maxTeseraId = teseraIdObject.getTeseraId();

        User user = userService.findUserById(message.getChatId());
        List<Publication> publicationList = mainService.getPublicationList();
        for (Publication p : publicationList) {
//            if (p.getObjectId() <= maxTeseraId-TEMP_SHIFT) continue;
            maxTeseraId = p.getObjectId();
            SendMessage sendMessage = sendMessageFormat.getSendMessageBaseFormat(user.getChatId());
            sendMessage.setText("PUBLICATION: " + publicationModel.getPublicationMessageText(p));
            teseraBot.send(sendMessage);

        }

        List<Comment> comments = mainService.getCommentList();
        for (Comment c : comments) {
            if (c.getTeseraId() <= maxTeseraId-TEMP_SHIFT) continue;
            maxTeseraId = c.getTeseraId();
            SendMessage sendMessage = sendMessageFormat.getSendMessageBaseFormat(user.getChatId());
            sendMessage.setText("COMMENT: " + commentModel.getCommentMessage(c));
            messageSenderService.offerBotApiMethodToQueue(sendMessage);
        }
        teseraIdObject.setTeseraId(maxTeseraId);
        teseraIdObjectService.saveTeseraIdObject(teseraIdObject);
    }
}
