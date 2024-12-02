package main.Models;

    /**
     * Represents an administrator in the system.
     * This class holds information about the admin such as their ID, username, password hash, and email.
     */
public class Admin {
    private int admin_id;
    private String username;
    private String password_hash;
    private String email;

    /**
     * Constructs an Admin object with the specified admin ID, username, password hash, and email.
     *
     * @param admin_id       The unique identifier for the admin.
     * @param username       The username of the admin.
     * @param password_hash  The hashed password of the admin.
     * @param email          The email address of the admin.
     */
    public Admin(int admin_id, String username, String password_hash, String email) {
        this.admin_id = admin_id;
        this.username = username;
        this.password_hash = password_hash;
        this.email = email;
    }

    /**
     * Retrieves the unique admin ID.
     *
     * @return The unique identifier for the admin.
     */
    public int getadmin_id() {
        return admin_id;
    }

    /**
     * Sets the unique admin ID.
     *
     * @param admin_id The unique identifier to be assigned to the admin.
     */
    public void setadmin_id(int admin_id) {
        this.admin_id = admin_id;
    }

    /**
     * Retrieves the username of the admin.
     *
     * @return The username of the admin.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the admin.
     *
     * @param username The username to be assigned to the admin.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Retrieves the hashed password of the admin.
     *
     * @return The hashed password of the admin.
     */
    public String getPassword_hash() {
        return password_hash;
    }

   /**
     * Sets the hashed password of the admin.
     *
     * @param password_hash The hashed password to be assigned to the admin.
     */
    public void setPassword_hash(String password_hash) {
        this.password_hash = password_hash;
    }

    /**
     * Retrieves the email of the admin.
     *
     * @return The email of the admin.
     */
    public String getEmail() {
        return email;
    }


   /**
     * Sets the email address of the admin.
     *
     * @param email The email address to be assigned to the admin.
     */
    public void setEmail(String email) {
        this.email = email;
    }


    /**
     * Returns a string representation of the Admin object, including the admin ID, username, password hash, and email.
     *
     * @return A string representing the Admin object in the format: 
     *         "Admin{admin_id=<admin_id>, username='<username>', password_hash='<password_hash>', email='<email>'}"
     */
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
