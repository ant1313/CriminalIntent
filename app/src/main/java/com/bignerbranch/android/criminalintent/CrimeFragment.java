package com.bignerbranch.android.criminalintent;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import org.json.JSONException;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Антон on 27.01.2017.
 */

public class CrimeFragment extends Fragment {
    public static final String EXTRA_CRIME_ID =
            "com.bignerdranch.android.criminalintent.crime_id";
    private static final String DIALOG_DATE = "date";

    private static final String DIALOG_TIME = "time";
    private static final int REQUEST_TIME = 1;

    private static final String DIOLOG_DATA = "data";
    private static final String DIOLOG_IMAGE = "image";
    private static final String TAG = "CrimeFragment";
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_PHOTO = 1;
    private static final int REQUEST_CONTACT = 2;

    private Crime mCrime;
    private EditText mTitleFeild;
    private Button mDateButton;
    private CheckBox mSolvedCheckBox;
    private ImageButton mPictureButon;
    private ImageView mPhototView;

    private Button mSuspectButton;
  //  private Callbacks mCallbacks;

    /**
     * Обязательный интерфейс для активности-хоста
     */
/*
    public interface Callbacks {
        void onCrimeUpdated(Crime crime);
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallbacks = (Callbacks)activity;
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }
*/

    public CrimeFragment() {
        // Required empty public constructor
    }

    public void updataData() {
        //дата в формате Tuesday, Oct 12, 2012
        String dateFormat = "EEEE, MMM dd, yyyy";
        String dateString = DateFormat.format(dateFormat, mCrime.getDate()).toString();

        mDateButton.setText(dateString);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        UUID id = (UUID) getArguments().getSerializable(EXTRA_CRIME_ID);
        mCrime = CrimeLab.get(getActivity()).getCrime(id);
    }

