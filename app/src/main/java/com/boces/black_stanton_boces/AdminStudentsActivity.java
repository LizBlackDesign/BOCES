package com.boces.black_stanton_boces;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class AdminStudentsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_students);

    }
    //Opens Admin Student Back (back one screen)
    private void onClickAdminBackStudent(View v)
    {
        startActivity(new Intent(this, AdminMenuActivity.class));
    }

    //Opens Admin Student Add (back one screen)
    private void onClickAdminAddStudent(View v)
    {
        startActivity(new Intent(this, AdminAddStudentActivity.class));
    }
    //Opens Admin Student Edit (back one screen)
    private void onClickAdminEditStudent(View v)
    {
        startActivity(new Intent(this, AdminEditStudentActivity.class));
    }
}
