package com.boces.black_stanton_boces.student;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.boces.black_stanton_boces.R;
import com.boces.black_stanton_boces.persistence.PersistenceInteractor;
import com.boces.black_stanton_boces.persistence.model.Student;
import com.boces.black_stanton_boces.persistence.model.Teacher;

import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder>{
    private List<Student> students;
    private PersistenceInteractor persistence;
    private StudentAdapterOnclick onclickHandler;

    public StudentAdapter(List<Student> students, PersistenceInteractor persistence, StudentAdapterOnclick onclickHandler) {
        this.students = students;
        this.persistence = persistence;
        this.onclickHandler = onclickHandler;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View studentView = inflater.inflate(R.layout.item_student, parent, false);
        return new ViewHolder(studentView, onclickHandler);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Student student = students.get(position);
        holder.studentId = student.getId();

        String studentName = student.getFirstName() + " " + student.getLastName();
        holder.studentName.setText(studentName);

        holder.studentAge.setText(Integer.toString(student.getAge()));

        Teacher teacher = persistence.getTeacher(student.getTeacherId());
        if (teacher != null) {
            String teacherName = teacher.getFirstName() + " " + teacher.getLastName();
            holder.teacherName.setText(teacherName);
        }

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
        public TextView studentName;
        public TextView studentAge;
        public TextView teacherName;
        public ImageView studentImage;
        /**
         * Id of The Current Student
         * Should Probably Not Be Displayed
         */
        public int studentId;

        /**
         * Handles When The Adapter Item Is Clicked
         */
        StudentAdapterOnclick onclick;

        public ViewHolder(View v, final StudentAdapterOnclick onclick) {
            super(v);
            studentName = v.findViewById(R.id.studentListName);
            studentAge = v.findViewById(R.id.studentListAge);
            teacherName = v.findViewById(R.id.studentListTeacherName);
            studentImage = v.findViewById(R.id.studentListImage);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (studentId < 1) {
                        throw new IllegalStateException("Student Id Not Defined");
                    }

                    onclick.onClick(studentId);
                }
            });
        }
    }
}