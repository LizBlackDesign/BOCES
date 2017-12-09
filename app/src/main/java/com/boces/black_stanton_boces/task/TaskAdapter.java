package com.boces.black_stanton_boces.task;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.boces.black_stanton_boces.R;
import com.boces.black_stanton_boces.persistence.model.Task;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {
    private List<Task> tasks;
    private TaskAdapterOnclick onclickHandler;

    public TaskAdapter(List<Task> tasks, TaskAdapterOnclick onclickHandler) {
        this.tasks = tasks;
        this.onclickHandler = onclickHandler;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View taskView = inflater.inflate(R.layout.item_task, parent, false);
        return new ViewHolder(taskView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Task task = tasks.get(position);

        holder.taskId = task.getId();
        holder.taskName.setText(task.getName());
        if (task.getImage() != null)
            holder.taskImage.setImageBitmap(task.getImage());
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    @SuppressWarnings("WeakerAccess")
    public class ViewHolder extends RecyclerView.ViewHolder {
        int taskId;
        public TextView taskName;
        public ImageView taskImage;

        public ViewHolder(View v) {
            super(v);
            taskName = v.findViewById(R.id.taskName);
            taskImage = v.findViewById(R.id.taskImage);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (taskId < 1)
                        throw new IllegalStateException("Task Id Not Defined");

                    onclickHandler.onClick(taskId);
                }
            });
        }
    }
}
