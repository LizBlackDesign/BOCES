package com.boces.black_stanton_boces;
//TODO: search bar, save information for current task page
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

import java.util.ArrayList;
import java.util.List;

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


        StudentAdapter adapter = new StudentAdapter(persistence.getStudentsForTeacher(teacherId), persistence);

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

            holder.studentId = student.getId();
            holder.studentName.setText(student.getFirstName() + " " + student.getLastName());
            holder.studentAge.setText(Integer.toString(student.getAge()));

            Teacher teacher = persistence.getTeacher(student.getTeacherId());
            holder.teacherName.setText(teacher.getFirstName() + " " + teacher.getLastName());
            if (student.getImage() != null)
                holder.studentImage.setImageBitmap(student.getImage());
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
            public int studentId;
            public ImageView studentImage;
            public TextView studentName;
            public TextView studentAge;
            public TextView teacherName;

            public ViewHolder(View v) {
                super(v);
                studentImage = v.findViewById(R.id.studentListImage);
                studentName = v.findViewById(R.id.studentListName);
                studentAge = v.findViewById(R.id.studentListAge);
                teacherName = v.findViewById(R.id.studentListTeacherName);

                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (studentId < 1)
                            throw new IllegalStateException("Student ID Not Defined");
                        Intent selectTask = new Intent(getApplicationContext(), StudentLoginSelectTaskActivity.class);
                        selectTask.putExtra(StudentLoginSelectTaskActivity.BUNDLE_KEY.STUDENT_ID.name(), studentId);
                        startActivity(selectTask);
                    }
                });
            }
        }
    }


    //Opens Log in type Screen (back one screen)
    public void onClickAdminStudentBack(View v) {
        finish();
    }

    //Opens task select
    public void onClickAdminStudentNext(View v) {
        startActivity(new Intent(this, StudentLoginSelectTaskActivity.class));
    }
}
