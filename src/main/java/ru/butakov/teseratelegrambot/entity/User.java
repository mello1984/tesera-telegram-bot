package ru.butakov.teseratelegrambot.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = {"chatId"})
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int userId;

    @Column(unique = true)
    long chatId;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_object_types",
            joinColumns = {@JoinColumn(name = "userId")},
            inverseJoinColumns = {@JoinColumn(name = "objectTypeId")}
    )
    Set<ObjectType> objectTypes = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
            @JoinTable(name = "users_games",
            joinColumns = {@JoinColumn(name = "userId")},
            inverseJoinColumns = {@JoinColumn(name = "gameId")})
    Set<Game> games = new HashSet<>();

    public User(long chatId) {
        this.chatId = chatId;
    }


}
