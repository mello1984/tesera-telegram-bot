package ru.butakov.teseratelegrambot.bot;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class SendMessageFormat {
    private static final String commandHelp = "/help";
    private static final String commandSettings = "/settings";

    public SendMessage getSendMessageBaseFormat(Long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setParseMode("HTML");
        sendMessage.disableWebPagePreview();
        sendMessage.setChatId(String.valueOf(chatId));
        setButtons(sendMessage, commandHelp, commandSettings);
        return sendMessage;
    }

    public void setButtons(SendMessage sendMessage, List<List<String>> buttons) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        List<KeyboardRow> keyboard = new ArrayList<>();
        buttons.forEach(l -> {
            KeyboardRow keyboardRow = new KeyboardRow();
            l.forEach(button -> keyboardRow.add(new KeyboardButton(button)));
            keyboard.add(keyboardRow);
        });

        replyKeyboardMarkup.setKeyboard(keyboard);
    }

    public void setButtons(SendMessage sendMessage, String... buttons) {
        List<List<String>> oneLineButtons = new ArrayList<>();
        oneLineButtons.add(Arrays.asList(buttons));
        setButtons(sendMessage, oneLineButtons);
    }
}
