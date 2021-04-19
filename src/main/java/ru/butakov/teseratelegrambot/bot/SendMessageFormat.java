package ru.butakov.teseratelegrambot.bot;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.butakov.teseratelegrambot.bot.handlers.callbackquery.CallbackCommand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class SendMessageFormat {
    public SendMessage getSendMessageBaseFormat(Long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setParseMode("HTML");
        sendMessage.disableWebPagePreview();
        sendMessage.setChatId(String.valueOf(chatId));
        return sendMessage;
    }

    public void setButtons(SendMessage sendMessage, List<List<String>> buttons) {
        // Создаем клавиатуру
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        // Создаем список строк клавиатуры
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

    public SendMessage setInlineButtons(SendMessage sendMessage) {
        InlineKeyboardButton buttonSubscribe = new InlineKeyboardButton("On");
        InlineKeyboardButton buttonUnsubscribe = new InlineKeyboardButton("Off");
        buttonSubscribe.setCallbackData("buttonSubscribe");
        buttonUnsubscribe.setCallbackData("buttonUnsubscribe");

        List<InlineKeyboardButton> keyboardRow1 = new ArrayList<>();
        keyboardRow1.add(buttonSubscribe);
        keyboardRow1.add(buttonUnsubscribe);

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(keyboardRow1);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup(keyboard);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        return sendMessage;
    }
}
