package com.bot.todo.command;

import com.bot.todo.entity.Task;
import com.bot.todo.service.SendBotMessageService;
import com.bot.todo.service.TaskService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.*;

/*
Команда выводит список задач пользователя
Через пагинацию меняет сообщение
 */

public class ListTasksCommand implements Command {
    private final SendBotMessageService sendBotMessageService;
    private final TaskService taskService;
    private static final int PAGE_SIZE = 3;

    public ListTasksCommand(SendBotMessageService sendBotMessageService, TaskService taskService) {
        this.sendBotMessageService = sendBotMessageService;
        this.taskService = taskService;
    }

    @Override
    public void execute(Update update) {
        Long chatId = update.getMessage().getChatId();

        showTasksPage(chatId, 0, null);
    }

    // Обработка изменения сообщения
    public void handleCallback(Long chatId, int page, Integer messageId) {
        showTasksPage(chatId, page, messageId);
    }

    private void showTasksPage(Long chatId, int page, Integer messageId) {
        List<Task> tasks = taskService.findByChatId(chatId);
        int totalPages = (int) Math.ceil((double) tasks.size() / PAGE_SIZE);

        if (tasks.isEmpty()) {
            sendBotMessageService.sendMessage(chatId.toString(), "У вас пока нет задач.");
            return;
        }

        int totalTasks = tasks.size();
        int fromIndex = Math.min(page * PAGE_SIZE, totalTasks);
        int toIndex = Math.min(fromIndex + PAGE_SIZE, totalTasks);

        if (fromIndex > toIndex) {
            fromIndex = Math.max(0, toIndex - PAGE_SIZE);
        }

        List<Task> pageTasks = tasks.subList(fromIndex, toIndex);

        StringBuilder message = new StringBuilder(
                String.format("🗒️ Ваши задачи (Страница %d из %d):\n\n", page + 1, totalPages)
        );

        for (int i = 0; i < pageTasks.size(); i++) {
            Task task = pageTasks.get(i);
            message.append(String.format("%d. %s [%s]\n",
                    i + 1,
                    task.getDescription(),
                    task.isCompleted() ? "✅" : "❌"
            ));
        }

        message.append("\nВыберите задачу через кнопки ниже.");


        // Кнопки под сообщением
        var next = InlineKeyboardButton
                .builder()
                .text("➡️ Далее")
                .callbackData("tasks_page:" + (page + 1))
                .build();

        var prev = InlineKeyboardButton
                .builder()
                .text("⬅️ Назад")
                .callbackData("tasks_page:" + (page - 1))
                .build();

        List<InlineKeyboardRow> row = new ArrayList<>();

        if (page == 0 && totalPages > 1) {
            row.add(new InlineKeyboardRow(next));
        } else if (page == totalPages - 1 && totalPages > 1) {
            row.add(new InlineKeyboardRow(prev));
        } else if (totalPages > 1) {
            row.add(new InlineKeyboardRow(prev, next));
        }

        InlineKeyboardRow selectRow = new InlineKeyboardRow();
        for (int i = 0; i < pageTasks.size(); i++) {
            Task task = pageTasks.get(i);
            InlineKeyboardButton btn = InlineKeyboardButton
                    .builder()
                    .text(String.valueOf(i + 1))
                    .callbackData("selecttask:" + task.getId())
                    .build();
            selectRow.add(btn);
        }
        row.add(selectRow);

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup(row);


        if (messageId == null) {
            sendBotMessageService.sendMessage(chatId.toString(), message.toString(), markup);
        } else {
            sendBotMessageService.editMessage(chatId.toString(), messageId, message.toString(), markup);
        }
    }
}