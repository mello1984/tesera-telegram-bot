package ru.butakov.teseratelegrambot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.butakov.teseratelegrambot.entity.Game;
import ru.butakov.teseratelegrambot.entity.User;
import ru.butakov.teseratelegrambot.model.GameModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class SubscriptionGameService {
    @Autowired
    RestTemplate restTemplate;
    @Value("${tesera.api.gamesearch}")
    String teseraApiGameSearchUrl;
    @Autowired
    GameModel gameModel;
    @Autowired
    UserService userService;
    @Autowired
    GameService gameService;

    public String getSubscribeTextMessage(String query) {
        return getTextMessageFromList(getGamesList(query));
    }

    private List<Game> getGamesList(String query) {
        String url = teseraApiGameSearchUrl + query;
        ResponseEntity<List<Game>> response = restTemplate.exchange(
                url, HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                });
        List<Game> result = new ArrayList<>();
        if (response.hasBody() && response.getBody() != null) result = response.getBody();
        result.forEach(game -> {
            gameModel.updateUrl(game);
            gameService.saveGameIfEmpty(game);
        });
        return result;
    }


    private String getTextMessageFromList(List<Game> games) {
        StringBuilder result = new StringBuilder("Результаты поиска:\n");
        for (Game g : games) {
            result.append(String.format("%s\nСсылка: %s\nПодписаться: %s\n***\n", g.getTitle(), g.getUrl(), "/gameadd_" + g.getTeseraId()));
        }
        return result.toString();
    }

    public String getUnsubscribeTextMessage(long chatId) {
        User user = userService.findUserByIdOrCreateNewUser(chatId);
        Set<Game> gameSet = user.getGames();
        StringBuilder result = new StringBuilder("Результаты поиска:\n");
        for (Game g : gameSet) {
            result.append(String.format("%s\nОтписаться: %s\n***\n", g.getTitle(), "/gameremove_" + g.getTeseraId()));
        }
        return result.toString();
    }
}
