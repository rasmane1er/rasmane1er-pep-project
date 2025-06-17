package DAO;
import  java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Model.Account;

import Util.ConnectionUtil;

public class AccountDAO {
    
   
  
    public Account accountRegistration(Account account){
        Connection connection = ConnectionUtil.getConnection();
        Account newAccount=new Account();
        try {

             String sql1="SELECT * FROM Account WHERE username=?";
            PreparedStatement preparedStatement1 = connection.prepareStatement(sql1);
            preparedStatement1.setString(1, account.getUsername());
            ResultSet rs=preparedStatement1.executeQuery();
            while(rs.next()){   
                newAccount= new Account(rs.getInt("account_id"),rs.getString("username"),rs.getString("password"));
            }
            if(newAccount!=null&&account.getUsername()!=null&&account.getUsername().trim()!=""&& account.getPassword().length()>=4){
            //Write SQL logic here
            String sql = "INSERT INTO Account (username,password) VALUES (?,?);" ;
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            //write preparedStatement's setString
            preparedStatement.setString(1,account.getUsername());
            preparedStatement.setString(2,account.getPassword());
            

            preparedStatement.executeUpdate();

             ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
            if(pkeyResultSet.next()){

                int generated_account_id = (int) pkeyResultSet.getLong(1);
                newAccount=new Account(generated_account_id, account.getUsername(),account.getPassword());

                return newAccount;
            }
        }
            
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
   

    public Account login(Account account){
        Connection connection = ConnectionUtil.getConnection();
        try {
            //Write SQL logic here
            String sql = "SELECT * FROM Account WHERE username=? AND password=?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
                 preparedStatement.setString(1,account.getUsername());
                 preparedStatement.setString(2,account.getPassword());
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Account exsisted_account = new Account(rs.getInt("account_id"),
                        rs.getString("username"),
                        rs.getString("password"));
                return exsisted_account;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
}

