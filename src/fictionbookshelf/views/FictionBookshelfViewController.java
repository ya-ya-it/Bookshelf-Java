/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fictionbookshelf.views;

import fictionbookshelf.models.Book;
import fictionbookshelf.models.FictionBook;
import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
    @FXML private TableColumn<FictionBook, String> genreColumn;
    @FXML private TableColumn<FictionBook, String> priceColumn;
    @FXML private TableColumn<FictionBook, String> publicationDateColumn;
    @FXML private TableColumn<FictionBook, String> amountSoldColumn;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        titleColumn.setCellValueFactory(new PropertyValueFactory<FictionBook, String>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<FictionBook, String>("authorName"));
        genreColumn.setCellValueFactory(new PropertyValueFactory<FictionBook, String>("genre"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<FictionBook, String>("price"));
        publicationDateColumn.setCellValueFactory(new PropertyValueFactory<FictionBook, String>("dateOfPublication"));
        amountSoldColumn.setCellValueFactory(new PropertyValueFactory<FictionBook, String>("amountSold"));
        bookShelf.setItems(getBooks());
    }    

    private ObservableList<FictionBook> getBooks() {
         ObservableList<FictionBook> books = FXCollections.observableArrayList();
        
        //add employees to the list
        books.add(new FictionBook("Harry Potter and the Philosopher's Stone", "J.K. Rowling", FictionBook.FictionGenre.ADVENTURE, "Harry Potter", 10.00, LocalDate.of(1997, Month.JUNE, 26)));
        
        //return the list
        return books;
    }
    
}
