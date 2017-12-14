/*
 * BOCES
 *
 * Authors: Evan Black, Elizabeth Stanton
 */
package com.boces.black_stanton_boces.activity.punch;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.boces.black_stanton_boces.R;
import com.boces.black_stanton_boces.persistence.PersistenceInteractor;
import com.boces.black_stanton_boces.persistence.model.Student;
import com.boces.black_stanton_boces.persistence.model.Task;
import com.boces.black_stanton_boces.persistence.model.TaskPunch;
import com.boces.black_stanton_boces.student.StudentSpinnerInteractor;
import com.boces.black_stanton_boces.task.TaskSpinnerInteractor;
import com.boces.black_stanton_boces.util.DatePickerDialogueFactory;
import com.boces.black_stanton_boces.util.TimePickerDialogueFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Pulls Existing Information And Save Update Information From User
 */
public class AdminPunchEditActivity extends AppCompatActivity {

    private int punchId;
    private PersistenceInteractor persistence;

    private Spinner spinnerStudent;
    private StudentSpinnerInteractor studentSpinnerInteractor;

    private Spinner spinnerTask;
    private TaskSpinnerInteractor taskSpinnerInteractor;

    private EditText txtDate;
    private EditText txtStart;
    private EditText txtEnd;
    private TextView lblDurationValue;

    private final SimpleDateFormat dateParser = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
    private final SimpleDateFormat timeParser = new SimpleDateFormat("h:mm a", Locale.US);
    private final SimpleDateFormat fullParser = new SimpleDateFormat("MM/dd/yyyy h:mm a", Locale.US);

    /**
     * Recognised Values That May Be Passed Through Bundles
     */
    public enum BUNDLE_KEY {
        PUNCH_ID
    }

