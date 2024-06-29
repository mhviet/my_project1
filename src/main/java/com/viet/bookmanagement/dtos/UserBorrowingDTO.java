package com.viet.bookmanagement.dtos;

import lombok.Data;

import java.util.Date;

@Data
public class UserBorrowingDTO {
    private String username;
    private long count;

    // Constructors, getters, and setters
}