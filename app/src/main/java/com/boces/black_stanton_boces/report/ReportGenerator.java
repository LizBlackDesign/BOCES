package com.boces.black_stanton_boces.report;

import android.annotation.SuppressLint;
import android.os.Environment;


import com.boces.black_stanton_boces.persistence.model.Task;
import com.boces.black_stanton_boces.persistence.model.TaskPunch;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class ReportGenerator {
    public static void exportTaskReport(List<StudentPunches> studentPunches, String fileName, List<Task> tasks) {
        File tempFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "HelloWorld.csv");
        @SuppressLint("UseSparseArrays")
        HashMap<Integer, Task> taskMap = new HashMap<>();

        // Key All Tasks For Later Retrieval
        for (Task task : tasks) {
            taskMap.put(task.getId(), task);
        }

        final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        final SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a", Locale.US);


        try {
            CSVWriter writer = new CSVWriter(new FileWriter(tempFile));

            // Header
            writer.writeNext(new String[]{"Student", "Task", "Date", "Duration"});


            // Data
            String buffer[] = new String[4];
            int lastStudentId = -1;
            for (StudentPunches studentPunch : studentPunches) {

                for (TaskPunch taskPunch : studentPunch.getPunches()) {
                    if (studentPunch.getStudent().getId() != lastStudentId) {
                        buffer[0] = studentPunch.getStudent().getLastName() + " " + studentPunch.getStudent().getFirstName();
                        lastStudentId = studentPunch.getStudent().getId();
                    } else
                        buffer[0] = "";

                    buffer[1] = taskMap.get(taskPunch.getTaskId()).getName();
                    buffer[2] = dateFormat.format(taskPunch.getTimeStart());
                    final long deltaTime = taskPunch.getTimeStart().getTime();
                    final long deltaMinutes = TimeUnit.MILLISECONDS.toMinutes(deltaTime);
                    final long extraSeconds = TimeUnit.MILLISECONDS.toSeconds(deltaTime) % 60;
                    buffer[3] = Long.toString(deltaMinutes) + ":" + Long.toString(extraSeconds);

                    writer.writeNext(buffer);
                }
            }

            writer.flush();
        } catch (Exception ignored) {
        }

    }
}
