package com.test.weather;

import android.app.Application;

import com.codeslap.persistence.DatabaseSpec;
import com.codeslap.persistence.PersistenceConfig;
import com.test.weather.models.Database;
import com.test.weather.volley.MyVolley;

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        MyVolley.init(this);

        DatabaseSpec database = PersistenceConfig.registerSpec(1);
        database.match(Database.class);
    }
}