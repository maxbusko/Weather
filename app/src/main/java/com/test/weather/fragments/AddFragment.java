package com.test.weather.fragments;

import android.app.ActionBar;
import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.codeslap.persistence.Persistence;
import com.codeslap.persistence.SqlAdapter;
import com.google.gson.Gson;
import com.test.weather.Config;
import com.test.weather.R;
import com.test.weather.adapters.AddAdapter;
import com.test.weather.models.City;
import com.test.weather.models.Database;
import com.test.weather.models.Find;
import com.test.weather.volley.MyVolley;

public class AddFragment extends Fragment implements Response.Listener<String>, Response.ErrorListener, AdapterView.OnItemClickListener {

    private Activity mContext;
    private AddAdapter mAdapter;
    private OnSearchItemClickListener mCallback;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private EditText searchView;
    private InputMethodManager inputMethodManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        mContext = getActivity();

        inputMethodManager = (InputMethodManager) mContext.getSystemService(Activity.INPUT_METHOD_SERVICE);

        mAdapter = new AddAdapter(inflater);
        ListView lv = (ListView) view.findViewById(android.R.id.list);
        lv.setAdapter(mAdapter);
        lv.setOnItemClickListener(this);

        searchView = (EditText) view.findViewById(R.id.search);

        searchView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String s = v.getText().toString();
                    if (s.length() > 3) {
                        doRequest(s);
                    }
                    return true;
                }
                return false;
            }
        });

        return view;
    }

    private void doRequest(String s) {
        StringRequest myReq = new StringRequest(Request.Method.GET,
                String.format(Config.URL_FIND, s),
                this, this);

        MyVolley.getRequestQueue().add(myReq);
    }

    @Override
    public void onResponse(String s) {
        if (s != null && !s.isEmpty()) {
            Find find = new Gson().fromJson(s, Find.class);
            if (find.getCount() > 0) {
                mAdapter.setItems(find.getCities());
            }
        }
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        Toast.makeText(mContext, mContext.getString(R.string.error), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        City city = mAdapter.getItem(position);
        SqlAdapter adapter = Persistence.getAdapter(getActivity());

        Database record = new Database();
        record.setCityId(city.getId());
        record.setCityName(city.getName());

        adapter.store(record);
        mCallback.onSearchItemClick();
        mDrawerLayout.closeDrawer(Gravity.LEFT);
    }

    public interface OnSearchItemClickListener {
        public void onSearchItemClick();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (OnSearchItemClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnSearchItemClickListener");
        }
    }

    public void setUp(DrawerLayout drawerLayout) {

        final ActionBar ab = getActivity().getActionBar();

        mDrawerLayout = drawerLayout;
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        mDrawerToggle = new ActionBarDrawerToggle(
                getActivity(),
                mDrawerLayout,
                R.drawable.ic_navigation_drawer,
                R.string.drawer_open,
                R.string.drawer_close
        ) {
            public void onDrawerClosed(View view) {
                if (!isAdded()) return;
                getActivity().invalidateOptionsMenu();
                ab.setTitle(R.string.app_name);
                inputMethodManager.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
            }

            public void onDrawerOpened(View drawerView) {
                if (!isAdded()) return;
                getActivity().invalidateOptionsMenu();
                ab.setTitle(R.string.add_city);
            }
        };

        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
}
