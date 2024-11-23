package com.example.stockvault;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class Profile extends AppCompatActivity
{
    Button profile_j_btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        profile_j_btn_back = findViewById(R.id.prof_v_btn_back);
        profile_j_btn_back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // source system for knowing which intent to return too, those bundled strings sent from
                // the intents earlier land here, based on that info, we know where we came from and technically
                // where to return also.
                String source = getIntent().getStringExtra("source");
                if (source != null)
                {
                    if (source.equals("Dashboard"))
                    {
                        Intent intent = new Intent(Profile.this, Dashboard.class);
                        startActivity(intent);
                        // play animation
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    }
                    else if (source.equals("AddItems"))
                    {
                        Intent intent = new Intent(Profile.this, AddItems.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    }
                    else if (source.equals("EditItems"))
                    {
                        Intent intent = new Intent(Profile.this, EditItems.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    }
                }
            }
        });
    }
}
