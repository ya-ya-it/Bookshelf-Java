package models;

import java.time.LocalDate;
import javafx.scene.image.Image;

/**
 *
 * @author Dasha
 * 
 * This is the class with the book model
 */
public class FictionBook extends Book{
    
    public FictionBook(String title, String authorName, Genre genre, double price,
            LocalDate dateOfPublication, int amountInStock, int amountSold) {
        super(title, authorName, genre, price, dateOfPublication, amountInStock, amountSold);
        super.genre = Book.Genre.FICTION;
    }
    private String mainCharacter;
    public enum FictionGenre {
        MAGIC, APOCALYPSYS, HORROR, ADVENTURE, MYSTERY, MYTHOLOGY, PAST
    };
    private FictionGenre fictionGenre;

    /**
     * Default constructor
     * @param title
     * @param authorName
     * @param fictionGenre
     * @param mainCharacter
     * @param price
     * @param amountInStock
     * @param dateOfPublication 
     * @param amountSold 
     */
    public FictionBook(String title, String authorName, FictionGenre fictionGenre,
            String mainCharacter, double price, LocalDate dateOfPublication, int amountInStock, int amountSold) {
        super(title, authorName, genre, price, dateOfPublication, amountInStock, amountSold);
        super.genre = Book.Genre.FICTION;
        setMainCharacter(mainCharacter);
        setFictionGenre((FictionGenre) fictionGenre);
    }

    /**
     * Default constructor with image cover
     * @param title
     * @param authorName
     * @param fictionGenre
     * @param mainCharacter
     * @param price
     * @param amountInStock
     * @param dateOfPublication
     * @param amountSold
     * @param cover 
     */
    public FictionBook(String title, String authorName, FictionGenre fictionGenre,
            String mainCharacter, double price, LocalDate dateOfPublication, int amountInStock, int amountSold, Image cover) {
        super(title, authorName, genre, price, dateOfPublication, amountInStock, amountSold);      
        super.genre = Book.Genre.FICTION;
        setMainCharacter(mainCharacter);
        setFictionGenre((FictionGenre) fictionGenre);
    }

    /**
     * This method validate the main character's name parameter. The first letter should be 
     * capital or the Illegal argument exception is thrown.
     * @param mainCharacter
     */
    public void setMainCharacter(String mainCharacter) {
        if(mainCharacter.isEmpty()) {
            throw new IllegalArgumentException("Please, enter the main character name");
        } else if (!mainCharacter.matches("[A-Z].*")) {
            throw new IllegalArgumentException("Name of the main character must start with an upper case");
        } else {
            this.mainCharacter = mainCharacter;
        }
    }

    public void setFictionGenre(FictionGenre fictionGenre) {
        this.fictionGenre = fictionGenre;
    }

    public String getMainCharacter() {
        return mainCharacter;
    }

    public FictionGenre getFictionGenre() {
        return fictionGenre;
    }

    /**
     * This method returns fiction book title by authorName in genre fictionGenre cost 
     * price, publication date is dateOfPublication
     * @return 
     */
    @Override
    public String toString() {
        return "Book " + title + " by " + authorName + " in genre " + 
                fictionGenre + " cost " + price + ", publication date is " + dateOfPublication;
    }
    
}
