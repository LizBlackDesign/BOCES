package com.boces.black_stanton_boces;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.boces.black_stanton_boces.persistence.PersistenceInteractor;

public class DeveloperOptionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer_options);
    }

    public void onClearDatabase(View v) {
        PersistenceInteractor persistence = new PersistenceInteractor(this);
        persistence.dropAndRecreate();
        Toast.makeText(this, "Database Recreated", Toast.LENGTH_LONG).show();
    }
}
