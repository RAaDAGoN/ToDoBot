package com.bot.todo.scheduled;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.bot.todo.service.SendBotMessageService;
import com.bot.todo.service.TaskService;
import com.bot.todo.entity.Task;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DeadlineNotifier {
    private final TaskService taskService;
    private final SendBotMessageService sendBotMessageService;

    public DeadlineNotifier(TaskService taskService, SendBotMessageService sendBotMessageService) {
        this.taskService = taskService;
        this.sendBotMessageService = sendBotMessageService;
    }

    @Scheduled(fixedRate = 360000)
    public void notifyUsers(){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime soon = now.plusHours(48);

        List<Task> tasks = taskService.findTasksWithDeadlinesBefore(soon);

        for (Task task : tasks) {
            if (!task.isCompleted() && task.getDeadline() != null && task.getDeadline().isAfter(now)) {
                String msg = "Напоминание: задача *" + task.getDescription() +
                        "* истекает " + task.getDeadline().toLocalDate();
                sendBotMessageService.sendMessage(task.getUser().getChatId().toString(), msg);
            }
        }
    }
}
