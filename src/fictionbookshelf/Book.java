package fictionbookshelf;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javax.imageio.ImageIO;

/**
 *
 * @author Dasha
 * 
 * This is the class with the book model
 */
public class Book {

    private static int nextBookId = 1;
    private int bookId;
    protected double price;
    protected String title, authorName;

    private enum Genre {
        COMEDY, DRAMA, HORROR, TRAGEDY, FICTION, BIOGRAPHY
    };
    protected Genre genre;
    protected LocalDate dateOfPublication;
    protected Image cover;

    /**
     * Default constructor
     * @param title
     * @param authorName
     * @param genre
     * @param price
     * @param dateOfPublication 
     */
    public Book(String title, String authorName, Enum genre, double price, LocalDate dateOfPublication) {
        bookId = nextBookId;
        nextBookId++;
        setTitle(title);
        setAuthorName(authorName);
        this.genre = Genre.FICTION;
        setPrice(price);
        setDateOfPublication(dateOfPublication);
        try {
            BufferedImage bufferedImage = ImageIO.read(new File("./src/images/placeholder-cover.jpg"));
            cover = SwingFXUtils.toFXImage(bufferedImage, null);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Default constructor with image cover
     * @param title
     * @param authorName
     * @param genre
     * @param price
     * @param dateOfPublication
     * @param cover 
     */
    public Book(String title, String authorName, Enum genre, double price,
            LocalDate dateOfPublication, Image cover) {
        this(title, authorName, genre, price, dateOfPublication);
        this.cover = cover;
    }

    /**
     * This method validate the price. If the price is less then 0, 
     * Illegal argument exception is thrown.
     * @param price 
     */
    public void setPrice(double price) {
        if (price > 0) {
            this.price = price;
        } else {
            throw new IllegalArgumentException("Price should be greater then 0");
        }
    }

    /**
     * This method validate the title parameter. The first letter should be 
     * capital or the Illegal argument exception is thrown.
     * @param title 
     */
    public void setTitle(String title) {
        if (title.matches("[A-Z][a-zA-Z\\-]*?")) {
            this.title = title;
        } else {
            throw new IllegalArgumentException("Title must start with an upper case"
                    + "letter, followed by letters or -");
        }
    }

    /**
     * This method validate the authors name parameter. The first letter should be 
     * capital or the Illegal argument exception is thrown.
     * @param authorName 
     */
    public void setAuthorName(String authorName) {
        if (authorName.matches("[A-Z][a-zA-Z\\-]*?")) {
            this.authorName = authorName;
        } else {
            throw new IllegalArgumentException("Author's name must start with an upper case"
                    + "letter, followed by letters or -");
        }
    }

    /**
     * This method validate the date of publication. It should be not greater then now.
     * @param dateOfPublication 
     */
    public void setDateOfPublication(LocalDate dateOfPublication) {
        if (dateOfPublication.isBefore(LocalDate.now()) || dateOfPublication.isEqual(LocalDate.now())) {
            this.dateOfPublication = dateOfPublication;
        } else {
            throw new IllegalArgumentException("The book can't be from the future");
        }
    }

    public int getBookId() {
        return bookId;
    }

    public double getPrice() {
        return price;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthorName() {
        return authorName;
    }

    public Genre getGenre() {
        return genre;
    }

    public LocalDate getDateOfPublication() {
        return dateOfPublication;
    }

    public Image getCover() {
        return cover;
    }
    
    /**
     * This method returns Book title by authorName in genre genre cost 
     * price, publication date is dateOfPublication
     * @return 
     */

    @Override
    public String toString() {
        return "Book " + title + " by " + authorName + " in genre " + genre + 
                " cost " + price + ", publication date is " + dateOfPublication;
    }

    
}
