package com.hijookim.android.imagesearchapp.app.utils;

import android.content.Context;
import android.os.AsyncTask;

import com.hijookim.android.imagesearchapp.app.OnReadQueryWordTaskCompleted;

import java.util.List;

/**
 * This task used to read previous image query words asynchronously
 */
public class ReadQueryWordTask extends AsyncTask<Void, Void, List<String>> {

    private OnReadQueryWordTaskCompleted mOnReadQueryWordTaskCompleted;
    private Context mContext;

    public ReadQueryWordTask(Context context, OnReadQueryWordTaskCompleted onReadQueryWordTaskCompleted) {
        mContext = context;
        mOnReadQueryWordTaskCompleted = onReadQueryWordTaskCompleted;
    }

    @Override
    protected List<String> doInBackground(Void... params) {

        return ImageSearchUtils.readQueryKeywordsFromLocalStorage(mContext);
    }

    @Override
    protected void onPostExecute(List<String> queryWords) {
        super.onPostExecute(queryWords);
        mOnReadQueryWordTaskCompleted.onReadCompleted(queryWords);
    }
}