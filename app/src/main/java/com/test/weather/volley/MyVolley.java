package com.test.weather.volley;

import android.content.Context;

import com.android.volley.Cache;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.io.UnsupportedEncodingException;

public class MyVolley {
    private static RequestQueue mRequestQueue;


    private MyVolley() {
    }


    public static void init(Context context) {
        mRequestQueue = Volley.newRequestQueue(context);
    }


    public static RequestQueue getRequestQueue() {
        if (mRequestQueue != null) {
            return mRequestQueue;
        } else {
            throw new IllegalStateException("RequestQueue not initialized");
        }
    }

    public static String getCache(String url) {
        Cache cache = getRequestQueue().getCache();
        Cache.Entry entry = cache.get(url);
        if (entry != null) {
            try {
                return new String(entry.data, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                return "";
            }
        } else {
            return "";
        }
    }
}