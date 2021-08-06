package com.highcom.ponshu.ui.detailitem;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import java.util.Calendar;

public class DatePick extends DialogFragment {

    private DatePickerDialog.OnDateSetListener mListener;
    public DatePick(DatePickerDialog.OnDateSetListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getContext(), mListener, year, month, day);
    }
}