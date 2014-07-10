package com.test.weather;

import android.content.Context;
import android.preference.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
    public static boolean isJSONValid(String test) {
        try {
            new JSONObject(test);
        } catch (JSONException ex) {
            try {
                new JSONArray(test);
            } catch (JSONException e) {
                return false;
            }
        }
        return true;
    }

    private static final String FIRST_START_KEY = "firststartkey";

    public static boolean isFirstStart(Context c){
        return PreferenceManager.getDefaultSharedPreferences(c).getBoolean(FIRST_START_KEY, true);
    }

    public static void setFirstStart(Context c){
        PreferenceManager.getDefaultSharedPreferences(c).edit().putBoolean(FIRST_START_KEY, false).commit();
    }

    private static final String MENU_DAY_CNT = "menu_days_cnt";

    public static int getMenuDayCnt(Context c){
        return PreferenceManager.getDefaultSharedPreferences(c).getInt(MENU_DAY_CNT, 3);
    }

    public static void setMenuDaysCnt(Context c, int cnt){
        PreferenceManager.getDefaultSharedPreferences(c).edit().putInt(MENU_DAY_CNT, cnt).commit();
    }

    public static String formatDay(long dt){
        SimpleDateFormat ft = new SimpleDateFormat("E, d MMM");
        return ft.format(new Date(dt*1000));
    }
}
