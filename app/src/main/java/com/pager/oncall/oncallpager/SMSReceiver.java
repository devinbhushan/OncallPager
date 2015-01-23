package com.pager.oncall.oncallpager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import java.util.HashMap;

public class SMSReceiver extends BroadcastReceiver {
    private final String DEBUG_TAG = getClass().getSimpleName();
    private static final String ACTION_SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    private Context mContext;
    private Intent mIntent;
    private HashMap<String, Boolean> patterns;

    public SMSReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        mIntent = intent;

        String action = intent.getAction();

        if(action.equals(ACTION_SMS_RECEIVED)) {
            SmsMessage[] messageList = getMessagesFromIntent(intent);

            if (messageList != null) {
                for (int i = 0; i < messageList.length; i++) {
                    handleMessage(messageList[i]);
                }
            }
        }
    }

    private void handleMessage(SmsMessage msg) {
        String address = msg.getOriginatingAddress();
        String messageText = msg.getMessageBody();
        patterns = OncallPager.getPatterns();

        for (String pattern : patterns.keySet()) {
            if (patterns.get(pattern) && messageText.contains(pattern)) {
                MusicPlayer.startPlaying(mContext, R.raw.heartless);

                Intent i = new Intent();
                i.setClassName("com.pager.oncall.oncallpager", "com.pager.oncall.oncallpager.PageDialog");
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mContext.startActivity(i);

                AudioManager audioManager = (AudioManager)mContext.getSystemService(Context.AUDIO_SERVICE);

                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC) / 2, 0);

                return;
            }
        }
    }

    private SmsMessage[] getMessagesFromIntent(Intent intent) {
        Object[] messages = (Object[]) intent.getSerializableExtra("pdus");
        byte[][] pduObjs = new byte[messages.length][];

        for (int i = 0; i < messages.length; i++) {
            pduObjs[i] = (byte[]) messages[i];
        }
        byte[][] pdus = new byte[pduObjs.length][];
        int pduCount = pdus.length;
        SmsMessage[] msgs = new SmsMessage[pduCount];
        for (int i = 0; i < pduCount; i++) {
            pdus[i] = pduObjs[i];
            msgs[i] = SmsMessage.createFromPdu(pdus[i]);
        }

        return msgs;
    }
}
