package com.bot.todo.command;

import com.bot.todo.entity.Task;
import com.bot.todo.service.SendBotMessageService;
import com.bot.todo.service.TaskService;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

public class EditTaskCommand implements Command{
    private final TaskService taskService;
    private final SendBotMessageService sendBotMessageService;

    public EditTaskCommand(TaskService taskService, SendBotMessageService sendBotMessageService) {
        this.taskService = taskService;
        this.sendBotMessageService = sendBotMessageService;
    }

    @Override
    public void execute(Update update) {
        Long chatId = update.getMessage().getChatId();
        String messageText = update.getMessage().getText();

        String[] parts = messageText.split(" ", 3);

        if (parts.length < 3){
            sendBotMessageService.sendMessage(chatId.toString(), "Неверный формат команды");
            return;
        }

        Long taskId = Long.parseLong(parts[1]);
        String newText = parts[2];

        Optional<Task> taskOptional = taskService.findByIdAndChatId(taskId, chatId);

        if(taskOptional == null){
            sendBotMessageService.sendMessage(chatId.toString(), "задача не найдена");
            return;
        }

        Task task = taskOptional.get();

        task.setDescription(newText);
        taskService.saveTask(task);

        sendBotMessageService.sendMessage(chatId.toString(), "Задача успешно обновлена");

    }
}
