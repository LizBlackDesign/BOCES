package com.boces.black_stanton_boces;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.boces.black_stanton_boces.persistence.PersistenceInteractor;
import com.boces.black_stanton_boces.persistence.model.AdminAccount;

public class AdminEditAccountActivity extends AppCompatActivity {

    private int id;
    private EditText username;
    private EditText password;
    private EditText confirmPassword;

    /**
     * Recognised Values That May Be Passed Through Bundles
     */
    public enum BUNDLE_KEY {
        ACCOUNT_ID
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_account);
        Bundle extras = getIntent().getExtras();

        // Painfully Validate That We Got Something
        if (extras == null)
            throw new IllegalStateException("No Data Passed To Edit");
        id = extras.getInt(BUNDLE_KEY.ACCOUNT_ID.name());
        if (id == 0)
            throw new IllegalStateException("Account ID Not Passed To Edit");

        PersistenceInteractor persistence = new PersistenceInteractor(this);
        AdminAccount account = persistence.getAdminAccount(id);
        if (account == null)
            throw new IllegalStateException("Account With ID " + id + " Not Found");

        // Get Input References
        username = (EditText) findViewById(R.id.inputUsername);
        password = (EditText) findViewById(R.id.inputPassword);
        confirmPassword = (EditText) findViewById(R.id.inputConfirmPassword);

        username.setText(account.getUsername());
    }

    public void onSave(View v) {
        PersistenceInteractor persistence = new PersistenceInteractor(this);
        AdminAccount account = persistence.getAdminAccount(id);

        if (account == null) {
            Toast.makeText(this, "Error Account With ID " + id + " Not Found", Toast.LENGTH_LONG).show();
            return;
        }

        if (username.getText().toString().isEmpty())
            return;

        // I Hate Java
        if (!account.getUsername().equals(username.getText().toString())) {
            account.setUsername(username.getText().toString());
            persistence.updateUsername(account);
        }

        // If A New Password Was Not Entered, Then  We're Done
        if (password.getText().toString().isEmpty() && confirmPassword.getText().toString().isEmpty()) {
            finish();
            return;
        }

        if (password.getText().toString().equals(confirmPassword.getText().toString()))
            persistence.updateAdminPassword(id, password.getText().toString());

        finish();
    }
}
