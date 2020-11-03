package org.christecclesia.pjdigitalpool.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.christecclesia.pjdigitalpool.Fragments.LibraryFragment;
import org.christecclesia.pjdigitalpool.Fragments.LiveFragment;
import org.christecclesia.pjdigitalpool.Fragments.ReadFragment;
import org.christecclesia.pjdigitalpool.Fragments.TodayFragment;
import org.christecclesia.pjdigitalpool.Fragments.WitnessFragment;
import org.christecclesia.pjdigitalpool.Inc.Util;
import org.christecclesia.pjdigitalpool.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigation;
    public static ViewPager mFragmentsHolderViewPager;
    private MyPageAdapter pageAdapter;
    private int currentMenuItemSelected = Util.TODAY_FRAMENT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFragmentsHolderViewPager = findViewById(R.id.activity_main_viewpager);
        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        bottomNavigation.setSelectedItemId(R.id.navigation_today);

        //SETTING A PAGE/FRAGMENT CHANGE LISTENER FOR THE VIEWPAGER
        mFragmentsHolderViewPager.addOnPageChangeListener(view_paper_listener);

        List<Fragment> fragmentsList = getFragments();

        pageAdapter = new MyPageAdapter(getSupportFragmentManager(), fragmentsList);
        mFragmentsHolderViewPager.setAdapter(pageAdapter);
        mFragmentsHolderViewPager.setCurrentItem(0);

        /******************************************************************************************************
         *
         * BACKGROUND SYNC ITEM FROM SERVER
         *
         ******************************************************************************************************/
        Util.setSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_INFO_BANNER_IMG_URL, "https://images.unsplash.com/photo-1499081589563-7c400fcd94e8?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1000&q=80");

        Util.setSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_AUDIO_TRACK_ID, "100");
        Util.setSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_AUDIO_TRACK_URL, "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3");
        Util.setSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_AUDIO_IMG_URL, "https://images.unsplash.com/photo-1492176273113-2d51f47b23b0?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1000&q=80");
        Util.setSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_AUDIO_TITLE, "FAITH & HOPE");
        Util.setSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_AUDIO_BODY, "The best things in life are free. That is what some say. Listen to this audio to learn more");
        Util.setSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_AUDIO_UPLOAD_TIME, "2 days ago");

        Util.setSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_VIDEO1_URL, "http://clips.vorwaerts-gmbh.de/VfE_html5.mp4");
        Util.setSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_VIDEO1_IMG_URL, "https://images.unsplash.com/photo-1518601794912-1af91724e528?ixlib=rb-1.2.1&auto=format&fit=crop&w=1000&q=80");
        Util.setSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_VIDEO1_TITLE, "ANCIENT WORD");
        Util.setSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_VIDEO1_LENGTH, "0:30");
        Util.setSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_VIDEO1_UPLOAD_TIME, "4 days ago");
        Util.setSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_VIDEO1_BODY, "The best things in life are free. That is what some say. Listen to this audio to learn more");

        Util.setSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_VIDEO2_URL, "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4");
        Util.setSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_VIDEO2_IMG_URL, "https://images.unsplash.com/photo-1499946981954-e7f4b234d7fa?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1000&q=80");
        Util.setSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_VIDEO2_TITLE, "REALITY & FAITH");
        Util.setSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_VIDEO2_LENGTH, "0:22");
        Util.setSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_VIDEO2_UPLOAD_TIME, "6 hr ago");
        Util.setSharedPreferenceString(getApplicationContext(), Util.SHARED_PREF_KEY_TODAY_VIDEO2_BODY, "The best things in life are free. That is what some say. Listen to this audio to learn more");

        /******************************************************************************************************
         *
         * --- END BACKGROUND SYNC ITEM FROM SERVER
         *
         ******************************************************************************************************/

        //Util.open_fragment(getSupportFragmentManager(),R.id.container, TodayFragment.newInstance("", ""), "TodayFragment", 0);

    }

    private List<Fragment> getFragments(){
        List<Fragment> fList = new ArrayList<Fragment>();

        fList.add(TodayFragment.newInstance("", ""));
        fList.add(LibraryFragment.newInstance("", ""));
        fList.add(ReadFragment.newInstance("", ""));
        fList.add(LiveFragment.newInstance("", ""));
        fList.add(WitnessFragment.newInstance("", ""));
        return fList;
    }

    // THE FRAGMENT ADAPTER
    private class MyPageAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragmentList;

        public MyPageAdapter(FragmentManager fragmentManager, List<Fragment> fragmentsList ) {
            super(fragmentManager);
            this.fragmentList = fragmentsList;
        }

        @Override
        public Fragment getItem(int position) {
            return this.fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return this.fragmentList.size();
        }
    }


    ViewPager.OnPageChangeListener view_paper_listener = new ViewPager.OnPageChangeListener(){

        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {
            currentMenuItemSelected = i;
            if(i == Util.TODAY_FRAMENT){
                bottomNavigation.setSelectedItemId(R.id.navigation_today);
            } else if(i == Util.LIBRARY_FRAMENT){
                bottomNavigation.setSelectedItemId(R.id.navigation_library);
            } else if(i == Util.READ_FRAMENT){
                bottomNavigation.setSelectedItemId(R.id.navigation_read);
            } else if(i == Util.LIVE_FRAMENT){
                bottomNavigation.setSelectedItemId(R.id.navigation_live);
            } else if(i == Util.WITNESS_FRAMENT){
                bottomNavigation.setSelectedItemId(R.id.navigation_witness);
            }


        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.navigation_today:
                            currentMenuItemSelected = Util.TODAY_FRAMENT;
                            mFragmentsHolderViewPager.setCurrentItem(0);
                            return true;
                        case R.id.navigation_library:
                            currentMenuItemSelected = Util.LIBRARY_FRAMENT;
                            mFragmentsHolderViewPager.setCurrentItem(1);
                            return true;
                        case R.id.navigation_read:
                            currentMenuItemSelected = Util.READ_FRAMENT;
                            mFragmentsHolderViewPager.setCurrentItem(2);
                            return true;
                        case R.id.navigation_live:
                            currentMenuItemSelected = Util.LIVE_FRAMENT;
                            mFragmentsHolderViewPager.setCurrentItem(3);
                            return true;
                        case R.id.navigation_witness:
                            currentMenuItemSelected = Util.WITNESS_FRAMENT;
                            mFragmentsHolderViewPager.setCurrentItem(4);
                            return true;
                    }
                    return false;
                }
            };

    @Override
    public void onBackPressed() {

        //GETTING THE FRAGMENT MANAGER OF ALL FRAGMENTS OPEN ON THE MAIN ACTIVITY
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if(count == 0){
            if(mFragmentsHolderViewPager.getCurrentItem() == 0){
                super.onBackPressed();
            } else {
                mFragmentsHolderViewPager.setCurrentItem(0);
            }
        } else {
            super.onBackPressed();
        }

    }


}
