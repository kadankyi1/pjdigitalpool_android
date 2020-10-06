package org.christecclesia.pjdigitalpool.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.christecclesia.pjdigitalpool.Fragments.TodayFragment;
import org.christecclesia.pjdigitalpool.Inc.Util;
import org.christecclesia.pjdigitalpool.R;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        bottomNavigation.setSelectedItemId(R.id.bottom_navigation);
        Util.open_fragment(getSupportFragmentManager(),R.id.container, TodayFragment.newInstance("", ""), "TodayFragment", 0);

    }


    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.navigation_today:
                            Util.open_fragment(getSupportFragmentManager(),R.id.container, TodayFragment.newInstance("", ""), "TodayFragment", 0);
                            return true;
                    }
                    return false;
                }
            };


    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if(count == 1){
            finish();
        } else {
            super.onBackPressed();
        }

    }

}
