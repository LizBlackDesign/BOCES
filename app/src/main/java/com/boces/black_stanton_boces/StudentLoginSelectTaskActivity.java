/*
 * BOCES
 *
 * Authors: Evan Black, Elizabeth Stanton
 */
package com.boces.black_stanton_boces;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.SearchView;

import com.boces.black_stanton_boces.persistence.PersistenceInteractor;
import com.boces.black_stanton_boces.persistence.model.Student;
import com.boces.black_stanton_boces.task.TaskAdapter;
import com.boces.black_stanton_boces.task.TaskAdapterOnclick;
/**
 * Allows Student to Choose A Task
 */
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

    /**
     * Brings in Extras, Validates
     * @throws IllegalStateException
     * When Extras Fail to Validate
     * @param savedInstanceState
     * Bundle with Extras Set
     */
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
                Intent startTask = new Intent(getApplicationContext(), StudentTaskStart.class);
                startTask.putExtra(StudentTaskStart.BUNDLE_KEY.TASK_ID.name(), taskId);
                startTask.putExtra(StudentTaskStart.BUNDLE_KEY.STUDENT_ID.name(), studentId);
                startActivity(startTask);
            }
        });

        taskList = findViewById(R.id.recyclerSelectTask);
        taskList.setAdapter(adapter);
        taskList.setLayoutManager(new LinearLayoutManager(this));

        SearchView searchView = findViewById(R.id.login_select_task_search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        });
    }
    /**
     * Updates Changed Information
     */
    @Override
    public void onResume() {
        super.onResume();
        ((TaskAdapter) taskList.getAdapter()).setTasks(persistence.getAllTasks());
        taskList.getAdapter().notifyDataSetChanged();
    }
}
