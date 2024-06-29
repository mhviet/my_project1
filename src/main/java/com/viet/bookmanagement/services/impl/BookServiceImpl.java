package com.viet.bookmanagement.services.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.viet.bookmanagement.dao.IBookDAO;
import com.viet.bookmanagement.entities.Book;
import com.viet.bookmanagement.response.restcountries.CountryInfo;
import com.viet.bookmanagement.services.IBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;

@Service
public class BookServiceImpl implements IBookService {

    private final WebClient webClient;

    @Autowired
    private RestTemplate restTemplate;

    public BookServiceImpl() {
        this.webClient = WebClient.create();
    }

    @Autowired
    private IBookDAO bookDAO;

    @Override
    @Transactional
    public List<Book> getAllBooks() {
        return bookDAO.findAll();
    }

    @Override
    public Book getBookById(Long id) {
        return null;
    }

    @Override
    public List<Book> getBookByTitle(String title) {
        return bookDAO.getBooksByTitle(title);
    }

    @Override
    public void addBook(Book book) {

    }

    @Override
    public void updateBook(Book book) {

    }

    @Override
    public void deleteBook(Long id) {

    }

//    @Override
//    public Mono<String> getCurrency(String currency) {
//        CountryInfo CountryInfo = new
//        return webClient.get()
//                .uri("https://restcountries.com/v3.1/currency/"+currency)
//                .retrieve()
//                .bodyToMono(String.class);
//    }

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Mono<List<CountryInfo>> getCountryInfoByCurrency(String currency) {
        return webClient.get()
                .uri("https://restcountries.com/v3.1/currency/" + currency)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE) // Add header
                // You can add more headers if needed
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(json -> {
                    try {
                        List<CountryInfo> countryInfoList = objectMapper.readValue(json, new TypeReference<List<CountryInfo>>() {});
                        return Mono.just(countryInfoList);
                    } catch (Exception e) {
                        return Mono.error(e);
                    }
                });
    }

    public List<CountryInfo> getCountryInfoByCurrencyRestTemplate(String currency) {
        // Define the URL of the external API
        String apiUrl = "https://restcountries.com/v3.1/currency/" + currency;

        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Make a GET request to the API and retrieve the response as a String
//        String jsonResponse = restTemplate.getForObject(apiUrl, String.class);

        // Make the API call with the exchange() method, passing in the URL, HttpMethod, headers, and response type
        ResponseEntity<String> jsonResponse = restTemplate.exchange(apiUrl, HttpMethod.GET, new HttpEntity<>(headers), String.class);

        // Parse the JSON response into a list of CountryInfo objects
        List<CountryInfo> countryInfoList = null;
        try {
            countryInfoList = objectMapper.readValue(jsonResponse.getBody(), new TypeReference<List<CountryInfo>>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }

        return countryInfoList;
    }
}
