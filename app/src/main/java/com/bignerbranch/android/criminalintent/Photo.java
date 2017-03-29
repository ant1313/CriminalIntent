package com.bignerbranch.android.criminalintent;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Антон on 26.01.2017.
 */

public class Photo {
    private static final String JSON_FILENAME = "filename";
    private String mFilename;

    public Photo(String filename){
        mFilename = filename;
    }

    public Photo(JSONObject json) throws JSONException {
        mFilename = json.getString(JSON_FILENAME);
    }

    public JSONObject toJson() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(JSON_FILENAME,mFilename);
        return json;
    }

    public String getFilename(){
        return mFilename;
    }
}
