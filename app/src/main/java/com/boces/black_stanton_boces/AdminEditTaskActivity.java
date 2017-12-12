/*
 * BOCES
 *
 * Authors: Evan Black, Elizabeth Stanton
 */
package com.boces.black_stanton_boces;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.boces.black_stanton_boces.persistence.PersistenceInteractor;
import com.boces.black_stanton_boces.persistence.model.Task;

/**
 * Pulls Existing Information And Save Update Information From User
 */
public class AdminEditTaskActivity extends AppCompatActivity {

    private int id;
    private EditText taskName;
    private ImageView imageView;
    private Bitmap image;

    /**
     * Recognised Values That May Be Passed Through Bundles
     */
    public enum BUNDLE_KEY {
        TASK_ID
    }

    private static final int EXTERNAL_STORAGE_REQUEST = 0;
    private static final int RESULT_LOAD_IMAGE = 1;

    /**
     * Brings in Extras, Validates, Sets Fields
     * @throws IllegalStateException
     * When Extras Fail to Validate
     * @param savedInstanceState
     * Bundle with Extras Set
     */
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
        Task task = persistence.getTask(id);//Re-retrieve information in case account is deleted

        if (task == null)
            throw new IllegalStateException("Task With ID " + id + " Not Found"); //ID doesn't match task

        // Get Input References
        taskName = (EditText) findViewById(R.id.inputTask);
        imageView = (ImageView) findViewById(R.id.imgTask);
        if (task.getImage() != null)
            imageView.setImageBitmap(task.getImage());

        // Set Current Values
        taskName.setText(task.getName());
    }

    /**
     * Checks If Required Field Is Empty Before For Saving
     * @param v
     * Current View
     */
    public void onSave(View v) {
        PersistenceInteractor persistence = new PersistenceInteractor(this);
        Task task = persistence.getTask(id);
        if (task == null) {
            Toast.makeText(this, "Error Task With ID " + id + " Not Found", Toast.LENGTH_LONG).show();
            return;
        }

        if (taskName.getText().toString().trim().isEmpty()) {
            taskName.setError("Name Is Required");
            return;
        }

        if (image != null)
            task.setImage(image);

        persistence.update(task);
        finish(); //Ends activity
    }

    /**
     * Bring Up A Media Picker To Choose A Picture
     * If Not Already Granted, Requests WRITE_EXTERNAL_STORAGE Permission
     * Reenters onActivityResult With Result RESULT_LOAD_IMAGE
     * @param v
     * Current View, May Be Null
     */
    public void onSelectImage(View v) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_REQUEST);
            return;
        }
        Intent mediaIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(mediaIntent, RESULT_LOAD_IMAGE);
    }

    /**
     * Reentrant Point For onSelectImage
     * Enters With RESULT_LOAD_IMAGE
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImage = data.getData();
            String filePathColumn[] = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            String path = "";

            if (cursor == null)
                return;

            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(filePathColumn[0]));
            }
            cursor.close();

            if (path.isEmpty())
                return;

            image = BitmapFactory.decodeFile(path);
            if (image == null)
                return;

            imageView.setImageBitmap(image);
        }
    }

    /**
     * Reentrant Point If Permission Must Be Requested
     * Enters With EXTERNAL_STORAGE_REQUEST
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case EXTERNAL_STORAGE_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    onSelectImage(null);
                }
                break;
        }
    }

    /**
     * Deletes Task
     * @param v
     * Current View
     */
    public void onDelete(View v) {
        PersistenceInteractor persistence = new PersistenceInteractor(this);
        persistence.deleteTask(id);
        finish();
    }

}
