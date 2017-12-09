package com.boces.black_stanton_boces;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.boces.black_stanton_boces.persistence.PersistenceInteractor;
import com.boces.black_stanton_boces.teacher.TeacherAdapter;
import com.boces.black_stanton_boces.teacher.TeacherAdapterOnclick;

public class AdminTeachersActivity extends AppCompatActivity {

    private RecyclerView teacherList;
    private PersistenceInteractor persistence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_teachers);

        persistence = new PersistenceInteractor(this);
        TeacherAdapter adapter = new TeacherAdapter(persistence.getAllTeachers(), new TeacherAdapterOnclick() {
            @Override
            public void onClick(int teacherId) {
                Intent editTeacher = new Intent(getApplicationContext(), AdminEditTeacherActivity.class);
                editTeacher.putExtra(AdminEditTeacherActivity.BUNDLE_KEY.TEACHER_ID.name(), teacherId);
                startActivity(editTeacher);
            }
        });

        teacherList = (RecyclerView) findViewById(R.id.teacherList);
        teacherList.setAdapter(adapter);
        teacherList.setLayoutManager(new LinearLayoutManager(this));
    }


    @Override
    public void onResume() {
        super.onResume();
        ((TeacherAdapter) teacherList.getAdapter()).setTeachers(persistence.getAllTeachers());
        teacherList.getAdapter().notifyDataSetChanged();
    }

    //Opens Admin Teacher Back (back one screen)
    public void onClickAdminBackTeacher(View v) {
        finish();
    }

    //Opens Admin Teacher Add (back one screen)
    public void onClickAdminAddTeacher(View v) {
        startActivity(new Intent(this, AdminAddTeacherActivity.class));
    }
}
