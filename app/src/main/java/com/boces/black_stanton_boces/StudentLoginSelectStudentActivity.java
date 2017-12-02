package com.boces.black_stanton_boces;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.boces.black_stanton_boces.persistence.PersistenceInteractor;
import com.boces.black_stanton_boces.persistence.model.TaskPunch;
import com.boces.black_stanton_boces.persistence.model.Teacher;
import com.boces.black_stanton_boces.student.StudentAdapter;
import com.boces.black_stanton_boces.student.StudentAdapterOnclick;

public class StudentLoginSelectStudentActivity extends AppCompatActivity {

    private int teacherId;
    private PersistenceInteractor persistence;
    private RecyclerView studentList;

    /**
     * Recognised Values That May Be Passed Through Bundles
     */
    public enum BUNDLE_KEY {
        TEACHER_ID
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login_select_student);
        Bundle extras = getIntent().getExtras();

        // Painfully Validate That We Got Something
        if (extras == null)
            throw new IllegalArgumentException("No Data Passed To Edit");
        teacherId = extras.getInt(BUNDLE_KEY.TEACHER_ID.name());
        if (teacherId == 0)
            throw new IllegalArgumentException("Teacher ID Not Passed To Edit");

        persistence = new PersistenceInteractor(this);

        Teacher teacher = persistence.getTeacher(teacherId);
        if (teacher == null)
            throw new IllegalArgumentException("Teacher With ID " + teacherId + " Not Found");


        StudentAdapterOnclick onclick = new StudentAdapterOnclick() {
            @Override
            public void onClick(int studentId) {
                TaskPunch openPunch = persistence.getOpenPunch(studentId);
                if (openPunch != null) {
                    Intent currentTask = new Intent(getApplicationContext(), StudentCurrentTaskViewActivity.class);
                    currentTask.putExtra(StudentCurrentTaskViewActivity.BUNDLE_KEY.TASK_ID.name(), openPunch.getTaskId());
                    currentTask.putExtra(StudentCurrentTaskViewActivity.BUNDLE_KEY.STUDENT_ID.name(), studentId);
                    currentTask.putExtra(StudentCurrentTaskViewActivity.BUNDLE_KEY.PUNCH_ID.name(), openPunch.getId());
                    startActivity(currentTask);
                } else {
                    Intent selectTask = new Intent(getApplicationContext(), StudentLoginSelectTaskActivity.class);
                    selectTask.putExtra(StudentLoginSelectTaskActivity.BUNDLE_KEY.STUDENT_ID.name(), studentId);
                    startActivity(selectTask);
                }

            }
        };
        StudentAdapter adapter = new StudentAdapter(persistence.getStudentsForTeacher(teacherId), persistence, onclick);

        studentList = (RecyclerView) findViewById(R.id.recyclerSelectStudent);
        studentList.setAdapter(adapter);
        studentList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onResume() {
        super.onResume();
        ((StudentAdapter) studentList.getAdapter()).setStudents(persistence.getStudentsForTeacher(teacherId));
        studentList.getAdapter().notifyDataSetChanged();
    }

    //Opens Log in type Screen (back one screen)
    public void onClickAdminStudentBack(View v) {
        finish();
    }
}
