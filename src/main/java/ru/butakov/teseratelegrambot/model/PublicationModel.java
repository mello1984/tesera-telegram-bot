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
import ru.butakov.teseratelegrambot.entity.Publication;
import ru.butakov.teseratelegrambot.service.ReplyMessageService;
import ru.butakov.teseratelegrambot.utils.Emojis;

import java.util.ArrayList;
import java.util.List;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class PublicationModel {
    @Value("${tesera.article}")
    String teseraArticleURL;
    @Value("${tesera.journal}")
    String teseraJournalURL;
    @Value("${tesera.new}")
    String teseraNewsURL;
    @Value("${tesera.thought}")
    String teseraThoughtURL;
    @Value("${tesera.api.publications}")
    String teseraApiPublicationsURL;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    GameModel gameModel;

    @Autowired
    ReplyMessageService replyMessageService;

    public List<Publication> getListPublications() {
        List<Publication> publications = new ArrayList<>();

        ResponseEntity<List<Publication>> response = restTemplate.exchange(
                teseraApiPublicationsURL,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });

        if (response.hasBody()) {
            publications = response.getBody();
        }
        publications.forEach(this::updateUrl);

        return publications;
    }

    private void updateUrl(Publication publication) {
        switch (publication.getObjectType()) {
            case "Article" -> publication.setUrl(teseraArticleURL + publication.getAlias());
            case "News" -> publication.setUrl(teseraNewsURL + publication.getAlias());
            case "Journal" -> publication.setUrl(String.format(teseraJournalURL, publication.getAuthor().getLogin(), publication.getAlias()));
            case "Thought" -> publication.setUrl(teseraThoughtURL + publication.getAlias());
            default -> log.warn("Unknown publication type: " + publication.toString());
        }
    }

    public String getPublicationMessageText(Publication publication) {

        String publicationHead = switch (publication.getObjectType()) {
            case "Article" -> replyMessageService.getMessage("reply.publication.article", new Object[]{Emojis.ARTICLE.toString()});
            case "News" -> replyMessageService.getMessage("reply.publication.news", new Object[]{Emojis.NEWS.toString()});
            case "Journal" -> replyMessageService.getMessage("reply.publication.journal", new Object[]{Emojis.JOURNAL.toString()});
            case "Thought" -> replyMessageService.getMessage("reply.publication.thought", new Object[]{Emojis.THOUGHT.toString()});
            default -> "";
        };

        return replyMessageService.getMessage("reply.publication.textbody", new Object[]{
                publicationHead,
                publication.getTitle(),
                publication.getAuthor().getLogin(),
                gameModel.getGameListMessageText(publication.getGames()),
                publication.getContentShort(),
                publication.getUrl()});
    }

}
