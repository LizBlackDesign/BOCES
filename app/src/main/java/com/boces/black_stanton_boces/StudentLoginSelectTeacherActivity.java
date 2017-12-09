package com.boces.black_stanton_boces;
//TODO: search bar, save information for current task page, filter for teacher selected in this page to display students in next page

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.boces.black_stanton_boces.persistence.PersistenceInteractor;
import com.boces.black_stanton_boces.teacher.TeacherAdapter;
import com.boces.black_stanton_boces.teacher.TeacherAdapterOnclick;

public class StudentLoginSelectTeacherActivity extends AppCompatActivity {

    private RecyclerView teacherList;
    private PersistenceInteractor persistence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login_select_teacher);

        persistence = new PersistenceInteractor(this);
        TeacherAdapter adapter = new TeacherAdapter(persistence.getAllTeachers(), new TeacherAdapterOnclick() {
            @Override
            public void onClick(int teacherId) {
                Intent selectStudent = new Intent(getApplicationContext(), StudentLoginSelectStudentActivity.class);
                selectStudent.putExtra(StudentLoginSelectStudentActivity.BUNDLE_KEY.TEACHER_ID.name(), teacherId);
                startActivity(selectStudent);
            }
        });

        teacherList = (RecyclerView) findViewById(R.id.recyclerSelectTeacher);
        teacherList.setAdapter(adapter);
        teacherList.setLayoutManager(new LinearLayoutManager(this));
    }


    @Override
    public void onResume() {
        super.onResume();
        ((TeacherAdapter) teacherList.getAdapter()).setTeachers(persistence.getAllTeachers());
        teacherList.getAdapter().notifyDataSetChanged();
    }
}
