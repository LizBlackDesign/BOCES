package com.boces.black_stanton_boces;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

    /**
     * Recognised Values That May Be Passed Through Bundles
     */
    public enum BUNDLE_KEY {
        PUNCH_ID
    }

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
        TaskPunch punch = persistence.getTaskPunch(punchId);
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
                        try {
                            Date endDate = timeFormat.parse(txtEnd.getText().toString());
                            lblDurationValue.setText(calculateDifference(date, endDate));
                        } catch (ParseException ignored) {
                        }
                    }

                    @Override
                    public void onNegative(Dialog dialog) {
                        //ignored
                    }
                }).show();
            }
        });

        txtEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialogueFactory.make(context, new TimePickerDialogueFactory.TimePickerDialogueListener() {
                    @Override
                    public void onPositive(Date date, Dialog dialog) {
                        txtEnd.setText(timeFormat.format(date));
                        try {
                            Date startDate = timeFormat.parse(txtStart.getText().toString());
                            lblDurationValue.setText(calculateDifference(startDate, date));
                        } catch (ParseException ignored) {
                        }
                    }

                    @Override
                    public void onNegative(Dialog dialog) {
                        // Ignored
                    }
                }).show();
            }
        });


        txtDate.setText(dateFormat.format(punch.getTimeStart()));
        txtStart.setText(timeFormat.format(punch.getTimeStart()));
        if (punch.getTimeEnd() != null) {
            txtEnd.setText(timeFormat.format(punch.getTimeEnd()));
            final long diff = punch.getTimeEnd().getTime() - punch.getTimeStart().getTime();
            final long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);
            final long seconds = TimeUnit.MILLISECONDS.toSeconds(diff) % 60L;
            String difference = Long.toString(minutes) + ":" + seconds;
            lblDurationValue.setText(difference);
        }
    }


    public void onSave(View v) {
        TaskPunch punch = persistence.getTaskPunch(punchId);
        if (punch == null) {
            Toast.makeText(this, "Error: Task Punch Not Found", Toast.LENGTH_LONG).show();
            return;
        }

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

        persistence.update(punch);
        finish();
    }

    private String calculateDifference(Date start, Date end) {
        Date startNoDate = removeDateSeconds(start);
        Date endNoDate = removeDateSeconds(end);
        final long diff = endNoDate.getTime() - startNoDate.getTime();
        final long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);
        return Long.toString(minutes) + "Min";
    }

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
