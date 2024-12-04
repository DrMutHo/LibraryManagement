package test.Models;

import main.Models.GoogleBooksAPI;

import org.junit.jupiter.api.*;

public class GoogleBooksAPITest {

    @Test
    void testSearchBookByISBNSuccess() throws Exception {
        // Simulate a successful API response
        String isbn = "9780134685991";
        GoogleBooksAPI.searchBookByISBN(isbn);

    }

    @Test
    void testSearchBookByISBNNullOrEmptyISBN() {
        // Test with a null or empty ISBN
        GoogleBooksAPI.searchBookByISBN(null);
        GoogleBooksAPI.searchBookByISBN("");
    }
}
