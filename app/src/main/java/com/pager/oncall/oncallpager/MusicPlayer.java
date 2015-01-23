package com.pager.oncall.oncallpager;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;

/**
 * Created by devinb on 1/21/15.
 */
public class MusicPlayer {
    public static MediaPlayer player = null;

    public static void startPlaying(Context ctx, int raw_id){
        if (player == null) {
            player = MediaPlayer.create(ctx, raw_id);
            player.setLooping(true);
            player.setVolume(100, 100);

            player.start();
        }
    }

    public static void stopPlaying() {
        if (player != null && player.isPlaying()) {
            player.stop();
        }
    }
}
