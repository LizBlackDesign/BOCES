/*
 * BOCES
 *
 * Authors: Evan Black, Elizabeth Stanton
 */
package com.boces.black_stanton_boces.activity.punch;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.boces.black_stanton_boces.R;
import com.boces.black_stanton_boces.persistence.PersistenceInteractor;
import com.boces.black_stanton_boces.persistence.model.Student;
import com.boces.black_stanton_boces.persistence.model.Task;
import com.boces.black_stanton_boces.taskpunch.PunchAdapter;
import com.boces.black_stanton_boces.taskpunch.PunchAdapterOnclick;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Shows Existing Punches and Allows User Choose to Edit or Create and New One
 */
public class AdminStudentPunchesActivity extends AppCompatActivity {

    private PersistenceInteractor persistence;
    private int studentId;
    private RecyclerView punchesList;

    /**
     * Recognised Values That May Be Passed Through Bundles
     */
    public enum BUNDLE_KEY {
        STUDENT_ID
    }

    /**
     * Retrieve Existing Information for Chosen Student
     * @param savedInstanceState
     * Bundle with Extras Set
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_student_punches);
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
            throw new IllegalStateException("Student Not Found");


        PunchAdapterOnclick onclick = new PunchAdapterOnclick() {
            @Override
            public void onclick(int punchId) {
                Intent editPunch = new Intent(getApplicationContext(),AdminPunchEditActivity.class);
                editPunch.putExtra(AdminPunchEditActivity.BUNDLE_KEY.PUNCH_ID.name(), punchId);
                startActivity(editPunch);
            }
        };
        PunchAdapter adapter = new PunchAdapter(persistence.getTaskPunchesForStudent(studentId),
                Collections.singletonMap(studentId, student),
                getTaskCache() ,onclick);

        punchesList = findViewById(R.id.punchList);
        punchesList.setAdapter(adapter);
        punchesList.setLayoutManager(new LinearLayoutManager(this));
    }

    /**
     * Re-retrieves Information In Case of Updates
     */
    @Override
    protected void onResume() {
        super.onResume();
        PunchAdapter adapter = (PunchAdapter) punchesList.getAdapter();
        adapter.setPunches(persistence.getTaskPunchesForStudent(studentId));
        adapter.setStudents(Collections.singletonMap(studentId, persistence.getStudent(studentId)));
        adapter.setTasks(getTaskCache());
        adapter.notifyDataSetChanged();
    }

    /**
     * Starts Add Activity
     * @param v
     * View Holder
     */
    public void onCreatePunch(View v) {
        Intent createPunch = new Intent(this, AdminPunchAddActivity.class);
        createPunch.putExtra(AdminPunchAddActivity.BUNDLE_KEY.STUDENT_ID.name(), studentId);
        startActivity(createPunch);
    }

    /**
     * Makes Adapter Aware of Tasks
     * @return
     * Tasks
     */
    private HashMap<Integer, Task> getTaskCache() {
        @SuppressLint("UseSparseArrays")
        HashMap<Integer, Task> tasks = new HashMap<>();
        ArrayList<Task> dbTasks = persistence.getAllTasks();
        for (Task task : dbTasks) {
            tasks.put(task.getId(), task);
        }
        return tasks;
    }
}
