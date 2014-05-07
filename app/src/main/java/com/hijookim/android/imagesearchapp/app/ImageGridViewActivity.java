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

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.grid_view_container);

        if (fragment == null) {
            Bundle bundle;
            if (savedInstanceState != null) {
                bundle = new Bundle(savedInstanceState);
                bundle.putStringArrayList(ImageGridViewFragment.EXTRA_IMAGE_URL_LIST, savedInstanceState.getStringArrayList(ImageGridViewFragment.EXTRA_IMAGE_URL_LIST));
            } else {
                bundle = new Bundle();
            }
            bundle = getIntent() != null ? getIntent().getExtras() : null;
            fragment = ImageGridViewFragment.getInstance(fm, bundle);
            fm.beginTransaction().add(R.id.grid_view_container, fragment).commit();
        }
    }

}