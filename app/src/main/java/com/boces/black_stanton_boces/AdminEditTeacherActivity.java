package com.boces.black_stanton_boces;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.boces.black_stanton_boces.persistence.PersistenceInteractor;
import com.boces.black_stanton_boces.persistence.model.Teacher;

public class AdminEditTeacherActivity extends AppCompatActivity {

    private int teacherId;
    private EditText firstName;
    private EditText lastName;
    private EditText email;
    private EditText phone;

    /**
     * Recognised Values That May Be Passed Through Bundles
     */
    public enum BUNDLE_KEY {
        TEACHER_ID
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_teacher);
        Bundle extras = getIntent().getExtras();

        // Painfully Validate That We Got Something
        if (extras == null)
            throw new IllegalStateException("No Data Passed To Edit");
        teacherId = extras.getInt(BUNDLE_KEY.TEACHER_ID.name());
        if (teacherId == 0)
            throw new IllegalStateException("Student ID Not Passed To Edit");

        PersistenceInteractor persistence = new PersistenceInteractor(this);
        Teacher teacher = persistence.getTeacher(teacherId);
        if (teacher == null)
            throw new IllegalStateException("Teacher With ID " + teacherId + " Not Found");

        // Get Input References
        firstName = (EditText) findViewById(R.id.inputTeacherFirstName);
        lastName = (EditText) findViewById(R.id.inputTeacherLastName);
        email = (EditText) findViewById(R.id.inputTeacherEmail);
        phone = (EditText) findViewById(R.id.inputTeacherPhone);

        // Set Current Values
        firstName.setText(teacher.getFirstName());
        lastName.setText(teacher.getLastName());
        email.setText(teacher.getEmail());
        phone.setText(teacher.getPhoneNumber());

    }

    public void onSave(View view) {
        PersistenceInteractor persistence = new PersistenceInteractor(this);
        Teacher teacher = persistence.getTeacher(teacherId);
        if (teacher == null) {
            Toast.makeText(this, "Error Teacher With ID " + teacherId + " Not Found", Toast.LENGTH_LONG).show();
            return;
        }

        teacher.setFirstName(firstName.getText().toString());
        teacher.setLastName(lastName.getText().toString());
        teacher.setEmail(email.getText().toString());
        teacher.setPhoneNumber(phone.getText().toString());

        persistence.update(teacher);
        finish();
    }

    //Opens Teacher Manager (back one screen)
    public void onClickAdminTeacherEditBack(View v) {
        finish();
    }
}
