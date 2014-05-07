package com.hijookim.android.imagesearchapp.app.model;

import org.json.*;


public class Results {
	
    private String titleNoFormatting;
    private String contentNoFormatting;
    private String width;
    private String url;
    private String originalContextUrl;
    private String title;
    private String tbUrl;
    private String gsearchResultClass;
    private String imageId;
    private String height;
    private String tbWidth;
    private String tbHeight;
    private String unescapedUrl;
    private String visibleUrl;
    private String content;
    
    
	public Results () {
		
	}	
        
    public Results (JSONObject json) {
    
        this.titleNoFormatting = json.optString("titleNoFormatting");
        this.contentNoFormatting = json.optString("contentNoFormatting");
        this.width = json.optString("width");
        this.url = json.optString("url");
        this.originalContextUrl = json.optString("originalContextUrl");
        this.title = json.optString("title");
        this.tbUrl = json.optString("tbUrl");
        this.gsearchResultClass = json.optString("GsearchResultClass");
        this.imageId = json.optString("imageId");
        this.height = json.optString("height");
        this.tbWidth = json.optString("tbWidth");
        this.tbHeight = json.optString("tbHeight");
        this.unescapedUrl = json.optString("unescapedUrl");
        this.visibleUrl = json.optString("visibleUrl");
        this.content = json.optString("content");

    }
    
    public String getTitleNoFormatting() {
        return this.titleNoFormatting;
    }

    public void setTitleNoFormatting(String titleNoFormatting) {
        this.titleNoFormatting = titleNoFormatting;
    }

    public String getContentNoFormatting() {
        return this.contentNoFormatting;
    }

    public void setContentNoFormatting(String contentNoFormatting) {
        this.contentNoFormatting = contentNoFormatting;
    }

    public String getWidth() {
        return this.width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getOriginalContextUrl() {
        return this.originalContextUrl;
    }

    public void setOriginalContextUrl(String originalContextUrl) {
        this.originalContextUrl = originalContextUrl;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTbUrl() {
        return this.tbUrl;
    }

    public void setTbUrl(String tbUrl) {
        this.tbUrl = tbUrl;
    }

    public String getGsearchResultClass() {
        return this.gsearchResultClass;
    }

    public void setGsearchResultClass(String gsearchResultClass) {
        this.gsearchResultClass = gsearchResultClass;
    }

    public String getImageId() {
        return this.imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getHeight() {
        return this.height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getTbWidth() {
        return this.tbWidth;
    }

    public void setTbWidth(String tbWidth) {
        this.tbWidth = tbWidth;
    }

    public String getTbHeight() {
        return this.tbHeight;
    }

    public void setTbHeight(String tbHeight) {
        this.tbHeight = tbHeight;
    }

    public String getUnescapedUrl() {
        return this.unescapedUrl;
    }

    public void setUnescapedUrl(String unescapedUrl) {
        this.unescapedUrl = unescapedUrl;
    }

    public String getVisibleUrl() {
        return this.visibleUrl;
    }

    public void setVisibleUrl(String visibleUrl) {
        this.visibleUrl = visibleUrl;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    
}
