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
import android.view.View;
import android.widget.SearchView;

import com.boces.black_stanton_boces.persistence.PersistenceInteractor;
import com.boces.black_stanton_boces.teacher.TeacherAdapter;
import com.boces.black_stanton_boces.teacher.TeacherAdapterOnclick;

/**
 * Shows Existing Teachers and Allows User Choose to Edit or Create and New One
 */
public class AdminTeachersActivity extends AppCompatActivity {

    private RecyclerView teacherList;
    private PersistenceInteractor persistence;

    /**
     * Retrieve Existing Information
     * @param savedInstanceState
     * Bundle with Extras Set
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_teachers);

        persistence = new PersistenceInteractor(this);
        final TeacherAdapter adapter = new TeacherAdapter(persistence.getAllTeachers(), new TeacherAdapterOnclick() {
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

        SearchView searchView = findViewById(R.id.searchAdminTeachers);
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
     * Re-retrieves Information In Case of Updates
     */
    @Override
    public void onResume() {
        super.onResume();
        ((TeacherAdapter) teacherList.getAdapter()).setTeachers(persistence.getAllTeachers());
        teacherList.getAdapter().notifyDataSetChanged();
    }

    /**
     * Starts Add Activity
     * @param v
     * View Holder
     */
    public void onClickAdminAddTeacher(View v) {
        startActivity(new Intent(this, AdminAddTeacherActivity.class));
    }
}
