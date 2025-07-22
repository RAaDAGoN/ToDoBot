package com.bot.todo.service;

import com.bot.todo.entity.User;
import com.bot.todo.repository.TaskRepository;
import com.bot.todo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User findByChatId(Long chatId) {
        return userRepository.findById(chatId).orElse(null);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }
}
