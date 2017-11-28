package com.boces.black_stanton_boces;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.boces.black_stanton_boces.persistence.PersistenceInteractor;
import com.boces.black_stanton_boces.persistence.model.Task;

public class AdminAddTaskActivity extends AppCompatActivity {

    private EditText inputTaskName;
    private ImageView imageView;
    private Bitmap image;
    private PersistenceInteractor persistence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_task);

        // Get Input References
        inputTaskName = (EditText) findViewById(R.id.inputTask);
        imageView = (ImageView) findViewById(R.id.imgTask);

        persistence = new PersistenceInteractor(this);
    }

    public void onClickAdminTaskAddSave(View v) {
        Task task = new Task();
        task.setName(inputTaskName.getText().toString());
        if (image != null)
            task.setImage(image);

        persistence.addTask(task);
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

    //Opens Task Manager(back one screen)
    public void onClickAdminTasksAddBack(View v) {
        finish();
    }
}
