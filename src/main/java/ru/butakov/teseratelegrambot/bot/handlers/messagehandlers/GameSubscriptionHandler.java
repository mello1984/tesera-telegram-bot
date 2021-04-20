package ru.butakov.teseratelegrambot.bot.handlers.messagehandlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.butakov.teseratelegrambot.bot.SendMessageFormat;
import ru.butakov.teseratelegrambot.entity.Game;
import ru.butakov.teseratelegrambot.entity.User;
import ru.butakov.teseratelegrambot.service.GameService;
import ru.butakov.teseratelegrambot.service.ReplyMessageService;
import ru.butakov.teseratelegrambot.service.SubscriptionGameService;
import ru.butakov.teseratelegrambot.service.UserService;

import java.util.Optional;

@Component
public class GameSubscriptionHandler implements InputMessageHandler {
    @Autowired
    ReplyMessageService replyMessageService;
    @Autowired
    SendMessageFormat sendMessageFormat;
    @Autowired
    SubscriptionGameService subscriptionGameService;
    @Autowired
    UserService userService;
    @Autowired
    GameService gameService;

    @Override
    public SendMessage handle(Message message) {
        String replyText = "";
        if (message.getText().equals("/game_unsubscribe"))
            replyText = unSubscribeMessage(message.getChatId());
        else if (message.getText().equals("/game_unsubscribe_all"))
            replyText = removeGameSet(message);
        else
            replyText = changeGameSet(message);

        SendMessage replyMessage = sendMessageFormat.getSendMessageBaseFormat(message.getChatId());
        replyMessage.setText(replyText);
        return replyMessage;
    }

    private String removeGameSet(Message message) {
        User user = userService.findUserByIdOrCreateNewUser(message.getChatId());
        user.getGames().clear();
        userService.saveUser(user);
        return "Список подписок очищен";
    }

    private String changeGameSet(Message message) {
        String replyText = "";
        int gameId = Integer.parseInt(message.getText().split("_")[1]);
        User user = userService.findUserByIdOrCreateNewUser(message.getChatId());
        Optional<Game> game = gameService.findByTeseraId(gameId);
        if (game.isEmpty()) return "Игра с данным кодом не найдена в вашем списке подписок";

        if (message.getText().startsWith("/gameadd")) {
            user.getGames().add(game.get());
            replyText = "Игра добавлена в список подписки";
        }

        if (message.getText().startsWith("/gameremove")) {
            user.getGames().remove(game.get());
            replyText = "Игра удалена из списка подписки";
        }
        userService.saveUser(user);
        return replyText;
    }

    private String unSubscribeMessage(long chatId) {
        return subscriptionGameService.getUnsubscribeTextMessage(chatId);
    }


    @Override
    public BotCommand getHandlerCommand() {
        return BotCommand.GAMESUBSCRIPTION;
    }
}
