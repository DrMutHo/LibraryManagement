package main.Models;

import javafx.beans.property.*;

/**
 * Represents a book with various attributes such as title, author, ISBN, genre, and more.
 * This class provides a detailed representation of a book, including its metadata and ratings.
 */
public class Book {
    
    private final IntegerProperty book_id;
    private final StringProperty title;
    private final StringProperty author;
    private final StringProperty isbn;
    private final StringProperty genre;
    private final StringProperty language;
    private final IntegerProperty publication_year;
    private final StringProperty description;
    private final StringProperty image_path;
    private final DoubleProperty average_rating;
    private final IntegerProperty review_count;
    private final IntegerProperty quantity;

    /**
     * Default constructor to initialize a new Book object with empty values.
     * This constructor sets all properties to their default values (e.g., empty strings, zero for numbers).
     */
    public Book() {
        this.book_id = new SimpleIntegerProperty();
        this.title = new SimpleStringProperty();
        this.author = new SimpleStringProperty();
        this.isbn = new SimpleStringProperty();
        this.genre = new SimpleStringProperty();
        this.language = new SimpleStringProperty();
        this.publication_year = new SimpleIntegerProperty();
        this.description = new SimpleStringProperty();
        this.image_path = new SimpleStringProperty();
        this.average_rating = new SimpleDoubleProperty();
        this.review_count = new SimpleIntegerProperty();
        this.quantity = new SimpleIntegerProperty();
    }

    /**
     * Constructor to initialize a Book object with specific values.
     *
     * @param book_id         The unique identifier for the book.
     * @param title          The title of the book.
     * @param author         The author of the book.
     * @param isbn           The ISBN (International Standard Book Number) of the book.
     * @param genre          The genre/category of the book.
     * @param language       The language in which the book is written.
     * @param description    A brief description of the book's content.
     * @param publication_year The year the book was published.
     * @param image_path     The file path to the book's cover image.
     * @param average_rating The average rating given to the book by readers.
     * @param review_count   The total number of reviews the book has received.
     * @param quantity       The number of copies of the book available.
     */
    public Book(int book_id, String title, String author, String isbn, String genre, String language,
                String description, int publication_year, String image_path, double average_rating, 
                int review_count, int quantity) {
        this.book_id = new SimpleIntegerProperty(book_id);
        this.title = new SimpleStringProperty(title);
        this.author = new SimpleStringProperty(author);
        this.isbn = new SimpleStringProperty(isbn);
        this.genre = new SimpleStringProperty(genre);
        this.language = new SimpleStringProperty(language);
        this.publication_year = new SimpleIntegerProperty(publication_year);
        this.description = new SimpleStringProperty(description);
        this.image_path = new SimpleStringProperty(image_path);
        this.average_rating = new SimpleDoubleProperty(average_rating);
        this.review_count = new SimpleIntegerProperty(review_count);
        this.quantity = new SimpleIntegerProperty(quantity);
    }


    /**
     * Gets the unique ID of the book.
     *
     * @return The unique identifier for the book.
     */
    public int getBook_id() {
        return book_id.get();
    }

    /**
     * Sets the unique ID of the book.
     *
     * @param value The unique identifier to be assigned to the book.
     */
    public void setBook_id(int value) {
        book_id.set(value);
    }

    /**
     * Gets the property for the book's ID.
     * This method allows access to the property for binding or listening for changes.
     *
     * @return The IntegerProperty representing the book's unique ID.
     */
    public IntegerProperty book_idProperty() {
        return book_id;
    }

    /**
     * Gets the title of the book.
     *
     * @return The title of the book.
     */
    public String getTitle() {
        return title.get();
    }

    /**
     * Sets the title of the book.
     *
     * @param value The title to be assigned to the book.
     */
    public void setTitle(String value) {
        title.set(value);
    }


    /**
     * Gets the property for the book's title.
     * This method allows access to the property for binding or listening for changes.
     *
     * @return The StringProperty representing the book's title.
     */
    public StringProperty titleProperty() {
        return title;
    }

    /**
     * Gets the author of the book.
     *
     * @return The author of the book.
     */
    public String getAuthor() {
        return author.get();
    }

    /**
     * Sets the author of the book.
     *
     * @param value The author to be assigned to the book.
     */
    public void setAuthor(String value) {
        author.set(value);
    }

    /**
     * Gets the property for the book's author.
     * This method allows access to the property for binding or listening for changes.
     *
     * @return The StringProperty representing the book's author.
     */
    public StringProperty authorProperty() {
        return author;
    }

    /**
     * Gets the ISBN of the book.
     *
     * @return The ISBN of the book.
     */
    public String getIsbn() {
        return isbn.get();
    }

    /**
     * Sets the ISBN of the book.
     *
     * @param value The ISBN to be assigned to the book.
     */
    public void setIsbn(String value) {
        isbn.set(value);
    }

