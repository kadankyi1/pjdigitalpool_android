package org.christecclesia.pjdigitalpool.BroadcastReceivers;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import org.christecclesia.pjdigitalpool.Activities.AudioPlayerActivity;
import org.christecclesia.pjdigitalpool.Inc.Util;

public class MyNotificationReceiver extends BroadcastReceiver {
    public static int REQUEST_CODE_NOTIFICATION = 1212;
    public static int REQUEST_CODE = 10;
    public static final String RESUME_ACTION = "RESUME_ACTION";
    public static final String STOP_ACTION = "STOP_ACTION";
    public static final String CANCEL_ACTION = "CANCEL_ACTION";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        Util.show_log_in_console("AUDIONOTIFICATION", action);

        if (intent.getAction() != null) {
            switch (intent.getAction()) {
                case RESUME_ACTION :
                    if(AudioPlayerActivity.mPlayer != null){
                        if(!AudioPlayerActivity.mPlayer.isPlaying()){
                            AudioPlayerActivity.mPlayer.start();
                        }
                    }
                    // you resume action
                    break;
                case STOP_ACTION :
                    if(AudioPlayerActivity.mPlayer != null){
                        if(AudioPlayerActivity.mPlayer.isPlaying()){
                            AudioPlayerActivity.mPlayer.pause();
                        }
                    }
                    // you stop action
                    break;
                case CANCEL_ACTION:
                    if(AudioPlayerActivity.mPlayer != null){
                        AudioPlayerActivity.mPlayer.stop();
                    }
                    String ns = Context.NOTIFICATION_SERVICE;
                    NotificationManager nMgr = (NotificationManager) context.getSystemService(ns);
                    nMgr.cancelAll();
                    Toast.makeText(context, "AUDIONOTIFICATION: stop", Toast.LENGTH_SHORT).show();// you cancel action
                    break;
            }
        }
    }



}
