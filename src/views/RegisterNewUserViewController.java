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
 * @author Dasha
 */
public class RegisterNewUserViewController implements Initializable, ControllerClass {

    @FXML private CheckBox isAdminCheckBox;

    @FXML private TextField usernameTextField;
    @FXML private TextField phoneNumTextField;

    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;

    @FXML private Label errorMsg;
    @FXML private Label adminLabel;

    private User user;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (user == null || !SceneChanger.getLoggedInUser().isAdmin()) {
            isAdminCheckBox.setVisible(false);
            adminLabel.setVisible(false);

        }
        errorMsg.setText("");
    }

    public void saveUserButtonPushed(ActionEvent event) throws NoSuchAlgorithmException, SQLException, IOException {
        
        if (validPassword() || user != null) {
            try {
                if (user != null) {
                    //update the user information
                    updateUser();
                    user.updateUserInDB();

                    //update the password if it changed
                    if (!passwordField.getText().isEmpty()) {
                        if (validPassword()) {
                            user.changePassword(passwordField.getText());
                        }
                    }
                } else //create new user
                {
                    user = new User(usernameTextField.getText(), phoneNumTextField.getText(),
                            passwordField.getText(),
                            isAdminCheckBox.isSelected());
                }
                errorMsg.setText("");
                user.insertIntoDB();

                SaleBooksViewController controllerClass = new SaleBooksViewController();
                SceneChanger sc = new SceneChanger();
                SceneChanger.setLoggedInUser(user);
                
                if (user != null && !SceneChanger.getLoggedInUser().isAdmin()) {
                    sc.changeScenes(event, "FictionBookshelfView.fxml", "Bookshelf", user, controllerClass);
                } else {
                    sc.changeScenes(event, "AllUsersView.fxml", "Users", user, controllerClass);
                }
            } catch (Exception e) {
                errorMsg.setText(e.getMessage());
                System.out.println(errorMsg);
            }

        }
    }

    /**
     * This method will validate that the password match confirm password
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
     * This method will read from the GUI fields and update the user object
     */
    public void updateUser() throws IOException {
        user.setUsername(usernameTextField.getText());
        user.setPhoneNum(phoneNumTextField.getText());
        user.setPassword(passwordField.getText());
        user.setAdmin(isAdminCheckBox.isSelected());

    }

    /**
     * This method will bring user to the bookshelf if user is logged in, or to the
     * login window if user is not logged, or to the lists of users if the user is admin
     * @param event
     * @throws IOException 
     */
    public void backButtonPushed(ActionEvent event) throws IOException {
        SceneChanger sc = new SceneChanger();
        if (user != null && !SceneChanger.getLoggedInUser().isAdmin()) {
            sc.changeScenes(event, "FictionBookshelfView.fxml", "Bookshelf");
        } else if (user != null && SceneChanger.getLoggedInUser().isAdmin()) {
            sc.changeScenes(event, "AllUsersView.fxml", "Users");
        } else {
            sc.changeScenes(event, "LoginView.fxml", "Log in");
        }
    }

    @Override
    public void preloadData(User user) {
        this.user = user;
        this.usernameTextField.setText(user.getUsername());
        this.phoneNumTextField.setText(user.getPhoneNum());

        if (user.isAdmin()) {
            isAdminCheckBox.setSelected(true);
            adminLabel.setVisible(true);
        }
    }

    @Override
    public void preloadData(FictionBook book) {
    }
}
