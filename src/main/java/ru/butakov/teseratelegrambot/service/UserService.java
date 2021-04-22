package ru.butakov.teseratelegrambot.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.butakov.teseratelegrambot.dao.UserRepository;
import ru.butakov.teseratelegrambot.entity.User;

import java.util.Optional;


@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserService {
    @Autowired
    UserRepository userRepository;

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public User findUserByIdOrCreateNewUser(long chatId) {
        Optional<User> userFromDb = userRepository.findUsersByChatId(chatId);
        User user = userFromDb.orElseGet(() -> new User(chatId));
        if (userFromDb.isEmpty()) saveUser(user);
        return user;
    }
}
