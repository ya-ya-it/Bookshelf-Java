/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

    @FXML
    private TextField titleTextField;
    @FXML
    private TextField authorTextField;
    @FXML
    private TextField priceTextField;

    @FXML
    private Spinner<Integer> amountSoldSpinner;

    @FXML
    private DatePicker dateSoldDatePicker;

    @FXML
    private Label errorMsgLabel;
    @FXML
    private Label nameLabel;

    FictionBook book;
    int amountInStock;
    int amountSold;
    int bookId;

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
            LocalDate dateSold;
            if (dateSoldDatePicker.getValue() == null) {
                throw new IllegalArgumentException("Please choose a date of publication");
            } else if (dateSoldDatePicker.getValue().isAfter(LocalDate.now())) {
                throw new IllegalArgumentException("Please enter the correct date");
            } else {
                dateSold = dateSoldDatePicker.getValue();
            }
            
            if (book.getAmountInStock() > 1) {
                sellBookFromDB();
                errorMsgLabel.setText("You successfully sell the book");
            } else {
                deleteBookFromDB();
                errorMsgLabel.setText("You successfully sell the last book.");
            }
        } catch (IllegalArgumentException e) {
            errorMsgLabel.setText(e.getMessage());
        }
    }

    /**
     * This method removes selected item from database if the amount of books is 1
     */
    public void deleteBookFromDB() throws SQLException {
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            //1.  Connect to the database
            conn = DriverManager.getConnection("jdbc:mysql://localhost:8889/fictionBookShelf?useSSL=false", "root", "root");

            //2. create a String with the sql statement
            String sql = "DELETE FROM fictionBooks " +
                         " WHERE bookId = ?;";

            //3. create the statement
            statement = conn.prepareCall(sql);

            //4. bind the parameters
            statement.setInt(1, book.getBookId());

            //5. execute the query
            resultSet = statement.executeQuery();

        } catch (SQLException e) {
            System.err.println(e.getMessage());
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
    }
    
    /**
     * This method decreases the number of books sold if the amount of books is 
     * more than 1
     */
    public void sellBookFromDB() throws SQLException{
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        int amountInStockNew = amountInStock - amountSoldSpinner.getValue();
        int amountSoldNew = amountSold + amountSoldSpinner.getValue();

        try {
            //1.  Connect to the database
            conn = DriverManager.getConnection("jdbc:mysql://localhost:8889/fictionBookShelf?useSSL=false", "root", "root");

            //2. create a String with the sql statement
            String sql = " UPDATE fictionBooks " +
                         " SET amountSold = ?, amountInStock = ? " +
                         " WHERE bookId = ?;";

            //3. create the statement
            statement = conn.prepareCall(sql);

            //4. bind the parameters
            statement.setInt(1, amountSoldNew);
            statement.setInt(2, amountInStockNew);
            statement.setInt(3, book.getBookId());

            //5. execute the query
            resultSet = statement.executeQuery();

        } catch (SQLException e) {
            System.err.println(e.getMessage());
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
