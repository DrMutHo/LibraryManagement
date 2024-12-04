package main.Models;

import java.sql.Date;

/**
 * Represents a client in the library system.
 * Contains details about the client, such as their personal information,
 * library card number, registration date, outstanding fees, and login credentials.
 */
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

    /**
     * Constructs a new Client object with the specified details.
     *
     * @param clientId The unique ID of the client.
     * @param name The name of the client.
     * @param libraryCardNumber The library card number of the client.
     * @param email The email address of the client.
     * @param phoneNumber The phone number of the client.
     * @param address The address of the client.
     * @param registrationDate The registration date of the client.
     * @param outstandingFees The outstanding fees the client owes to the library.
     * @param username The username for the client's account.
     * @param passwordHash The hashed password for the client's account.
     * @param avatarImagePath The path to the client's avatar image.
     */
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

    /**
     * Gets the path to the client's avatar image.
     *
     * @return The path to the avatar image.
     */
    public String getAvatarImagePath() {
        return avatarImagePath;
    }

    /**
     * Sets the path to the client's avatar image.
     *
     * @param avatarImagePath The path to the avatar image to set.
     */
    public void setAvatarImagePath(String avatarImagePath) {
        this.avatarImagePath = avatarImagePath;
    }

    /**
     * Gets the unique ID of the client.
     *
     * @return The unique ID of the client.
     */
    public int getClientId() {
        return clientId;
    }

    /**
     * Sets the unique ID of the client.
     *
     * @param clientId The unique ID of the client to set.
     */
    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    /**
     * Gets the name of the client.
     *
     * @return The name of the client.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the client.
     *
     * @param name The name of the client to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the library card number of the client.
     *
     * @return The library card number of the client.
     */
    public String getLibraryCardNumber() {
        return libraryCardNumber;
    }

    /**
     * Sets the library card number of the client.
     *
     * @param libraryCardNumber The library card number to set.
     */
    public void setLibraryCardNumber(String libraryCardNumber) {
        this.libraryCardNumber = libraryCardNumber;
    }

    /**
     * Gets the email address of the client.
     *
     * @return The email address of the client.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of the client.
     *
     * @param email The email address to set.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the phone number of the client.
     *
     * @return The phone number of the client.
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets the phone number of the client.
     *
     * @param phoneNumber The phone number to set.
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Gets the address of the client.
     *
     * @return The address of the client.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the address of the client.
     *
     * @param address The address to set.
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Gets the registration date of the client.
     *
     * @return The registration date of the client.
     */
    public Date getRegistrationDate() {
        return registrationDate;
    }

    /**
     * Sets the registration date of the client.
     *
     * @param registrationDate The registration date to set.
     */
    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    /**
     * Gets the outstanding fees the client owes to the library.
     *
     * @return The outstanding fees of the client.
     */
    public double getOutstandingFees() {
        return outstandingFees;
    }

    /**
     * Sets the outstanding fees the client owes to the library.
     *
     * @param outstandingFees The outstanding fees to set.
     */
    public void setOutstandingFees(double outstandingFees) {
        this.outstandingFees = outstandingFees;
    }

    /**
     * Gets the username for the client's account.
     *
     * @return The username of the client.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username for the client's account.
     *
     * @param username The username to set for the client.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the hashed password for the client's account.
     *
     * @return The hashed password of the client.
     */
    public String getPasswordHash() {
        return passwordHash;
    }

    /**
     * Sets the hashed password for the client's account.
     *
     * @param passwordHash The hashed password to set for the client.
     */
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
}
