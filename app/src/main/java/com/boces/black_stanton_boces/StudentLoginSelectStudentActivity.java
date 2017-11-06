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
import android.widget.Spinner;
import android.widget.TextView;

import com.boces.black_stanton_boces.persistence.PersistenceInteractor;
import com.boces.black_stanton_boces.persistence.model.Student;
import com.boces.black_stanton_boces.persistence.model.Teacher;

import java.util.ArrayList;
import java.util.List;

public class StudentLoginSelectStudentActivity extends AppCompatActivity {

    private PersistenceInteractor persistence;
    private RecyclerView studentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login_select_student);

        persistence = new PersistenceInteractor(this);
        StudentAdapter adapter = new StudentAdapter(persistence.getAllStudents(), persistence);

        studentList = (RecyclerView) findViewById(R.id.studentList);
        studentList.setAdapter(adapter);
        studentList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onResume() {
        super.onResume();
        ((StudentAdapter) studentList.getAdapter()).setStudents(persistence.getAllStudents());
        studentList.getAdapter().notifyDataSetChanged();
    }

    private class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder>{
        List<Student> students;
        PersistenceInteractor persistence;

        public StudentAdapter(List<Student> students, PersistenceInteractor persistence) {
            this.students = students;
            this.persistence = persistence;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View studentView = inflater.inflate(R.layout.item_student, parent, false);
            return new ViewHolder(studentView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Student student = students.get(position);

            holder.studentName.setText(student.getFirstName() + " " + student.getLastName());
            holder.studentAge.setText(Integer.toString(student.getAge()));

            Teacher teacher = persistence.getTeacher(student.getTeacherId());
            holder.teacherName.setText(teacher.getFirstName() + " " + teacher.getLastName());
        }

        @Override
        public int getItemCount() {
            return students.size();
        }

        public List<Student> getStudents() {
            return students;
        }

        public void setStudents(List<Student> students) {
            this.students = students;
        }

        @SuppressWarnings("WeakerAccess")
        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView studentName;
            public TextView studentAge;
            public TextView teacherName;

            public ViewHolder(View v) {
                super(v);
                studentName = v.findViewById(R.id.studentListName);
                studentAge = v.findViewById(R.id.studentListAge);
                teacherName = v.findViewById(R.id.studentListTeacherName);
            }
        }
    }


    //Opens Log in type Screen (back one screen)
    public void onClickAdminStudentBack(View v)
    {
        startActivity(new Intent(this, LoginTypeActivity.class));
    }
    //Opens task select
    public void onClickAdminStudentNext(View v)
    {
        startActivity(new Intent(this, StudentLoginSelectTaskActivity.class));
    }
}
