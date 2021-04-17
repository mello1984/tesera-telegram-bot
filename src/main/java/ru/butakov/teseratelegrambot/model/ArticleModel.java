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

import java.util.ArrayList;
import java.util.List;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ArticleModel {
    @Value("${tesera.article}")
    String teseraArticleURL;
    @Value("${tesera.api.articles}")
    String teseraArticlesURL;
    @Autowired
    RestTemplate restTemplate;

    public List<ArticleDetail> getListArticleDetail() {
        List<ArticleDetail> articleDetailList = new ArrayList<>();

        ResponseEntity<List<Article>> response = restTemplate.exchange(
                teseraArticlesURL,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });

        if (response.hasBody()) {
            List<Article> articles = response.getBody();
            if (articles != null) {
                articles.forEach(a -> articleDetailList.add(getArticleDetail(a)));
            }
        }
        articleDetailList.forEach(a->updateUrl(a.getArticle()));

        return articleDetailList;
    }

    private void updateUrl(Article article) {
        article.setUrl(teseraArticleURL + article.getAlias());
    }

    private ArticleDetail getArticleDetail(Article article) {
        String url = teseraArticlesURL + article.getAlias();
        ResponseEntity<ArticleDetail> response = restTemplate.exchange(
                url,
                HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                });
        return response.getBody();
    }
}
