package com.bot.todo.repository;

import com.bot.todo.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUser_ChatId(Long id);
    List<Task> findByUser_ChatIdAndCompleted(Long id, boolean completed);
    Optional<Task> findByIdAndUser_ChatId(Long id, Long chatId);
    int countByUser_ChatIdAndCompleted(Long id, boolean completed);

    Optional<Task> deleteByIdAndUser_ChatId(Long id, Long chatId);

    List<Task> findByDeadlineBeforeAndCompletedFalse(LocalDateTime time);

}