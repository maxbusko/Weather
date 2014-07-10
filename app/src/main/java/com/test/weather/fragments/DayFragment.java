package com.test.weather.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.test.weather.Config;
import com.test.weather.R;
import com.test.weather.models.Forecast;

public class DayFragment extends Fragment {

    private static final String ARG_DAY = "DayFragment:day";

    public static DayFragment newInstance(Forecast.Day day) {
        DayFragment f = new DayFragment();
        Bundle b = new Bundle();
        b.putParcelable(ARG_DAY, day);
        f.setArguments(b);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Context context = getActivity();
        View view = inflater.inflate(R.layout.fragment_day, container, false);

        Forecast.Day day = getArguments().getParcelable(ARG_DAY);
        Forecast.Day.Weather weather = day.getWeather().get(0);
        Forecast.Day.Temp temp = day.getTemp();

        AQuery aq = new AQuery(view);

        aq.id(R.id.icon).image(String.format(Config.IMG_WEATHER, weather.getIcon()), true, true);
        ((TextView)view.findViewById(R.id.description)).setText(weather.getDescription());
        ((TextView)view.findViewById(R.id.temp)).setText(String.format(context.getString(R.string.temp), temp.getDay()));
        ((TextView)view.findViewById(R.id.min)).setText(String.format(context.getString(R.string.min_temp), temp.getMin()));
        ((TextView)view.findViewById(R.id.max)).setText(String.format(context.getString(R.string.min_temp), temp.getMax()));

        ((TextView)view.findViewById(R.id.humidity)).setText(String.format(context.getString(R.string.humidity), day.getHumidity()));
        ((TextView)view.findViewById(R.id.pressure)).setText(String.format(context.getString(R.string.pressure), day.getPressure()));
        ((TextView)view.findViewById(R.id.wind)).setText(String.format(context.getString(R.string.wind), day.getSpeed()));
        return view;
    }
}
