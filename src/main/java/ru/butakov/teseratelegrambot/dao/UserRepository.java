package ru.butakov.teseratelegrambot.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.butakov.teseratelegrambot.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findUsersByChatId(long chatId);
}
