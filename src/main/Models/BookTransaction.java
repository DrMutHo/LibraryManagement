package main.Models;

import java.time.LocalDate;

public class BookTransaction {
    private int transaction_id;
    private int book_id;
    private int user_id;
    private LocalDate transaction_date;
    private LocalDate due_date; // Ngày dự kiến trả
    private String transaction_type; // borrow, return, purchase, reservation
    private int quantity;
    private double total_price;
    private String status; // pending, completed, overdue, canceled
    private String notes;

    // Constructor không tham số
    public BookTransaction() {
    }

    // Constructor đầy đủ với tất cả các thuộc tính
    public BookTransaction(int transaction_id, int book_id, int user_id, LocalDate transaction_date,
            LocalDate due_date, String transaction_type, int quantity, double total_price,
            String status, String notes) {
        this.transaction_id = transaction_id;
        this.book_id = book_id;
        this.user_id = user_id;
        this.transaction_date = transaction_date;
        this.due_date = due_date;
        this.transaction_type = transaction_type;
        this.quantity = quantity;
        this.total_price = total_price;
        this.status = status;
        this.notes = notes;
    }

    // Getters và Setters cho tất cả các thuộc tính

    public int getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(int transaction_id) {
        this.transaction_id = transaction_id;
    }

    public int getBook_id() {
        return book_id;
    }

    public void setBook_id(int book_id) {
        this.book_id = book_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public LocalDate getTransaction_date() {
        return transaction_date;
    }

    public void setTransaction_date(LocalDate transaction_date) {
        this.transaction_date = transaction_date;
    }

    public LocalDate getDue_date() {
        return due_date;
    }

    public void setDue_date(LocalDate due_date) {
        this.due_date = due_date;
    }

    public String getTransaction_type() {
        return transaction_type;
    }

    public void setTransaction_type(String transaction_type) {
        this.transaction_type = transaction_type;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotal_price() {
        return total_price;
    }

    public void setTotal_price(double total_price) {
        this.total_price = total_price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
