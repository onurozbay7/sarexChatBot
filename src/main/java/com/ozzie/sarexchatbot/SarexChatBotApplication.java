package com.ozzie.sarexchatbot;

import com.ozzie.sarexchatbot.service.GoogleSheetsService;
import com.ozzie.sarexchatbot.util.GoogleSheetsUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
public class SarexChatBotApplication {



    public static void main(String[] args) {
         SpringApplication.run(SarexChatBotApplication.class, args);

         GoogleSheetsUtil googleSheetsUtil = new GoogleSheetsUtil();
         GoogleSheetsService googleSheetsService = new GoogleSheetsService(googleSheetsUtil);
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(new SarexChatBot(googleSheetsService));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }


    }

}
