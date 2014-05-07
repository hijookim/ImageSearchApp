package com.hijookim.android.imagesearchapp.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;


public class ImageSearchActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_search_activity);

        if (savedInstanceState == null) {
            FragmentManager fm = getSupportFragmentManager();
            Fragment fragment = fm.findFragmentById(R.id.container);

            if (fragment == null) {
                fragment = ImageSearchFragment.getInstance(fm, new Bundle());
                fm.beginTransaction().add(R.id.container, fragment).commit();
            }
        }
    }

}
