package ru.butakov.teseratelegrambot.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@ToString(of = {"objectType", "title", "author", "games", "url"})
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(of = {"objectId"})
public class Publication {
    @JsonProperty
    int objectId;
    @JsonProperty
    ObjectType objectType;
    @JsonProperty
    String title;
    @JsonProperty
    String alias;
    @JsonProperty
    String contentShort;
    @JsonProperty
    Author author;
    @JsonProperty
    List<Game> games;
    String url;
}
