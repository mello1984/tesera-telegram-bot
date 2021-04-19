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
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommentObject {
    @JsonProperty
    int teseraId;
    @JsonProperty
    String alias;
    @JsonProperty
    String title;
    @JsonProperty
    String objectType;
//    ObjectType objectType;
}
