package com.example.stockvault;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class Settings extends AppCompatActivity
{

    Button settings_j_btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);

        settings_j_btn_back = findViewById(R.id.settings_v_btn_back);
        settings_j_btn_back.setOnClickListener(new View.OnClickListener()
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
                        // need new intents here to adapt based on the bundle string passed
                        Intent intent = new Intent(Settings.this, Dashboard.class);
                        startActivity(intent);
                        // play the animation too
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    }
                    else if (source.equals("AddItems"))
                    {
                        Intent intent = new Intent(Settings.this, AddItems.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    }
                    else if (source.equals("EditItems"))
                    {
                        Intent intent = new Intent(Settings.this, EditItems.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    }
                }
            }
        });
    }
}
