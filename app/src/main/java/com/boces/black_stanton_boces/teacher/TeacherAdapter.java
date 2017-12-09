package com.boces.black_stanton_boces.teacher;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.boces.black_stanton_boces.R;
import com.boces.black_stanton_boces.persistence.model.Teacher;

import java.util.List;

public class TeacherAdapter extends RecyclerView.Adapter<TeacherAdapter.ViewHolder> {
    private List<Teacher> teachers;
    private TeacherAdapterOnclick onclickHandler;

    public TeacherAdapter(List<Teacher> teachers, TeacherAdapterOnclick onclickHandler) {
        this.teachers = teachers;
        this.onclickHandler = onclickHandler;
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

        holder.teacherId = teacher.getId();
        holder.teacherName.setText(teacher.getFirstName() + " " + teacher.getLastName());
        holder.teacherEmail.setText(teacher.getEmail());
        holder.teacherPhone.setText(teacher.getPhoneNumber());
        if (teacher.getImage() != null)
            holder.teacherImage.setImageBitmap(teacher.getImage());
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
        public ImageView teacherImage;
        public int teacherId;

        public ViewHolder(View v) {
            super(v);
            teacherName = v.findViewById(R.id.teacherName);
            teacherEmail = v.findViewById(R.id.teacherEmail);
            teacherPhone = v.findViewById(R.id.teacherPhone);
            teacherImage = v.findViewById(R.id.teacherImage);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (teacherId < 1)
                        throw new IllegalStateException("Teacher Id Not Defined");
                    onclickHandler.onClick(teacherId);
                }
            });
        }
    }

}