package com.example.stockvault;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity
{
    Button loginact_j_btn_login;
    TextView loginact_j_tv_signup_prompt;
    EditText loginact_j_et_username;
    EditText loginact_j_et_password;

    DatabaseHelper db_j_helper;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db_j_helper = new DatabaseHelper(this);

        loginact_j_btn_login = findViewById(R.id.loginact_v_btn_login);
        loginact_j_tv_signup_prompt = findViewById(R.id.loginact_v_tv_signup_prompt5);
        loginact_j_et_username = findViewById(R.id.loginact_v_et_email5); // adjust the id as necessary
        loginact_j_et_password = findViewById(R.id.loginact_v_et_password);

        // login button logic
        loginact_j_btn_login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String username = loginact_j_et_username.getText().toString().trim();
                String password = loginact_j_et_password.getText().toString().trim();

                if (username.isEmpty() || password.isEmpty())
                {
                    Toast.makeText(Login.this, "please enter both username and password", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    boolean isValid = db_j_helper.checkUserExist(username, password);
                    if (isValid)
                    {
                        // fetch the logged-in user's data
                        User loggedInUser = db_j_helper.getUserByUsername(username);
                        if (loggedInUser != null)
                        {
                            // store the logged-in user in sessiondata
                            SessionData.setLoggedInUser(loggedInUser);

                            Toast.makeText(Login.this, "login successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Login.this, Dashboard.class);
                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                            Toast.makeText(Login.this, "error fetching user details", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(Login.this, "invalid username or password", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // redirect to registration screen
        loginact_j_tv_signup_prompt.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Login.this, Registration.class);
                startActivity(intent);
            }
        });
    }
}
