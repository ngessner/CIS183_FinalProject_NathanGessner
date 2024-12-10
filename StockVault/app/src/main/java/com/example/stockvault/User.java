package com.example.stockvault;

public class User
{
    private int userId;        // pkey
    private String username;   // Username will be unique still, but not the primary key.
    private String firstName;
    private String password;
    private String lastName;
    private String email;


    public User()
    {
        // default constructor
    }

    // overloaded constructor for object initialization
    public User(int p_userId, String p_username, String p_firstName, String p_lastName, String p_email, String p_password)
    {
        userId = p_userId;
        username = p_username;
        firstName = p_firstName;
        lastName = p_lastName;
        email = p_email;
        password = p_password;
    }

    // getters and sets
    public int getUserId() {
        return userId;
    }

    public void setUserId(int p_userId) {
        userId = p_userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String p_username) {
        username = p_username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String p_firstName) {
        firstName = p_firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String p_lastName) {
        lastName = p_lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String p_email) {
        email = p_email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String p_password) {
        password = p_password;
    }
}
