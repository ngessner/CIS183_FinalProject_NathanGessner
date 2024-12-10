package com.example.stockvault;

public class Product
{
    private int productId; // pkey
    private int userId;    // fkey
    private int stock;
    private double price;

    private String productName;
    private String description;
    private String dateCreated;

    public Product()
    {
        // default constructor
    }

    // overloaded constructor for object initialization
    public Product(int p_productId, int p_userId, String p_productName, String p_description, double p_price, int p_stock, String p_dateCreated)
    {
        productId = p_productId;
        userId = p_userId;
        productName = p_productName;
        description = p_description;
        price = p_price;
        stock = p_stock;
        dateCreated = p_dateCreated;
    }

    // gets and sets
    public int getProductId() {
        return productId;
    }

    public void setProductId(int p_productId) {
        productId = p_productId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int p_userId) {
        userId = p_userId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String p_productName) {
        productName = p_productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String p_description) {
        description = p_description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double p_price) {
        price = p_price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int p_stock) {
        stock = p_stock;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String p_dateCreated) {
        dateCreated = p_dateCreated;
    }
}
