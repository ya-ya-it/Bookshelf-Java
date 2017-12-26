package views;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import models.FictionBook;
import models.User;
import static views.FictionBookshelfViewController.currencyFormat;

/**
 * FXML Sale books controller class
 *
 * @author dasha
 */
public class SaleBooksViewController implements Initializable, ControllerClass {

    @FXML private TextField titleTextField;
    @FXML private TextField authorTextField;
    @FXML private TextField priceTextField;

    @FXML private Spinner<Integer> amountSoldSpinner;

    @FXML private DatePicker dateSoldDatePicker;

    @FXML private Label errorMsgLabel;
    @FXML private Label nameLabel;

    private FictionBook book;
    private int amountInStock;
    private int amountSold;
    private int bookId;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        nameLabel.setText(SceneChanger.getLoggedInUser().getUsername());
        errorMsgLabel.setText("");

        titleTextField.setEditable(false);
        authorTextField.setEditable(false);
        priceTextField.setEditable(false);

        SpinnerValueFactory<Integer> amountSoldValueFactory
                = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10000, 1);
        amountSoldSpinner.setValueFactory(amountSoldValueFactory);
        
        dateSoldDatePicker.setValue(LocalDate.now());
    }

    /**
     * This method returns scene to the Bookshelf without saving data
     *
     * @param event
     * @throws IOException
     */
    public void backButtonPushed(ActionEvent event) throws IOException {
        SceneChanger sc = new SceneChanger();
        sc.changeScenes(event, "FictionBookshelfView.fxml", "Bookshelf");
    }

    /**
     * This method checks fields and throws errors if data are incorrect. If
     * data are correct, it saves to the database
     *
     * @param event
     * @throws IOException
     */
    public void saveBookButtonPushed(ActionEvent event) throws IOException, SQLException {
        try {
            if (dateSoldDatePicker.getValue() == null) {
                throw new IllegalArgumentException("Please choose a date of publication");
            } else if (dateSoldDatePicker.getValue().isAfter(LocalDate.now())) {
                throw new IllegalArgumentException("Please enter the correct date");
            } else {
                if (book.getAmountInStock() > 1) {
                    sellBookFromDB();
                    errorMsgLabel.setText("You successfully sell the book");
                    System.out.println(dateSoldDatePicker.getValue());
                } else {
                    deleteBookFromDB();
                    errorMsgLabel.setText("You successfully sell the last book.");
                     System.out.println(dateSoldDatePicker.getValue());
                }
            }

        } catch (IllegalArgumentException e) {
            errorMsgLabel.setText(e.getMessage());
        }
    }

    /**
     * This method removes selected item from database if the amount of books is
     * 1
     */
    public void deleteBookFromDB() throws SQLException {
        Connection conn = null;
        PreparedStatement statement = null;

        try {
            //1.  Connect to the database
            conn = DriverManager.getConnection("jdbc:mysql://localhost:8889/fictionBookShelf?useSSL=false", "root", "root");

            //2. create a String with the sql statement
            String sql = "DELETE FROM fictionBooks "
                    + " WHERE bookId = ?;";

            //3. create the statement
            statement = conn.prepareCall(sql);

            //4. bind the parameters
            statement.setInt(1, book.getBookId());

            //5. execute the query
            statement.executeUpdate();

            saveTransactionIntoDB();

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            if (conn != null) {
                conn.close();
            }
            if (statement != null) {
                statement.close();
            }
        }
    }

    /**
     * This method decreases the number of books sold if the amount of books is
     * more than 1
     */
    public void sellBookFromDB() throws SQLException {
        Connection conn = null;
        PreparedStatement statement = null;

        int amountInStockNew = amountInStock - amountSoldSpinner.getValue();
        int amountSoldNew = amountSold + amountSoldSpinner.getValue();

        try {
            //1.  Connect to the database
            conn = DriverManager.getConnection("jdbc:mysql://localhost:8889/fictionBookShelf?useSSL=false", "root", "root");

            //2. create a String with the sql statement
            String sql = " UPDATE inventory "
                    + " SET amountSold = ?, amountInStock = ? "
                    + " WHERE bookId = ?;";

            //3. create the statement
            statement = conn.prepareCall(sql);

            //4. bind the parameters
            statement.setInt(1, amountSoldNew);
            statement.setInt(2, amountInStockNew);
            statement.setInt(3, book.getBookId());

            //5. execute the query
            statement.executeUpdate();

            saveTransactionIntoDB();

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            if (conn != null) {
                conn.close();
            }
            if (statement != null) {
                statement.close();
            }
        }
    }

    /**
     * This method saves the transaction to the sales table in db
     * @throws SQLException 
     */
    public void saveTransactionIntoDB() throws SQLException {
        Connection conn = null;
        PreparedStatement preparedStatement = null;

        try {
            //1. Connect to the database
            conn = DriverManager.getConnection("jdbc:mysql://localhost:8889/fictionBookshelf?useSSL=false", "root", "root");

            //2. Create a String that holds the query with ? as user inputs
            String sql = " INSERT INTO SALES (amountSold, dateSold, userId, bookId) VALUES "
                    + " (?, ?, ?, ?); ";

            //3. prepare the query
            preparedStatement = conn.prepareStatement(sql);

            //4. Convert the dates into a SQL date
            Date dateSold = Date.valueOf(dateSoldDatePicker.getValue());

            //5. Bind the values to the parameters
            preparedStatement.setInt(1, amountSoldSpinner.getValue());
            preparedStatement.setDate(2, dateSold);
            preparedStatement.setInt(3, SceneChanger.getLoggedInUser().getUserId());
            preparedStatement.setInt(4, bookId);

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

    @Override
    public void preloadData(FictionBook book) {
        this.book = book;
        this.titleTextField.setText(book.getTitle());
        this.authorTextField.setText(book.getAuthorName());
        this.priceTextField.setText(currencyFormat(book.getPrice()));
        this.amountInStock = book.getAmountInStock();
        this.amountSold = book.getAmountSold();
        this.bookId = book.getBookId();
    }

    @Override
    public void preloadData(User user) {
    }

}
