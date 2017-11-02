package com.boces.black_stanton_boces;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class StudentLoginSelectStudentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login_select_student);

    }
    //Opens Log in type Screen (back one screen)
    public void onClickAdminStudentBack(View v)
    {
        startActivity(new Intent(this, LoginTypeActivity.class));
    }
    //Opens task select
    public void onClickAdminStudentNext(View v)
    {
        startActivity(new Intent(this, StudentLoginSelectTaskActivity.class));
    }
}
