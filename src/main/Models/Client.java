package main.Models;

import java.sql.Date;

public class Client {
    private int clientId;
    private String name;
    private String libraryCardNumber;
    private String email;
    private String phoneNumber;
    private String address;
    private Date registrationDate;
    private double outstandingFees;
    private String username;
    private String passwordHash;
    private String avatarImagePath;

    public Client(int clientId, String name, String libraryCardNumber, String email, String phoneNumber, String address,
            Date registrationDate, double outstandingFees, String username, String passwordHash,
            String avatarImagePath) {
        this.clientId = clientId;
        this.name = name;
        this.libraryCardNumber = libraryCardNumber;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.registrationDate = registrationDate;
        this.outstandingFees = outstandingFees;
        this.username = username;
        this.passwordHash = passwordHash;
        this.avatarImagePath = avatarImagePath;
    }

    public String getAvatarImagePath() {
        return avatarImagePath;
    }

    public void setAvatarImagePath(String avatarImagePath) {
        this.avatarImagePath = avatarImagePath;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLibraryCardNumber() {
        return libraryCardNumber;
    }

    public void setLibraryCardNumber(String libraryCardNumber) {
        this.libraryCardNumber = libraryCardNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public java.util.Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public double getOutstandingFees() {
        return outstandingFees;
    }

    public void setOutstandingFees(double outstandingFees) {
        this.outstandingFees = outstandingFees;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
}