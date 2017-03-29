package com.bignerbranch.android.criminalintent;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

/**
 * Created by Антон on 19.03.2017.
 */

public class CriminalIntentJSONSerializer {
    private static final String TAG = "...IntentJSONSerializer";
    private Context mContext;
    private String mFileName;

    public CriminalIntentJSONSerializer(Context context, String fileName){
        mContext = context;
        mFileName = fileName;
    }

    public void saveCrimes(ArrayList<Crime> crimes) throws JSONException {
        JSONArray jsonArray = new JSONArray();
        for (Crime c: crimes) {
            jsonArray.put(c.toJson());
        }
        Writer writer = null;
        try {
            Log.d(TAG,"try to save JSON. File:"+mFileName);
            FileOutputStream out = mContext.openFileOutput(mFileName,Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(jsonArray.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(writer!=null){
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public ArrayList<Crime> loadJSon() throws IOException {
        ArrayList<Crime> crimes = new ArrayList<Crime>();
        BufferedReader reader = null;
        try {
            // Открытие и чтение файла в StringBuilder
            FileInputStream in = new FileInputStream(mFileName);
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuffer buffer = new StringBuffer();
            String data = null;
            while( (data = reader.readLine())!=null){
                buffer.append(data);
            }
            // Разбор JSON с использованием JSONTokener
            JSONArray array = (JSONArray) new JSONTokener(buffer.toString()).nextValue();
            // Построение массива объектов Crime по данным JSONObject
            for(int i = 0; i < array.length(); i++){
                crimes.add(new Crime(array.getJSONObject(i)));
            }

        } catch (FileNotFoundException e) {
            // Происходит при начале "с нуля"; не обращайте внимания
           // e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if(reader!=null){
                reader.close();
            }
        }
        return crimes;

    }
}
