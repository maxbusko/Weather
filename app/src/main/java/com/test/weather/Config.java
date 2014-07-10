package com.test.weather;

public class Config {
    public static final String URL_MAIN = "http://api.openweathermap.org/data/2.5/group?id=%s&lang=ru&units=metric";
    public static final String URL_FIND = "http://api.openweathermap.org/data/2.5/find?type=like&q=%s&lang=ru&units=metric";
    public static final String URL_FORECAST = "http://api.openweathermap.org/data/2.5/forecast/daily?id=%1$d&lang=ru&units=metric&cnt=%2$d";

    public static final String IMG_WEATHER = "http://openweathermap.org/img/w/%s.png";
}
