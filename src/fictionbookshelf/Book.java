/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fictionbookshelf;

import java.time.LocalDate;
import javafx.scene.image.Image;

/**
 *
 * @author Dasha
 */
public class Book {
    private static int nextBookId = 1;
    private int bookId;
    private double price;
    private String title, authorName;
    private enum Genre {
        COMEDY, DRAMA, HORROR, TRAGEDY, FICTION, BIOGRAPHY
    };
    private Genre genre;
    private LocalDate yearOfPublication;
    private Image cover;

    public Book(String title, String authorName, Genre genre, double price,
            LocalDate yearOfPublication) {
        bookId = nextBookId;
        nextBookId++;
        setTitle(title);
        setAuthorName(authorName);
        this.genre = genre;
        setPrice(price);
        setYearOfPublication(yearOfPublication);
    }
    public FictionBook(String title, String authorName, Enum genre, double price,
            LocalDate yearOfPublication, Image cover) {
        this(title, authorName, genre, price, yearOfPublication);
        this.cover = cover;
    }
    
    
}
