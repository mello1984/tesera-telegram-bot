package ru.butakov.teseratelegrambot.bot.handlers.messagehandlers;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface InputMessageHandler {
    SendMessage handle(Message message);
    BotCommand getHandlerCommand();
}
