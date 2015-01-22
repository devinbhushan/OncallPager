package com.pager.oncall.oncallpager;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.nfc.cardemulation.HostApduService;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class OncallPager extends ActionBarActivity {

    Button add_pattern_button, view_patterns_button;
    private static Globals globals;
    private static Gson gson;
    private static SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oncall_pager);
        SMSReceiver recv = new SMSReceiver();

        globals = Globals.getInstance();
        addListenersOnButton();
        gson = new Gson();
        prefs = getSharedPreferences(globals.getSharedPrefFile(), Context.MODE_PRIVATE);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        SMSReceiver recv = new SMSReceiver();

        globals = Globals.getInstance();
        addListenersOnButton();
        gson = new Gson();
        prefs = getSharedPreferences(globals.getSharedPrefFile(), Context.MODE_PRIVATE);

        return Service.START_STICKY;
    }

    public void showDialog(final String[] pattern_array, final boolean[] checked_vals,
                           final HashMap<String, Boolean> patterns) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Patterns")
                .setCancelable(false)
                .setMultiChoiceItems(pattern_array, checked_vals, new DialogInterface.OnMultiChoiceClickListener() {
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if (isChecked) {
                            patterns.put(pattern_array[which], true);
                        } else {
                            patterns.put(pattern_array[which], false);
                        }
                    }
                })
                .setNeutralButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        writeSharedPref("patterns", patterns);

                        dialog.dismiss();
                    }
                });
        builder.create();
        builder.show();
    }

    private static void writeSharedPref(String key, Object value) {
        SharedPreferences.Editor editor = prefs.edit();

        String json = gson.toJson(value);
        editor.putString(key, json);
        editor.commit();
    }

    private static void addPattern(String pattern, boolean value) {
        HashMap<String, Boolean> patterns = getPatterns();

        patterns.put(pattern, value);
        String json = gson.toJson(patterns);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("patterns", json);
        editor.commit();
    }

    public static HashMap<String, Boolean> getPatterns() {
        HashMap<String, Boolean> patterns = gson.fromJson(prefs.getString("patterns", null), new TypeToken<HashMap<String, Boolean>>() {}.getType());

        if (patterns == null) {
            patterns = new HashMap<String, Boolean>();
        }
        return patterns;
    }

    public static HashMap<String, Boolean> findActivePatterns(HashMap<String, Boolean> patterns) {
        HashMap<String, Boolean> activePatterns = new HashMap<String, Boolean>();
        for (String pattern : patterns.keySet()) {
            if (patterns.get(pattern)) {
                activePatterns.put(pattern, true);
            }
        }

        return activePatterns;
    }

    public void addListenersOnButton() {
        add_pattern_button = (Button) findViewById(R.id.add_pattern);
        view_patterns_button = (Button) findViewById(R.id.view_patterns);

        add_pattern_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                EditText patternField = (EditText)findViewById(R.id.pattern_str);
                String patternText = patternField.getText().toString();

                addPattern(patternText, true);

                Toast.makeText(getBaseContext(), "Added " + patternText, Toast.LENGTH_LONG).show();
            }
        });

        view_patterns_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Log.d("PAGER", "clicked the button!");

                HashMap<String, Boolean> patterns = getPatterns();

                if (patterns.size() > 0) {
                    String[] patternStrings = new String[patterns.size()];
                    boolean[] isSet = new boolean[patterns.size()];

                    int index = 0;
                    for (String pattern : patterns.keySet()) {
                        patternStrings[index] = pattern;
                        isSet[index] = patterns.get(pattern);
                        index++;
                    }

                    showDialog(patternStrings, isSet, patterns);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_oncall_pager, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
