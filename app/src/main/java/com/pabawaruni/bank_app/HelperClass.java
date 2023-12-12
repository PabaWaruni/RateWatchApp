package com.pabawaruni.bank_app;

public class HelperClass {

    String username,email, mobileNo, password;


    // get and set username
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    // get and set email
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    //get and set mobile no
    public String getmobileNo() {
        return mobileNo;
    }
    public void setmobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    //get and set password
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }


    public HelperClass(String username, String email, String mobileNo, String password) {
        this.username = username;
        this.email = email;
        this.mobileNo = mobileNo;
        this.password = password;
    }

    public HelperClass() {
    }
}
