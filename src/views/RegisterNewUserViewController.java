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
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import models.FictionBook;
import models.User;

/**
 * FXML Controller class
 *
 * @author dasha
 */
public class RegisterNewUserViewController implements Initializable, ControllerClass {
    
    @FXML private CheckBox isAdminCheckBox;
    @FXML private TextField usernameTextField;
    @FXML private TextField phoneNumTextField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField xonfirmPasswordField;
    @FXML private ImageView bookImage;
    @FXML private Label errorMsg;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    public void saveUserButtonPushed(ActionEvent event) {
        
    }
    
    public void backButtonPushed(ActionEvent event) throws IOException {
        SceneChanger sc = new SceneChanger();
        sc.changeScenes(event, "FictionBookshelfView.fxml", "Bookshelf");
    }

    @Override
    public void preloadData(FictionBook book) {
    }

    @Override
    public void preloadData(User user) {
    }
}
