package com.hijookim.android.imagesearchapp.app;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.hijookim.android.imagesearchapp.app.image.SquareImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static android.widget.ImageView.ScaleType.CENTER_INSIDE;

final class ImageGridViewAdapter extends BaseAdapter {
    private final Context context;
    private List<String> mImageUrls;

    public ImageGridViewAdapter(Context context, List<String> imageUrls) {
        this.context = context;
        this.mImageUrls = imageUrls;
    }

    @Override public View getView(int position, View convertView, ViewGroup parent) {
        SquareImageView view = (SquareImageView) convertView;
        if (view == null) {
            view = new SquareImageView(context);
            view.setScaleType(CENTER_INSIDE);
        }

        // Get the image URL for the current position.
        String url = getItem(position);

        // Trigger the download of the URL asynchronously into the image view.
        Picasso.with(context) //
                .load(url) //
                .fit() //
                .into(view);

        return view;
    }

    public void addMoreImageUrls(ArrayList<String> additionalImageUrls) {
        if (mImageUrls != null && additionalImageUrls != null) {
            mImageUrls.addAll(additionalImageUrls);
        }
        notifyDataSetChanged();
    }

    @Override public int getCount() {
        return mImageUrls.size();
    }

    @Override public String getItem(int position) {
        return mImageUrls.get(position);
    }

    @Override public long getItemId(int position) {
        return position;
    }
}