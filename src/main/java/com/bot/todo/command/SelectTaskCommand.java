package com.bot.todo.command;

import com.bot.todo.entity.Task;
import com.bot.todo.service.SendBotMessageService;
import com.bot.todo.service.TaskService;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SelectTaskCommand implements Command {
    private final TaskService taskService;
    private final SendBotMessageService sendBotMessageService;

    public SelectTaskCommand(TaskService taskService, SendBotMessageService sendBotMessageService) {
        this.taskService = taskService;
        this.sendBotMessageService = sendBotMessageService;
    }

    // Вызывается при нажатии на кнопку "Редактировать"
    public void handleEditCallback(Long chatId, Long taskId) {
        Optional<Task> optionalTask = taskService.findByIdAndChatId(taskId, chatId);
        if (optionalTask.isEmpty()) {
            sendBotMessageService.sendMessage(chatId.toString(), "❌ Задача не найдена.");
            return;
        }

        Task task = optionalTask.get();
        String prompt = String.format(
                "✏️ Чтобы отредактировать задачу, отправьте:\n/edittask %d Новый текст задачи\n\nТекущий текст:\n%s",
                task.getId(),
                task.getDescription()
        );
        sendBotMessageService.sendMessage(chatId.toString(), prompt);
    }

    public void executeByTaskId(Long chatId, Long taskId) {
        executeTask(chatId, taskId);
    }

    @Override
    public void execute(Update update) {
        String[] parts = update.getMessage().getText().split(" ");
        Long chatId = update.getMessage().getChatId();
        Long taskId = Long.parseLong(parts[1]);
        executeTask(chatId, taskId);
    }

    private void executeTask(Long chatId, Long taskId) {

//        if (parts.length < 2) {
//            sendBotMessageService.sendMessage(chatId.toString(), "Пожалуйста, укажите ID задачи. Пример: /selecttask 1");
//            return;
//        }


        Optional<Task> taskOptional = taskService.findByIdAndChatId(taskId, chatId);

        if (taskOptional == null) {
            sendBotMessageService.sendMessage(chatId.toString(), "Задача не найдена");
            return;
        }

        Task task = taskOptional.get();

        StringBuilder message = new StringBuilder("Задача: ");
        message.append("*Описание:* ").append(task.getDescription()).append("\n");
        message.append("*Создана:* ").append(task.getCreatedAt().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))).append("\n");
        if (task.getDeadline() != null) {
            message.append("*Дедлайн:* ").append(task.getDeadline().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))).append("\n");
        }
        if (task.isCompleted()) {
            message.append("*Выполнена:* ✅\n");
            if (task.getCompletedAt() != null) {
                message.append("*Закрыта:* ").append(task.getCompletedAt().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))).append("\n");
            }
        } else {
            message.append("*Выполнена:* ❌\n");
        }

        // Кнопки под сообщением
        var edit = InlineKeyboardButton
                .builder()
                .text("Редактировать")
                .callbackData("edit:" + taskId)
                .build();

        var done = InlineKeyboardButton
                .builder()
                .text("Выполнить")
                .callbackData("done:" + taskId)
                .build();

        var delete = InlineKeyboardButton
                .builder()
                .text("Удалить")
                .callbackData("delete:" + taskId)
                .build();

        List<InlineKeyboardRow> row = new ArrayList<>();

        row.add(new InlineKeyboardRow(edit));
        row.add(new InlineKeyboardRow(done));
        row.add(new InlineKeyboardRow(delete));

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup(row);

        sendBotMessageService.sendMessage(chatId.toString(), message.toString(), markup);
    }
}
