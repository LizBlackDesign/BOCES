package com.boces.black_stanton_boces;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;


import com.boces.black_stanton_boces.persistence.PersistenceInteractor;
import com.boces.black_stanton_boces.persistence.model.Task;

import java.util.ArrayList;
import java.util.List;

public class AdminTasksActivity extends AppCompatActivity {

    private PersistenceInteractor persistence;
    private RecyclerView taskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_tasks);
        persistence = new PersistenceInteractor(this);
        TaskAdapter adapter = new TaskAdapter(persistence.getAllTasks(), persistence);

        taskList = (RecyclerView) findViewById(R.id.taskList);
        taskList.setAdapter(adapter);
        taskList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onResume() {
        super.onResume();
        ((TaskAdapter) taskList.getAdapter()).setTasks(persistence.getAllTasks());
        taskList.getAdapter().notifyDataSetChanged();
    }

    private class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder>{
        List<Task> tasks;
        PersistenceInteractor persistence;

        public TaskAdapter(List<Task> tasks, PersistenceInteractor persistence) {
            this.tasks = tasks;
            this.persistence = persistence;
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


    //Opens Admin Task Back (back one screen)
    public void onClickAdminBackTasks(View v)
    {
        startActivity(new Intent(this, AdminMenuActivity.class));
    }

    //Opens Admin Task Add (back one screen)
    public void onClickAdminAddTasks(View v)
    {
        startActivity(new Intent(this, AdminAddTaskActivity.class));
    }
    //Opens Admin Task Edit (back one screen)
    public void onClickAdminEditTasks(View v)
    {
        startActivity(new Intent(this, AdminEditTaskActivity.class));
    }
}
