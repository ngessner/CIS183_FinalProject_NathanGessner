package com.example.stockvault;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class Dashboard extends AppCompatActivity
{
    BottomNavigationView home_j_bnv_navigation;
    ArrayList<Product> productList;
    ArrayList<Material> materialList;

    ListView dashboard_j_lv_items;
    ItemCustomAdapter adapter;
    TabLayout dashitems_j_tl_tabs;
    DatabaseHelper db_j_helper;

    TextView product_j_tv_counter;
    TextView material_j_tv_counter;
    TextView product_j_tv_title;
    TextView product_j_tv_description;
    TextView product_j_tv_price;
    TextView product_j_tv_stock;
    TextView material_j_tv_title;
    TextView material_j_tv_name;
    TextView material_j_tv_description;
    TextView material_j_tv_unit;
    TextView material_j_tv_quantity;

    View product_j_v_container;
    View material_j_v_container;

    Button product_j_btn_exit;
    Button material_j_btn_exit;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        home_j_bnv_navigation = findViewById(R.id.view_v_bnv_bottom_navigation);
        home_j_bnv_navigation.setSelectedItemId(R.id.bottom_home);

        dashboard_j_lv_items = findViewById(R.id.dashboard_v_lv_items);
        dashitems_j_tl_tabs = findViewById(R.id.dashItems_v_tl_tabs);

        product_j_tv_counter = findViewById(R.id.dash_v_tv_subHeader4);
        material_j_tv_counter = findViewById(R.id.dash_v_tv_subHeader5);

        // for containers
        product_j_v_container = findViewById(R.id.addItems_v_ll_container);
        material_j_v_container = findViewById(R.id.additems_v_ll_material_container);

        product_j_tv_title = findViewById(R.id.dash_v_tv_product);
        product_j_tv_description = findViewById(R.id.dash_v_tv_prod_desc);
        product_j_tv_price = findViewById(R.id.dash_v_tv_price);
        product_j_tv_stock = findViewById(R.id.dash_v_tv_stockAmt);

        material_j_tv_title = findViewById(R.id.additems_v_tv_material_title);
        material_j_tv_name = findViewById(R.id.dash_v_tv_mat_name);
        material_j_tv_description = findViewById(R.id.dash_v_tv_mat_desc);
        material_j_tv_unit = findViewById(R.id.dash_v_tv_unit);
        material_j_tv_quantity = findViewById(R.id.dash_v_tv_mat_stockamt);

        product_j_btn_exit = findViewById(R.id.additems_v_btn_exit);
        material_j_btn_exit = findViewById(R.id.additems_v_btn_mat_exit);

        db_j_helper = new DatabaseHelper(this);

        home_j_bnv_navigation.setOnItemSelectedListener(item ->
        {
            int id = item.getItemId();
            if (id == R.id.bottom_home)
            {
                return true;
            }
            else if (id == R.id.bottom_view)
            {
                startActivity(new Intent(getApplicationContext(), EditItems.class).putExtra("source", "Dashboard"));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }
            else if (id == R.id.bottom_add)
            {
                startActivity(new Intent(getApplicationContext(), AddItems.class).putExtra("source", "Dashboard"));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }
            else if (id == R.id.bottom_settings)
            {
                startActivity(new Intent(getApplicationContext(), Settings.class).putExtra("source", "Dashboard"));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }
            else if (id == R.id.bottom_profile)
            {
                startActivity(new Intent(getApplicationContext(), Profile.class).putExtra("source", "Dashboard"));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }
            return false;
        });

        // load data from database
        productList = db_j_helper.getProductsFromDB();
        materialList = db_j_helper.getMaterialsFromDB();

        // update counters
        updateCounters();

        // default to products tab
        adapter = new ItemCustomAdapter(this, new ArrayList<>());
        dashboard_j_lv_items.setAdapter(adapter);
        showProducts();

        // listener for tablayout tabs
        dashitems_j_tl_tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener()
        {
            @Override
            public void onTabSelected(TabLayout.Tab tab)
            {
                if (tab.getPosition() == 0)
                {
                    showProducts();
                }
                else if (tab.getPosition() == 1)
                {
                    showMaterials();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        // listener for clicking listview items
        dashboard_j_lv_items.setOnItemClickListener((parent, view, position, id) ->
        {
            if (!adapter.isEmpty())
            {
                Object clickedItem = adapter.getItem(position);
                if (clickedItem instanceof Product)
                {
                    Product clickedProduct = (Product) clickedItem;
                    showProductDetails(clickedProduct);
                }
                else if (clickedItem instanceof Material)
                {
                    Material clickedMaterial = (Material) clickedItem;
                    showMaterialDetails(clickedMaterial);
                }
            }
        });

        // set up exit button listeners
        setupExitButtons();
    }

    private void showProducts()
    {
        product_j_v_container.setVisibility(View.GONE);
        material_j_v_container.setVisibility(View.GONE);
        adapter.clear();
        adapter.addAll(productList);
        adapter.notifyDataSetChanged();
    }

    private void showMaterials()
    {
        product_j_v_container.setVisibility(View.GONE);
        material_j_v_container.setVisibility(View.GONE);
        adapter.clear();
        adapter.addAll(materialList);
        adapter.notifyDataSetChanged();
    }

    private void updateCounters()
    {
        product_j_tv_counter.setText(String.valueOf(productList.size()));
        material_j_tv_counter.setText(String.valueOf(materialList.size()));
    }

    private void showProductDetails(Product product)
    {
        product_j_v_container.setVisibility(View.VISIBLE);
        material_j_v_container.setVisibility(View.GONE);

        product_j_tv_title.setText("Product Name: " + product.getProductName());
        product_j_tv_description.setText("Description: " + product.getDescription());
        product_j_tv_price.setText("Price: $" + product.getPrice());
        product_j_tv_stock.setText("Stock: " + product.getStock());
    }

    private void showMaterialDetails(Material material)
    {
        product_j_v_container.setVisibility(View.GONE);
        material_j_v_container.setVisibility(View.VISIBLE);

        material_j_tv_name.setText("Material Name: " + material.getMaterialName());
        material_j_tv_description.setText("Description: " + material.getDescription());
        material_j_tv_unit.setText("Unit: " + db_j_helper.getUnitNameById(material.getUnitId()));
        material_j_tv_quantity.setText("Quantity: " + material.getUnitAmount());
    }

    private void setupExitButtons()
    {
        product_j_btn_exit.setOnClickListener(v -> product_j_v_container.setVisibility(View.GONE));
        material_j_btn_exit.setOnClickListener(v -> material_j_v_container.setVisibility(View.GONE));
    }
}
