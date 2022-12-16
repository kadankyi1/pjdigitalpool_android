package org.christecclesia.pjdigitalpool.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.christecclesia.pjdigitalpool.BroadcastReceivers.MyNotificationReceiver;
import org.christecclesia.pjdigitalpool.Inc.Util;
import org.christecclesia.pjdigitalpool.R;
import org.christecclesia.pjdigitalpool.Views.RoundedCornerImageView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class AudioPlayerActivity extends AppCompatActivity implements View.OnClickListener {

    private CheckBox m_favorite_icon, m_play_icon_checkbox;
    private ImageView m_back_imageview, m_rewind_icon_imageview, m_forward_icon_imageview;
    private RoundedCornerImageView m_audio_image_imageview;
    private TextView m_audio_title_textview, m_uploadtime_textview, m_start_time_textview, m_end_time_textview;
    private SeekBar m_audio_scruber_seekbar;
    private static int oTime =0, sTime =0, eTime =0, fTime = 5000, bTime = 5000;
    public static MediaPlayer mPlayer = null;
    private Boolean player_started = false;
    private Handler hdlr = new Handler();
    private Thread audio_thread = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_player);

        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager nMgr = (NotificationManager) getApplicationContext().getSystemService(ns);
        nMgr.cancelAll();


        m_back_imageview = findViewById(R.id.activity_audioplayer_backimage_imageview);
        m_favorite_icon = findViewById(R.id.activity_audioplayer_favoriteimage_imageview);
        m_audio_image_imageview = findViewById(R.id.activity_audioplayer_audioimage_roundedcornerimageview);
        m_audio_title_textview = findViewById(R.id.activity_audioplayer_title_textview);
        m_uploadtime_textview = findViewById(R.id.activity_audioplayer_uploadtime_textview);
        m_rewind_icon_imageview = findViewById(R.id.activity_audioplayer_rewindicon_imageview);
        m_play_icon_checkbox = findViewById(R.id.activity_audioplayer_playicon_imageview);
        m_forward_icon_imageview = findViewById(R.id.activity_audioplayer_forwardicon_imageview);
        m_audio_scruber_seekbar =  findViewById(R.id.activity_audioplayer_scrubber_textview);
        m_start_time_textview =  findViewById(R.id.activity_audioplayer_playtime_textview);
        m_end_time_textview =  findViewById(R.id.activity_audioplayer_endtime_textview);


        if(mPlayer != null){
            if(Util.getSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_AUDIO_PLAYER_AUDIO_URL).equalsIgnoreCase(Util.getSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_AUDIO_PLAYER_CURRENT_PLAYING_AUDIO))){
                m_audio_scruber_seekbar.setVisibility(View.INVISIBLE);
                if(mPlayer.isPlaying()){
                    m_play_icon_checkbox.setChecked(true);
                }
            } else {
                mPlayer.stop();
                hdlr.removeCallbacks(UpdateSongTime);
                hdlr.postDelayed(UpdateSongTime, 100);
                mPlayer = null;
            }
        }

        m_audio_title_textview.setText(Util.getSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_AUDIO_PLAYER_TITLE));
        m_uploadtime_textview.setText(Util.getSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_AUDIO_PLAYER_UPLOAD_TIME));

        ArrayList<String> favorites = Util.getSharedPreferenceStringSet(getApplicationContext(), Util.SHARED_PREF_KEY_FAVORITE_AUDIOS);
        if(favorites != null){
            if(favorites.contains(Util.getSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_AUDIO_TRACK_ID))){
                m_favorite_icon.setChecked(true);
            }
        }

        if(!this.isFinishing() && !Util.getSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_AUDIO_PLAYER_IMG_URL).equalsIgnoreCase("")){
            Util.loadImageView(getApplicationContext(), Util.getSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_AUDIO_PLAYER_IMG_URL), m_audio_image_imageview, null);
        }

        if(!this.isFinishing() && !Util.getSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_AUDIO_PLAYER_AUDIO_URL).equalsIgnoreCase("")){
            //jcAudios.add(JcAudio.createFromURL(Util.getSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_AUDIO_PLAYER_AUDIO_URL),Util.getSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_AUDIO_PLAYER_AUDIO_URL)));
        } else {
            Toast.makeText(getApplicationContext(), "Audio not found", Toast.LENGTH_LONG).show();
        }

        m_audio_scruber_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //Toast.makeText(getApplicationContext(), "onStartTrackingTouch seekBar" + seekBar.getProgress(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //Toast.makeText(getApplicationContext(), "onStopTrackingTouch seekBar" + seekBar.getProgress(), Toast.LENGTH_SHORT).show();
                mPlayer.seekTo(seekBar.getProgress());
                m_audio_scruber_seekbar.setProgress(seekBar.getProgress());
            }
        });

        m_back_imageview.setOnClickListener(this);
        m_favorite_icon.setOnClickListener(this);
        m_audio_image_imageview.setOnClickListener(this);
        m_rewind_icon_imageview.setOnClickListener(this);
        m_play_icon_checkbox.setOnClickListener(this);
        m_forward_icon_imageview.setOnClickListener(this);

        m_audio_title_textview.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(!Util.getSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_ARTICLE_ID).isEmpty()) {
                    Toast.makeText(getApplicationContext(), String.valueOf(Util.getSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_ARTICLE_ID)), Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
    }

    private Runnable UpdateSongTime = new Runnable() {
        @Override
        public void run() {
            if(mPlayer != null){
                sTime = mPlayer.getCurrentPosition();
                m_start_time_textview.setText(String.format("%d:%d", TimeUnit.MILLISECONDS.toMinutes(sTime),
                        TimeUnit.MILLISECONDS.toSeconds(sTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(sTime))) );
                m_audio_scruber_seekbar.setProgress(sTime);
                hdlr.postDelayed(this, 100);
            }
        }
    };

    @Override
    public void onClick(View view)
    {
        if(view.getId() == m_back_imageview.getId()){
            onBackPressed();
        } else if(view.getId() == m_favorite_icon.getId()){
            ArrayList<String> favorites = Util.getSharedPreferenceStringSet(getApplicationContext(), Util.SHARED_PREF_KEY_FAVORITE_AUDIOS);
            if(m_favorite_icon.isChecked()){
                // ADD TO FAVORITES ARRAY
                if(favorites == null){
                    favorites = new ArrayList<>();
                    favorites.add(Util.getSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_AUDIO_TRACK_ID));
                    Util.setSharedPreferenceStringSet(getApplicationContext(), Util.SHARED_PREF_KEY_FAVORITE_AUDIOS, favorites);
                    Toast.makeText(getApplicationContext(), "Added to favorites", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!favorites.contains(Util.getSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_AUDIO_TRACK_ID))){
                    favorites.add(Util.getSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_AUDIO_TRACK_ID));
                    Util.setSharedPreferenceStringSet(getApplicationContext(), Util.SHARED_PREF_KEY_FAVORITE_AUDIOS, favorites);
                    Toast.makeText(getApplicationContext(), "Added to favorites", Toast.LENGTH_SHORT).show();
                }
            } else {
                // REMOVE FROM FAV ARRAY
                if(favorites == null){
                    return;
                }
                if(favorites.contains(Util.getSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_AUDIO_TRACK_ID))){
                    favorites.remove(Util.getSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_AUDIO_TRACK_ID));
                    Util.setSharedPreferenceStringSet(getApplicationContext(), Util.SHARED_PREF_KEY_FAVORITE_AUDIOS, favorites);
                    Toast.makeText(getApplicationContext(), "Removed from favorites", Toast.LENGTH_SHORT).show();
                };
            }
        } else if(view.getId() == m_rewind_icon_imageview.getId()){
            rewindOnlinAudio();
        } else if(view.getId() == m_play_icon_checkbox.getId()){

            if(player_started == false) {
                audio_thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (mPlayer != null) {
                            if (mPlayer.isPlaying()) {
                                mPlayer.pause();
                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        m_play_icon_checkbox.setChecked(false);
                                    }
                                });
                            } else {
                                mPlayer.start();
                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        m_play_icon_checkbox.setChecked(true);
                                    }
                                });
                            }
                        } else {
                            if (player_started == false) {
                                playOnlineAudio(Util.getSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_AUDIO_PLAYER_AUDIO_URL));
                            }
                        }
                    }
                });
                audio_thread.start();
            }

        } else if(view.getId() == m_forward_icon_imageview.getId()){
            forwardOnlineAudio();
        }
    }


    private void playOnlineAudio(String url)
    {
        if(player_started == false){
            m_play_icon_checkbox.setClickable(false);
            mPlayer = new MediaPlayer();
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            try {
                mPlayer.setDataSource(url);
                mPlayer.prepare(); // might take long! (for buffering, etc)
                mPlayer.start();
                Util.setSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_AUDIO_PLAYER_CURRENT_PLAYING_AUDIO, Util.getSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_AUDIO_PLAYER_AUDIO_URL));
                eTime = mPlayer.getDuration();
                sTime = mPlayer.getCurrentPosition();
                //if(oTime == 0){
                    m_audio_scruber_seekbar.setMax(eTime);
                    //oTime =1;
                //}
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        m_end_time_textview.setText(String.format("%d:%d", TimeUnit.MILLISECONDS.toMinutes(eTime),
                                TimeUnit.MILLISECONDS.toSeconds(eTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS. toMinutes(eTime))) );
                        m_start_time_textview.setText(String.format("%d:%d", TimeUnit.MILLISECONDS.toMinutes(sTime),
                                TimeUnit.MILLISECONDS.toSeconds(sTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS. toMinutes(sTime))) );
                        m_audio_scruber_seekbar.setProgress(sTime);
                    }
                });
                hdlr.postDelayed(UpdateSongTime, 100);
            } catch (IOException e) {
                e.printStackTrace();
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        m_play_icon_checkbox.setChecked(false);
                        player_started = false;
                        //mPlayer = null;
                        Toast.makeText(getApplicationContext(), "Audio Player Error", Toast.LENGTH_LONG).show();
                    }
                });
            }
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    m_play_icon_checkbox.setClickable(true);
                    player_started = false;
                }
            });
        }
    }



    private void rewindOnlinAudio()
    {
        if((sTime - bTime) > 0) {
            sTime = sTime - bTime;
            mPlayer.seekTo(sTime);
        }
    }

    private void forwardOnlineAudio(){
        if(mPlayer != null){
            if((sTime + fTime) <= eTime) {
                sTime = sTime + fTime;
                mPlayer.seekTo(sTime);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if(mPlayer != null){
            //if(mPlayer.isPlaying()){
            showActionButtonsNotification(Util.getSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_AUDIO_PLAYER_TITLE));
            super.onBackPressed();
            /*
                new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.myDialog))
                        .setTitle("Stop Audio")
                        .setMessage("Use the home if you want to leave the app without stopping audio. Going back will stop the audio track. Continue?")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                mPlayer.stop();
                                mPlayer = null;
                                m_audio_scruber_seekbar = null;
                                hdlr.removeCallbacks(UpdateSongTime);
                                UpdateSongTime = null;
                                finish();
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                */
            //}   else {
                //super.onBackPressed();
            //}
        } else {
            super.onBackPressed();
        }
    }

    private void showActionButtonsNotification(String title) {


        Notification.Builder notif;
        NotificationManager nm;
        notif = new Notification.Builder(getApplicationContext());
        notif.setSmallIcon(R.drawable.logo_real);
        notif.setContentTitle("Audio Playing");
        notif.setContentText(title);
        //Uri path = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        //notif.setSound(path);
        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        Intent yesReceive = new Intent();
        yesReceive.setAction(MyNotificationReceiver.RESUME_ACTION);
        PendingIntent pendingIntentYes = PendingIntent.getBroadcast(this, MyNotificationReceiver.REQUEST_CODE_NOTIFICATION, yesReceive, PendingIntent.FLAG_UPDATE_CURRENT);
        notif.addAction(R.drawable.ic_audio_play_icon, "resume", pendingIntentYes);


        Intent yesReceive2 = new Intent();
        yesReceive2.setAction(MyNotificationReceiver.STOP_ACTION);
        PendingIntent pendingIntentYes2 = PendingIntent.getBroadcast(this, MyNotificationReceiver.REQUEST_CODE_NOTIFICATION, yesReceive2, PendingIntent.FLAG_UPDATE_CURRENT);
        notif.addAction(R.drawable.ic_audio_pause_icon, "pause", pendingIntentYes2);




        Intent maybeReceive2 = new Intent();
        maybeReceive2.setAction(MyNotificationReceiver.CANCEL_ACTION);
        PendingIntent pendingIntentMaybe2 = PendingIntent.getBroadcast(this, MyNotificationReceiver.REQUEST_CODE_NOTIFICATION, maybeReceive2, PendingIntent.FLAG_UPDATE_CURRENT);
        //notif.addAction(R.drawable.exo_icon_stop, "stop", pendingIntentMaybe2);


        assert nm != null;
        nm.notify(MyNotificationReceiver.REQUEST_CODE, notif.getNotification());


    }

}
