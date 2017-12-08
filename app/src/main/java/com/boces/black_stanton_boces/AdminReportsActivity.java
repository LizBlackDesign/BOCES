package com.boces.black_stanton_boces;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.boces.black_stanton_boces.persistence.PersistenceInteractor;
import com.boces.black_stanton_boces.persistence.model.Task;
import com.boces.black_stanton_boces.report.ReportGenerator;
import com.boces.black_stanton_boces.report.StudentPunches;
import com.boces.black_stanton_boces.util.DatePickerDialogueFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AdminReportsActivity extends AppCompatActivity {

    private EditText txtStartDate;
    private EditText txtEndDate;
    private DateCache dateCache = new DateCache();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_reports);

        txtStartDate = findViewById(R.id.txtStart);
        txtEndDate = findViewById(R.id.txtEnd);

        final Context context = this;

        txtStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialogueFactory.make(context, new DatePickerDialogueFactory.DatePickerDialogueListener() {
                    @Override
                    public void onPositive(Date date, Dialog dialog) {
                        txtStartDate.setText(dateFormat.format(date));
                        dateCache.start = date;
                    }

                    @Override
                    public void onNegative(Dialog dialog) {
                        // ignored
                    }
                }).show();
            }
        });

        txtEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialogueFactory.make(context, new DatePickerDialogueFactory.DatePickerDialogueListener() {
                    @Override
                    public void onPositive(Date date, Dialog dialog) {
                        txtEndDate.setText(dateFormat.format(date));
                        dateCache.end = date;
                    }

                    @Override
                    public void onNegative(Dialog dialog) {

                    }
                }).show();
            }
        });
    }


    public void onSave(View v) {
        PersistenceInteractor persistence = new PersistenceInteractor(this);
        ArrayList<StudentPunches> punches = persistence.getStudentPunches(dateCache.start, dateCache.end);
        ArrayList<Task> tasks = persistence.getAllTasks();
        ReportGenerator.exportTaskReport(punches, "file", tasks);
    }

    private class DateCache {
        public Date start = new Date();
        public Date end = new Date();
    }

    private class ReportRunner implements Runnable {
        private Date startDate;
        private Date endDate;
        private String filename;
        private List<StudentPunches> punches;
        private ArrayList<Task> tasks;

        @Override
        public void run() {

        }
    }
}
