package DAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import Model.Message;
import Util.ConnectionUtil;

public class MessageDAO {

    //Create New Message
     public Message newMessage(Message message){
        Connection connection = ConnectionUtil.getConnection();
        
     try{
            // Helper to check if a user exists based on the accountId
            boolean isUserExists=false;

            String sql = "SELECT COUNT(*) FROM Account WHERE account_id = ?;";
                  PreparedStatement statement = connection.prepareStatement(sql);
                  statement.setInt(1, message.getPosted_by());
                 ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                isUserExists=count>0;
            }
 
            // Check if the message text is not blank and within the character limit and exists
        if (message.getMessage_text() != null && !message.getMessage_text().isEmpty() && message.getMessage_text().length() <= 255&&isUserExists)
             {
                //INSERT Query logic here
                String sql1 = "INSERT INTO Message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?);";
                  PreparedStatement statement1 = connection.prepareStatement(sql1, Statement.RETURN_GENERATED_KEYS);
                  //write preparedStatement's setString and setInt methode 
                   statement1.setInt(1,message.getPosted_by());
                   statement1.setString(2,message.getMessage_text());
                   statement1.setLong(3,message.getTime_posted_epoch());

                         statement1.executeUpdate();
                           
                       //Getting the Auto generated key

                         ResultSet pkeyResultSet = statement1.getGeneratedKeys();
                         if(pkeyResultSet.next()){
                         int generated_message_id = (int) pkeyResultSet.getLong(1);
                         return new Message(generated_message_id,message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
                       }
                    
                } else {
                return null;
                } 
             
              }catch(SQLException e){
            System.out.println(e.getMessage());
             }
             return null;
          }

   // Get All Messages
    public List<Message> getAllMessages(){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            //SELECT Query  logic here
            String sql = "SELECT * FROM Message;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();

            //Looping to get the return Select Query
            while(rs.next()){
                 messages.add(new Message(rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch")));             
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }
    
    //Get One Message Given Message Id
    public Message getMessageById(int message_id){
        Connection connection = ConnectionUtil.getConnection();
        Message message= new Message();
        try {
            // SELECT QUERY returning  Message that match the provided Id here
            String sql = "SELECT * FROM Message WHERE message_id=?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            //write preparedStatement's setInt, setLong and setString  method here.
            preparedStatement.setInt(1, message_id);

        //Looping to get the return Select Query
            ResultSet rs = preparedStatement.executeQuery();
             while(rs.next()){
                message = new Message(rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch"));
                        return message;    
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
       return null;
    }

   // Delete a Message via Given Message Id
     public Message deleteMessageById(int message_id){
        Connection connection = ConnectionUtil.getConnection();
        Message deletedMessage=new Message();
        try {
            //SELECT QUERY logic here
            String sql1="SELECT * FROM Message WHERE message_id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql1);
            preparedStatement.setInt(1, message_id);
            ResultSet rs=preparedStatement.executeQuery();
            while(rs.next()){   
                deletedMessage= new Message(rs.getInt("message_id"),rs.getInt("posted_by"),rs.getString("message_text"),rs.getLong("time_posted_epoch"));
            }
            //DELETE query to remove message with the giving id
            String sql = "DELETE FROM Message WHERE message_id=?;";
            PreparedStatement preparedStatement1 = connection.prepareStatement(sql);

            //write preparedStatement's setInt method here.
            preparedStatement1.setInt(1, message_id);

            int deleted=preparedStatement1.executeUpdate();
             
            if(deleted==1){
                return deletedMessage;
            }
            
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    //Update Message via Given Message Id
    public Message UpdateMessageById(int id,String messageText){
        Connection connection = ConnectionUtil.getConnection();
        Message updatedMessage=new Message();
        try {
            //// Helper to check if a message exists in the database and if message_text is not blank and not more than 255 character  
            if(messageText.length()<=255&&messageText.trim()!=""&&messageText!=null){
              //Write SQL logic here
            String sql = "UPDATE Message SET message_text=? WHERE message_id=?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
             //write preparedStatement's setInt and setString  method here.
            preparedStatement.setString(1, messageText);
            preparedStatement.setInt(2, id);
            int exists=preparedStatement.executeUpdate();

            if (exists==1){
             String sql1="SELECT * FROM Message WHERE message_id=?";
             PreparedStatement statement1 = connection.prepareStatement(sql1);
             statement1.setInt(1, id);
             ResultSet rs=statement1.executeQuery();
            
             while(rs.next()){
                updatedMessage = new Message(rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch"));
               
            }
            return updatedMessage;
           }
            return null;
            }    
            
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

     //Get All Messages From User Given Account Id
     public List<Message> getAllMessagesFromAccountId(int id){
         Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            //Write SQL logic here
            String sql = "SELECT * FROM Message WHERE posted_by=?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                 messages.add(new Message(rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch")));             
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }


    
}
