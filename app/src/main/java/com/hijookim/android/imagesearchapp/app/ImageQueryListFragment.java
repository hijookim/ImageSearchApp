package com.hijookim.android.imagesearchapp.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.hijookim.android.imagesearchapp.app.utils.ImageQueryAdapter;
import com.hijookim.android.imagesearchapp.app.utils.ImageSearchTask;
import com.hijookim.android.imagesearchapp.app.utils.ReadQueryWordTask;
import com.hijookim.android.imagesearchapp.app.utils.SaveQueryWordTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * A fragment representing a list of previously searched image query words
 */
public class ImageQueryListFragment extends ListFragment implements OnImageQueryTaskExecuted, OnReadQueryWordTaskCompleted {

    private static final String TAG = "ImageQueryListFragment";
    private static final String FRAGMENT_TAG = "fragment_image_query_list";

    private OnImageQueryTaskExecuted mOnImageQueryTaskExecuted = this;
    private OnReadQueryWordTaskCompleted mOnReadQueryWordTaskCompleted = this;

    private ImageQueryAdapter mQueryListAdapter;
    private ListView mListView;
    private ArrayList<String> mImageQueryList;


    public static ImageQueryListFragment newInstance(Bundle args) {
        ImageQueryListFragment fragment = new ImageQueryListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static ImageQueryListFragment getInstance(FragmentManager fragmentManager, Bundle args) {
        ImageQueryListFragment instance = null;
        Fragment fragment = fragmentManager.findFragmentByTag(FRAGMENT_TAG);
        if (fragment == null) {
            instance = ImageQueryListFragment.newInstance(args);
        } else {
            instance = (ImageQueryListFragment) fragment;
        }
        return instance;
    }

    public static String getFragmentTag() {
        return FRAGMENT_TAG;
    }

    public ImageQueryListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //mImageQueryList = ImageSearchUtils.readQueryKeywordsFromLocalStorage(getActivity());
        mImageQueryList = new ArrayList<String>();
        mQueryListAdapter = new ImageQueryAdapter(getActivity(), R.layout.query_item, mImageQueryList);
        setListAdapter(mQueryListAdapter);

    }

    private AdapterView.OnItemClickListener getImageQueryItemOnClickListener() {
        AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mQueryListAdapter != null && mQueryListAdapter.getCount() > position && position >= 0) {
                    String queryKeyword = mQueryListAdapter.getItem(position);
                    new SaveQueryWordTask(getActivity(), queryKeyword).execute();
                    //ImageSearchUtils.writeQueryKeywordsToLocalStorage(getActivity(), queryKeyword);

                    new ImageSearchTask(mOnImageQueryTaskExecuted, queryKeyword, null).execute();
                }
            }
        };
        return listener;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mListView == null) {
            mListView = getListView();
            mListView.setOnItemClickListener(getImageQueryItemOnClickListener());
        }
        new ReadQueryWordTask(getActivity(), mOnReadQueryWordTaskCompleted).execute();
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

    }

    public void refreshPastImageQueryList(List<String> queries) {
        if (queries != null && mQueryListAdapter != null && mListView != null) {
            mQueryListAdapter.setImageQueryList(queries);
            mQueryListAdapter.notifyDataSetChanged();
            mListView.invalidateViews();
            mListView.smoothScrollToPosition(0);
        }
    }

    public void onReadCompleted(List<String> queryWords) {
        refreshPastImageQueryList(queryWords);
    }

    @Override
    public void onImageQueryTaskComplete(Map<String, Object> imageResult) {
        Context context = getActivity();
        if (context != null) {
            Intent intent = new Intent(context, ImageGridViewActivity.class);
            Bundle bundle = new Bundle();

            try {
                ArrayList<String> urls = (ArrayList<String>) imageResult.get(ImageGridViewFragment.EXTRA_IMAGE_URL_LIST);
                ArrayList<Integer> pages = (ArrayList<Integer>) imageResult.get(ImageGridViewFragment.EXTRA_IMAGE_QUERY_PAGES);
                String keyword = (String) imageResult.get(ImageGridViewFragment.EXTRA_IMAGE_QUERY_KEYWORD);
                Double currentPage = (Double) imageResult.get(ImageGridViewFragment.EXTRA_IMAGE_QUERY_CURRENT_PAGE);

                bundle.putStringArrayList(ImageGridViewFragment.EXTRA_IMAGE_URL_LIST, urls);
                bundle.putIntegerArrayList(ImageGridViewFragment.EXTRA_IMAGE_QUERY_PAGES, pages);
                bundle.putString(ImageGridViewFragment.EXTRA_IMAGE_QUERY_KEYWORD, keyword);
                bundle.putInt(ImageGridViewFragment.EXTRA_IMAGE_QUERY_CURRENT_PAGE, currentPage.intValue());

            } catch (ClassCastException e) {
                Log.v(TAG, "wrong objects mapped to the keys", e);
            }
            intent.putExtras(bundle);

            context.startActivity(intent);
        }
    }
}
