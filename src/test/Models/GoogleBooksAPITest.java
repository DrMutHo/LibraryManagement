package test.Models;

import main.Models.GoogleBooksAPI;
import main.Models.Book;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

/**
 * Unit tests for the GoogleBooksAPI class.
 * <p>
 * This class contains test methods to verify the behavior of the
 * GoogleBooksAPI,
 * including the searchBook method for querying Google Books and ensuring proper
 * handling
 * of valid, empty, and null queries.
 * </p>
 */
public class GoogleBooksAPITest {

    /**
     * Tests the searchBook method with a valid query.
     * <p>
     * This test verifies that the method returns a non-null response containing
     * data
     * when provided with a valid query string.
     * </p>
     * <p>
     * Example query: "Harry Potter"
     * </p>
     */
    @Test
    void testSearchBookWithValidQuery() {
        // Example of using a valid query
        String query = "Harry Potter";
        StringBuilder response = GoogleBooksAPI.searchBook(query);

        // Validate that a response is returned
        assertNotNull(response, "Response should not be null for a valid query.");
        assertTrue(response.length() > 0, "Response should contain data.");
    }

    /**
     * Tests the searchBook method with an empty query.
     * <p>
     * This test ensures that the method handles empty query strings correctly and
     * returns null when no input is provided.
     * </p>
     */
    @Test
    void testSearchBookWithEmptyQuery() {
        // Example of using an empty query
        String query = "";
        StringBuilder response = GoogleBooksAPI.searchBook(query);

        // Validate that no response is returned
        assertNull(response, "Response should be null for an empty query.");
    }
}
