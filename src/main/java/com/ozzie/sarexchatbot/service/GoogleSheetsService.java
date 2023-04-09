package com.ozzie.sarexchatbot.service;

import com.ozzie.sarexchatbot.dto.GoogleSheetsDto;
import com.ozzie.sarexchatbot.util.GoogleSheetsUtil;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GoogleSheetsService {

    private final GoogleSheetsUtil googleSheetsUtil;

    private List<List<Object>> list;
    @SneakyThrows
    public GoogleSheetsService(GoogleSheetsUtil googleSheetsUtil){
        this.googleSheetsUtil = googleSheetsUtil;
        this.list = googleSheetsUtil.getDataFromSheet();
        list.remove(0);
    }

    public List<List<String>> getDataFromSheet() throws GeneralSecurityException, IOException {
        List<List<Object>> list = googleSheetsUtil.getDataFromSheet();

      /*  Set<Object> set = new HashSet<>();

        for (List<Object> l : list){
            set.add(l.get(0));
        }*/

        //list.removeIf(objects -> !objects.get(0).equals("DALIAN"));

        return list.stream().map(objects -> objects.stream().map(Object::toString).toList()).toList();
    }

    public GoogleSheetsDto setAnswer(GoogleSheetsDto dto) throws GeneralSecurityException, IOException {
        List<List<String>> data = getDataFromSheet();

        for(List<String> list : data){
            if(list.get(0).equals(dto.getPol())&& list.get(1).equals(dto.getPod()) && list.get(2).equals(dto.getCarrier()) && list.get(3).equals(dto.getContainerType())) {
                dto.setPodLocalCharges(list.get(4));
                dto.setCurrency(list.get(5));
                dto.setValidity(list.get(6));
            }
        }

        return dto;
    }



    @SneakyThrows
    public Set<String> getColumnSet(int index, String userMessage, boolean flag) {
        if(index == 0 && flag) {
            list = googleSheetsUtil.getDataFromSheet();
            list.remove(0);
        }

        list.removeIf(l -> flag && index != 0 && !l.get(index - 1).toString().equals(userMessage));
        Set<Object> set = new HashSet<>();

        for (List<Object> l : list){
            set.add(l.get(index));
        }


        //list.removeIf(objects -> !objects.get(0).equals("DALIAN"));

        return set.stream().map(Object::toString).collect(Collectors.toSet());
    }


}
