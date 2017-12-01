/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import java.io.IOException;
import java.math.BigDecimal;
import models.FictionBook;
import java.net.URL;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Dasha
 */
public class FictionBookshelfViewController implements Initializable {

    @FXML private TableView<FictionBook> bookShelf;
    @FXML private TableColumn<FictionBook, String> titleColumn;
    @FXML private TableColumn<FictionBook, String> authorColumn;
    @FXML private TableColumn<FictionBook, FictionBook.FictionGenre> genreColumn;
    @FXML private TableColumn<FictionBook, BigDecimal> priceColumn;
    @FXML private TableColumn<FictionBook, LocalDate> publicationDateColumn;
    @FXML private TableColumn<FictionBook, Integer> amountInStockColumn;
    
    @FXML private Label totalSaleLabel;
    @FXML private Label bookInStocLabel;
    @FXML private Label bookSoldLabel;
    @FXML private Label totalInventoryPriceLavel;
    
    ObservableList<FictionBook> books;
    
    BigDecimal totalSales = new BigDecimal("0");
    int booksSold = 0;
    int booksInStock = 0;
    BigDecimal totalInventoryPrice = new BigDecimal("0");
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        titleColumn.setCellValueFactory(new PropertyValueFactory<FictionBook, String>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<FictionBook, String>("authorName"));
        genreColumn.setCellValueFactory(new PropertyValueFactory<FictionBook, FictionBook.FictionGenre>("fictionGenre"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<FictionBook, BigDecimal>("price"));
        publicationDateColumn.setCellValueFactory(new PropertyValueFactory<FictionBook, LocalDate>("dateOfPublication"));
        amountInStockColumn.setCellValueFactory(new PropertyValueFactory<FictionBook, Integer>("amountInStock"));
        bookShelf.setItems(getBooks());
       
        ObservableList<FictionBook> books = bookShelf.getItems();
        for(FictionBook book : books){
            booksSold += book.getAmountSold();
            totalInventoryPrice = totalInventoryPrice.add((book.getPrice().multiply(new BigDecimal(book.getAmountInStock()))));
            booksInStock += book.getAmountInStock();
            totalSales = totalSales.add(book.getPrice().multiply(new BigDecimal(book.getAmountSold())));
        }
        
        totalInventoryPriceLavel.setText(currencyFormat(totalInventoryPrice));
        bookSoldLabel.setText(Integer.toString(booksSold));
        bookInStocLabel.setText(Integer.toString(booksInStock));
        totalSaleLabel.setText(currencyFormat(totalSales));
        
   }    

    private ObservableList<FictionBook> getBooks() {
        books = FXCollections.observableArrayList();
        
        //add employees to the list
        books.add(new FictionBook("Harry Potter and the Philosopher's Stone", "J.K. Rowling", FictionBook.FictionGenre.ADVENTURE, "Harry Potter", new BigDecimal("10.00"), LocalDate.of(1997, Month.JUNE, 26), 10, 1));
        books.add(new FictionBook("Java", "JavaJava", FictionBook.FictionGenre.HORROR, "Compiler", new BigDecimal("18.00"), LocalDate.of(2017, Month.NOVEMBER, 29), 2, 3));
        //return the list
        return books;
    }
    
    public void loadBooks(ObservableList<FictionBook> newList)
    {
        this.bookShelf.setItems(newList);
        bookShelf.refresh();
    }
    
    public void addNewBookButtonPushed(ActionEvent event) throws IOException
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("AddNewBookView.fxml"));
        Parent parent = loader.load();
        Scene newBookScene = new Scene(parent);
        
        //access the controller of the newEmployeeScene and send over
        //the current list of employees
        AddNewBookViewController controller = loader.getController();
        controller.initialData(bookShelf.getItems());
        
        //Get the current "stage" (aka window) 
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        
        //change the scene to the new scene
        stage.setTitle("Add new book");
        stage.setScene(newBookScene);
        stage.show();
    }
    
    public void sellBookButtonPushed(ActionEvent event) {
       books = bookShelf.getItems();
       FictionBook currentBook = bookShelf.getSelectionModel().getSelectedItem();
       if (currentBook.getAmountInStock() != 1) {
           currentBook.setAmountInStock(currentBook.getAmountInStock()-1);
       } else {
            books.remove(currentBook);
       }
      
       booksSold++;
       booksInStock--;
       totalInventoryPrice = totalInventoryPrice.subtract(currentBook.getPrice());
       totalSales = totalSales.add(currentBook.getPrice());
       
       
       totalInventoryPriceLavel.setText(currencyFormat(totalInventoryPrice));
       bookSoldLabel.setText(Integer.toString(booksSold));
       bookInStocLabel.setText(Integer.toString(booksInStock));
       totalSaleLabel.setText(currencyFormat(totalSales));
       bookShelf.refresh();
    }
    
    public static String currencyFormat(BigDecimal n) {
        return NumberFormat.getCurrencyInstance().format(n);
    }
}
