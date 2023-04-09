package com.ozzie.sarexchatbot;

import com.ozzie.sarexchatbot.dto.GoogleSheetsDto;
import com.ozzie.sarexchatbot.service.GoogleSheetsService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class SarexChatBot extends TelegramLongPollingBot {

    private final GoogleSheetsDto googleSheetsDto = new GoogleSheetsDto();

    private final GoogleSheetsService googleSheetsService;

    private int step;

    public SarexChatBot(GoogleSheetsService googleSheetsService) {
        this.googleSheetsService = googleSheetsService;
    }


    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {

            String user_message = update.getMessage().getText();
            SendMessage message = new SendMessage();


            ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
            keyboardMarkup.setResizeKeyboard(true);
            keyboardMarkup.setSelective(true);
            keyboardMarkup.setOneTimeKeyboard(true);

            if(user_message.equals("/start") || user_message.equals("info") || user_message.equals("begin")){
                message.setText("Welcome! " + update.getMessage().getFrom().getUserName() + ". Please SELECT POL");
                step = 0;
                setMarkup(0,keyboardMarkup,message,user_message);
                step ++;
                executeMessage(message, update);

            }
            else if(googleSheetsService.getColumnSet(0,user_message, false).contains(user_message) && step == 1){
                googleSheetsDto.setPol(user_message);
                setMarkup(1,keyboardMarkup,message,user_message);
                message.setText("Please SELECT POD");
                step++;
                executeMessage(message, update);

            }

            else if(googleSheetsService.getColumnSet(1,user_message, false).contains(user_message) && step == 2){
                googleSheetsDto.setPod(user_message);
                setMarkup(2,keyboardMarkup,message,user_message);
                message.setText("Please SELECT CARRIER");
                step++;
                executeMessage(message, update);
            }
            else if (googleSheetsService.getColumnSet(2,user_message, false).contains(user_message) && step == 3) {
                googleSheetsDto.setCarrier(user_message);
                setMarkup(3,keyboardMarkup,message,user_message);
                message.setText("Please SELECT CONTAINER TYPE");
                step++;
                executeMessage(message, update);
            }
            else if (googleSheetsService.getColumnSet(3,user_message, false).contains(user_message) && step == 4) {
                googleSheetsDto.setContainerType(user_message);
                try {
                    GoogleSheetsDto dto = googleSheetsService.setAnswer(googleSheetsDto);
                    message.setText("Local Charge: " + dto.getPodLocalCharges() + "\n"
                    + "Currency: " + dto.getCurrency() + "\n" + "Validity: " + dto.getValidity() + "\n"
                    + "If you want to again, please write /start");
                    List<KeyboardRow> keyboardRows = new ArrayList<>();
                    KeyboardButton keyboardButton = new KeyboardButton();
                    KeyboardRow keyboardRow = new KeyboardRow();
                    keyboardButton.setText("/start");
                    keyboardRow.add(keyboardButton);
                    keyboardRows.add(keyboardRow);
                    keyboardMarkup.setKeyboard(keyboardRows);
                    message.setReplyMarkup(keyboardMarkup);

                    executeMessage(message, update);
                } catch (GeneralSecurityException | IOException e) {
                    throw new RuntimeException(e);
                }

            }
            else{
                message.setText("Missing or incorrect entry, please choose an option from the menu.");
                executeMessage(message, update);
            }


        }

        /*else if (update.hasCallbackQuery()) {
        }*/

    }



    public void setMarkup(int index, ReplyKeyboardMarkup keyboardMarkup, SendMessage message, String userMessage){
        Set<String> sheetDataList = googleSheetsService.getColumnSet(index,userMessage, true);
        List<KeyboardRow> keyboardRows = new ArrayList<>();



        for (String sheetData : sheetDataList) {
            KeyboardButton keyboardButton = new KeyboardButton();
            KeyboardRow keyboardRow = new KeyboardRow();
            keyboardButton.setText(sheetData);
            keyboardRow.add(keyboardButton);
            keyboardRows.add(keyboardRow);

        }

        keyboardMarkup.setKeyboard(keyboardRows);
        message.setReplyMarkup(keyboardMarkup);
    }

    public void executeMessage(SendMessage message, Update update){

        try {
            message.setChatId(update.getMessage().getChatId());
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getBotUsername() {
        return "SarexChatBot";
    }

    @Override
    public String getBotToken() {
        return "6125977667:AAEsl5Z-TisUTzfwIYfEyXpfTjZV2JbAQlg";
    }
}
