package com.example.stockvault;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class Profile extends AppCompatActivity
{
    Button profile_j_btn_back;

    // textviews for displaying user information
    TextView profile_j_tv_firstname;
    TextView profile_j_tv_lastname;
    TextView profile_j_tv_username;
    TextView profile_j_tv_email;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        // bind textviews
        profile_j_tv_firstname = findViewById(R.id.prof_v_tv_firstname);
        profile_j_tv_lastname = findViewById(R.id.prof_v_tv_lastname);
        profile_j_tv_username = findViewById(R.id.prof_v_tv_username);
        profile_j_tv_email = findViewById(R.id.prof_v_tv_email);

        // load user information
        loadUserProfile();

        profile_j_btn_back = findViewById(R.id.prof_v_btn_back);
        profile_j_btn_back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // determine where to navigate back to based on the source
                String source = getIntent().getStringExtra("source");
                if (source != null)
                {
                    if (source.equals("Dashboard"))
                    {
                        navigateTo(Dashboard.class);
                    }
                    else if (source.equals("AddItems"))
                    {
                        navigateTo(AddItems.class);
                    }
                    else if (source.equals("EditItems"))
                    {
                        navigateTo(EditItems.class);
                    }
                }
            }
        });
    }

    private void loadUserProfile()
    {
        // get the logged-in user data from the sessiondata or database
        User loggedInUser = SessionData.getLoggedInUser();

        if (loggedInUser != null)
        {
            // set user information to the corresponding textviews
            profile_j_tv_firstname.setText("first name: " + loggedInUser.getFirstName());
            profile_j_tv_lastname.setText("last name: " + loggedInUser.getLastName());
            profile_j_tv_username.setText("username: " + loggedInUser.getUsername());
            profile_j_tv_email.setText("email: " + loggedInUser.getEmail());
        }
        else
        {
            // if no user is logged in, set default values or display an error
            // this scenario shouldn't really happen, but its just a type case really.
            profile_j_tv_firstname.setText("first name: n/a");
            profile_j_tv_lastname.setText("last name: n/a");
            profile_j_tv_username.setText("username: n/a");
            profile_j_tv_email.setText("email: n/a");
        }
    }

    private void navigateTo(Class<?> targetActivity)
    {
        Intent intent = new Intent(Profile.this, targetActivity);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
