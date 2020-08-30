package org.christecclesia.pjdigitalpool.Inc;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;


import org.christecclesia.pjdigitalpool.R;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class Util {

    public static Boolean ALLOW_LOGGING = true;
    public static String LINK_PROTOCOL = "https://";
    public static String LINK_DOMAIN = "www.fishpott.com";

    // API LINKS
    public static String LINK_SIGNUP = LINK_PROTOCOL + LINK_DOMAIN + "/pjdigitalpool/v1/signup";
    public static String LINK_LOGIN = LINK_PROTOCOL + LINK_DOMAIN + "/pjdigitalpool/v1/login";
    public static String LINK_SEND_RESET_CODE = LINK_PROTOCOL + LINK_DOMAIN + "/pjdigitalpool/v1/send-code";
    public static String LINK_RESET_PASSWORD = LINK_PROTOCOL + LINK_DOMAIN + "/pjdigitalpool/v1/reset-password";


    //SHARES PREFERENCES
    public static String SHARED_PREF_KEY_USER_TOKEN = "SHARED_PREF_KEY_USER_TOKEN";

    public static void show_log_in_console(String title, String description){
        if(ALLOW_LOGGING){
            Log.e(title, description);
        }
    }


    // OPENING A FRAGMENT
    public static void openFragment(FragmentManager fragmentManager, int fragmentContainerId, Fragment newFragment, String fragmentName, int includeAnimation){
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

    // CLEAR ALL SHARED PREFERENCE
    public static void deleteAllDataInSharedPreference(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
        context = null;
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
