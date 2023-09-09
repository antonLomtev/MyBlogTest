package com.example.myblogtest.controller;

import com.example.myblogtest.entity.enums.Models;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class EnumController {
    @GetMapping("/mode2str")
    public Models getStringToMode(@RequestParam("mode") Models mode) {
        return mode;
    }
    @GetMapping("/findbymode/{mode}")
    public Models findByEnum(@PathVariable("mode") Models mode) {
      return mode;
    }
}
