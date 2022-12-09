package org.piazza.commands;

import com.google.firebase.messaging.FirebaseMessagingException;
import org.json.simple.parser.ParseException;
import org.piazza.services.NotificationService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class SendNotificationSingleTokenCommand extends NotificationsCommand{
    @Override
    protected void getRequiredFields() {
        requiredFields = new ArrayList<Field>();
        Field userId=new Field("userId");
        Field title=new Field("title");
        Field body=new Field("body");
        requiredFields.add(userId);
        requiredFields.add(title);
        requiredFields.add(body);
    }
    @Override
    public HashMap<String, Object> execute() throws Exception {
        String userId = (String) request.get("userId");
        String title = (String) request.get("title");
        String body = (String) request.get("body");
        return notificationService.SendNotificationToken(userId,title,body);
    }
}
