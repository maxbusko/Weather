package com.test.weather;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.codeslap.persistence.Persistence;
import com.codeslap.persistence.SqlAdapter;
import com.test.weather.models.Database;

import java.util.List;

public class DatabaseLoader extends AsyncTaskLoader<String> {
    private final Context mContext;

    public DatabaseLoader(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if (takeContentChanged())
            forceLoad();
    }

    @Override
    public String loadInBackground() {
        StringBuilder sb = new StringBuilder();
        SqlAdapter adapter = Persistence.getAdapter(mContext);
        List<Database> records = adapter.findAll(Database.class);
        for(Database record : records){
            sb.append(record.getCityId()).append(",");
        }
        return sb.toString();
    }

    @Override
    public void deliverResult(String data) {
        super.deliverResult(data);
    }
}
