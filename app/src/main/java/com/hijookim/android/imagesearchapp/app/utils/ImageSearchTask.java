package com.hijookim.android.imagesearchapp.app.utils;

import android.os.AsyncTask;

import com.hijookim.android.imagesearchapp.app.ImageGridViewFragment;
import com.hijookim.android.imagesearchapp.app.OnImageQueryTaskExecuted;
import com.hijookim.android.imagesearchapp.app.model.ImageResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * This task used to query images asychronously - also used to load more pages of images
 */
public class ImageSearchTask extends AsyncTask<Void, Map<String, Object>, Map<String, Object>> {

    private OnImageQueryTaskExecuted mInstance;
    private ImageResult mImageSearchResponse;
    private String mKeyword;
    private String mPageStartIndex;

    public ImageSearchTask(OnImageQueryTaskExecuted instance, String keyword, String pageStartIndex) {
        mInstance = instance;
        mKeyword = keyword;
        mPageStartIndex = pageStartIndex;
    }

    @Override
    protected Map<String, Object> doInBackground(Void... params) {

        mImageSearchResponse = ImageSearchUtils.queryImage(mKeyword, mPageStartIndex);

        Map<String, Object> imageUrlMap = new HashMap<String, Object>();
        if (mImageSearchResponse != null) {
            ArrayList<String> imageUrls = ImageSearchUtils.getImageUrls(mImageSearchResponse, null);
            ArrayList<Integer> imagePages = ImageSearchUtils.getImageSearchPages(mImageSearchResponse);
            Double currentPage = ImageSearchUtils.getCurrentPageIndex(mImageSearchResponse);

            imageUrlMap.put(ImageGridViewFragment.EXTRA_IMAGE_URL_LIST, imageUrls);
            imageUrlMap.put(ImageGridViewFragment.EXTRA_IMAGE_QUERY_PAGES, imagePages);
            imageUrlMap.put(ImageGridViewFragment.EXTRA_IMAGE_QUERY_CURRENT_PAGE, currentPage);
            imageUrlMap.put(ImageGridViewFragment.EXTRA_IMAGE_QUERY_KEYWORD, mKeyword);
        }

        return imageUrlMap;
    }

    @Override
    protected void onPostExecute(Map<String, Object> imageResult) {
        super.onPostExecute(imageResult);

        if (mInstance != null) {
            mInstance.onImageQueryTaskComplete(imageResult);
        }

    }
}