package com.test.weather.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.test.weather.R;
import com.test.weather.models.City;

import java.util.ArrayList;

public class AddAdapter extends BaseAdapter{

    private final LayoutInflater mInflater;
    private ArrayList<City> mItems = new ArrayList<City>();

    public AddAdapter(LayoutInflater inflater){
        mInflater = inflater;
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

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;
        if (view == null) {
            view = mInflater.inflate(R.layout.list_item_add, parent, false);
            holder = new ViewHolder();

            holder.text = (TextView) view.findViewById(R.id.text);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        City item = mItems.get(position);

        holder.text.setText(item.getName()+" ("+item.getSys().getCountry()+")");

        return view;
    }

    static class ViewHolder {
        TextView text;
    }
}
