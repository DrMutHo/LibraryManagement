package main.Models;

import java.awt.TextField;
import java.time.LocalDate;
import javafx.beans.property.*;
import javafx.scene.control.*;;

/**
 * Represents a borrowing transaction of a book by a client.
 * Contains details about the transaction including the transaction ID, client ID,
 * book title, copy ID, borrow and return dates, and transaction status.
 */
public class BorrowTransaction {
    private int transactionId;
    private int clientId;
    private String title;
    private int copyId;
    private LocalDate borrowDate;
    private LocalDate returnDate;
    private String status;
    private CheckBox selected;

    /**
     * Default constructor for BorrowTransaction.
     */
    public BorrowTransaction() {
    }

    /**
     * Constructs a new BorrowTransaction object with the specified details.
     *
     * @param transactionId The unique ID of the transaction.
     * @param clientId The unique ID of the client who borrowed the book.
     * @param title The title of the borrowed book.
     * @param copyId The unique ID of the specific copy of the book being borrowed.
     * @param borrowDate The date when the book was borrowed.
     * @param returnDate The expected return date of the book.
     * @param status The current status of the transaction (e.g., "borrowed", "returned").
     */
    public BorrowTransaction(int transactionId, int clientId, String title, int copyId, LocalDate borrowDate,
            LocalDate returnDate, String status) {
        this.transactionId = transactionId;
        this.clientId = clientId;
        this.title = title;
        this.copyId = copyId;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
        this.status = status;
        this.selected = new CheckBox();
    }

    /**
     * Gets the unique ID of the transaction.
     *
     * @return The unique ID of the transaction.
     */
    public int getTransactionId() {
        return transactionId;
    }

    /**
     * Sets the unique ID of the transaction.
     *
     * @param transactionId The unique ID to be set for the transaction.
     */
    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    /**
     * Gets the unique ID of the client who borrowed the book.
     *
     * @return The unique ID of the client.
     */
    public int getClientId() {
        return clientId;
    }

    /**
     * Sets the unique ID of the client who borrowed the book.
     *
     * @param clientId The unique ID to be set for the client.
     */
    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    /**
     * Gets the title of the borrowed book.
     *
     * @return The title of the book.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the borrowed book.
     *
     * @param title The title to be set for the book.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the ID of the specific copy of the book being borrowed.
     *
     * @return The unique ID of the book copy.
     */
    public int getCopyId() {
        return copyId;
    }

    /**
     * Sets the ID of the specific copy of the book being borrowed.
     *
     * @param copyId The ID to be set for the book copy.
     */
    public void setCopyId(int copyId) {
        this.copyId = copyId;
    }

    /**
     * Gets the date when the book was borrowed.
     *
     * @return The date the book was borrowed.
     */
    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    /**
     * Sets the date when the book was borrowed.
     *
     * @param borrowDate The date to be set for the borrow date.
     */
    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }

    /**
     * Gets the date when the book is expected to be returned.
     *
     * @return The expected return date of the book.
     */
    public LocalDate getReturnDate() {
        return returnDate;
    }

    /**
     * Sets the date when the book is expected to be returned.
     *
     * @param returnDate The expected return date to be set.
     */
    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    /**
     * Gets the current status of the transaction (e.g., "borrowed", "returned").
     *
     * @return The status of the transaction.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the current status of the transaction.
     *
     * @param status The status to be set for the transaction.
     */
    public void setStatus(String status) {
        this.status = status;
    }

    public CheckBox getSelected() {
        return this.selected; //
    }

    public void setSelected(CheckBox selected) {
        this.selected = selected; // Set the checkbox selected or not
    }

   
}
