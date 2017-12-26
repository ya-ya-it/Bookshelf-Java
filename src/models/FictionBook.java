package models;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

/**
 *
 * @author Dasha
 *
 * This is the class with the book model
 */
public class FictionBook extends Book {

    private String mainCharacter;

    public enum FictionGenre {
        MAGIC, APOCALYPSYS, HORROR, ADVENTURE, MYSTERY, MYTHOLOGY, PAST
    };
    private FictionGenre fictionGenre;

    /**
     * Default constructor
     *
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
     *
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
     * This method validate the main character's name parameter. The first
     * letter should be capital or the Illegal argument exception is thrown.
     *
     * @param mainCharacter
     */
    public void setMainCharacter(String mainCharacter) {
        if (mainCharacter.isEmpty()) {
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
     * This method returns fiction book title by authorName in genre
     * fictionGenre cost price, publication date is dateOfPublication
     *
     * @return
     */
    @Override
    public String toString() {
        return "Book " + title + " by " + authorName + " in genre "
                + fictionGenre + " cost " + price + ", publication date is " + dateOfPublication;
    }

    /**
     * This method inserts new book into the db
     *
     * @throws SQLException
     */
    public void insertIntoDB() throws SQLException {
        Connection conn = null;
        PreparedStatement preparedStatement = null;

        try {
            //1. Connect to the database
            conn = DriverManager.getConnection("jdbc:mysql://localhost:8889/fictionBookshelf?useSSL=false", "root", "root");

            //2. Create a String that holds the query with ? as user inputs
            String sql = " INSERT INTO fictionBooks (title, authorName, fictionGenre, mainCharacters, price, dateOfPublication, bookCover) VALUES"
                    + "(?, ?, ?, ?, ?, ?, ?); ";

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
            preparedStatement.setString(7, cover.getName());

            preparedStatement.executeUpdate();
            //insert info about inventory
            insertIntoInventory();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }

            if (conn != null) {
                conn.close();
            }
        }
    }

    /**
     * This method inserts inventory info about the new book into the db
     *
     * @throws SQLException
     */
    public void insertIntoInventory() throws SQLException {
        Connection conn = null;
        PreparedStatement preparedStatement = null;

        try {
            //1. Connect to the database
            conn = DriverManager.getConnection("jdbc:mysql://localhost:8889/fictionBookshelf?useSSL=false", "root", "root");

            //2. Create a String that holds the query with ? as user inputs
            String sql = " INSERT INTO inventory (bookId, amountInStock, amountSold) VALUES "
                    + "(?, ?, ?); ";

            //3. prepare the query
            preparedStatement = conn.prepareStatement(sql);

            //4. Convert the dates into a SQL date
            Date dop = Date.valueOf(dateOfPublication);

            //5. Bind the values to the parameters
            preparedStatement.setInt(1, getLastBookId());
            preparedStatement.setInt(2, getAmountInStock());
            preparedStatement.setInt(3, getAmountSold());

            preparedStatement.executeUpdate();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }

            if (conn != null) {
                conn.close();
            }
        }
    }

    /**
     * This method returns the id of the last added book
     *
     * @return
     * @throws SQLException
     */
    public int getLastBookId() throws SQLException {
        Connection conn = null;
        Statement statement = null;
        ResultSet resultSet = null;
        int bookId = 0;

        try {
            //1. connect to the database
            conn = DriverManager.getConnection("jdbc:mysql://localhost:8889/fictionBookShelf?useSSL=false", "root", "root");

            //2.  create a statement object
            statement = conn.createStatement();

            //3.  create the SQL query
            resultSet = statement.executeQuery("SELECT * FROM fictionBooks ORDER BY bookId DESC LIMIT 1;");

            //4.  create book objects from each record
            while (resultSet.next()) {
                bookId = resultSet.getInt("bookId");
            }
        } catch (Exception e) {
            System.err.println(e);
        } finally {
            if (conn != null) {
                conn.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (resultSet != null) {
                resultSet.close();
            }
        }
        return bookId;
    }
}
