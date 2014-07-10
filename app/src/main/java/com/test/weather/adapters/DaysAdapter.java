package com.test.weather.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.test.weather.Utils;
import com.test.weather.fragments.DayFragment;
import com.test.weather.models.Forecast;

import java.util.ArrayList;

public class DaysAdapter extends FragmentPagerAdapter {
    private ArrayList<Forecast.Day> mItems = new ArrayList<Forecast.Day>();

    public DaysAdapter(FragmentManager fm, ArrayList<Forecast.Day> items) {
        super(fm);
        mItems = items;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return Utils.formatDay(mItems.get(position).getDt());
    }

    @Override
    public Fragment getItem(int i) {
        return DayFragment.newInstance(mItems.get(i));
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    public void setItems(ArrayList<Forecast.Day> items){
        mItems = items;
        notifyDataSetChanged();
    }
}
