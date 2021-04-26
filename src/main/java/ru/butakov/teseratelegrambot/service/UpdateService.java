package ru.butakov.teseratelegrambot.service;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.butakov.teseratelegrambot.bot.SendMessageFormat;
import ru.butakov.teseratelegrambot.bot.WebHookTeseraBot;
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
    MainService mainService;
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

    @Value("${tesera.object.type.game}")
    String teseraObjectTypeGame;
    @Value("${tesera.object.type.comment}")
    String teseraObjectTypeComment;

    @Scheduled(fixedRateString = "${tesera.updateperiod}")
    public void updateTask() {
        int maxTeseraId = sendPublicationsAndCommentsAndGetMaxTeseraId();
        log.info("UpdateTask executed, maxTeseraId = {}", maxTeseraId);
    }


    public int sendPublicationsAndCommentsAndGetMaxTeseraId() {
        TeseraIdObject teseraIdObject = teseraIdObjectService.findTopByTeseraId();
        Map<String, ObjectType> objectTypeMap = objectTypeService.getMapObjectTypes();

        int maxTeseraId = teseraIdObject.getTeseraId();
        maxTeseraId = sendPublicationsAndGetMaxTeseraId(teseraIdObject, maxTeseraId, objectTypeMap);
        maxTeseraId = sendCommentsAndGetMaxTeseraId(teseraIdObject, maxTeseraId, objectTypeMap);

        teseraIdObject.setTeseraId(maxTeseraId);
        teseraIdObjectService.saveTeseraIdObject(teseraIdObject);
        return maxTeseraId;
    }

    private int sendCommentsAndGetMaxTeseraId(TeseraIdObject teseraIdObject, int maxTeseraId, Map<String, ObjectType> objectTypeMap) {
        List<Comment> comments = mainService.getCommentList();
        Set<User> subscribedOnComments = objectTypeMap.get(teseraObjectTypeComment).getUserSet();
        for (Comment c : comments) {
            if (c.getTeseraId() <= teseraIdObject.getTeseraId()) continue;
            maxTeseraId = Math.max(maxTeseraId, c.getTeseraId());

            if (c.getCommentObject() == null) continue;
            Game game = null;
            if (c.getCommentObject().getObjectType().equals(teseraObjectTypeGame)) {
                game = gameService.findByTeseraId(c.getCommentObject().getTeseraId()).orElse(null);
            }

            Set<User> subscribedOnObjectType = objectTypeMap.get(c.getCommentObject().getObjectType()).getUserSet();
            for (User user : subscribedOnObjectType) {
                if (!subscribedOnComments.contains(user) && !user.getGames().contains(game)) continue;
                SendMessage sendMessage = sendMessageFormat.getSendMessageBaseFormat(user.getChatId());
                sendMessage.setText(commentModel.getCommentMessage(c));
                messageSenderService.offerBotApiMethodToQueue(sendMessage);
            }
        }
        return maxTeseraId;
    }

    private int sendPublicationsAndGetMaxTeseraId(TeseraIdObject teseraIdObject, int maxTeseraId, Map<String, ObjectType> objectTypeMap) {
        List<Publication> publicationList = mainService.getPublicationList();
        for (Publication p : publicationList) {
            if (p.getObjectId() <= teseraIdObject.getTeseraId()) continue;
            maxTeseraId = Math.max(maxTeseraId, p.getObjectId());

            for (User user : objectTypeMap.get(p.getObjectType()).getUserSet()) {
                SendMessage sendMessage = sendMessageFormat.getSendMessageBaseFormat(user.getChatId());
                sendMessage.setText(publicationModel.getPublicationMessageText(p));
                messageSenderService.offerBotApiMethodToQueue(sendMessage);
            }
        }
        return maxTeseraId;
    }


}
