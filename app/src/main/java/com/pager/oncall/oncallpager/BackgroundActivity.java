package com.pager.oncall.oncallpager;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.gson.Gson;

/**
 * Created by devinb on 1/22/15.
 */
public class BackgroundActivity extends Activity {
    private static SharedPreferences prefs;
    private static Globals globals;
    private static Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_dialog);
        SMSReceiver recv = new SMSReceiver();

        globals = Globals.getInstance();
        gson = new Gson();
        prefs = getSharedPreferences(globals.getSharedPrefFile(), Context.MODE_PRIVATE);
        OncallPager.setPrefs(prefs);

        finish();
    }
}
