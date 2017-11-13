package com.boces.black_stanton_boces;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.boces.black_stanton_boces.persistence.PersistenceInteractor;
import com.boces.black_stanton_boces.persistence.model.Task;

public class AdminEditTaskActivity extends AppCompatActivity {

    private int id;
    private EditText name;
    private ImageView imageView;
    private Bitmap image;

    /**
     * Recognised Values That May Be Passed Through Bundles
     */
    public enum BUNDLE_KEY {
        TASK_ID
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_task);
        Bundle extras = getIntent().getExtras();

        // Painfully Validate That We Got Something
        if (extras == null)
            throw new IllegalStateException("No Data Passed To Edit");
        id = extras.getInt(BUNDLE_KEY.TASK_ID.name());
        if (id == 0)
            throw new IllegalStateException("Task ID Not Passed To Edit");

        PersistenceInteractor persistence = new PersistenceInteractor(this);
        Task task = persistence.getTask(id);
        if (task == null)
            throw new IllegalStateException("Task With ID " + id + " Not Found");

        // Get Input References
        name = (EditText) findViewById(R.id.inputTask);
        imageView = (ImageView) findViewById(R.id.imgTask);
        if (task.getImage() != null)
            imageView.setImageBitmap(task.getImage());

        // Set Current Values
        name.setText(task.getName());
    }

    public void onSave(View view) {
        PersistenceInteractor persistence = new PersistenceInteractor(this);
        Task task = persistence.getTask(id);
        if (task == null) {
            Toast.makeText(this, "Error Task With ID " + id + " Not Found", Toast.LENGTH_LONG).show();
            return;
        }

        task.setName(name.getText().toString());
        if (image != null)
            task.setImage(image);

        persistence.update(task);
        finish();
    }

    public void onCamera(View v) {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, 1);
        cameraIntent.setType("image/*");
        cameraIntent.putExtra("crop", "true");
        cameraIntent.putExtra("aspectX", 0);
        cameraIntent.putExtra("aspectY", 0);
        cameraIntent.putExtra("outputX", 250);
        cameraIntent.putExtra("outputY", 200);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bundle extras = data.getExtras();

        if (extras == null) {
            Toast.makeText(this, "No Image Passed Back", Toast.LENGTH_LONG).show();
            return;
        }
        image = extras.getParcelable("data");
        if (image == null) {
            Toast.makeText(this, "No Image Passed Back", Toast.LENGTH_LONG).show();
            return;
        }
        imageView.setImageBitmap(image);
    }

    public void onDelete(View view) {
        PersistenceInteractor persistence = new PersistenceInteractor(this);
        persistence.deleteTask(id);
        finish();
    }

    // Opens Task Manager(back one screen)
    public void onClickAdminTaskEditBack(View v) {
        finish();
    }
}
