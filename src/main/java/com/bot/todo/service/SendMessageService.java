package com.bot.todo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.generics.TelegramClient;


@Service
public class SendMessageService implements SendBotMessageService {
    private final TelegramClient telegramClient;

    @Autowired
    public SendMessageService(TelegramClient telegramClient) {
        this.telegramClient = telegramClient;
    }


    @Override
    public void sendMessage(String chatId, String message) {
        SendMessage sendMessage = SendMessage
                .builder()
                .chatId(chatId)
                .text(message)
                .build();

        try {
            telegramClient.execute(sendMessage);
        } catch (Exception e) {
            System.out.printf("Ошибка отправки сообщения: %s\n", e.getMessage());
        }
    }

    @Override
    public void sendMessage(String chatId, String message, InlineKeyboardMarkup keyboard) {
        SendMessage sendMessage = SendMessage
                .builder()
                .chatId(chatId)
                .text(message)
                .build();

        sendMessage.setReplyMarkup(keyboard);

        try {
            telegramClient.execute(sendMessage);
        } catch (Exception e) {
            System.out.printf("Ошибка отправки сообщения: %s\n", e.getMessage());
        }
    }

    @Override
    public void editMessage(String chatId, Integer messageId, String text, InlineKeyboardMarkup markup) {
        EditMessageText editMessage = EditMessageText
                .builder()
                .chatId(chatId)
                .messageId(messageId)
                .text(text)
                .build();

        editMessage.setReplyMarkup(markup);

        try {
            telegramClient.execute(editMessage);
        } catch (Exception e) {
            System.out.printf("Ошибка изменения сообщения: %s\n", e.getMessage());
        }
    }
}
