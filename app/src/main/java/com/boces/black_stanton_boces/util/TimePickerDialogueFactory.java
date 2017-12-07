package com.boces.black_stanton_boces.util;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

import com.boces.black_stanton_boces.R;

import java.util.Calendar;
import java.util.Date;

public class TimePickerDialogueFactory {
    public interface TimePickerDialogueListener {
        void onPositive(Date date, Dialog dialog);
        void onNegative(Dialog dialog);
    }

    public static Dialog make(Context context, final TimePickerDialogueListener listener) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialogue_time_picker);
        final TimePicker timePicker = dialog.findViewById(R.id.timePicker);

        dialog.setTitle("Pick A Time");

        Button okay = dialog.findViewById(R.id.btnDialogueOkay);
        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.HOUR, timePicker.getCurrentHour());
                cal.set(Calendar.MINUTE, timePicker.getCurrentMinute());

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
