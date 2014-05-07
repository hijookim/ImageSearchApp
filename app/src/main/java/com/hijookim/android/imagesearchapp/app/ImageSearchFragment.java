package com.hijookim.android.imagesearchapp.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.hijookim.android.imagesearchapp.app.utils.ImageSearchTask;
import com.hijookim.android.imagesearchapp.app.utils.ImageSearchUtils;

import java.util.ArrayList;
import java.util.Map;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * Use the {@link ImageSearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class ImageSearchFragment extends Fragment implements OnImageQueryTaskExecuted {

    public static final String TAG = "ImageSearchFragment";
    public static final String FRAGMENT_TAG = "fragment_image_search";
    public static final String EXTRA_PAST_IMAGE_QUERY_LIST = "extra_past_image_query_list";  // should this constant be defined in ImageQueryListFragment?

    private OnImageQueryTaskExecuted mOnImageQueryTaskExecuted = this;
    private ImageQueryListFragment mImageQueryListFragment;

    private EditText mSearchEditor;
    private Button mSearchSubmitButton;


    public static ImageSearchFragment newInstance(Bundle args) {
        ImageSearchFragment fragment = new ImageSearchFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static ImageSearchFragment getInstance(FragmentManager fragmentManager, Bundle args) {
        ImageSearchFragment instance = null;
        Fragment fragment = fragmentManager.findFragmentByTag(FRAGMENT_TAG);
        if (fragment == null) {
            instance = ImageSearchFragment.newInstance(args);
        } else {
            instance = (ImageSearchFragment) fragment;
        }
        return instance;
    }

    public ImageSearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.image_search_fragment, container, false);

        if (rootView == null) {
            return rootView;
        }

        mSearchEditor = (EditText) rootView.findViewById(R.id.search_editor);
        mSearchSubmitButton = (Button) rootView.findViewById(R.id.submit_button);
        mSearchSubmitButton.setOnClickListener(getSubmitOnClickListener());

        ArrayList<String> pastImageQueries = ImageSearchUtils.readQueryKeywordsFromLocalStorage(getActivity());
        if (pastImageQueries != null && !pastImageQueries.isEmpty()) {
            Bundle bundle = new Bundle();
            bundle.putStringArrayList(EXTRA_PAST_IMAGE_QUERY_LIST, pastImageQueries);
            attachImageQueryListFragment(bundle);
        }

        // ** IMPLEMENT CHANGE TO REFRESH THE QUERY HISTORY WHEN BACK BUTTON IS PRESSED

        // change the logic so that when we come back from the image grid view, the prev query list will be updated

        return rootView;
    }

    private void attachImageQueryListFragment(Bundle bundle) {
        FragmentManager mgr = getFragmentManager();
        mImageQueryListFragment = ImageQueryListFragment.getInstance(mgr, bundle);
        FragmentTransaction ft = mgr.beginTransaction();
        ft.replace(R.id.image_query_list_container, mImageQueryListFragment, mImageQueryListFragment.getFragmentTag()).commit();
        //mgr.executePendingTransactions();

    }

    private View.OnClickListener getSubmitOnClickListener() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on click, save the query keyword into a local cache
                if (mSearchEditor == null || TextUtils.isEmpty(mSearchEditor.getText())) {
                    return;
                }

                String queryKeyword = mSearchEditor.getText().toString();
                ImageSearchUtils.writeQueryKeywordsToLocalStorage(getActivity(), queryKeyword);

                new ImageSearchTask(mOnImageQueryTaskExecuted, queryKeyword, null).execute();
            }
        };
        return listener;
    }

    @Override
    public void onImageQueryTaskComplete(Map<String, Object> imageResult) {
        Context context = getActivity();
        Intent intent = new Intent(context, ImageGridViewActivity.class);
        Bundle bundle = new Bundle();

        ArrayList<String> urls = (ArrayList<String>) imageResult.get(ImageGridViewFragment.EXTRA_IMAGE_URL_LIST);
        ArrayList<Integer> pages = (ArrayList<Integer>) imageResult.get(ImageGridViewFragment.EXTRA_IMAGE_QUERY_PAGES);
        String keyword = (String) imageResult.get(ImageGridViewFragment.EXTRA_IMAGE_QUERY_KEYWORD);
        Integer currentPage = (Integer) imageResult.get(ImageGridViewFragment.EXTRA_IMAGE_QUERY_CURRENT_PAGE);

        bundle.putStringArrayList(ImageGridViewFragment.EXTRA_IMAGE_URL_LIST, urls);
        bundle.putIntegerArrayList(ImageGridViewFragment.EXTRA_IMAGE_QUERY_PAGES, pages);
        bundle.putString(ImageGridViewFragment.EXTRA_IMAGE_QUERY_KEYWORD, keyword);
        bundle.putInt(ImageGridViewFragment.EXTRA_IMAGE_QUERY_CURRENT_PAGE, currentPage);
        intent.putExtras(bundle);

        context.startActivity(intent);
    }
}