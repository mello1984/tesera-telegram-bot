package ru.butakov.teseratelegrambot.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.butakov.teseratelegrambot.entity.Game;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class GameModel {
    @Value("${tesera.game}")
    String teseraGameUrl;

    public void updateUrl(Game game) {
        game.setUrl(teseraGameUrl + game.getAlias());
    }

    public String getGameMessageText(Game game) {
        return game.getTitle();
    }

    public String getGameListMessageText(List<Game> games) {
        if (games == null || games.size() == 0) return "-";
        if (games.size()==1) return games.get(0).getTitle();
        return games.stream()
                .sorted(Comparator.comparing(Game::getTitle))
                .map(this::getGameMessageText)
                .collect(Collectors.joining("\n- ", "\n- ", ""));
    }
}
