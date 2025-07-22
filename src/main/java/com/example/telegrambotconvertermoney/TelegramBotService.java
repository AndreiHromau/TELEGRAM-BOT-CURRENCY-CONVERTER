package com.example.telegrambotconvertermoney;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface TelegramBotService {

    void processCurrencyCommand(long chatId, String currencyCode);

    void startCommandReceived(Long chatId, String name);

    void sendMessage(Long chatId, String textToSend);

    String getBotUsername();

    String getBotToken();

    void onUpdateReceived(Update update);
}
