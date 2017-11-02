package com.boces.black_stanton_boces;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class AdminTasksActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_tasks);

    }

    //Opens Admin Task Back (back one screen)
    private void onClickAdminBackTasks(View v)
    {
        startActivity(new Intent(this, AdminMenuActivity.class));
    }

    //Opens Admin Task Add (back one screen)
    private void onClickAdminAddTasks(View v)
    {
        startActivity(new Intent(this, AdminAddTaskActivity.class));
    }
    //Opens Admin Task Edit (back one screen)
    private void onClickAdminEditTasks(View v)
    {
        startActivity(new Intent(this, AdminEditTaskActivity.class));
    }
}
