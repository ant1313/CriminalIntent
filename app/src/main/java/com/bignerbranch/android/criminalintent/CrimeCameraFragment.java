package com.bignerbranch.android.criminalintent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * Created by Антон on 19.03.2017.
 */

public class CrimeCameraFragment extends Fragment {
    //private static final String TAG = "com.bignerbranch.android.criminalintent.CrimeCameraFragment";
    private static final String TAG = "...CrimeCameraFragment";
    public static final String EXTRA_PHOTO_FILENAME = "com.bignerbranch.android.criminalintent.CrimeCameraFragmet.photo_filename";
    private SurfaceView mSurfaceView;
    private Button takePictureButton;
    private android.hardware.Camera mCamera;
    private View mProgressContainer;

    private Camera.ShutterCallback mshutterCallback = new Camera.ShutterCallback() {
        @Override
        public void onShutter() {
            mProgressContainer.setVisibility(View.VISIBLE);
        }
    };

    private Camera.PictureCallback mJpegCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            String fileName = UUID.randomUUID().toString()+".jpg";
            boolean success = true;
            FileOutputStream os = null;
            try {
                os = getActivity().openFileOutput(fileName, Context.MODE_APPEND);
                os.write(data);
            } catch (IOException e) {
                e.printStackTrace();
                success = false;
            } finally {
                if(os!=null){
                    try {
                        os.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        success =false;
                    }
                }
            }
            if(success){
                Intent i = new Intent();
                i.putExtra(EXTRA_PHOTO_FILENAME,fileName);
                getActivity().setResult(Activity.RESULT_OK,i);
            } else {
                getActivity().setResult(Activity.RESULT_CANCELED);
            }
            getActivity().finish();
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime_camera,container,false);
        takePictureButton = (Button) v.findViewById(R.id.crime_camera_takePictureButton);
        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCamera.takePicture(mshutterCallback,null,mJpegCallback);
            }
        });
        mProgressContainer = v.findViewById(R.id.camera_progressbar_container);
        mProgressContainer.setVisibility(View.INVISIBLE);
        mSurfaceView = (SurfaceView) v.findViewById(R.id.crime_camera_surfaceView);
        SurfaceHolder holder = mSurfaceView.getHolder();
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if(mCamera!=null){
                    try {
                        mCamera.setPreviewDisplay(holder);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                if(mCamera==null)
                    return;
                Camera.Parameters parameters = mCamera.getParameters();
                Camera.Size size = getBestSupportSizes(parameters.getSupportedPictureSizes(),width,height);
                parameters.setPreviewSize(size.width,size.height);
                mCamera.setParameters(parameters);
                try {
                    mCamera.startPreview();
                } catch (Exception e){
                    Log.e(TAG,"Cannot start preview",e);
                    mCamera.release();
                    mCamera=null;
                }

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                if(mCamera!=null){
                    mCamera.stopPreview();
                }
            }
        });
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB){
            mCamera = Camera.open(0);
        } else {
            mCamera = Camera.open();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mCamera!=null){
            mCamera.release();
            mCamera = null;
        }
    }

    private Camera.Size getBestSupportSizes(List<Camera.Size> sizes, int widht, int height){
        Camera.Size bestSize = sizes.get(0);
        int largeArea = bestSize.width*bestSize.height;
        for (Camera.Size size: sizes) {
            int Area = size.width*size.height;
            if(Area>largeArea){
                bestSize = size;
                largeArea = Area;
            }
        }
        return bestSize;
    }
}
