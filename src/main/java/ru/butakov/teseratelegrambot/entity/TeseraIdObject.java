package ru.butakov.teseratelegrambot.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "tesera_id")
@Getter
@Setter
@NoArgsConstructor
public class TeseraIdObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column
    int teseraId;
}
