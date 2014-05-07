package com.hijookim.android.imagesearchapp.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.hijookim.android.imagesearchapp.app.utils.EndlessScrollListener;
import com.hijookim.android.imagesearchapp.app.utils.ImageSearchTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Fragment used to display the images in a grid format by setting the GridViewAdapter
 *
 */
public class ImageGridViewFragment extends Fragment implements OnImageQueryTaskExecuted {

    public static final String FRAGMENT_TAG = "fragment_image_grid_view";
    public static final String EXTRA_IMAGE_URL_LIST = "extra_image_url_list";
    public static final String EXTRA_IMAGE_QUERY_PAGES = "extra_image_query_pages";
    public static final String EXTRA_IMAGE_QUERY_CURRENT_PAGE = "extra_image_query_current_page";
    public static final String EXTRA_IMAGE_QUERY_KEYWORD = "extra_image_query_keyword";

    private OnImageQueryTaskExecuted mOnImageQueryTaskExecuted = this;
    private ImageGridViewAdapter mImageGridViewAdapter;

    private GridView mImageGridView;

    private List<String> mImageUrls;
    private List<Integer> mImagePages;
    private Integer mCurrentPage;
    private String mKeyword;


    public static ImageGridViewFragment newInstance(Bundle args) {
        ImageGridViewFragment fragment = new ImageGridViewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static ImageGridViewFragment getInstance(FragmentManager fragmentManager, Bundle args) {
        ImageGridViewFragment instance = null;
        Fragment fragment = fragmentManager.findFragmentByTag(FRAGMENT_TAG);
        if (fragment == null) {
            instance = ImageGridViewFragment.newInstance(args);
        } else {
            instance = (ImageGridViewFragment) fragment;
        }
        return instance;
    }

    public static String getFragmentTag() {
        return FRAGMENT_TAG;
    }

    public ImageGridViewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {   // do we even need this?
            Bundle bundle = getArguments();
            mImageUrls = bundle.getStringArrayList(EXTRA_IMAGE_URL_LIST);
            mImagePages = bundle.getIntegerArrayList(EXTRA_IMAGE_QUERY_PAGES);
            mCurrentPage = bundle.getInt(EXTRA_IMAGE_QUERY_CURRENT_PAGE);
            mKeyword = bundle.getString(EXTRA_IMAGE_QUERY_KEYWORD);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.image_grid_view_fragment, container, false);

        if (rootView == null) {
            return null;
        }

        mImageGridView = (GridView) rootView.findViewById(R.id.grid_view);
        mImageGridViewAdapter = new ImageGridViewAdapter(getActivity(), mImageUrls);
        mImageGridView.setAdapter(mImageGridViewAdapter);
        mImageGridView.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                customLoadMoreDataFromApi();
            }
        });

        return rootView;
    }

    public void customLoadMoreDataFromApi() {
        if (mCurrentPage + 1 < mImagePages.size()) {
            Integer nextPage = mImagePages.get(mCurrentPage + 1);
            new ImageSearchTask(mOnImageQueryTaskExecuted, mKeyword, nextPage.toString()).execute();
        }
    }

    @Override
    public void onImageQueryTaskComplete(Map<String, Object> imageResult) {
        //mImageGridViewAdapter.notifyDataSetChanged();
        ArrayList<String> imageUrls = (ArrayList<String>) imageResult.get(ImageGridViewFragment.EXTRA_IMAGE_URL_LIST);
        mCurrentPage = (Integer) imageResult.get(ImageGridViewFragment.EXTRA_IMAGE_QUERY_CURRENT_PAGE);
        mImageUrls.addAll(imageUrls);
        mImageGridViewAdapter.addMoreImageUrls(imageUrls);
    }

}
