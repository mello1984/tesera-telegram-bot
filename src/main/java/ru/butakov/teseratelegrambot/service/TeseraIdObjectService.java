package ru.butakov.teseratelegrambot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.butakov.teseratelegrambot.dao.TeseraIdObjectRepository;
import ru.butakov.teseratelegrambot.entity.TeseraIdObject;

import java.util.Optional;

@Service
public class TeseraIdObjectService {
    @Autowired
    TeseraIdObjectRepository teseraIdObjectRepository;

    public void saveTeseraIdObject(TeseraIdObject teseraIdObject) {
        teseraIdObjectRepository.save(teseraIdObject);
    }

    public TeseraIdObject findTopByTeseraId() {
        TeseraIdObject teseraIdObject;
        Optional<TeseraIdObject> teseraIdObjectFromDb = teseraIdObjectRepository.findTopByTeseraIdIsNotNull();
        if (teseraIdObjectFromDb.isEmpty()) {
            teseraIdObject = new TeseraIdObject();
            teseraIdObject.setTeseraId(1);
            teseraIdObjectRepository.save(teseraIdObject);
        } else teseraIdObject = teseraIdObjectFromDb.get();

        return teseraIdObject;
    }
}
