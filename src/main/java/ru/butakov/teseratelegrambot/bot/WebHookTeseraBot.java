package ru.butakov.teseratelegrambot.bot;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
@Slf4j
public class WebHookTeseraBot extends TelegramWebhookBot {
    String botToken;
    String botUsername;
    String botPath;

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        SendMessage sendMessage = new SendMessage(update.getMessage().getChatId().toString(), update.getMessage().getText());
        try {
            this.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean send(SendMessage sendMessage) {
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


}
