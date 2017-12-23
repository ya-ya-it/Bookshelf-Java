/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
 * @author dasha
 */
public class LoginViewController implements Initializable {

    
    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField userIdTextField;
    
    @FXML
    private Label errorMsg;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        errorMsg.setText("");
    }    
    public void loginButtonPushed(ActionEvent event) throws IOException, NoSuchAlgorithmException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        
        int userId = Integer.parseInt(userIdTextField.getText());
        
        try{
            //1.  connect to the DB
            conn = DriverManager.getConnection("jdbc:mysql://localhost:8889/fictionBookShelf?useSSL=false", "root", "root");
            
            //2.  create a query string with ? used instead of the values given by the user
            String sql = "SELECT * FROM users WHERE userId = ?";
            
            //3.  prepare the statement
            ps = conn.prepareStatement(sql);
            
            //4.  bind the userId to the ?
            ps.setInt(1, userId);
            
            //5. execute the query
            resultSet = ps.executeQuery();
            
            //6.  extract the password and salt from the resultSet
            String dbPassword=null;
            byte[] salt = null;
            boolean admin = false;
            User user = null;
            
            while (resultSet.next())
            {
                dbPassword = resultSet.getString("password");
                
                Blob blob = resultSet.getBlob("salt");
                
                //convert into a byte array
                int blobLength = (int) blob.length();
                salt = blob.getBytes(1, blobLength);
                
                admin = resultSet.getBoolean("admin");
                
                user = new User(resultSet.getString("username"),
                                                   resultSet.getString("phoneNum"),
                                                   resultSet.getString("password"),
                                                   resultSet.getBoolean("isAdmin"));
                user.setUserId(resultSet.getInt("userId"));
            }
            
            //convert the password given by the user into an encryted password
            //using the salt from the database
            String userPW = PasswordGenerator.getSHA512Password(passwordField.getText(), salt);
            
            SceneChanger sc = new SceneChanger();
            
            if (userPW.equals(dbPassword))
                SceneChanger.setLoggedInUser(user);
            
            //if the passwords match - change to the VolunteerTableView
            if (userPW.equals(dbPassword) && admin)
                sc.changeScenes(event, "FictionBookshelfView.fxml", "Bookshelf");
            else if (userPW.equals(dbPassword))
            {
                //create an instance of the controller class for log hours view
                SaleBooksViewController controllerClass = new SaleBooksViewController();
                sc.changeScenes(event, "SaleBooksView.fxml", "Sale Book", user, controllerClass);
            }
            else
                //if the do not match, update the error message
                errorMsg.setText("The userId and password do not match");
        }
        catch (SQLException e)
        {
            System.err.println(e.getMessage());
        }
    }
}
