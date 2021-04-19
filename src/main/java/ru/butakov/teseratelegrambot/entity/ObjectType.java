package ru.butakov.teseratelegrambot.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "object_types")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@EqualsAndHashCode(of = {"objectTypeId"})
@ToString
public class ObjectType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int objectTypeId;

    @Column(unique = true)
    String type;

    @ManyToMany(mappedBy = "objectTypes")
    Set<User> userSet = new HashSet<>();

    public ObjectType(String type) {
        this.type = type;
    }
}
