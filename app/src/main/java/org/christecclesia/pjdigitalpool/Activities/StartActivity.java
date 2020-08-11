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
    private LinearLayout mDotlayout;
    private Button mNextBtn;
    private TextView[] mDots;
    private StartActivitySliderAdapter startActivitySliderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);


        mSlideViewPager = findViewById(R.id.slider_activity_view_pager);
        mDotlayout = findViewById(R.id.slider_activity_linear_layout);
        mNextBtn = findViewById(R.id.slideractivity_button_next);

        startActivitySliderAdapter = new StartActivitySliderAdapter(this);
        mSlideViewPager.setAdapter(startActivitySliderAdapter);

        // SETTING THE DOTS OF THE SLIDER
        addDotsIndicator(0);

        mSlideViewPager.addOnPageChangeListener(viewListener);
        mSlideViewPager.setOffscreenPageLimit(0);

        mNextBtn.setOnClickListener(this);
    }

    public void addDotsIndicator(int position){

        mDotlayout.removeAllViews();
        mDots = new TextView[2];
        for (int i = 0; i < 2; i++ ){
            mDots[i] = new TextView (this);
            mDots[i].setText(Html.fromHtml("&#8226;"));
            mDots[i].setTextSize(50);
            mDots[i].setTextColor(getResources().getColor(R.color.colorDotInactive));
            mDotlayout.addView(mDots[i]);
        }

        if(mDots.length > 0){
            mDots[position].setTextColor(getResources().getColor(R.color.colorDotActive));
        }

    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener(){
        @Override
        public void onPageScrolled(int i, float v, int i1) {}

        @Override
        public void onPageSelected(int i) {
            addDotsIndicator(i);
        }
        @Override
        public void onPageScrollStateChanged(int state) {}
    };

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
