package ru.butakov.teseratelegrambot.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.butakov.teseratelegrambot.dao.ObjectTypeRepository;
import ru.butakov.teseratelegrambot.entity.ObjectType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ObjectTypeService {
    ObjectTypeRepository objectTypeRepository;
    Map<String, ObjectType> objectTypeMap = new HashMap<>();

    @Autowired
    public void setObjectTypeRepository(ObjectTypeRepository objectTypeRepository) {
        this.objectTypeRepository = objectTypeRepository;
        objectTypeRepository.findAll().forEach(objectType -> objectTypeMap.put(objectType.getType(), objectType));
    }

    public void save(ObjectType objectType) {
        objectTypeRepository.save(objectType);
    }

    public ObjectType findObjectTypeByIdOrCreateNewObjectType(String type) {
        Optional<ObjectType> objectTypeFromDb = objectTypeRepository.findByType(type);
        ObjectType objectType = objectTypeFromDb.orElseGet(() -> new ObjectType(type));
        if (objectTypeFromDb.isEmpty()) save(objectType);
        return objectType;
    }

    public List<ObjectType> findAll() {
        return objectTypeRepository.findAll();
    }

    public boolean hasObjectType(String type){
        return objectTypeMap.containsKey(type);
    }

    public ObjectType getObjectType(String type){
        return objectTypeMap.get(type);
    }
}
