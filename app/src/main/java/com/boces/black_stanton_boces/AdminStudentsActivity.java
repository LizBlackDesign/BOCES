package com.boces.black_stanton_boces;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.boces.black_stanton_boces.persistence.PersistenceInteractor;
import com.boces.black_stanton_boces.persistence.model.Student;
import com.boces.black_stanton_boces.persistence.model.Teacher;
import com.boces.black_stanton_boces.student.StudentAdapter;
import com.boces.black_stanton_boces.student.StudentAdapterOnclick;

import java.util.ArrayList;
import java.util.List;

public class AdminStudentsActivity extends AppCompatActivity {

    private PersistenceInteractor persistence;
    private RecyclerView studentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_students);
        StudentAdapterOnclick onclick = new StudentAdapterOnclick() {
            @Override
            public void onClick(int studentId) {
                Intent editStudent = new Intent(getApplicationContext(), AdminEditStudentActivity.class);
                editStudent.putExtra(AdminEditStudentActivity.BUNDLE_KEY.STUDENT_ID.name(), studentId);
                startActivity(editStudent);
            }
        };

        persistence = new PersistenceInteractor(this);
        StudentAdapter adapter = new StudentAdapter(persistence.getAllStudents(), persistence, onclick);

        studentList = (RecyclerView) findViewById(R.id.recyclerSelectStudent);
        studentList.setAdapter(adapter);
        studentList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onResume() {
        super.onResume();
        ((StudentAdapter) studentList.getAdapter()).setStudents(persistence.getAllStudents());
        studentList.getAdapter().notifyDataSetChanged();
    }

    //Opens Admin Student Back (back one screen)
    public void onClickAdminBackStudent(View v) {
        finish();
    }

    //Opens Admin Student Add (back one screen)
    public void onClickAdminAddStudent(View v) {
        startActivity(new Intent(this, AdminAddStudentActivity.class));
    }
}
