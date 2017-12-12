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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.boces.black_stanton_boces.persistence.PersistenceInteractor;
import com.boces.black_stanton_boces.persistence.model.Student;
import com.boces.black_stanton_boces.persistence.model.Task;
import com.boces.black_stanton_boces.persistence.model.TaskPunch;

import java.util.Date;

/**
 * Allows Student to Review Information Before Starting Punch
 */
public class StudentTaskStart extends AppCompatActivity {

    private int taskId;
    private int studentId;
    private PersistenceInteractor persistence;

    /**
     * Recognised Values That May Be Passed Through Bundles
     */
    public enum BUNDLE_KEY {
        TASK_ID, STUDENT_ID
    }

    /**
     * Brings in Extras, Validate, Set-up Listeners
     *
     * @throws IllegalArgumentException
     * When Extras Fail to Validate
     *
     * @param savedInstanceState
     * Bundle with Extras Set
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_task_start);

        persistence = new PersistenceInteractor(this);
        Bundle extras = getIntent().getExtras();

        // Painfully Validate That We Got Something
        if (extras == null)
            throw new IllegalArgumentException("No Data Passed");
        taskId = extras.getInt(StudentCurrentTaskViewActivity.BUNDLE_KEY.TASK_ID.name());
        if (taskId == 0)
            throw new IllegalArgumentException("Task ID Not Passed");
        studentId = extras.getInt(StudentCurrentTaskViewActivity.BUNDLE_KEY.STUDENT_ID.name());
        if (studentId == 0)
            throw new IllegalArgumentException("Student ID Not Passed");

        Task task = persistence.getTask(taskId);
        if (task == null)
            throw new IllegalStateException("Task With ID " + taskId + " Not Found");

        Student student = persistence.getStudent(studentId);
        if (student == null)
            throw new IllegalStateException("Student With ID " + studentId + " Not Found");

        // Get Elements
        TextView lblStudentName = findViewById(R.id.lblStudentName);
        TextView lblTaskName = findViewById(R.id.lblTaskName);
        ImageView imgCurrentTask = findViewById(R.id.imgCurrentTask);
        Button btnLoginDifferentStudent = findViewById(R.id.btnLoginDifferentStudent);
        Button btnReady = findViewById(R.id.btnReady);

        String studentName = student.getFirstName() + " " + student.getLastName();
        lblStudentName.setText(studentName);

        lblTaskName.setText(task.getName());

        if (task.getImage() != null)
            imgCurrentTask.setImageBitmap(task.getImage());

        btnLoginDifferentStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 12/11/17 Implement
            }
        });

        btnReady.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TaskPunch taskPunch = new TaskPunch();
                taskPunch.setStudentId(studentId);
                taskPunch.setTaskId(taskId);
                taskPunch.setTimeStart(new Date());
                int punchId = persistence.addTaskPunch(taskPunch);

                Intent startTask = new Intent(getApplicationContext(), StudentCurrentTaskViewActivity.class);
                startTask.putExtra(StudentCurrentTaskViewActivity.BUNDLE_KEY.TASK_ID.name(), taskId);
                startTask.putExtra(StudentCurrentTaskViewActivity.BUNDLE_KEY.STUDENT_ID.name(), studentId);
                startTask.putExtra(StudentCurrentTaskViewActivity.BUNDLE_KEY.PUNCH_ID.name(), punchId);
                startActivity(startTask);
            }
        });
    }
}
