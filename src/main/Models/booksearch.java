package main.Models;
import java.sql.*;
import java.util.*;

/**
 * Represents a search result for a book.
 * Contains book details like title, author, ISBN, genre, and more.
 */
public class booksearch {
    private Integer bookid;
    private String title;
    private String author;
    private String isbn;
    private String genre;
    private String language;
    private String description;
    private Integer publication_year;
    private String image_url;

    /**
     * Constructs a new booksearch object with the specified book details.
     *
     * @param bookid The unique identifier for the book.
     * @param title The title of the book.
     * @param author The author of the book.
     * @param isbn The ISBN of the book.
     * @param genre The genre of the book.
     * @param language The language the book is written in.
     * @param description A short description of the book.
     * @param publication_year The year the book was published.
     * @param image_url The URL for the book's image.
     */
    public booksearch(Integer bookid, String title, String author,
                      String isbn, String genre, String language,
                      String description, Integer publication_year,
                      String image_url) {
        this.bookid = bookid;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.genre = genre;
        this.language = language;
        this.description = description;
        this.publication_year = publication_year;
        this.image_url = image_url;
    }

    /**
     * Gets the unique ID of the book.
     *
     * @return The unique identifier for the book.
     */
    public Integer getbookid() {
        return bookid;
    }

    /**
     * Sets the unique ID of the book.
     *
     * @param bookid The unique identifier to be set for the book.
     */
    public void setbookid(Integer bookid) {
        this.bookid = bookid;
    }

    /**
     * Gets the title of the book.
     *
     * @return The title of the book.
     */
    public String gettitle() {
        return title;
    }

    /**
     * Sets the title of the book.
     *
     * @param title The title to be set for the book.
     */
    public void settitle(String title) {
        this.title = title;
    }

    /**
     * Gets the author of the book.
     *
     * @return The author of the book.
     */
    public String getauthor() {
        return author;
    }

    /**
     * Sets the author of the book.
     *
     * @param author The author to be set for the book.
     */
    public void setauthor(String author) {
        this.author = author;
    }

    /**
     * Gets the ISBN of the book.
     *
     * @return The ISBN of the book.
     */
    public String getisbn() {
        return isbn;
    }

    /**
     * Sets the ISBN of the book.
     *
     * @param isbn The ISBN to be set for the book.
     */
    public void setisbn(String isbn) {
        this.isbn = isbn;
    }

    /**
     * Gets the genre of the book.
     *
     * @return The genre of the book.
     */
    public String getgenre() {
        return genre;
    }

    /**
     * Sets the genre of the book.
     *
     * @param genre The genre to be set for the book.
     */
    public void setgenre(String genre) {
        this.genre = genre;
    }

    /**
     * Gets the language the book is written in.
     *
     * @return The language the book is written in.
     */
    public String getlanguage() {
        return language;
    }

    /**
     * Sets the language the book is written in.
     *
     * @param language The language to be set for the book.
     */
    public void setlanguage(String language) {
        this.language = language;
    }

    /**
     * Gets the description of the book.
     *
     * @return A description of the book.
     */
    public String getdescription() {
        return description;
    }

    /**
     * Sets the description of the book.
     *
     * @param description The description to be set for the book.
     */
    public void setdescription(String description) {
        this.description = description;
    }

    /**
     * Gets the year the book was published.
     *
     * @return The year the book was published.
     */
    public Integer getpublication_year() {
        return publication_year;
    }

    /**
     * Sets the year the book was published.
     *
     * @param publication_year The year to be set for the book.
     */
    public void setpublication_year(Integer publication_year) {
        this.publication_year = publication_year;
    }

    /**
     * Gets the URL for the book's image.
     *
     * @return The URL for the book's image.
     */
    public String getimage_url() {
        return image_url;
    }

    /**
     * Sets the URL for the book's image.
     *
     * @param image_url The image URL to be set for the book.
     */
    public void setimage_url(String image_url) {
        this.image_url = image_url;
    }
}
