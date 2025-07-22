package com.bot.todo.service;

import com.bot.todo.entity.Task;
import com.bot.todo.repository.TaskRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    public Task saveTask(Task task) {
        return taskRepository.save(task);
    }

    public List<Task> findByChatId(Long chatId){
        return taskRepository.findByUser_ChatId(chatId);
    }

    public Optional<Task> findByIdAndChatId(Long id, Long chatId){
        return taskRepository.findByIdAndUser_ChatId(id, chatId);
    }

    public void markAsCompleted(Long taskId, Long chatId){
        Optional<Task> optionalTask = taskRepository.findByIdAndUser_ChatId(taskId, chatId);
        optionalTask.ifPresent(task -> {
            task.setCompleted(true);
            task.setCompletedAt(LocalDateTime.now());
            taskRepository.save(task);
        });
    }

    @Transactional
    public void deleteByIdAndChatId(Long taskId, Long chatId) {
        taskRepository.deleteByIdAndUser_ChatId(taskId, chatId);
    }

    public List<Task> findTasksWithDeadlinesBefore(LocalDateTime time) {
        return taskRepository.findByDeadlineBeforeAndCompletedFalse(time);
    }

}
