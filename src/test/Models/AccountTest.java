package test.Models;

import main.Models.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AccountTest {

    private Account person;

    @BeforeEach
    public void setUp() {
        person = new Account("user123", "hashedpassword", "user@example.com");
    }

    @Test
    public void testGetUsername() {
        assertEquals("user123", person.getUsername());
    }

    @Test
    public void testSetUsername() {
        person.setUsername("newUser");
        assertEquals("newUser", person.getUsername());
    }

    @Test
    public void testGetPasswordHash() {
        assertEquals("hashedpassword", person.getPassword_hash());
    }

    @Test
    public void testSetPasswordHash() {
        person.setPassword_hash("newHashedPassword");
        assertEquals("newHashedPassword", person.getPassword_hash());
    }

    @Test
    public void testGetEmail() {
        assertEquals("user@example.com", person.getEmail());
    }

    @Test
    public void testSetEmail() {
        person.setEmail("newemail@example.com");
        assertEquals("newemail@example.com", person.getEmail());
    }

    @Test
    public void testConstructor() {
        Account person2 = new Account("user456", "anotherHash", "user456@example.com");
        assertEquals("user456", person2.getUsername());
        assertEquals("anotherHash", person2.getPassword_hash());
        assertEquals("user456@example.com", person2.getEmail());
    }
}
