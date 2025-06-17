package Service;
import Model.Message;

import java.util.List;

import DAO.MessageDAO;

public class MessageService {
     public MessageDAO messageDAO;

    public MessageService(){
        messageDAO = new MessageDAO();
    }
  

    public MessageService(MessageDAO messageDAO){
        this.messageDAO = messageDAO;
    }

    public Message addMessage(Message message){
        return messageDAO.newMessage(message);
    }

    public List<Message> getAllMessages(){
        return messageDAO.getAllMessages();
    }

    public Message getMessageById(int messageId){
        return messageDAO.getMessageById(messageId);
    }

    public Message deleteMessageById(int messageId){
        return messageDAO.deleteMessageById(messageId);
    }

    public Message UpdateMessageById(int id,String message){
        return messageDAO.UpdateMessageById(id, message);
    }

    public List<Message> getAllMessagesFromAccountId(int id){
        return messageDAO.getAllMessagesFromAccountId(id);
    }
}
