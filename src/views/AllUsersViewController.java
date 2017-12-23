/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import models.User;

/**
 * FXML Controller class
 *
 * @author dasha
 */
public class AllUsersViewController implements Initializable {

    @FXML
    private TableView<User> userdTableView;

    @FXML
    private TableColumn<User, Integer> userIdColumn;

    @FXML
    private TableColumn<User, String> usernameColumn;

    @FXML
    private TableColumn<User, String> phoneNumColumn;


    @FXML
    private Label nameLabel;
    
    @FXML
    private Button editUserButton;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        userIdColumn.setCellValueFactory(new PropertyValueFactory<User, Integer>("userId"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<User, String>("username"));
        phoneNumColumn.setCellValueFactory(new PropertyValueFactory<User, String>("phoneNum"));
        
        editUserButton.setDisable(true);
        nameLabel.setText(SceneChanger.getLoggedInUser().getUsername());
        
        try{
            loadUsers();
        }
        catch (SQLException e)
        {
            System.err.println(e.getMessage());
        }
       
    }    
    public void addUserButtonPushed(ActionEvent event) throws IOException {
        SceneChanger sc = new SceneChanger();
        sc.changeScenes(event, "RegisterNewUserView.fxml", "Register New User");
    }
    public void backButtonPushed(ActionEvent event) throws IOException {
        SceneChanger sc = new SceneChanger();
        sc.changeScenes(event, "FictionBookshelfView.fxml", "Bookshelf");
    }
    public void editUserButtonPushed(ActionEvent event) throws IOException {
        SceneChanger sc = new SceneChanger();
        User user = this.userdTableView.getSelectionModel().getSelectedItem();
        RegisterNewUserViewController controller = new RegisterNewUserViewController();
        
        sc.changeScenes(event, "RegisterNewUserView.fxml", "Edit User", user, controller);
    }
    
    public void loadUsers() throws SQLException
    {
        ObservableList<User> users = FXCollections.observableArrayList();
        
        Connection conn = null;
        Statement statement = null;
        ResultSet resultSet = null;
        
        try{
            //1. connect to the database
            conn = DriverManager.getConnection("jdbc:mysql://localhost:8889/fictionBookShelf?useSSL=false", "root", "root");
            //2.  create a statement object
            statement = conn.createStatement();
            
            //3.  create the SQL query
            resultSet = statement.executeQuery("SELECT * FROM users");
            
            //4.  create volunteer objects from each record
            while (resultSet.next())
            {
                //String username, String phoneNum, String password, 
                //boolean admin
                User user = new User(resultSet.getString("username"),
                                                   resultSet.getString("phoneNum"),
                                                   resultSet.getString("password"),
                                                   resultSet.getBoolean("isAdmin"));
                user.setUserId(resultSet.getInt("userId"));
                
                users.add(user);
            }
            
            userdTableView.getItems().addAll(users);
            
            
        } catch (Exception e)
        {
            System.err.println(e);
        }
        finally
        {
            if (conn != null)
                conn.close();
            if(statement != null)
                statement.close();
            if(resultSet != null)
                resultSet.close();
        }
    }
     public void userSelected()
    {
        editUserButton.setDisable(false);
    }
}
