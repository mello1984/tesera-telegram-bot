package ru.butakov.teseratelegrambot.bot.handlers.messagehandlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.butakov.teseratelegrambot.entity.Game;
import ru.butakov.teseratelegrambot.entity.User;
import ru.butakov.teseratelegrambot.service.GameService;
import ru.butakov.teseratelegrambot.service.MessageSenderService;
import ru.butakov.teseratelegrambot.service.SubscriptionGameService;
import ru.butakov.teseratelegrambot.service.UserService;

import java.util.List;
import java.util.Optional;

@Component
public class GameSubscriptionHandler extends AbstractHandler {
    @Autowired
    SubscriptionGameService subscriptionGameService;
    @Autowired
    UserService userService;
    @Autowired
    GameService gameService;
    @Autowired
    MessageSenderService messageSenderService;

    @Override
    public SendMessage handle(Message message) {
        String replyText = "";
        if (message.getText().equals("/game_unsubscribe"))
            sendUnsubscribeMessage(message.getChatId());
        else if (message.getText().equals("/game_unsubscribe_all"))
            replyText = clearGameSet(message);
        else
            replyText = changeGameSet(message);

        SendMessage replyMessage = sendMessageFormat.getSendMessageBaseFormat(message.getChatId());
        replyMessage.setText(replyText);
        return replyMessage;
    }

    private String clearGameSet(Message message) {
        User user = userService.findUserByIdOrCreateNewUser(message.getChatId());
        user.getGames().clear();
        userService.saveUser(user);
        return replyMessageService.getMessage("reply.subscription.unsubscribe.all");
    }

    private String changeGameSet(Message message) {
        String replyText = "";
        User user = userService.findUserByIdOrCreateNewUser(message.getChatId());
        int gameId = Integer.parseInt(message.getText().split("_")[1]);
        Optional<Game> game = gameService.findByTeseraId(gameId);
        if (game.isEmpty()) return replyMessageService.getMessage("reply.subscription.game.notfound");

        if (message.getText().startsWith("/gameadd")) {
            user.getGames().add(game.get());
            replyText = replyMessageService.getMessage("reply.subscription.subscribe", game.get().getTitle());
        }

        if (message.getText().startsWith("/gameremove")) {
            user.getGames().remove(game.get());
            replyText = replyMessageService.getMessage("reply.subscription.unsubscribe", game.get().getTitle());
        }
        userService.saveUser(user);
        return replyText;
    }

    //    private String getUnsubscribeMessage(long chatId) {
//        return subscriptionGameService.getUnsubscribeTextMessage(chatId);
//    }
    private void sendUnsubscribeMessage(long chatId) {
        List<String> textList = subscriptionGameService.getListUnsubscribeTextMessage(chatId);
        if (textList.isEmpty()) textList.add(replyMessageService.getMessage("reply.search.unsubscribe.empty"));
        textList.forEach(text->{
            SendMessage sendMessage = sendMessageFormat.getSendMessageBaseFormat(chatId);
            sendMessage.setText(text);
            messageSenderService.offerBotApiMethodToQueue(sendMessage);
        });
    }

    @Override
    public BotCommand getHandlerCommand() {
        return BotCommand.GAMESUBSCRIPTION;
    }
}
