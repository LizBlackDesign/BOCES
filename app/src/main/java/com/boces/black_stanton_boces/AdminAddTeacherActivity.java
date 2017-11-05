package com.boces.black_stanton_boces;


import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.boces.black_stanton_boces.persistence.PersistenceInteractor;
import com.boces.black_stanton_boces.persistence.model.Teacher;

import java.util.List;

import java.util.List;

public class AdminAddTeacherActivity extends AppCompatActivity {

    private RecyclerView teacherList;
    private EditText inputTeacherFirstName;
    private EditText inputTeacherLastName;
    private EditText inputTeacherEmail;
    private EditText inputTeacherPhone;
    private PersistenceInteractor persistence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_teacher);

        // Get Input References
        inputTeacherFirstName = (EditText) findViewById(R.id.inputTeacherFirstName);
        inputTeacherLastName = (EditText) findViewById(R.id.inputTeacherLastName);
        inputTeacherEmail = (EditText) findViewById(R.id.inputTeacherEmail);
        inputTeacherPhone = (EditText) findViewById(R.id.inputTeacherPhone);

        persistence = new PersistenceInteractor(this);
        TeacherAdapter adapter = new TeacherAdapter(persistence.getAllTeachers());

        teacherList = (RecyclerView) findViewById(R.id.teachersList);
        teacherList.setAdapter(adapter);

        teacherList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onResume() {
        super.onResume();
        ((TeacherAdapter) teacherList.getAdapter()).setTeachers(persistence.getAllTeachers());
        teacherList.getAdapter().notifyDataSetChanged();
    }

    public void onClickAdminTeacherAddSave(View v) {
        Teacher teacher = new Teacher();
        teacher.setFirstName(inputTeacherFirstName.getText().toString());
        teacher.setLastName(inputTeacherLastName.getText().toString());
        teacher.setEmail(inputTeacherEmail.getText().toString());
        teacher.setPhoneNumber(inputTeacherPhone.getText().toString());

        int teacherId = persistence.addTeacher(teacher);
        teacher.setId(teacherId);
        ((TeacherAdapter) teacherList.getAdapter()).addTeacher(teacher);
    }


    @SuppressWarnings("WeakerAccess")
    private class TeacherAdapter extends RecyclerView.Adapter<TeacherAdapter.ViewHolder> {
        private List<Teacher> teachers;

        public TeacherAdapter(List<Teacher> teachers) {
            this.teachers = teachers;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View teacherView = inflater.inflate(R.layout.item_teacher, parent, false);

            return new ViewHolder(teacherView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Teacher teacher = teachers.get(position);

            holder.teacherName.setText(teacher.getFirstName() + " " + teacher.getLastName());
            holder.teacherEmail.setText(teacher.getEmail());
            holder.teacherPhone.setText(teacher.getPhoneNumber());
        }

        @Override
        public int getItemCount() {
            return teachers.size();
        }

        public void addTeacher(Teacher teacher) {
            teachers.add(teacher);
            this.notifyItemInserted(teachers.size() - 1);
        }

        public void clearAll() {
            teachers.clear();
            this.notifyDataSetChanged();
        }

        public List<Teacher> getTeachers() {
            return teachers;
        }

        public void setTeachers(List<Teacher> teachers) {
            this.teachers = teachers;
        }


        @SuppressWarnings("WeakerAccess")
        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView teacherName;
            public TextView teacherEmail;
            public TextView teacherPhone;

            public ViewHolder(View v) {
                super(v);
                teacherName = v.findViewById(R.id.teacherName);
                teacherEmail = v.findViewById(R.id.teacherEmail);
                teacherPhone = v.findViewById(R.id.teacherPhone);
            }
        }

    }

    //Opens Teacher Manager (back one screen)
    public void onClickAdminTeacherAddBack(View v)
    {
        startActivity(new Intent(this, AdminTeachersActivity.class));
    }
}
