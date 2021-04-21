package ru.butakov.teseratelegrambot.bot.handlers.messagehandlers;

import org.springframework.stereotype.Component;

@Component
public class StartHandler extends AbstractHandler {

    @Override
    protected String getSimpleMessageCode() {
        return "reply.startmessage";
    }

    @Override
    public BotCommand getHandlerCommand() {
        return BotCommand.START;
    }
}
