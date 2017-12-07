package com.boces.black_stanton_boces;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class AdminMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_menu);

    }
    //Opens Student Manager
    public void onClickAdminMenuStudents(View v) {
        startActivity(new Intent(this, AdminStudentsActivity.class));
    }
    //Opens Teacher Manager
    public void onClickAdminMenuTeachers(View v) {
        startActivity(new Intent(this, AdminTeachersActivity.class));
    }
    //Opens Task Manager
    public void onClickAdminMenuTasks(View v) {
        startActivity(new Intent(this, AdminTasksActivity.class));
    }

    public void onTimeClocked(View v) {
        startActivity(new Intent(this, AdminPunchSelectStudentActivity.class));
    }
    //There is no reports currently

    public void onClickAdminMenuAccounts(View v) {
        startActivity(new Intent(this, AdminManageAccounts.class));
    }
}
