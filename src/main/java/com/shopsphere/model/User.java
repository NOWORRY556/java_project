package com.shopsphere.model;

import java.io.Serializable;

public class User implements Serializable {
    private int userId;
    private String name;
    private String email;
    private String password;
    private String mobile;
    private String role;
    private boolean status;

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public boolean isStatus() { return status; }
    public void setStatus(boolean status) { this.status = status; }
}
