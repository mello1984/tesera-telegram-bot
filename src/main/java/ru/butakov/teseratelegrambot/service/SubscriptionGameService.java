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
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    @Autowired
    ReplyMessageService replyMessageService;

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
        StringBuilder result = new StringBuilder(replyMessageService.getMessage("reply.search.results.head"));
        for (Game g : games) {
            result.append(replyMessageService.getMessage("reply.search.subscribe",
                    new Object[]{g.getTitle(), g.getUrl(), "/gameadd_" + g.getTeseraId()}));
        }
        return result.toString();
    }

    public String getUnsubscribeTextMessage(long chatId) {
        User user = userService.findUserByIdOrCreateNewUser(chatId);
        Set<Game> gameSet = user.getGames();
        StringBuilder result = new StringBuilder(replyMessageService.getMessage("reply.search.results.head"));
        for (Game g : gameSet) {
            result.append(replyMessageService.getMessage("reply.search.unsubscribe",
                    new Object[]{g.getTitle(), "/gameremove_" + g.getTeseraId()}));
        }
        return result.toString();
    }

    public List<String> getListUnsubscribeTextMessage(long chatId) {

        User user = userService.findUserByIdOrCreateNewUser(chatId);
        List<Game> games = user.getGames().stream()
                .sorted(Comparator.comparing(Game::getTitle))
                .collect(Collectors.toList());

        List<String> resultList = new ArrayList<>();
        StringBuilder textMessage = new StringBuilder(replyMessageService.getMessage("reply.search.results.head"));
        int count = 0;

        for (Game g : games) {
            textMessage.append(replyMessageService.getMessage("reply.search.unsubscribe",
                    new Object[]{g.getTitle(), "/gameremove_" + g.getTeseraId()}));
            count += 1;
            if (count % 20 == 0 && count > 0) {
                resultList.add(textMessage.toString());
                textMessage.setLength(0);
            }
        }

        if (textMessage.length() > 0) resultList.add(textMessage.toString());
        return resultList;
    }
}