    /**
     * Brings in Extras, Validates, Sets Fields
     * @throws IllegalStateException
     * When Extras Fail to Validate
     * @param savedInstanceState
     * Bundle with Extras Set
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_punch_edit);
        Bundle extras = getIntent().getExtras();
        persistence = new PersistenceInteractor(this);

        // Painfully Validate That We Got Something
        if (extras == null)
            throw new IllegalStateException("No Data Passed");
        punchId = extras.getInt(BUNDLE_KEY.PUNCH_ID.name());
        if (punchId == 0)
            throw new IllegalStateException("Punch ID Not Passed");
        final TaskPunch punch = persistence.getTaskPunch(punchId);
        if (punch == null)
            throw new IllegalStateException("Punch Not Found");

        spinnerStudent = findViewById(R.id.spinnerStudent);
        studentSpinnerInteractor = new StudentSpinnerInteractor(spinnerStudent, this, persistence.getAllStudents());
        studentSpinnerInteractor.setSelectedItem(punch.getStudentId());

        spinnerTask = findViewById(R.id.spinnerTask);
        taskSpinnerInteractor = new TaskSpinnerInteractor(spinnerTask, this, persistence.getAllTasks());
        taskSpinnerInteractor.setSelectedItem(punch.getTaskId());

        txtDate = findViewById(R.id.txtDate);
        txtStart = findViewById(R.id.txtStart);
        txtEnd = findViewById(R.id.txtEnd);
        lblDurationValue = findViewById(R.id.lblDurationValue);

        final Context context = this;

        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialogueFactory.make(context, new DatePickerDialogueFactory.DatePickerDialogueListener() {
                    @Override
                    public void onPositive(Date date, Dialog dialog) {
                        txtDate.setText(dateParser.format(date));
                    }

                    @Override
                    public void onNegative(Dialog dialog) {
                        // Ignored
                    }
                }).show();
            }
        });

        txtStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialogueFactory.make(context, new TimePickerDialogueFactory.TimePickerDialogueListener() {
                    @Override
                    public void onPositive(Date date, Dialog dialog) {
                        txtStart.setText(timeParser.format(date));
                        punch.setTimeStart(date);
                        if (!txtEnd.getText().toString().isEmpty()) {
                            lblDurationValue.setText(calculateDifference(punch.getTimeStart(), punch.getTimeEnd()));
                        }
                    }

                    @Override
                    public void onNegative(Dialog dialog) {
                        //ignored
                    }
                }, punch.getTimeStart()).show();
            }
        });

        txtEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialogueFactory.make(context, new TimePickerDialogueFactory.TimePickerDialogueListener() {
                    @Override
                    public void onPositive(Date date, Dialog dialog) {
                        txtEnd.setText(timeParser.format(date));
                        punch.setTimeEnd(date);
                        if (!txtStart.getText().toString().isEmpty()) {
                            lblDurationValue.setText(calculateDifference(punch.getTimeStart(), punch.getTimeEnd()));
                        }
                    }

                    @Override
                    public void onNegative(Dialog dialog) {
                        // Ignored
                    }
                }, punch.getTimeEnd()).show();
            }
        });


        txtDate.setText(dateParser.format(punch.getTimeStart()));
        txtStart.setText(timeParser.format(punch.getTimeStart()));
        if (punch.getTimeEnd() != null) {
            txtEnd.setText(timeParser.format(punch.getTimeEnd()));
            lblDurationValue.setText(calculateDifference(punch.getTimeStart(), punch.getTimeEnd()));
        }
    }

    /**
     * Checks If Required Field Is Empty Before For Saving
     * @param v
     * Current View
     */
    public void onSave(View v) {
        TaskPunch punch = persistence.getTaskPunch(punchId);
        if (punch == null) {
            Toast.makeText(this, "Error: Task Punch Not Found", Toast.LENGTH_LONG).show();
            return;
        }

        boolean hasError = false;

        if (txtDate.getText().toString().trim().isEmpty()) {
            hasError = true;
            txtDate.setError("Date Is Required");
        } else {
            try {
                dateParser.parse(txtDate.getText().toString());
            } catch (ParseException e) {
                hasError = true;
                txtDate.setError("Date Is In Invalid Format");
            }
        }

        if (txtStart.getText().toString().trim().isEmpty()) {
            hasError = true;
            txtStart.setError("Start Time Is Required");
        } else {
            try {
                timeParser.parse(txtStart.getText().toString().trim());
            } catch (ParseException e) {
                hasError = true;
                txtStart.setError("Time Is In Invalid Format");
            }
        }

        if (txtEnd.getText().toString().trim().isEmpty()) {
            hasError = true;
            txtEnd.setError("End Time Is Required");
        } else {
            try {
                timeParser.parse(txtEnd.getText().toString().trim());
            } catch (ParseException e) {
                hasError = true;
                txtEnd.setError("Time Is In Invalid Format");
            }
        }

        // Block Empty Fields Before Parsing, Since They Will Error Anyway
        if (hasError)
            return;

        String start = txtDate.getText().toString().trim() + " " + txtStart.getText().toString().trim();
        String end = txtDate.getText().toString().trim() + " " + txtEnd.getText().toString().trim();

        try {
            punch.setTimeStart(fullParser.parse(start));
            punch.setTimeEnd(fullParser.parse(end));
        } catch (ParseException e) {
            // If This Fails, Crash
            throw new RuntimeException("Time Fails To Parse After Validation", e);
        }

        Student selectedStudent = studentSpinnerInteractor.getSelectedItem();
        if (selectedStudent == null) {
            hasError = true;
            new AlertDialog.Builder(this)
                    .setTitle("A Student Is Required")
                    .setMessage("Error, A Student Is Required")
                    .setCancelable(true)
                    .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .show();
        } else
            punch.setStudentId(selectedStudent.getId());

        Task selectedTask = taskSpinnerInteractor.getSelectedItem();
        if (selectedTask == null) {
            hasError = true;
            new AlertDialog.Builder(this)
                    .setTitle("A Task Is Required")
                    .setMessage("Error, A Task Is Required")
                    .setCancelable(true)
                    .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .show();
        } else
            punch.setTaskId(selectedTask.getId());

        // Block Invalid Data
        if (hasError)
            return;

        persistence.update(punch);
        finish();
    }

    /**
     * Deletes The Associated Punch
     *
     * @param v
     * Current View
     * Unused, May Be null
     */
    public void onDelete(View v) {
        persistence.deleteTaskPunch(punchId);
        finish();
    }

    /**
     * Calculates Duration
     * @param start
     * Start Time
     * @param end
     * Start End
     * @return
     * Duration
     */
    private String calculateDifference(Date start, Date end) {
        Date startNoDate = removeDateSeconds(start);
        Date endNoDate = removeDateSeconds(end);
        final long diff = endNoDate.getTime() - startNoDate.getTime();
        final long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);
        return Long.toString(minutes) + "Min";
    }

    /**
     * Removes Date And Seconds From Time
     * @param date
     * Date of Punch
     * @return
     * Time Without A Date/Seconds
     */
    private Date removeDateSeconds(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.YEAR, 0);
        cal.set(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_MONTH, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
}
