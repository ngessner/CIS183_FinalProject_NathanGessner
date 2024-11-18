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

        // https://www.youtube.com/watch?v=MUl19ppdu0o&t=785s found a nice tutoiral series on the navigation thing.
        home_j_bnv_bottom_navigation.setOnItemSelectedListener(item ->
        {
            switch (item.getItemId()) {
                case R.id.bottom_home:
                    return true;

                case R.id.bottom_view:
                    startActivity(new Intent(getApplicationContext(), ViewItemsView.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;

                case R.id.bottom_add:
                    startActivity(new Intent(getApplicationContext(), AddItemsView.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;

                case R.id.bottom_settings:
                    startActivity(new Intent(getApplicationContext(), SettingsView.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;

                case R.id.bottom_profile:
                    startActivity(new Intent(getApplicationContext(), ProfileView.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;

                default:
                    return false;
            }
        });
    }
}
