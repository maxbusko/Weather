package com.test.weather.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.test.weather.Config;
import com.test.weather.R;
import com.test.weather.models.City;

import java.util.ArrayList;

public class CitiesAdapter extends BaseAdapter {
    private final LayoutInflater mInflater;
    private ArrayList<City> mItems = new ArrayList<City>();
    private Context mContext;

    public CitiesAdapter(Context context){
        mInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public City getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setItems(ArrayList<City> items){
        mItems = items;
        notifyDataSetChanged();
    }

    public void remove(int i){
        mItems.remove(i);
        notifyDataSetChanged();
    }

    public void insert(City item, int i) {
        mItems.add(i, item);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;
        if (view == null) {
            view = mInflater.inflate(R.layout.list_item_city, parent, false);
            holder = new ViewHolder();

            holder.name = (TextView) view.findViewById(R.id.name);
            holder.icon = (ImageView) view.findViewById(R.id.icon);
            holder.desc = (TextView) view.findViewById(R.id.description);
            holder.temp = (TextView) view.findViewById(R.id.temp);
            holder.min = (TextView) view.findViewById(R.id.min);
            holder.max = (TextView) view.findViewById(R.id.max);
            holder.humidity = (TextView) view.findViewById(R.id.humidity);
            holder.pressure = (TextView) view.findViewById(R.id.pressure);
            holder.wind = (TextView) view.findViewById(R.id.wind);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        AQuery aq = new AQuery(view);

        City item = mItems.get(position);
        City.Weather weather = item.getWeather().get(0);
        City.Main main = item.getMain();
        City.Wind wind = item.getWind();

        holder.name.setText(item.getName());
        holder.desc.setText(weather.getDescription());
        aq.id(holder.icon).image(String.format(Config.IMG_WEATHER, weather.getIcon()), true, true);

        holder.temp.setText(String.format(mContext.getString(R.string.temp), main.getTemp()));
        holder.min.setText(String.format(mContext.getString(R.string.min_temp), main.getMin()));
        holder.max.setText(String.format(mContext.getString(R.string.max_temp), main.getMax()));
        holder.humidity.setText(String.format(mContext.getString(R.string.humidity), main.getHumidity()));
        holder.pressure.setText(String.format(mContext.getString(R.string.pressure), main.getPressure()));
        holder.wind.setText(String.format(mContext.getString(R.string.wind), wind.getSpeed()));

        return view;
    }

    static class ViewHolder {
        TextView name;
        ImageView icon;
        TextView desc;
        TextView temp;
        TextView min;
        TextView max;
        TextView humidity;
        TextView pressure;
        TextView wind;
    }
}
