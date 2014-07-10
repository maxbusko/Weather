package com.test.weather.fragments;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.codeslap.persistence.Persistence;
import com.codeslap.persistence.SqlAdapter;
import com.google.gson.Gson;
import com.test.weather.Config;
import com.test.weather.DatabaseLoader;
import com.test.weather.R;
import com.test.weather.Utils;
import com.test.weather.adapters.CitiesAdapter;
import com.test.weather.models.City;
import com.test.weather.models.Database;
import com.test.weather.models.Group;
import com.test.weather.volley.MyVolley;

import de.timroes.android.listview.EnhancedListView;

public class MainFragment extends Fragment implements AdapterView.OnItemClickListener, Response.Listener<String>, Response.ErrorListener {

    private Activity mContext;
    private CitiesAdapter mAdapter;
    private EnhancedListView mListView;
    private Menu optionsMenu;
    private StringRequest mRequest;
    private Loader<String> mLoader;
    private SqlAdapter sqlAdapter;
    private OnCityItemClickListener mCallback;
    private String mIds;

    @Override
    public void onResume() {
        super.onResume();
        mLoader.forceLoad();

        ActionBar ab = getActivity().getActionBar();
        ab.setDisplayShowTitleEnabled(true);
        ab.setTitle(R.string.app_name);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);
        ab.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        sqlAdapter = Persistence.getAdapter(getActivity());
        mLoader = getLoaderManager().initLoader(0, null, new LoaderManager.LoaderCallbacks<String>() {
            @Override
            public Loader<String> onCreateLoader(int id, Bundle args) {
                return new DatabaseLoader(getActivity());
            }

            @Override
            public void onLoadFinished(Loader<String> loader, String data) {
                mIds = data;
                doRequest();
            }

            @Override
            public void onLoaderReset(Loader<String> loader) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        mAdapter = new CitiesAdapter(mContext);

        mListView = (EnhancedListView) view.findViewById(R.id.listview);
        mListView.setDismissCallback(new EnhancedListView.OnDismissCallback() {
            @Override
            public EnhancedListView.Undoable onDismiss(EnhancedListView enhancedListView, final int i) {
                final City item = (City) mAdapter.getItem(i);
                mAdapter.remove(i);

                return new EnhancedListView.Undoable() {
                    @Override
                    public void undo() {
                        mAdapter.insert(item, i);
                    }

                    @Override
                    public void discard() {
                        Database record = new Database();
                        record.setCityId(item.getId());
                        sqlAdapter.delete(record);
                    }
                };
            }
        });
        mListView.enableSwipeToDismiss();
        mListView.setUndoHideDelay(3000);
        mListView.setRequireTouchBeforeDismiss(false);
        mListView.setUndoStyle(EnhancedListView.UndoStyle.COLLAPSED_POPUP);
        mListView.setOnItemClickListener(this);
        mListView.setAdapter(mAdapter);

        return view;
    }

    private void doRequest() {
        setRefreshActionButtonState(true);

        mRequest = new StringRequest(Request.Method.GET,
                String.format(Config.URL_MAIN, mIds),
                this, this);
        MyVolley.getRequestQueue().add(mRequest);
    }

    @Override
    public void onResponse(String s) {
        if (s != null && !s.isEmpty() && Utils.isJSONValid(s)) {
            Group group = new Gson().fromJson(s, Group.class);
            if (group.getCnt() > 0) {
                mAdapter.setItems(group.getCities());
            }
        } else {
            mRequest.deliverError(null);
        }
        setRefreshActionButtonState(false);
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        if(mIds != null){
            String s = MyVolley.getCache(String.format(Config.URL_MAIN, mIds));
            //s = "{\"cnt\":2,\"list\":[{\"coord\":{\"lon\":37.62,\"lat\":55.75},\"sys\":{\"message\":0.0332,\"country\":\"RU\",\"sunrise\":1404781041,\"sunset\":1404843120},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"ясно\",\"icon\":\"01n\"}],\"main\":{\"temp\":17.56,\"humidity\":69,\"pressure\":1003.7,\"temp_min\":16,\"temp_max\":20.3},\"wind\":{\"speed\":2.02,\"deg\":58.0001},\"clouds\":{\"all\":0},\"dt\":1404855739,\"id\":524901,\"name\":\"Moscow\"},{\"coord\":{\"lon\":30.26,\"lat\":59.89},\"sys\":{\"message\":0.0364,\"country\":\"RU\",\"sunrise\":1404780735,\"sunset\":1404846955},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"ясно\",\"icon\":\"01n\"}],\"main\":{\"temp\":14,\"pressure\":1020,\"humidity\":82,\"temp_min\":14,\"temp_max\":14},\"wind\":{\"speed\":1.97,\"deg\":45.0001},\"clouds\":{\"all\":0},\"dt\":1404853200,\"id\":498817,\"name\":\"Saint Petersburg\"}]}";
            if (s != null && !s.isEmpty() && Utils.isJSONValid(s)) {
                Group group = new Gson().fromJson(s, Group.class);
                if (group.getCnt() > 0) {
                    mAdapter.setItems(group.getCities());
                }
            } else {
                Toast.makeText(mContext, mContext.getString(R.string.error), Toast.LENGTH_LONG).show();
            };
        }
        setRefreshActionButtonState(false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        optionsMenu = menu;
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                mLoader.forceLoad();
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mCallback.onCityItemClick(mAdapter.getItem(position).getId());
    }

    public void updateList() {
        if (mLoader != null) {
            mLoader.forceLoad();
        }
    }

    public interface OnCityItemClickListener {
        public void onCityItemClick(int cityId);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (OnCityItemClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnCityItemClickListener");
        }
    }
}
