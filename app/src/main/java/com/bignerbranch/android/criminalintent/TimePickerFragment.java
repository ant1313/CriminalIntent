package com.bignerbranch.android.criminalintent;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Антон on 13.03.2017.
 */

public class TimePickerFragment extends DialogFragment {
    public static final String EXTRA_TIME =
            "com.bignerdranch.android.criminalintent.time";
    private Date mDate;
    public static TimePickerFragment newInstance(Date date) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_TIME, date);
        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void sendResult(int resultCode) {
        if (getTargetFragment() == null)
            return;
        Intent i = new Intent();
        i.putExtra(EXTRA_TIME, mDate);
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
        mDate = (Date)getArguments().getSerializable(EXTRA_TIME);
// создание объекта Calendar для получения часов и минут
        Calendar calendar = Calendar.getInstance();
                  calendar.setTime(mDate);
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);

        View v = getActivity().getLayoutInflater()
                .inflate(R.layout.dialog_time, null);

        TimePicker timePicker = (TimePicker)v.findViewById(R.id.dialog_time_datePicker);

        timePicker.setCurrentHour(hour);

        timePicker.setCurrentMinute(minute);

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            public void onTimeChanged(TimePicker view, int hour, int minute) {
// Преобразование года, месяца и дня в объект Date
                mDate = new GregorianCalendar(2017, 03, 13, hour, minute).getTime();
// обновление аргумента для сохранения
// выбранного значения при повороте
                getArguments().putSerializable(EXTRA_TIME, mDate);
            }
        });

        return new TimePickerDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.time_picker_title)
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
