package com.example.myblogtest.service;

import com.example.myblogtest.entity.enums.Models;
import org.springframework.core.convert.converter.Converter;

public class StringToEnumConverter implements Converter<String, Models> {
    @Override
    public Models convert(String source) {
            return Models.valueOf(source.toUpperCase());
    }
}