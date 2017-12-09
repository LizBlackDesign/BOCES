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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

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
 * Collects Punch Information From User And Create New Punch
 */
public class AdminPunchAddActivity extends AppCompatActivity {

    private int studentId;
    private PersistenceInteractor persistence;

    private Spinner spinnerStudent;
    private StudentSpinnerInteractor studentSpinnerInteractor;

    private Spinner spinnerTask;
    private TaskSpinnerInteractor taskSpinnerInteractor;

    private EditText txtDate;
    private EditText txtStart;
    private EditText txtEnd;
    private TextView lblDurationValue;
    private DateCache dateCache = new DateCache();

    /**
     * Recognised Values That May Be Passed Through Bundles
     */
    public enum BUNDLE_KEY {
        STUDENT_ID
    }


    /**
     * Gathers Input References
     * @param savedInstanceState
     * Bundle with Extras Set - Unused
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_punch_add);
        Bundle extras = getIntent().getExtras();
        persistence = new PersistenceInteractor(this);

        // Painfully Validate That We Got Something
        if (extras == null)
            throw new IllegalStateException("No Data Passed");
        studentId = extras.getInt(BUNDLE_KEY.STUDENT_ID.name());
        if (studentId == 0)
            throw new IllegalStateException("Student ID Not Passed");
        Student student = persistence.getStudent(studentId);
        if (student == null)
            throw new IllegalStateException("Student With ID: " + studentId+ " Not Found");

        spinnerStudent = findViewById(R.id.spinnerStudent);
        studentSpinnerInteractor = new StudentSpinnerInteractor(spinnerStudent, this, persistence.getAllStudents());

        spinnerTask = findViewById(R.id.spinnerTask);
        taskSpinnerInteractor = new TaskSpinnerInteractor(spinnerTask, this, persistence.getAllTasks());

        txtDate = findViewById(R.id.txtDate);
        txtStart = findViewById(R.id.txtStart);
        txtEnd = findViewById(R.id.txtEnd);
        lblDurationValue = findViewById(R.id.lblDurationValue);


        final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        final SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a", Locale.US);
        final Context context = this;

        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialogueFactory.make(context, new DatePickerDialogueFactory.DatePickerDialogueListener() {
                    @Override
                    public void onPositive(Date date, Dialog dialog) {
                        txtDate.setText(dateFormat.format(date));
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
                        txtStart.setText(timeFormat.format(date));
                        dateCache.startDate = date;
                        if (!txtEnd.getText().toString().isEmpty()) {
                            lblDurationValue.setText(calculateDifference(dateCache.startDate, dateCache.endDate));
                        }
                    }

                    @Override
                    public void onNegative(Dialog dialog) {
                        //ignored
                    }
                }, dateCache.startDate).show();
            }
        });

        txtEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialogueFactory.make(context, new TimePickerDialogueFactory.TimePickerDialogueListener() {
                    @Override
                    public void onPositive(Date date, Dialog dialog) {
                        txtEnd.setText(timeFormat.format(date));
                        dateCache.endDate = date;
                        if (!txtStart.getText().toString().isEmpty()) {
                            lblDurationValue.setText(calculateDifference(dateCache.startDate, dateCache.endDate));
                        }
                    }

                    @Override
                    public void onNegative(Dialog dialog) {
                        // Ignored
                    }
                }, dateCache.endDate).show();
            }
        });

    }


    /**
     * Brings in Extras, Validates, Sets Fields
     * @throws IllegalStateException
     * When Extras Fail to Validate
     * @param v
     * Current View
     */
    public void onSave(View v) {
        TaskPunch punch = new TaskPunch();

        final SimpleDateFormat parser = new SimpleDateFormat("MM/dd/yyyy h:mm a", Locale.US);
        String start = txtDate.getText().toString() + " " + txtStart.getText().toString();
        String end = txtDate.getText().toString() + " " + txtEnd.getText().toString();

        try {
            punch.setTimeStart(parser.parse(start));
            punch.setTimeEnd(parser.parse(end));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Student selectedStudent = studentSpinnerInteractor.getSelectedItem();
        if (selectedStudent != null)
            punch.setStudentId(selectedStudent.getId());

        Task selectedTask = taskSpinnerInteractor.getSelectedItem();
        if (selectedTask != null)
            punch.setTaskId(selectedTask.getId());

        persistence.addTaskPunch(punch);
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
        Date startNoDate = removeDateSecond(start);
        Date endNoDate = removeDateSecond(end);
        final long diff = endNoDate.getTime() - startNoDate.getTime();
        final long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);
        return Long.toString(minutes) + "Min";
    }

    /**
     * Removes Seconds From Time
     * @param date
     * Date of Punch
     * @return
     * Time Without Seconds
     */
    private Date removeDateSecond(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.YEAR, 0);
        cal.set(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_MONTH, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }


    /**
     * Cache Start/End Dates So They May
     * Be Modified By The Dialogue Listener
     */
    private class DateCache {
        public Date startDate = new Date();
        public Date endDate = new Date();
    }
}
