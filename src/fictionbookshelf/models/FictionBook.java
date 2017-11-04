package fictionbookshelf.models;

import java.time.LocalDate;
import javafx.scene.image.Image;

/**
 *
 * @author Dasha
 * 
 * This is the class with the book model
 */
public class FictionBook extends Book{
    
    public FictionBook(String title, String authorName, Enum genre, double price, 
            LocalDate dateOfPublication) {
        super(title, authorName, genre, price, dateOfPublication);
    }
    private String mainCharacter;
    private enum FictionGenre {
        MAGIC, APOCALIPSIS, HORROR, ADVENTURE, MYSTERY, MYTHOLOGY, PAST
    };
    private FictionGenre fictionGenre;

    /**
     * Default constructor
     * @param title
     * @param authorName
     * @param genre
     * @param fictionGenre
     * @param mainCharacter
     * @param price
     * @param dateOfPublication 
     */
    public FictionBook(String title, String authorName, Enum genre, Enum fictionGenre,
            String mainCharacter, double price, LocalDate dateOfPublication) {
        super(title, authorName, genre, price, dateOfPublication);
        setMainCharacter(mainCharacter);
        setFictionGenre((FictionGenre) fictionGenre);
    }

    /**
     * Default constructor with image cover
     * @param title
     * @param authorName
     * @param genre
     * @param fictionGenre
     * @param mainCharacter
     * @param price
     * @param dateOfPublication
     * @param cover 
     */
    public FictionBook(String title, String authorName, Enum genre, Enum fictionGenre,
            String mainCharacter, double price, LocalDate dateOfPublication, Image cover) {
        super(title, authorName, genre, price, dateOfPublication, cover);
        setMainCharacter(mainCharacter);
        setFictionGenre((FictionGenre) fictionGenre);
    }

    /**
     * This method validate the main character's name parameter. The first letter should be 
     * capital or the Illegal argument exception is thrown.
     * @param authorName 
     */
    public void setMainCharacter(String mainCharacter) {
        if (mainCharacter.matches("[A-Z][a-zA-Z\\-]*?")) {
            this.mainCharacter = mainCharacter;
        } else {
            throw new IllegalArgumentException("Name of the main character must"
                    + "start with an upper case letter, followed by letters or -");
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
