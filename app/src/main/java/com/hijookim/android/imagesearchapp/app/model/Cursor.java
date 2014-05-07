package com.hijookim.android.imagesearchapp.app.model;

import org.json.*;
import java.util.ArrayList;

public class Cursor {
	
    private String estimatedResultCount;
    private ArrayList<Pages> pages;
    private String resultCount;
    private double currentPageIndex;
    private String moreResultsUrl;
    private String searchResultTime;
    
    
	public Cursor () {
		
	}	
        
    public Cursor (JSONObject json) {
    
        this.estimatedResultCount = json.optString("estimatedResultCount");

        this.pages = new ArrayList<Pages>();
        JSONArray arrayPages = json.optJSONArray("pages");
        if (null != arrayPages) {
            int pagesLength = arrayPages.length();
            for (int i = 0; i < pagesLength; i++) {
                JSONObject item = arrayPages.optJSONObject(i);
                if (null != item) {
                    this.pages.add(new Pages(item));
                }
            }
        }
        else {
            JSONObject item = json.optJSONObject("pages");
            if (null != item) {
                this.pages.add(new Pages(item));
            }
        }

        this.resultCount = json.optString("resultCount");
        this.currentPageIndex = json.optDouble("currentPageIndex");
        this.moreResultsUrl = json.optString("moreResultsUrl");
        this.searchResultTime = json.optString("searchResultTime");

    }
    
    public String getEstimatedResultCount() {
        return this.estimatedResultCount;
    }

    public void setEstimatedResultCount(String estimatedResultCount) {
        this.estimatedResultCount = estimatedResultCount;
    }

    public ArrayList<Pages> getPages() {
        return this.pages;
    }

    public void setPages(ArrayList<Pages> pages) {
        this.pages = pages;
    }

    public String getResultCount() {
        return this.resultCount;
    }

    public void setResultCount(String resultCount) {
        this.resultCount = resultCount;
    }

    public double getCurrentPageIndex() {
        return this.currentPageIndex;
    }

    public void setCurrentPageIndex(double currentPageIndex) {
        this.currentPageIndex = currentPageIndex;
    }

    public String getMoreResultsUrl() {
        return this.moreResultsUrl;
    }

    public void setMoreResultsUrl(String moreResultsUrl) {
        this.moreResultsUrl = moreResultsUrl;
    }

    public String getSearchResultTime() {
        return this.searchResultTime;
    }

    public void setSearchResultTime(String searchResultTime) {
        this.searchResultTime = searchResultTime;
    }


    
}
