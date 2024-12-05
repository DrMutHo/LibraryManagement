package main.Models;

/**
 * Represents an administrator in the system.
 * This class holds information about the admin such as their ID, username,
 * password hash, and email.
 */
public class Admin extends Account {
    private int admin_id;

    /**
     * Constructs an Admin object with the specified admin ID, username, password
     * hash, and email.
     *
     * @param admin_id      The unique identifier for the admin.
     * @param username      The username of the admin.
     * @param password_hash The hashed password of the admin.
     * @param email         The email address of the admin.
     */
    public Admin(int admin_id, String username, String password_hash, String email) {
        super(username, password_hash, email);
        this.admin_id = admin_id;
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
     * Returns a string representation of the Admin object, including the admin ID,
     * username, password hash, and email.
     *
     * @return A string representing the Admin object in the format:
     *         "Admin{admin_id=<admin_id>, username='<username>',
     *         password_hash='<password_hash>', email='<email>'}"
     */
    @Override
    public String toString() {
        return "Admin{" +
                "admin_id=" + admin_id +
                ", username='" + this.getUsername() + '\'' +
                ", password_hash='" + this.getPassword_hash() + '\'' +
                ", email='" + this.getEmail() + '\'' +
                '}';
    }
}
