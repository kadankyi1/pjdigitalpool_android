package org.christecclesia.pjdigitalpool.Views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import org.christecclesia.pjdigitalpool.R;

import androidx.appcompat.widget.AppCompatImageView;


/**
 * Created by zatana on 8/22/19.
 */

/*
public class RoundedCornerImageView extends AppCompatImageView {
    public RoundedCornerImageView(Context context) {
        super(context);
        init();
    }

    public RoundedCornerImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RoundedCornerImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    public void setClipToOutline(boolean clipToOutline) {
        super.setClipToOutline(clipToOutline);
    }


    private void init() {
        setClipToOutline(true);
        setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_four_corners, null));
    }

    @Override
    public void setBackgroundDrawable(Drawable background) {
        super.setBackgroundDrawable(background);
    }
}

 */


public class RoundedCornerImageView extends androidx.appcompat.widget.AppCompatImageView {

    private float radius = 18.0f;
    private Path path;
    private RectF rect;

    public RoundedCornerImageView(Context context) {
        super(context);
        init();
    }

    public RoundedCornerImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RoundedCornerImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        path = new Path();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        rect = new RectF(0, 0, this.getWidth(), this.getHeight());
        path.addRoundRect(rect, radius, radius, Path.Direction.CW);
        canvas.clipPath(path);
        super.onDraw(canvas);
    }
}