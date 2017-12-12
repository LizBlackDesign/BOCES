/*
 * BOCES
 *
 * Authors: Evan Black, Elizabeth Stanton
 */
package com.boces.black_stanton_boces;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.boces.black_stanton_boces.persistence.PersistenceInteractor;

/**
 * Collects Account Information From User And Creates New Admin Account
 */
public class AdminAddAccountActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private EditText confirmPassword;

    /**
     * Gathers Input References
     * @param savedInstanceState
     * Unused/Null
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_account);

        username = (EditText) findViewById(R.id.inputUsername);
        password = (EditText) findViewById(R.id.inputPassword);
        confirmPassword = (EditText) findViewById(R.id.inputConfirmPassword);
    }

    /**
     * Checks If Required Field Is Empty Before For Saving
     * @param v
     * Current View
     */
    public void onSave(View v) {
        PersistenceInteractor persistence = new PersistenceInteractor(this);
        boolean hasError = false;

        if (username.getText().toString().isEmpty()) {
            username.setError("Username Is Required");
            hasError = true;
        }
        if (password.getText().toString().isEmpty()) {
            hasError = true;
            password.setError("Password Is Required");
        }

        if (confirmPassword.getText().toString().isEmpty()) {
            hasError = true;
            confirmPassword.setError("Password Must Be Confirmed");
        }

        //Checks both passwords to assure they match
        // Also Make Sure One Isn't Empty
        if (!password.getText().toString().equals(confirmPassword.getText().toString()) && !password.getText().toString().isEmpty()) {
            hasError = true;
            confirmPassword.setError("Passwords Must Match");
        }

        // Stop Runs With Errors
        if (hasError)
            return;

        persistence.createAdminAccount(username.getText().toString(), password.getText().toString());

        finish(); //Ends activity
    }
}