    /**
     * Gets the property for the book's ISBN.
     * This method allows access to the property for binding or listening for changes.
     *
     * @return The StringProperty representing the book's ISBN.
     */
    public StringProperty isbnProperty() {
        return isbn;
    }

    /**
     * Gets the genre of the book.
     *
     * @return The genre of the book.
     */
    public String getGenre() {
        return genre.get();
    }


    /**
     * Sets the genre of the book.
     *
     * @param value The genre to be assigned to the book.
     */
    public void setGenre(String value) {
        genre.set(value);
    }

    /**
     * Gets the property for the book's genre.
     * This method allows access to the property for binding or listening for changes.
     *
     * @return The StringProperty representing the book's genre.
     */
    public StringProperty genreProperty() {
        return genre;
    }

    /**
     * Gets the language of the book.
     *
     * @return The language of the book.
     */
    public String getLanguage() {
        return language.get();
    }

    /**
     * Sets the language of the book.
     *
     * @param value The language to be assigned to the book.
     */
    public void setLanguage(String value) {
        language.set(value);
    }

    /**
     * Gets the property for the book's language.
     * This method allows access to the property for binding or listening for changes.
     *
     * @return The StringProperty representing the book's language.
     */
    public StringProperty languageProperty() {
        return language;
    }

    /**
     * Gets the publication year of the book.
     *
     * @return The publication year of the book.
     */
    public int getPublication_year() {
        return publication_year.get();
    }

    /**
     * Sets the publication year of the book.
     *
     * @param value The publication year to be assigned to the book.
     */
    public void setPublication_year(int value) {
        publication_year.set(value);
    }


    /**
     * Gets the property for the book's publication year.
     * This method allows access to the property for binding or listening for changes.
     *
     * @return The IntegerProperty representing the book's publication year.
     */
    public IntegerProperty publication_yearProperty() {
        return publication_year;
    }

    /**
     * Gets the description of the book.
     *
     * @return The description of the book.
     */
    public String getDescription() {
        return description.get();
    }

    /**
     * Sets the description of the book.
     *
     * @param value The description to be assigned to the book.
     */
    public void setDescription(String value) {
        description.set(value);
    }

    /**
     * Gets the property for the book's description.
     * This method allows access to the property for binding or listening for changes.
     *
     * @return The StringProperty representing the book's description.
     */
    public StringProperty descriptionProperty() {
        return description;
    }

    /**
     * Gets the file path to the book's image.
     *
     * @return The file path to the book's image.
     */
    public String getImage_path() {
        return image_path.get();
    }

    /**
     * Sets the file path to the book's image.
     *
     * @param value The file path to be assigned to the book's image.
     */
    public void setImage_path(String value) {
        image_path.set(value);
    }

    /**
     * Gets the property for the book's image path.
     * This method allows access to the property for binding or listening for changes.
     *
     * @return The StringProperty representing the book's image path.
     */
    public StringProperty image_pathProperty() {
        return image_path;
    }

    /**
     * Gets the average rating of the book.
     *
     * @return The average rating of the book.
     */
    public double getAverage_rating() {
        return average_rating.get();
    }

    /**
     * Sets the average rating of the book.
     *
     * @param value The average rating to be assigned to the book.
     */
    public void setAverage_rating(double value) {
        average_rating.set(value);
    }


    /**
     * Gets the property for the book's average rating.
     * This method allows access to the property for binding or listening for changes.
     *
     * @return The DoubleProperty representing the book's average rating.
     */
    public DoubleProperty average_ratingProperty() {
        return average_rating;
    }

    /**
     * Gets the review count for the book.
     *
     * @return The total number of reviews for the book.
     */
    public int getReview_count() {
        return review_count.get();
    }

    /**
     * Sets the review count for the book.
     *
     * @param value The review count to be assigned to the book.
     */
    public void setReview_count(int value) {
        review_count.set(value);
    }

    /**
     * Gets the property for the book's review count.
     * This method allows access to the property for binding or listening for changes.
     *
     * @return The IntegerProperty representing the book's review count.
     */
    public IntegerProperty review_countProperty() {
        return review_count;
    }

    /**
     * Gets the quantity of the book available.
     *
     * @return The quantity of the book in stock.
     */
    public int getQuantity() {
        return quantity.get();
    }

    /**
     * Sets the quantity of the book available.
     *
     * @param value The quantity to be assigned to the book.
     */
    public void setQuantity(int value) {
        quantity.set(value);
    }

    /**
     * Gets the property for the book's quantity.
     * This method allows access to the property for binding or listening for changes.
     *
     * @return The IntegerProperty representing the book's quantity.
     */
    public IntegerProperty quantityProperty() {
        return quantity;
}

}
