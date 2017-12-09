/*
 * BOCES
 *
 * Authors: Evan Black, Elizabeth Stanton
 */
package com.boces.black_stanton_boces;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

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
    }

    /**
     * Brings in Extras, Validates, Sets Fields
     * @throws IllegalStateException
     * When Extras Fail to Validate
     * @param v
     * Current View
     */
    public void onClickAdminLoginValidate(View v) {
        PersistenceInteractor persistence = new PersistenceInteractor(this);

        AdminAccount found = persistence.getAdminAccount(username.getText().toString());
        if (found == null)
            return;

        if (persistence.checkPassword(password.getText().toString(), found.getPassword()))
            startActivity(new Intent(this, AdminMenuActivity.class));
    }
}
