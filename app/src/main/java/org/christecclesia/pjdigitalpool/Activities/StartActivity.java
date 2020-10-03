package org.christecclesia.pjdigitalpool.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.christecclesia.pjdigitalpool.Adapters.StartActivitySliderAdapter;
import org.christecclesia.pjdigitalpool.Inc.Util;
import org.christecclesia.pjdigitalpool.R;

public class StartActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewPager mSlideViewPager;
    private Button mNextBtn;
    private StartActivitySliderAdapter startActivitySliderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        String access_token = Util.getSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_USER_TOKEN);
        int user_id = Util.getSharedPreferenceInt(getApplicationContext(), Util.SHARED_PREF_KEY_USER_ID);
        String user_first_name = Util.getSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_USER_FIRST_NAME);
        String user_last_name = Util.getSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_USER_LAST_NAME);
        String user_country = Util.getSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_USER_COUNTRY);
        String user_phone = Util.getSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_USER_PHONE);
        String user_email = Util.getSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_USER_EMAIL);
        int user_flagged = Util.getSharedPreferenceInt(getApplicationContext(), Util.SHARED_PREF_KEY_USER_FLAGGED);

        if(     !access_token.trim().equalsIgnoreCase("")
                && user_id > 0
                && !user_first_name.trim().equalsIgnoreCase("")
                && !user_last_name.trim().equalsIgnoreCase("")
                && !user_country.trim().equalsIgnoreCase("")
                && !user_phone.trim().equalsIgnoreCase("")
                && !user_email.trim().equalsIgnoreCase("")
                && user_flagged == 0){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }

        mSlideViewPager = findViewById(R.id.slider_activity_view_pager);
        mNextBtn = findViewById(R.id.slideractivity_button_next);

        startActivitySliderAdapter = new StartActivitySliderAdapter(this);
        mSlideViewPager.setAdapter(startActivitySliderAdapter);

        mSlideViewPager.setOffscreenPageLimit(0);

        mNextBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == mNextBtn.getId()){
            if(mSlideViewPager.getCurrentItem() == 1){
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(intent);
                finish();
            } else {
                mSlideViewPager.setCurrentItem(mSlideViewPager.getCurrentItem()+1);
            }
        }

    }

}
