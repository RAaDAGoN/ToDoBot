package com.bot.todo.command;

import com.bot.todo.service.SendBotMessageService;
import org.telegram.telegrambots.meta.api.objects.Update;

public class UnknownMessage implements Command{
    private final SendBotMessageService sendBotMessageService;

    public UnknownMessage(SendBotMessageService sendBotMessageService) {
        this.sendBotMessageService = sendBotMessageService;
    }

    @Override
    public void execute(Update update) {
        sendBotMessageService.sendMessage(update.getMessage().getChatId().toString(), "Я не понимаю вашу команду." +
                "Попробуй /help.");
    }
}
