package com.viet.bookmanagement.dao;

import com.viet.bookmanagement.entities.QueueAuditLog;
import com.viet.bookmanagement.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IQueueAuditLogDAO extends CrudRepository<QueueAuditLog, Long> {
}
