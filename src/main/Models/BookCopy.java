package main.Models;

/**
 * Represents a copy of a book in the library.
 */
public class BookCopy {
    private int copyId;
    private int bookId;
    private boolean isAvailable;
    private String bookCondition;

    /**
     * Constructs a new BookCopy instance with the specified copy ID, book ID, availability status, and book condition.
     *
     * @param copyId The unique identifier for this copy of the book.
     * @param bookId The ID of the book that this copy belongs to.
     * @param isAvailable The availability status of the book copy (true if available, false otherwise).
     * @param bookCondition The condition of the book copy (e.g., "New", "Good", "Damaged").
     */
    public BookCopy(int copyId, int bookId, boolean isAvailable, String bookCondition) {
        this.copyId = copyId;
        this.bookId = bookId;
        this.isAvailable = isAvailable;
        this.bookCondition = bookCondition;
    }

    /**
     * Gets the unique identifier of the book copy.
     *
     * @return The unique identifier of the book copy.
     */
    public int getCopyId() {
        return copyId;
    }

    /**
     * Sets the unique identifier of the book copy.
     *
     * @param copyId The unique identifier to be assigned to the book copy.
     */
    public void setCopyId(int copyId) {
        this.copyId = copyId;
    }

    /**
     * Gets the ID of the book that this copy belongs to.
     *
     * @return The ID of the book that this copy belongs to.
     */
    public int getBookId() {
        return bookId;
    }

    /**
     * Sets the ID of the book that this copy belongs to.
     *
     * @param bookId The ID of the book to be assigned to this copy.
     */
    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    /**
     * Checks if the book copy is available for borrowing.
     *
     * @return true if the book copy is available for borrowing, false otherwise.
     */
    public boolean isAvailable() {
        return isAvailable;
    }

    /**
     * Sets the availability status of the book copy.
     *
     * @param available The availability status to be set (true if available, false otherwise).
     */
    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    /**
     * Gets the condition of the book copy (e.g., "New", "Good", "Damaged").
     *
     * @return The condition of the book copy.
     */
    public String getBookCondition() {
        return bookCondition;
    }

    /**
     * Sets the condition of the book copy.
     *
     * @param bookCondition The condition to be assigned to the book copy.
     */
    public void setBookCondition(String bookCondition) {
        this.bookCondition = bookCondition;
    }
}
