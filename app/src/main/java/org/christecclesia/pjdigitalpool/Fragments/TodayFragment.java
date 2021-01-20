package org.christecclesia.pjdigitalpool.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.christecclesia.pjdigitalpool.Activities.AudioPlayerActivity;
import org.christecclesia.pjdigitalpool.Activities.ImageArticleActivity;
import org.christecclesia.pjdigitalpool.Activities.MainActivity;
import org.christecclesia.pjdigitalpool.Activities.VideoPlayerActivity;
import org.christecclesia.pjdigitalpool.Adapters.AdBannerSliderAdapter;
import org.christecclesia.pjdigitalpool.Inc.Util;
import org.christecclesia.pjdigitalpool.R;
import org.christecclesia.pjdigitalpool.Views.CircleImageView;
import org.christecclesia.pjdigitalpool.Views.RoundedCornerImageView;

import static org.christecclesia.pjdigitalpool.Inc.Util.SHARED_PREF_KEY_IMAGE_ARTICLE_ARTICLE_BODY;
import static org.christecclesia.pjdigitalpool.Inc.Util.SHARED_PREF_KEY_IMAGE_ARTICLE_ARTICLE_TITLE;
import static org.christecclesia.pjdigitalpool.Inc.Util.SHARED_PREF_KEY_IMAGE_ARTICLE_IMG_URL;
import static org.christecclesia.pjdigitalpool.Inc.Util.SHARED_PREF_KEY_IMAGE_ARTICLE_TAG_TEXT;
import static org.christecclesia.pjdigitalpool.Inc.Util.SHARED_PREF_KEY_IMAGE_ARTICLE_UPLOAD_TIME;
import static org.christecclesia.pjdigitalpool.Inc.Util.setSharedPreferenceString;

public class TodayFragment extends Fragment implements View.OnClickListener {

    private RoundedCornerImageView m_todaybanner_roundedcornerimageview, m_todayaudio_roundedcornerimageview;
    private CircleImageView m_todayvideo1_image_circleimageview, m_todayvideo2_image_circleimageview;
    private ProgressBar m_todaybanner_progressbar;
    private TextView m_welcome_textview, m_todayaudio_title_textview, m_todayaudio_body_textview, m_todayvideo1_title_textview, m_todayvideo1_length_textview, m_todayvideo2_title_textview, m_todayvideo2_length_textview;
    private ConstraintLayout m_todayaudio_play_imageview, m_todayaudio_holder_constraintlayout, m_todayaudio_listen_label_constraintlayout, m_todayvideo1_holder_constraintlayout, m_todayvideo2_holder_constraintlayout;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ViewPager mSlideViewPager;
    private LinearLayout mDotlayout;
    private TextView[] mDots;
    private AdBannerSliderAdapter sliderAdapter;

    private String mParam1;
    private String mParam2;

    public TodayFragment() {
        // Required empty public constructor
    }

    public static TodayFragment newInstance(String param1, String param2) {
        TodayFragment fragment = new TodayFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_today, container, false);
        m_welcome_textview = view.findViewById(R.id.fragment_today_welcome_textview);
        mSlideViewPager = view.findViewById(R.id.fragment_today_newsinfobanner_viewpager);
        mDotlayout = view.findViewById(R.id.fragment_today_newsinfobannerdots_linearlayout);
        //m_todaybanner_roundedcornerimageview = view.findViewById(R.id.fragment_today_newsinfobanner_roundedcornerimageview);
        //m_todaybanner_progressbar = view.findViewById(R.id.fragment_today_newsinfobannerloader_progressbar);
        m_todayaudio_holder_constraintlayout = view.findViewById(R.id.fragment_today_heraldofgloryholder_constraintlayout);
        m_todayaudio_roundedcornerimageview = view.findViewById(R.id.fragment_today_heraldofgloryimage_roundedcornerimageview);
        m_todayaudio_title_textview = view.findViewById(R.id.fragment_today_heraldofglorytitle_textview);
        m_todayaudio_body_textview = view.findViewById(R.id.fragment_today_heraldofglorybody_textview);
        m_todayaudio_play_imageview = view.findViewById(R.id.fragment_today_heraldofgloryplayicon_imageview);
        m_todayaudio_listen_label_constraintlayout = view.findViewById(R.id.fragment_today_today_listen_audio_label_textview);
        m_todayvideo1_holder_constraintlayout = view.findViewById(R.id.fragment_today_audio1holder_constraintlayout);
        m_todayvideo1_image_circleimageview = view.findViewById(R.id.fragment_today_audio1image_roundedcornerimageview);
        m_todayvideo1_title_textview = view.findViewById(R.id.fragment_today_audio1title_textview);
        m_todayvideo1_length_textview = view.findViewById(R.id.fragment_today_audio1body_textview);
        m_todayvideo2_holder_constraintlayout = view.findViewById(R.id.fragment_today_audio2holder_constraintlayout);
        m_todayvideo2_image_circleimageview = view.findViewById(R.id.fragment_today_audio2image_roundedcornerimageview);
        m_todayvideo2_title_textview = view.findViewById(R.id.fragment_today_audio2title_textview);
        m_todayvideo2_length_textview = view.findViewById(R.id.fragment_today_audio2body_textview);

