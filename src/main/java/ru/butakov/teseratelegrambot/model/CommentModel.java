package ru.butakov.teseratelegrambot.model;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.butakov.teseratelegrambot.entity.Comment;

import java.util.ArrayList;
import java.util.List;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class CommentModel {
    @Value("${tesera.api.comments}")
    String teseraApiCommentsURL;

    @Value("${tesera.game.comment}")
    String teseraGameCommentUrl;
    @Value("${tesera.article.comment}")
    String teseraArticleCommentUrl;
    @Value("${tesera.news.comment}")
    String teseraNewsCommentUrl;
    @Value("${tesera.thought.comment}")
    String teseraThoughtCommentUrl;
    @Value("${tesera.journal.comment}")
    String teseraJournalCommentUrl;
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    JournalModel journalModel;

    public List<Comment> getListComments() {
        List<Comment> comments = new ArrayList<>();

        ResponseEntity<List<Comment>> response = restTemplate.exchange(
                teseraApiCommentsURL,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });

        if (response.hasBody()) {
            comments = response.getBody();
        }
        comments.forEach(this::updateUrl);

        return comments;
    }


    private void updateUrl(Comment comment) {
        switch (comment.getCommentObject().getObjectType()) {
            case "Article" -> comment.setUrl(String.format(teseraArticleCommentUrl, comment.getCommentObject().getAlias(), comment.getTeseraId()));
            case "News" -> comment.setUrl(String.format(teseraNewsCommentUrl, comment.getCommentObject().getAlias(), comment.getTeseraId()));
            case "Journal" -> comment.setUrl(journalModel.getJournalUrl(comment.getCommentObject().getAlias()) + "/comments/#post" + comment.getTeseraId());
            case "Thought" -> comment.setUrl(String.format(teseraThoughtCommentUrl, comment.getCommentObject().getAlias(), comment.getTeseraId()));
            case "Game" -> comment.setUrl(String.format(teseraGameCommentUrl, comment.getCommentObject().getAlias(), comment.getTeseraId()));
            default -> log.warn("Unknown publication type: " + comment.toString());
        }
    }

    public String getCommentMessage(Comment comment) {
        return comment.toString().replaceAll("<br>", "");

    }
}
