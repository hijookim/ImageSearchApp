package com.hijookim.android.imagesearchapp.app.model;

import org.json.*;
import java.util.ArrayList;

public class ResponseData {
	
    private ArrayList<Results> results;
    private Cursor cursor;
    
    
	public ResponseData () {
		
	}	
        
    public ResponseData (JSONObject json) {
    

        this.results = new ArrayList<Results>();
        JSONArray arrayResults = json.optJSONArray("results");
        if (null != arrayResults) {
            int resultsLength = arrayResults.length();
            for (int i = 0; i < resultsLength; i++) {
                JSONObject item = arrayResults.optJSONObject(i);
                if (null != item) {
                    this.results.add(new Results(item));
                }
            }
        }
        else {
            JSONObject item = json.optJSONObject("results");
            if (null != item) {
                this.results.add(new Results(item));
            }
        }

        this.cursor = new Cursor(json.optJSONObject("cursor"));

    }
    
    public ArrayList<Results> getResults() {
        return this.results;
    }

    public void setResults(ArrayList<Results> results) {
        this.results = results;
    }

    public Cursor getCursor() {
        return this.cursor;
    }

    public void setCursor(Cursor cursor) {
        this.cursor = cursor;
    }


    
}
