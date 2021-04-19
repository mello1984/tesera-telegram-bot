package ru.butakov.teseratelegrambot.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.butakov.teseratelegrambot.entity.ObjectType;

import java.util.Optional;

public interface ObjectTypeRepository extends JpaRepository<ObjectType, Integer> {
    Optional<ObjectType> findByType(String type);
}
