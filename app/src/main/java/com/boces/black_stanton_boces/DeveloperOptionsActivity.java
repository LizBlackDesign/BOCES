package com.boces.black_stanton_boces;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.boces.black_stanton_boces.persistence.PersistenceInteractor;
import com.boces.black_stanton_boces.persistence.model.Student;
import com.boces.black_stanton_boces.persistence.model.Task;
import com.boces.black_stanton_boces.persistence.model.TaskPunch;
import com.boces.black_stanton_boces.report.ReportGenerator;
import com.boces.black_stanton_boces.report.StudentPunches;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DeveloperOptionsActivity extends AppCompatActivity {
    private static final int EXTERNAL_STORAGE_REQUEST = 0;

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

    public void onGenerateReport(View v) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_REQUEST);
            return;
        }

        PersistenceInteractor persistence = new PersistenceInteractor(this);

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DATE, -100);
        Date start = cal.getTime();
        Date end = new Date();

        ArrayList<StudentPunches> studentPunches = persistence.getStudentPunches(start, end);
        ReportGenerator.exportTaskReport(studentPunches, "", persistence.getAllTasks());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case EXTERNAL_STORAGE_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    onGenerateReport(null);
                }
                break;
        }
    }
}
