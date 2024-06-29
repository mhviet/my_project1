package com.viet.bookmanagement.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import com.viet.bookmanagement.entities.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
public class BookDAOTest {

    @Autowired
    private IBookDAO bookDAO;

    @Test
    public void testFindAll() {

        List<Book> books = bookDAO.findAll();

        // Assert that the list is not null and has some elements
        assertThat(books).isNotNull();
        assertThat(books).isNotEmpty();
    }

    @Test
    @Sql("/data.sql") // Load SQL script to insert test data
    public void testGetBooksByTitle() {
        List<Book> books = bookDAO.getBooksByTitle("Sample Title");

        // Assert that the list is not null and has some elements
        assertThat(books).isNotNull();
        assertThat(books).isNotEmpty();

    }
}
