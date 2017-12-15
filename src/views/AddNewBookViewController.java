package views;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import models.FictionBook;

/**
 * FXML Controller class for AddNewBook view
 *
 * @author Dasha
 */
public class AddNewBookViewController implements Initializable {

    private ObservableList<FictionBook> books;
    private FileChooser fileChooser;
    private File imageFile;
    
    @FXML private TextField titleField;
    @FXML private TextField authorNameField;
    @FXML private ComboBox fictionGenreComboBox;
    @FXML private TextField mainCharacterField;
    @FXML private DatePicker dateOfPublicationDatePicker;
    @FXML private TextField priceField;
    @FXML private TextField amountInStockField;
    @FXML private TextField amountSoldField;
    @FXML private ImageView bookImage;
    @FXML private Label errorMsg;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        errorMsg.setVisible(false);
        
        //set up image
        try
        {
            BufferedImage bufferedImage = ImageIO.read(new File("./src/images/placeholder-cover.png"));
            Image image = SwingFXUtils.toFXImage(bufferedImage, null);
            bookImage.setImage(image);
        }
        catch (IOException e)
        {
            System.err.println(e.getMessage());
        }
        
        //set up combobox
        fictionGenreComboBox.getItems().addAll(FictionBook.FictionGenre.values());
        fictionGenreComboBox.setVisibleRowCount(5);
        fictionGenreComboBox.setEditable(false);
        fictionGenreComboBox.setPromptText("Choose Genre");
        
    }
    
    /**
     * Method to receive data from the Main table
     * @param listOfBooks 
     */
    public void initialData(ObservableList<FictionBook> listOfBooks) {
        books = listOfBooks;
    }
    
    /**
     * This method checks fields and throws errors if data are incorrect. If
     * data are correct, it saves to the ObservableList and sends to the Main Table.
     * @param event
     * @throws IOException 
     */
    public void saveBookButtonPushed(ActionEvent event) throws IOException {
        try
        {
            String title = titleField.getText();
            String author = authorNameField.getText();
            FictionBook.FictionGenre genre;
            LocalDate dateOfPublication;
            String mainCharacters = mainCharacterField.getText();
            BigDecimal price;
            if (!priceField.getText().isEmpty()) {
                price = new BigDecimal(priceField.getText());
            } else {
                price = new BigDecimal("0");
            }
            int amountInStock;
            int amountSold;
            Image bookCover = bookImage.getImage();
            
            if (fictionGenreComboBox.getSelectionModel().isEmpty()) {
                System.out.print("Genre");
                throw new IllegalArgumentException("Please choose genre");
            } else {
                genre = (FictionBook.FictionGenre)fictionGenreComboBox.getSelectionModel().getSelectedItem();
            }
            
            if (dateOfPublicationDatePicker.getValue() == null) {
                throw new IllegalArgumentException("Please choose a date of publication");
            } else {
                dateOfPublication = dateOfPublicationDatePicker.getValue();
            }
            
            if(!amountInStockField.getText().matches("\\d+(\\.\\d+)?")) {
                throw new IllegalArgumentException("Please enter the book amount in stock");
            } else {
                amountInStock = Integer.parseInt(amountInStockField.getText());
            }
            
            if(!amountSoldField.getText().matches("\\d+(\\.\\d+)?")) {
                throw new IllegalArgumentException("Please enter the correct amount of sold books");
            } else {
                amountSold = Integer.parseInt(amountSoldField.getText());
            }
            
            FictionBook newBook = new FictionBook(title, author, genre, mainCharacters,
                                        price, dateOfPublication, amountInStock,
                                        amountSold,bookCover);
            books.add(newBook);
            
            
            SceneChanger sc = new SceneChanger();
            sc.changeScenes(event, "FictionBookshelfView.fxml", "Bookshelf");
        }
        catch (IllegalArgumentException e)
        {
            errorMsg.setVisible(true);
            errorMsg.setText(e.getMessage());
        }
    }
    
    /**
     * This method returns scene to the Main one without saving data
     * @param event
     * @throws IOException 
     */
    public void backButtonPushed(ActionEvent event) throws IOException {
        SceneChanger sc = new SceneChanger();
        sc.changeScenes(event, "FictionBookshelfView.fxml", "Bookshelf");
    }
    
    /**
     * This method create a new window with a filechooser to choose the cover
     * of the new book.
     * @param event 
     */
    public void chooseImageButtonPushed(ActionEvent event) {
        
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        
        fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image");
        
        FileChooser.ExtensionFilter jpgFilter 
                = new FileChooser.ExtensionFilter("Image File (*.jpg)", "*.jpg");
        FileChooser.ExtensionFilter pngFilter 
                = new FileChooser.ExtensionFilter("Image File (*.png)", "*.png");
        
        fileChooser.getExtensionFilters().addAll(jpgFilter, pngFilter);
       
        String userDirectoryString = System.getProperty("user.home");
        File userDirectory = new File(userDirectoryString);
        
        if (!userDirectory.canRead())
            userDirectory = new File("c:/");
        
        fileChooser.setInitialDirectory(userDirectory);
        
        imageFile = fileChooser.showOpenDialog(stage);
        
        if (imageFile.isFile())
        {
            try
            {
                BufferedImage bufferedImage = ImageIO.read(imageFile);
                Image image = SwingFXUtils.toFXImage(bufferedImage,null);
                bookImage.setImage(image);
            }
            catch (IOException e)
            {
                System.err.println(e.getMessage());
            }
        }

    }
}
