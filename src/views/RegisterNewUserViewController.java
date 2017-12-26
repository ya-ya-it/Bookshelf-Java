package views;

import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import models.FictionBook;
import models.PasswordGenerator;
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
    private int userId;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (user != null && SceneChanger.getLoggedInUser().isAdmin()) {
            isAdminCheckBox.setVisible(true);
            adminLabel.setVisible(true);
            System.out.println("here!!!");
        } else {
            isAdminCheckBox.setVisible(false);
            adminLabel.setVisible(false);
        }

        errorMsg.setText("");
        
    }

    /**
     * This method saves to or updates user into db.
     *
     * @param event
     * @throws SQLException
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    public void saveUserButtonPushed(ActionEvent event) throws SQLException, IOException, NoSuchAlgorithmException {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        if (phoneNumTextField.getText().matches("[2-9]\\d{2}[-.]?\\d{3}[-.]\\d{4}")) {
            try {
                //1. Connect to the database
                conn = DriverManager.getConnection("jdbc:mysql://localhost:8889/fictionBookshelf?useSSL=false", "root", "root");

                byte[] salt = PasswordGenerator.getSalt();

                //2. Create a String that holds the query with ? as user inputs
                if (user == null) {
                    if (validatePassword()) {
                        preparedStatement = conn.prepareStatement("INSERT INTO users (username, phoneNum, password, salt, isAdmin) VALUES (?,?,?,?,?)");
                        preparedStatement.setString(1, usernameTextField.getText());
                        preparedStatement.setString(2, phoneNumTextField.getText());
                        preparedStatement.setString(3, PasswordGenerator.getSHA512Password(passwordField.getText(), salt));
                        preparedStatement.setBlob(4, new javax.sql.rowset.serial.SerialBlob(salt));
                        preparedStatement.setBoolean(5, isAdminCheckBox.isSelected());
                    }
                } else if (user != null && !passwordField.getText().isEmpty() && !confirmPasswordField.getText().isEmpty()) {
                    if (validatePassword()) {
                        preparedStatement = conn.prepareStatement("UPDATE users SET username = ?, phoneNum = ?, password = ?, salt = ?, isAdmin = ? WHERE userId = " + user.getUserId());
                        preparedStatement.setString(1, usernameTextField.getText());
                        preparedStatement.setString(2, phoneNumTextField.getText());
                        preparedStatement.setString(3, PasswordGenerator.getSHA512Password(passwordField.getText(), salt));
                        preparedStatement.setBlob(4, new javax.sql.rowset.serial.SerialBlob(salt));
                        preparedStatement.setBoolean(5, isAdminCheckBox.isSelected());

                        //update user if he/she is not admin
                        if (!SceneChanger.getLoggedInUser().isAdmin()) {
                            try {
                                //set user info
                                user.setUsername(usernameTextField.getText());
                                user.setPhoneNum(phoneNumTextField.getText());
                                user.setPassword(PasswordGenerator.getSHA512Password(passwordField.getText(), salt));
                                user.setSalt(salt);
                                user.setAdmin(isAdminCheckBox.isSelected());
                            } catch (IllegalArgumentException ex) {
                                errorMsg.setText(ex.toString());
                            }
                        }
                    }
                } else if (user != null) {
                    preparedStatement = conn.prepareStatement("UPDATE users SET username = ?, phoneNum = ?, isAdmin = ? WHERE userId = " + user.getUserId());
                    preparedStatement.setString(1, usernameTextField.getText());
                    preparedStatement.setString(2, phoneNumTextField.getText());
                    preparedStatement.setBoolean(3, isAdminCheckBox.isSelected());

                    if (!SceneChanger.getLoggedInUser().isAdmin()) {
                        try {
                            //set user info
                            user.setUsername(usernameTextField.getText());
                            user.setPhoneNum(phoneNumTextField.getText());
                            user.setAdmin(isAdminCheckBox.isSelected());
                        } catch (IllegalArgumentException e) {
                            errorMsg.setText(e.toString());
                        }
                    }
                }

                preparedStatement.executeUpdate();

                SceneChanger sc = new SceneChanger();

                if (user == null) {
                    errorMsg.setText("Thanks for registration! Now please go back "
                            + "and log in using your userId and password. Your userId is "
                            + getUserId());
                } else if (user != null && !SceneChanger.getLoggedInUser().isAdmin()) {
                    sc.changeScenes(event, "FictionBookshelfView.fxml", "Bookshelf");
                } else if (user != null && SceneChanger.getLoggedInUser().isAdmin()) {
                    sc.changeScenes(event, "AllUsersView.fxml", "Users");
                }

            } catch (SQLException e) {
                System.err.println(e);
            } finally {
                if (conn != null) {
                    conn.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            }
        } else {
            errorMsg.setText("Phone numbers must be in the pattern NXX-XXX-XXXX");
        }
    }

    /**
     * This method will bring user to the bookshelf if user is logged in, or to
     * the login window if user is not logged, or to the lists of users if the
     * user is admin
     *
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

    /**
     * This method validates password field and returns true only if password is
     * greater than 5, and equal to confirm password field
     *
     * @return
     */
    public boolean validatePassword() {
        if (passwordField.getText().isEmpty()) {
            errorMsg.setText("Please enter the password");
            return false;
        } else if (passwordField.getText().length() < 5) {
            errorMsg.setText("Password is too short");
            return false;
        } else if (!passwordField.getText().equals(confirmPasswordField.getText())) {
            errorMsg.setText("Passwords don't match");
            return false;
        } else {
            return true;
        }
    }

    /**
     * This method returns the id of the last registered user
     *
     * @return
     * @throws SQLException
     */
    public int getUserId() throws SQLException {
        Connection conn = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            //1. connect to the database
            conn = DriverManager.getConnection("jdbc:mysql://localhost:8889/fictionBookShelf?useSSL=false", "root", "root");

            //2.  create a statement object
            statement = conn.createStatement();

            //3.  create the SQL query
            resultSet = statement.executeQuery("SELECT * FROM users ORDER BY userId DESC LIMIT 1;");

            //4.  create book objects from each record
            while (resultSet.next()) {
                userId = resultSet.getInt("userId");
            }
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
        return userId;
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
