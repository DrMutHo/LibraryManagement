package test.Models;

import main.Models.Client;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.*;

public class ClientTest {

    private Client client;

    @BeforeEach
    public void setUp() {
        // Initialize the Client object before each test
        Date registrationDate = new Date(Calendar.getInstance().getTimeInMillis());
        client = new Client(1, "John Doe", "12345", "john@example.com",
                "123-456-7890", "123 Main St",
                registrationDate, 0.0, "john_doe",
                "hashedPassword", "avatar.png");
    }

    @Test
    public void testClientConstructor() {
        // Test that the constructor initializes the object with the correct values
        assertEquals(1, client.getClientId());
        assertEquals("John Doe", client.getName());
        assertEquals("12345", client.getLibraryCardNumber());
        assertEquals("john@example.com", client.getEmail());
        assertEquals("123-456-7890", client.getPhoneNumber());
        assertEquals("123 Main St", client.getAddress());
        assertNotNull(client.getRegistrationDate());
        assertEquals(0.0, client.getOutstandingFees());
        assertEquals("john_doe", client.getUsername());
        assertEquals("hashedPassword", client.getPassword_hash());
        assertEquals("avatar.png", client.getAvatarImagePath());
    }

    @Test
    public void testSettersAndGetters() {
        // Test setter and getter methods
        client.setName("Jane Smith");
        client.setEmail("jane@example.com");
        client.setPhoneNumber("987-654-3210");
        client.setAddress("456 Secondary St");
        client.setOutstandingFees(10.5);
        client.setUsername("jane_smith");
        client.setPassword_hash("newHashedPassword");
        client.setAvatarImagePath("newAvatar.png");

        // Check that the values have been updated
        assertEquals("Jane Smith", client.getName());
        assertEquals("jane@example.com", client.getEmail());
        assertEquals("987-654-3210", client.getPhoneNumber());
        assertEquals("456 Secondary St", client.getAddress());
        assertEquals(10.5, client.getOutstandingFees());
        assertEquals("jane_smith", client.getUsername());
        assertEquals("newHashedPassword", client.getPassword_hash());
        assertEquals("newAvatar.png", client.getAvatarImagePath());
    }

    @Test
    public void testEmailValidation() {
        // Test if email is set properly
        client.setEmail("valid_email@example.com");
        assertEquals("valid_email@example.com", client.getEmail());

        // Optionally, test for invalid email (if validation is added in the future)
        // assertThrows(IllegalArgumentException.class, () ->
        // client.setEmail("invalidEmail"));
    }

    @Test
    public void testOutstandingFees() {
        // Test setting and getting outstanding fees
        client.setOutstandingFees(20.5);
        assertEquals(20.5, client.getOutstandingFees());

        client.setOutstandingFees(0.0);
        assertEquals(0.0, client.getOutstandingFees());
    }

    @Test
    public void testRegistrationDate() {
        // Test if registrationDate is set correctly
        Date newRegistrationDate = Date.valueOf("2024-12-01");
        client.setRegistrationDate(newRegistrationDate);
        assertEquals(newRegistrationDate, client.getRegistrationDate());
    }

    @Test
    public void testDefaultConstructor() {
        // Test the default constructor (if needed)
        Client defaultClient = new Client(0, "", "", "", "", "", null, 0.0, "", "", "");
        assertNotNull(defaultClient);
        assertEquals(0, defaultClient.getClientId());
        assertEquals("", defaultClient.getName());
        assertEquals("", defaultClient.getLibraryCardNumber());
    }
}
