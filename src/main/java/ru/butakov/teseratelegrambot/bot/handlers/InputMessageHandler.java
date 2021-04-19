package ru.butakov.teseratelegrambot.bot.handlers;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.butakov.teseratelegrambot.bot.BotCommand;

public interface InputMessageHandler {
    SendMessage handle(Message message);
    BotCommand getHandlerCommand();
}
