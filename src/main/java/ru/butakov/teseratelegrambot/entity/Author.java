package ru.butakov.teseratelegrambot.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;


@NoArgsConstructor
@Getter
@Setter
@ToString(of = {"login"})
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(of = {"teseraId"})
public class Author {
    @JsonProperty
    int teseraId;
    @JsonProperty
    String login;
}
