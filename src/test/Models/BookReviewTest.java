package test.Models;

import main.Models.BookReview;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class BookReviewTest {

    private BookReview bookReview;
    private LocalDateTime reviewDate;

    @BeforeEach
    void setUp() {
        // Khởi tạo một đối tượng BookReview trước mỗi bài kiểm tra
        reviewDate = LocalDateTime.now();
        bookReview = new BookReview(1, 101, 1001, 4.5, "Great book!", reviewDate);
    }

    @Test
    void testGetReviewId() {
        assertEquals(1, bookReview.getReviewId(), "Review ID should be 1");
    }

    @Test
    void testSetReviewId() {
        bookReview.setReviewId(2);
        assertEquals(2, bookReview.getReviewId(), "Review ID should be updated to 2");
    }

    @Test
    void testGetBookId() {
        assertEquals(101, bookReview.getBookId(), "Book ID should be 101");
    }

    @Test
    void testSetBookId() {
        bookReview.setBookId(102);
        assertEquals(102, bookReview.getBookId(), "Book ID should be updated to 102");
    }

    @Test
    void testGetClientId() {
        assertEquals(1001, bookReview.getClientId(), "Client ID should be 1001");
    }

    @Test
    void testSetClientId() {
        bookReview.setClientId(1002);
        assertEquals(1002, bookReview.getClientId(), "Client ID should be updated to 1002");
    }

    @Test
    void testGetRating() {
        assertEquals(4.5, bookReview.getRating(), "Rating should be 4.5");
    }

    @Test
    void testSetRating() {
        bookReview.setRating(5.0);
        assertEquals(5.0, bookReview.getRating(), "Rating should be updated to 5.0");
    }

    @Test
    void testGetComment() {
        assertEquals("Great book!", bookReview.getComment(), "Comment should be 'Great book!'");
    }

    @Test
    void testSetComment() {
        bookReview.setComment("Fantastic read!");
        assertEquals("Fantastic read!", bookReview.getComment(), "Comment should be updated to 'Fantastic read!'");
    }

    @Test
    void testGetReviewDate() {
        assertEquals(reviewDate, bookReview.getReviewDate(), "Review date should match");
    }

    @Test
    void testSetReviewDate() {
        LocalDateTime newReviewDate = LocalDateTime.now().plusDays(1);
        bookReview.setReviewDate(newReviewDate);
        assertEquals(newReviewDate, bookReview.getReviewDate(), "Review date should be updated");
    }
}
