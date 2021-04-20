package ru.butakov.teseratelegrambot.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.butakov.teseratelegrambot.entity.Game;

import java.util.Optional;

public interface GameRepository extends JpaRepository<Game, Integer> {
    public Optional<Game> findByTeseraId(int gameId);

}
