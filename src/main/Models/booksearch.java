package main.Models;
import java.sql.*;
import java.util.*;
public class booksearch{
    Integer bookid;
    String title, author, isbn, genre, language, description;
    Integer publication_year;
    String image_url;
    public booksearch(Integer bookid, String title, String author 
                      ,String isbn, String genre, String language 
                      ,String description, Integer publication_year
                      ,String image_url){
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
    public Integer getbookid() {
        return bookid;
    }

    public void setbookid(Integer bookid) {
        this.bookid = bookid;
    }

    public String gettitle() {
        return title;
    }

    public void settitle(String title) {
        this.title = title;
    }

    public String getauthor() {
        return author;
    }

    public void setauthor(String author) {
        this.author = author;
    }

    public String getisbn() {
        return isbn;
    }

    public void setisbn(String isbn) {
        this.isbn = isbn;
    }

    public String getgenre() {
        return genre;
    }

    public void setgenre(String genre) {
        this.genre = genre;
    }

    public String getlanguage() {
        return language;
    }

    public void setlanguage(String language) {
        this.language = language;
    }

    public String getdescription() {
        return description;
    }

    public void setdescription(String description) {
        this.description = description;
    }

    public Integer getpublication_year() {
        return publication_year;
    }

    public void setpublication_year(Integer publication_year) {
        this.publication_year = publication_year;
    }

    public String getimage_url() {
        return image_url;
    }

    public void setimage_url(String image_url) {
        this.image_url = image_url;
    }
}