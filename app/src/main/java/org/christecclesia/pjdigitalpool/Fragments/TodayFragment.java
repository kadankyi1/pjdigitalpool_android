package org.christecclesia.pjdigitalpool.Fragments;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.christecclesia.pjdigitalpool.Inc.Util;
import org.christecclesia.pjdigitalpool.R;
import org.christecclesia.pjdigitalpool.Views.CircleImageView;
import org.christecclesia.pjdigitalpool.Views.RoundedCornerImageView;

public class TodayFragment extends Fragment {

    private RoundedCornerImageView m_todaybanner_roundedcornerimageview, m_todayaudio_roundedcornerimageview;
    private CircleImageView m_todayvideo1_image_circleimageview, m_todayvideo2_image_circleimageview;
    private ProgressBar m_todaybanner_progressbar;
    private TextView m_welcome_textview, m_todayaudio_title_textview, m_todayaudio_body_textview, m_todayvideo1_title_textview, m_todayvideo1_length_textview, m_todayvideo2_title_textview, m_todayvideo2_length_textview;
    private ConstraintLayout m_todayaudio_holder_constraintlayout, m_todayvideo1_holder_constraintlayout, m_todayvideo2_holder_constraintlayout;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

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
        m_todaybanner_roundedcornerimageview = view.findViewById(R.id.fragment_today_newsinfobanner_roundedcornerimageview);
        m_todaybanner_progressbar = view.findViewById(R.id.fragment_today_newsinfobannerloader_progressbar);
        m_todayaudio_holder_constraintlayout = view.findViewById(R.id.fragment_today_heraldofgloryholder_constraintlayout);
        m_todayaudio_roundedcornerimageview = view.findViewById(R.id.fragment_today_heraldofgloryimage_roundedcornerimageview);
        m_todayaudio_title_textview = view.findViewById(R.id.fragment_today_heraldofglorytitle_textview);
        m_todayaudio_body_textview = view.findViewById(R.id.fragment_today_heraldofglorybody_textview);
        m_todayvideo1_holder_constraintlayout = view.findViewById(R.id.fragment_today_audio1holder_constraintlayout);
        m_todayvideo1_image_circleimageview = view.findViewById(R.id.fragment_today_audio1image_roundedcornerimageview);
        m_todayvideo1_title_textview = view.findViewById(R.id.fragment_today_audio1title_textview);
        m_todayvideo1_length_textview = view.findViewById(R.id.fragment_today_audio1body_textview);
        m_todayvideo2_holder_constraintlayout = view.findViewById(R.id.fragment_today_audio2holder_constraintlayout);
        m_todayvideo2_image_circleimageview = view.findViewById(R.id.fragment_today_audio2image_roundedcornerimageview);
        m_todayvideo2_title_textview = view.findViewById(R.id.fragment_today_audio2title_textview);
        m_todayvideo2_length_textview = view.findViewById(R.id.fragment_today_audio2body_textview);

        m_welcome_textview.append(" " + Util.getSharedPreferenceString(getActivity().getApplicationContext(), Util.SHARED_PREF_KEY_USER_FIRST_NAME));

        if(!getActivity().isFinishing()){
            Util.loadImageView(getActivity().getApplicationContext(), Util.getSharedPreferenceString(getActivity().getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_INFO_BANNER_IMG_URL), m_todaybanner_roundedcornerimageview, m_todaybanner_progressbar);
        }

        if(
                !Util.getSharedPreferenceString(getActivity().getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_AUDIO_IMG_URL).isEmpty()
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
                !Util.getSharedPreferenceString(getActivity().getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_VIDEO1_URL).isEmpty()
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
                !Util.getSharedPreferenceString(getActivity().getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_VIDEO2_URL).isEmpty()
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



        return view;
    }
}
