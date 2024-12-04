package test.Models;

import main.Models.BorrowTransaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class BorrowTransactionTest {

    private BorrowTransaction transaction;

    @BeforeEach
    void setUp() {
        // Khởi tạo một đối tượng BorrowTransaction trước mỗi bài kiểm tra
        transaction = new BorrowTransaction(1, 101, "The Great Gatsby", 1001,
                LocalDate.of(2024, 12, 1), LocalDate.of(2024, 12, 15), "Borrowed");
    }

    @Test
    void testGetTransactionId() {
        assertEquals(1, transaction.getTransactionId(), "Transaction ID should be 1");
    }

    @Test
    void testSetTransactionId() {
        transaction.setTransactionId(2);
        assertEquals(2, transaction.getTransactionId(), "Transaction ID should be updated to 2");
    }

    @Test
    void testGetClientId() {
        assertEquals(101, transaction.getClientId(), "Client ID should be 101");
    }

    @Test
    void testSetClientId() {
        transaction.setClientId(102);
        assertEquals(102, transaction.getClientId(), "Client ID should be updated to 102");
    }

    @Test
    void testGetTitle() {
        assertEquals("The Great Gatsby", transaction.getTitle(), "Title should be 'The Great Gatsby'");
    }

    @Test
    void testSetTitle() {
        transaction.setTitle("1984");
        assertEquals("1984", transaction.getTitle(), "Title should be updated to '1984'");
    }

    @Test
    void testGetCopyId() {
        assertEquals(1001, transaction.getCopyId(), "Copy ID should be 1001");
    }

    @Test
    void testSetCopyId() {
        transaction.setCopyId(1002);
        assertEquals(1002, transaction.getCopyId(), "Copy ID should be updated to 1002");
    }

    @Test
    void testGetBorrowDate() {
        assertEquals(LocalDate.of(2024, 12, 1), transaction.getBorrowDate(), "Borrow date should be 2024-12-01");
    }

    @Test
    void testSetBorrowDate() {
        transaction.setBorrowDate(LocalDate.of(2024, 12, 2));
        assertEquals(LocalDate.of(2024, 12, 2), transaction.getBorrowDate(),
                "Borrow date should be updated to 2024-12-02");
    }

    @Test
    void testGetReturnDate() {
        assertEquals(LocalDate.of(2024, 12, 15), transaction.getReturnDate(), "Return date should be 2024-12-15");
    }

    @Test
    void testSetReturnDate() {
        transaction.setReturnDate(LocalDate.of(2024, 12, 20));
        assertEquals(LocalDate.of(2024, 12, 20), transaction.getReturnDate(),
                "Return date should be updated to 2024-12-20");
    }

    @Test
    void testGetStatus() {
        assertEquals("Borrowed", transaction.getStatus(), "Status should be 'Borrowed'");
    }

    @Test
    void testSetStatus() {
        transaction.setStatus("Returned");
        assertEquals("Returned", transaction.getStatus(), "Status should be updated to 'Returned'");
    }
}
