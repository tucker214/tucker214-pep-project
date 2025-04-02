package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;
import Service.AccountService;
import Service.MessageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import Model.Account;
import Model.Message;
import java.util.List;
import java.util.ArrayList;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {

    AccountService accountService;
    MessageService messageService;

    public SocialMediaController()
    {
        accountService = new AccountService();
        messageService = new MessageService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);
        app.post("/register", this::postAccountHandler);
        app.post("/login", this::loginHandler);
        app.post("/messages", this::postMessageHandler);
        app.get("/messages", this::getMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessageByIdHandler);
        app.patch("/messages/{message_id}", this::updateMessageByIdHandler);
        app.get("/accounts/{account_id}/messages", this::getAllMessagesByUserId);
        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    private void postAccountHandler(Context context) throws JsonProcessingException {
        
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        Account registeredAccount = accountService.addAccount(account);

        if (registeredAccount == null)
        {
            context.status(400);
        }
        else if (registeredAccount.getPassword().length() < 4 || registeredAccount.getUsername().isEmpty())
        {
            context.status(400);
        }
        else 
            {
                context.status(200);
                context.json(mapper.writeValueAsString(registeredAccount));
            }
    
    }

    private void loginHandler(Context context) throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapper();

        Account account = mapper.readValue(context.body(), Account.class);
        Account isloggedInAccount = accountService.loginAccount(account.username, account.password);

        if (isloggedInAccount != null)
        {
            context.status(200);
            context.json(mapper.writeValueAsString(isloggedInAccount));
        }
        else
            {
                context.status(401);
            }
    }

    private void postMessageHandler(Context context) throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapper();

        Message message = mapper.readValue(context.body(), Message.class);
        Message createdMessage = null;
        if (!message.getMessage_text().isEmpty())
            createdMessage = messageService.createMessage(message);

        if (createdMessage != null)
        {
            if (createdMessage.message_text.length() <= 255 && createdMessage.message_text.length() > 0 && createdMessage.getPosted_by() > 0)
            {
                context.status(200);
                context.json(mapper.writeValueAsString(createdMessage));
            }

        }

        else
            {
                context.status(400);
            }
    }

    public void getMessagesHandler(Context context) throws JsonProcessingException
    {
        context.status(200);
        context.json(messageService.getAllMessages());
    }

    public void getMessageByIdHandler(Context context) throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapper();
        //Message message = mapper.readValue(context.body(), Message.class);
        int message_id = Integer.parseInt(context.pathParam("message_id"));
        context.status(200);
        Message message = messageService.getMessageById(message_id);
        if (message != null)
            context.json(mapper.writeValueAsString(message));

    }

    public void deleteMessageByIdHandler(Context context) throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapper();
        int message_id = Integer.parseInt(context.pathParam("message_id"));
        Message message = messageService.getMessageById(message_id);
        messageService.deleteMessageById(message_id);
        if (message != null)
            if (!message.getMessage_text().isEmpty())
                context.json(mapper.writeValueAsString(message));

    }

    public void updateMessageByIdHandler(Context context) throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapper();
        int message_id = Integer.parseInt(context.pathParam("message_id"));
        Message new_message = mapper.readValue(context.body(), Message.class);
        messageService.updateMessageById(message_id, new_message.message_text);
        Message updatedMessage = messageService.getMessageById(message_id);
       
        if (updatedMessage == null)
            context.status(400);
        else if (new_message.message_text.isEmpty())
            context.status(400);
        else if (new_message.message_text.length() > 255)
            context.status(400);
        else
        {
            context.status(200);
            context.json(mapper.writeValueAsString(updatedMessage));
        }
    }

    public void getAllMessagesByUserId(Context context) throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapper();
        int account_id = Integer.parseInt(context.pathParam("account_id"));
        List<Message> message_list = new ArrayList<>();
        message_list = messageService.getAllMessagesByUserId(account_id);
        context.status(200);
        context.json(mapper.writeValueAsString(message_list));

    }
}