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
import org.christecclesia.pjdigitalpool.R;

public class StartActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewPager mSlideViewPager;
    private Button mNextBtn;
    private StartActivitySliderAdapter startActivitySliderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);


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
