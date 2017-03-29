package com.bignerbranch.android.criminalintent;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Антон on 29.01.2017.
 */

public class CrimeLab extends Object {
    private static final String TAG = "CrimeLab";
    private static final String FIlENAME = "crime.json";

    private static CrimeLab sCrimelab;
    private Context mAppContext;
    private ArrayList<Crime> mCrimes;
    private CriminalIntentJSONSerializer mSerializer;

    public CrimeLab(Context context){
        mAppContext = context;
        try {
            Log.d(TAG,"Loading JSON....");
            mSerializer = new CriminalIntentJSONSerializer(mAppContext,FIlENAME);
            mCrimes = mSerializer.loadJSon();
        } catch (IOException e) {
            mCrimes = new ArrayList<Crime>();
            Log.d(TAG,"Error load crime:"+e);
        }
    }

    public void loadCrimes(){
        try {
            Log.d(TAG,"Loading JSON....");
            mSerializer = new CriminalIntentJSONSerializer(mAppContext,FIlENAME);
            mCrimes = mSerializer.loadJSon();
        } catch (IOException e) {
            mCrimes = new ArrayList<Crime>();
            Log.d(TAG,"Error load crime:"+e);
        }
    }

    public static CrimeLab get(Context context){
        if(sCrimelab==null){
            sCrimelab = new CrimeLab(context.getApplicationContext());
        }
        return sCrimelab;
    }

    public ArrayList<Crime> getCrimes(){
        return mCrimes;
    }

    public Crime getCrime(UUID id){
        for (Crime c: mCrimes) {
            if(c.getId().equals(id)){
                return c;
            }
        }
        return null;
    }

    public void addCrime(Crime c){
        mCrimes.add(c);
    }

    public void removeCrime(Crime c){
        mCrimes.remove(c);
    }

    public boolean saveCrimes() throws JSONException {
        Log.d(TAG,"try to save JSON....");
        mSerializer.saveCrimes(mCrimes);
        //для реального приложения применить уведомление через Toast или диалогового окна.
        Log.d(TAG,"Crimes saved to file.");
        return true;
    }

}
