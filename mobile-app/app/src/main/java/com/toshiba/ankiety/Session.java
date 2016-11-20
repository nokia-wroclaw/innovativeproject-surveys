package com.toshiba.ankiety;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Session {

    private SharedPreferences prefs;

    public Session(Context cntx) {
        prefs = PreferenceManager.getDefaultSharedPreferences(cntx);
    }

    public void setusename(String usename) {
        prefs.edit().putString("usename", usename).commit();
        //prefsCommit;
    }

    public String getusename() {
        String usename = prefs.getString("usename","");
        return usename;
    }
}