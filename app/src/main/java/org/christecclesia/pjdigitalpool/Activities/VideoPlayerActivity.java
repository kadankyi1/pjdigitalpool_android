package org.christecclesia.pjdigitalpool.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.potyvideo.library.AndExoPlayerView;

import org.christecclesia.pjdigitalpool.Inc.Util;
import org.christecclesia.pjdigitalpool.R;

public class VideoPlayerActivity extends AppCompatActivity {

    AndExoPlayerView m_videoplayer_andexoplayerview;
    TextView m_video_title_textview, m_video_upload_time_textview, m_video_body_textview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        m_videoplayer_andexoplayerview = findViewById(R.id.andExoPlayerView);
        m_video_title_textview = findViewById(R.id.activity_videoplayer_title_textview);
        m_video_upload_time_textview = findViewById(R.id.activity_videplayer_uploadtime_textview);
        m_video_body_textview = findViewById(R.id.activity_videplayer_videodescription_textview);

        m_video_title_textview.setText(Util.getSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_VIDEO_PLAYER_TITLE));
        m_video_upload_time_textview.setText(Util.getSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_VIDEO_PLAYER_UPLOAD_TIME));
        m_video_body_textview.setText(Util.getSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_VIDEO_PLAYER_BODY));

        if(!this.isFinishing() && !Util.getSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_VIDEO_PLAYER_VIDEO_URL).equalsIgnoreCase("")){
            m_videoplayer_andexoplayerview.setSource(Util.getSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_VIDEO_PLAYER_VIDEO_URL));
        } else {
            Toast.makeText(getApplicationContext(), "Video Not Found", Toast.LENGTH_LONG).show();
        }

        m_video_title_textview.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(!Util.getSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_ARTICLE_ID).isEmpty()) {
                    Toast.makeText(getApplicationContext(), String.valueOf(Util.getSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_ARTICLE_ID)), Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        //if(m_videoplayer_andexoplayerview.isEnabled())
        m_videoplayer_andexoplayerview.pausePlayer();
    }

}
