package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;
import Service.AccountService;
import Service.MessageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import Model.Account;
import Model.Message;

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
        if (registeredAccount == null || registeredAccount.getUsername().length() < 4 || registeredAccount.getPassword().isEmpty())
        {
            context.status(400);
        }
        else if (accountService.getAccountUsernameById(account.getAccount_id()) != null)
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


}