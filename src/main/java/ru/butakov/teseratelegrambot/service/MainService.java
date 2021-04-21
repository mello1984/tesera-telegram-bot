package ru.butakov.teseratelegrambot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.butakov.teseratelegrambot.bot.TelegramFacade;
import ru.butakov.teseratelegrambot.entity.Comment;
import ru.butakov.teseratelegrambot.entity.Publication;
import ru.butakov.teseratelegrambot.model.CommentModel;
import ru.butakov.teseratelegrambot.model.PublicationModel;

import java.util.List;

@Service
public class MainService {
    @Autowired
    PublicationModel publicationModel;
    @Autowired
    CommentModel commentModel;
    @Autowired
    TelegramFacade telegramFacade;

    public BotApiMethod<?> onUpdateReceived(Update update) {
        return telegramFacade.handleUpdate(update);
    }

    public List<Publication> getPublicationList() {
        return publicationModel.getListPublications();
    }

    public List<Comment> getCommentList() {
        return commentModel.getListComments();
    }
}
