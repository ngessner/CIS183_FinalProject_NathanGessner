package com.example.stockvault;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class EditItems extends AppCompatActivity {
    BottomNavigationView home_j_bnv_navigation;
    ListView edititems_j_lv_items;

    ItemCustomAdapter adapter;

    TabLayout edititems_j_tl_tabs;

    LinearLayout product_j_ll_layout;
    LinearLayout material_j_ll_layout;
    LinearLayout addunit_j_ll_layout;

    Button product_j_btn_save;
    Button material_j_btn_save;
    Button saveunit_j_btn_save;
    Button product_j_btn_delete;
    Button material_j_btn_delete;

    EditText product_j_et_name;
    EditText product_j_et_description;
    EditText product_j_et_price;
    EditText product_j_et_stock;

    EditText material_j_et_name;
    EditText material_j_et_description;
    EditText material_j_et_quantity;
    EditText unit_j_et_nameinput;

    Spinner unit_j_sp_spinner;

    TextView addunit_j_tv_button;

    ArrayList<Product> productList;
    ArrayList<Material> materialList;

    DatabaseHelper db_j_helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_items);

        db_j_helper = new DatabaseHelper(this);

        // bind ui elements
        home_j_bnv_navigation = findViewById(R.id.view_v_bnv_bottom_navigation);
        edititems_j_lv_items = findViewById(R.id.editItems_v_lv_items);
        edititems_j_tl_tabs = findViewById(R.id.editItems_v_tl_tabs);

        product_j_ll_layout = findViewById(R.id.addItems_v_ll_container);
        material_j_ll_layout = findViewById(R.id.additems_v_ll_material_container);
        addunit_j_ll_layout = findViewById(R.id.addItems_v_ll_addUnitType);

        product_j_btn_save = findViewById(R.id.additems_v_btn_save);
        material_j_btn_save = findViewById(R.id.additems_v_btn_mat_exit);
        addunit_j_tv_button = findViewById(R.id.additems_create_unit_btn);
        saveunit_j_btn_save = findViewById(R.id.addunit_v_btn_save);
        product_j_btn_delete = findViewById(R.id.additems_v_btn_remove);
        material_j_btn_delete = findViewById(R.id.additems_v_btn_mat_remove);

        product_j_et_name = findViewById(R.id.additems_v_et_product_name);
        product_j_et_description = findViewById(R.id.additems_v_et_product_desc);
        product_j_et_price = findViewById(R.id.additems_v_et_product_price);
        product_j_et_stock = findViewById(R.id.additems_v_et_product_stock);

        material_j_et_name = findViewById(R.id.additems_v_et_material_name);
        material_j_et_description = findViewById(R.id.additems_v_et_material_desc);
        material_j_et_quantity = findViewById(R.id.additems_v_et_material_quantity);
        unit_j_et_nameinput = findViewById(R.id.addunit_v_et_unit_name);

        unit_j_sp_spinner = findViewById(R.id.spinner);

        setupBottomNavigationView();
        populateUnitSpinner();

        productList = db_j_helper.getProductsFromDB();
        materialList = db_j_helper.getMaterialsFromDB();

        adapter = new ItemCustomAdapter(this, new ArrayList<>());
        edititems_j_lv_items.setAdapter(adapter);

        // set initial visibility
        product_j_ll_layout.setVisibility(View.GONE);
        material_j_ll_layout.setVisibility(View.GONE);
        addunit_j_ll_layout.setVisibility(View.GONE);

        adapter.clear();
        adapter.addAll(productList);

        edititems_j_tl_tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        // listview item click listener
        edititems_j_lv_items.setOnItemClickListener((parent, view, position, id) ->
        {
            if (!adapter.isEmpty()) {
                Object clickedItem = adapter.getItem(position);
                if (clickedItem instanceof Product)
                {
                    Product clickedProduct = (Product) clickedItem;
                    loadProductDetails(clickedProduct);
                }
                else if (clickedItem instanceof Material)
                {
                    Material clickedMaterial = (Material) clickedItem;
                    loadMaterialDetails(clickedMaterial);
                }
            }
        });

        product_j_btn_delete.setOnClickListener(v ->
        {
            Product selectedProduct = (Product) edititems_j_lv_items.getTag();
            if (selectedProduct != null)
            {
                db_j_helper.deleteProduct(selectedProduct.getProductId());
                Toast.makeText(this, "product deleted successfully", Toast.LENGTH_SHORT).show();
                // update the product list
                productList = db_j_helper.getProductsFromDB();
                // refresh the view
                showProducts();
            }
            else
            {
                Toast.makeText(this, "no product selected for deletion", Toast.LENGTH_SHORT).show();
            }
        });

        material_j_btn_delete.setOnClickListener(v ->
        {
            Material selectedMaterial = (Material) edititems_j_lv_items.getTag();
            if (selectedMaterial != null)
            {
                db_j_helper.deleteMaterial(selectedMaterial.getMaterialId());
                Toast.makeText(this, "material deleted successfully", Toast.LENGTH_SHORT).show();
                // update the mat list
                materialList = db_j_helper.getMaterialsFromDB();
                // refresh the view
                showMaterials();
            }
            else
            {
                Toast.makeText(this, "no material selected for deletion", Toast.LENGTH_SHORT).show();
            }
        });

        addunit_j_tv_button.setOnClickListener(v ->
        {
            product_j_ll_layout.setVisibility(View.GONE);
            material_j_ll_layout.setVisibility(View.GONE);
            addunit_j_ll_layout.setVisibility(View.VISIBLE);
        });

        // save new unit
        saveunit_j_btn_save.setOnClickListener(v ->
        {
            String newUnitName = unit_j_et_nameinput.getText().toString().trim();
            if (newUnitName.isEmpty())
            {
                Toast.makeText(this, "please enter a unit name", Toast.LENGTH_SHORT).show();
                return;
            }

            long result = db_j_helper.addUnit(newUnitName);
            if (result != -1)
            {
                Toast.makeText(this, "unit added successfully", Toast.LENGTH_SHORT).show();
                populateUnitSpinner();
                unit_j_et_nameinput.setText("");
                addunit_j_ll_layout.setVisibility(View.GONE);
                showMaterials(); // return to materials view
            }
            else
            {
                Toast.makeText(this, "unit already exists or could not be added", Toast.LENGTH_SHORT).show();
            }
        });

        // save changes for product
        product_j_btn_save.setOnClickListener(v -> saveProductChanges());

        // save changes for material
        material_j_btn_save.setOnClickListener(v -> saveMaterialChanges());
    }

    private void setupBottomNavigationView() {
        home_j_bnv_navigation.setSelectedItemId(R.id.bottom_view);

        home_j_bnv_navigation.setOnItemSelectedListener(item ->
        {
            int id = item.getItemId();
            if (id == R.id.bottom_home)
            {
                startActivity(new Intent(getApplicationContext(), Dashboard.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                return true;
            }
            else if (id == R.id.bottom_add)
            {
                startActivity(new Intent(getApplicationContext(), AddItems.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                return true;
            }
            else if (id == R.id.bottom_settings)
            {
                startActivity(new Intent(getApplicationContext(), Settings.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                return true;
            }
            else if (id == R.id.bottom_profile)
            {
                startActivity(new Intent(getApplicationContext(), Profile.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                return true;
            }
            return false;
        });
    }

    private void populateUnitSpinner() {
        ArrayList<String> unitList = db_j_helper.getUnitTableNames();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, unitList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unit_j_sp_spinner.setAdapter(adapter);
    }

    private void showProducts() {
        product_j_ll_layout.setVisibility(View.GONE);
        material_j_ll_layout.setVisibility(View.GONE);
        addunit_j_ll_layout.setVisibility(View.GONE);
        adapter.clear();
        adapter.addAll(productList);
        adapter.notifyDataSetChanged();
    }

    private void showMaterials() {
        product_j_ll_layout.setVisibility(View.GONE);
        material_j_ll_layout.setVisibility(View.GONE);
        addunit_j_ll_layout.setVisibility(View.GONE);
        adapter.clear();
        adapter.addAll(materialList);
        adapter.notifyDataSetChanged();
    }

    private void loadProductDetails(Product product) {
        product_j_ll_layout.setVisibility(View.VISIBLE);
        material_j_ll_layout.setVisibility(View.GONE);
        addunit_j_ll_layout.setVisibility(View.GONE);

        product_j_et_name.setText(product.getProductName());
        product_j_et_description.setText(product.getDescription());
        product_j_et_price.setText(String.valueOf(product.getPrice()));
        product_j_et_stock.setText(String.valueOf(product.getStock()));

        edititems_j_lv_items.setTag(product); // store the selected product
    }

    private void loadMaterialDetails(Material material) {
        product_j_ll_layout.setVisibility(View.GONE);
        material_j_ll_layout.setVisibility(View.VISIBLE);
        addunit_j_ll_layout.setVisibility(View.GONE);

        material_j_et_name.setText(material.getMaterialName());
        material_j_et_description.setText(material.getDescription());
        material_j_et_quantity.setText(String.valueOf(material.getUnitAmount()));

        String unitName = db_j_helper.getUnitNameById(material.getUnitId());

        if (unitName != null)
        {
            ArrayAdapter<String> adapter = (ArrayAdapter<String>) unit_j_sp_spinner.getAdapter();
            int position = adapter.getPosition(unitName);
            unit_j_sp_spinner.setSelection(position);
        }
        // store the selected material
        edititems_j_lv_items.setTag(material);
    }

    private void saveProductChanges() {
        String name = product_j_et_name.getText().toString();
        String desc = product_j_et_description.getText().toString();
        String price = product_j_et_price.getText().toString();
        String stock = product_j_et_stock.getText().toString();

        if (name.isEmpty() || price.isEmpty() || stock.isEmpty())
        {
            Toast.makeText(this, "please fill in all fields for product", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Product selectedProduct = (Product) edititems_j_lv_items.getTag();
            if (selectedProduct != null)
            {
                db_j_helper.updateProduct(selectedProduct.getProductId(), name, desc, Double.parseDouble(price), Integer.parseInt(stock));
                Toast.makeText(this, "product updated successfully", Toast.LENGTH_SHORT).show();
                productList = db_j_helper.getProductsFromDB();
                showProducts();
            }
            else
            {
                Toast.makeText(this, "no product selected for updating", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveMaterialChanges() {
        String name = material_j_et_name.getText().toString();
        String desc = material_j_et_description.getText().toString();
        String quantity = material_j_et_quantity.getText().toString();
        String unit = unit_j_sp_spinner.getSelectedItem().toString();

        if (name.isEmpty() || quantity.isEmpty() || unit.isEmpty())
        {
            Toast.makeText(this, "please fill in all fields for material", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Material selectedMaterial = (Material) edititems_j_lv_items.getTag();
            if (selectedMaterial != null)
            {
                int unitId = db_j_helper.getUnitId(unit);
                db_j_helper.updateMaterial(selectedMaterial.getMaterialId(), name, unitId, Double.parseDouble(quantity), desc);
                Toast.makeText(this, "material updated successfully", Toast.LENGTH_SHORT).show();
                materialList = db_j_helper.getMaterialsFromDB();
                showMaterials();
            }
            else
            {
                Toast.makeText(this, "no material selected for updating", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
