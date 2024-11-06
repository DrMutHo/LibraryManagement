package main.Models;

public class Book {
    private int book_id;
    private String title;
    private String author;
    private String isbn;
    private String genre;
    private String language;
    private int publication_year;
    private String description;
    private String image_path;
    private double average_rating;
    private int review_count;

    // Constructor không tham số
    public Book() {
    }

    // Constructor đầy đủ với tất cả các thuộc tính
    public Book(int book_id, String title, String author, String isbn, String genre, String language,
            String description, int publication_year, String image_path, double average_rating, int review_count) {
        this.book_id = book_id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.genre = genre;
        this.language = language;
        this.publication_year = publication_year;
        this.description = description;
        this.image_path = image_path;
        this.average_rating = average_rating;
        this.review_count = review_count;
    }

    // Getters và Setters cho tất cả các thuộc tính

    public int getBook_id() {
        return book_id;
    }

    public void setBook_id(int book_id) {
        this.book_id = book_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getPublicationYear() {
        return publication_year;
    }

    public void setPublicationYear(int publication_year) {
        this.publication_year = publication_year;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImagePath() {
        return image_path;
    }

    public void setImagePath(String image_path) {
        this.image_path = image_path;
    }

    public double getAverageRating() {
        return average_rating;
    }

    public void setAverageRating(double average_rating) {
        this.average_rating = average_rating;
    }

    public int getReviewCount() {
        return review_count;
    }

    public void setReviewCount(int review_count) {
        this.review_count = review_count;
    }
}
