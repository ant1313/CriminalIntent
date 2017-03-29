package com.bignerbranch.android.criminalintent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Антон on 26.01.2017.
 */

public class Crime extends Object {
    private static final String JSON_ID = "id";
    private static final String JSON_TITLE = "title";
    private static final String JSON_DATE = "date";
    private static final String JSON_SOLVED = "solved";
    private static final String JSON_PHOTO = "photo";
    private static final String JSON_SUSPECT = "suspect";

    private UUID mId;
    private String mTitle;
    private Date mDate;
    private Photo mPhoto;
    private boolean isSolved;
    private String mSuspect;

    public Crime(){
        mId = UUID.randomUUID();
        mDate = new Date();
        //mTime = new Date();
    }

    public Crime(JSONObject json) throws JSONException {
        mId = UUID.fromString(json.getString(JSON_ID));
        if(json.has(JSON_TITLE)){
            mTitle = json.getString(JSON_TITLE);
        }
        isSolved = json.getBoolean(JSON_SOLVED);
        mDate = new Date(json.getLong(JSON_DATE));
        if(json.has(JSON_PHOTO)){
            mPhoto = new Photo(json.getString(JSON_PHOTO));
        }
        if (json.has(JSON_SUSPECT))
            mSuspect = json.getString(JSON_SUSPECT);

    }

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date mData) {
        this.mDate = mData;
    }

    public boolean isSolved() {
        return isSolved;
    }

    public void setSolved(boolean sloved) {
        isSolved = sloved;
    }

    public Photo getPhoto() {
        return mPhoto;
    }

    public void setmPhoto(Photo mPhoto) {
        this.mPhoto = mPhoto;
    }

    @Override
    public String toString() {
        return mTitle;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(JSON_ID,mId.toString());
        json.put(JSON_DATE,mDate);
        json.put(JSON_TITLE,mTitle);
        json.put(JSON_SOLVED,isSolved);
        if(mPhoto!=null){
            json.put(JSON_PHOTO,mPhoto.getFilename());
        }
        json.put(JSON_SUSPECT, mSuspect);
        return json;
    }
    public String getSuspect() {
        return mSuspect;
    }
    public void setSuspect(String suspect) {
        mSuspect = suspect;
    }
    /*
    public Date getTime() {
        return mTime;
    }
    */
}
