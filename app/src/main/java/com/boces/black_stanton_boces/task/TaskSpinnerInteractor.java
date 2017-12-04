package com.boces.black_stanton_boces.task;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.boces.black_stanton_boces.persistence.model.Task;

import java.util.ArrayList;
import java.util.List;

public class TaskSpinnerInteractor {
    private Spinner spinner;

    public TaskSpinnerInteractor(Spinner spinner, Context context, List<Task> items) {
        this.spinner = spinner;
        ArrayList<TaskSpinnerItem> adapterItems = new ArrayList<>(items.size());

        for (Task task : items) {
            adapterItems.add(new TaskSpinnerItem(task));
        }

        ArrayAdapter<TaskSpinnerItem> adapter =
                new ArrayAdapter<TaskSpinnerItem>(context, android.R.layout.simple_spinner_item, adapterItems);
        spinner.setAdapter(adapter);
    }

    public Task getSelectedItem() {
        TaskSpinnerItem item = (TaskSpinnerItem) spinner.getSelectedItem();
        if (item == null)
            return null;
        return  item.getTask();
    }

    public int setSelectedItem(Task task) {
        return setSelectedItem(task.getId());
    }

    public int setSelectedItem(int taskId) {
        int index = 0;
        for (int i = 0; i < spinner.getCount(); i++) {
            TaskSpinnerItem task = (TaskSpinnerItem) spinner.getItemAtPosition(i);
            if (task.getTask().getId() == taskId) {
                index = i;
                break;
            }
        }

        spinner.setSelection(index);
        return index;
    }
}
