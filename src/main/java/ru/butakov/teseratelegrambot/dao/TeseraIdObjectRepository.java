package ru.butakov.teseratelegrambot.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.butakov.teseratelegrambot.entity.TeseraIdObject;

import java.util.Optional;

public interface TeseraIdObjectRepository extends JpaRepository<TeseraIdObject, Integer> {
    Optional<TeseraIdObject> findTopByTeseraIdIsNotNull();
}
