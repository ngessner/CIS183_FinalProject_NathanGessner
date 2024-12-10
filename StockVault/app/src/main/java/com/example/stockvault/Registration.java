package com.example.stockvault;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class Registration extends AppCompatActivity
{
    EditText regact_j_et_username;
    EditText regact_j_et_firstname;
    EditText regact_j_et_lastname;
    EditText regact_j_et_email;
    EditText regact_j_et_password;
    EditText regact_j_et_confirmpassword;

    Button regact_j_btn_signup;
    TextView regact_j_tv_signin;

    DatabaseHelper db_j_helper;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registration);

        db_j_helper = new DatabaseHelper(this);

        regact_j_et_username = findViewById(R.id.regact_v_et_username);
        regact_j_et_firstname = findViewById(R.id.regact_v_et_first_name); // add first name field
        regact_j_et_lastname = findViewById(R.id.regact_v_et_last_name);   // add last name field
        regact_j_et_email = findViewById(R.id.regact_v_et_email);
        regact_j_et_password = findViewById(R.id.regact_v_et_password);
        regact_j_et_confirmpassword = findViewById(R.id.regact_v_et_confirm_password);

        regact_j_btn_signup = findViewById(R.id.regact_v_btn_sign_up);
        regact_j_tv_signin = findViewById(R.id.regact_v_tv_sign_in);

        // set up sign-up button click listener
        regact_j_btn_signup.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                registerUser();
            }
        });

        // set up sign-in button click listener
        regact_j_tv_signin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // navigate to login screen
                Intent intent = new Intent(Registration.this, Login.class);
                startActivity(intent);
                finish(); // finish the current activity so it doesn't remain in the back stack
            }
        });
    }

    private void registerUser()
    {
        String username = regact_j_et_username.getText().toString().trim();
        String firstname = regact_j_et_firstname.getText().toString().trim();
        String lastname = regact_j_et_lastname.getText().toString().trim();
        String email = regact_j_et_email.getText().toString().trim();
        String password = regact_j_et_password.getText().toString().trim();
        String confirmpassword = regact_j_et_confirmpassword.getText().toString().trim();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(firstname) || TextUtils.isEmpty(lastname)
                || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmpassword))
        {
            Toast.makeText(this, "please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            Toast.makeText(this, "invalid email address", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmpassword))
        {
            Toast.makeText(this, "passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6)
        {
            Toast.makeText(this, "password must be at least 6 characters long", Toast.LENGTH_SHORT).show();
            return;
        }

        if (db_j_helper.checkUserExist(username, password))
        {
            Toast.makeText(this, "username already exists", Toast.LENGTH_SHORT).show();
            return;
        }

        long result = db_j_helper.addUser(username, firstname, lastname, email, password);

        if (result != -1)
        {
            Toast.makeText(this, "registration successful", Toast.LENGTH_SHORT).show();

            // store the new user in sessiondata
            User newUser = new User((int) result, username, firstname, lastname, email, password);
            SessionData.setLoggedInUser(newUser);

            // navigate to dashboard after successful registration
            Intent intent = new Intent(Registration.this, Dashboard.class);
            startActivity(intent);
            finish();
        }
        else
        {
            Toast.makeText(this, "registration failed try again", Toast.LENGTH_SHORT).show();
        }
    }
}
