package org.christecclesia.pjdigitalpool.Inc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;


import org.christecclesia.pjdigitalpool.R;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class Util {

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
