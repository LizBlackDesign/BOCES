package com.boces.black_stanton_boces;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class AdminTeachersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_teachers);

    }
    //Opens Admin Teacher Back (back one screen)
    public void onClickAdminBackTeacher(View v)
    {
        startActivity(new Intent(this, AdminMenuActivity.class));
    }

    //Opens Admin Teacher Add (back one screen)
    public void onClickAdminAddTeacher(View v)
    {
        startActivity(new Intent(this, AdminAddTeacherActivity.class));
    }
    //Opens Admin Teacher Edit (back one screen)
    public void onClickAdminEditTeacher(View v)
    {
        startActivity(new Intent(this, AdminEditTeacherActivity.class));
    }
}
