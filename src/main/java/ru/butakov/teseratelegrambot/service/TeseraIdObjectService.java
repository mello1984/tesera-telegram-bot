package ru.butakov.teseratelegrambot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.butakov.teseratelegrambot.dao.TeseraIdObjectRepository;
import ru.butakov.teseratelegrambot.entity.TeseraIdObject;

@Service
public class TeseraIdObjectService {
    @Autowired
    TeseraIdObjectRepository teseraIdObjectRepository;

    public void saveTeseraIdObject(TeseraIdObject teseraIdObject) {
        teseraIdObjectRepository.save(teseraIdObject);
    }

    public TeseraIdObject findTopByTeseraId() {
        return teseraIdObjectRepository.findTopByTeseraIdIsNotNull();
    }

//    public TeseraIdObject findTopTeseraIdObject
}
