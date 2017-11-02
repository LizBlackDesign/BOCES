package com.boces.black_stanton_boces;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class AdminAddStudentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_student);
    }

    //Opens Student Manager (back one screen)
    public void onClickAdminStudentsAddBack(View v)
    {
        startActivity(new Intent(this, AdminStudentsActivity.class));
    }
}
