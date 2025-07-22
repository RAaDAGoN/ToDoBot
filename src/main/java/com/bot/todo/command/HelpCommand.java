package com.bot.todo.command;

import com.bot.todo.service.SendBotMessageService;
import org.telegram.telegrambots.meta.api.objects.Update;

public class HelpCommand implements Command{
    private final SendBotMessageService sendBotMessageService;

    public HelpCommand(SendBotMessageService sendBotMessageService) {
        this.sendBotMessageService = sendBotMessageService;
    }

    @Override
    public void execute(Update update) {
        sendBotMessageService.sendMessage(update.getMessage().getChatId().toString(), "Доступные команды:\n" +
                "/start - начать работу\n" +
                "/addtask погулять\n" +
                "/listtask - Список задач");
    }
}
