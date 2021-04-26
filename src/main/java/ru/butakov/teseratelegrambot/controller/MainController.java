package ru.butakov.teseratelegrambot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.butakov.teseratelegrambot.service.MainService;

@RestController
public class MainController {
    @Autowired
    MainService mainService;

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public BotApiMethod<?> onUpdateReceived(@RequestBody Update update) {
        return mainService.onUpdateReceived(update);
    }

    @RequestMapping(value = "/callback/", method = RequestMethod.POST)
    public BotApiMethod<?> onUpdateReceived2(@RequestBody Update update) {
        return mainService.onUpdateReceived(update);
    }
}
