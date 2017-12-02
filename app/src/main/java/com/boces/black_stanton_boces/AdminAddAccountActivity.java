package com.boces.black_stanton_boces;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.boces.black_stanton_boces.persistence.PersistenceInteractor;
import com.boces.black_stanton_boces.persistence.model.AdminAccount;

public class AdminAddAccountActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private EditText confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_account);

        username = (EditText) findViewById(R.id.inputUsername);
        password = (EditText) findViewById(R.id.inputPassword);
        confirmPassword = (EditText) findViewById(R.id.inputConfirmPassword);
    }

    public void onSave(View v) {
        PersistenceInteractor persistence = new PersistenceInteractor(this);

        if (username.getText().toString().isEmpty())
            return;

        if (password.getText().toString().isEmpty())
            return;
        if (confirmPassword.getText().toString().isEmpty())
            return;

        persistence.createAdminAccount(username.getText().toString(), password.getText().toString());
        finish();
    }
}
