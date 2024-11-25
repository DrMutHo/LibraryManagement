package main.Models;

import java.time.LocalDate;

public class BorrowTransaction {
    private int transactionId;
    private String title;
    private int copyId;
    private LocalDate borrowDate;
    private LocalDate returnDate;
    private String status;

    public BorrowTransaction() {
    }

    public BorrowTransaction(int transactionId, String title, int copyId, LocalDate borrowDate,
            LocalDate returnDate, String status) {
        this.transactionId = transactionId;
        this.title = title;
        this.copyId = copyId;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
        this.status = status;
    }

    // Getters và Setters cho tất cả các thuộc tính

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCopyId() {
        return copyId;
    }

    public void setCopyId(int copyId) {
        this.copyId = copyId;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
