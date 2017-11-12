package com.boces.black_stanton_boces;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.boces.black_stanton_boces.persistence.PersistenceInteractor;
import com.boces.black_stanton_boces.persistence.model.Student;
import com.boces.black_stanton_boces.persistence.model.Task;
import com.boces.black_stanton_boces.persistence.model.TaskPunch;

import java.util.Date;

public class StudentCurrentTaskViewActivity extends AppCompatActivity {

    private int taskId;
    private int studentId;
    private int punchId;
    private TextView lblCurrentTaskStudentName;
    private TextView lblCurrentTaskTaskName;

    /**
     * Recognised Values That May Be Passed Through Bundles
     */
    public enum BUNDLE_KEY {
        TASK_ID, STUDENT_ID, PUNCH_ID
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        lblCurrentTaskStudentName = (TextView) findViewById(R.id.lblCurrentTaskStudentName);
        String studentName = student.getFirstName() + " " + student.getLastName();
        lblCurrentTaskStudentName.setText(studentName);

        lblCurrentTaskTaskName = (TextView) findViewById(R.id.lblCurrentTaskTaskName);
        lblCurrentTaskTaskName.setText(task.getName());
    }

    public void onTaskComplete(View view) {
        PersistenceInteractor persistence = new PersistenceInteractor(this);
        TaskPunch taskPunch = persistence.getTaskPunch(punchId);
        if (taskPunch != null) {
            taskPunch.setTimeEnd(new Date());
            persistence.update(taskPunch);
        } else {
            Toast.makeText(this, "Task Not Found", Toast.LENGTH_LONG).show();
        }
    }

    //Opens Task Selection Screen (back one screen)
    public void onClickAdminCurrentTaskBack(View v) {
        startActivity(new Intent(this, StudentLoginSelectTaskActivity.class));
    }

}
