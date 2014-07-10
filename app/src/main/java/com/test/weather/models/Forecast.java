package com.test.weather.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays;

public class Forecast {
    private int cnt;
    private ArrayList<Day> list;
    private City city;

    public ArrayList<Day> getDays() {
        return list;
    }

    public int getCnt() {
        return cnt;
    }

    public City getCity() {
        return city;
    }

    public class City {
        private String name;

        public String getName() {
            return name;
        }
    }

    public class Day implements Parcelable {
        private long dt;
        private Temp temp = new Temp();
        private String pressure;
        private String humidity;
        private ArrayList<Weather> weather;
        private String speed;

        public long getDt() {
            return dt;
        }

        public Temp getTemp() {
            return temp;
        }

        public String getPressure() {
            return pressure;
        }

        public String getHumidity() {
            return humidity;
        }

        public ArrayList<Weather> getWeather() {
            return weather;
        }

        public String getSpeed() {
            return speed;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public Day(Parcel parcel){
            dt = parcel.readLong();
            temp = parcel.readParcelable(Temp.class.getClassLoader());
            pressure = parcel.readString();
            humidity = parcel.readString();
            Weather[] w = (Weather[]) parcel.readParcelableArray(Weather.class.getClassLoader());
            weather = new ArrayList<Weather>(Arrays.asList(w));
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            Weather[] w = (Weather[]) weather.toArray();
            dest.writeLong(dt);
            dest.writeParcelable(temp, flags);
            dest.writeString(pressure);
            dest.writeString(humidity);
            dest.writeParcelableArray(w, flags);
            dest.writeString(speed);
        }

        public class Temp implements Parcelable {
            private String day;
            private String min;
            private String max;
            private String night;
            private String eve;
            private String morn;

            public Temp(){}

            public String getDay() {
                return day;
            }

            public String getMin() {
                return min;
            }

            public String getMax() {
                return max;
            }

            public String getNight() {
                return night;
            }

            public String getEve() {
                return eve;
            }

            public String getMorn() {
                return morn;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(day);
                dest.writeString(min);
                dest.writeString(max);
                dest.writeString(night);
                dest.writeString(eve);
                dest.writeString(morn);
            }

            public Temp(Parcel parcel){
                day = parcel.readString();
                min = parcel.readString();
                max = parcel.readString();
                night = parcel.readString();
                eve = parcel.readString();
                morn = parcel.readString();
            }

            public final Parcelable.Creator<Temp> CREATOR = new Parcelable.Creator<Temp>() {

                public Temp createFromParcel(Parcel in) {
                    return new Temp(in);
                }

                public Temp[] newArray(int size) {
                    return new Temp[size];
                }
            };
        }


        public class Weather implements Parcelable{
            private String description;
            private String icon;

            public Weather(){}

            public String getDescription() {
                return description;
            }

            public String getIcon() {
                return icon;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(description);
                dest.writeString(icon);
            }

            public Weather(Parcel parcel) {
                description = parcel.readString();
                icon = parcel.readString();
            }

            public final Parcelable.Creator<Weather> CREATOR = new Parcelable.Creator<Weather>() {

                public Weather createFromParcel(Parcel in) {
                    return new Weather(in);
                }

                public Weather[] newArray(int size) {
                    return new Weather[size];
                }
            };
        }

        //Parcel
    }
}
