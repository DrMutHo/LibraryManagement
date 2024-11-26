package main.Models;

public class BookCopy {
    private int copyId;
    private int bookId;
    private boolean isAvailable;
    private String bookCondition;

    public BookCopy(int copyId, int bookId, boolean isAvailable, String bookCondition) {
        this.copyId = copyId;
        this.bookId = bookId;
        this.isAvailable = isAvailable;
        this.bookCondition = bookCondition;
    }

    public int getCopyId() {
        return copyId;
    }

    public void setCopyId(int copyId) {
        this.copyId = copyId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public String getBookCondition() {
        return bookCondition;
    }

    public void setBookCondition(String bookCondition) {
        this.bookCondition = bookCondition;
    }
}
