package ru.butakov.teseratelegrambot.service;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.hk2.api.messaging.MessageReceiver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.butakov.teseratelegrambot.TeseraTelegramBotApplication;
import ru.butakov.teseratelegrambot.bot.SendMessageFormat;
import ru.butakov.teseratelegrambot.bot.WebHookTeseraBot;
import ru.butakov.teseratelegrambot.bot.handlers.messagehandlers.BotCommand;
import ru.butakov.teseratelegrambot.entity.*;
import ru.butakov.teseratelegrambot.model.CommentModel;
import ru.butakov.teseratelegrambot.model.PublicationModel;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateService {

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
    @Autowired
    GameService gameService;

    @Scheduled(fixedRateString = "${tesera.updateperiod}")
    public void updateTask() {
        sendPublicationsAndComments();
        log.info("UpdateTask executed");
    }


    public void sendPublicationsAndComments() {
        int TEMP_SHIFT = 0; // for test only

        TeseraIdObject teseraIdObject = teseraIdObjectService.findTopByTeseraId();
        int maxTeseraId = teseraIdObject.getTeseraId();

        Map<String, ObjectType> objectTypeMap = objectTypeService.getMapObjectTypes();

        List<Publication> publicationList = mainService.getPublicationList();
        for (Publication p : publicationList) {
            if (p.getObjectId() <= teseraIdObject.getTeseraId() - TEMP_SHIFT) continue;
            maxTeseraId = Math.max(maxTeseraId, p.getObjectId());

            for (User user : objectTypeMap.get(p.getObjectType()).getUserSet()) {
                SendMessage sendMessage = sendMessageFormat.getSendMessageBaseFormat(user.getChatId());
                sendMessage.setText(publicationModel.getPublicationMessageText(p));
                messageSenderService.offerBotApiMethodToQueue(sendMessage);
            }
        }

        List<Comment> comments = mainService.getCommentList();
        Set<User> subscribedOnComments = objectTypeMap.get("Comment").getUserSet();
        for (Comment c : comments) {
            if (c.getTeseraId() <= teseraIdObject.getTeseraId() - TEMP_SHIFT) continue;
            maxTeseraId = Math.max(maxTeseraId, c.getTeseraId());

            Game game = null;
            if (c.getCommentObject().getObjectType().equals("Game")) {
                Optional<Game> gameFromDb = gameService.findByTeseraId(c.getCommentObject().getTeseraId());
                game = gameFromDb.orElse(null);
            }

            Set<User> subscribedOnObjectType = objectTypeMap.get(c.getCommentObject().getObjectType()).getUserSet();
            for (User user : subscribedOnObjectType) {

                if (!subscribedOnComments.contains(user) && !user.getGames().contains(game)) continue;
                SendMessage sendMessage = sendMessageFormat.getSendMessageBaseFormat(user.getChatId());
                sendMessage.setText(commentModel.getCommentMessage(c));
                messageSenderService.offerBotApiMethodToQueue(sendMessage);
            }
        }
        teseraIdObject.setTeseraId(maxTeseraId);
        teseraIdObjectService.saveTeseraIdObject(teseraIdObject);
    }


}
