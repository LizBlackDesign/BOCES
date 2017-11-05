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

public class AdminAddStudentActivity extends AppCompatActivity {

    private PersistenceInteractor persistence;
    private RecyclerView studentList;
    private EditText inputStudentFirstName;
    private EditText inputStudentLastName;
    private EditText inputStudentAge;
    private Spinner teacherSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_student);

        // Get Input References
        inputStudentFirstName = (EditText) findViewById(R.id.inputStudentFirstName);
        inputStudentLastName = (EditText) findViewById(R.id.inputStudentLastName);
        inputStudentAge = (EditText) findViewById(R.id.inputStudentAge);

        // Get Access To The Database
        persistence = new PersistenceInteractor(this);

        // Get Spinner For Input/Setup
        teacherSpinner = (Spinner) findViewById(R.id.spinnerTeacher);



        ArrayAdapter<TeacherSpinnerItem> spinnerItemArrayAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, this.retrieveTeacherSpinnerItems());
        teacherSpinner.setAdapter(spinnerItemArrayAdapter);


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

    /**
     * Persists A Student Based Based On Inputs
     * And Updates Recycler
     *
     * @param v
     * Current View
     */
    public void onClickAdminStudentAddSave(View v) {
        Student student = new Student();
        student.setFirstName(inputStudentFirstName.getText().toString());
        student.setLastName(inputStudentLastName.getText().toString());

        try {
            student.setAge(Integer.parseInt(inputStudentAge.getText().toString()));
        } catch (NumberFormatException e) {
            new AlertDialog.Builder(this)
                    .setTitle("Age Is Invalid")
                    .setMessage("Error, Please Enter A Valid Number For Age")
                    .setCancelable(true)
                    .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .show();
            return;
        }


        TeacherSpinnerItem spinnerTeacher = (TeacherSpinnerItem) teacherSpinner.getSelectedItem();
        if (spinnerTeacher == null) {
            new AlertDialog.Builder(this)
                    .setTitle("A Teacher Is Required")
                    .setMessage("Error, A Teacher Is Required")
                    .setCancelable(true)
                    .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .show();
            return;
        }

        student.setTeacherId(spinnerTeacher.getTeacher().getId());

        int studentId = persistence.addStudent(student);
        student.setId(studentId);
        ((StudentAdapter) studentList.getAdapter()).addStudent(student);
    }


    /**
     * Retrieve The Items For The Teacher Spinner
     * @return List of Teachers Converted For Use In The Spinner
     */
    private ArrayList<TeacherSpinnerItem> retrieveTeacherSpinnerItems() {
        // Grab All Teachers And Convert
        ArrayList<Teacher> persistedTeachers = persistence.getAllTeachers();
        ArrayList<TeacherSpinnerItem> spinnerTeachers = new ArrayList<>(persistedTeachers.size());

        for (Teacher teacher : persistedTeachers) {
            spinnerTeachers.add(new TeacherSpinnerItem(teacher));
        }

        return spinnerTeachers;
    }

    private class TeacherSpinnerItem {
        private Teacher teacher;

        public TeacherSpinnerItem(Teacher teacher) {
            this.teacher = teacher;
        }

        @Override
        public String toString() {
            return teacher.getFirstName() + " " + teacher.getLastName();
        }

        public Teacher getTeacher() {
            return teacher;
        }

        public void setTeacher(Teacher teacher) {
            this.teacher = teacher;
        }
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
            holder.studentYear.setText(Integer.toString(student.getYear()));

            Teacher teacher = persistence.getTeacher(student.getTeacherId());
            holder.teacherName.setText(teacher.getFirstName() + " " + teacher.getLastName());
        }

        @Override
        public int getItemCount() {
            return students.size();
        }

        public void addStudent(Student student)
        {
            students.add(student);
            this.notifyItemInserted(students.size() - 1);
        }

        public List<Student> getStudents() {
            return students;
        }

        public void setStudents(List<Student> students) {
            this.students = students;
        }

        @SuppressWarnings("WeakerAccess")
        public class ViewHolder extends RecyclerView.ViewHolder
        {
            public TextView studentName;
            public TextView studentAge;
            public TextView studentYear;
            public TextView teacherName;

            public ViewHolder(View v) {
                super(v);
                studentName = v.findViewById(R.id.studentListName);
                studentAge = v.findViewById(R.id.studentListAge);
                studentYear = v.findViewById(R.id.studentListYear);
                teacherName = v.findViewById(R.id.studentListTeacherName);
            }
        }
    }


    //Opens Student Manager (back one screen)
    public void onClickAdminStudentsAddBack(View v)
    {
        startActivity(new Intent(this, AdminStudentsActivity.class));
    }
}
