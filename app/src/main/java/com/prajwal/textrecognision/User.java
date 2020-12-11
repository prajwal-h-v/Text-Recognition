package com.prajwal.textrecognision;

public class User {
    static String email= "Default";
    static String username = "Default";
    static boolean islogin = false;

    public static String getUsername() {
        if (!email.isEmpty()) {
            username = email.split("@")[0];
            return username;
        }
        else return "";
    }
}
