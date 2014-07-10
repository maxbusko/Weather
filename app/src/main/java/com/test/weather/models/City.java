package com.test.weather.models;

import java.util.ArrayList;

public class City {
    private int id;
    private long dt;
    private String name;
    private Coord coord;
    private Sys sys;
    private ArrayList<Weather> weather;
    private Main main;
    private Wind wind;
    private Clouds clouds;


    public int getId() {
        return id;
    }

    public long getDt() {
        return dt;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Weather> getWeather() {
        return weather;
    }

    public Main getMain() {
        return main;
    }

    public Wind getWind() {
        return wind;
    }

    public Sys getSys() {
        return sys;
    }

    public class Coord {
        private double lon;
        private double lat;
    }

    public class Sys {
        private String message;
        private String country;
        private long sunrise;
        private long sunset;

        public String getCountry() {
            return country;
        }
    }

    public class Weather {
        private int id;
        private String main;
        private String description;
        private String icon;

        public String getDescription() {
            return description;
        }

        public String getIcon() {
            return icon;
        }
    }

    public class Main {
        private String temp;
        private String humidity;
        private String pressure;
        private String temp_min;
        private String temp_max;

        public String getTemp() {
            return temp;
        }

        public String getHumidity() {
            return String.valueOf(humidity);
        }

        public String getPressure() {
            return String.valueOf(pressure);
        }

        public String getMin() {
            return temp_min;
        }

        public String getMax() {
            return temp_max;
        }
    }

    public class Wind {
        private double speed;
        private double gust;
        private double deg;

        public String getSpeed() {
            return String.valueOf(speed);
        }
    }

    public class Clouds {
        private int all;
    }
}
