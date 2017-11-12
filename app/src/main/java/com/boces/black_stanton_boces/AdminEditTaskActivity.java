package com.boces.black_stanton_boces;
//Todo: Evan please make edit view
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.boces.black_stanton_boces.persistence.PersistenceInteractor;
import com.boces.black_stanton_boces.persistence.model.Task;

public class AdminEditTaskActivity extends AppCompatActivity {

    private int id;
    private EditText name;

    /**
     * Recognised Values That May Be Passed Through Bundles
     */
    public enum BUNDLE_KEY {
        TASK_ID
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_task);
        Bundle extras = getIntent().getExtras();

        // Painfully Validate That We Got Something
        if (extras == null)
            throw new IllegalStateException("No Data Passed To Edit");
        id = extras.getInt(BUNDLE_KEY.TASK_ID.name());
        if (id == 0)
            throw new IllegalStateException("Task ID Not Passed To Edit");

        PersistenceInteractor persistence = new PersistenceInteractor(this);
        Task task = persistence.getTask(id);
        if (task == null)
            throw new IllegalStateException("Task With ID " + id + " Not Found");

        // Get Input References
        name = (EditText) findViewById(R.id.inputTask);

        // Set Current Values
        name.setText(task.getName());
    }

    public void onSave(View view) {
        PersistenceInteractor persistence = new PersistenceInteractor(this);
        Task task = persistence.getTask(id);
        if (task == null) {
            Toast.makeText(this, "Error Task With ID " + id + " Not Found", Toast.LENGTH_LONG).show();
            return;
        }

        task.setName(name.getText().toString());
        persistence.update(task);
        finish();
    }

    public void onDelete(View view) {
        PersistenceInteractor persistence = new PersistenceInteractor(this);
        persistence.deleteTask(id);
        finish();
    }

    // Opens Task Manager(back one screen)
    public void onClickAdminTaskEditBack(View v) {
        finish();
    }
}
