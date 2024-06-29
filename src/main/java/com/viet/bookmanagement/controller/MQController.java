package com.viet.bookmanagement.controller;

import com.viet.bookmanagement.entities.Book;
import com.viet.bookmanagement.entities.QueueAuditLog;
import com.viet.bookmanagement.ibm.mq.MQMessageConsumer;
import com.viet.bookmanagement.services.IQueueAuditLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

@RestController
@RequestMapping("/mq")
@Slf4j
public class MQController {

    @Value("${queue.name2}")
    private String queueName2;

    @Value("${queue.manager}")
    private String queueManager;


    @GetMapping("/lisenerLocal")
    public ResponseEntity<Void> triggerMQListener() {
        QueueAuditLog queueAuditLog = MQMessageConsumer.initListener(queueName2, queueManager);

        return ResponseEntity.status(HttpStatus.OK).build();
    }


}
