package test.Models;

import main.Models.BookCopy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BookCopyTest {

    private BookCopy bookCopy;

    @BeforeEach
    void setUp() {
        // Khởi tạo một đối tượng BookCopy trước mỗi bài kiểm tra
        bookCopy = new BookCopy(1, 101, true, "New");
    }

    @Test
    void testGetCopyId() {
        assertEquals(1, bookCopy.getCopyId(), "Copy ID should be 1");
    }

    @Test
    void testSetCopyId() {
        bookCopy.setCopyId(2);
        assertEquals(2, bookCopy.getCopyId(), "Copy ID should be updated to 2");
    }

    @Test
    void testGetBookId() {
        assertEquals(101, bookCopy.getBookId(), "Book ID should be 101");
    }

    @Test
    void testSetBookId() {
        bookCopy.setBookId(102);
        assertEquals(102, bookCopy.getBookId(), "Book ID should be updated to 102");
    }

    @Test
    void testIsAvailable() {
        assertTrue(bookCopy.isAvailable(), "The book copy should be available");
    }

    @Test
    void testSetAvailable() {
        bookCopy.setAvailable(false);
        assertFalse(bookCopy.isAvailable(), "The book copy should not be available");
    }

    @Test
    void testGetBookCondition() {
        assertEquals("New", bookCopy.getBookCondition(), "The book condition should be 'New'");
    }

    @Test
    void testSetBookCondition() {
        bookCopy.setBookCondition("Good");
        assertEquals("Good", bookCopy.getBookCondition(), "The book condition should be updated to 'Good'");
    }
}
