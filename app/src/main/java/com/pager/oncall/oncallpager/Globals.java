package com.pager.oncall.oncallpager;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by devinb on 1/20/15.
 */
public class Globals{
    private static Globals instance;

    // Global variable
    private HashMap<String, Boolean> patterns = new HashMap<String, Boolean>();
    private static final String SHARED_PREF_FILE = "oncall_pager_settings";
    // Restrict the constructor from being instantiated
    private Globals() {}

    public void addPattern(String pattern) {
        this.patterns.put(pattern, true);
    }
    public HashMap<String, Boolean> getPattern() {
        return this.patterns;
    }

    public void setPattern(String pattern, boolean selected) {
        this.patterns.put(pattern, selected);
    }
    public String getSharedPrefFile() {
        return this.SHARED_PREF_FILE;
    }

    public static synchronized Globals getInstance() {
        if(instance == null){
            instance = new Globals();
        }

        return instance;
    }
}
