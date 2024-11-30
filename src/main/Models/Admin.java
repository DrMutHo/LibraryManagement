package main.Models;

public class Admin {
    private int admin_id;
    private String username;
    private String password_hash;
    private String email;

    public Admin(int admin_id, String username, String password_hash, String email) {
        this.admin_id = admin_id;
        this.username = username;
        this.password_hash = password_hash;
        this.email = email;
    }

    public int getadmin_id() {
        return admin_id;
    }

    public void setadmin_id(int admin_id) {
        this.admin_id = admin_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword_hash() {
        return password_hash;
    }

    public void setPassword_hash(String password_hash) {
        this.password_hash = password_hash;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "admin_id=" + admin_id +
                ", username='" + username + '\'' +
                ", password_hash='" + password_hash + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}

