package com.boces.black_stanton_boces;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.boces.black_stanton_boces.persistence.PersistenceInteractor;
import com.boces.black_stanton_boces.persistence.model.Teacher;

public class AdminEditTeacherActivity extends AppCompatActivity {

    private int id;
    private EditText firstName;
    private EditText lastName;
    private EditText email;
    private EditText phone;
    private ImageView imageView;
    private Bitmap image;

    private static final int EXTERNAL_STORAGE_REQUEST = 0;
    private static final int RESULT_LOAD_IMAGE = 1;

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
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_REQUEST);
            return;
        }
        Intent mediaIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(mediaIntent, RESULT_LOAD_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImage = data.getData();
            String filePathColumn[] = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            String path = "";

            if (cursor == null)
                return;

            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(filePathColumn[0]));
            }
            cursor.close();

            if (path.isEmpty())
                return;

            image = BitmapFactory.decodeFile(path);
            if (image == null)
                return;

            imageView.setImageBitmap(image);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case EXTERNAL_STORAGE_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    onCamera(null);
                }
                break;
        }
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
