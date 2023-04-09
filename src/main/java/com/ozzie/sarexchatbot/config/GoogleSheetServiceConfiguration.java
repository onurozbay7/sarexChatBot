package com.ozzie.sarexchatbot.config;

import com.ozzie.sarexchatbot.service.GoogleSheetsService;
import com.ozzie.sarexchatbot.util.GoogleSheetsUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GoogleSheetServiceConfiguration {

    @Bean
    public GoogleSheetsUtil getUtil(){
        return new GoogleSheetsUtil();
    }

    @Bean
    public GoogleSheetsService getService(GoogleSheetsUtil googleSheetsUtil){
        return new GoogleSheetsService(googleSheetsUtil);
    }
}
