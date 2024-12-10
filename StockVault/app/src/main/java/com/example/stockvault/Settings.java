package com.example.stockvault;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class Settings extends AppCompatActivity
{
    Button settings_j_btn_back;
    Button settings_j_btn_save;

    EditText settings_j_et_firstname;
    EditText settings_j_et_lastname;
    EditText settings_j_et_username;
    EditText settings_j_et_email;
    EditText settings_j_et_password;
    EditText settings_j_et_confirmpass;

    DatabaseHelper db_j_helper;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);

        // bind ui elements
        settings_j_et_firstname = findViewById(R.id.settings_v_et_firstname);
        settings_j_et_lastname = findViewById(R.id.settings_v_et_lastname);
        settings_j_et_username = findViewById(R.id.settings_v_et_username);
        settings_j_et_email = findViewById(R.id.settings_v_et_email);
        settings_j_et_password = findViewById(R.id.settings_v_et_pass);
        settings_j_et_confirmpass = findViewById(R.id.settings_v_et_confirmPass);

        settings_j_btn_back = findViewById(R.id.settings_v_btn_back);
        settings_j_btn_save = findViewById(R.id.settings_v_btn_save);

        db_j_helper = new DatabaseHelper(this);

        // load user data into the fields
        loadUserSettings();

        // save button listener
        settings_j_btn_save.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                saveUserSettings();
            }
        });

        // back button listener
        settings_j_btn_back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String source = getIntent().getStringExtra("source");
                if (source != null)
                {
                    navigateToSource(source);
                }
            }
        });
    }

    private void loadUserSettings()
    {
        User loggedInUser = SessionData.getLoggedInUser();
        if (loggedInUser != null)
        {
            settings_j_et_firstname.setText(loggedInUser.getFirstName());
            settings_j_et_lastname.setText(loggedInUser.getLastName());
            settings_j_et_username.setText(loggedInUser.getUsername());
            settings_j_et_email.setText(loggedInUser.getEmail());
        }
        else
        {
            Toast.makeText(this, "error loading user data", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveUserSettings()
    {
        String firstname = settings_j_et_firstname.getText().toString().trim();
        String lastname = settings_j_et_lastname.getText().toString().trim();
        String username = settings_j_et_username.getText().toString().trim();
        String email = settings_j_et_email.getText().toString().trim();
        String password = settings_j_et_password.getText().toString().trim();
        String confirmpass = settings_j_et_confirmpass.getText().toString().trim();

        if (firstname.isEmpty() || lastname.isEmpty() || username.isEmpty() || email.isEmpty())
        {
            Toast.makeText(this, "please fill in all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.isEmpty() && !password.equals(confirmpass))
        {
            Toast.makeText(this, "passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        User loggedInUser = SessionData.getLoggedInUser();
        if (loggedInUser != null)
        {
            boolean updateResult = db_j_helper.updateUser(
                    loggedInUser.getUserId(),
                    firstname,
                    lastname,
                    username,
                    email,
                    password.isEmpty() ? loggedInUser.getPassword() : password // keep old password if none entered
            );

            if (updateResult)
            {
                Toast.makeText(this, "settings updated successfully", Toast.LENGTH_SHORT).show();
                // refresh session data
                loggedInUser.setFirstName(firstname);
                loggedInUser.setLastName(lastname);
                loggedInUser.setUsername(username);
                loggedInUser.setEmail(email);
                if (!password.isEmpty())
                {
                    loggedInUser.setPassword(password);
                }
            }
            else
            {
                Toast.makeText(this, "failed to update settings", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(this, "user not logged in", Toast.LENGTH_SHORT).show();
        }
    }

    private void navigateToSource(String source)
    {
        Intent intent = null;
        if (source.equals("Dashboard"))
        {
            intent = new Intent(Settings.this, Dashboard.class);
        }
        else if (source.equals("AddItems"))
        {
            intent = new Intent(Settings.this, AddItems.class);
        }
        else if (source.equals("EditItems"))
        {
            intent = new Intent(Settings.this, EditItems.class);
        }

        if (intent != null)
        {
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
    }
}
