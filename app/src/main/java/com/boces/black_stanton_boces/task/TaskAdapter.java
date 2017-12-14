package com.boces.black_stanton_boces.task;

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
import com.boces.black_stanton_boces.persistence.model.Task;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> implements Filterable {
    private List<Task> displayTasks;
    private TaskAdapterOnclick onclickHandler;
    private TaskFilter filter;

    public TaskAdapter(List<Task> tasks, TaskAdapterOnclick onclickHandler) {
        this.displayTasks = tasks;
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
        Task task = displayTasks.get(position);

        holder.taskId = task.getId();
        holder.taskName.setText(task.getName());
        if (task.getImage() != null)
            holder.taskImage.setImageBitmap(task.getImage());
    }

    @Override
    public int getItemCount() {
        return displayTasks.size();
    }

    public void setTasks(List<Task> tasks) {
        // If We Have A Filter, Update It As Well
        if (filter != null) {
            filter.updateTasks(tasks);
        }
        this.displayTasks = tasks;
    }

    @Override
    public Filter getFilter() {
        // If The Filter Has Not Been Constructed Yet, Do So
        if (filter == null) {
            filter = new TaskFilter(this.displayTasks);
        }

        return filter;
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

    private class TaskFilter extends Filter {
        private List<Task> tasks;

        public TaskFilter(List<Task> tasks) {
            this.tasks = tasks;
        }

        private void updateTasks(List<Task> tasks) {
            this.tasks = tasks;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            // Filter Class Requires Empty Filters To Return All Data
            if (constraint == null || constraint.length() == 0) {
                results.values = tasks;
                results.count = tasks.size();
                return results;
            }

            final String caseConstant = constraint.toString().toUpperCase();
            ArrayList<Task> filteredTasks = new ArrayList<>();
            for (Task task : tasks) {
                if (task.getName().toUpperCase().contains(caseConstant)) {
                    filteredTasks.add(task);
                }
            }

            // Build Results
            results.values = filteredTasks;
            results.count = filteredTasks.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            //noinspection unchecked
            displayTasks = (List<Task>) results.values;
            notifyDataSetChanged();
        }
    }
}
