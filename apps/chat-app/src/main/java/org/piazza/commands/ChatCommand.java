package org.piazza.commands;

import com.google.firebase.messaging.FirebaseMessagingException;
import org.json.simple.parser.ParseException;
import org.piazza.services.ChatService;
import org.piazza.utils.Command;
import org.piazza.utils.Receiver;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public abstract class ChatCommand implements Command {
    protected HttpServletResponse response;

    protected ChatService chatService;

    protected HashMap<String,Object> request;
    protected ArrayList<Field> requiredFields;
    protected ArrayList<String> missingFields;

    protected abstract void getRequiredFields();
    public void init(Receiver receiver) {
        chatService = (ChatService) receiver;
    }
    public abstract HashMap<String, Object> execute() throws IOException, Exception;

    public HashMap<String, Object> execute(HashMap<String,Object> request) throws Exception{
        this.request=request;
        HashMap<String,Object> response=new HashMap<>();
        if(!checkRequiredFields()){
            response.put("message","Missing Fields "+missingFields.toString());
            response.put("status",400);
            return response;
        }

        return execute();

    }





    public boolean checkRequiredFields(){
        getRequiredFields();
        missingFields=new ArrayList<String>();
        for(Field field:requiredFields){
            if(!request.containsKey(field.getName())){
                missingFields.add(field.getName());
            }
        }
        return missingFields.isEmpty();
    }
}
