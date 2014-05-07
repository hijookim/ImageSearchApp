package com.hijookim.android.imagesearchapp.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.hijookim.android.imagesearchapp.app.utils.ImageQueryAdapter;
import com.hijookim.android.imagesearchapp.app.utils.ImageSearchTask;
import com.hijookim.android.imagesearchapp.app.utils.ImageSearchUtils;

import java.util.ArrayList;
import java.util.Map;


/**
 * A fragment representing a list of Items.
 * <p />
 * <p />
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class ImageQueryListFragment extends ListFragment implements OnImageQueryTaskExecuted {

    private static final String TAG = "ImageQueryListFragment";
    private static final String FRAGMENT_TAG = "fragment_image_query_list";

    private OnImageQueryTaskExecuted mOnImageQueryTaskExecuted = this;

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

    private ImageQueryListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mImageQueryList = getArguments().getStringArrayList(ImageSearchFragment.EXTRA_PAST_IMAGE_QUERY_LIST);
        }

        setListAdapter(new ImageQueryAdapter(getActivity(), R.layout.query_item, mImageQueryList));

    }

    private AdapterView.OnItemClickListener getImageQueryItemOnClickListener() {
        AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mImageQueryList != null && position >= 0 && position < mImageQueryList.size()) {
                    String queryKeyword = mImageQueryList.get(position);

                    ImageSearchUtils.writeQueryKeywordsToLocalStorage(getActivity(), queryKeyword);
                    String pageIndex = "0";

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
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

    }

    public void refreshPastImageQueryList(ArrayList<String> queries) {
        mImageQueryList = queries;
        //
    }

    @Override
    public void onImageQueryTaskComplete(Map<String, Object> imageResult) {
        Context context = getActivity();
        if (context != null) {
            Intent intent = new Intent(context, ImageGridViewActivity.class);
            Bundle bundle = new Bundle();

            ArrayList<String> urls = (ArrayList<String>) imageResult.get(ImageGridViewFragment.EXTRA_IMAGE_URL_LIST);
            ArrayList<Integer> pages = (ArrayList<Integer>) imageResult.get(ImageGridViewFragment.EXTRA_IMAGE_QUERY_PAGES);
            String keyword = (String) imageResult.get(ImageGridViewFragment.EXTRA_IMAGE_QUERY_KEYWORD);
            Double currentPage = (Double) imageResult.get(ImageGridViewFragment.EXTRA_IMAGE_QUERY_CURRENT_PAGE);

            bundle.putStringArrayList(ImageGridViewFragment.EXTRA_IMAGE_URL_LIST, urls);
            bundle.putIntegerArrayList(ImageGridViewFragment.EXTRA_IMAGE_QUERY_PAGES, pages);
            bundle.putString(ImageGridViewFragment.EXTRA_IMAGE_QUERY_KEYWORD, keyword);
            bundle.putInt(ImageGridViewFragment.EXTRA_IMAGE_QUERY_CURRENT_PAGE, currentPage.intValue());
            intent.putExtras(bundle);

            context.startActivity(intent);
        }
    }
}
