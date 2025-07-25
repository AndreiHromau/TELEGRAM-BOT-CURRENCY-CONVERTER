package com.example.telegrambotconvertermoney.model;

import lombok.Data;

import java.util.Date;

@Data
public class CurrencyModel {
    private Integer cur_ID;
    private Date date;
    private String cur_Abbreviation;
    private Integer cur_Scale;
    private String cur_Name;
    private Double cur_OfficialRate;
}