package test.Models;

import main.Models.Admin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AdminTest {

    private Admin admin;

    @BeforeEach
    void setUp() {
        // Khởi tạo một Admin mới cho mỗi bài kiểm tra
        admin = new Admin(1, "adminUser", "hashedPassword", "admin@example.com");
    }

    @Test
    void testGetAdminId() {
        assertEquals(1, admin.getadmin_id(), "Admin ID should be 1");
    }

    @Test
    void testSetAdminId() {
        admin.setadmin_id(2);
        assertEquals(2, admin.getadmin_id(), "Admin ID should be updated to 2");
    }

    @Test
    void testGetUsername() {
        assertEquals("adminUser", admin.getUsername(), "Username should be 'adminUser'");
    }

    @Test
    void testSetUsername() {
        admin.setUsername("newUsername");
        assertEquals("newUsername", admin.getUsername(), "Username should be updated to 'newUsername'");
    }

    @Test
    void testGetPasswordHash() {
        assertEquals("hashedPassword", admin.getPassword_hash(), "Password hash should be 'hashedPassword'");
    }

    @Test
    void testSetPasswordHash() {
        admin.setPassword_hash("newHashPassword");
        assertEquals("newHashPassword", admin.getPassword_hash(), "Password hash should be updated");
    }

    @Test
    void testGetEmail() {
        assertEquals("admin@example.com", admin.getEmail(), "Email should be 'admin@example.com'");
    }

    @Test
    void testSetEmail() {
        admin.setEmail("newemail@example.com");
        assertEquals("newemail@example.com", admin.getEmail(), "Email should be updated to 'newemail@example.com'");
    }

    @Test
    void testToString() {
        String expected = "Admin{admin_id=1, username='adminUser', password_hash='hashedPassword', email='admin@example.com'}";
        assertEquals(expected, admin.toString(),
                "The toString method should return the correct string representation of the Admin object");
    }
}
