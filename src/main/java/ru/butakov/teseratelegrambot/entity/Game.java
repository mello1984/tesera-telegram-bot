package ru.butakov.teseratelegrambot.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@ToString(of={"title"})
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Game {
    @JsonProperty
    int teseraId;
    @JsonProperty
    String title;
    @JsonProperty
    String title2;
    @JsonProperty
    String title3;
    @JsonProperty
    String alias;
    @JsonProperty
    String url;
}
