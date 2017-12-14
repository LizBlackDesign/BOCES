/*
 * BOCES
 *
 * Authors: Evan Black, Elizabeth Stanton
 */
package com.boces.black_stanton_boces.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.boces.black_stanton_boces.R;
import com.boces.black_stanton_boces.persistence.PersistenceInteractor;
import com.boces.black_stanton_boces.persistence.model.AdminAccount;

/**
 * Guards Admin Activities and Verifies Login Information
 */
public class AdminLoginActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;

    /**
     * Gathers Input References
     * @param savedInstanceState
     * Bundle with Extras Set - Unused
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        username = findViewById(R.id.inputAdminUserName);
        password = findViewById(R.id.inputAdminPassword);

        // Allows User To Use Check Mark On Keyboard To Submit
        password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    onClickAdminLoginValidate(null);
                    return true;
                }
                return false;
            }
        });
    }


    /**
     * Validates Login Info & Moves To Admin Menu
     *
     * @param v
     * Unused/May Be null
     */
    public void onClickAdminLoginValidate(View v) {
        PersistenceInteractor persistence = new PersistenceInteractor(this);

        // If Username Does Not Match, Then found Is null
        AdminAccount found = persistence.getAdminAccount(username.getText().toString());

        // Makes Sure Account Exists And Password Matches
        if (found != null && persistence.checkPassword(password.getText().toString(), found.getPassword())) {
            startActivity(new Intent(this, AdminMenuActivity.class));
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("Invalid Login")
                    .setMessage("Username/Password Is Incorrect")
                    .setCancelable(true)
                    .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .show();
        }
    }
}
