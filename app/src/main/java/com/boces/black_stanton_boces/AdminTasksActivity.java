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
import android.view.View;
import android.widget.SearchView;

import com.boces.black_stanton_boces.persistence.PersistenceInteractor;
import com.boces.black_stanton_boces.task.TaskAdapter;
import com.boces.black_stanton_boces.task.TaskAdapterOnclick;

/**
 * Shows Existing Tasks and Allows User Choose to Edit or Create and New One
 */
public class AdminTasksActivity extends AppCompatActivity {

    private PersistenceInteractor persistence;
    private RecyclerView taskList;

    /**
     * Retrieve Existing Information
     * @param savedInstanceState
     * Bundle with Extras Set
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_tasks);
        persistence = new PersistenceInteractor(this);
        final TaskAdapter adapter = new TaskAdapter(persistence.getAllTasks(), new TaskAdapterOnclick() {
            @Override
            public void onClick(int taskId) {
                Intent editTask = new Intent(getApplicationContext(), AdminEditTaskActivity.class);
                editTask.putExtra(AdminEditTaskActivity.BUNDLE_KEY.TASK_ID.name(), taskId);
                startActivity(editTask);
            }
        });

        taskList = findViewById(R.id.taskList);
        taskList.setAdapter(adapter);
        taskList.setLayoutManager(new LinearLayoutManager(this));

        SearchView searchView = findViewById(R.id.searchAdminTask);
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
     * Re-retrieves Information In Case of Updates
     */
    @Override
    public void onResume() {
        super.onResume();
        ((TaskAdapter) taskList.getAdapter()).setTasks(persistence.getAllTasks());
        taskList.getAdapter().notifyDataSetChanged();
    }

    /**
     * Starts Add Activity
     * @param v
     * View Holder
     */
    public void onClickAdminAddTasks(View v) {
        startActivity(new Intent(this, AdminAddTaskActivity.class));
    }
}
