package ru.butakov.teseratelegrambot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.butakov.teseratelegrambot.dao.GameRepository;
import ru.butakov.teseratelegrambot.entity.Game;

import java.util.Optional;

@Service
public class GameService {
    @Autowired
    GameRepository gameRepository;

    public void saveGameIfEmpty(Game game) {
        Optional<Game> gameFromDb = gameRepository.findByTeseraId(game.getTeseraId());
        if (gameFromDb.isEmpty()) gameRepository.save(game);
    }

    public Optional<Game> findByTeseraId(int gameId) {
        Optional<Game> gameFromDb = gameRepository.findByTeseraId(gameId);
//        Game game = gameFromDb.orElse(new Game(gameId));
//        if (gameFromDb.isEmpty()) {
//            gameRepository.save(game);
//        }
        return gameFromDb;
    }
}
