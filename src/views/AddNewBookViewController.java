package views;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
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

    private FileChooser fileChooser;
    private File imageFile;
    private FictionBook book;
    private boolean imageChanged;

    @FXML
    private TextField titleField;
    @FXML
    private TextField authorNameField;
    @FXML
    private ComboBox fictionGenreComboBox;
    @FXML
    private TextField mainCharacterField;
    @FXML
    private DatePicker dateOfPublicationDatePicker;
    @FXML
    private TextField priceField;
    @FXML
    private Spinner amountInStockSpinner;
    @FXML
    private Spinner amountSoldSpinner;
    @FXML
    private ImageView bookImage;
    @FXML
    private Label errorMsg;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        errorMsg.setVisible(false);

        //set up image
        try {
            BufferedImage bufferedImage = ImageIO.read(new File("./src/images/placeholder-cover.png"));
            Image image = SwingFXUtils.toFXImage(bufferedImage, null);
            bookImage.setImage(image);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        //set up combobox
        fictionGenreComboBox.getItems().addAll(FictionBook.FictionGenre.values());
        fictionGenreComboBox.setVisibleRowCount(5);
        fictionGenreComboBox.setEditable(false);
        fictionGenreComboBox.setPromptText("Choose Genre");

        //set up spinners
        SpinnerValueFactory<Integer> amountInStockValueFactory
                = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10000, 1);
        amountInStockSpinner.setValueFactory(amountInStockValueFactory);

        SpinnerValueFactory<Integer> amountSoldValueFactory
                = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10000, 0);
        amountSoldSpinner.setValueFactory(amountSoldValueFactory);

        imageChanged = false;
    }

    /**
     * This method checks fields and throws errors if data are incorrect. If
     * data are correct, it saves to the ObservableList and sends to the Main
     * Table.
     *
     * @param event
     * @throws IOException
     */
    public void saveBookButtonPushed(ActionEvent event) throws IOException, SQLException {
        try {
            String title = titleField.getText();
            String author = authorNameField.getText();
            FictionBook.FictionGenre genre;
            LocalDate dateOfPublication;
            String mainCharacters = mainCharacterField.getText();
            BigDecimal price;
            if (priceField.getText().isEmpty()) {
                throw new IllegalArgumentException("Please enter the price");
            } else {
                price = new BigDecimal(priceField.getText());
            }
            int amountInStock;
            int amountSold;

            if (fictionGenreComboBox.getSelectionModel().isEmpty()) {
                System.out.print("Genre");
                throw new IllegalArgumentException("Please choose genre");
            } else {
                genre = (FictionBook.FictionGenre) fictionGenreComboBox.getSelectionModel().getSelectedItem();
            }

            if (dateOfPublicationDatePicker.getValue() == null) {
                throw new IllegalArgumentException("Please choose a date of publication");
            } else {
                dateOfPublication = dateOfPublicationDatePicker.getValue();
            }
            amountInStock = (int) amountInStockSpinner.getValue();
            amountSold = (int) amountSoldSpinner.getValue();

            if (imageChanged) {
                book = new FictionBook(title, author, genre, mainCharacters,
                        price, dateOfPublication, amountInStock,
                        amountSold, imageFile);
            } else {
                book = new FictionBook(title, author, genre, mainCharacters,
                        price, dateOfPublication, amountInStock,
                        amountSold);
            }

            book.insertIntoDB();
            book.copyImageFile();

            SceneChanger sc = new SceneChanger();
            sc.changeScenes(event, "FictionBookshelfView.fxml", "Bookshelf");
        } catch (IllegalArgumentException e) {
            errorMsg.setVisible(true);
            errorMsg.setText(e.getMessage());
        }
    }

    /**
     * This method returns scene to the Main one without saving data
     *
     * @param event
     * @throws IOException
     */
    public void backButtonPushed(ActionEvent event) throws IOException {
        SceneChanger sc = new SceneChanger();
        sc.changeScenes(event, "FictionBookshelfView.fxml", "Bookshelf");
    }

    /**
     * This method create a new window with a filechooser to choose the cover of
     * the new book.
     *
     * @param event
     */
    public void chooseImageButtonPushed(ActionEvent event) {

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image");

        FileChooser.ExtensionFilter jpgFilter
                = new FileChooser.ExtensionFilter("Image File (*.jpg)", "*.jpg");
        FileChooser.ExtensionFilter pngFilter
                = new FileChooser.ExtensionFilter("Image File (*.png)", "*.png");

        fileChooser.getExtensionFilters().addAll(jpgFilter, pngFilter);

        String userDirectoryString = System.getProperty("user.home");
        File userDirectory = new File(userDirectoryString);

        if (!userDirectory.canRead()) {
            userDirectory = new File("c:/");
        }

        fileChooser.setInitialDirectory(userDirectory);

        File tmpImageFile = fileChooser.showOpenDialog(stage);

        if (tmpImageFile != null) {

            imageFile = tmpImageFile;

            if (imageFile.isFile()) {
                try {
                    BufferedImage bufferedImage = ImageIO.read(imageFile);
                    Image image = SwingFXUtils.toFXImage(bufferedImage, null);
                    bookImage.setImage(image);
                    imageChanged = true;
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                }
            }
        }

    }
}
