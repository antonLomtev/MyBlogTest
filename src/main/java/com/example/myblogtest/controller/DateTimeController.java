package com.example.myblogtest.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@RestController
public class DateTimeController {

    @PostMapping("/date1")
    public Date date1(@RequestParam("date1")
                     @DateTimeFormat(pattern = "dd.MM.yyyy") Date date1) {
        return date1;
        //
    }
    @PostMapping("/date")
    public Date date(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date) {
        return date;
    }

    @PostMapping("/localdate")
    public LocalDate localDate(@RequestParam("localDate") LocalDate localDate) {
        return localDate;
    }

    @PostMapping("/localdatetime")
    public LocalDateTime dateTime(@RequestParam("localDateTime") LocalDateTime localDateTime) {
       return localDateTime;
    }
}
