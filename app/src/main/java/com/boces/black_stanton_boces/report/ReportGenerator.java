package com.boces.black_stanton_boces.report;

import android.annotation.SuppressLint;
import android.os.Environment;

import com.boces.black_stanton_boces.persistence.model.Task;
import com.boces.black_stanton_boces.persistence.model.TaskPunch;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class ReportGenerator {
    public static void exportTaskReport(List<StudentPunches> studentPunches, String fileName, List<Task> tasks) throws IOException {
        File tempFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),  fileName+".csv");
        @SuppressLint("UseSparseArrays")
        HashMap<Integer, Task> taskMap = new HashMap<>();

        // Key All Tasks For Later Retrieval
        for (Task task : tasks) {
            taskMap.put(task.getId(), task);
        }

        final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        final SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a", Locale.US);
        StringBuilder stringBuilder = new StringBuilder();


        CSVWriter writer = new CSVWriter(new FileWriter(tempFile));

        final String header[] = new String[]{"Student", "Task", "Date", "Time In", "Time Out", "Duration"};
        // Header
        writer.writeNext(header);


        // Data
        String buffer[] = new String[header.length];
        int lastStudentId = -1;
        for (StudentPunches studentPunch : studentPunches) {

            for (TaskPunch taskPunch : studentPunch.getPunches()) {

                // If We've Moved To A New Student, Write Their Name
                if (studentPunch.getStudent().getId() != lastStudentId) {
                    buffer[0] = studentPunch.getStudent().getLastName() + " " + studentPunch.getStudent().getFirstName();
                    lastStudentId = studentPunch.getStudent().getId();
                } else // Do Not Repeat Student Names If We Don't Need To
                    buffer[0] = "";

                buffer[1] = taskMap.get(taskPunch.getTaskId()).getName();
                buffer[2] = dateFormat.format(taskPunch.getTimeStart());

                // Both Start Date & Time Come From timeStart
                buffer[3] = timeFormat.format(taskPunch.getTimeStart());

                if (taskPunch.getTimeEnd() != null)
                    buffer[4] = timeFormat.format(taskPunch.getTimeEnd());
                else
                    buffer[4] = "--Clocked In--";

                final long deltaTime = taskPunch.getTimeEnd().getTime() - taskPunch.getTimeStart().getTime();
                final long deltaHours = TimeUnit.MILLISECONDS.toHours(deltaTime);
                final long extraMinutes = TimeUnit.MILLISECONDS.toMinutes(deltaTime) % 60;
                final long extraSeconds = TimeUnit.MILLISECONDS.toSeconds(deltaTime) % 60;

                // Only Add Hours If Needed
                if (deltaHours > 0)
                    stringBuilder.append(Long.toString(deltaHours)).append(":");

                // Always Add Minutes/Seconds
                stringBuilder.append(Long.toString(extraMinutes)).append(":").append(Long.toString(extraSeconds));

                buffer[5] = stringBuilder.toString();

                // Empty String Builder
                stringBuilder.setLength(0);

                // Write Line
                writer.writeNext(buffer);
            }
        }

        writer.flush();

    }
}
