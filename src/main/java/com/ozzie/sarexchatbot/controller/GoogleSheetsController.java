package com.ozzie.sarexchatbot.controller;

import com.ozzie.sarexchatbot.service.GoogleSheetsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

//@RestController
//@RequestMapping("/sheets")
//public class GoogleSheetsController {
//
//    private final GoogleSheetsService googleSheetsService;
//
//    public GoogleSheetsController(GoogleSheetsService googleSheetsService) {
//        this.googleSheetsService = googleSheetsService;
//    }
//
//    @GetMapping
//    public List<List<Object>> readSheet() throws GeneralSecurityException, IOException {
//        return googleSheetsService.getDataFromSheet();
//    }
//}
