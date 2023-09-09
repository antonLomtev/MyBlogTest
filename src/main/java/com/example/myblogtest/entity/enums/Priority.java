package com.example.myblogtest.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.stream.Stream;

public enum Priority {
    LOW(100) , MEDIUM(200), HIGH(300);

    private Priority(int priority) {
        this.priority = priority;
    }

    private int priority;

    public static Priority of(int priority){
        return Stream.of(Priority.values())
                .filter(p -> p.getPriority() == priority)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public int getPriority() {
        return priority;
    }
}
