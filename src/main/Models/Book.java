package main.Models;

import javafx.beans.property.*;

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

    public Book(int book_id, String title, String author, String isbn, String genre, String language,
            String description, int publication_year, String image_path, double average_rating, int review_count,
            int quantity) {
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

    public int getBook_id() {
        return book_id.get();
    }

    public void setBook_id(int value) {
        book_id.set(value);
    }

    public IntegerProperty book_idProperty() {
        return book_id;
    }

    public String getTitle() {
        return title.get();
    }

    public void setTitle(String value) {
        title.set(value);
    }

    public StringProperty titleProperty() {
        return title;
    }

    public String getAuthor() {
        return author.get();
    }

    public void setAuthor(String value) {
        author.set(value);
    }

    public StringProperty authorProperty() {
        return author;
    }

    public String getIsbn() {
        return isbn.get();
    }

    public void setIsbn(String value) {
        isbn.set(value);
    }

    public StringProperty isbnProperty() {
        return isbn;
    }

    public String getGenre() {
        return genre.get();
    }

    public void setGenre(String value) {
        genre.set(value);
    }

    public StringProperty genreProperty() {
        return genre;
    }

    public String getLanguage() {
        return language.get();
    }

    public void setLanguage(String value) {
        language.set(value);
    }

    public StringProperty languageProperty() {
        return language;
    }

    public int getPublication_year() {
        return publication_year.get();
    }

    public void setPublication_year(int value) {
        publication_year.set(value);
    }

    public IntegerProperty publication_yearProperty() {
        return publication_year;
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String value) {
        description.set(value);
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public String getImagePath() {
        return image_path.get();
    }

    public void setImage_path(String value) {
        image_path.set(value);
    }

    public StringProperty image_pathProperty() {
        return image_path;
    }

    public double getAverage_rating() {
        return average_rating.get();
    }

    public void setAverage_rating(double value) {
        average_rating.set(value);
    }

    public DoubleProperty average_ratingProperty() {
        return average_rating;
    }

    public int getReview_count() {
        return review_count.get();
    }

    public void setReview_count(int value) {
        review_count.set(value);
    }

    public IntegerProperty review_countProperty() {
        return review_count;
    }

    public int getQuantity() {
        return quantity.get();
    }

    public void setQuantity(int value) {
        quantity.set(value);
    }

    public IntegerProperty quantityProperty() {
        return quantity;
    }
}
