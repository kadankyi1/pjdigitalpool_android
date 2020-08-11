package org.christecclesia.pjdigitalpool.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.christecclesia.pjdigitalpool.R;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

public class StartActivitySliderAdapter  extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public StartActivitySliderAdapter (Context context){
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
        if(position == 0){
            view = layoutInflater.inflate(R.layout.slider_item_1, container, false);
        } else {
            view = layoutInflater.inflate(R.layout.slider_item_2, container, false);
        }

        container.addView(view);
        return view;
    }



    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ConstraintLayout) object);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return super.getItemPosition(object);
    }
}
