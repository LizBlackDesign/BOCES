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
 * Displays Admin Options
 */
public class AdminMenuActivity extends AppCompatActivity {

    /**
     * Retrieve Existing Information
     * @param savedInstanceState
     * Bundle with Extras Set
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_menu);

    }
    //Opens Student Manager

    /**
     * Starts Student Manager Activity
     * @param v
     * Current View
     */
    public void onClickAdminMenuStudents(View v) {
        startActivity(new Intent(this, AdminStudentsActivity.class));
    }

    /**
     * Starts Teacher Manager Activity
     * @param v
     * Current View
     */
    public void onClickAdminMenuTeachers(View v) {
        startActivity(new Intent(this, AdminTeachersActivity.class));
    }

    /**
     * Starts Task Manager Activity
     * @param v
     * Current View
     */
    public void onClickAdminMenuTasks(View v) {
        startActivity(new Intent(this, AdminTasksActivity.class));
    }

    /**
     * Starts Punch Manager Activity
     * @param v
     * Current View
     */
    public void onTimeClocked(View v) {
        startActivity(new Intent(this, AdminPunchSelectStudentActivity.class));
    }

    /**
     * Starts Report Manager Activity
     * @param v
     * Current View
     */
    public void onReports(View v) {
        startActivity(new Intent(this, AdminReportsActivity.class));
    }

    /**
     * Starts Admin Manager Activity
     * @param v
     * Current View
     */
    public void onClickAdminMenuAccounts(View v) {
        startActivity(new Intent(this, AdminManageAccounts.class));
    }
}
