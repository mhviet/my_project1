package com.viet.bookmanagement.dao;

import com.viet.bookmanagement.entities.Borrowing;
import com.viet.bookmanagement.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IBorrowingDAO extends CrudRepository<Borrowing, Long>  {
    public List<Borrowing> findByUserId(Long userId);

}
