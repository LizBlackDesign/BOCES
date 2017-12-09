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
import com.boces.black_stanton_boces.student.StudentAdapter;
import com.boces.black_stanton_boces.student.StudentAdapterOnclick;

/**
 * Shows Existing Students and Allows User Choose to Edit or Create and New One
 */
public class AdminStudentsActivity extends AppCompatActivity {

    private PersistenceInteractor persistence;
    private RecyclerView studentList;
    private SearchView searchAdminStudent;

    /**
     * Retrieve Existing Information
     * @param savedInstanceState
     * Bundle with Extras Set
     */
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
        final StudentAdapter adapter = new StudentAdapter(persistence.getAllStudents(), persistence, onclick);

        studentList = (RecyclerView) findViewById(R.id.recyclerSelectStudent);
        studentList.setAdapter(adapter);
        studentList.setLayoutManager(new LinearLayoutManager(this));
        searchAdminStudent = (SearchView) findViewById(R.id.login_select_student_search);
        searchAdminStudent.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
        ((StudentAdapter) studentList.getAdapter()).setStudents(persistence.getAllStudents());
        studentList.getAdapter().notifyDataSetChanged();
    }

    /**
     * Starts Add Activity
     * @param v
     * View Holder
     */
    public void onClickAdminAddStudent(View v) {
        startActivity(new Intent(this, AdminAddStudentActivity.class));
    }
}
