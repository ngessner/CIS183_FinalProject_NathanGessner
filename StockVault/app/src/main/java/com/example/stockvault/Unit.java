package com.example.stockvault;

public class Unit
{
    private int unitId;         // p key
    private String unitName;    // type unit (e.g., grams, kilograms, pounds)

    // default constructor
    public Unit()
    {

    }

    // overloaded constructor for creating a unit without an ID (db use)
    public Unit(String p_unitName)
    {
        unitName = p_unitName;
    }

    // constructor for database uses
    public Unit(int p_unitId, String p_unitName)
    {
        unitId = p_unitId;
        unitName = p_unitName;
    }

    // getters and setters
    public int getUnitId() {
        return unitId;
    }

    public void setUnitId(int p_unitId) {
        unitId = p_unitId;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String p_unitName) {
        unitName = p_unitName;
    }
}
