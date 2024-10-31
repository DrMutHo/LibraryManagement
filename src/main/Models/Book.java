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
    private String image_url;

    public Book() {
    }

    public Book(int book_id, String title, String author, String isbn, String genre, String language,
            int publication_date, String description, String image_url) {
        this.book_id = book_id;
        this.title = title;
        this.author = author;
        this.description = description;
        this.image_url = image_url;
        this.isbn = isbn;
        this.genre = genre;
        this.language = language;
        this.publication_year = publication_date;
    }

    public String getGenre() {
        return this.genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getLanguage() {
        return this.language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getPublication_year() {
        return this.publication_year;
    }

    public void setPublication_year(int publication_date) {
        this.publication_year = publication_date;
    }

    public String getIsbn() {
        return this.isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getBook_id() {
        return this.book_id;
    }

    public void setBook_id(int book_id) {
        this.book_id = book_id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage_url() {
        return this.image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
