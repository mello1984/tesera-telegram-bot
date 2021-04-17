package ru.butakov.teseratelegrambot.model;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.butakov.teseratelegrambot.entity.Article;
import ru.butakov.teseratelegrambot.entity.ArticleDetail;
import ru.butakov.teseratelegrambot.entity.News;
import ru.butakov.teseratelegrambot.entity.NewsDetail;

import java.util.ArrayList;
import java.util.List;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewsModel {
    @Value("${tesera.new}")
    String teseraNewsURL;
    @Value("${tesera.api.news}")
    String teseraApiNewsURL;
    @Autowired
    RestTemplate restTemplate;

    public List<NewsDetail> getListNewsDetail() {
        List<NewsDetail> newsDetailList = new ArrayList<>();

        ResponseEntity<List<News>> response = restTemplate.exchange(
                teseraApiNewsURL,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });

        if (response.hasBody()) {
            List<News> newsList = response.getBody();
            if (newsList != null) {
                newsList.forEach(a -> newsDetailList.add(getNewsDetail(a)));
            }
        }
        newsDetailList.forEach(a->updateUrl(a.getNews()));

        return newsDetailList;
    }

    private void updateUrl(News news) {
        news.setUrl(teseraNewsURL + news.getAlias());
    }

    private NewsDetail getNewsDetail(News news) {
        String url = teseraApiNewsURL + news.getAlias();
        ResponseEntity<NewsDetail> response = restTemplate.exchange(
                url,
                HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                });
        return response.getBody();
    }
}
