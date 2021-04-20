package ru.butakov.teseratelegrambot.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@Getter
@Setter
@ToString(of = {"title"})
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "games")
@EqualsAndHashCode(of = {"teseraId"})
@NoArgsConstructor
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int gameId;
    @Column(unique = true)
    @JsonProperty
    int teseraId;
    @Column
    @JsonProperty
    String title;
    @Transient
    @JsonProperty
    String title2;
    @Transient
    @JsonProperty
    String title3;
    @Transient
    @JsonProperty
    String alias;
    @Transient
    @JsonProperty
    String url;

    public Game(int teseraId) {
        this.teseraId = teseraId;
    }
}
