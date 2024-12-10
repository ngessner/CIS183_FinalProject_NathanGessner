package com.example.stockvault;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

public class AddItems extends AppCompatActivity {

    BottomNavigationView home_j_bnv_navigation;
    TabLayout addItems_j_tl_tabs;

    LinearLayout product_j_ll_layout;
    LinearLayout material_j_ll_layout;

    Button product_j_btn_save;
    Button material_j_btn_save;

    EditText product_j_et_name;
    EditText product_j_et_description;
    EditText product_j_et_price;
    EditText product_j_et_stock;

    EditText material_j_et_name;
    EditText material_j_et_description;
    EditText material_j_et_quantity;

    Spinner unit_j_sp_spinner;

    DatabaseHelper db_j_helper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_items);

        db_j_helper = new DatabaseHelper(this);

        home_j_bnv_navigation = findViewById(R.id.view_v_bnv_bottom_navigation);
        addItems_j_tl_tabs = findViewById(R.id.additems_v_tl_tabs);

        product_j_ll_layout = findViewById(R.id.addItems_v_ll_container);
        material_j_ll_layout = findViewById(R.id.additems_v_ll_material_container);

        product_j_btn_save = findViewById(R.id.additems_v_btn_save);
        material_j_btn_save = findViewById(R.id.additems_v_btn_mat_exit);

        product_j_et_name = findViewById(R.id.additems_v_et_product_name);
        product_j_et_description = findViewById(R.id.additems_v_et_product_desc);
        product_j_et_price = findViewById(R.id.additems_v_et_product_price);
        product_j_et_stock = findViewById(R.id.additems_v_et_product_stock);

        material_j_et_name = findViewById(R.id.additems_v_et_material_name);
        material_j_et_description = findViewById(R.id.additems_v_et_material_desc);
        material_j_et_quantity = findViewById(R.id.additems_v_et_material_quantity);

        unit_j_sp_spinner = findViewById(R.id.spinner);

        populateUnitSpinner();

        // initially show the product layout and hide the material layout
        product_j_ll_layout.setVisibility(View.VISIBLE);
        material_j_ll_layout.setVisibility(View.GONE);

        // tab layout listener to toggle visibility
        addItems_j_tl_tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) // products tab
                {
                    product_j_ll_layout.setVisibility(View.VISIBLE);
                    material_j_ll_layout.setVisibility(View.GONE);
                } else if (tab.getPosition() == 1) // materials tab
                {
                    product_j_ll_layout.setVisibility(View.GONE);
                    material_j_ll_layout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        // save button for products
        product_j_btn_save.setOnClickListener(v ->
        {
            String name = product_j_et_name.getText().toString();
            String desc = product_j_et_description.getText().toString();
            String price = product_j_et_price.getText().toString();
            String stock = product_j_et_stock.getText().toString();

            if (name.isEmpty() || price.isEmpty() || stock.isEmpty())
            {
                Toast.makeText(this, "please fill in all required fields for product.", Toast.LENGTH_SHORT).show();
            }
            else
            {
                try
                {
                    db_j_helper.addProduct(name, desc, Double.parseDouble(price), Integer.parseInt(stock));
                    Toast.makeText(this, "product added successfully!", Toast.LENGTH_SHORT).show();
                    clearProductFields();
                }
                catch (Exception e)
                {
                    Toast.makeText(this, "error adding product: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // save button for materials
        material_j_btn_save.setOnClickListener(v ->
        {
            saveMaterialChanges();
        });

        // bottom navigation setup
        home_j_bnv_navigation.setSelectedItemId(R.id.bottom_add);

        home_j_bnv_navigation.setOnItemSelectedListener(item ->
        {
            int id = item.getItemId();
            if (id == R.id.bottom_home)
            {
                startActivity(new Intent(getApplicationContext(), Dashboard.class).putExtra("source", "AddItems"));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                return true;
            }
            else if (id == R.id.bottom_view)
            {
                startActivity(new Intent(getApplicationContext(), EditItems.class).putExtra("source", "AddItems"));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                return true;
            }
            else if (id == R.id.bottom_add)
            {
                return true;
            }
            else if (id == R.id.bottom_settings)
            {
                startActivity(new Intent(getApplicationContext(), Settings.class).putExtra("source", "AddItems"));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                return true;
            }
            else if (id == R.id.bottom_profile)
            {
                startActivity(new Intent(getApplicationContext(), Profile.class).putExtra("source", "AddItems"));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                return true;
            }
            return false;
        });
    }

    private void saveMaterialChanges()
    {
        String name = material_j_et_name.getText().toString();
        String desc = material_j_et_description.getText().toString();
        String quantity = material_j_et_quantity.getText().toString();
        String unit = unit_j_sp_spinner.getSelectedItem().toString();

        if (name.isEmpty() || quantity.isEmpty() || unit.isEmpty())
        {
            Toast.makeText(this, "please fill in all fields for material", Toast.LENGTH_SHORT).show();
            return;
        }

        try
        {
            // get the unitid for the selected unit
            int unitId = db_j_helper.getUnitId(unit);
            if (unitId == -1) {
                Toast.makeText(this, "invalid unit selected.", Toast.LENGTH_SHORT).show();
                return;
            }

            // parse the quantity to a double
            double unitAmount = Double.parseDouble(quantity);

            // add material to database
            db_j_helper.addMaterial(name, unitId, unitAmount, desc);

            Toast.makeText(this, "material added", Toast.LENGTH_SHORT).show();
            clearMaterialFields();
        }
        catch (Exception e)
        {
            Toast.makeText(this, "error adding material" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void populateUnitSpinner() {
        List<String> units = db_j_helper.getUnitTableNames();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, units);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unit_j_sp_spinner.setAdapter(adapter);
    }

    private void clearProductFields() {
        product_j_et_name.setText("");
        product_j_et_description.setText("");
        product_j_et_price.setText("");
        product_j_et_stock.setText("");
    }

    private void clearMaterialFields() {
        material_j_et_name.setText("");
        material_j_et_description.setText("");
        material_j_et_quantity.setText("");
        unit_j_sp_spinner.setSelection(0);
    }
}
