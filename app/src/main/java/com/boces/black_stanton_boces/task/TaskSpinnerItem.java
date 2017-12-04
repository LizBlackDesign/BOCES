package com.boces.black_stanton_boces.task;

import com.boces.black_stanton_boces.persistence.model.Task;

public class TaskSpinnerItem {
    private Task task;

    public TaskSpinnerItem(Task task) {
        this.task = task;
    }

    @Override
    public String toString() {
        return task.getName();
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }
}
