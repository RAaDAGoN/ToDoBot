package com.bot.todo.bot;

import com.bot.todo.command.CommandContainer;
import com.bot.todo.command.SelectTaskCommand;
import com.bot.todo.service.SendBotMessageService;
import com.bot.todo.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.bot.todo.command.CommandName.*;

@Component
public class ToDoBot implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {
    private final CommandContainer commandContainer;
    private final TaskService taskService;
    private final SendBotMessageService sendBotMessageService;

    @Value("${bot.username}")
    private String username;

    @Value("${bot.token}")
    private String token;

    @Autowired
    public ToDoBot(CommandContainer commandContainer, TaskService taskService, SendBotMessageService sendBotMessageService) {
        this.commandContainer = commandContainer;
        this.taskService = taskService;
        this.sendBotMessageService = sendBotMessageService;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return this;
    }

    @Override
    public void consume(Update update) {
        if (update.hasCallbackQuery()) {
            String data = update.getCallbackQuery().getData();
            Long chatId = update.getCallbackQuery().getMessage().getChatId();
            Integer messageId = update.getCallbackQuery().getMessage().getMessageId();

            if (data.startsWith("tasks_page:")) {
                int page = Integer.parseInt(data.split(":")[1]);
                commandContainer.getListTasksCommand().handleCallback(chatId, page, messageId);
            }

            if (data.startsWith("done:")) {
                Long taskId = Long.parseLong(data.split(":")[1]);
                taskService.markAsCompleted(taskId, chatId);
                sendBotMessageService.sendMessage(chatId.toString(), "Задача выполнена");
            } else if (data.startsWith("delete:")) {
                Long taskId = Long.parseLong(data.split(":")[1]);
                taskService.deleteByIdAndChatId(taskId, chatId);
                sendBotMessageService.sendMessage(chatId.toString(), "Задача удалена");
            } else if (data.startsWith("edit:")){
                Long taskId = Long.parseLong(data.split(":")[1]);

                SelectTaskCommand selectTaskCommand = new SelectTaskCommand(taskService, sendBotMessageService);
                selectTaskCommand.handleEditCallback(chatId, taskId);
            }else if (data.startsWith("selecttask:")) {
                Long taskId = Long.parseLong(data.split(":")[1]);

                SelectTaskCommand selectTaskCommand = new SelectTaskCommand(taskService, sendBotMessageService);
                selectTaskCommand.executeByTaskId(chatId, taskId);
            }

        }

        if (update.hasMessage() && update.getMessage().hasText()) {
            String message = update.getMessage().getText();

            if (message.startsWith("/")) {
                String commandIdentifier = message.split(" ")[0].toLowerCase();
                commandContainer.retrieveCommand(commandIdentifier).execute(update);
            } else {
                commandContainer.retrieveCommand(NO.getCommandName()).execute(update);
            }

        }
    }
}