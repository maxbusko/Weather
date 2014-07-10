package com.test.weather.fragments;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.astuetz.PagerSlidingTabStrip;
import com.google.gson.Gson;
import com.test.weather.Config;
import com.test.weather.R;
import com.test.weather.Utils;
import com.test.weather.adapters.DaysAdapter;
import com.test.weather.models.Forecast;
import com.test.weather.volley.MyVolley;

public class DetailsFragment extends Fragment implements Response.Listener<String>, Response.ErrorListener, ActionBar.OnNavigationListener {

    private StringRequest mRequest;
    private Menu optionsMenu;
    private int cityId;
    private Activity mContext;
    private DaysAdapter mAdapter;
    private PagerSlidingTabStrip mTabs;
    private ViewPager mPager;
    private TextView cityView;

    private int mDaysCount = 3;
    private ActionBar mActionBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mContext = getActivity();
        mActionBar = getActivity().getActionBar();

        mActionBar.setDisplayShowTitleEnabled(false);
        mActionBar.setDisplayHomeAsUpEnabled(false);
        mActionBar.setHomeButtonEnabled(false);
        mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

        cityId = getArguments().getInt("id", 0);
        mDaysCount = Utils.getMenuDayCnt(mContext);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getActivity(),
                R.layout.menu_spinner_item2,
                android.R.id.text1, getResources().getStringArray(R.array.menu));
        adapter.setDropDownViewResource(R.layout.menu_spinner_item);
        mActionBar.setListNavigationCallbacks(adapter, this);
        mActionBar.setSelectedNavigationItem((mDaysCount == 3) ? 0 : 1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);

        cityView = (TextView) view.findViewById(R.id.city);

        mTabs = (PagerSlidingTabStrip) view.findViewById(R.id.tabs);
        mPager = (ViewPager) view.findViewById(R.id.pager);
        return view;
    }

    private void doRequest() {
        setRefreshActionButtonState(true);
        mRequest = new StringRequest(Request.Method.GET,
                String.format(Config.URL_FORECAST, cityId, mDaysCount),
                this, this);
        MyVolley.getRequestQueue().add(mRequest);
    }

    @Override
    public void onResponse(String s) {
        if (s != null && !s.isEmpty() && Utils.isJSONValid(s)) {
            Forecast forecast = new Gson().fromJson(s, Forecast.class);
            if (forecast.getDays() != null && forecast.getCnt() > 0) {
                cityView.setText(forecast.getCity().getName());
                mAdapter = new DaysAdapter(getChildFragmentManager(), forecast.getDays());
                mPager.setAdapter(mAdapter);
                mTabs.setViewPager(mPager);
            } else {
                mRequest.deliverError(null);
            }
        } else {
            mRequest.deliverError(null);
        }
        setRefreshActionButtonState(false);
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        String s = MyVolley.getCache(String.format(Config.URL_FORECAST, cityId, mDaysCount));
        if (s != null && !s.isEmpty() && Utils.isJSONValid(s)) {
            Forecast forecast = new Gson().fromJson(s, Forecast.class);
            if (forecast.getCnt() > 0) {
                cityView.setText(forecast.getCity().getName());
                mAdapter = new DaysAdapter(getChildFragmentManager(), forecast.getDays());
                mPager.setAdapter(mAdapter);
                mTabs.setViewPager(mPager);
            }
        } else {
            Toast.makeText(mContext, mContext.getString(R.string.error), Toast.LENGTH_LONG).show();
        }
        ;
        setRefreshActionButtonState(false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        optionsMenu = menu;
        inflater.inflate(R.menu.menu_details, menu);
        setRefreshActionButtonState(true);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                doRequest();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setRefreshActionButtonState(final boolean refreshing) {
        if (optionsMenu != null) {
            final MenuItem refreshItem = optionsMenu.findItem(R.id.refresh);
            if (refreshItem != null) {
                if (refreshing) {
                    refreshItem.setActionView(R.layout.indeterminate_progress);
                } else {
                    refreshItem.setActionView(null);
                }
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
        mDaysCount = (itemPosition == 0) ? 3 : 7;
        Utils.setMenuDaysCnt(mContext, mDaysCount);
        doRequest();
        return false;
    }
}