        sliderAdapter = new AdBannerSliderAdapter(getActivity().getApplicationContext());
        mSlideViewPager.setAdapter(sliderAdapter);
        addDotsIndicator(0);
        mSlideViewPager.addOnPageChangeListener(viewListener);
        //mSlideViewPager.setOffscreenPageLimit(0);

        m_todayaudio_holder_constraintlayout.setOnClickListener(this);
        m_todayaudio_play_imageview.setOnClickListener(this);
        m_todayaudio_listen_label_constraintlayout.setOnClickListener(this);
        //m_todayaudio_body_textview.setOnClickListener(this);
        m_todayvideo1_holder_constraintlayout.setOnClickListener(this);
        //m_todayvideo1_image_circleimageview.setOnClickListener(this);
        //m_todayvideo1_title_textview.setOnClickListener(this);
        //m_todayvideo1_length_textview.setOnClickListener(this);
        m_todayvideo2_holder_constraintlayout.setOnClickListener(this);
        //m_todayvideo2_image_circleimageview.setOnClickListener(this);
        //m_todayvideo2_title_textview.setOnClickListener(this);
        //m_todayvideo2_length_textview.setOnClickListener(this);

        m_welcome_textview.append(" " + Util.getSharedPreferenceString(getActivity().getApplicationContext(), Util.SHARED_PREF_KEY_USER_FIRST_NAME));

        if(!getActivity().isFinishing() && !Util.getSharedPreferenceString(getActivity().getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_INFO_BANNER_IMG_URL).equalsIgnoreCase("")){
            //Util.loadImageView(getActivity().getApplicationContext(), Util.getSharedPreferenceString(getActivity().getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_INFO_BANNER_IMG_URL), m_todaybanner_roundedcornerimageview, m_todaybanner_progressbar);
        }

        if(
                !getActivity().isFinishing()
                        && !Util.getSharedPreferenceString(getActivity().getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_AUDIO_IMG_URL).isEmpty()
                        && !Util.getSharedPreferenceString(getActivity().getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_AUDIO_IMG_URL).isEmpty()
                        && !Util.getSharedPreferenceString(getActivity().getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_AUDIO_TITLE).isEmpty()
                        && !Util.getSharedPreferenceString(getActivity().getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_AUDIO_BODY).isEmpty()
        ){

            Util.loadImageView(getActivity().getApplicationContext(), Util.getSharedPreferenceString(getActivity().getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_AUDIO_IMG_URL), m_todayaudio_roundedcornerimageview, null);
            m_todayaudio_title_textview.setText(Util.getSharedPreferenceString(getActivity().getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_AUDIO_TITLE));
            m_todayaudio_body_textview.setText(Util.getSharedPreferenceString(getActivity().getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_AUDIO_BODY));
        } else {
            m_todayaudio_holder_constraintlayout.setVisibility(View.GONE);
        }

