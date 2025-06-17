package Controller;
import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;



/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController(){
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::postNewAccountHandler);
        app.post("/login", this::postLoginHandler);
        app.post("/messages",this::postNewMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageHandler);
        app.delete("/messages/{message_id}", this::deleteMessageHandler);
        app.patch("/messages/{message_id}", this::patchMessageHandler);
        app.get("/accounts/{account_id}/messages", this::getAllMessageFromAccountHandler);
       
        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void postNewAccountHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
          Account addedAccount = accountService.addAccount(account);
            if(addedAccount!=null){
                ctx.json(mapper.writeValueAsString(addedAccount)).status(200);
            }else{
                ctx.status(400);
            }
        }

    


    private void postLoginHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account logInAccount = accountService.login(account);
        if(logInAccount!=null){
            ctx.json(mapper.writeValueAsString(logInAccount));
            ctx.status(200);
             
        }else{
            ctx.status(401);
        }

    }


    private void postNewMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
       Message message = mapper.readValue(ctx.body(), Message.class);
        Message addedMessage = messageService.addMessage(message);
        if(addedMessage!=null){
            ctx.json(mapper.writeValueAsString(addedMessage));
            ctx.status(200);  
        }else{
            ctx.status(400);
        }
    }

    

    private void getAllMessagesHandler(Context ctx) {
        List<Message> messages = messageService.getAllMessages();
        ctx.json(messages).status(200);
    }

    private void getMessageHandler(Context ctx) throws JsonProcessingException {
       // ObjectMapper mapper = new ObjectMapper();
       //Message message = mapper.readValue(ctx.body(), Message.class);
       int messageId=Integer.parseInt(ctx.pathParam("message_id"));
        Message addedMessage = messageService.getMessageById(messageId);
      if(addedMessage!=null){

          ctx.json(addedMessage).status(200);
      }else{
        ctx.status(200);
      }
            
       

    }

     private void deleteMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
      // Message message = mapper.readValue(ctx.body(), Message.class);
        int messageId=Integer.parseInt(ctx.pathParam("message_id"));
        Message deletedMessage = messageService.deleteMessageById(messageId);
        if(deletedMessage!=null){
            ctx.json(mapper.writeValueAsString(deletedMessage));
            ctx.status(200);
        }else{
            ctx.status(200);
        }

       }

    
     private void patchMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
           int messageId=Integer.parseInt(ctx.pathParam("message_id"));
           
            Message addedMessage = messageService.UpdateMessageById(messageId,message.getMessage_text());
            if(addedMessage!=null){
                ctx.json(mapper.writeValueAsString(addedMessage));
                ctx.status(200);
            }else{
                ctx.status(400);
            }
        }

    


    private void getAllMessageFromAccountHandler(Context ctx) throws JsonProcessingException {
       List<Message> messages = messageService.getAllMessagesFromAccountId(Integer.parseInt(ctx.pathParam("account_id")));
        ctx.json(messages).status(200);   

    }



}