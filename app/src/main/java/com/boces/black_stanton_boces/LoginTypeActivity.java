/*
 * BOCES
 *
 * Authors: Evan Black, Elizabeth Stanton
 */
package com.boces.black_stanton_boces;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Allows User to Choose the Login Type
 */
public class LoginTypeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_type);
    }

    /**
     * Starts Student Login Process
     * @param v
     * Current View
     */
    public void onClickStudentLogin(View v)
    {
        startActivity(new Intent( this, StudentLoginSelectTeacherActivity.class));
    }

    /**
     * Starts Admin Login Process
     * @param v
     * Current View
     */
    public void onClickAdminLogin(View v)
    {
        startActivity(new Intent(this, AdminLoginActivity.class));
    }

    /**
     * Starts Developer Options
     * @param v
     * Current View
     */
    public void onClickDeveloperOptions(View v) {
        startActivity(new Intent(this, DeveloperOptionsActivity.class));
    }


}
