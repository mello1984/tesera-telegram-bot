package ru.butakov.teseratelegrambot.bot;


import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LongPollingTeseraBot extends TelegramLongPollingBot {
    String token;
    String name;

    public LongPollingTeseraBot(String token, String name) {
        this.token = token;
        this.name = name;
    }

    @Override
    public String getBotUsername() {
        return name;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public void onUpdateReceived(Update update) {
        System.out.println("Received message: " + update.getMessage().getText());
        SendMessage sendMessage = new SendMessage(update.getMessage().getChatId().toString(), update.getMessage().getText());
        try {
            this.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
