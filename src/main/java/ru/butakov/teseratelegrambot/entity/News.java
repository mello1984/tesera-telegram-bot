package ru.butakov.teseratelegrambot.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class News {
    @JsonProperty
    int teseraId;
    @JsonProperty
    String alias;
    @JsonProperty
    String title;
    @JsonProperty
    String title2;
    @JsonProperty
    String contentShort;
    String url;
}
