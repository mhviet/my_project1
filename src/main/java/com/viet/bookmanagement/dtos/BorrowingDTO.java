package com.viet.bookmanagement.dtos;

import lombok.Data;

import java.util.Date;

@Data
public class BorrowingDTO {
    private String username;
    private String email;
    private Date dueDate;

    // Constructors, getters, and setters
}