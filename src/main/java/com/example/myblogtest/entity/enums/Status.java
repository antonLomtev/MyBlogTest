package com.example.myblogtest.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Status {
    OPEN, REVIEW, APPROVED, REJECTED;

    Status() {
    }


//    @JsonCreator
//    public static Status getDepartmentFromCode(String value) {
//
//        for (Status status : Status.values()) {
//
//            if (status.equals(value)) {
//                return status;
//            }else throw new  IllegalArgumentException();
//        }
//
//        return null;
//    }
}
