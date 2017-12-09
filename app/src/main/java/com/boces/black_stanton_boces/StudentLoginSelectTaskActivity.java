package com.boces.black_stanton_boces;
//TODO: search bar, save information for current task page

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.boces.black_stanton_boces.persistence.PersistenceInteractor;
import com.boces.black_stanton_boces.persistence.model.Student;
import com.boces.black_stanton_boces.persistence.model.TaskPunch;
import com.boces.black_stanton_boces.task.TaskAdapter;
import com.boces.black_stanton_boces.task.TaskAdapterOnclick;

import java.util.Date;

public class StudentLoginSelectTaskActivity extends AppCompatActivity {

    private int studentId;
    private PersistenceInteractor persistence;
    private RecyclerView taskList;

    /**
     * Recognised Values That May Be Passed Through Bundles
     */
    public enum BUNDLE_KEY {
        STUDENT_ID
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login_select_task);
        Bundle extras = getIntent().getExtras();

        // Painfully Validate That We Got Something
        if (extras == null)
            throw new IllegalStateException("No Data Passed To Edit");
        studentId = extras.getInt(BUNDLE_KEY.STUDENT_ID.name());

        persistence = new PersistenceInteractor(this);
        Student currentStudent = persistence.getStudent(studentId);
        if (currentStudent == null)
            throw new IllegalStateException("Student With ID " + studentId + " Not Found");

        final TaskAdapter adapter = new TaskAdapter(persistence.getAllTasks(), new TaskAdapterOnclick() {
            @Override
            public void onClick(int taskId) {
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

        taskList = (RecyclerView) findViewById(R.id.recyclerSelectTask);
        taskList.setAdapter(adapter);
        taskList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onResume() {
        super.onResume();
        ((TaskAdapter) taskList.getAdapter()).setTasks(persistence.getAllTasks());
        taskList.getAdapter().notifyDataSetChanged();
    }
}
