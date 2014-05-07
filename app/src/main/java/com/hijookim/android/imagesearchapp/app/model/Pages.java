package com.hijookim.android.imagesearchapp.app.model;

import org.json.*;


public class Pages {
	
    private String start;
    private double label;
    
    
	public Pages () {
		
	}	
        
    public Pages (JSONObject json) {
    
        this.start = json.optString("start");
        this.label = json.optDouble("label");

    }
    
    public String getStart() {
        return this.start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public double getLabel() {
        return this.label;
    }

    public void setLabel(double label) {
        this.label = label;
    }


    
}
