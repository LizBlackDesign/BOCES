package com.boces.black_stanton_boces;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.boces.black_stanton_boces.persistence.PersistenceInteractor;
import com.boces.black_stanton_boces.persistence.model.Student;
import com.boces.black_stanton_boces.persistence.model.Teacher;
import com.boces.black_stanton_boces.teacher.TeacherSpinnerInteractor;
import com.boces.black_stanton_boces.teacher.TeacherSpinnerItem;

import java.util.ArrayList;
import java.util.List;

public class AdminAddStudentActivity extends AppCompatActivity {

    private PersistenceInteractor persistence;
    private EditText inputStudentFirstName;
    private EditText inputStudentLastName;
    private EditText inputStudentAge;
    private EditText inputStudentYear;
    private ImageView imageView;
    private Bitmap image;
    private TeacherSpinnerInteractor teacherSpinnerInteractor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_student);

        // Get Input References
        inputStudentFirstName = (EditText) findViewById(R.id.inputStudentFirstName);
        inputStudentLastName = (EditText) findViewById(R.id.inputStudentLastName);
        inputStudentAge = (EditText) findViewById(R.id.inputStudentAge);
        inputStudentYear = (EditText) findViewById(R.id.inputStudentYear);
        imageView = (ImageView) findViewById(R.id.imgAddStudent);

        // Get Access To The Database
        persistence = new PersistenceInteractor(this);

        // Get Spinner For Input/Setup
        Spinner teacherSpinner = (Spinner) findViewById(R.id.spinnerTeacher);
        teacherSpinnerInteractor = new TeacherSpinnerInteractor(teacherSpinner, persistence.getAllTeachers(), this);
    }

    /**
     * Persists A Student Based Based On Inputs
     * And Updates Recycler
     *
     * @param v
     * Current View
     */
    public void onClickAdminStudentAddSave(View v) {
        Student student = new Student();
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

        persistence.addStudent(student);
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

    //Opens Student Manager (back one screen)
    public void onClickAdminStudentsAddBack(View v) {
        finish();
    }
}
