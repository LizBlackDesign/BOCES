package com.boces.black_stanton_boces.util;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import com.boces.black_stanton_boces.R;

import java.util.Calendar;
import java.util.Date;

public class DatePickerDialogueFactory {
    public interface DatePickerDialogueListener {
        void onPositive(Date date, Dialog dialog);
        void onNegative(Dialog dialog);
    }

    public static Dialog make(Context context, final DatePickerDialogueListener listener) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialogue_date_picker);
        final DatePicker datePicker = dialog.findViewById(R.id.datePicker);


        dialog.setTitle("Pick A Date");

        Button okay = dialog.findViewById(R.id.btnDialogueOkay);
        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, datePicker.getYear());
                cal.set(Calendar.MONTH, datePicker.getMonth());
                cal.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());

                listener.onPositive(cal.getTime(), dialog);
                dialog.dismiss();
            }
        });

        Button cancel = dialog.findViewById(R.id.btnDialogueCancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onNegative(dialog);
                dialog.dismiss();
            }
        });

        return dialog;
    }
}
