package test.Models;

import main.Models.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BookTest {

    private Book book;

    @BeforeEach
    void setUp() {
        // Khởi tạo một đối tượng Book mới trước mỗi bài kiểm tra
        book = new Book(1, "Java Programming", "John Doe", "ISBN-123456789", "Programming", "English",
                "A comprehensive guide to Java.", 2022, "images/java.jpg", 4.5, 200, 10);
    }

    @Test
    void testGetBookId() {
        assertEquals(1, book.getBook_id(), "Book ID should be 1");
    }

    @Test
    void testSetBookId() {
        book.setBook_id(2);
        assertEquals(2, book.getBook_id(), "Book ID should be updated to 2");
    }

    @Test
    void testGetTitle() {
        assertEquals("Java Programming", book.getTitle(), "Title should be 'Java Programming'");
    }

    @Test
    void testSetTitle() {
        book.setTitle("Advanced Java Programming");
        assertEquals("Advanced Java Programming", book.getTitle(), "Title should be updated");
    }

    @Test
    void testGetAuthor() {
        assertEquals("John Doe", book.getAuthor(), "Author should be 'John Doe'");
    }

    @Test
    void testSetAuthor() {
        book.setAuthor("Jane Doe");
        assertEquals("Jane Doe", book.getAuthor(), "Author should be updated");
    }

    @Test
    void testGetIsbn() {
        assertEquals("ISBN-123456789", book.getIsbn(), "ISBN should be 'ISBN-123456789'");
    }

    @Test
    void testSetIsbn() {
        book.setIsbn("ISBN-987654321");
        assertEquals("ISBN-987654321", book.getIsbn(), "ISBN should be updated");
    }

    @Test
    void testGetGenre() {
        assertEquals("Programming", book.getGenre(), "Genre should be 'Programming'");
    }

    @Test
    void testSetGenre() {
        book.setGenre("Science");
        assertEquals("Science", book.getGenre(), "Genre should be updated");
    }

    @Test
    void testGetLanguage() {
        assertEquals("English", book.getLanguage(), "Language should be 'English'");
    }

    @Test
    void testSetLanguage() {
        book.setLanguage("French");
        assertEquals("French", book.getLanguage(), "Language should be updated");
    }

    @Test
    void testGetPublicationYear() {
        assertEquals(2022, book.getPublication_year(), "Publication Year should be 2022");
    }

    @Test
    void testSetPublicationYear() {
        book.setPublication_year(2023);
        assertEquals(2023, book.getPublication_year(), "Publication Year should be updated");
    }

    @Test
    void testGetDescription() {
        assertEquals("A comprehensive guide to Java.", book.getDescription(), "Description should match");
    }

    @Test
    void testSetDescription() {
        book.setDescription("An updated guide to Java.");
        assertEquals("An updated guide to Java.", book.getDescription(), "Description should be updated");
    }

    @Test
    void testGetImagePath() {
        assertEquals("images/java.jpg", book.getImagePath(), "Image Path should match");
    }

    @Test
    void testSetImagePath() {
        book.setImage_path("images/advanced_java.jpg");
        assertEquals("images/advanced_java.jpg", book.getImagePath(), "Image Path should be updated");
    }

    @Test
    void testGetAverageRating() {
        assertEquals(4.5, book.getAverage_rating(), 0.01, "Average Rating should be 4.5");
    }

    @Test
    void testSetAverageRating() {
        book.setAverage_rating(4.7);
        assertEquals(4.7, book.getAverage_rating(), 0.01, "Average Rating should be updated");
    }

    @Test
    void testGetReviewCount() {
        assertEquals(200, book.getReview_count(), "Review Count should be 200");
    }

    @Test
    void testSetReviewCount() {
        book.setReview_count(250);
        assertEquals(250, book.getReview_count(), "Review Count should be updated");
    }

    @Test
    void testGetQuantity() {
        assertEquals(10, book.getQuantity(), "Quantity should be 10");
    }

    @Test
    void testSetQuantity() {
        book.setQuantity(15);
        assertEquals(15, book.getQuantity(), "Quantity should be updated to 15");
    }

    @Test
    void testBookProperties() {
        // Test các phương thức Property (không phải getter/setter thông thường)
        assertNotNull(book.book_idProperty(), "book_idProperty should not be null");
        assertNotNull(book.titleProperty(), "titleProperty should not be null");
        assertNotNull(book.authorProperty(), "authorProperty should not be null");
        assertNotNull(book.isbnProperty(), "isbnProperty should not be null");
        assertNotNull(book.genreProperty(), "genreProperty should not be null");
        assertNotNull(book.languageProperty(), "languageProperty should not be null");
        assertNotNull(book.publication_yearProperty(), "publication_yearProperty should not be null");
        assertNotNull(book.descriptionProperty(), "descriptionProperty should not be null");
        assertNotNull(book.image_pathProperty(), "image_pathProperty should not be null");
        assertNotNull(book.average_ratingProperty(), "average_ratingProperty should not be null");
        assertNotNull(book.review_countProperty(), "review_countProperty should not be null");
        assertNotNull(book.quantityProperty(), "quantityProperty should not be null");
    }
}
