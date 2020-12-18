package org.christecclesia.pjdigitalpool.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import org.christecclesia.pjdigitalpool.Inc.Util;
import org.christecclesia.pjdigitalpool.R;
import org.christecclesia.pjdigitalpool.Views.RoundedCornerImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;


/**
 * Created by zatana on 10/30/18.
 */

public class AdBannerSliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;
    private RoundedCornerImageView m_adbanner_roundedcornerimageview;
    private ProgressBar m_adbanner_progressbar;

    public AdBannerSliderAdapter (Context context){
        this.context = context;
    }


    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view;
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.adbanner_layout, container, false);

        m_adbanner_roundedcornerimageview = view.findViewById(R.id.fragment_today_newsinfobanner_roundedcornerimageview);
        m_adbanner_progressbar = view.findViewById(R.id.fragment_today_newsinfobannerloader_progressbar);

        if(position == 0){
            if(context != null && !Util.getSharedPreferenceString(context, Util.SHARED_PREF_KEY_TODAY_INFO_BANNER_IMG_URL).equalsIgnoreCase("")){
                Util.loadImageView(context, Util.getSharedPreferenceString(context, Util.SHARED_PREF_KEY_TODAY_INFO_BANNER_IMG_URL), m_adbanner_roundedcornerimageview, m_adbanner_progressbar);
            }
        } else {
            if(context != null && !Util.getSharedPreferenceString(context, Util.SHARED_PREF_KEY_TODAY_INFO_BANNER_IMG2_URL).equalsIgnoreCase("")){
                Util.loadImageView(context, Util.getSharedPreferenceString(context, Util.SHARED_PREF_KEY_TODAY_INFO_BANNER_IMG2_URL), m_adbanner_roundedcornerimageview, m_adbanner_progressbar);
            }
        }
        container.addView(view);
        return view;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return super.getItemPosition(object);
    }
}
