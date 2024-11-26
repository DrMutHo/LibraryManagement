package main.Models;

import java.time.LocalDate;

public class BorrowTransaction {
    private int transaction_id;
    private int client_id;
    private int copy_id;
    private LocalDate borrow_date;
    private LocalDate return_date;
    private String status;

    public BorrowTransaction() {

    }

    public BorrowTransaction(int transaction_id, int client_id, int copy_id, LocalDate borrow_date,
            LocalDate return_date, String status) {
        this.transaction_id = transaction_id;
        this.client_id = client_id;
        this.copy_id = copy_id;
        this.borrow_date = borrow_date;
        this.return_date = return_date;
        this.status = status;
    }

    public int getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(int transaction_id) {
        this.transaction_id = transaction_id;
    }

    public int getClient_id() {
        return client_id;
    }

    public void setClient_id(int client_id) {
        this.client_id = client_id;
    }

    public int getCopy_id() {
        return copy_id;
    }

    public void setCopy_id(int copy_id) {
        this.copy_id = copy_id;
    }

    public LocalDate getBorrow_date() {
        return borrow_date;
    }

    public void setBorrow_date(LocalDate borrow_date) {
        this.borrow_date = borrow_date;
    }

    public LocalDate getReturn_date() {
        return return_date;
    }

    public void setReturn_date(LocalDate return_date) {
        this.return_date = return_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
