package com.boces.black_stanton_boces;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.boces.black_stanton_boces.persistence.PersistenceInteractor;
import com.boces.black_stanton_boces.persistence.model.Student;
import com.boces.black_stanton_boces.persistence.model.Teacher;
import com.boces.black_stanton_boces.teacher.TeacherSpinnerInteractor;
import com.boces.black_stanton_boces.teacher.TeacherSpinnerItem;

public class AdminEditStudentActivity extends AppCompatActivity {

    private int studentId;
    private EditText inputStudentFirstName;
    private EditText inputStudentLastName;
    private EditText inputStudentAge;
    private EditText inputStudentYear;
    private TeacherSpinnerInteractor teacherSpinnerInteractor;
    private ImageView imageView;
    private Bitmap image;

    /**
     * Recognised Values That May Be Passed Through Bundles
     */
    public enum BUNDLE_KEY {
        STUDENT_ID
    }

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
        Student currentStudent = persistence.getStudent(studentId);
        if (currentStudent == null)
            throw new IllegalStateException("Student With ID " + studentId + " Not Found");

        // Get Input References
        inputStudentFirstName = (EditText) findViewById(R.id.inputStudentFirstName);
        inputStudentLastName = (EditText) findViewById(R.id.inputStudentLastName);
        inputStudentAge = (EditText) findViewById(R.id.inputStudentAge);
        inputStudentYear = (EditText) findViewById(R.id.inputStudentYear);
        imageView = (ImageView) findViewById(R.id.imgEditStudent);

        if (currentStudent.getImage() != null)
            imageView.setImageBitmap(currentStudent.getImage());

        // Get Spinner For Input/Setup
        Spinner teacherSpinner = (Spinner) findViewById(R.id.spinnerTeacher);
        teacherSpinnerInteractor = new TeacherSpinnerInteractor(teacherSpinner, persistence.getAllTeachers(), this);
        teacherSpinnerInteractor.setSelectedItem(currentStudent.getTeacherId());

        inputStudentFirstName.setText(currentStudent.getFirstName());
        inputStudentLastName.setText(currentStudent.getLastName());
        inputStudentAge.setText(Integer.toString(currentStudent.getAge()));
        inputStudentYear.setText(Integer.toString(currentStudent.getYear()));

    }

    public void onSave(View view) {
        // Collect Existing Info
        PersistenceInteractor persistence = new PersistenceInteractor(this);
        Student student = persistence.getStudent(studentId);
        if (student == null) {
            Toast.makeText(this, "Error Student With ID " + studentId + " Not Found", Toast.LENGTH_LONG).show();
            return;
        }
        student.setFirstName(inputStudentFirstName.getText().toString());
        student.setLastName(inputStudentLastName.getText().toString());

        try {
            student.setAge(Integer.parseInt(inputStudentAge.getText().toString()));
        } catch (NumberFormatException e) {
            new AlertDialog.Builder(this)
                    .setTitle("Age Is Invalid")
                    .setMessage("Error, Please Enter A Valid Number For Age")
                    .setCancelable(true)
                    .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .show();
            return;
        }

        try {
            student.setYear(Integer.parseInt(inputStudentYear.getText().toString()));
        } catch (NumberFormatException e) {
            new AlertDialog.Builder(this)
                    .setTitle("Age Is Invalid")
                    .setMessage("Error, Please Enter A Valid Number For Year")
                    .setCancelable(true)
                    .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .show();
            return;
        }


        Teacher spinnerTeacher = teacherSpinnerInteractor.getSelectedItem();
        if (spinnerTeacher == null) {
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
            return;
        }

        student.setTeacherId(spinnerTeacher.getId());

        if (image != null)
            student.setImage(image);

        persistence.update(student);

        // End The Current Activity
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


    public void onDeleteStudent(View v) {
        PersistenceInteractor persistence = new PersistenceInteractor(this);
        persistence.deleteStudent(studentId);
        finish();
    }

    public void onClickAdminStudentsEditBack(View v) {
        finish();
    }

}
