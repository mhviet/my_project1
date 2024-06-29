package com.viet.bookmanagement.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data
public class QueueAuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String type;
    private String message;
    private String result;
    @Column(name = "date_sent")
    private LocalDateTime datesent;

    // Getters and setters
    // Constructors
    // Other methods as needed

    // Example constructor and getters/setters for demonstration
    public QueueAuditLog() {
    }

    public QueueAuditLog(String type, String message, String result, LocalDateTime datesent) {
        this.type = type;
        this.message = message;
        this.result = result;
        this.datesent = datesent;
    }

}