package com.viet.bookmanagement.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import com.viet.bookmanagement.entities.Book;
import com.viet.bookmanagement.exception.BookNotFoundException;
import com.viet.bookmanagement.services.IBookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@SpringBootTest
public class BookControllerTest {

    private MockMvc mockMvc;

    @Mock
    private IBookService bookService;

    @InjectMocks
    private BookController bookController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
        // creates a MockMvc instance that is configured to test the bookController in isolation, without needing to load the entire Spring application context. It allows to perform HTTP requests and verify the responses directly against the controller.
        mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();


    }

    @Test
    public void testGetAllBooks() {
        // Mocking behavior of bookService
        List<Book> mockBooks = new ArrayList<>();
        mockBooks.add(new Book("Title1", "Author1", 1));
        mockBooks.add(new Book("Title2", "Author2", 1));
        when(bookService.getAllBooks()).thenReturn(mockBooks);

        // Calling the controller method
        ResponseEntity<List<Book>> responseEntity = bookController.getAllBooks();

        // Verifying the response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(mockBooks, responseEntity.getBody());
    }

    @Test
    public void testGetBookByTitle() {
        // Mocking behavior of bookService
        List<Book> mockBooks = new ArrayList<>();
        mockBooks.add(new Book("Title1", "Author1", 1));
        when(bookService.getBookByTitle("Title1")).thenReturn(mockBooks);

        // Calling the controller method
        ResponseEntity<List<Book>> responseEntity = bookController.getBookByTitle("Title1");

        // Verifying the response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(mockBooks, responseEntity.getBody());
    }

    @Test
    public void testGetBookByTitle_NotFound() {
        // Mocking behavior of bookService
        when(bookService.getBookByTitle(anyString())).thenReturn(null);

        // Calling the controller method
        BookNotFoundException exception = assertThrows(BookNotFoundException.class, () -> {
            bookController.getBookByTitle("NonExistingTitle");
        });

        // Verifying the exception message
        assertEquals("Book with this title is not existing.", exception.getMessage());
    }

    @Test
    public void testGetAllBooksAPI() throws Exception {
        // Mocking bookService response
        List<Book> books = new ArrayList<>();
        books.add(new Book("Book 1", "Author 1", 1));
        books.add(new Book("Book 2", "Author 2", 1));
        when(bookService.getAllBooks()).thenReturn(books);

        // Performing GET request
        mockMvc.perform(get("/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].title").value("Book 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].title").value("Book 2"));
    }

    @Test
    public void testGetBookByTitleAPI() throws Exception {
        // Mocking bookService response
        List<Book> books = new ArrayList<>();
        books.add(new Book("Book 1", "Author 1", 1));
        when(bookService.getBookByTitle("Book 1")).thenReturn(books);

        // Performing GET request
        mockMvc.perform(get("/books/Book 1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].title").value("Book 1"));
    }
}
