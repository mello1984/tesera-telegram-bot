package ru.butakov.teseratelegrambot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.butakov.teseratelegrambot.entity.Article;
import ru.butakov.teseratelegrambot.entity.Publication;
import ru.butakov.teseratelegrambot.model.ArticleModel;
import ru.butakov.teseratelegrambot.model.CommentModel;
import ru.butakov.teseratelegrambot.model.NewsModel;
import ru.butakov.teseratelegrambot.model.PublicationModel;

import java.util.List;

@Service
public class MainService {
    @Autowired
    RestTemplate restTemplate;
    @Value("${tesera.api.main}")
    String teseraLink;
    @Value("${tesera.api.articles}")
    String teseraArticlesLink;
    @Autowired
    ArticleModel articleModel;
    @Autowired
    NewsModel newsModel;
    @Autowired
    PublicationModel publicationModel;
    @Autowired
    CommentModel commentModel;

//    public void getArticles() {
//        articleModel.getListArticleDetail().forEach(System.out::println);
//    }
//
//    public void getNews() {
//        newsModel.getListNewsDetail().forEach(System.out::println);
//    }
//
//    public void getPublications() {
//        publicationModel.getListPublications().forEach(System.out::println);
//    }
//
//    public void getComments(){
//        commentModel.getListComments().forEach(System.out::println);
//    }

    public List<Publication> getPublicationList(){
        return publicationModel.getListPublications();
    }

}
