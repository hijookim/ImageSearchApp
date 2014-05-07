package com.hijookim.android.imagesearchapp.app.utils;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.hijookim.android.imagesearchapp.app.R;

import java.util.ArrayList;

public class ImageQueryAdapter extends ArrayAdapter<String> {

    public static final String TAG = "ImageQueryAdapter";

    private Context mContext;
    private int mLayoutResourceId;
    private ArrayList<String> mImageQueries;

    public ImageQueryAdapter(Context context, int layoutResourceId, ArrayList<String> imageQueries) {
        super(context, layoutResourceId, imageQueries);

        mContext = context;
        mLayoutResourceId = layoutResourceId;
        mImageQueries = imageQueries;
    }

    @Override
    public int getCount() {
        return mImageQueries.size();
    }

    @Override
    public String getItem(int index) {
        String imageQuery = null;
        if (index >= 0 && index < mImageQueries.size()) {
            imageQuery = mImageQueries.get(index);
        }
        return imageQuery;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View queryItemView = convertView;
        QueryItemHolder holder = null;

        if (queryItemView == null) {
            LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();
            queryItemView = inflater.inflate(mLayoutResourceId, parent, false);

            if (queryItemView == null) {
                return null;
            }

            holder = new QueryItemHolder();
            holder.mImageQuery = (TextView) queryItemView.findViewById(R.id.image_query_keyword);
            queryItemView.setTag(holder);
        } else {
            holder = (QueryItemHolder) queryItemView.getTag();
        }

        if (position >= 0 && position < mImageQueries.size()) {
            String query = mImageQueries.get(position);
            holder.mImageQuery.setText(query);
        }

        return queryItemView;
    }

    static class QueryItemHolder {
        TextView mImageQuery;
    }

    @Override
    public Filter getFilter() {
        final Filter filter = new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                if (constraint == null) {
                    return null;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mImageQueries;
                filterResults.count = mImageQueries.size();

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
        return filter;
    }

//    @Override
//    public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
//        Intent i = new Intent(this, ProductActivity.class);
//        i.putExtra("item_id", manager.getItemIdAtIndex(pos));
//        startActivity(i);
//    }
}
