package Service;

import java.util.List;

import DAO.MessageDAO;
import Model.Message;

public class MessageService {
    
    MessageDAO messageDAO;

    public MessageService()
    {
        this.messageDAO = new MessageDAO();
    }

    public MessageService(MessageDAO messageDAO)
    {
        this.messageDAO = messageDAO;
    }

    public Message createMessage(Message message)
    {
        return this.messageDAO.createMessage(message);
    }

    public List<Message> getAllMessages()
    {
        return this.messageDAO.getAllMessages();
    }

    public Message getMessageById(int message_id)
    {
        return this.messageDAO.getMessageById(message_id);
    }

    public void deleteMessageById(int message_id)
    {
        this.messageDAO.deleteMessageById(message_id);
    }

    public void updateMessageById(int message_id, String message_text)
    {
        this.messageDAO.updateMessageById(message_id, message_text);
    }

    public List<Message> getAllMessagesByUserId(int account_id)
    {
        return this.messageDAO.getAllMessagesByUserId(account_id);
    }
}
