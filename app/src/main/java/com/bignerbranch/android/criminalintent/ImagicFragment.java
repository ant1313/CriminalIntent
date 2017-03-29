package com.bignerbranch.android.criminalintent;


import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


public class ImagicFragment extends DialogFragment {
   public static final String EXTRA_IMAGE_PATH = "com.exameple.luos.criminalintent.image_path";
   private ImageView mImageView;

   public static ImagicFragment newInstance(String imageFilename){
       Bundle args = new Bundle();
       args.putSerializable(EXTRA_IMAGE_PATH,imageFilename);

       ImagicFragment fragment = new ImagicFragment();
       fragment.setArguments(args);
       fragment.setStyle(DialogFragment.STYLE_NO_TITLE,0);

       return fragment;
   }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mImageView = new ImageView(getActivity());
        String path = (String) getArguments().getSerializable(EXTRA_IMAGE_PATH);
        BitmapDrawable image = PictureUitls.getScaledDrawable(getActivity(),path);
        mImageView.setImageDrawable(image);
        return mImageView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        PictureUitls.cleanImagView(mImageView);
    }
}
