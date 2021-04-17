package ru.butakov.teseratelegrambot.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewsDetail {
    @JsonProperty(value = "news")
    News news;
    @JsonProperty(value = "author")
    Author author;

    @Override
    public String toString() {
        return news.getTitle() + "\n" +
                (news.getTitle2() != null ? news.getTitle2() + "\n" : "") +
                (news.getContentShort() != null ? news.getContentShort() + "\n" : "") +
                author.getLogin() + "\n" +
                news.getUrl() + "\n";
    }
}
