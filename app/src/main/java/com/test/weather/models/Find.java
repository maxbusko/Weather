package com.test.weather.models;

import java.util.ArrayList;

public class Find {
    private String message;
    private String code;
    private int count;
    private ArrayList<City> list;

    public int getCount() {
        return count;
    }

    public ArrayList<City> getCities() {
        return list;
    }
}
