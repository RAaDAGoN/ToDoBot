package com.bot.todo.service;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

public interface SendBotMessageService {
    void sendMessage(String chatId, String message);
    void sendMessage(String chatId, String message, InlineKeyboardMarkup keyboard);
    void editMessage(String chatId, Integer messageId, String text, InlineKeyboardMarkup markup);
}
