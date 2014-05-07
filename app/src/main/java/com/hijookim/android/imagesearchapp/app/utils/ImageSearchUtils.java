package com.hijookim.android.imagesearchapp.app.utils;


import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ImageSearchUtils {

    public static final String TAG = "ImageSearchUtils";
    public static final String QUERY_FILE_NAME = "image_search_query_file";

    public static final String GOOGLE_IMAGE_SEARCH_BASE_URL = "https://ajax.googleapis.com/ajax/services/search/images?q=";
    public static final String IMAGE_SEARCH_VERSION = "&v=1.0";
    public static final String IMAGE_SEARCH_PAGE_START = "&start=";

    public static DefaultHttpClient mHttpClient = new DefaultHttpClient();
    public static final ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
    }


    public static void writeQueryKeywordsToLocalStorage(Context context, String keyword) {
        if (keyword != null && context != null) {
            try {
                FileOutputStream fos = context.openFileOutput(QUERY_FILE_NAME, Context.MODE_APPEND);
                fos.write(keyword.getBytes());
                fos.write('\n');
                fos.close();
            } catch (FileNotFoundException e) {
                Log.v(TAG, "file with name: " + QUERY_FILE_NAME + " not found to write to");
            } catch (IOException e) {
                Log.v(TAG, "unable to write to file");
            }
        }
    }

    public static ArrayList<String> readQueryKeywordsFromLocalStorage(Context context) {
        ArrayList<String> pastQueryWords = new ArrayList<String>();

        if (context != null) {
            try {
                FileInputStream fos = context.openFileInput(QUERY_FILE_NAME);
                BufferedInputStream bufferedInputStream = new BufferedInputStream(fos);

                StringBuffer strbuf = new StringBuffer();
                String queryWord;
                while(bufferedInputStream.available() > 0) {
                    char c = (char) bufferedInputStream.read();
                    if (c != '\n') {
                        strbuf.append(c);
                    } else {
                        queryWord = strbuf.toString();
                        pastQueryWords.add(0, queryWord);  // adding string in rev chron order
                        strbuf = new StringBuffer();
                    }
                }

                // for the last word in the file that hasn't been added to the list yet
                queryWord = strbuf.toString();
                if (!TextUtils.isEmpty(queryWord)) {
                    pastQueryWords.add(queryWord);
                }

            } catch (FileNotFoundException e) {
                Log.v(TAG, "file with name: " + QUERY_FILE_NAME + " not found to read from");
            } catch (IOException e) {
                Log.v(TAG, "unable to read from file");
            }
        }

        return pastQueryWords;
    }

    public static Map<String, Object> queryImage(String keywords, String pageStartIndex) {
        List<String> imageUrls;
        Map<String, Object> result = null;

        if (keywords == null || TextUtils.isEmpty(keywords)) {
            return null;
        }

        if (pageStartIndex == null) {
            pageStartIndex = String.valueOf(0);
        }

        try {
            StringBuilder url = new StringBuilder(GOOGLE_IMAGE_SEARCH_BASE_URL);
            String encodedKeyword = URLEncoder.encode(keywords);
            url.append(encodedKeyword);
            url.append(IMAGE_SEARCH_VERSION);
            url.append(IMAGE_SEARCH_PAGE_START);
            url.append(String.valueOf(pageStartIndex));

            HttpGet queryRequest = new HttpGet(url.toString());
            result = makeRequest(queryRequest, Map.class);
            //imageUrls = convertResultToImageUrlArray(result);
        } catch (IOException e) {
            Log.v(TAG, "error making image search request");
        }
        return result;
    }


    public static <T> T makeRequest(HttpUriRequest request, Class<T> clazz) throws IOException {

        if (request == null) {
            return null;
        }
        String responseStr = "";
        T responseObj = null;

        request.addHeader("Accept", "application/json");
        HttpResponse response = null;
        try {
            response = mHttpClient.execute(request);
            int statusCode = response.getStatusLine().getStatusCode();
            HttpEntity entity = response.getEntity();

            if (statusCode == 200) {
                InputStream instream = entity.getContent();
                //responseStr = convertStreamToString(instream);
                responseObj = convertObjectFromJson(instream, clazz);
                instream.close();
            }
        } catch (IOException e) {
            Log.v(TAG, "error making request");
        }
        return responseObj;
    }


    public static String convertStreamToString(InputStream inputStream) throws IOException {
        if (inputStream != null) {
            StringBuilder sb = new StringBuilder();
            String line;
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
            } catch (IOException e) {
                Log.v(TAG, "error convertint stream to String");
            }
            return sb.toString();
        }
        return "";
    }

    public static <T> T convertObjectFromJson(InputStream json, Class<T> clazz) {
        T obj = null;
        try {
            obj = mapper.readValue(json, clazz);
        } catch (Exception e) {
            Log.v(TAG, "unable to convert json to object");
        }
        return obj;
    }

    public static ArrayList<String> getImageUrls(Map<String, Object> imageSearchResponse, ArrayList<String> imageUrls) {
        if (imageUrls == null) {
            imageUrls = new ArrayList<String>();
        }

        Map<String, Object> responseData = (Map<String, Object>) imageSearchResponse.get("responseData");
        if (responseData != null && responseData.get("results") != null) {

            List<Object> results = (List<Object>) ((Map<String, Object>) imageSearchResponse.get("responseData")).get("results");

            for (Object result : results) {
                String imageUrl = (String) ((Map<String, Object>) result).get("url");
                if (!TextUtils.isEmpty(imageUrl)) {
                    imageUrls.add(imageUrl);
                }
            }
        }
        return imageUrls;
    }

    public static ArrayList<Integer> getImageSearchPages(Map<String, Object> imageSearchResponse) {

        ArrayList<Integer> imagePages = new ArrayList<Integer>();

        Map<String, Object> responseData = (Map<String, Object>) imageSearchResponse.get("responseData");
        if (responseData != null && responseData.get("cursor") != null) {
            Map<String, Object> cursor = (Map<String, Object>) responseData.get("cursor");

            int currentPageIndex = (Integer) cursor.get("currentPageIndex");
            int nextPageIndex = currentPageIndex + 1;
            ArrayList<Object> pages = (ArrayList<Object>) cursor.get("pages");

            for (Object page : pages) {
                Map<String, Object> pageMap = (Map<String, Object>) page;
                String startVal = (String)pageMap.get("start");
                if (!TextUtils.isEmpty(startVal)) {
                    imagePages.add(Integer.valueOf(startVal));
                }
            }
        }
        return imagePages;
    }

    public static int getCurrentPageIndex(Map<String, Object> imageSearchResponse) {
        int currentPageIndex = 0;

        Map<String, Object> responseData = (Map<String, Object>) imageSearchResponse.get("responseData");
        if (responseData != null && responseData.get("cursor") != null) {
            Map<String, Object> cursor = (Map<String, Object>) responseData.get("cursor");

            currentPageIndex = (Integer) cursor.get("currentPageIndex");
        }
        return currentPageIndex;
    }

}
