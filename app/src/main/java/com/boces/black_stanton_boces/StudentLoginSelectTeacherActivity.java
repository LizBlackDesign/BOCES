/*
 * BOCES
 *
 * Authors: Evan Black, Elizabeth Stanton
 */
package com.boces.black_stanton_boces;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.SearchView;

import com.boces.black_stanton_boces.persistence.PersistenceInteractor;
import com.boces.black_stanton_boces.teacher.TeacherAdapter;
import com.boces.black_stanton_boces.teacher.TeacherAdapterOnclick;

/**
 * Allows Student to Choose a Teacher
 */

public class StudentLoginSelectTeacherActivity extends AppCompatActivity {

    private RecyclerView teacherList;
    private PersistenceInteractor persistence;

    /**
     * Brings in Extras, Validates
     * @throws IllegalStateException
     * When Extras Fail to Validate
     * @param savedInstanceState
     * Bundle with Extras Set
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login_select_teacher);

        persistence = new PersistenceInteractor(this);
        final TeacherAdapter adapter = new TeacherAdapter(persistence.getAllTeachers(), new TeacherAdapterOnclick() {
            @Override
            public void onClick(int teacherId) {
                Intent selectStudent = new Intent(getApplicationContext(), StudentLoginSelectStudentActivity.class);
                selectStudent.putExtra(StudentLoginSelectStudentActivity.BUNDLE_KEY.TEACHER_ID.name(), teacherId);
                startActivity(selectStudent);
            }
        });

        teacherList = findViewById(R.id.recyclerSelectTeacher);
        teacherList.setAdapter(adapter);
        teacherList.setLayoutManager(new LinearLayoutManager(this));

        SearchView searchView = findViewById(R.id.login_select_teacher_search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        });
    }

    /**
     * Updates Changed Information
     */
    @Override
    public void onResume() {
        super.onResume();
        ((TeacherAdapter) teacherList.getAdapter()).setTeachers(persistence.getAllTeachers());
        teacherList.getAdapter().notifyDataSetChanged();
    }
}
