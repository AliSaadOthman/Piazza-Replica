package org.piazza.commands;

import com.google.firebase.messaging.FirebaseMessagingException;
import org.json.simple.parser.ParseException;
import org.piazza.services.NotificationService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class RegisterTokenCommand extends NotificationsCommand{
    @Override
    protected void getRequiredFields() {
        Field userId=new Field("userId");
        Field token = new Field("token");
        requiredFields= new ArrayList<Field>();
        requiredFields.add(userId);
        requiredFields.add(token);
   }
    public HashMap<String, Object> execute() throws Exception {
        String userId= (String) request.get("userId");
        String token = (String) request.get("token");
        return notificationService.registerToken(userId,token);
    }
}
