package com.boces.black_stanton_boces.student;

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
import com.boces.black_stanton_boces.persistence.PersistenceInteractor;
import com.boces.black_stanton_boces.persistence.model.Student;
import com.boces.black_stanton_boces.persistence.model.Teacher;

import java.util.ArrayList;
import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> implements Filterable {
    private List<Student> displayStudents;
    private PersistenceInteractor persistence;
    private StudentAdapterOnclick onclickHandler;
    private StudentFilter studentFilter;

    public StudentAdapter(List<Student> students, PersistenceInteractor persistence, StudentAdapterOnclick onclickHandler) {
        this.displayStudents = students;
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
        Student student = displayStudents.get(position);
        holder.studentId = student.getId();

        String studentName = student.getFirstName() + " " + student.getLastName();
        holder.studentName.setText(studentName);

        // Handle If No Teacher Is Set
        if (student.getTeacherId() == null) {
            holder.teacherName.setText(R.string.no_teacher);
        } else {
            Teacher teacher = persistence.getTeacher(student.getTeacherId());
            // Make Sure Teacher Was Found
            if (teacher != null) {
                String teacherName = teacher.getFirstName() + " " + teacher.getLastName();
                holder.teacherName.setText(teacherName);
            }
        }

        if (student.getImage() != null)
            holder.studentImage.setImageBitmap(student.getImage());
    }

    @Override
    public int getItemCount() {
        return displayStudents.size();
    }

    public void setStudents(List<Student> students) {
        if (studentFilter != null)
            studentFilter.updateStudents(students);
        this.displayStudents = students;
    }

    @Override
    public Filter getFilter() {
        // If The Filter Has Not Been Constructed Yet, Do So
        if (studentFilter == null) {
            studentFilter = new StudentFilter(this.displayStudents);
        }

        return studentFilter;
    }

    @SuppressWarnings("WeakerAccess")
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView studentName;
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


    @SuppressWarnings("WeakerAccess")
    private class StudentFilter extends Filter {
        private List<Student> students;

        public StudentFilter(List<Student> students) {
            this.students = students;
        }

        public void updateStudents(List<Student> students) {
            this.students = students;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            // Filter Class Requires Empty Filters To Return All Data
            if (constraint == null || constraint.length() == 0) {
                results.values = students;
                results.count = students.size();
                return results;
            }

            final String caseConstraint = constraint.toString().toUpperCase();
            ArrayList<Student> filteredStudents = new ArrayList<>();

            // If Student First/Last Name Contains The Filter, Return Them
            for (Student student : students) {
                if (student.getFirstName().toUpperCase().contains(caseConstraint) ||
                        student.getLastName().toUpperCase().contains(caseConstraint)) {
                    filteredStudents.add(student);
                }
            }

            // Build Results
            results.values = filteredStudents;
            results.count = filteredStudents.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            //noinspection unchecked
            displayStudents = (List<Student>) results.values;
            notifyDataSetChanged();
        }

    }
}