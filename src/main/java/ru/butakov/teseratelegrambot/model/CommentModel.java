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
import ru.butakov.teseratelegrambot.service.ReplyMessageService;
import ru.butakov.teseratelegrambot.utils.Emojis;

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
    ReplyMessageService replyMessageService;

    @Autowired
    JournalModel journalModel;

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
        String type = comment.getCommentObject().getObjectType();
        String alias = comment.getCommentObject().getAlias();
        int teseraId = comment.getTeseraId();
        String userLogin = comment.getAuthor().getLogin();

        if (type.equals(teseraObjectTypeArticle))
            comment.setUrl(String.format(teseraArticleCommentUrl, alias, teseraId));
        if (type.equals(teseraObjectTypeNews))
            comment.setUrl(String.format(teseraNewsCommentUrl, alias, teseraId));
        if (type.equals(teseraObjectTypeThought))
            comment.setUrl(String.format(teseraThoughtCommentUrl, alias, teseraId));
        if (type.equals(teseraObjectTypeGame))
            comment.setUrl(String.format(teseraGameCommentUrl, alias, teseraId));
        if (type.equals(teseraObjectTypeJournal))
            comment.setUrl(String.format(teseraJournalCommentUrl, userLogin, alias, teseraId));

//        switch (comment.getCommentObject().getObjectType()) {
//            case "Article" -> comment.setUrl(String.format(teseraArticleCommentUrl, comment.getCommentObject().getAlias(), comment.getTeseraId()));
//            case "News" -> comment.setUrl(String.format(teseraNewsCommentUrl, comment.getCommentObject().getAlias(), comment.getTeseraId()));
//            case "Journal" -> comment.setUrl(journalModel.getJournalUrl(comment.getCommentObject().getAlias()) + "/comments/#post" + comment.getTeseraId());
//            case "Thought" -> comment.setUrl(String.format(teseraThoughtCommentUrl, comment.getCommentObject().getAlias(), comment.getTeseraId()));
//            case "Game" -> comment.setUrl(String.format(teseraGameCommentUrl, comment.getCommentObject().getAlias(), comment.getTeseraId()));
//            default -> log.warn("Unknown publication type: " + comment.toString());
//        }
    }

    public String getCommentMessage(Comment comment) {
        String commentHead = "";
        String type = comment.getCommentObject().getObjectType();
        if (type.equals(teseraObjectTypeArticle))
            commentHead = replyMessageService.getMessage("reply.comment.article", new Object[]{Emojis.COMMENT.toString(), comment.getCommentObject().getTitle()});
        if (type.equals(teseraObjectTypeNews))
            commentHead = replyMessageService.getMessage("reply.comment.news", new Object[]{Emojis.COMMENT.toString(), comment.getCommentObject().getTitle()});
        if (type.equals(teseraObjectTypeThought))
            commentHead = replyMessageService.getMessage("reply.comment.thought", new Object[]{Emojis.COMMENT.toString(), comment.getCommentObject().getTitle()});
        if (type.equals(teseraObjectTypeGame))
            commentHead = replyMessageService.getMessage("reply.comment.game", new Object[]{Emojis.COMMENT.toString(), comment.getCommentObject().getTitle()});
        if (type.equals(teseraObjectTypeJournal))
            commentHead = replyMessageService.getMessage("reply.comment.journal", new Object[]{Emojis.COMMENT.toString(), comment.getCommentObject().getTitle()});

//
//                    String commentHead = switch (comment.getCommentObject().getObjectType()) {
//                            case "Article" -> replyMessageService.getMessage("reply.comment.article", new Object[]{Emojis.COMMENT.toString(), comment.getCommentObject().getTitle()});
//                            case "News" -> replyMessageService.getMessage("reply.comment.news", new Object[]{Emojis.COMMENT.toString(), comment.getCommentObject().getTitle()});
//                            case "Journal" -> replyMessageService.getMessage("reply.comment.journal", new Object[]{Emojis.COMMENT.toString(), comment.getCommentObject().getTitle()});
//                            case "Thought" -> replyMessageService.getMessage("reply.comment.thought", new Object[]{Emojis.COMMENT.toString(), comment.getCommentObject().getTitle()});
//                            case "Game" -> replyMessageService.getMessage("reply.comment.game", new Object[]{Emojis.COMMENT.toString(), comment.getCommentObject().getTitle()});
//                            default -> "";
//                        };

        return replyMessageService.getMessage("reply.comment.textbody", new Object[]{
                commentHead,
                comment.getAuthor().getLogin(),
                comment.getContent().replaceAll("<br>", ""),
                comment.getUrl()
        });
    }
}
