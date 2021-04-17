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
import ru.butakov.teseratelegrambot.entity.Publication;

import java.util.ArrayList;
import java.util.List;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class CommentModel {
    @Value("${tesera.article}")
    String teseraArticleURL;
    @Value("${tesera.journal}")
    String teseraJournalURL;
    @Value("${tesera.new}")
    String teseraNewsURL;
    @Value("${tesera.thought}")
    String teseraThoughtURL;
    @Value("${tesera.api.comments}")
    String teseraApiCommentsURL;
    @Autowired
    RestTemplate restTemplate;

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
//        comments.forEach(this::updateUrl);

        return comments;
    }



//    private void updateUrl(Comment comment) {
//        switch (comment.getCommentObject().getObjectType()) {
//            case Article -> comment.setUrl(teseraArticleURL + comment.getAlias());
//            case News -> comment.setUrl(teseraNewsURL + comment.getAlias());
//            case Journal -> comment.setUrl(String.format(teseraJournalURL, comment.getAuthor().getLogin(), comment.getAlias()));
//            case Thought -> comment.setUrl(teseraThoughtURL + comment.getAlias());
//            default -> log.warn("Unknown publication type: " + comment.toString());
//        }
//    }
}
