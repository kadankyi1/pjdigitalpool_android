package org.christecclesia.pjdigitalpool.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.christecclesia.pjdigitalpool.Inc.Util;
import org.christecclesia.pjdigitalpool.R;

public class ImageArticleActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView m_article_covert_art_imageview, m_back_imageview;
    private ConstraintLayout m_tag_holder_constraintlayout;
    private TextView m_tag_textview, m_article_date, m_article_title, m_article_body;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_article);

        m_article_covert_art_imageview =  findViewById(R.id.activity_imagearticle_image_imageview);
        m_back_imageview =  findViewById(R.id.activity_imagearticle_backimage_imageview);
        m_tag_holder_constraintlayout =  findViewById(R.id.tag_holder);
        m_tag_textview =  findViewById(R.id.fragment_today_heraldofglorylabel_textview);
        m_article_date =  findViewById(R.id.activity_imagearticle_date_textview);
        m_article_title =  findViewById(R.id.activity_imagearticle_title_textview);
        m_article_body =  findViewById(R.id.activity_imagearticle_body_textview);

        if(Util.getSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_IMAGE_ARTICLE_TAG_TEXT).trim().equalsIgnoreCase("HERALD OF GLORY")){

            m_tag_holder_constraintlayout.setBackground(getDrawable(R.drawable.rounded_four_corners_gold));
        } else if(Util.getSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_IMAGE_ARTICLE_TAG_TEXT).trim().equalsIgnoreCase("SPECIAL ARTICLE")){
            m_tag_holder_constraintlayout.setBackground(getDrawable(R.drawable.rounded_corners_specialarticle));
        } else if(Util.getSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_IMAGE_ARTICLE_TAG_TEXT).trim().equalsIgnoreCase("GLORY NEWS")){
            m_tag_holder_constraintlayout.setBackground(getDrawable(R.drawable.rounded_corners_glorynews));
        } else if(Util.getSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_IMAGE_ARTICLE_TAG_TEXT).trim().equalsIgnoreCase("BIBLE READING PLAN")){
            m_tag_holder_constraintlayout.setBackground(getDrawable(R.drawable.rounded_corners_bible_reading));
        }
        m_article_date.setText(Util.getSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_IMAGE_ARTICLE_UPLOAD_TIME).trim());
        m_article_title.setText(Util.getSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_IMAGE_ARTICLE_ARTICLE_TITLE).trim());
        m_article_body.setText(Util.getSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_IMAGE_ARTICLE_ARTICLE_BODY).trim());

        if(
                !ImageArticleActivity.this.isFinishing()
                        && !Util.getSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_IMAGE_ARTICLE_IMG_URL).trim().equalsIgnoreCase("")
        ){

            Util.loadImageView(getApplicationContext(), Util.getSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_IMAGE_ARTICLE_IMG_URL).trim(), m_article_covert_art_imageview, null);

        }



        m_back_imageview.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(view.getId() == m_back_imageview.getId()){
            onBackPressed();
        }
    }
}
