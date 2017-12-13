/*
 * BOCES
 *
 * Authors: Evan Black, Elizabeth Stanton
 */
package com.boces.black_stanton_boces;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.boces.black_stanton_boces.persistence.PersistenceInteractor;
import com.boces.black_stanton_boces.persistence.model.Student;
import com.boces.black_stanton_boces.persistence.model.Task;
import com.boces.black_stanton_boces.persistence.model.TaskPunch;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Displays Student, Task, And Duration
 */
public class StudentCurrentTaskViewActivity extends AppCompatActivity {

    private int taskId;
    private int studentId;
    private int punchId;
    private TextView lblCurrentTaskStudentName;
    private TextView lblCurrentTaskTaskName;
    private TextView lblTaskTime;
    private ImageView imageCurrentTask;
    private Timer timer;
    private TimerTask timerTask;

    /**
     * Recognised Values That May Be Passed Through Bundles
     */
    public enum BUNDLE_KEY {
        TASK_ID, STUDENT_ID, PUNCH_ID
    }

    /**
     * Brings in Extras, Validates
     * @throws IllegalStateException
     * When Extras Fail to Validate
     * @param savedInstanceState
     * Bundle with Extras Set
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_current_task_view);

        Bundle extras = getIntent().getExtras();

        // Painfully Validate That We Got Something
        if (extras == null)
            throw new IllegalArgumentException("No Data Passed");
        taskId = extras.getInt(BUNDLE_KEY.TASK_ID.name());
        if (taskId == 0)
            throw new IllegalArgumentException("Task ID Not Passed");
        studentId = extras.getInt(BUNDLE_KEY.STUDENT_ID.name());
        if (studentId == 0)
            throw new IllegalArgumentException("Student ID Not Passed");
        punchId = extras.getInt(BUNDLE_KEY.PUNCH_ID.name());
        if (punchId == 0)
            throw new IllegalArgumentException("Punch ID Not Passed");

        PersistenceInteractor persistence = new PersistenceInteractor(this);

        Task task = persistence.getTask(taskId);
        if (task == null)
            throw new IllegalStateException("Task With ID " + taskId + " Not Found");

        Student student = persistence.getStudent(studentId);
        if (student == null)
            throw new IllegalStateException("Student With ID " + studentId + " Not Found");

        final TaskPunch taskPunch = persistence.getTaskPunch(punchId);
        if (taskPunch == null)
            throw new IllegalStateException("Punch With ID "+ punchId + " Not Found");

        lblCurrentTaskStudentName = (TextView) findViewById(R.id.lblCurrentTaskStudentName);
        String studentName = student.getFirstName() + " " + student.getLastName();
        lblCurrentTaskStudentName.setText(studentName);

        lblCurrentTaskTaskName = (TextView) findViewById(R.id.lblCurrentTaskTaskName);
        lblCurrentTaskTaskName.setText(task.getName());
        lblTaskTime = (TextView) findViewById(R.id.timeCurrent);
        imageCurrentTask = (ImageView) findViewById(R.id.imageCurrentTask);
        if (task.getImage() != null) {
            imageCurrentTask.setImageBitmap(task.getImage());
        }

        timer = new Timer();
        timerTask = new TimerTask() {
            long seconds = new Date().getTime()/1000L - taskPunch.getTimeStart().getTime()/1000L;
            @Override
            public void run() {
                seconds++;
                StringBuilder stringBuilder = new StringBuilder();
                if (seconds >= 60L * 60L) {
                    stringBuilder.append("Hours: ");
                    stringBuilder.append(seconds / (60L * 60L));
                    stringBuilder.append(' ');
                }
                if (seconds >= 60L) {
                    stringBuilder.append("Minutes: ");
                    stringBuilder.append(seconds / 60L);
                    stringBuilder.append(' ');
                }
                stringBuilder.append("Seconds: ");
                stringBuilder.append(seconds % 60);

                final String timeString = stringBuilder.toString();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        lblTaskTime.setText(timeString);
                    }
                });

            }

        };
        timer.schedule(timerTask, 1000L, 1000L);
    }

    /**
     * Validates, and Updates End Punch
     * @param view
     * Current View
     */
    public void onTaskComplete(View view) {
        timer.cancel();
        PersistenceInteractor persistence = new PersistenceInteractor(this);
        TaskPunch taskPunch = persistence.getTaskPunch(punchId);
        if (taskPunch != null) {
            taskPunch.setTimeEnd(new Date());
            persistence.update(taskPunch);

            startActivity(new Intent(this, LoginTypeActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        } else {
            Toast.makeText(this, "Task Not Found", Toast.LENGTH_LONG).show();
        }
    }
}
