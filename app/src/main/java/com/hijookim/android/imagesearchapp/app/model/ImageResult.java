package com.hijookim.android.imagesearchapp.app.model;

import org.json.*;


public class ImageResult {
	
    private double responseStatus;
    private ResponseData responseData;
    private Object responseDetails;
    
    
	public ImageResult () {
		
	}	
        
    public ImageResult (JSONObject json) {
    
        this.responseStatus = json.optDouble("responseStatus");
        this.responseData = new ResponseData(json.optJSONObject("responseData"));
        this.responseDetails = json.opt("responseDetails");

    }
    
    public double getResponseStatus() {
        return this.responseStatus;
    }

    public void setResponseStatus(double responseStatus) {
        this.responseStatus = responseStatus;
    }

    public ResponseData getResponseData() {
        return this.responseData;
    }

    public void setResponseData(ResponseData responseData) {
        this.responseData = responseData;
    }

    public Object getResponseDetails() {
        return this.responseDetails;
    }

    public void setResponseDetails(Object responseDetails) {
        this.responseDetails = responseDetails;
    }


    
}
