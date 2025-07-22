package com.example.telegrambotconvertermoney;

import com.example.telegrambotconvertermoney.config.BotConfig;
import com.example.telegrambotconvertermoney.model.CurrencyModel;
import com.example.telegrambotconvertermoney.service.CurrencyService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.text.ParseException;


@Component
@AllArgsConstructor
public class TelegramBotImpl extends TelegramLongPollingBot implements TelegramBotService {
    private final BotConfig botConfig;

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) {
            return;
        }

        Message message = update.getMessage();
        String messageText = message.getText().trim();
        long chatId = message.getChatId();

        if (messageText.startsWith("/")) {
            String command = messageText.split("@")[0].toLowerCase();

            switch (command) {
                case "/begin":
                    startCommandReceived(chatId, message.getChat().getFirstName());
                    return;
                case "/usd":
                    processCurrencyCommand(chatId, "usd");
                    return;
                default:
                    if (command.startsWith("/")) {
                        processCurrencyCommand(chatId, command.substring(1));
                        return;
                    }
            }
        } else {
            processCurrencyCommand(chatId, messageText.toLowerCase());
        }
    }

    @Override
    public void processCurrencyCommand(long chatId, String currencyCode) {
        CurrencyModel currencyModel = new CurrencyModel();
        try {
            String result = CurrencyService.getCurrencyRate(currencyCode, currencyModel);
            sendMessage(chatId, result);
        } catch (IOException e) {
            sendMessage(chatId, "Не удалось получить курс валюты. Пожалуйста, попробуйте позже.");
        } catch (ParseException e) {
            sendMessage(chatId, "Ошибка при обработке данных. Пожалуйста, попробуйте другую валюту.");
        } catch (Exception e) {
            sendMessage(chatId, "Произошла непредвиденная ошибка.");
        }
    }

    @Override
    public void startCommandReceived(Long chatId, String name) {
        String answer = " Здарова!" + "\n" +
                "Введи или выбери валюту, официальный обменный курс которой" + "\n" +
                "ты хочешь знать относительно BYN." + "\n" +
                "Например:\n" +
                "/usd - курс доллара США\n" +
                "/eur - курс евро\n" +
                "/rub - курс российского рубля\n" +
                "/pln - курс польского злотого\n" +
                "/aud - курс австралийского доллара \n" +
                "/cad - курс канадского доллара \n" +
                "/chf - курс швейцарского франка \n" +
                "/cny - курс китайского юаня \n" +
                "/czk - курс чешского крона \n" +
                "/dkk - курс датского крона \n" +
                "/gbp - курс британского фунта \n" +
                "/hkd - курс гонконгского доллара \n" +
                "/huf - курс угорского форинта \n" +
                "/jpy - курс японского иены \n" +
                "/krw - курс корейского вона \n" +
                "/mxn - курс мексиканского песо \n" +
                "/nok - курс норвежского крона \n" +
                "/nzd - курс новозеландского доллара \n" +
                "/sek - курс шведского крона \n" +
                "/sgd - курс сингапурского доллара \n";
        sendMessage(chatId, answer);
    }

    @Override
    public void sendMessage(Long chatId, String textToSend) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(textToSend);
        sendMessage.enableMarkdown(true);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            System.err.println("Error sending message: " + e.getMessage());
        }
    }
}
