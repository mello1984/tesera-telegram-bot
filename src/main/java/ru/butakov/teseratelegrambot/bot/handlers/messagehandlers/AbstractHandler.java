package ru.butakov.teseratelegrambot.bot.handlers.messagehandlers;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.butakov.teseratelegrambot.bot.SendMessageFormat;
import ru.butakov.teseratelegrambot.service.ReplyMessageService;

@FieldDefaults(level = AccessLevel.PROTECTED)
public abstract class AbstractHandler implements InputMessageHandler {
    @Autowired
    ReplyMessageService replyMessageService;
    @Autowired
    SendMessageFormat sendMessageFormat;

    @Override
    public SendMessage handle(Message message) {
        SendMessage replyMessage = sendMessageFormat.getSendMessageBaseFormat(message.getChatId());
        String replyText = getSimpleMessageCode() != null ? replyMessageService.getMessage(getSimpleMessageCode()) : "";
        replyMessage.setText(replyText);
        return replyMessage;
    }

    protected String getSimpleMessageCode() {
        return null;
    }
}
