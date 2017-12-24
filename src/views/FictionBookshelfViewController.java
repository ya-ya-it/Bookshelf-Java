package views;

import java.io.IOException;
import java.math.BigDecimal;
import models.FictionBook;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import models.User;

/**
 * FXML Controller class for FictionBookShelf view
 *
 * @author Dasha
 */
public class FictionBookshelfViewController implements Initializable, ControllerClass {

    @FXML private TableView<FictionBook> bookShelf;
    @FXML private TableColumn<FictionBook, String> titleColumn;
    @FXML private TableColumn<FictionBook, String> authorColumn;
    @FXML private TableColumn<FictionBook, FictionBook.FictionGenre> genreColumn;
    @FXML private TableColumn<FictionBook, BigDecimal> priceColumn;
    @FXML private TableColumn<FictionBook, LocalDate> publicationDateColumn;
    @FXML private TableColumn<FictionBook, Integer> amountInStockColumn;
    
    @FXML private Button sellBookButton;
    @FXML private Button allUsersButton;
    @FXML private Button addNewBookButton;
    @FXML private Button salesReportButton;

    @FXML private Label totalSaleLabel;
    @FXML private Label bookInStocLabel;
    @FXML private Label bookSoldLabel;
    @FXML private Label totalInventoryPriceLavel;
    @FXML private Label usernameLabel;

    ObservableList<FictionBook> books;

    BigDecimal totalSales = new BigDecimal("0");
    int booksSold = 0;
    int booksInStock = 0;
    BigDecimal totalInventoryPrice = new BigDecimal("0");

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // set up columns in the table
        titleColumn.setCellValueFactory(new PropertyValueFactory<FictionBook, String>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<FictionBook, String>("authorName"));
        genreColumn.setCellValueFactory(new PropertyValueFactory<FictionBook, FictionBook.FictionGenre>("fictionGenre"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<FictionBook, BigDecimal>("price"));
        publicationDateColumn.setCellValueFactory(new PropertyValueFactory<FictionBook, LocalDate>("dateOfPublication"));
        amountInStockColumn.setCellValueFactory(new PropertyValueFactory<FictionBook, Integer>("amountInStock"));

        try {
            loadBooks();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        // set up labels with business information
        ObservableList<FictionBook> books = bookShelf.getItems();
        for (FictionBook book : books) {
            booksSold += book.getAmountSold();
            totalInventoryPrice = totalInventoryPrice.add((book.getPrice().multiply(new BigDecimal(book.getAmountInStock()))));
            booksInStock += book.getAmountInStock();
            totalSales = totalSales.add(book.getPrice().multiply(new BigDecimal(book.getAmountSold())));
        }

        totalInventoryPriceLavel.setText(currencyFormat(totalInventoryPrice));
        bookSoldLabel.setText(Integer.toString(booksSold));
        bookInStocLabel.setText(Integer.toString(booksInStock));
        totalSaleLabel.setText(currencyFormat(totalSales));

        usernameLabel.setText(SceneChanger.getLoggedInUser().getUsername());
        sellBookButton.setDisable(true);

        // disables admin console
        if (!SceneChanger.getLoggedInUser().isAdmin()) {
            allUsersButton.setVisible(false);
            addNewBookButton.setVisible(false);
            salesReportButton.setVisible(false);
        }

    }

    /**
     * This method calls new window to add new book when the Add button is pushed.
     *
     * @param event
     * @throws IOException
     */
    public void addNewBookButtonPushed(ActionEvent event) throws IOException {

        SceneChanger sc = new SceneChanger();
        sc.changeScenes(event, "AddNewBookView.fxml", "Add new book");

    }

    /**
     * This method calls new window to show sales report
     * @param event
     * @throws IOException 
     */
    public void salesReportButtonPushed(ActionEvent event) throws IOException {
        SceneChanger sc = new SceneChanger();
        sc.changeScenes(event, "SalesReviewView.fxml", "Sales report");
    }

    /**
     * This this method calls window with all users table when All users button
     * pushed
     * @param event
     * @throws IOException 
     */
    public void allUsersButtonPushed(ActionEvent event) throws IOException {
        SceneChanger sc = new SceneChanger();
        sc.changeScenes(event, "AllUsersView.fxml", "Users");
    }

    /**
     * This method removes data from the table when the Sell button is pushed
     * and update labels with business information
     *
     * @param event
     */
    public void sellBookButtonPushed(ActionEvent event) throws SQLException, IOException {
        SceneChanger sc = new SceneChanger();
        FictionBook book = this.bookShelf.getSelectionModel().getSelectedItem();
        SaleBooksViewController controller = new SaleBooksViewController();

        sc.changeScenes(event, "SaleBooksView.fxml", "Sale Book", book, controller);
    }

    /**
     * This method brings user to edit window when Edit user button pushed
     * @param event
     * @throws IOException 
     */
    public void editUserButtonPushed(ActionEvent event) throws IOException {
        SceneChanger sc = new SceneChanger();
        User user = SceneChanger.getLoggedInUser();
        RegisterNewUserViewController controller = new RegisterNewUserViewController();
        sc.changeScenes(event, "RegisterNewUserView.fxml", "Edit User", user, controller);
    }

    /**
     * This method deletes information about the users session and bring user to
     * Login window
     * @param event
     * @throws IOException 
     */
    public void logOutButtonPushed(ActionEvent event) throws IOException {
        SceneChanger.setLoggedInUser(null);
        SceneChanger sc = new SceneChanger();
        sc.changeScenes(event, "LoginView.fxml", "Login");
    }

    /**
     * This method format business information on the bottom panel to currency format
     * @param n
     * @return 
     */
    public static String currencyFormat(BigDecimal n) {
        return NumberFormat.getCurrencyInstance().format(n);
    }

    /**
     * This method load new books to the TableView and update labels with
     * business information
     *
     * @param newList
     */
    public void loadBooks() throws SQLException {
        ObservableList<FictionBook> books = FXCollections.observableArrayList();

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

                books.add(book);
            }

            bookShelf.getItems().addAll(books);

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
    }

    @Override
    public void preloadData(FictionBook book) {
    }

    @Override
    public void preloadData(User user) {
    }

    /**
     * This method set sell button disable to false when some book selected from
     * the table
     */
    public void bookSelected() {
        sellBookButton.setDisable(false);
    }
}
