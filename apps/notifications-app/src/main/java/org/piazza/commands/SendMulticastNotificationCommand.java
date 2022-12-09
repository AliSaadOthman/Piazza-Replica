package org.piazza.commands;

import com.google.firebase.messaging.FirebaseMessagingException;
import org.json.simple.parser.ParseException;
import org.piazza.services.NotificationService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class SendMulticastNotificationCommand extends  NotificationsCommand{
    @Override
    protected void getRequiredFields() {
        Field userIds=new Field("userIds");
        Field title = new Field("title");
        Field body = new Field("body");
        requiredFields= new ArrayList<Field>();
        requiredFields.add(userIds);
        requiredFields.add(title);
        requiredFields.add(body);
    }
    @Override
    public HashMap<String, Object> execute() throws ParseException, FirebaseMessagingException, IOException {
        String[] userIds = (String[]) request.get("userIds");
        String title= (String) request.get("title");
        String body = (String) request.get("body");
        return notificationService.sendMulticastNotification(userIds,title,body);
    }
}
