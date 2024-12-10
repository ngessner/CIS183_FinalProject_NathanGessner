package com.example.stockvault;

public class SessionData {
    private static User loggedInUser;

    public static User getLoggedInUser() {
        return loggedInUser;
    }

    // calling this will set the current logged in user, we'll get their data statically for program usage.
    public static void setLoggedInUser(User user) {
        loggedInUser = user;
    }
}
