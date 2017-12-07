package com.boces.black_stanton_boces.student;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.boces.black_stanton_boces.persistence.model.Student;

import java.util.ArrayList;
import java.util.List;

public class StudentSpinnerInteractor {
    private Spinner spinner;

    public StudentSpinnerInteractor(Spinner spinner, Context context, List<Student> items) {
        this.spinner = spinner;
        ArrayList<StudentSpinnerItem> adapterItems = new ArrayList<>(items.size());

        for (Student student: items) {
            adapterItems.add(new StudentSpinnerItem(student));
        }

        ArrayAdapter<StudentSpinnerItem> adapter =
                new ArrayAdapter<StudentSpinnerItem>(context, android.R.layout.simple_spinner_item, adapterItems);
        spinner.setAdapter(adapter);
    }

    public Student getSelectedItem() {
        StudentSpinnerItem item = (StudentSpinnerItem) spinner.getSelectedItem();
        if (item == null)
            return null;
        return item.getStudent();
    }

    public int setSelectedItem(Student student) {
        return setSelectedItem(student.getId());
    }

    public int setSelectedItem(int studentId) {
        int index = 0;
        for (int i = 0; i < spinner.getCount(); i++) {
            StudentSpinnerItem student = (StudentSpinnerItem) spinner.getItemAtPosition(i);
            if (student.getStudent().getId() == studentId) {
                index = i;
                break;
            }
        }
        spinner.setSelection(index);
        return index;
    }
}
