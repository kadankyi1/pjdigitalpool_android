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
    ViewPager mFragmentsHolderViewPager;
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
