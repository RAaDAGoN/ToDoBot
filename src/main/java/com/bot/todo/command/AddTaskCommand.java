package com.bot.todo.command;

import com.bot.todo.entity.Task;
import com.bot.todo.entity.User;
import com.bot.todo.service.SendBotMessageService;
import com.bot.todo.service.TaskService;
import com.bot.todo.service.UserService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Component
public class AddTaskCommand implements Command {
    private final SendBotMessageService sendBotMessageService;

    private final TaskService taskService;

    private final UserService userService;

    public AddTaskCommand(SendBotMessageService sendBotMessageService, TaskService taskService, UserService userService) {
        this.sendBotMessageService = sendBotMessageService;
        this.taskService = taskService;
        this.userService = userService;
    }

    @Override
    public void execute(Update update) {
        long chatId = update.getMessage().getChatId();
        String[] parts = update.getMessage().getText().split(" ", 3);

        try {
            User user = userService.findByChatId(chatId);
            if (user == null) {
                user = new User();
                user.setChatId(chatId);
                user.setUsername(update.getMessage().getFrom().getUserName());
                userService.saveUser(user);
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            String date = parts[2];

            LocalDateTime deadline = LocalDate.parse(date, formatter).atStartOfDay();

            Task task = new Task();
            task.setDescription(parts[1]);
            task.setUser(user);
            task.setDeadline(deadline);
            task.setCompleted(false);

            taskService.saveTask(task);
            sendBotMessageService.sendMessage(update.getMessage().getChatId().toString(), "Задача успешно добавлена");
        } catch (Exception e) {
            System.out.printf("Ошибка: %s", e.getMessage());
        }
    }
}
