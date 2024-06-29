package com.viet.bookmanagement.dao;

import com.viet.bookmanagement.entities.Book;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IBookDAO extends CrudRepository<Book, Long> {
    List<Book> findAll();

    static final  String GET_BOOK_BY_TITLE = "SELECT * FROM books b WHERE title =:title";

    @Query(value = GET_BOOK_BY_TITLE, nativeQuery = true)
    public List<Book> getBooksByTitle(@Param("title") String title);
}