/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import models.FictionBook;
import static views.FictionBookshelfViewController.currencyFormat;

/**
 * FXML Controller class
 *
 * @author dasha
 */
public class SalesReviewViewController implements Initializable {

    
    @FXML private LineChart<?, ?> salesChart;
    @FXML private CategoryAxis Xaxis;
    @FXML private NumberAxis Yaxis;
          private XYChart.Series salesLoggedSeries;
    
    @FXML private Label totalSalesLabel;
    @FXML private Label inventoryPriceLabel;
    @FXML private Label booksInStockLabel;
    @FXML private Label bookSoldLabel;

    @FXML private ComboBox<Integer> yearCombobox;
    
    ObservableList<FictionBook> books;

    BigDecimal totalSales = new BigDecimal("0");
    int booksSold = 0;
    int booksInStock = 0;
    BigDecimal totalInventoryPrice = new BigDecimal("0");
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            books = loadBooks();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        // set up labels with business information
        for (FictionBook book : books) {
            booksSold += book.getAmountSold();
            totalInventoryPrice = totalInventoryPrice.add((book.getPrice().multiply(new BigDecimal(book.getAmountInStock()))));
            booksInStock += book.getAmountInStock();
            totalSales = totalSales.add(book.getPrice().multiply(new BigDecimal(book.getAmountSold())));
        }

        inventoryPriceLabel.setText(currencyFormat(totalInventoryPrice));
        bookSoldLabel.setText(Integer.toString(booksSold));
        booksInStockLabel.setText(Integer.toString(booksInStock));
        totalSalesLabel.setText(currencyFormat(totalSales));
        
        //set up chart
        updateLineChart();
    }    
    public void backButtonPushed(ActionEvent event) throws IOException {
        SceneChanger sc = new SceneChanger();
        sc.changeScenes(event, "FictionBookshelfView.fxml", "Bookshelf");
    }
    
    /**
     * This method load new books to the ObservableList
     *
     * @param newList
     */
    public ObservableList<FictionBook> loadBooks() throws SQLException {
        ObservableList<FictionBook> localBooks = FXCollections.observableArrayList();

        Connection conn = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            //1. connect to the database
            conn = DriverManager.getConnection("jdbc:mysql://localhost:8889/fictionBookShelf?useSSL=false", "root", "root");
            
            //2.  create a statement object
            statement = conn.createStatement();

            //3.  create the SQL query
            resultSet = statement.executeQuery("SELECT * FROM fictionBooks");

            //4.  create book objects from each record
            while (resultSet.next()) {

                //title, author, genre, mainCharacters,price, 
                //dateOfPublication, amountInStock,amountSold,bookCover
                FictionBook book = new FictionBook(resultSet.getString("title"),
                        resultSet.getString("authorName"),
                        FictionBook.FictionGenre.valueOf(resultSet.getString("fictionGenre")),
                        resultSet.getString("mainCharacters"),
                        resultSet.getBigDecimal("price"),
                        resultSet.getDate("dateOfPublication").toLocalDate(),
                        resultSet.getInt("amountInStock"),
                        resultSet.getInt("amountSold"));
                book.setBookId(resultSet.getInt("bookId"));

                localBooks.add(book);
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
        
        return localBooks;
    }
    
    /**
     * The goal of this method is to update the line chart with the latest
     * information stored in the database
     */
    public void updateLineChart() {
        //initialize the instance variables for the chart
        salesLoggedSeries = new XYChart.Series<>();
        salesLoggedSeries.setName(Integer.toString(LocalDate.now().getYear()));
        salesChart.getData().clear();
        
        try{
            populateSeriesFromDB();
        }
        catch (SQLException e)
        {
            System.err.println(e.getMessage());
        }
        salesChart.getData().addAll(salesLoggedSeries);
    }
    
    /**
     * This method will populate the sales with the latest info
     * from the database
     */
    public void populateSeriesFromDB() throws SQLException
    {
        Connection conn=null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try{
            //1.  Connect to the database
            conn = DriverManager.getConnection("jdbc:mysql://localhost:8889/fictionBookShelf?useSSL=false", "root", "root");
            
            //2. create a String with the sql statement
            String sql =    "SELECT MONTHNAME(dateSold), SUM(amountSold) " +
                            "FROM fictionBooks " +
                            "WHERE userId=? AND YEAR(dateSold)=? " +
                            "GROUP BY MONTH(dateSold);";
            
            //3. create the statement
            statement = conn.prepareCall(sql);
            
            //4. bind the parameters
            statement.setInt(1, SceneChanger.getLoggedInUser().getUserId());
            statement.setInt(2, LocalDate.now().getYear());
            
            //5. execute the query
            resultSet = statement.executeQuery();
            
            //6. loop over the result set and build the series
            while (resultSet.next())
            {
                salesLoggedSeries.getData().add(new XYChart.Data(resultSet.getString(1), resultSet.getInt(2)));
            }
        }
        catch (SQLException e)
        {
            System.err.println(e.getMessage());
        }
        finally
        {
            if (conn != null)
                conn.close();
            if (statement != null)
                statement.close();
            if (resultSet != null)
                resultSet.close();
        }
    }
}
