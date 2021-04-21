package ru.butakov.teseratelegrambot.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Component
public class ReplyMessageService {
    @Autowired
    MessageSource messageSource;
    final Locale locale = Locale.forLanguageTag("ru_RU");


    public String getMessage(String messageCode, Object[] args) {
        return messageSource.getMessage(messageCode, args, locale);
    }

    public String getMessage(String messageCode) {
        return getMessage(messageCode, null);
    }
    public String getMessage(String messageCode, Object arg) {
        return getMessage(messageCode, new Object[]{arg});
    }
}
