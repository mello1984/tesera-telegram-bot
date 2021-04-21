package ru.butakov.teseratelegrambot.bot.handlers.messagehandlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.butakov.teseratelegrambot.service.SubscriptionGameService;

@Component
public class SearchHandler extends AbstractHandler {
    @Autowired
    SubscriptionGameService subscriptionGameService;

    @Override
    public SendMessage handle(Message message) {
        String query = message.getText().replaceAll("/search ","");
        String replyText = subscriptionGameService.getSubscribeTextMessage(query);

        SendMessage replyMessage = sendMessageFormat.getSendMessageBaseFormat(message.getChatId());
        replyMessage.setText(replyText);

        return replyMessage;
    }

    @Override
    public BotCommand getHandlerCommand() {
        return BotCommand.SEARCH;
    }
}
