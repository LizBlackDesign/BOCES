package com.boces.black_stanton_boces.taskpunch;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.boces.black_stanton_boces.R;
import com.boces.black_stanton_boces.persistence.model.Student;
import com.boces.black_stanton_boces.persistence.model.Task;
import com.boces.black_stanton_boces.persistence.model.TaskPunch;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class PunchAdapter extends RecyclerView.Adapter<PunchAdapter.ViewHolder> {
    private List<TaskPunch> punches;
    private Map<Integer, Student> students;
    private Map<Integer, Task> tasks;
    private PunchAdapterOnclick onclick;

    public PunchAdapter(List<TaskPunch> punches, Map<Integer, Student> students, Map<Integer, Task> tasks,  PunchAdapterOnclick onclick) {
        this.punches = punches;
        this.students = students;
        this.tasks = tasks;
        this.onclick = onclick;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View punchView = inflater.inflate(R.layout.item_time, parent, false);
        return new ViewHolder(punchView, onclick);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TaskPunch punch = punches.get(position);
        Student student = students.get(punch.getStudentId());
        String studentName = "";
        if (student != null)
            studentName = student.getFirstName() + " " + student.getLastName();
        String taskName = "";
        Task task = tasks.get(punch.getTaskId());
        if (task != null)
            taskName = task.getName();

        holder.punchId = punch.getId();
        holder.studentListName.setText(studentName);
        holder.timeListDate.setText(new SimpleDateFormat("HH:mm:ss", Locale.US).format(punch.getTimeStart()));
        if (punch.getTimeEnd() != null) {
            final long diff = punch.getTimeEnd().getTime() - punch.getTimeStart().getTime();
            final long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);
            final long seconds = TimeUnit.MILLISECONDS.toSeconds(diff) % 60L;
            String duration = Long.toString(minutes) + ":" + Long.toString(seconds);
            holder.timeListDuration.setText(duration);
        }
        holder.timeListTask.setText(taskName);
    }

    @Override
    public int getItemCount() {
        return punches.size();
    }

    public List<TaskPunch> getPunches() {
        return punches;
    }

    public void setPunches(List<TaskPunch> punches) {
        this.punches = punches;
    }

    public Map<Integer, Student> getStudents() {
        return students;
    }

    public void setStudents(Map<Integer, Student> students) {
        this.students = students;
    }

    public Map<Integer, Task> getTasks() {
        return tasks;
    }

    public void setTasks(Map<Integer, Task> tasks) {
        this.tasks = tasks;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    @SuppressWarnings("WeakerAccess")
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView studentListName;
        TextView timeListTask;
        TextView timeListDate;
        TextView timeListDuration;
        int punchId;

        public ViewHolder(View v, final PunchAdapterOnclick onClick) {
            super(v);
            studentListName = v.findViewById(R.id.studentListName);
            timeListTask = v.findViewById(R.id.timeListTask);
            timeListDate = v.findViewById(R.id.timeListDate);
            timeListDuration = v.findViewById(R.id.timeListDuration);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (punchId < 0)
                        throw new IllegalStateException("Punch ID Not Defined");
                    onClick.onclick(punchId);
                }
            });
        }
    }
}
