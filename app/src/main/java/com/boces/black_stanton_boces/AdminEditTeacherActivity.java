package com.boces.black_stanton_boces;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.boces.black_stanton_boces.persistence.PersistenceInteractor;
import com.boces.black_stanton_boces.persistence.model.Teacher;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class AdminEditTeacherActivity extends AppCompatActivity {

    private int id;
    private EditText firstName;
    private EditText lastName;
    private EditText email;
    private EditText phone;
    private ImageView imageView;
    private Bitmap image;

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
        id = extras.getInt(BUNDLE_KEY.TEACHER_ID.name());
        if (id == 0)
            throw new IllegalStateException("Teacher ID Not Passed To Edit");

        PersistenceInteractor persistence = new PersistenceInteractor(this);
        Teacher teacher = persistence.getTeacher(id);
        if (teacher == null)
            throw new IllegalStateException("Teacher With ID " + id + " Not Found");

        // Get Input References
        firstName = (EditText) findViewById(R.id.inputTeacherFirstName);
        lastName = (EditText) findViewById(R.id.inputTeacherLastName);
        email = (EditText) findViewById(R.id.inputTeacherEmail);
        phone = (EditText) findViewById(R.id.inputTeacherPhone);
        imageView = (ImageView) findViewById(R.id.imgEditTeacher);

        // Set Current Values
        firstName.setText(teacher.getFirstName());
        lastName.setText(teacher.getLastName());
        email.setText(teacher.getEmail());
        phone.setText(teacher.getPhoneNumber());
        if (teacher.getImage() != null)
            imageView.setImageBitmap(teacher.getImage());
    }

    public void onSave(View view) {
        PersistenceInteractor persistence = new PersistenceInteractor(this);
        Teacher teacher = persistence.getTeacher(id);
        if (teacher == null) {
            Toast.makeText(this, "Error Teacher With ID " + id + " Not Found", Toast.LENGTH_LONG).show();
            return;
        }

        teacher.setFirstName(firstName.getText().toString());
        teacher.setLastName(lastName.getText().toString());
        teacher.setEmail(email.getText().toString());
        teacher.setPhoneNumber(phone.getText().toString());

        if (image != null)
            teacher.setImage(image);

        persistence.update(teacher);
        finish();
    }

    public void onCamera(View v) {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, 1);
        cameraIntent.setType("image/*");
        cameraIntent.putExtra("crop", "true");
        cameraIntent.putExtra("aspectX", 0);
        cameraIntent.putExtra("aspectY", 0);
        cameraIntent.putExtra("outputX", 250);
        cameraIntent.putExtra("outputY", 200);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bundle extras = data.getExtras();

        if (extras == null) {
            Toast.makeText(this, "No Image Passed Back", Toast.LENGTH_LONG).show();
            return;
        }
        image = extras.getParcelable("data");
        if (image == null) {
            Toast.makeText(this, "No Image Passed Back", Toast.LENGTH_LONG).show();
            return;
        }
        imageView.setImageBitmap(image);
    }

    public void onDeleteTeacher(View v) {
        PersistenceInteractor persistence = new PersistenceInteractor(this);
        persistence.deleteTeacher(id);
        finish();
    }

    //Opens Teacher Manager (back one screen)
    public void onClickAdminTeacherEditBack(View v) {
        finish();
    }
}
