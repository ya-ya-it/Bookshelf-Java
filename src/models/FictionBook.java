package models;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import views.SceneChanger;

/**
 *
 * @author Dasha
 * 
 * This is the class with the book model
 */
public class FictionBook extends Book{
    
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
            String mainCharacter, BigDecimal price, LocalDate dateOfPublication, 
            int amountInStock, int amountSold) {
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
            String mainCharacter, BigDecimal price, LocalDate dateOfPublication, 
            int amountInStock, int amountSold, File cover) {
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
    
    public void insertIntoDB() throws SQLException
    {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        
        try
        {
            //1. Connect to the database
            conn = DriverManager.getConnection("jdbc:mysql://localhost:8889/fictionBookshelf?useSSL=false", "root", "root");
            
            //2. Create a String that holds the query with ? as user inputs
            String sql = " INSERT INTO fictionBooks (title, authorName, fictionGenre, mainCharacters, price, dateOfPublication, amountInStock, amountSold, bookCover, userId) VALUES" +
"                       (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
                    
            //3. prepare the query
            preparedStatement = conn.prepareStatement(sql);
            
            //4. Convert the dates into a SQL date
            Date dop = Date.valueOf(dateOfPublication);
            
               
            //5. Bind the values to the parameters
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, authorName);
            preparedStatement.setString(3, fictionGenre.toString());
            preparedStatement.setString(4, mainCharacter);
            preparedStatement.setBigDecimal(5, price);
            preparedStatement.setDate(6, dop);
            preparedStatement.setInt(7, getAmountInStock());
            preparedStatement.setInt(8, getAmountSold());
            preparedStatement.setString(9, cover.getName());
            preparedStatement.setInt(10, SceneChanger.getLoggedInUser().getUserId());
            
            preparedStatement.executeUpdate();
        }
        catch (Exception e)
        {
            System.err.println(e.getMessage());
        }
        finally
        {
            if (preparedStatement != null)
                preparedStatement.close();
            
            if (conn != null)
                conn.close();
        }
    }
}
