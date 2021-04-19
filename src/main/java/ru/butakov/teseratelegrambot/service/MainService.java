package ru.butakov.teseratelegrambot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    public List<Publication> getPublicationList() {
        return publicationModel.getListPublications();
    }

    public List<Comment> getCommentList() {
        return commentModel.getListComments();
    }
}
