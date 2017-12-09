package com.boces.black_stanton_boces.report;

import com.boces.black_stanton_boces.persistence.model.Task;

import java.io.IOException;
import java.util.List;


public class ReportRunner implements Runnable {
    private String filename;
    private List<StudentPunches> punches;
    private List<Task> tasks;
    private Callback callback;

    public ReportRunner(String filename, List<StudentPunches> punches, List<Task> tasks, Callback callback) {
        this.filename = filename;
        this.punches = punches;
        this.tasks = tasks;
        this.callback = callback;
    }

    @Override
    public void run() {
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);

        try {
            ReportGenerator.exportTaskReport(punches, filename, tasks);
            callback.onSuccess();
        } catch (IOException e) {
            callback.onFail(e.getMessage());
        }
        callback.always();
    }

    public interface Callback {
        void onSuccess();
        void onFail(String message);
        void always();
    }
}
