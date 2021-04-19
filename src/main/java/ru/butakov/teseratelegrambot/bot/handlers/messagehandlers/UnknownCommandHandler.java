package ru.butakov.teseratelegrambot.bot.handlers.messagehandlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.butakov.teseratelegrambot.bot.SendMessageFormat;
import ru.butakov.teseratelegrambot.service.ReplyMessageService;

@Component
public class UnknownCommandHandler implements InputMessageHandler {
    @Autowired
    ReplyMessageService replyMessageService;
    @Autowired
    SendMessageFormat sendMessageFormat;

    @Override
    public SendMessage handle(Message message) {
        String replyText = replyMessageService.getMessage("reply.unknowncommand");
        SendMessage replyMessage = sendMessageFormat.getSendMessageBaseFormat(message.getChatId());
        replyMessage.setText(replyText);
        return replyMessage;
    }

    @Override
    public BotCommand getHandlerCommand() {
        return BotCommand.UNKNONWN;
    }
}
