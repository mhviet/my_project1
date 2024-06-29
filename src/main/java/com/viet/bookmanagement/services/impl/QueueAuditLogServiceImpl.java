package com.viet.bookmanagement.services.impl;

import com.viet.bookmanagement.dao.IQueueAuditLogDAO;
import com.viet.bookmanagement.entities.QueueAuditLog;
import com.viet.bookmanagement.services.IQueueAuditLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.stereotype.Service;

@Service
public class QueueAuditLogServiceImpl implements IQueueAuditLogService {

    @Autowired
    private IQueueAuditLogDAO queueAuditLogDAO;
    @Override
    public void save(QueueAuditLog queueAuditLog) {
        queueAuditLogDAO.save(queueAuditLog);
    }
}