    @Override
    public void onStart() {
        super.onStart();
        showPhoto();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode!= Activity.RESULT_OK){
            return;
        }
        switch (requestCode){
            case REQUEST_DATE:
                Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
                mCrime.setDate(date);
                updataData();
                break;
            case REQUEST_PHOTO:
                String fileName = data.getStringExtra(CrimeCameraFragment.EXTRA_PHOTO_FILENAME);
                if(fileName!=null){
                    Photo p = new Photo(fileName);
                    mCrime.setmPhoto(p);
                    showPhoto();
                }
            case REQUEST_CONTACT: {
                Uri contactUri = data.getData();
// Определение полей, значения которых должны быть
// возвращены запросом.
                String[] queryFields = new String[]{
                        ContactsContract.Contacts.DISPLAY_NAME
                };
// Выполнение запроса - contactUri здесь выполняет функции
// условия "where"
                Cursor c = getActivity().getContentResolver()
                        .query(contactUri, queryFields, null, null, null);
// Проверка получения результатов
                if (c.getCount() == 0) {
                    c.close();
                    return;
                }
// Извлечение первого столбца данных - имени подозреваемого.
                c.moveToFirst();
                String suspect = c.getString(0);
                mCrime.setSuspect(suspect);
                mSuspectButton.setText(suspect);
                c.close();
            }
        }
        /*
            FragmentManager fm = getActivity()
                    .getSupportFragmentManager();
            TimePickerFragment dialog = TimePickerFragment
                    .newInstance(mCrime.getTime());
            dialog.setTargetFragment(CrimeFragment1.this, REQUEST_TIME);
            dialog.show(fm, DIALOG_TIME);
        }

        if (requestCode == REQUEST_TIME) {
            Date date = (Date)data
                    .getSerializableExtra(TimePickerFragment.EXTRA_TIME);
            mCrime.setTime(date);
            updateTime();
        }
        */
    }


    @Override
    public void onPause() {
        super.onPause();
        try {
            CrimeLab.get(getActivity()).saveCrimes();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        PictureUitls.cleanImagView(mPhototView);
    }

    @TargetApi(11)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime,container,false);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB){
            if(NavUtils.getParentActivityName(getActivity())!=null){
                ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }
        mTitleFeild = (EditText) view.findViewById(R.id.crime_title);
        mTitleFeild.setText(mCrime.getTitle());
        mTitleFeild.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCrime.setTitle(s.toString());
             //   mCallbacks.onCrimeUpdated(mCrime);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
/*
        mPictureButon = (ImageButton) view.findViewById(R.id.crime_camera_imageButton);
        mPictureButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),CrimeCameraActivity.class);
                startActivityForResult(i,REQUEST_PHOTO);
            }
        });
        */
        PackageManager pm = getActivity().getPackageManager();
        boolean hasACrame = pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)||
                pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)||
                Build.VERSION.SDK_INT<Build.VERSION_CODES.HONEYCOMB;
        if(!hasACrame){
            mPictureButon.setEnabled(false);
        }

        mPhototView = (ImageView) view.findViewById(R.id.crime_imageView);
        mPhototView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Photo p = mCrime.getPhoto();
                if(p==null)
                    return;
                FragmentManager fm = getActivity().getSupportFragmentManager();
                String path = getActivity().getFileStreamPath(p.getFilename()).getAbsolutePath();
                ImagicFragment.newInstance(path).show(fm, DIOLOG_IMAGE);
            }
        });

        mDateButton= (Button) view.findViewById(R.id.crime_data);
        if(mDateButton!=null){
            updataData();
            mDateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getDate());
                    dialog.setTargetFragment(CrimeFragment.this,REQUEST_DATE);
                    dialog.show(fm,DIOLOG_DATA);
                }
            });
        }
        /*
        mTimeButton = (Button)v.findViewById(R.id.crime_time);
        updateTime();

        mTimeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentManager fm = getActivity()
                        .getSupportFragmentManager();
                TimePickerFragment dialog = TimePickerFragment
                        .newInstance(mCrime.getTime());
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_TIME);
                dialog.show(fm, DIALOG_TIME);
            }
        });
*/

        mSolvedCheckBox = (CheckBox) view.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setSolved(isChecked);
            }
        });

        Button reportButton = (Button)view.findViewById(R.id.crime_reportButton);
        reportButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
                i.putExtra(Intent.EXTRA_SUBJECT,
                        getString(R.string.crime_report_subject));
                //Использование списка выбора приложения
                i = Intent.createChooser(i, getString(R.string.send_report));
                startActivity(i);
            }
        });

        mSuspectButton = (Button)view.findViewById(R.id.crime_suspectButton);
        mSuspectButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK,
                        ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(i, REQUEST_CONTACT);
            }
        });
        if (mCrime.getSuspect() != null) {
            mSuspectButton.setText(mCrime.getSuspect());
        }
        return view;
    }

    private void showPhoto(){
        Photo p = mCrime.getPhoto();
        BitmapDrawable b = null;
        if(p!=null){
            String path = getActivity().getFileStreamPath(p.getFilename()).getAbsolutePath();
            b = PictureUitls.getScaledDrawable(getActivity(),path);
        }
        mPhototView.setImageDrawable(b);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                if(NavUtils.getParentActivityName(getActivity())!=null){
                    NavUtils.navigateUpFromSameTask(getActivity());
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public static CrimeFragment newInstance(UUID id){
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_CRIME_ID,id);

        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);

        return fragment;
    }
    private String getCrimeReport() {
        String solvedString = null;
        if (mCrime.isSolved()) {
            solvedString = getString(R.string.crime_report_solved);
        } else {
            solvedString = getString(R.string.crime_report_unsolved);
        }
        String dateFormat = "EEE, MMM dd";
        String dateString = DateFormat.format(dateFormat, mCrime.getDate()).toString();
        String suspect = mCrime.getSuspect();
        if (suspect == null) {
            suspect = getString(R.string.crime_report_no_suspect);
        } else {
            suspect = getString(R.string.crime_report_suspect, suspect);
        }
        String report = getString(R.string.crime_report,
                mCrime.getTitle(), dateString, solvedString, suspect);
        return report;
    }
/*
        mTimeButton = (Button)v.findViewById(R.id.crime_time);
        updateTime();

        mTimeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentManager fm = getActivity()
                        .getSupportFragmentManager();
                TimePickerFragment dialog = TimePickerFragment
                        .newInstance(mCrime.getTime());
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_TIME);
                dialog.show(fm, DIALOG_TIME);
            }
        });
*/

}
