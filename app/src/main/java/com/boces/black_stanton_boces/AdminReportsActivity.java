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
import com.boces.black_stanton_boces.util.ProgressBarDialogueFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Lets User Choose Settings for Exporting Report
 */
public class AdminReportsActivity extends AppCompatActivity {

    /**
     * Button To Start Report Generation
     */
    private Button btnSaveTime;

    /**
     * Text View For The Start Date Selected From Dialogue
     */
    private EditText txtStartDate;

    /**
     * Text View For The End Date Selected From Dialogue
     */
    private EditText txtEndDate;

    /**
     * Save Dates Selected By Dialogues
     */
    private DateCache dateCache = new DateCache();

    /**
     * Name of The File To Create
     */
    private EditText txtFileName;

    /**
     * Formats Dates From Dialogues
     */
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

    /**
     * Thread Report Is Generated On
     */
    private Thread reportGenerator;

    /**
     * Dialogue Shown While A Report Is Generating
     */
    private Dialog progressDialogue;

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
        txtFileName = findViewById(R.id.txtFileName);

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
     * Current View. Unused
     */
    public void onSave(View v) {

        // Clear Old Errors
        txtStartDate.setError(null);
        txtEndDate.setError(null);

        boolean hasError = false;

        if (txtStartDate.getText().toString().trim().isEmpty()) {
            hasError = true;
            txtStartDate.setError("Start Date Is Required");
        }

        if (txtEndDate.getText().toString().trim().isEmpty()) {
            hasError = true;
            txtEndDate.setError("End Date Is Required");
        }

        // Make Sure We Have A Filename Before Continuing
        if (txtFileName.getText().toString().trim().isEmpty()) {
            hasError = true;
            txtFileName.setError("Filename Is Required");
        }

        if (dateCache.start != null && dateCache.end != null && !txtStartDate.getText().toString().trim().isEmpty()) {
            if (dateCache.end.before(dateCache.start)) {
                hasError = true;
                txtStartDate.setError("Start Date Must Be Before End Date");
            }
        }

        // Stop Bad Input
        if (hasError)
            return;

        btnSaveTime.setEnabled(false);
        btnSaveTime.setVisibility(View.GONE);
        PersistenceInteractor persistence = new PersistenceInteractor(this);
        ArrayList<StudentPunches> punches = persistence.getStudentPunches(dateCache.start, dateCache.end);
        ArrayList<Task> tasks = persistence.getAllTasks();
        String filename = txtFileName.getText().toString().trim();
        final Context context = this;
        progressDialogue = ProgressBarDialogueFactory.make(context);
        progressDialogue.show();

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
                            btnSaveTime.setVisibility(View.VISIBLE);

                            // Make Sure Dialogue Isn't Null
                            if (progressDialogue != null)
                                progressDialogue.dismiss();
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
