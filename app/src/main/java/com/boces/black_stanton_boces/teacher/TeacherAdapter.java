package com.boces.black_stanton_boces.teacher;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.boces.black_stanton_boces.R;
import com.boces.black_stanton_boces.persistence.model.Teacher;

import java.util.ArrayList;
import java.util.List;

public class TeacherAdapter extends RecyclerView.Adapter<TeacherAdapter.ViewHolder> implements Filterable {
    private List<Teacher> displayTeachers;
    private TeacherAdapterOnclick onclickHandler;
    private TeacherFilter filter;

    public TeacherAdapter(List<Teacher> teachers, TeacherAdapterOnclick onclickHandler) {
        this.displayTeachers = teachers;
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
        Teacher teacher = displayTeachers.get(position);

        holder.teacherId = teacher.getId();

        String teacherName = teacher.getFirstName() + " " + teacher.getLastName();
        holder.teacherName.setText(teacherName);

        holder.teacherEmail.setText(teacher.getEmail());
        holder.teacherPhone.setText(teacher.getPhoneNumber());
        if (teacher.getImage() != null)
            holder.teacherImage.setImageBitmap(teacher.getImage());
    }

    @Override
    public int getItemCount() {
        return displayTeachers.size();
    }

    public void setTeachers(List<Teacher> teachers) {
        // If We Have A Filter, Then It Controls The Content
        if (filter != null)
            filter.updateTeachers(teachers);
        else
            this.displayTeachers = teachers;
    }

    @Override
    public Filter getFilter() {
        // If The Filter Has Not Been Constructed Yet, Do So
        if (filter == null)
            filter = new TeacherFilter(displayTeachers);
        return filter;
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

    private class TeacherFilter extends Filter {
        private List<Teacher> teachers;

        public TeacherFilter(List<Teacher> teachers) {
            this.teachers = teachers;
        }

        public void updateTeachers(List<Teacher> teachers) {
            this.teachers = teachers;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint == null || constraint.length() == 0) {
                results.values = teachers;
                results.count = teachers.size();
                return results;
            }

            final String caseConstraint = constraint.toString().toUpperCase();
            ArrayList<Teacher> filteredTeachers = new ArrayList<>();

            for (Teacher teacher: teachers) {
                if (teacher.getFirstName().toUpperCase().contains(caseConstraint) ||
                        teacher.getLastName().toUpperCase().contains(caseConstraint)) {
                    filteredTeachers.add(teacher);
                }
            }

            results.values = filteredTeachers;
            results.count = filteredTeachers.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            //noinspection unchecked
            displayTeachers = (List<Teacher>) results.values ;
            notifyDataSetChanged();
        }
    }
}