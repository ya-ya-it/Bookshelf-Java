/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import models.FictionBook;
import models.User;

/**
 * FXML Controller class
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
    private Label errorMsgLabel;

    @FXML
    private Label nameLabel;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        nameLabel.setText(SceneChanger.getLoggedInUser().getUsername());
    }    

    @Override
    public void preloadData(FictionBook book) {
    }

    @Override
    public void preloadData(User user) {
    }
    
}
