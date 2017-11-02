package com.boces.black_stanton_boces;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class LoginTypeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_type);

    }
    //Open Student Login Process (Teacher Select)
    public void onClickStudentLogin(View v)
    {
        startActivity(new Intent( this, StudentLoginSelectTeacherActivity.class));
    }

    //Open Admin Login Process (Admin Login)
    private void onClickAdminLogin(View v)
    {
        startActivity(new Intent(this, AdminLoginActivity.class));
    }


}
