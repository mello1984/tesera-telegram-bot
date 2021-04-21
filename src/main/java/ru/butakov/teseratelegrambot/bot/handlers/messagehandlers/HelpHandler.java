package ru.butakov.teseratelegrambot.bot.handlers.messagehandlers;

import org.springframework.stereotype.Component;

@Component
public class HelpHandler extends AbstractHandler {

    @Override
    protected String getSimpleMessageCode() {
        return "reply.help";
    }

    @Override
    public BotCommand getHandlerCommand() {
        return BotCommand.HELP;
    }
}
