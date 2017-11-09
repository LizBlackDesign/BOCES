package com.boces.black_stanton_boces.teacher;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.boces.black_stanton_boces.persistence.model.Teacher;

import java.util.ArrayList;
import java.util.List;

public class TeacherSpinnerInteractor {
    private Spinner spinner;
    private Context context;

    public TeacherSpinnerInteractor(Spinner spinner, Context context) {
        this.spinner = spinner;
        this.context = context;
    }

    public TeacherSpinnerInteractor(Spinner spinner, List<Teacher> items, Context context) {
        this.spinner = spinner;
        this.context = context;
        ArrayList<TeacherSpinnerItem> adapterItems = new ArrayList<>(items.size());

        // Convert To Spinner Items
        for (Teacher teacher : items) {
            adapterItems.add(new TeacherSpinnerItem(teacher));
        }

        ArrayAdapter<TeacherSpinnerItem> adapter =
                new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, adapterItems);
        spinner.setAdapter(adapter);
    }

    public Teacher getSelectedItem() {
        TeacherSpinnerItem item = (TeacherSpinnerItem) spinner.getSelectedItem();
        if (item == null)
            return null;
        return item.getTeacher();
    }

    public int setSelectedItem(Teacher teacher) {
        return setSelectedItem(teacher.getId());
    }

    public int setSelectedItem(int teacherId) {
        // Find Index of The Teacher In The Spinner
        int index = 0;
        for (int i = 0; i < spinner.getCount(); i++) {
            TeacherSpinnerItem teacher = (TeacherSpinnerItem) spinner.getItemAtPosition(i);
            if (teacher.getTeacher().getId() == teacherId) {
                index = i;
                break;
            }
        }
        spinner.setSelection(index);
        return index;
    }
}
