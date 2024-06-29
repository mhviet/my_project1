package com.viet.bookmanagement.repositoties;

import com.viet.bookmanagement.services.IBorrowingService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.awt.image.ImageObserver;
import java.util.List;

@Repository
public class BorrowingRepositoryImpl implements IBorrowingRepository {

    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public List<Object[]> getUsersWithDueDatesTodayAndTomorrow() {
        Query query = entityManager.createNativeQuery(
                "SELECT u.username, u.email, b.due_date " +
                        "FROM users u " +
                        "JOIN borrowings b ON u.id = b.user_id " +
                        "WHERE DATE(b.due_date) = CURDATE() OR DATE(b.due_date) = DATE_ADD(CURDATE(), INTERVAL 1 DAY)"
        );
        return query.getResultList();
    }

    @Override
    public List<Object[]> getAllUsersByBorrowing() {
        Query query = entityManager.createNativeQuery(
                "SELECT u.username, COUNT(b.book_id) AS borrow FROM users u INNER JOIN borrowings b ON b.user_id = u.id GROUP BY b.user_id ORDER BY u.username desc"

        );
        return query.getResultList();
    }
}
