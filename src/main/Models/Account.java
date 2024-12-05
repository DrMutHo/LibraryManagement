package main.Models;

public class Account {
    private String username;
    private String password_hash;
    private String email;

    public Account() {
    };

    public Account(String username, String password_hash, String email) {
        this.username = username;
        this.password_hash = password_hash;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // Getter và Setter cho password_hash
    public String getPassword_hash() {
        return password_hash;
    }

    public void setPassword_hash(String password_hash) {
        this.password_hash = password_hash;
    }

    // Getter và Setter cho email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
