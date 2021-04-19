package ru.butakov.teseratelegrambot.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.butakov.teseratelegrambot.entity.Journal;


@Component
public class JournalModel {
    @Autowired
    RestTemplate restTemplate;

    @Value("${tesera.api.journals}")
    String teseraApiJournalUrl;

    @Value("${tesera.journal}")
    String teseraJournalURL;

    public String getJournalUrl(String alias) {
        ResponseEntity<Journal> response = restTemplate.exchange(
                teseraApiJournalUrl + alias,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });

        String result = null;
        if (response.hasBody()) {
            Journal journal = response.getBody();
            result = String.format(teseraJournalURL, journal.getAuthor().getLogin(), alias);
        }
        return result;
    }

}
