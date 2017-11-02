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
    private void onClickAdminMenuStudents(View v)
    {
        startActivity(new Intent(this, AdminStudentsActivity.class));
    }
    //Opens Teacher Manager
    private void onClickAdminMenuTeachers(View v)
    {
        startActivity(new Intent(this, AdminTeachersActivity.class));
    }

    //There is no time clocked currently
    //There is no reports currently

    //Opens Admin LogIn (back one screen)
    private void onClickAdminMenuBack(View v)
    {
        startActivity(new Intent(this, AdminMenuActivity.class));
    }
}
