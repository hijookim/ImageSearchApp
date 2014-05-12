package com.hijookim.android.imagesearchapp.app.utils;

import android.content.Context;
import android.os.AsyncTask;

/**
* This task used to save the most recently used image query word asychronously
*/
public class SaveQueryWordTask extends AsyncTask<Void, Void, Void> {

    private Context mContext;
    private String mKeyword;

    public SaveQueryWordTask(Context context, String keyword) {
        mContext = context;
        mKeyword = keyword;
    }

    @Override
    protected Void doInBackground(Void... params) {

        ImageSearchUtils.writeQueryKeywordsToLocalStorage(mContext, mKeyword);

        return null;
    }

    @Override
    protected void onPostExecute(Void imageResult) {
        super.onPostExecute(imageResult);
    }
}