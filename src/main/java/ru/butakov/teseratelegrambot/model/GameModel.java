package ru.butakov.teseratelegrambot.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.butakov.teseratelegrambot.entity.Game;

@Component
public class GameModel {
    @Value("${tesera.game}")
    String teseraGameUrl;

    public void updateUrl(Game game) {
        game.setUrl(teseraGameUrl + game.getAlias());
    }
}
