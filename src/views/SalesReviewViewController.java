/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author dasha
 */
public class SalesReviewViewController implements Initializable {

    
    @FXML
    private LineChart<?, ?> salesBarChart;

    @FXML
    private CategoryAxis Xaxis;

    @FXML
    private NumberAxis Yaxis;

    @FXML
    private Label totalSalesLabel;

    @FXML
    private Label inventoryPriceLabel;

    @FXML
    private Label booksInStockLabel;

    @FXML
    private Label bookSoldLabel;

    @FXML
    private ComboBox<Integer> yearCombobox;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    public void backButtonPushed(ActionEvent event) throws IOException {
        SceneChanger sc = new SceneChanger();
        sc.changeScenes(event, "FictionBookshelfView.fxml", "Bookshelf");
    }
}
