package com.example.telegrambotconvertermoney.config;

import com.example.telegrambotconvertermoney.TelegramBotImpl;
import com.example.telegrambotconvertermoney.TelegramBotService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Slf4j
@Component
public class BotInitializer {
    private final TelegramBotService telegramBotService;
    private final TelegramBotImpl telegramBotImpl;

    @Autowired
    public BotInitializer(TelegramBotService telegramBotService, TelegramBotImpl telegramBotImpl) {
        this.telegramBotService = telegramBotService;
        this.telegramBotImpl = telegramBotImpl;
    }

    @EventListener({ContextRefreshedEvent.class})
    public void init() {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(telegramBotImpl);
            log.info("Telegram bot '{}' successfully registered.", telegramBotImpl.getBotUsername());
        } catch (TelegramApiException e) {
            log.error("Error occurred while registering Telegram bot: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to register Telegram bot", e);
        }
    }
}
