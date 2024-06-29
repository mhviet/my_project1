package com.viet.bookmanagement.services;

import com.viet.bookmanagement.entities.Book;
import com.viet.bookmanagement.response.restcountries.CountryInfo;
import reactor.core.publisher.Mono;

import java.util.List;
public interface IBookService {
    List<Book> getAllBooks();
    Book getBookById(Long id);
    List<Book> getBookByTitle(String title);
    void addBook(Book book);
    void updateBook(Book book);
    void deleteBook(Long id);

    Mono<List<CountryInfo>> getCountryInfoByCurrency(String currency);
    public List<CountryInfo> getCountryInfoByCurrencyRestTemplate(String currency);
}
