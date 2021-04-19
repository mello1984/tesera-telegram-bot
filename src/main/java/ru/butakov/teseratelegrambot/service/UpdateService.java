package ru.butakov.teseratelegrambot.service;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateService {

    @Scheduled(fixedRateString = "${tesera.updateperiod}")
    public void updateTask() {
//        log.info("Update task execute");
    }
}
