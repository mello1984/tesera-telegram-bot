package ru.butakov.teseratelegrambot.bot.handlers.messagehandlers;

import org.springframework.stereotype.Component;

@Component
public class UnknownCommandHandler extends AbstractHandler {

    @Override
    protected String getSimpleMessageCode() {
        return "reply.unknowncommand";
    }

    @Override
    public BotCommand getHandlerCommand() {
        return BotCommand.UNKNONWN;
    }
}
