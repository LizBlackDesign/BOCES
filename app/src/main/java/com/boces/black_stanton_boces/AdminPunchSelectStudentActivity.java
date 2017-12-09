package com.boces.black_stanton_boces;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.SearchView;

import com.boces.black_stanton_boces.persistence.PersistenceInteractor;
import com.boces.black_stanton_boces.student.StudentAdapter;
import com.boces.black_stanton_boces.student.StudentAdapterOnclick;

public class AdminPunchSelectStudentActivity extends AppCompatActivity {

    private PersistenceInteractor persistence;
    private RecyclerView studentList;
    private SearchView studentSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_punch_select_student);

        persistence = new PersistenceInteractor(this);
        StudentAdapterOnclick onclick = new StudentAdapterOnclick() {
            @Override
            public void onClick(int studentId) {
                Intent editPunch = new Intent(getApplicationContext(), AdminStudentPunchesActivity.class);
                editPunch.putExtra(AdminStudentPunchesActivity.BUNDLE_KEY.STUDENT_ID.name(), studentId);
                startActivity(editPunch);
            }
        };

        studentList = findViewById(R.id.recyclerSelectStudent);
        final StudentAdapter adapter = new StudentAdapter(persistence.getAllStudents(), persistence, onclick);
        studentList.setAdapter(adapter);
        studentList.setLayoutManager(new LinearLayoutManager(this));

        studentSearch = (SearchView) findViewById(R.id.login_select_student_search);
        studentSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

    @Override
    protected void onResume() {
        super.onResume();
        StudentAdapter adapter = (StudentAdapter) studentList.getAdapter();
        adapter.setStudents(persistence.getAllStudents());
        adapter.notifyDataSetChanged();
    }
}
