package com.boces.black_stanton_boces;


import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.boces.black_stanton_boces.persistence.PersistenceInteractor;
import com.boces.black_stanton_boces.persistence.model.Teacher;


public class AdminAddTeacherActivity extends AppCompatActivity {

    private EditText inputTeacherFirstName;
    private EditText inputTeacherLastName;
    private EditText inputTeacherEmail;
    private EditText inputTeacherPhone;
    private ImageView imageView;
    private Bitmap image;
    private PersistenceInteractor persistence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_teacher);

        // Get Input References
        inputTeacherFirstName = (EditText) findViewById(R.id.inputTeacherFirstName);
        inputTeacherLastName = (EditText) findViewById(R.id.inputTeacherLastName);
        inputTeacherEmail = (EditText) findViewById(R.id.inputTeacherEmail);
        inputTeacherPhone = (EditText) findViewById(R.id.inputTeacherPhone);
        imageView = (ImageView) findViewById(R.id.imgTeacher);

        persistence = new PersistenceInteractor(this);
    }

    public void onClickAdminTeacherAddSave(View v) {
        Teacher teacher = new Teacher();
        teacher.setFirstName(inputTeacherFirstName.getText().toString());
        teacher.setLastName(inputTeacherLastName.getText().toString());
        teacher.setEmail(inputTeacherEmail.getText().toString());
        teacher.setPhoneNumber(inputTeacherPhone.getText().toString());
        if (image != null)
            teacher.setImage(image);

        persistence.addTeacher(teacher);
    }

    //Opens Teacher Manager (back one screen)
    public void onClickAdminTeacherAddBack(View v)
    {
        startActivity(new Intent(this, AdminTeachersActivity.class));
    }
}
