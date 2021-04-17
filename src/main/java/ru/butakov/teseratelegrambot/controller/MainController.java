package ru.butakov.teseratelegrambot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.butakov.teseratelegrambot.bot.WebHookTeseraBot;
import ru.butakov.teseratelegrambot.service.MainService;

@RestController
public class MainController {
    @Autowired
    WebHookTeseraBot bot;
    @Autowired
    MainService service;

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public void getUpdate(@RequestBody Update update) {
        System.out.println("MainController.getUpdate");
//        System.out.println("ARTICLES:");
//        service.getArticles();
//        System.out.println("NEWS:");
//        service.getNews();
        System.out.println("PUBLICATIONS:");
        service.getPublications();
        System.out.println("COMMENTS:");
        service.getComments();
//        bot.onWebhookUpdateReceived(update);
    }
}
