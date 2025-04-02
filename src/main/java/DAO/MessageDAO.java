package DAO;

import Util.ConnectionUtil;
import Model.Account;
import Model.Message;
import java.util.List;
import java.util.ArrayList;
import java.sql.*;

public class MessageDAO
{
    
    public Message createMessage(Message message)
    {
        Connection connection = Util.ConnectionUtil.getConnection();

        try {
            String sql = "INSERT INTO message (posted_by, message_text) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text());

            preparedStatement.executeUpdate();
            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
            if(pkeyResultSet.next())
            {
                int generatedId = pkeyResultSet.getInt(1);
                return new Message(generatedId, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
            }
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    public List<Message> getAllMessages()
    {
        Connection connection = Util.ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            String sql = "SELECT * FROM message";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next())
            {
                Message message = new Message(resultSet.getInt("message_id"), resultSet.getInt("posted_by"), 
                resultSet.getString("message_text"), resultSet.getLong("time_posted_epoch"));
                messages.add(message);
            }
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return messages;
    }

    public Message getMessageById(int message_id)
    {
        Connection connection = Util.ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, message_id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next())
            {
                return new Message(resultSet.getInt("message_id"), resultSet.getInt("posted_by"), 
                resultSet.getString("message_text"), resultSet.getLong("time_posted_epoch"));
            }
        } catch (SQLException e) {
            // TODO: handle exception
        }

        return null;
    }

    public void deleteMessageById(int message_id)
    {
        Connection connection = Util.ConnectionUtil.getConnection();
        try {
            String sql = "DELETE FROM message WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, message_id);
            preparedStatement.executeQuery();
            
        } catch (SQLException e) {
            e.getMessage();
        }
    }
}
