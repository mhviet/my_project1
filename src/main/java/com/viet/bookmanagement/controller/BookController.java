package com.viet.bookmanagement.controller;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.viet.bookmanagement.config.CountryInfoByCurrencyProxy;
import com.viet.bookmanagement.entities.Book;
import com.viet.bookmanagement.entities.QueueAuditLog;
import com.viet.bookmanagement.exception.BookNotFoundException;
import com.viet.bookmanagement.ibm.mq.XMLBuilder;
import com.viet.bookmanagement.response.restcountries.CountryInfo;
import com.viet.bookmanagement.services.IBookService;
import com.viet.bookmanagement.services.IQueueAuditLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static com.viet.bookmanagement.ibm.mq.MQSender.sendToQueue;

@RestController
@RequestMapping("/books")
@Slf4j
public class BookController {

    @Autowired
    private IBookService bookService;

    @Autowired
    private IQueueAuditLogService queueAuditLogService;

    @Value("${queue.name1}")
    private String queueName1;

    @Value("${queue.manager}")
    private String queueManager;

    @Autowired
    private Environment environment;

    @Autowired
    private CountryInfoByCurrencyProxy countryInfoByCurrencyProxy;


    @GetMapping("")
    public ResponseEntity<List<Book>> getAllBooks() {

        log.info("instance server port:" + environment.getProperty("local.server.port"));
        List<Book> books = bookService.getAllBooks();
        String xmlMessage = XMLBuilder.generateXML("books", null);
        sendToQueue(queueName1, queueManager, xmlMessage);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(books);
    }

    @GetMapping("/{title}")
    public ResponseEntity<List<Book>> getBookByTitle(@PathVariable String title) {
        List<Book> books = bookService.getBookByTitle(title);
        if(books == null || books.size() == 0) {
            throw new BookNotFoundException("Book with this title is not existing.");
        }
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(books);
    }

    @GetMapping("/currency/{currency}")
    public Mono<ResponseEntity<List<CountryInfo>>> getExternalData(@PathVariable String currency) {


        Mono<List<CountryInfo>> result = bookService.getCountryInfoByCurrency(currency);

        result.subscribe(countryInfoList -> {

            XmlMapper xmlMapper = new XmlMapper();
            xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);

            try {
                // Convert the list of CountryInfo objects to XML string
                String xml = xmlMapper.writeValueAsString(countryInfoList);
                System.out.println(xml);
            } catch (Exception e) {
                // Handle any exceptions
                e.printStackTrace();
            }
            // Handle the list of CountryInfo here
//            for (CountryInfo countryInfo : countryInfoList) {
//                // Do something with each country info
//                System.out.println(countryInfo.getName());
//            }
        }, error -> {
            // Handle error if any
            System.err.println("An error occurred: " + error.getMessage());
        });

        return bookService.getCountryInfoByCurrency(currency)
                .map(data -> ResponseEntity.ok().body(data))
                .defaultIfEmpty(ResponseEntity.notFound().build());

    }

    @GetMapping("/currency/v1/{currency}")
    public ResponseEntity<List<CountryInfo>> getExternalDataRestTemplate(@PathVariable String currency) {


        List<CountryInfo> result = bookService.getCountryInfoByCurrencyRestTemplate(currency);


            XmlMapper xmlMapper = new XmlMapper();
            xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);

            try {
                // Convert the list of CountryInfo objects to XML string
                String xml = xmlMapper.writeValueAsString(result);
                System.out.println(xml);

                String xmlMessage = XMLBuilder.generateXML("currency", null);
                sendToQueue(queueName1, queueManager, xmlMessage);

                queueAuditLogService.save(new QueueAuditLog("1",xmlMessage, "2", LocalDateTime.now()));
            } catch (Exception e) {
                // Handle any exceptions
                e.printStackTrace();
            }
            // Handle the list of CountryInfo here
//            for (CountryInfo countryInfo : countryInfoList) {
//                // Do something with each country info
//                System.out.println(countryInfo.getName());
//            }

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(result);

    }

    @GetMapping("/country/feign/{currency}")
    public ResponseEntity<List<CountryInfo>> getExternalDataFeign(@PathVariable String currency) {


        List<CountryInfo> result = countryInfoByCurrencyProxy.retrieveCountryInfoByCurrency(currency);


        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);

        try {
            // Convert the list of CountryInfo objects to XML string
            String xml = xmlMapper.writeValueAsString(result);
            System.out.println(xml);

            String xmlMessage = XMLBuilder.generateXML("currency", null);
            sendToQueue(queueName1, queueManager, xmlMessage);

            queueAuditLogService.save(new QueueAuditLog("1",xmlMessage, "2", LocalDateTime.now()));
        } catch (Exception e) {
            // Handle any exceptions
            e.printStackTrace();
        }
        // Handle the list of CountryInfo here
//            for (CountryInfo countryInfo : countryInfoList) {
//                // Do something with each country info
//                System.out.println(countryInfo.getName());
//            }

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(result);

    }

}
