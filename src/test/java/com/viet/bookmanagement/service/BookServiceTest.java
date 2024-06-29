package com.viet.bookmanagement.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import com.viet.bookmanagement.dao.IBookDAO;
import com.viet.bookmanagement.entities.Book;
import com.viet.bookmanagement.services.impl.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class BookServiceTest {

    @Mock
    private IBookDAO bookDAO;

    @InjectMocks
    private BookServiceImpl bookService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAllBooks() {
        // Create test data
        List<Book> books = new ArrayList<>();
        books.add(new Book("Book 1", "Author 1", 1));
        books.add(new Book("Book 2", "Author 2", 1));

        when(bookDAO.findAll()).thenReturn(books);

        List<Book> result = bookService.getAllBooks();

        assertEquals(2, result.size());
        assertEquals("Book 1", result.get(0).getTitle());
        assertEquals("Author 1", result.get(0).getAuthor());
        assertEquals("Book 2", result.get(1).getTitle());
        assertEquals("Author 2", result.get(1).getAuthor());

        // Verify that bookDAO.findAll() is called exactly once
        verify(bookDAO, times(1)).findAll();
    }

}
