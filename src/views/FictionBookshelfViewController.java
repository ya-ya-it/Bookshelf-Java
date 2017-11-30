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
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        titleColumn.setCellValueFactory(new PropertyValueFactory<FictionBook, String>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<FictionBook, String>("authorName"));
        genreColumn.setCellValueFactory(new PropertyValueFactory<FictionBook, FictionBook.FictionGenre>("fictionGenre"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<FictionBook, Double>("price"));
        publicationDateColumn.setCellValueFactory(new PropertyValueFactory<FictionBook, LocalDate>("dateOfPublication"));
        amountInStockColumn.setCellValueFactory(new PropertyValueFactory<FictionBook, Integer>("amountInStock"));
        bookShelf.setItems(getBooks());
        
        //totalSaleLabel.setText(getTotalSale());
        //bookInStocLabel.setText(getBookInStock());
        
   }    

    private ObservableList<FictionBook> getBooks() {
         ObservableList<FictionBook> books = FXCollections.observableArrayList();
        
        //add employees to the list
        books.add(new FictionBook("Harry Potter and the Philosopher's Stone", "J.K. Rowling", FictionBook.FictionGenre.ADVENTURE, "Harry Potter", 10.00, LocalDate.of(1997, Month.JUNE, 26), 10, 1));
        
        //return the list
        return books;
    }
    
    public void addNewBookButtonPushed(ActionEvent event) throws IOException
    {
        SceneChanger sc = new SceneChanger();
        sc.changeScenes(event, "AddNewBookView.fxml", "Add new book");
    }
    public void editBookButtonPushed(ActionEvent event) throws IOException
    {
        SceneChanger sc = new SceneChanger();
        sc.changeScenes(event, "EditBookView.fxml", "Edit book");
    }
    public void sellBookButtonPushed(ActionEvent event) {
       
        
        getBooks().remove(bookShelf.getSelectionModel().getSelectedItem());
    }

//    private String getTotalSale() {
//    }
//
//    private String getBookInStock() {
//    }
}
