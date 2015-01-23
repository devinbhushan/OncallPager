package com.pager.oncall.oncallpager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by devinb on 1/21/15.
 */

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;

public class PageDialog extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Pager Alert!")
                .setNeutralButton("Stop", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        MusicPlayer.stopPlaying();
                        finish();
                    }
                });
        // Create the AlertDialog object and return it
        builder.create();
        builder.show();
    }
}
