package com.hijookim.android.imagesearchapp.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;


public class ImageGridViewActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_grid_view_activity);

        if (savedInstanceState == null) {
            FragmentManager fm = getSupportFragmentManager();
            Fragment fragment = fm.findFragmentById(R.id.grid_view_container);

            if (fragment == null) {
                Bundle bundle = getIntent() != null ? getIntent().getExtras() : new Bundle();
                fragment = ImageGridViewFragment.getInstance(fm, bundle);
                fm.beginTransaction().add(R.id.grid_view_container, fragment).commit();
            }
        }
    }

}
