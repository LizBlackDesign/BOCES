package com.boces.black_stanton_boces;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class StudentLoginSelectTeacherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login_select_teacher);

    }
    //Opens Student Selection Screen (back one screen)
    private void onClickAdminTeacherBack(View v)
    {
        startActivity(new Intent(this, StudentLoginSelectStudentActivity.class));
    }
    //Opens student select
    private void onClickAdminTeacherNext(View v)
    {
        startActivity(new Intent(this, StudentLoginSelectStudentActivity.class));
    }
}
