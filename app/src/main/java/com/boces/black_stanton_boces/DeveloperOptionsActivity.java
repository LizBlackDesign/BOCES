package com.boces.black_stanton_boces;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.boces.black_stanton_boces.persistence.PersistenceInteractor;
import com.boces.black_stanton_boces.persistence.model.Student;
import com.boces.black_stanton_boces.persistence.model.Task;
import com.boces.black_stanton_boces.persistence.model.TaskPunch;

import java.util.ArrayList;
import java.util.Date;

public class DeveloperOptionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer_options);
    }

    public void onClearDatabase(View v) {
        PersistenceInteractor persistence = new PersistenceInteractor(this);
        persistence.dropAndRecreate();
        Toast.makeText(this, "Database Recreated", Toast.LENGTH_LONG).show();
    }

    public void onCreatePunch(View v) {
        PersistenceInteractor persistence = new PersistenceInteractor(this);
        ArrayList<Student> students = persistence.getAllStudents();
        if (students.size() < 1)
            return;

        ArrayList<Task> tasks = persistence.getAllTasks();
        if (tasks.size() < 1)
            return;

        TaskPunch taskPunch = new TaskPunch();
        taskPunch.setStudentId(students.get(0).getId());
        taskPunch.setTaskId(tasks.get(0).getId());
        taskPunch.setTimeStart(new Date());

        int id = persistence.addTaskPunch(taskPunch);
        TaskPunch inserted = persistence.getTaskPunch(id);
    }
}
