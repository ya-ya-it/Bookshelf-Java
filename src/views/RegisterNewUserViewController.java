/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import models.FictionBook;
import models.User;

/**
 * FXML Controller class
 *
 * @author dasha
 */
public class RegisterNewUserViewController implements Initializable, ControllerClass {

    @FXML
    private CheckBox isAdminCheckBox;

    @FXML
    private TextField usernameTextField;

    @FXML
    private TextField phoneNumTextField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Label errorMsg;

    private User user;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
      //  if (!SceneChanger.getLoggedInUser().isAdmin()) {
            isAdminCheckBox.setVisible(false);
       // }
        errorMsg.setText("");
    }

    public void saveUserButtonPushed(ActionEvent event) throws NoSuchAlgorithmException, SQLException, IOException {
        if (validPassword() || user != null) {
            try {
                if (user != null) //we need to edit/update an existing volunteer
                {
                    //update the user information
                    updateUser();
                    user.updateUserInDB();

                    //update the password if it changed
                    if (!passwordField.getText().isEmpty()) {
                        if (validPassword()) {
                            user.changePassword(passwordField.getText());
                        }
                    }
                } else //we need to create a new volunteer
                {

                    user = new User(usernameTextField.getText(), phoneNumTextField.getText(),
                            passwordField.getText(),
                            isAdminCheckBox.isSelected());
                }
                errorMsg.setText("");    //do not show errors if creating Volunteer was successful
                user.insertIntoDB();

                SceneChanger sc = new SceneChanger();
                sc.changeScenes(event, "AllUsersView.fxml", "All users");
            } catch (Exception e) {
                errorMsg.setText(e.getMessage());
            }

        }
    }
        /**
         * This method will validate that the passwords match
         *
         */
    public boolean validPassword() {
        if (passwordField.getText().length() < 5) {
            errorMsg.setText("Passwords must be greater than 5 characters in length");
            return false;
        }

        if (passwordField.getText().equals(confirmPasswordField.getText())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * This method will read from the GUI fields and update the volunteer object
     */
    public void updateUser() throws IOException {
        user.setUsername(usernameTextField.getText());
        user.setPhoneNum(phoneNumTextField.getText());
        user.setPassword(passwordField.getText());
        user.setAdmin(isAdminCheckBox.isSelected());

    }

    public void backButtonPushed(ActionEvent event) throws IOException {
        SceneChanger sc = new SceneChanger();
        sc.changeScenes(event, "FictionBookshelfView.fxml", "Bookshelf");
    }

    @Override
    public void preloadData(User user) {
        this.user = user;
        this.usernameTextField.setText(user.getUsername());
        this.phoneNumTextField.setText(user.getPhoneNum());

        if (user.isAdmin()) {
            isAdminCheckBox.setSelected(true);
        }
    }

    @Override
    public void preloadData(FictionBook book) {
    }
}
