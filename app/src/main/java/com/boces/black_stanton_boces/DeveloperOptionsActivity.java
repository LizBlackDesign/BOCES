/*
 * BOCES
 *
 * Authors: Evan Black, Elizabeth Stanton
 */

package com.boces.black_stanton_boces;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.boces.black_stanton_boces.persistence.PersistenceInteractor;
import com.boces.black_stanton_boces.report.ReportGenerator;
import com.boces.black_stanton_boces.report.StudentPunches;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DeveloperOptionsActivity extends AppCompatActivity {
    private static final int EXTERNAL_STORAGE_REQUEST = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer_options);
    }

    public void onClearDatabase(View v) {
        PersistenceInteractor persistence = new PersistenceInteractor(this);
        persistence.emptyAndRecreate();
        Toast.makeText(this, "Database Recreated", Toast.LENGTH_LONG).show();
    }

    public void onDropDatabase(View v) {
        PersistenceInteractor persistence = new PersistenceInteractor(this);
        persistence.dropDatabase(this);
        Toast.makeText(this, "Database Dropped. Restart App To See Effect", Toast.LENGTH_LONG).show();
    }

    public void onGenerateReport(View v) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_REQUEST);
            return;
        }

        PersistenceInteractor persistence = new PersistenceInteractor(this);

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DATE, -100);
        Date start = cal.getTime();
        Date end = new Date();

        ArrayList<StudentPunches> studentPunches = persistence.getStudentPunches(start, end);
        try {
            ReportGenerator.exportTaskReport(studentPunches, "", persistence.getAllTasks());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case EXTERNAL_STORAGE_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    onGenerateReport(null);
                }
                break;
        }
    }
}
