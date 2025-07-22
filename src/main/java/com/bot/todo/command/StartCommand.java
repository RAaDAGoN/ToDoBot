package com.bot.todo.command;

import com.bot.todo.service.SendBotMessageService;
import org.telegram.telegrambots.meta.api.objects.Update;

public class StartCommand implements Command {
    private final SendBotMessageService sendBotMessageService;

    public StartCommand(SendBotMessageService sendBotMessageService) {
        this.sendBotMessageService = sendBotMessageService;
    }

    @Override
    public void execute(Update update) {
        String message = "Привет, я ToDo бот\n" +
                "Вот список моих команд:\n" +
                "/start - начало взаимодействия со мной\n" +
                "/addtask Задача [дд.мм.гггг] - создание новой задачи с дедлайном\n" +
                "/listtask - выводит список задач(через него можно так же взаимодействовать с задачами)\n";
        sendBotMessageService.sendMessage(update.getMessage().getChatId().toString(), message);
    }
}
