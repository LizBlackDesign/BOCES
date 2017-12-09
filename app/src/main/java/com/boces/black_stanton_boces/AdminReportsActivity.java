/*
 * BOCES
 *
 * Authors: Evan Black, Elizabeth Stanton
 */
package com.boces.black_stanton_boces;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.boces.black_stanton_boces.persistence.PersistenceInteractor;
import com.boces.black_stanton_boces.persistence.model.Task;
import com.boces.black_stanton_boces.report.ReportRunner;
import com.boces.black_stanton_boces.report.StudentPunches;
import com.boces.black_stanton_boces.util.DatePickerDialogueFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Lets User Choose Settings for Exporting Report
 */
public class AdminReportsActivity extends AppCompatActivity {

    private Button btnSaveTime;
    private EditText txtStartDate;
    private EditText txtEndDate;
    private DateCache dateCache = new DateCache();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
    private Thread reportGenerator;

    /**
     * Retrieves Information
     * @param savedInstanceState
     * Bundle with Extras Set
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_reports);

        btnSaveTime = findViewById(R.id.btnSaveTime);
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

    /**
     * Checks If Required Field Is Empty Before For Saving
     * @param v
     * Current View
     */
    public void onSave(View v) {
        btnSaveTime.setEnabled(false);
        PersistenceInteractor persistence = new PersistenceInteractor(this);
        ArrayList<StudentPunches> punches = persistence.getStudentPunches(dateCache.start, dateCache.end);
        ArrayList<Task> tasks = persistence.getAllTasks();
        String filename = "Test";
        final Context context = this;

        if (reportGenerator == null || reportGenerator.getState() == Thread.State.TERMINATED) {
            ReportRunner runner = new ReportRunner(filename, punches, tasks, new ReportRunner.Callback() {
                @Override
                public void onSuccess() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "Report Done", Toast.LENGTH_LONG).show();
                        }
                    });
                }

                @Override
                public void onFail(String message) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "Report Failed", Toast.LENGTH_LONG).show();
                        }
                    });
                }

                @Override
                public void always() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            btnSaveTime.setEnabled(true);
                        }
                    });
                }
            });

            reportGenerator = new Thread(runner);
            reportGenerator.start();
        }

    }

    private class DateCache {
        public Date start = new Date();
        public Date end = new Date();
    }

}
