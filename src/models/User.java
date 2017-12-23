package models;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class User {
    private int userId;
    private String username, password, phoneNum;
    private byte[] salt;
    private boolean admin;

    public User(String username, String phoneNum, String password, boolean admin) throws NoSuchAlgorithmException {
        setUsername(username);
        setPhoneNum(phoneNum);
        salt = PasswordGenerator.getSalt();
        this.password = PasswordGenerator.getSHA512Password(password, salt);
        this.admin = admin;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        if (userId >= 0)
            this.userId = userId;
        else
            throw new IllegalArgumentException("UserId must be >= 0");
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        if (phoneNum.matches("[2-9]\\d{2}[-.]?\\d{3}[-.]\\d{4}"))
            this.phoneNum = phoneNum;
        else
            throw new IllegalArgumentException("Phone numbers must be in the pattern NXX-XXX-XXXX");
    }

    public byte[] getSalt() {
        return salt;
    }

    public void setSalt(byte[] salt) {
        this.salt = salt;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
    
    public void insertIntoDB() throws SQLException
    {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        
        try
        {
            //1. Connect to the database
            conn = DriverManager.getConnection("jdbc:mysql://localhost:8889/fictionBookShelf?useSSL=false", "root", "root");
            
            //2. Create a String that holds the query with ? as user inputs
            String sql = "INSERT INTO users (username, phoneNum, password, salt, isAdmin) VALUES " +
                         " (?, ?, ?, ?, ?)";
                    
            //3. prepare the query
            preparedStatement = conn.prepareStatement(sql);
                   
            //4. Bind the values to the parameters
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, phoneNum);
            preparedStatement.setString(3, password);
            preparedStatement.setBlob(4, new javax.sql.rowset.serial.SerialBlob(salt));
            preparedStatement.setBoolean(5, admin);
            
            preparedStatement.executeUpdate();
            
        }
        catch (Exception e)
        {
            System.err.println(e.getMessage());
        }
        finally
        {
            if (preparedStatement != null)
                preparedStatement.close();
            
            if (conn != null)
                conn.close();
        }
    }
    
    
    /**
     * This will update the Volunteer in the database
     */
    public void updateUserInDB() throws SQLException
    {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        
        try{
            //1.  connect to the DB
            conn = DriverManager.getConnection("jdbc:mysql://localhost:8889/fictionBookShelf?useSSL=false", "root", "root");
            
            //2.  create a String that holds our SQL update command with ? for user inputs
            String sql = "UPDATE users SET username = ?, phoneNum = ?, "
                    + "password=?, isAdmin = ?"
                    + "WHERE userId = ?";
            
            //3. prepare the query against SQL injection
            preparedStatement = conn.prepareStatement(sql);
            
            //4. bind the parameters
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, phoneNum);
            preparedStatement.setString(3, password);
            preparedStatement.setBlob(4, new javax.sql.rowset.serial.SerialBlob(salt));
            preparedStatement.setBoolean(5, admin);
            preparedStatement.setInt(7, userId);
            
            //6. run the command on the SQL server
            preparedStatement.executeUpdate();
            preparedStatement.close();
        }
        catch (SQLException e)
        {
            System.err.println(e.getMessage());
        }
        finally
        {
            if (conn != null)
                conn.close();
            if (preparedStatement != null)
                preparedStatement.close();
        }
    }
        
        public void changePassword(String newPassword) throws NoSuchAlgorithmException, SQLException
    {
        salt = PasswordGenerator.getSalt();
        password = PasswordGenerator.getSHA512Password(newPassword, salt);
        
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        
        try{
            //1.  connect to the DB
            conn = DriverManager.getConnection("jdbc:mysql://localhost:8889/fictionBookShelf?useSSL=false", "root", "root");
            
            //2.  create a String that holds our SQL update command with ? for user inputs
            String sql = "UPDATE users SET password = ?, salt = ?"
                         + "WHERE userId = ?";
            
            //3. prepare the query against SQL injection
            preparedStatement = conn.prepareStatement(sql);
               
            //4. bind the parameters
            preparedStatement.setString(1, password);
            preparedStatement.setBlob(2, new javax.sql.rowset.serial.SerialBlob(salt));
            preparedStatement.setInt(3, userId);
            
            //6. run the command on the SQL server
            preparedStatement.executeUpdate();
            preparedStatement.close();
        }
        catch (SQLException e)
        {
            System.err.println(e.getMessage());
        }
        finally
        {
            if (conn != null)
                conn.close();
            if (preparedStatement != null)
                preparedStatement.close();
        }
        
    }

    @Override
    public String toString() {
        return "User{" + "userId=" + userId + ", username=" + username + ", password=" + password + ", phoneNum=" + phoneNum + ", salt=" + salt + ", admin=" + admin + '}';
    }
     
    
    
}
