/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import java.io.IOException;
import models.FictionBook;
import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

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
    @FXML private TableColumn<FictionBook, Double> priceColumn;
    @FXML private TableColumn<FictionBook, LocalDate> publicationDateColumn;
    @FXML private TableColumn<FictionBook, Integer> amountInStockColumn;
    
    @FXML private Label totalSaleLabel;
    @FXML private Label bookInStocLabel;
    @FXML private Label bookSoldLabel;
    @FXML private Label totalInventoryPriceLavel;
    
    double totalSales = 0;
    int booksSold = 0;
    int booksInStock = 0;
    double totalInventoryPrice = 0;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        titleColumn.setCellValueFactory(new PropertyValueFactory<FictionBook, String>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<FictionBook, String>("authorName"));
        genreColumn.setCellValueFactory(new PropertyValueFactory<FictionBook, FictionBook.FictionGenre>("fictionGenre"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<FictionBook, Double>("price"));
        publicationDateColumn.setCellValueFactory(new PropertyValueFactory<FictionBook, LocalDate>("dateOfPublication"));
        amountInStockColumn.setCellValueFactory(new PropertyValueFactory<FictionBook, Integer>("amountInStock"));
        bookShelf.setItems(getBooks());
       
        ObservableList<FictionBook> books = bookShelf.getItems();
        for(FictionBook book : books){
            booksSold += book.getAmountSold();
            totalInventoryPrice += book.getPrice() * book.getAmountInStock();
            booksInStock += book.getAmountInStock();
            totalSales += book.getPrice() * book.getAmountSold();
        }
        
        totalInventoryPriceLavel.setText(Double.toString(totalInventoryPrice));
        bookSoldLabel.setText(Integer.toString(booksSold));
        bookInStocLabel.setText(Integer.toString(booksInStock));
        totalSaleLabel.setText(Double.toString(totalSales));
        
   }    

    private ObservableList<FictionBook> getBooks() {
         ObservableList<FictionBook> books = FXCollections.observableArrayList();
        
        //add employees to the list
        books.add(new FictionBook("Harry Potter and the Philosopher's Stone", "J.K. Rowling", FictionBook.FictionGenre.ADVENTURE, "Harry Potter", 10.00, LocalDate.of(1997, Month.JUNE, 26), 10, 1));
        books.add(new FictionBook("Java", "JavaJava", FictionBook.FictionGenre.HORROR, "Compiler", 8.00, LocalDate.of(2017, Month.NOVEMBER, 29), 2, 3));
        //return the list
        return books;
    }
    
    public void addNewBookButtonPushed(ActionEvent event) throws IOException
    {
        SceneChanger sc = new SceneChanger();
        sc.changeScenes(event, "AddNewBookView.fxml", "Add new book");
    }
    
    public void sellBookButtonPushed(ActionEvent event) {
       ObservableList<FictionBook> books = bookShelf.getItems();
       FictionBook currentBook = bookShelf.getSelectionModel().getSelectedItem();
       if (currentBook.getAmountInStock() != 1) {
           currentBook.setAmountInStock(currentBook.getAmountInStock()-1);
       } else {
            books.remove(currentBook);
       }
      
       booksSold++;
       booksInStock--;
       totalInventoryPrice = totalInventoryPrice - currentBook.getPrice();
       totalSales = totalSales + currentBook.getPrice();
       
       
       totalInventoryPriceLavel.setText(Double.toString(totalInventoryPrice));
       bookSoldLabel.setText(Integer.toString(booksSold));
       bookInStocLabel.setText(Integer.toString(booksInStock));
       totalSaleLabel.setText(Double.toString(totalSales));
       bookShelf.refresh();
    }
}
