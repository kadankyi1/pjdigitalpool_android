package org.christecclesia.pjdigitalpool.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.christecclesia.pjdigitalpool.Inc.Util;
import org.christecclesia.pjdigitalpool.R;
import org.christecclesia.pjdigitalpool.Views.RoundedCornerImageView;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class AudioPlayerActivity extends AppCompatActivity implements View.OnClickListener {

    private CheckBox m_favorite_icon, m_play_icon_checkbox;
    private ImageView m_back_imageview, m_rewind_icon_imageview, m_forward_icon_imageview;
    private RoundedCornerImageView m_audio_image_imageview;
    private TextView m_audio_title_textview, m_uploadtime_textview, m_start_time_textview, m_end_time_textview;
    private SeekBar m_audio_scruber_seekbar;
    private static int oTime =0, sTime =0, eTime =0, fTime = 5000, bTime = 5000;
    private MediaPlayer mPlayer = null;
    private Handler hdlr = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_player);

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


        m_audio_title_textview.setText(Util.getSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_AUDIO_PLAYER_TITLE));
        m_uploadtime_textview.setText(Util.getSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_AUDIO_PLAYER_UPLOAD_TIME));

        if(!this.isFinishing() && !Util.getSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_AUDIO_PLAYER_TITLE).equalsIgnoreCase("")){
            Util.loadImageView(getApplicationContext(), Util.getSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_AUDIO_PLAYER_IMG_URL), m_audio_image_imageview, null);
        }

        m_back_imageview.setOnClickListener(this);
        m_favorite_icon.setOnClickListener(this);
        m_audio_image_imageview.setOnClickListener(this);
        m_rewind_icon_imageview.setOnClickListener(this);
        m_play_icon_checkbox.setOnClickListener(this);
        m_forward_icon_imageview.setOnClickListener(this);


    }

    private Runnable UpdateSongTime = new Runnable() {
        @Override
        public void run() {
            sTime = mPlayer.getCurrentPosition();
            m_start_time_textview.setText(String.format("%d:%d", TimeUnit.MILLISECONDS.toMinutes(sTime),
                    TimeUnit.MILLISECONDS.toSeconds(sTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(sTime))) );
            m_audio_scruber_seekbar.setProgress(sTime);
            hdlr.postDelayed(this, 100);
        }
    };

    @Override
    public void onClick(View view)
    {
        if(view.getId() == m_back_imageview.getId()){
            onBackPressed();
        } else if(view.getId() == m_favorite_icon.getId()){
            if(m_favorite_icon.isChecked()){
                // ADD TO FAVORITES ARRAY
                Toast.makeText(getApplicationContext(), "Added to favories", Toast.LENGTH_SHORT).show();
            } else {
                // REMOVE FROM FAV ARRAY
                Toast.makeText(getApplicationContext(), "Removed from favorites", Toast.LENGTH_SHORT).show();
            }
        } else if(view.getId() == m_rewind_icon_imageview.getId()){
            rewindOnlinAudio();
        } else if(view.getId() == m_play_icon_checkbox.getId()){

            if(!m_play_icon_checkbox.isChecked()){
                m_play_icon_checkbox.setChecked(false);
                if(mPlayer != null){
                    mPlayer.pause();
                    m_play_icon_checkbox.setChecked(false);
                }
            } else {
                m_play_icon_checkbox.setChecked(true);
                Toast.makeText(getApplicationContext(), "NOT Checked", Toast.LENGTH_SHORT).show();
                if(mPlayer != null) {
                    if(!mPlayer.isPlaying()){
                        playOnlineAudio(Util.getSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_AUDIO_PLAYER_AUDIO_URL));
                    }
                } else {
                    playOnlineAudio(Util.getSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_AUDIO_PLAYER_AUDIO_URL));
                }
            }
        } else if(view.getId() == m_forward_icon_imageview.getId()){
            forwardOnlineAudio();
        }
    }

    private void playOnlineAudio(String url)
    {
        Toast.makeText(getApplicationContext(), "url: " + url, Toast.LENGTH_LONG).show();
        mPlayer = new MediaPlayer();
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mPlayer.setDataSource(url);
            mPlayer.prepare(); // might take long! (for buffering, etc)
            mPlayer.start();
            eTime = mPlayer.getDuration();
            sTime = mPlayer.getCurrentPosition();
            if(oTime == 0){
                m_audio_scruber_seekbar.setMax(eTime);
                oTime =1;
            }
            m_end_time_textview.setText(String.format("%d:%d", TimeUnit.MILLISECONDS.toMinutes(eTime),
                    TimeUnit.MILLISECONDS.toSeconds(eTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS. toMinutes(eTime))) );
            m_start_time_textview.setText(String.format("%d:%d", TimeUnit.MILLISECONDS.toMinutes(sTime),
                    TimeUnit.MILLISECONDS.toSeconds(sTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS. toMinutes(sTime))) );
            m_audio_scruber_seekbar.setProgress(sTime);
            hdlr.postDelayed(UpdateSongTime, 100);
        } catch (IOException e) {
            e.printStackTrace();
            m_play_icon_checkbox.setChecked(false);
            Toast.makeText(getApplicationContext(), "Audio Player Error", Toast.LENGTH_LONG).show();
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
}
