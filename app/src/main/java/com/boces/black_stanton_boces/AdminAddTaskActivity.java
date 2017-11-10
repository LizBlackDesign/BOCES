package com.boces.black_stanton_boces;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.boces.black_stanton_boces.persistence.PersistenceInteractor;
import com.boces.black_stanton_boces.persistence.model.Task;

import java.util.List;

public class AdminAddTaskActivity extends AppCompatActivity {

    private RecyclerView taskList;
    private EditText inputTaskName;
    private PersistenceInteractor persistence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_task);

        // Get Input References
        inputTaskName = (EditText) findViewById(R.id.inputTask);

        persistence = new PersistenceInteractor(this);
        TaskAdapter adapter = new TaskAdapter(persistence.getAllTasks());

        taskList = (RecyclerView) findViewById(R.id.tasksList);
        taskList.setAdapter(adapter);

        taskList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onResume() {
        super.onResume();
        ((TaskAdapter) taskList.getAdapter()).setTasks(persistence.getAllTasks());
        taskList.getAdapter().notifyDataSetChanged();
    }

    public void onClickAdminTaskAddSave(View v) {
        Task task = new Task();
        task.setName(inputTaskName.getText().toString());

        int taskId = persistence.addTask(task);
        task.setId(taskId);
        ((TaskAdapter) taskList.getAdapter()).addTask(task);
    }


    @SuppressWarnings("WeakerAccess")
    private class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {
        private List<Task> tasks;

        public TaskAdapter(List<Task> tasks) {
            this.tasks = tasks;
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
            holder.taskName.setText(task.getName());
        }

        @Override
        public int getItemCount() {
            return tasks.size();
        }

        public void addTask(Task task) {
            tasks.add(task);
            this.notifyItemInserted(tasks.size() - 1);
        }


        public List<Task> getTasks() {
            return tasks;
        }

        public void setTasks(List<Task> tasks) {
            this.tasks = tasks;
        }


        @SuppressWarnings("WeakerAccess")
        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView taskName;

            public ViewHolder(View v) {
                super(v);
                taskName = v.findViewById(R.id.taskName);
            }
        }
    }

    //Opens Task Manager(back one screen)
    public void onClickAdminTasksAddBack(View v)
    {
        startActivity(new Intent(this, AdminTasksActivity.class));
    }
}
