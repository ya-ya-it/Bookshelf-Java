package views;

import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import models.PasswordGenerator;
import models.User;

/**
 * FXML Controller class
 *
 * @author Dasha
 */
public class LoginViewController implements Initializable {

    @FXML private PasswordField passwordField;
    @FXML private TextField userIdTextField;

    @FXML private Label errorMsg;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        errorMsg.setText("");
    }

    /**
     * This method checks if the user enters correct username and password. If
     * everything is correct, user login,if not - error will be thrown.
     *
     * @param event
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    public void loginButtonPushed(ActionEvent event) throws IOException, NoSuchAlgorithmException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;

        int userId = Integer.parseInt(userIdTextField.getText());

        try {
            //1.  connect to the DB
            conn = DriverManager.getConnection("jdbc:mysql://localhost:8889/fictionBookShelf?useSSL=false", "root", "root");

            //2.  create a query string
            String sql = "SELECT * FROM users WHERE userId = ?";

            //3.  prepare the statement
            ps = conn.prepareStatement(sql);

            //4.  bind the userId to the ?
            ps.setInt(1, userId);

            //5. execute the query
            resultSet = ps.executeQuery();

            //6.  extract the password and salt from the resultSet
            String dbPassword = null;
            byte[] salt = null;
            boolean admin = false;
            User user = null;

            while (resultSet.next()) {
                dbPassword = resultSet.getString("password");

                Blob blob = resultSet.getBlob("salt");

                int blobLength = (int) blob.length();
                salt = blob.getBytes(1, blobLength);

                admin = resultSet.getBoolean("isAdmin");

                user = new User(resultSet.getString("username"),
                        resultSet.getString("phoneNum"),
                        resultSet.getString("password"),
                        resultSet.getBoolean("isAdmin"));
                user.setUserId(resultSet.getInt("userId"));
            }
            
            String userPW = PasswordGenerator.getSHA512Password(passwordField.getText(), salt);

            SceneChanger sc = new SceneChanger();

            if (userPW.equals(dbPassword)) {
                SceneChanger.setLoggedInUser(user);
                SaleBooksViewController controllerClass = new SaleBooksViewController();
                sc.changeScenes(event, "FictionBookshelfView.fxml", "Bookshelf", user, controllerClass);
            } else {
                errorMsg.setText("The userId and password do not match");
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * This methods brings user to the register window.
     *
     * @param event
     * @throws IOException
     */
    public void registerButtonPushed(ActionEvent event) throws IOException {
        SceneChanger sc = new SceneChanger();
        sc.changeScenes(event, "RegisterNewUserView.fxml", "Register new User");
    }
}
