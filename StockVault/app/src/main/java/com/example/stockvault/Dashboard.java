package com.example.stockvault;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Dashboard extends AppCompatActivity {

    BottomNavigationView home_j_bnv_bottom_navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        home_j_bnv_bottom_navigation = findViewById(R.id.home_v_bnv_bottom_navigation);
        home_j_bnv_bottom_navigation.setSelectedItemId(R.id.bottom_home);

                home_j_bnv_bottom_navigation.setOnItemSelectedListener(item ->
                {
                    int id = item.getItemId();
                    if (id == R.id.bottom_home)
                    {
                        return true;
                    }
                    else if (id == R.id.bottom_view)
                    {
                        startActivity(new Intent(getApplicationContext(), EditItems.class));
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        finish();
                        return true;
                    }
                    else if (id == R.id.bottom_add)
                    {
                        startActivity(new Intent(getApplicationContext(), AddItems.class));
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        finish();
                        return true;
                    }
                    else if (id == R.id.bottom_settings)
                    {
                        startActivity(new Intent(getApplicationContext(), Settings.class));
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        finish();
                        return true;
                    }
                    else if (id == R.id.bottom_profile)
                    {
                        startActivity(new Intent(getApplicationContext(), Profile.class));
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        finish();
                        return true;
                    }
                    return false;
                });

    }
}
