package com.viet.bookmanagement.response.restcountries;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class CountryInfo {
    private Name name;
    private List<String> tld;
    private String cca2;
    private String ccn3;
    private String cca3;
    private String cioc;
    private boolean independent;
    private String status;
    private boolean unMember;
    private Map<String, Currency> currencies;
    private Idd idd;
    private List<String> capital;
    private List<String> altSpellings;
    private String region;
    private String subregion;
    private Map<String, String> languages;
    private Map<String, Translation> translations;
    private List<Double> latlng;
    private boolean landlocked;
    private List<String> borders;
    private double area;
    private Demonyms demonyms;
    private String flag;
    private Map<String, String> maps;
    private long population;
    private Map<String, Double> gini;
    private String fifa;
    private Car car;
    private List<String> timezones;
    private List<String> continents;
    private Flags flags;
    private CoatOfArms coatOfArms;
    private String startOfWeek;
    private CapitalInfo capitalInfo;

    // Getters and setters
}

@Data
class Name {
    private String common;
    private String official;
    private Map<String, Map<String, String>> nativeName;

    // Getters and setters
}

@Data
class Currency {
    private String name;
    private String symbol;

    // Getters and setters
}

@Data
class Idd {
    private String root;
    private List<String> suffixes;

    // Getters and setters
}

@Data
class Translation {
    private String official;
    private String common;

    // Getters and setters
}

@Data
class Demonyms {
    private Map<String, String> eng;
    private Map<String, String> fra;

    // Getters and setters
}



@Data
class Car {
    private List<String> signs;
    private String side;

    // Getters and setters
}

@Data
class Flags {
    private String png;
    private String svg;
    private String alt;

    // Getters and setters
}

@Data
class CoatOfArms {
    private String png;
    private String svg;

    // Getters and setters
}

@Data
class CapitalInfo {
    private List<Double> latlng;

    // Getters and setters
}
