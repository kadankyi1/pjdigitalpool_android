package org.christecclesia.pjdigitalpool.Inc;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.christecclesia.pjdigitalpool.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class Util {

    public static Boolean ALLOW_LOGGING = true;
    public static String LINK_PROTOCOL = "http://";
    //public static String LINK_DOMAIN = "pjdigitalpool.fishpott.com";
    public static String LINK_DOMAIN = "144.202.76.74";

    // API LINKS
    //public static String LINK_SIGNUP = LINK_PROTOCOL + LINK_DOMAIN + "/api/v1/member/regis";
    public static String LINK_SIGNUP = LINK_PROTOCOL + LINK_DOMAIN + "/api/v1/member/register/";
    public static String LINK_LOGIN = LINK_PROTOCOL + LINK_DOMAIN + "/api/v1/member/login";
    public static String LINK_SEND_RESET_CODE = LINK_PROTOCOL + LINK_DOMAIN + "/api/v1/member/forgot";
    public static String LINK_RESET_PASSWORD = LINK_PROTOCOL + LINK_DOMAIN + "/api/v1/member/reset";
    public static String LINK_AUDIO_LIST = LINK_PROTOCOL + LINK_DOMAIN + "/api/v1/admin/audios/list";
    public static String LINK_VIDEOS_LIST = LINK_PROTOCOL + LINK_DOMAIN + "/api/v1/admin/videos/list";
    public static String LINK_FAVORITES_LIST = LINK_PROTOCOL + LINK_DOMAIN + "/api/v1/admin/favorites/list";
    public static String LINK_ARTICLES_LIST = LINK_PROTOCOL + LINK_DOMAIN + "/api/v1/admin/articles/list";


    //SHARES PREFERENCES
    public static String SHARED_PREF_KEY_USER_TOKEN = "SHARED_PREF_KEY_USER_TOKEN";
    public static String SHARED_PREF_KEY_USER_ID = "SHARED_PREF_KEY_USER_ID";
    public static String SHARED_PREF_KEY_USER_FIRST_NAME = "SHARED_PREF_KEY_USER_FIRST_NAME";
    public static String SHARED_PREF_KEY_USER_LAST_NAME = "SHARED_PREF_KEY_USER_LAST_NAME";
    public static String SHARED_PREF_KEY_USER_COUNTRY = "SHARED_PREF_KEY_USER_COUNTRY";
    public static String SHARED_PREF_KEY_USER_PHONE = "SHARED_PREF_KEY_USER_PHONE";
    public static String SHARED_PREF_KEY_USER_EMAIL= "SHARED_PREF_KEY_USER_EMAIL";
    public static String SHARED_PREF_KEY_USER_FLAGGED= "SHARED_PREF_KEY_USER_FLAGGED";

    // AUDIO PLAYER
    public static String SHARED_PREF_KEY_TODAY_AUDIO_TRACK_ID=  "SHARED_PREF_KEY_TODAY_AUDIO_TRACK_ID";
    public static String SHARED_PREF_KEY_AUDIO_PLAYER_IMG_URL=  "SHARED_PREF_KEY_AUDIO_PLAYER_IMG_URL";
    public static String SHARED_PREF_KEY_AUDIO_PLAYER_AUDIO_URL=  "SHARED_PREF_KEY_AUDIO_PLAYER_AUDIO_URL";
    public static String SHARED_PREF_KEY_AUDIO_PLAYER_TITLE =  "SHARED_PREF_KEY_AUDIO_PLAYER_TITLE";
    public static String SHARED_PREF_KEY_AUDIO_PLAYER_UPLOAD_TIME =  "SHARED_PREF_KEY_AUDIO_PLAYER_UPLOAD_TIME";

    // VIDEO PLAYER
    public static String SHARED_PREF_KEY_VIDEO_PLAYER_IMG_URL=  "SHARED_PREF_KEY_VIDEO_PLAYER_IMG_URL";
    public static String SHARED_PREF_KEY_VIDEO_PLAYER_VIDEO_URL=  "SHARED_PREF_KEY_VIDEO_PLAYER_VIDEO_URL";
    public static String SHARED_PREF_KEY_VIDEO_PLAYER_TITLE =  "SHARED_PREF_KEY_VIDEO_PLAYER_TITLE";
    public static String SHARED_PREF_KEY_VIDEO_PLAYER_UPLOAD_TIME =  "SHARED_PREF_KEY_VIDEO_PLAYER_UPLOAD_TIME";
    public static String SHARED_PREF_KEY_VIDEO_PLAYER_BODY =  "SHARED_PREF_KEY_VIDEO_PLAYER_BODY";

    // IMAGE ARTICLE
    public static String SHARED_PREF_KEY_IMAGE_ARTICLE_IMG_URL=  "SHARED_PREF_KEY_IMAGE_ARTICLE_IMG_URL";
    public static String SHARED_PREF_KEY_IMAGE_ARTICLE_TAG_TEXT=  "SHARED_PREF_KEY_IMAGE_ARTICLE_TAG_TEXT";
    public static String SHARED_PREF_KEY_IMAGE_ARTICLE_UPLOAD_TIME=  "SHARED_PREF_KEY_IMAGE_ARTICLE_UPLOAD_TIME";
    public static String SHARED_PREF_KEY_IMAGE_ARTICLE_ARTICLE_TITLE =  "SHARED_PREF_KEY_IMAGE_ARTICLE_ARTICLE_TITLE";
    public static String SHARED_PREF_KEY_IMAGE_ARTICLE_ARTICLE_BODY =  "SHARED_PREF_KEY_IMAGE_ARTICLE_ARTICLE_BODY";

    // RESOURCES
    public static String SHARED_PREF_KEY_TODAY_INFO_BANNER_IMG_URL = "SHARED_PREF_KEY_TODAY_BANNER_IMG_URL";
    public static String SHARED_PREF_KEY_TODAY_AUDIO_TRACK_URL = "SHARED_PREF_KEY_TODAY_AUDIO_TRACK_URL";
    public static String SHARED_PREF_KEY_TODAY_AUDIO_IMG_URL = "SHARED_PREF_KEY_TODAY_AUDIO_IMG_URL";
    public static String SHARED_PREF_KEY_TODAY_AUDIO_TITLE = "SHARED_PREF_KEY_TODAY_AUDIO_TITLE";
    public static String SHARED_PREF_KEY_TODAY_AUDIO_BODY = "SHARED_PREF_KEY_TODAY_AUDIO_BODY";
    public static String SHARED_PREF_KEY_TODAY_AUDIO_UPLOAD_TIME = "SHARED_PREF_KEY_TODAY_AUDIO_UPLOAD_TIME";

    public static String SHARED_PREF_KEY_TODAY_VIDEO1_URL = "SHARED_PREF_KEY_TODAY_VIDEO1_URL";
    public static String SHARED_PREF_KEY_TODAY_VIDEO1_IMG_URL = "SHARED_PREF_KEY_TODAY_VIDEO1_IMG_URL";
    public static String SHARED_PREF_KEY_TODAY_VIDEO1_TITLE = "SHARED_PREF_KEY_TODAY_VIDEO1_TITLE";
    public static String SHARED_PREF_KEY_TODAY_VIDEO1_LENGTH = "SHARED_PREF_KEY_TODAY_VIDEO1_LENGTH";
    public static String SHARED_PREF_KEY_TODAY_VIDEO1_UPLOAD_TIME = "SHARED_PREF_KEY_TODAY_VIDEO1_UPLOAD_TIME";
    public static String SHARED_PREF_KEY_TODAY_VIDEO1_BODY = "SHARED_PREF_KEY_TODAY_VIDEO1_BODY";

    public static String SHARED_PREF_KEY_TODAY_VIDEO2_URL = "SHARED_PREF_KEY_TODAY_VIDEO2_URL";
    public static String SHARED_PREF_KEY_TODAY_VIDEO2_IMG_URL = "SHARED_PREF_KEY_TODAY_VIDEO2_IMG_URL";
    public static String SHARED_PREF_KEY_TODAY_VIDEO2_TITLE = "SHARED_PREF_KEY_TODAY_VIDEO2_TITLE";
    public static String SHARED_PREF_KEY_TODAY_VIDEO2_LENGTH = "SHARED_PREF_KEY_TODAY_VIDEO2_LENGTH";
    public static String SHARED_PREF_KEY_TODAY_VIDEO2_UPLOAD_TIME = "SHARED_PREF_KEY_TODAY_VIDEO2_UPLOAD_TIME";
    public static String SHARED_PREF_KEY_TODAY_VIDEO2_BODY = "SHARED_PREF_KEY_TODAY_VIDEO2_BODY";

    //MENU ITEMS
    public static int TODAY_FRAMENT = 0;
    public static int LIBRARY_FRAMENT = 1;
    public static int READ_FRAMENT = 2;
    public static int LIVE_FRAMENT = 3;
    public static int WITNESS_FRAMENT = 4;

    //MISCELLNEOUS
    public static String SHARED_PREF_KEY_FAVORITE_AUDIOS = "SHARED_PREF_KEY_FAVORITE_AUDIOS";


    public static void show_log_in_console(String title, String description){
        if(ALLOW_LOGGING){
            Log.e(title, description);
        }
    }

    // OPENING A FRAGMENT
    public static void open_fragment(FragmentManager fragmentManager, int fragmentContainerId, Fragment newFragment, String fragmentName, int includeAnimation){
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if(includeAnimation == 1){
            transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right, R.anim.slide_in_right, R.anim.slide_out_right);
        } else if (includeAnimation == 2){
            transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_right, R.anim.slide_out_left);
        } else if (includeAnimation == 3){
            transaction.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_down,R.anim.slide_in_down, R.anim.slide_out_up);
        }
        transaction.addToBackStack(fragmentName);
        transaction.add(fragmentContainerId, newFragment, fragmentName).commit();
    }
    // GET SHARED PREFERENCE STRING
    public static String getSharedPreferenceString(Context context, String key) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        context = null;
        return preferences.getString(key, "");

    }

    // EDIT SHARED PREFERENCE STRING
    public static void setSharedPreferenceString(Context context, String key, String value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
        context = null;
    }

    // GET SHARED PREFERENCE INT
    public static int getSharedPreferenceInt(Context context, String key) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        context = null;
        return preferences.getInt(key, 0);

    }

    // SET SHARED PREFERENCE INT
    public static void setSharedPreferenceInt(Context context, String key, int value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.apply();
        context = null;
    }


    // GET SHARED PREFERENCE INT
    public static int getSharedPreferenceFloat(Context context, String key) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        context = null;
        return preferences.getInt(key, 0);

    }

    // SET SHARED PREFERENCE INT
    public static void setSharedPreferenceFloat(Context context, String key, float value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putFloat(key, value);
        editor.apply();
        context = null;
    }


    // GET SHARED PREFERENCE BOOLEAN
    public static boolean getSharedPreferenceBoolean(Context context, String key) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        context = null;
        return preferences.getBoolean(key, false);

    }

    // SET SHARED PREFERENCE BOOLEAN
    public static void setSharedPreferenceBoolean(Context context, String key, boolean value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        context = null;
        editor.apply();
    }

    // GET SHARED PREFERENCE STRING
    public static ArrayList<String> getSharedPreferenceStringSet(Context context, String key) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Set<String> set = preferences.getStringSet(key, null);
        if(set == null){
            return null;
        }
        ArrayList<String> array_list=new ArrayList<String>(set);
        context = null;
        return array_list;
    }

    // EDIT SHARED PREFERENCE STRING
    public static void setSharedPreferenceStringSet(Context context, String key, ArrayList<String> array_list) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        Set<String> set = new HashSet<String>();
        set.addAll(array_list);
        editor.putStringSet(key, set);
        editor.apply();
        context = null;
    }


    // CLEAR ALL SHARED PREFERENCE
    public static void deleteAllDataInSharedPreference(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
        context = null;
    }

    public static void loadImageView(Context context, String url, ImageView imageView, final ProgressBar progressBar){

        if(context != null && imageView != null){
            Util.show_log_in_console("loadImageView", "url: " + url);
            Glide.with(context)
                    .load(url)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            if(progressBar != null){
                                progressBar.setVisibility(View.GONE);
                            }
                            Util.show_log_in_console("loadImageView", "onLoadFailed: " + e.toString());
                            return false;
                        }
                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            if(progressBar != null){
                                progressBar.setVisibility(View.GONE);
                            }
                            Util.show_log_in_console("loadImageView", "onResourceReady");
                            return false;
                        }
                    })
                    .into(imageView);
            Util.show_log_in_console("loadImageView", "COMPLETED");
        }
    }




    /*
    public static void openActivity(Activity thisActivity, Class NewActivity, int includeAnimation, int finishActivity, int addData, String dataIndex, String dataValue) {
        Intent intent = new Intent(thisActivity, NewActivity);
        if(addData == 1){
            intent.putExtra(dataIndex, dataValue);
        }

        if(finishActivity == 1){
            thisActivity.startActivity(intent);
            thisActivity.finish();
            if(includeAnimation == 1){
                thisActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            } else if(includeAnimation == 2){
                thisActivity.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_down);
            }

        } else if(finishActivity == 2){
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            thisActivity.startActivity(intent);
            thisActivity.finish();
            if(includeAnimation == 1){
                thisActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            } else if(includeAnimation == 2){
                thisActivity.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_down);
            }

        } else {
            thisActivity.startActivity(intent);
            if(includeAnimation == 1){
                thisActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            } else if(includeAnimation == 2){
                thisActivity.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_down);
            }

        }
        thisActivity = null;
        Config.freeMemory();
    }
    */

}
