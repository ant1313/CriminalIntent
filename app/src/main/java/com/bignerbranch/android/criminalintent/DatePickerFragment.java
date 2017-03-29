package com.bignerbranch.android.criminalintent;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Антон on 13.03.2017.
 */

public class DatePickerFragment extends DialogFragment {
    public static final String EXTRA_DATE =
            "com.bignerdranch.android.criminalintent.date";
    private Date mDate;
    public static DatePickerFragment newInstance(Date date) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_DATE, date);
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void sendResult(int resultCode) {
        if (getTargetFragment() == null)
            return;
        Intent i = new Intent();
        i.putExtra(EXTRA_DATE, mDate);
        getTargetFragment()
                .onActivityResult(getTargetRequestCode(), resultCode, i);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        /**
         * Экземпляр DatePickerFragment должен инициализировать DatePicker по инфор-
         мации, хранящейся в Date. Однако для инициализации DatePicker необходимо
         иметь целочисленные значения месяца, дня и года. Объект Date больше напоминает
         временную метку и не может предоставить нужные целые значения напрямую.
         Чтобы получить нужные значения, следует создать объект Calendar и использовать
         Date для определения его конфигурации. После этого вы сможете получить нужную
         информацию из Calendar.
         В методе onCreateDialog(…) получите объект Date из аргументов и используйте его
         с Calendar для инициализации DatePicker.
         */
        mDate = (Date)getArguments().getSerializable(EXTRA_DATE);
// создание объекта Calendar для получения года, месяца и дня
        Calendar calendar = Calendar.getInstance();
                  calendar.setTime(mDate);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        View v = getActivity().getLayoutInflater()
                .inflate(R.layout.dialog_date, null);

        DatePicker datePicker = (DatePicker)v.findViewById(R.id.dialog_date_datePicker);
        datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
            public void onDateChanged(DatePicker view, int year, int month, int day) {
// Преобразование года, месяца и дня в объект Date
                mDate = new GregorianCalendar(year, month, day).getTime();
// обновление аргумента для сохранения
// выбранного значения при повороте
                getArguments().putSerializable(EXTRA_DATE, mDate);
            }
        });

        return new DatePickerDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.date_picker_title)
                .setPositiveButton(
                        android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                               sendResult(Activity.RESULT_OK);
                            }
                        })
                .create();
    }
}
