package com.example.stockvault;

public class Material
{
    private int materialId; // pkey
    private int userId;     // fkey, from users table
    private int unitId;
    private double unitAmount;

    private String materialName;
    private String unit;
    private String description;
    private String dateAdded;

    public Material()
    {
        // nothin in it. just a default constructor
    }

    // overloaded constructor for object inintialization
    public Material(int p_materialId, int p_userId, int p_unitId, String p_materialName, String p_unit, double p_unitAmount, String p_description, String p_dateAdded)
    {
        materialId = p_materialId;
        userId = p_userId;
        unitId = p_unitId;
        materialName = p_materialName;
        unit = p_unit;
        unitAmount = p_unitAmount;
        description = p_description;
        dateAdded = p_dateAdded;
    }

    // gets and sets
    public int getMaterialId() {
        return materialId;
    }

    public void setMaterialId(int p_materialId) {
        materialId = p_materialId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int p_userId) {
        userId = p_userId;
    }

    public int getUnitId() {
        return unitId;
    }

    public void setUnitId(int p_unitId) {
        unitId = p_unitId;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String p_materialName) {
        materialName = p_materialName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String p_unit) {
        unit = p_unit;
    }

    public double getUnitAmount() {
        return unitAmount;
    }

    public void setUnitAmount(double p_unitAmount) {
        unitAmount = p_unitAmount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String p_description) {
        description = p_description;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String p_dateAdded) {
        dateAdded = p_dateAdded;
    }
}
