package DAO;

import Util.ConnectionUtil;
import Model.Account;
import Model.Message;
import java.util.List;
import java.util.ArrayList;
import java.sql.*;

public class AccountDAO
{
    
    //#1 Process new user registrations
    public Account addAccount(Account account)
    {
        Connection connection = Util.ConnectionUtil.getConnection();

        // if (doesUsernameExist(account.getUsername()))
        //     return null;
        try
        {
            String sql = "INSERT INTO (account_id, username, password) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setInt(1, account.getAccount_id());
            preparedStatement.setString(2, account.getUsername());
            preparedStatement.setString(3, account.getPassword());

            preparedStatement.executeUpdate();
            
            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();

            if(pkeyResultSet.next())
            {
                int account_id = (int) pkeyResultSet.getLong(1);
                return new Account(account_id, account.getUsername(), account.getPassword());
            }

        } catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }

        return null;
    }

    public String getAccountUsernameById(int account_id)
    {
        Connection connection = Util.ConnectionUtil.getConnection();
        String accountName = null;
        try {

            String sql = "SELECT * FROM account WHERE account.account_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, account_id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next())
            {
                accountName = new String(resultSet.getString("username"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return accountName;
    }

    public Account loginAccount(String username, String password)
    {
        Connection connection = Util.ConnectionUtil.getConnection();
        Account temp = null;
        try {
            String sql = "SELECT * FROM account WHERE account.username = ?, account.password = ?";
            
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next())
            {
               if (resultSet.getString("username").equals(username) && resultSet.getString("password").equals(password))
                {
                    temp = new Account(username, password);
                    temp.setAccount_id(resultSet.getInt("account_id"));
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return temp;
    }

    public boolean doesUsernameExist(String username)
    {
        Connection connection = Util.ConnectionUtil.getConnection();
        String temp = null;
        try {
            String sql = "SELECT * FROM account WHERE account.username = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next())
            {
                temp = resultSet.getString("username");
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        if (temp.equals(username))
            return true;
        else
            return false;
    }
    

}