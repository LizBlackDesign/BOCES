package com.boces.black_stanton_boces;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.boces.black_stanton_boces.persistence.PersistenceInteractor;
import com.boces.black_stanton_boces.persistence.model.AdminAccount;

import java.util.ArrayList;

public class AdminLoginActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        username = findViewById(R.id.inputAdminUserName);
        password = findViewById(R.id.inputAdminPassword);

    }

    //Open Admin Menu
    public void onClickAdminLoginValidate(View v) {
        PersistenceInteractor persistence = new PersistenceInteractor(this);

        AdminAccount found = persistence.getAdminAccount(username.getText().toString());
        if (found == null) {
            return;
        }

        if (persistence.checkPassword(password.getText().toString(), found.getPassword()))
            startActivity(new Intent(this, AdminMenuActivity.class));
    }

}
