package ru.butakov.teseratelegrambot.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ArticleDetail {
    @JsonProperty(value = "article")
    Article article;
    @JsonProperty(value = "author")
    Author author;

    @Override
    public String toString() {
        return article.getTitle() + "\n" +
                (article.getTitle2() != null ? article.getTitle2()+ "\n" : "")  +
                (article.getContentShort() != null ? article.getContentShort()+ "\n" : "")  +
                author.getLogin() + "\n" +
                article.getUrl() + "\n";
    }
}
