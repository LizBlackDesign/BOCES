/*
 * BOCES
 *
 * Authors: Evan Black, Elizabeth Stanton
 */
package com.boces.black_stanton_boces.activity.student;

import android.Manifest;
import android.content.DialogInterface;
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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.boces.black_stanton_boces.R;
import com.boces.black_stanton_boces.persistence.PersistenceInteractor;
import com.boces.black_stanton_boces.persistence.model.Student;
import com.boces.black_stanton_boces.persistence.model.Teacher;
import com.boces.black_stanton_boces.teacher.TeacherSpinnerInteractor;

/**
 * Pulls Existing Information And Save Updated Information From User
 */
public class AdminEditStudentActivity extends AppCompatActivity {

    private int studentId;
    private EditText firstName;
    private EditText lastName;
    private EditText age;
    private EditText year;
    private TeacherSpinnerInteractor teacherSpinnerInteractor;
    private ImageView imageView;
    private Bitmap image;

    private static final int EXTERNAL_STORAGE_REQUEST = 0;
    private static final int RESULT_LOAD_IMAGE = 1;

    /**
     * Recognised Values That May Be Passed Through Bundles
     */
    public enum BUNDLE_KEY {
        STUDENT_ID
    }

    /**
     * Brings in Extras, Validates, Sets Fields
     * @throws IllegalStateException
     * When Extras Fail to Validate
     * @param savedInstanceState
     * Bundle with Extras Set
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_student);
        Bundle extras = getIntent().getExtras();

        // Painfully Validate That We Got Something
        if (extras == null)
            throw new IllegalStateException("No Data Passed To Edit");
        studentId = extras.getInt(BUNDLE_KEY.STUDENT_ID.name());
        if (studentId == 0)
            throw new IllegalStateException("Student ID Not Passed To Edit");


        PersistenceInteractor persistence = new PersistenceInteractor(this);
        Student currentStudent = persistence.getStudent(studentId); //Re-retrieve information in case account is deleted

        if (currentStudent == null)
            throw new IllegalStateException("Student With ID " + studentId + " Not Found"); //ID doesn't match student

        // Get Input References
        firstName = (EditText) findViewById(R.id.inputStudentFirstName);
        lastName = (EditText) findViewById(R.id.inputStudentLastName);
        age = (EditText) findViewById(R.id.inputStudentAge);
        year = (EditText) findViewById(R.id.inputStudentYear);
        imageView = (ImageView) findViewById(R.id.imgEditStudent);

        if (currentStudent.getImage() != null)
            imageView.setImageBitmap(currentStudent.getImage());

        // Get Spinner For Input/Setup
        Spinner teacherSpinner = (Spinner) findViewById(R.id.spinnerTeacher);
        teacherSpinnerInteractor = new TeacherSpinnerInteractor(teacherSpinner, persistence.getAllTeachers(), this);
        if (currentStudent.getTeacherId() != null)
            teacherSpinnerInteractor.setSelectedItem(currentStudent.getTeacherId());

        firstName.setText(currentStudent.getFirstName());
        lastName.setText(currentStudent.getLastName());
        age.setText(Integer.toString(currentStudent.getAge()));
        year.setText(Integer.toString(currentStudent.getYear()));

    }

    /**
     * Checks If Required Field Is Empty Before For Saving
     * @param v
     * Current View
     */
    public void onSave(View v) {
        // Collect Existing Info
        PersistenceInteractor persistence = new PersistenceInteractor(this);
        Student student = persistence.getStudent(studentId);
        if (student == null) {
            Toast.makeText(this, "Error Student With ID " + studentId + " Not Found", Toast.LENGTH_LONG).show();
            return;
        }
        boolean hasError = false;

        if (firstName.getText().toString().isEmpty()) {
            hasError = true;
            firstName.setError("First Name Is Required");
        } else
            student.setFirstName(firstName.getText().toString());

        if (lastName.getText().toString().isEmpty()) {
            hasError = true;
            lastName.setError("Last Name Is Required");
        } else
            student.setLastName(lastName.getText().toString());

        if (age.getText().toString().isEmpty()) {
            hasError = true;
            age.setError("Age Is Required");
        } else {
            try {
                student.setAge(Integer.parseInt(age.getText().toString()));
                if (student.getAge() < 1) {
                    hasError = true;
                    age.setError("Age Must Be Positive");
                }
            } catch (NumberFormatException e) {
                age.setError("Age Must Be A Number");
            }
        }

        if (year.getText().toString().isEmpty()) {
            hasError = true;
            year.setError("Year Is Required");
        } else {
            try {
                student.setYear(Integer.parseInt(year.getText().toString()));
                if (student.getYear() < 1) {
                    hasError = true;
                    year.setError("Year Must Be Positive");
                }
            } catch (NumberFormatException e) {
                year.setError("Year Must Be A Number");
            }
        }

        Teacher spinnerTeacher = teacherSpinnerInteractor.getSelectedItem();
        if (spinnerTeacher == null) {
            hasError = true;
            new AlertDialog.Builder(this)
                    .setTitle("A Teacher Is Required")
                    .setMessage("Error, A Teacher Is Required")
                    .setCancelable(true)
                    .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .show();
        }

        if (spinnerTeacher != null)
            student.setTeacherId(spinnerTeacher.getId());

        if (image != null)
            student.setImage(image);

        // Stop If We Have An Error
        if (hasError)
            return;

        persistence.update(student);

        // End The Current Activity
        finish(); //Ends activity
    }

    public void onSelectImage(View v) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_REQUEST);
            return;
        }
        Intent mediaIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(mediaIntent, RESULT_LOAD_IMAGE);
    }

    /**
     * Reentrant Point For onSelectImage
     * Enters With RESULT_LOAD_IMAGE
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pull In Selected File
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

    /**
     * Reentrant Point If Permission Must Be Requested
     * Enters With EXTERNAL_STORAGE_REQUEST
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case EXTERNAL_STORAGE_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    onSelectImage(null);
                }
                break;
        }
    }

    /**
     * Deletes Student
     * @param v
     * Current View
     */
    public void onDeleteStudent(View v) {
        PersistenceInteractor persistence = new PersistenceInteractor(this);
        persistence.deleteStudent(studentId);
        finish();
    }

}
