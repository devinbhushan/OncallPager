package com.pager.oncall.oncallpager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class OncallPager extends ActionBarActivity {

    Button add_pattern_button, view_patterns_button;
    Globals globals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oncall_pager);
        SMSReceiver recv = new SMSReceiver();

        globals = Globals.getInstance();
        addListenersOnButton();
    }

    public void addListenersOnButton() {
        add_pattern_button = (Button) findViewById(R.id.add_pattern);
        view_patterns_button = (Button) findViewById(R.id.view_patterns);

        add_pattern_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                EditText patternField = (EditText)findViewById(R.id.pattern_str);
                String patternText = patternField.getText().toString();
                globals.addPattern(patternText);
                Toast.makeText(getBaseContext(), "Added " + patternText, Toast.LENGTH_LONG).show();
            }
        });

        view_patterns_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        getBaseContext());

                // set title
                alertDialogBuilder.setTitle("Patterns");

                // set dialog message
                alertDialogBuilder
                        .setNegativeButton("Close",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
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
