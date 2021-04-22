package ru.butakov.teseratelegrambot.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.butakov.teseratelegrambot.entity.TeseraIdObject;

public interface TeseraIdObjectRepository extends JpaRepository<TeseraIdObject, Integer> {
    TeseraIdObject findTopByTeseraIdIsNotNull();
}