        if(
                !getActivity().isFinishing()
                        && !Util.getSharedPreferenceString(getActivity().getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_VIDEO1_URL).isEmpty()
                        && !Util.getSharedPreferenceString(getActivity().getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_VIDEO1_IMG_URL).isEmpty()
                        && !Util.getSharedPreferenceString(getActivity().getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_VIDEO1_TITLE).isEmpty()
                        && !Util.getSharedPreferenceString(getActivity().getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_VIDEO1_LENGTH).isEmpty()
        ){
            Util.loadImageView(getActivity().getApplicationContext(), Util.getSharedPreferenceString(getActivity().getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_VIDEO1_IMG_URL), m_todayvideo1_image_circleimageview, null);
            m_todayvideo1_title_textview.setText(Util.getSharedPreferenceString(getActivity().getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_VIDEO1_TITLE));
            m_todayvideo1_length_textview.setText(Util.getSharedPreferenceString(getActivity().getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_VIDEO1_LENGTH));
        } else {
            m_todayvideo1_holder_constraintlayout.setVisibility(View.GONE);
        }

        if(
                !getActivity().isFinishing()
                        && !Util.getSharedPreferenceString(getActivity().getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_VIDEO2_URL).isEmpty()
                        && !Util.getSharedPreferenceString(getActivity().getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_VIDEO2_IMG_URL).isEmpty()
                        && !Util.getSharedPreferenceString(getActivity().getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_VIDEO2_TITLE).isEmpty()
                        && !Util.getSharedPreferenceString(getActivity().getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_VIDEO2_LENGTH).isEmpty()
        ){
            Util.loadImageView(getActivity().getApplicationContext(), Util.getSharedPreferenceString(getActivity().getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_VIDEO2_IMG_URL), m_todayvideo2_image_circleimageview, null);
            m_todayvideo2_title_textview.setText(Util.getSharedPreferenceString(getActivity().getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_VIDEO2_TITLE));
            m_todayvideo2_length_textview.setText(Util.getSharedPreferenceString(getActivity().getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_VIDEO2_LENGTH));
        } else {
            m_todayvideo2_holder_constraintlayout.setVisibility(View.GONE);
        }

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mSlideViewPager.setCurrentItem(1);
            }
        }, 1500);


        return view;
    }

    public void addDotsIndicator(int position){

        mDotlayout.removeAllViews();
        mDots = new TextView[2];
        for (int i = 0; i < 2; i++ ){
            mDots[i] = new TextView (getActivity());
            mDots[i].setText(Html.fromHtml("&#8226;"));
            mDots[i].setTextSize(25);
            mDots[i].setTextColor(getResources().getColor(R.color.colorAccent));
            mDotlayout.addView(mDots[i]);
        }

        if(mDots.length > 0){
            mDots[position].setTextColor(getResources().getColor(R.color.colorPrimaryDark));
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
        if(
                view.getId() == R.id.fragment_today_heraldofgloryholder_constraintlayout){
            if(
                    !getActivity().isFinishing()
                            && !Util.getSharedPreferenceString(getActivity().getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_AUDIO_IMG_URL).isEmpty()
                            && !Util.getSharedPreferenceString(getActivity().getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_AUDIO_IMG_URL).isEmpty()
                            && !Util.getSharedPreferenceString(getActivity().getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_AUDIO_TITLE).isEmpty()
                            && !Util.getSharedPreferenceString(getActivity().getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_AUDIO_BODY).isEmpty()
            ){
                setSharedPreferenceString(getActivity().getApplicationContext(), SHARED_PREF_KEY_IMAGE_ARTICLE_IMG_URL, Util.getSharedPreferenceString(getActivity().getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_AUDIO_IMG_URL));
                setSharedPreferenceString(getActivity().getApplicationContext(), SHARED_PREF_KEY_IMAGE_ARTICLE_TAG_TEXT, "BIBLE READING PLAN");
                setSharedPreferenceString(getActivity().getApplicationContext(), SHARED_PREF_KEY_IMAGE_ARTICLE_UPLOAD_TIME, "Recent");
                setSharedPreferenceString(getActivity().getApplicationContext(), SHARED_PREF_KEY_IMAGE_ARTICLE_ARTICLE_TITLE, Util.getSharedPreferenceString(getActivity().getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_AUDIO_TITLE));
                setSharedPreferenceString(getActivity().getApplicationContext(), SHARED_PREF_KEY_IMAGE_ARTICLE_ARTICLE_BODY, Util.getSharedPreferenceString(getActivity().getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_AUDIO_BODY));
                Intent intent = new Intent(getActivity().getApplicationContext(), ImageArticleActivity.class);
                startActivity(intent);

            }
        } else if(
                view.getId() == R.id.fragment_today_today_listen_audio_label_textview ||
                        view.getId() == R.id.fragment_today_heraldofgloryplayicon_imageview){
            Util.setSharedPreferenceString(getActivity().getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_AUDIO_TRACK_ID, Util.getSharedPreferenceString(getActivity().getApplicationContext(), "2"));
            Util.setSharedPreferenceString(getActivity().getApplicationContext(), Util.SHARED_PREF_KEY_AUDIO_PLAYER_IMG_URL, Util.getSharedPreferenceString(getActivity().getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_AUDIO_IMG_URL));
            Util.setSharedPreferenceString(getActivity().getApplicationContext(), Util.SHARED_PREF_KEY_AUDIO_PLAYER_AUDIO_URL, Util.getSharedPreferenceString(getActivity().getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_AUDIO_TRACK_URL));
            Util.setSharedPreferenceString(getActivity().getApplicationContext(), Util.SHARED_PREF_KEY_AUDIO_PLAYER_TITLE, Util.getSharedPreferenceString(getActivity().getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_AUDIO_TITLE));
            Util.setSharedPreferenceString(getActivity().getApplicationContext(), Util.SHARED_PREF_KEY_AUDIO_PLAYER_UPLOAD_TIME, Util.getSharedPreferenceString(getActivity().getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_AUDIO_UPLOAD_TIME));
            Intent intent = new Intent(getActivity().getApplicationContext(), AudioPlayerActivity.class);
            startActivity(intent);

        } else if(
                view.getId() == R.id.fragment_today_audio1holder_constraintlayout){
            Util.setSharedPreferenceString(getActivity().getApplicationContext(), Util.SHARED_PREF_KEY_VIDEO_PLAYER_IMG_URL, Util.getSharedPreferenceString(getActivity().getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_VIDEO1_IMG_URL));
            Util.setSharedPreferenceString(getActivity().getApplicationContext(), Util.SHARED_PREF_KEY_VIDEO_PLAYER_VIDEO_URL, Util.getSharedPreferenceString(getActivity().getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_VIDEO1_URL));
            Util.setSharedPreferenceString(getActivity().getApplicationContext(), Util.SHARED_PREF_KEY_VIDEO_PLAYER_TITLE, Util.getSharedPreferenceString(getActivity().getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_VIDEO1_TITLE));
            Util.setSharedPreferenceString(getActivity().getApplicationContext(), Util.SHARED_PREF_KEY_VIDEO_PLAYER_UPLOAD_TIME, Util.getSharedPreferenceString(getActivity().getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_VIDEO1_UPLOAD_TIME));
            Util.setSharedPreferenceString(getActivity().getApplicationContext(), Util.SHARED_PREF_KEY_VIDEO_PLAYER_BODY, Util.getSharedPreferenceString(getActivity().getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_VIDEO1_BODY));
            Intent intent = new Intent(getActivity().getApplicationContext(), VideoPlayerActivity.class);
            startActivity(intent);
        } else if(
                view.getId() == R.id.fragment_today_audio2holder_constraintlayout){
            Util.setSharedPreferenceString(getActivity().getApplicationContext(), Util.SHARED_PREF_KEY_VIDEO_PLAYER_IMG_URL, Util.getSharedPreferenceString(getActivity().getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_VIDEO2_IMG_URL));
            Util.setSharedPreferenceString(getActivity().getApplicationContext(), Util.SHARED_PREF_KEY_VIDEO_PLAYER_VIDEO_URL, Util.getSharedPreferenceString(getActivity().getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_VIDEO2_URL));
            Util.setSharedPreferenceString(getActivity().getApplicationContext(), Util.SHARED_PREF_KEY_VIDEO_PLAYER_TITLE, Util.getSharedPreferenceString(getActivity().getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_VIDEO2_TITLE));
            Util.setSharedPreferenceString(getActivity().getApplicationContext(), Util.SHARED_PREF_KEY_VIDEO_PLAYER_UPLOAD_TIME, Util.getSharedPreferenceString(getActivity().getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_VIDEO2_UPLOAD_TIME));
            Util.setSharedPreferenceString(getActivity().getApplicationContext(), Util.SHARED_PREF_KEY_VIDEO_PLAYER_BODY, Util.getSharedPreferenceString(getActivity().getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_VIDEO2_BODY));
            Intent intent = new Intent(getActivity().getApplicationContext(), VideoPlayerActivity.class);
            startActivity(intent);
        }
    }
}
