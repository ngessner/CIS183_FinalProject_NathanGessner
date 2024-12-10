package com.example.stockvault;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "StockVault.db";
    private static final int DATABASE_VERSION = 18;

    // tables
    private static final String USER_TABLE = "Users";
    private static final String PRODUCT_TABLE = "Products";
    private static final String MATERIAL_TABLE = "Materials";
    private static final String UNIT_TABLE = "Units";

    public DatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE " + USER_TABLE + " (" + "userId INTEGER PRIMARY KEY AUTOINCREMENT, " + "username TEXT UNIQUE NOT NULL, " + "firstName TEXT NOT NULL, " + "lastName TEXT NOT NULL, " + "email TEXT NOT NULL, " + "password TEXT NOT NULL);");

        db.execSQL("CREATE TABLE " + PRODUCT_TABLE + " (" + "productId INTEGER PRIMARY KEY AUTOINCREMENT, " + "userId INTEGER NOT NULL, " + "productName TEXT NOT NULL, " + "description TEXT, " + "price REAL NOT NULL, " + "stock INTEGER NOT NULL, " + "dateCreated TEXT NOT NULL, " + "FOREIGN KEY(userId) REFERENCES " + USER_TABLE + "(userId));");

        db.execSQL("CREATE TABLE " + MATERIAL_TABLE + " (" + "materialId INTEGER PRIMARY KEY AUTOINCREMENT, " + "userId INTEGER NOT NULL, " + "materialName TEXT NOT NULL, " + "unitId INTEGER NOT NULL, " + "unitAmount REAL NOT NULL, " + "description TEXT, " + "dateAdded TEXT NOT NULL, " + "FOREIGN KEY(userId) REFERENCES " + USER_TABLE + "(userId), " + "FOREIGN KEY(unitId) REFERENCES " + UNIT_TABLE + "(unitId));");

        db.execSQL("CREATE TABLE " + UNIT_TABLE + " (" + "unitId INTEGER PRIMARY KEY AUTOINCREMENT, " + "unitName TEXT UNIQUE NOT NULL);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + PRODUCT_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + MATERIAL_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + UNIT_TABLE);
        onCreate(db);
    }

    // add unit
    public long addUnit(String unitName)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("unitName", unitName);
        long result = db.insert(UNIT_TABLE, null, values);
        db.close();
        return result;
    }

    // adds product
    public void addProduct(String productName, String description, double price, int stock)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        // uses session data to get the current user
        int userId = SessionData.getLoggedInUser().getUserId();
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        ContentValues values = new ContentValues();
        values.put("userId", userId);
        values.put("productName", productName);
        values.put("description", description);
        values.put("price", price);
        values.put("stock", stock);
        values.put("dateCreated", currentDate);

        db.insert(PRODUCT_TABLE, null, values);
        db.close();
    }

    // add mat
    public void addMaterial(String materialName, int unitId, double unitAmount, String description)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        // get the user id of the logged in user
        int userId = SessionData.getLoggedInUser().getUserId();
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        ContentValues values = new ContentValues();
        values.put("userId", userId);
        values.put("materialName", materialName);
        values.put("unitId", unitId);
        values.put("unitAmount", unitAmount);
        values.put("description", description);
        values.put("dateAdded", currentDate);

        db.insert(MATERIAL_TABLE, null, values);
        db.close();
    }

    // adds user
    public long addUser(String username, String firstName, String lastName, String email, String password)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        long result;

        try
        {
            ContentValues values = new ContentValues();
            values.put("username", username);
            values.put("firstName", firstName);
            values.put("lastName", lastName);
            values.put("email", email);
            values.put("password", password);


            result = db.insert(USER_TABLE, null, values);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            // -1 just means failure
            result = -1;
        }
        finally
        {
            db.close();
        }

        // return the userId or -1 if failed
        return result;
    }


    // get products for logged user
    public ArrayList<Product> getProductsFromDB()
    {
        ArrayList<Product> productsList = new ArrayList<>();
        // need session data for this
        int userId = SessionData.getLoggedInUser().getUserId();

        SQLiteDatabase db = this.getReadableDatabase();
        // store query in cursor
        Cursor cursor = db.rawQuery("SELECT * FROM " + PRODUCT_TABLE + " WHERE userId = ?", new String[]{String.valueOf(userId)});

        while (cursor.moveToNext())
        {
            Product product = new Product(
                    cursor.getInt(cursor.getColumnIndexOrThrow("productId")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("userId")),
                    cursor.getString(cursor.getColumnIndexOrThrow("productName")),
                    cursor.getString(cursor.getColumnIndexOrThrow("description")),
                    cursor.getDouble(cursor.getColumnIndexOrThrow("price")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("stock")),
                    cursor.getString(cursor.getColumnIndexOrThrow("dateCreated"))
            );
            productsList.add(product);
        }

        cursor.close();
        db.close();
        return productsList;
    }

    public boolean checkUserExist(String username, String password)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + USER_TABLE + " WHERE username = ? AND password = ?";
        Cursor cursor = db.rawQuery(query, new String[]{username, password});

        // check if a matching row exists
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    public ArrayList<String> getUnitTableNames()
    {
        ArrayList<String> unitList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT unitName FROM " + UNIT_TABLE, null);

        while (cursor.moveToNext())
        {
            unitList.add(cursor.getString(cursor.getColumnIndexOrThrow("unitName")));
        }

        cursor.close();
        db.close();
        return unitList;
    }

    public int getUnitId(String unitName)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        int unitId = -1;

        String query = "SELECT unitId FROM " + UNIT_TABLE + " WHERE unitName = ?";
        Cursor cursor = db.rawQuery(query, new String[]{unitName});

        if (cursor.moveToFirst())
        {
            unitId = cursor.getInt(cursor.getColumnIndexOrThrow("unitId"));
        }

        cursor.close();
        db.close();
        return unitId;
    }

    public String getUnitNameById(int unitId)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String unitName = null;

        String query = "SELECT unitName FROM " + UNIT_TABLE + " WHERE unitId = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(unitId)});

        try
        {
            if (cursor.moveToFirst())
            {
                unitName = cursor.getString(cursor.getColumnIndexOrThrow("unitName"));
            }
        }
        finally
        {
            cursor.close();
            db.close();
        }

        return unitName;
    }


    // get materials for logged user
    public ArrayList<Material> getMaterialsFromDB()
    {
        ArrayList<Material> materialsList = new ArrayList<>();
        int userId = SessionData.getLoggedInUser().getUserId();

        SQLiteDatabase db = this.getReadableDatabase();

        // query to join UNIT_TABLE and retrieve unitName
        String query = "SELECT m.materialId, m.userId, m.unitId, m.materialName, u.unitName, m.unitAmount, m.description, m.dateAdded " + "FROM " + MATERIAL_TABLE + " m " + "JOIN " + UNIT_TABLE + " u ON m.unitId = u.unitId " + "WHERE m.userId = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

        while (cursor.moveToNext())
        {
            int materialId = cursor.getInt(cursor.getColumnIndexOrThrow("materialId"));
            int materialUserId = cursor.getInt(cursor.getColumnIndexOrThrow("userId"));
            int unitId = cursor.getInt(cursor.getColumnIndexOrThrow("unitId"));
            String materialName = cursor.getString(cursor.getColumnIndexOrThrow("materialName"));
            String unitName = cursor.getString(cursor.getColumnIndexOrThrow("unitName")); // From UNIT_TABLE
            double unitAmount = cursor.getDouble(cursor.getColumnIndexOrThrow("unitAmount"));
            String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
            String dateAdded = cursor.getString(cursor.getColumnIndexOrThrow("dateAdded"));

            // create mat obj
            Material material = new Material(materialId, materialUserId, unitId, materialName, unitName, unitAmount, description, dateAdded);
            materialsList.add(material);
        }

        cursor.close();
        db.close();
        return materialsList;
    }

    public User getUserByUsername(String username)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        User user = null;

        String query = "SELECT * FROM " + USER_TABLE + " WHERE username = ?";
        Cursor cursor = db.rawQuery(query, new String[]{username});

        if (cursor.moveToFirst())
        {
            int userId = cursor.getInt(cursor.getColumnIndexOrThrow("userId"));
            String firstName = cursor.getString(cursor.getColumnIndexOrThrow("firstName"));
            String lastName = cursor.getString(cursor.getColumnIndexOrThrow("lastName"));
            String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
            String password = cursor.getString(cursor.getColumnIndexOrThrow("password"));

            user = new User(userId, username, firstName, lastName, email, password);
        }

        cursor.close();
        db.close();

        return user;
    }


    public void updateMaterial(int materialId, String materialName, int unitId, double unitAmount, String description) {
        SQLiteDatabase db = this.getWritableDatabase();

        try
        {
            ContentValues values = new ContentValues();
            values.put("materialName", materialName);
            values.put("unitId", unitId);
            values.put("unitAmount", unitAmount);
            values.put("description", description);

            int rowsAffected = db.update(MATERIAL_TABLE, values, "materialId = ?", new String[]{String.valueOf(materialId)});
            if (rowsAffected == 0)
            {
                throw new IllegalArgumentException("Material not found for ID: " + materialId);
            }
        }
        finally
        {
            db.close();
        }
    }

    public void updateProduct(int productId, String productName, String description, double price, int stock)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        try
        {
            ContentValues values = new ContentValues();
            values.put("productName", productName);
            values.put("description", description);
            values.put("price", price);
            values.put("stock", stock);

            int rowsAffected = db.update(PRODUCT_TABLE, values, "productId = ?", new String[]{String.valueOf(productId)});
            if (rowsAffected == 0)
            {
                throw new IllegalArgumentException("Product not found for ID: " + productId);
            }
        }
        finally
        {
            db.close();
        }
    }

    public boolean updateUser(int userId, String firstName, String lastName, String username, String email, String password)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("firstName", firstName);
        values.put("lastName", lastName);
        values.put("username", username);
        values.put("email", email);
        values.put("password", password);

        int rowsAffected = db.update(USER_TABLE, values, "userId = ?", new String[]{String.valueOf(userId)});
        db.close();
        return rowsAffected > 0;
    }

    // delete product
    public void deleteProduct(int productId)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(PRODUCT_TABLE, "productId = ?", new String[]{String.valueOf(productId)});
        db.close();
    }

    // delete mat
    public void deleteMaterial(int materialId)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(MATERIAL_TABLE, "materialId = ?", new String[]{String.valueOf(materialId)});
        db.close();
    }
}
