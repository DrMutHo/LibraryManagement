package test.Models;

import main.Models.booksearch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BookSearchTest {

    private booksearch book;

    @BeforeEach
    void setUp() {
        // Khởi tạo một đối tượng booksearch trước mỗi bài kiểm tra
        book = new booksearch(1, "The Great Gatsby", "F. Scott Fitzgerald",
                "9780743273565", "Fiction", "English",
                "A novel about the American dream.", 1925, "image_url");
    }

    @Test
    void testGetBookId() {
        assertEquals(1, book.getbookid(), "Book ID should be 1");
    }

    @Test
    void testSetBookId() {
        book.setbookid(2);
        assertEquals(2, book.getbookid(), "Book ID should be updated to 2");
    }

    @Test
    void testGetTitle() {
        assertEquals("The Great Gatsby", book.gettitle(), "Title should be 'The Great Gatsby'");
    }

    @Test
    void testSetTitle() {
        book.settitle("1984");
        assertEquals("1984", book.gettitle(), "Title should be updated to '1984'");
    }

    @Test
    void testGetAuthor() {
        assertEquals("F. Scott Fitzgerald", book.getauthor(), "Author should be 'F. Scott Fitzgerald'");
    }

    @Test
    void testSetAuthor() {
        book.setauthor("George Orwell");
        assertEquals("George Orwell", book.getauthor(), "Author should be updated to 'George Orwell'");
    }

    @Test
    void testGetIsbn() {
        assertEquals("9780743273565", book.getisbn(), "ISBN should be '9780743273565'");
    }

    @Test
    void testSetIsbn() {
        book.setisbn("9780451524935");
        assertEquals("9780451524935", book.getisbn(), "ISBN should be updated to '9780451524935'");
    }

    @Test
    void testGetGenre() {
        assertEquals("Fiction", book.getgenre(), "Genre should be 'Fiction'");
    }

    @Test
    void testSetGenre() {
        book.setgenre("Dystopian");
        assertEquals("Dystopian", book.getgenre(), "Genre should be updated to 'Dystopian'");
    }

    @Test
    void testGetLanguage() {
        assertEquals("English", book.getlanguage(), "Language should be 'English'");
    }

    @Test
    void testSetLanguage() {
        book.setlanguage("French");
        assertEquals("French", book.getlanguage(), "Language should be updated to 'French'");
    }

    @Test
    void testGetDescription() {
        assertEquals("A novel about the American dream.", book.getdescription(),
                "Description should be 'A novel about the American dream.'");
    }

    @Test
    void testSetDescription() {
        book.setdescription("A novel about the totalitarian regime.");
        assertEquals("A novel about the totalitarian regime.", book.getdescription(), "Description should be updated.");
    }

    @Test
    void testGetPublicationYear() {
        assertEquals(1925, book.getpublication_year(), "Publication year should be 1925");
    }

    @Test
    void testSetPublicationYear() {
        book.setpublication_year(1949);
        assertEquals(1949, book.getpublication_year(), "Publication year should be updated to 1949");
    }

    @Test
    void testGetImageUrl() {
        assertEquals("image_url", book.getimage_url(), "Image URL should be 'image_url'");
    }

    @Test
    void testSetImageUrl() {
        book.setimage_url("new_image_url");
        assertEquals("new_image_url", book.getimage_url(), "Image URL should be updated to 'new_image_url'");
    }
}
